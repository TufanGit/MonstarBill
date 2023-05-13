package com.monster.bill.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.google.common.base.CharMatcher;
import com.monster.bill.common.CommonUtils;
import com.monster.bill.common.CustomException;
import com.monster.bill.models.Employee;
import com.monster.bill.models.Invoice;
import com.monster.bill.models.InvoiceItem;
import com.monster.bill.models.Item;
import com.monster.bill.models.Mail;
import com.monster.bill.models.PurchaseOrder;
import com.monster.bill.models.Subsidiary;
import com.monster.bill.models.Template;
import com.monster.bill.models.TemplateSupplier;
import com.monster.bill.repository.EmployeeRepository;
import com.monster.bill.repository.ItemRepository;
import com.monster.bill.repository.MailRepository;
import com.monster.bill.repository.PurchaseOrderRepository;
import com.monster.bill.repository.SubsidiaryRepository;
import com.monster.bill.repository.TemplateSupplierRepository;
import com.monster.bill.service.MailService;

import lombok.extern.slf4j.Slf4j;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
	
	@Autowired
    private MailRepository mailRepository;
	
	@Autowired
    private TemplateSupplierRepository templateSupplierRepository;
	
	@Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
    private ItemRepository itemRepository;
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	@Autowired
    private SubsidiaryRepository subsidiaryRepository;
	
	private Session session;
	
	private AmazonS3 s3client;

	public MailServiceImpl() {
		
		Properties props = new Properties();
		try {
			File file = ResourceUtils.getFile("classpath:smtp.properties");
			props.load(new FileInputStream(file));
			session = Session.getDefaultInstance(props, null);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		AWSCredentials credentials = new BasicAWSCredentials(
				  "AKIA53OXLQDS4EUHC3OI", 
				  "9tbP+twU+F97tTibDMMHEbWuukS//UlaUzVEzT6D"
				);
		s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.AP_SOUTH_1)
				  .build();
		
	}
	/**
	 * Get Mails by logged in User
	 * @return Mails
	 */
	@Override
	public List<Mail> getMails(Long employeeId) {
		List<Mail> mails; String invoiceMail = null;
		log.info("Get Mails started.");
		Optional<Employee> empOptional = employeeRepository.findByIdAndIsDeleted(employeeId, false);
		if(empOptional.isPresent())	{
			Employee employee = empOptional.get();
			Long subsidiaryId = employee.getSubsidiaryId();
			Optional<Subsidiary> subOptional = subsidiaryRepository.findByIdAndIsDeleted(subsidiaryId, false);
			if(subOptional.isPresent())	{
				Subsidiary subsidiary = subOptional.get();
				invoiceMail = subsidiary.getInvoiceMail();
			}
		}
		mails = mailRepository.findByToId(invoiceMail);
		log.info("Get Mails: " + mails);
		return mails;
	}
	
	@Override
	public Mail getAttachment(Long mailId) {
		Mail attachment = null;
		log.info("Get Attachment File started.");
		Optional<Mail> mailOptional = mailRepository.findById(mailId);
		if(mailOptional.isPresent())	{
			attachment = mailOptional.get();	
			String timestamp = String.valueOf(attachment.getReceiveDate().getTime());
	       	S3Object s3object = s3client.getObject("mbl-dev-doc", "Attachments/T"+timestamp+"_"+attachment.getAttachName());
	       	S3ObjectInputStream inputStream = s3object.getObjectContent();
	        attachment.setAttachment(inputStream);
	        log.info("Get Attachment File : "+attachment);
		}
		return attachment;
	}
	/**
	 * Import Mails with Attachments from logged in Subsidiary Email
	 * @return Mails
	 */
	@Override
	@Transactional
	public List<Mail> saveMails(Long employeeId) {
		List<Mail> savedMails;
		String invoiceMail = null, invoicePasswd = null;
		log.info("Save Mails started.");
		Optional<Employee> empOptional = employeeRepository.findByIdAndIsDeleted(employeeId, false);
		if(empOptional.isPresent())	{
			Employee employee = empOptional.get();
			Long subsidiaryId = employee.getSubsidiaryId();
			Optional<Subsidiary> subOptional = subsidiaryRepository.findByIdAndIsDeleted(subsidiaryId, false);
			if(subOptional.isPresent())	{
				Subsidiary subsidiary = subOptional.get();
				invoiceMail = subsidiary.getInvoiceMail();
				invoicePasswd = subsidiary.getInvoicePasswd();
			}
		}
		Store store = null;
		Folder inbox = null;
		List<Mail> mails = new ArrayList<Mail>();
		try {
			store = session.getStore("imaps");
			store.connect("smtp.gmail.com", invoiceMail, invoicePasswd);
			inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
			Message[] messages = inbox.getMessages();
			Date maxReceiveDate = mailRepository.getMaxReceiveDate(true, invoiceMail);
	        for (Message message : messages)	{
	        	if(maxReceiveDate == null || message.getReceivedDate().after(maxReceiveDate))	{
		        	Multipart multipart = (Multipart) message.getContent();
					for (int j = 0; j < multipart.getCount(); ++j) {
						BodyPart bodyPart = multipart.getBodyPart(j);
		                if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
		                       !StringUtils.isBlank(bodyPart.getFileName())) {
		                	Tika tika = new Tika();
		                	String mimeType = tika.detect(bodyPart.getInputStream());
		                	if(mimeType.equals("application/pdf") || mimeType.equals("image/jpeg") || mimeType.equals("image/png"))	{
					        	String email = ((InternetAddress) message.getFrom()[0]).getAddress();
					        	Mail mail = new Mail();
					        	mail.setMail(true);
					        	mail.setFromId(email); mail.setToId(invoiceMail);
					        	mail.setSubjectLine(message.getSubject());
					        	mail.setReceiveDate(message.getReceivedDate());
					        	mail.setAttachName(bodyPart.getFileName());
					        	mail.setAttachType(mimeType.split("/")[1]);
					        	mail.setCreatedBy(CommonUtils.getLoggedInUsername());
					        	mail.setModifiedBy(CommonUtils.getLoggedInUsername());
					        	mails.add(mail);
					        	
					        	InputStream is = bodyPart.getInputStream();
			                	Long contentLength = Long.valueOf(IOUtils.toByteArray(is).length);
			                	 
			                	ObjectMetadata metadata = new ObjectMetadata();
			                	metadata.setContentLength(contentLength);
			                	String timestamp = String.valueOf(message.getReceivedDate().getTime());
			                    s3client.putObject(
			                			  "mbl-dev-doc", 
			                			  "Attachments/T"+timestamp+"_"+bodyPart.getFileName(), 
			                			  bodyPart.getInputStream(), metadata
			                			);
					        	break; // dealing with attachments only
		                	}
		                }
					}
	        	}
	        }
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(inbox!=null)
					inbox.close();
				if(store!=null)
					store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		try {
			savedMails = mailRepository.saveAll(mails);
		}catch (DataAccessException e) {
			log.error("Error while saving the Mails :: "+ e.getMostSpecificCause());
			throw new CustomException("Error while saving the Mails: " + e.getMostSpecificCause());
		}
		log.info("Saved Mails: " + savedMails);
		return savedMails;
	}

	@Override
	public Invoice getInvoiceData(Long mailId) {
		log.info("Get Invoice Data started.");
		Invoice invoice = new Invoice();
		Mail attachment = mailRepository.findById(mailId).get();
		Optional<TemplateSupplier> optTemplateSupplier = templateSupplierRepository.findBySupplierEmailAndIsDeleted(attachment.getFromId(), false);
		if(!optTemplateSupplier.isPresent())
			return invoice;
		TemplateSupplier templateSupplier = optTemplateSupplier.get();
		invoice.setSupplierId(templateSupplier.getSupplierId());
		String timestamp = String.valueOf(attachment.getReceiveDate().getTime());
       	S3Object s3object = s3client.getObject("mbl-dev-doc", "Attachments/T"+timestamp+"_"+attachment.getAttachName());
       	S3ObjectInputStream inputStream = s3object.getObjectContent();
       	Template template = templateSupplier.getTemplate();
       	invoice.setSubsidiaryId(template.getSubsidiaryId());
       	PDDocument document = null;
		try {
			document = PDDocument.load(inputStream);
			PDFTextStripper pdfStripper = new PDFTextStripper();
	  	  	String text = pdfStripper.getText(document);
	  	 // System.out.println(text);
	  	  	String regex = "\\W+([^\\s]*)";
	  	  	Pattern p;
	  	    Matcher m;
	  	  	Optional<String> optInvoiceDate = Optional.ofNullable(template.getInvoiceDate());
	  	  	if(optInvoiceDate.isPresent())	{
		  	  	p = Pattern.compile(optInvoiceDate.get()+regex);
		  	    m = p.matcher(text);       
		  	    String invDateStr = m.find()? m.group(1):null;
		  	  SimpleDateFormat sdf = new SimpleDateFormat(invDateStr.contains("-")? "dd-MMM-yyyy" : "dd.MM.yyyy");
		  	 //   sdf.setLenient(false);
		  	    invoice.setInvoiceDate(new java.sql.Date(sdf.parse(invDateStr).getTime()));
	  	  	}
	  	  	Optional<String> optInvoiceNo = Optional.ofNullable(template.getInvoiceNo());
	  	  	if(optInvoiceNo.isPresent())	{
		  	    p = Pattern.compile(optInvoiceNo.get()+regex);
		  	    m = p.matcher(text);
		  	    String invoiceNo = m.find()? m.group(1):null;
		  	    invoice.setInvoiceNo(invoiceNo);
	  	  	}
	  	  	boolean isPo = false;
	  	  	Optional<String> optPoNo = Optional.ofNullable(template.getPoNo());
	  	  	if(optPoNo.isPresent())	{
		  	    p = Pattern.compile(optPoNo.get()+regex);
		  	    m = p.matcher(text);
		  	    String poNo = m.find()? m.group(1):null;
		  	    if(poNo!=null && !poNo.isEmpty())	{
		  	    	Optional<PurchaseOrder> optPo = purchaseOrderRepository.findByPoNumber(poNo);
		  	    	if(optPo.isPresent())	{
			  	    	Long poId = optPo.get().getId();
			  	    	invoice.setPoId(poId);
			  	    	isPo = true;
		  	    	}
		  	    }
		  	  //  invoice.setPoNo(poNo);
	  	  	}
	  	    if(!isPo)	{
		  	    Optional<String> optDesc = Optional.ofNullable(template.getDescription());
		  	    Optional<String> optItem = Optional.ofNullable(template.getItem());
		  	    Optional<String> optUom = Optional.ofNullable(template.getUom());
		  	    Optional<String> optUnitPrice = Optional.ofNullable(template.getUnitPrice());
		  	    Optional<String> optDiscount = Optional.ofNullable(template.getDiscount());
		  	    Optional<String> optNetAmount = Optional.ofNullable(template.getNetAmount());
		  	    Optional<String> optBillQty = Optional.ofNullable(template.getBillQty());
		  	 	ObjectExtractor oe = new ObjectExtractor(document);
				SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm(); // Tabula algo.
				Page page = oe.extract(1); // extract only the first page
				List<Table> tables = sea.extract(page);
				for(Table table : tables) {
					List<List<RectangularTextContainer>> rows = table.getRows();
					int kDesc=-1, kItem=-1, kSlno=-1, kUom=-1, kUnitPrice=-1, kDiscount=-1, kNetAmount=-1, kBillQty=-1, k=-1;
					for(int i=0; i<rows.size(); i++)	{
						List<RectangularTextContainer> hdrCells = rows.get(i);
						for(int j=0; j<hdrCells.size(); j++) {
							String cellText = hdrCells.get(j).getText().trim().replaceAll("\\s+", " ");
							if(cellText.equals(template.getSlNo()))	{
								kSlno=j; k=i;
							} else if(optDesc.isPresent() && cellText.equals(optDesc.get()))	{
								kDesc=j;
							} else if(optItem.isPresent() && optItem.get().equals(cellText)) {
								kItem=j;
							} else if(optUom.isPresent() && optUom.get().equals(cellText)) {
								kUom=j;
							} else if(optUnitPrice.isPresent() && optUnitPrice.get().equals(cellText)) {
								kUnitPrice=j;
							} else if(optDiscount.isPresent() && optDiscount.get().equals(cellText)) {
								kDiscount=j;
							} else if(optNetAmount.isPresent() && optNetAmount.get().equals(cellText)) {
								kNetAmount=j;
							} else if(optBillQty.isPresent() && optBillQty.get().equals(cellText)) {
								kBillQty=j;
							}
						}
						if(kSlno!=-1)
							break;
					}
					if(kSlno==-1)
						continue;
					ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
					int slNo=1;
					for(int i=k+1; i<rows.size(); i++) {
						List<RectangularTextContainer> cells = rows.get(i);
						Optional<String> optSlData = Optional.ofNullable(cells.get(kSlno).getText());
				log.info("Sl No: "+optSlData.get());
						if(!optSlData.isPresent() || !optSlData.get().trim().equals(String.valueOf(slNo)))
							continue;
						++slNo;
						InvoiceItem invoiceItem = new InvoiceItem();
						if(kDesc!=-1)	{
							String description = cells.get(kDesc).getText();
							invoiceItem.setDescription(description);
							Optional<Item> optItemObj = itemRepository.findByDescriptionAndIsDeleted(description, false);
							if(optItemObj.isPresent())
								invoiceItem.setItemId(optItemObj.get().getId());
						}
						if(kItem!=-1)	{
							String itemName = cells.get(kItem).getText();
							invoiceItem.setItemName(itemName);
							Optional<Item> optItemObj = itemRepository.findByNameAndIsDeleted(itemName, false);
							if(optItemObj.isPresent())
								invoiceItem.setItemId(optItemObj.get().getId());
						}
						if(kUom!=-1)
							invoiceItem.setUom(cells.get(kUom).getText());
						if(kUnitPrice!=-1)	{
							String unitPriceStr = cells.get(kUnitPrice).getText();
							String unitPrice = CharMatcher.is('.').or(CharMatcher.inRange('0', '9')).retainFrom(unitPriceStr.split("\\s+")[0]);
				log.info("Rate: "+ unitPrice);
							invoiceItem.setRate(Double.parseDouble(unitPrice));
						}
						if(kDiscount!=-1)
							invoiceItem.setDiscount(cells.get(kDiscount).getText());
						if(kNetAmount!=-1)
							invoiceItem.setNetAmount(cells.get(kNetAmount).getText());
						if(kBillQty!=-1)
							invoiceItem.setBillQty(Double.parseDouble(cells.get(kBillQty).getText()));
						invoiceItems.add(invoiceItem);
					}
					invoice.setInvoiceItems(invoiceItems);
					break;
				}
	  	    }
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} finally {
			if(document!=null)
				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		log.info("Get Invoice Data: " + invoice);
		return invoice;
	}

	@Override
	@Transactional
	public Mail saveFile(MultipartFile file, Long employeeId) {
		Mail savedMail = null;
		String invoiceMail = null;
		Optional<Employee> empOptional = employeeRepository.findByIdAndIsDeleted(employeeId, false);
		if(empOptional.isPresent())	{
			Employee employee = empOptional.get();
			Long subsidiaryId = employee.getSubsidiaryId();
			Optional<Subsidiary> subOptional = subsidiaryRepository.findByIdAndIsDeleted(subsidiaryId, false);
			if(subOptional.isPresent())	{
				Subsidiary subsidiary = subOptional.get();
				invoiceMail = subsidiary.getInvoiceMail();
			}
		}
		Tika tika = new Tika();
    	String mimeType = null;
		try {
			mimeType = tika.detect(file.getInputStream());
			if(mimeType!=null && (mimeType.equals("application/pdf") || mimeType.equals("image/jpeg") || mimeType.equals("image/png")))	{
	        	Mail mail = new Mail();
	        	mail.setMail(false); mail.setToId(invoiceMail);
	        	mail.setSubjectLine(file.getOriginalFilename());
	        	mail.setReceiveDate(new Date());
	        	mail.setAttachName(file.getOriginalFilename());
	        	mail.setAttachType(mimeType.split("/")[1]);
	        	mail.setCreatedBy(CommonUtils.getLoggedInUsername());
	        	mail.setModifiedBy(CommonUtils.getLoggedInUsername());
	        	log.info("Save File started.");
	    		savedMail = mailRepository.save(mail);
	    		InputStream is = file.getInputStream();
	        	Long contentLength = Long.valueOf(IOUtils.toByteArray(is).length);        	 
	        	ObjectMetadata metadata = new ObjectMetadata();
	        	metadata.setContentLength(contentLength);
	        	String timestamp = String.valueOf(mail.getReceiveDate().getTime());
	            s3client.putObject(
	        			  "mbl-dev-doc", 
	        			  "Attachments/T"+timestamp+"_"+file.getOriginalFilename(), 
	        			  file.getInputStream(), metadata
	        			);
	            log.info("Saved File: " + savedMail);
	    	}
		} catch (DataAccessException | SdkClientException | IOException e) {
			log.error("Error while saving the File :: "+ e.getCause());
			throw new CustomException("Error while saving the File: " + e.getCause());
		}
		return savedMail;
	}

}
