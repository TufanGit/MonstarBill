package com.monster.bill.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.monster.bill.common.AppConstants;
import com.monster.bill.common.CommonUtils;
import com.monster.bill.common.ComponentUtility;
import com.monster.bill.common.CustomException;
import com.monster.bill.common.CustomMessageException;
import com.monster.bill.common.FilterNames;
import com.monster.bill.enums.FormNames;
import com.monster.bill.enums.Operation;
import com.monster.bill.enums.TransactionStatus;
import com.monster.bill.models.ApprovalPreference;
import com.monster.bill.models.GrnItem;
import com.monster.bill.models.Invoice;
import com.monster.bill.models.InvoiceHistory;
import com.monster.bill.models.InvoiceItem;
import com.monster.bill.models.InvoicePayment;
import com.monster.bill.models.Subsidiary;
import com.monster.bill.models.Supplier;
import com.monster.bill.payload.request.ApprovalRequest;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.repository.GrnItemRepository;
import com.monster.bill.repository.InvoiceHistoryRepository;
import com.monster.bill.repository.InvoiceItemRepository;
import com.monster.bill.repository.InvoicePaymentRepository;
import com.monster.bill.repository.InvoiceRepository;
import com.monster.bill.repository.SubsidiaryRepository;
import com.monster.bill.repository.SupplierRepository;
import com.monster.bill.service.ApprovalPreferenceService;
import com.monster.bill.service.DocumentSequenceService;
import com.monster.bill.service.InvoiceService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
    private InvoiceRepository invoiceRepository;
	
	@Autowired
    private InvoiceItemRepository invoiceItemRepository;
	
	@Autowired
    private InvoiceHistoryRepository invoiceHistoryRepository;
	
	@Autowired
	private DocumentSequenceService documentSequenceService;
	
	@Autowired
	private SubsidiaryRepository subsidiaryRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private InvoicePaymentRepository invoicePaymentRepository;
	
	@Autowired
	private ComponentUtility componentUtility;
	
	@Autowired
	private ApprovalPreferenceService approvalPreferenceService;
	
	@Autowired
	private GrnItemRepository grnItemRepository;

	@Override
	@Transactional
	public Invoice saveInvoice(Invoice invoice) {
		
		Invoice invoiceSaved;
		Optional<Invoice> oldInvoice = Optional.empty();
		Long invoiceId = invoice.getInvoiceId();
		if(invoiceId==null)	{
			invoice.setTotalPaidAmount(0.0);
			invoice.setAmountDue(invoice.getTotalAmount());
			invoice.setCreatedBy(CommonUtils.getLoggedInUsername());
			invoice.setInvStatus(TransactionStatus.OPEN.getTransactionStatus());
			String transactionalDate = CommonUtils.convertDateToFormattedString(invoice.getInvoiceDate());
			String documentSequenceNumber = this.documentSequenceService.getDocumentSequenceNumbers(transactionalDate, invoice.getSubsidiaryId(), AppConstants.INVOICE, false);
			if (StringUtils.isEmpty(documentSequenceNumber)) {
				throw new CustomMessageException("Please validate your configuration to generate the Invoice Number");
			}
			invoice.setInvoiceCode(documentSequenceNumber);
		}
		else	{
			oldInvoice = invoiceRepository.findById(invoiceId);
			// Get the existing object using the deep copy
			if (oldInvoice.isPresent()) {
				try {
					oldInvoice = Optional.ofNullable((Invoice) oldInvoice.get().clone());
				} catch (CloneNotSupportedException e) {
					log.error("Error while Cloning the object. Please contact administrator.");
					throw new CustomException("Error while Cloning the object. Please contact administrator.");
				}
			}
		}
		if (invoiceId != null) {
			Optional<Invoice> invoiceExistValue = Optional.empty();
			if(invoiceExistValue.isPresent()) {
			invoiceExistValue = this.invoiceRepository.getByInvoiceId(invoiceId);
			invoiceExistValue.get().getAmountDue();
			invoice.setAmountDue(invoiceExistValue.get().getAmountDue());
			}}
		
		invoice.setLastModifiedBy(CommonUtils.getLoggedInUsername());
		log.info("Save invoice started.");
		try {
			invoiceSaved = invoiceRepository.save(invoice);
		}catch (DataAccessException e) {
			log.error("Error while saving the Invoice :: "+ e.getMostSpecificCause());
			throw new CustomException("Error while saving the Invoice: " + e.getMostSpecificCause());
		}
		log.info("Saved invoice: " + invoiceSaved);
		updateInvoiceHistory(invoiceSaved, oldInvoice);
		log.info("Invoice Items are started.");
		List<InvoiceItem> invoiceItems=invoice.getInvoiceItems();
		if (CollectionUtils.isNotEmpty(invoiceItems))	{
			List<InvoiceItem> invoiceItemsSaved = new ArrayList<>();
			invoiceItems.forEach(invoiceItem -> {
				invoiceItem.setInvoiceId(invoiceSaved.getInvoiceId());
				InvoiceItem invoiceItemSaved = saveInvoiceItem(invoiceItem);
				invoiceItemsSaved.add(invoiceItemSaved);
			});
			invoiceSaved.setInvoiceItems(invoiceItemsSaved);
			
		}
		log.info("Invoice Items are Finished.");
		return invoiceSaved;
	}
	
	@Transactional
	private InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem) {
		InvoiceItem invoiceItemSaved = null;
		Optional<InvoiceItem> oldInvoiceItem = Optional.empty();
		Long invoiceItemId = invoiceItem.getInvoiceItemId();
		if(invoiceItemId==null)
			invoiceItem.setCreatedBy(CommonUtils.getLoggedInUsername());
		else {
			// Get the existing object using the deep copy
			oldInvoiceItem = invoiceItemRepository.findById(invoiceItemId);
			if (oldInvoiceItem.isPresent()) {
				try {
					oldInvoiceItem = Optional.ofNullable((InvoiceItem) oldInvoiceItem.get().clone());
				} catch (CloneNotSupportedException e) {
					log.error("Error while Cloning the object. Please contact administrator.");
					throw new CustomException("Error while Cloning the object. Please contact administrator.");
				}
			}
		}
		List<GrnItem> grnItems = new ArrayList<>();
		grnItems = this.grnItemRepository.getByGrnIdAndItemId(invoiceItem.getGrnId(), invoiceItem.getItemId());
		for (GrnItem grnItem : grnItems) {
			if((grnItem.getGrnId()==invoiceItem.getGrnId()) && (grnItem.getItemId()==invoiceItem.getItemId())) {
				grnItem.setInvoiceId(invoiceItem.getInvoiceId());
				this.grnItemRepository.save(grnItem);
			}
		}
		invoiceItem.setLastModifiedBy(CommonUtils.getLoggedInUsername());
		log.info("Save invoice Item started.");
		try {
			invoiceItemSaved = invoiceItemRepository.save(invoiceItem);
		}catch (DataAccessException e) {
			log.error("Error while saving the Invoice Item :: "+ e.getMostSpecificCause());
			throw new CustomException("Error while saving the Invoice Item: " + e.getMostSpecificCause());
		}
		log.info("Saved invoice Item: " + invoiceItemSaved);
		updateInvoiceItemHistory(invoiceItemSaved, oldInvoiceItem);
		return invoiceItemSaved;
	}
	
	/**
	 * This method save the data in history table
	 * Add entry as a Insert if Invoice is new 
	 * Add entry as a Update if Invoice is exists
	 * @param invoice
	 * @param oldInvoice
	 */
	@Transactional
	private void updateInvoiceHistory(Invoice invoice, Optional<Invoice> oldInvoice) {
		log.info("Invoice History is started.");
		if (oldInvoice.isPresent()) {
			// insert the updated fields in history table
			List<InvoiceHistory> invoiceHistories = new ArrayList<>();
			try {
				invoiceHistories = oldInvoice.get().compareFields(invoice);
				if (CollectionUtils.isNotEmpty(invoiceHistories)) {
					this.invoiceHistoryRepository.saveAll(invoiceHistories);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.error("Error while comparing the new and old objects. Please contact administrator.");
				throw new CustomException("Error while comparing the new and old objects. Please contact administrator.");
			}
			log.info("Invoice History is updated successfully");
		} else {
			// Insert in history table as Operation - INSERT 
			this.invoiceHistoryRepository.save(this.prepareInvoiceHistory(invoice.getInvoiceId(), null, AppConstants.INVOICE, 
					Operation.CREATE.toString(), invoice.getLastModifiedBy(), null, invoice.getInvoiceNo()));
		}
		log.info("Invoice History is Completed.");
	}
	@Transactional
	private void updateInvoiceItemHistory(InvoiceItem invoiceItem, Optional<InvoiceItem> oldInvoiceItem) {
		log.info("Invoice Item History is started.");
		if (oldInvoiceItem.isPresent()) {
			// insert the updated fields in history table
			List<InvoiceHistory> invoiceHistories = new ArrayList<>();
			try {
				invoiceHistories = oldInvoiceItem.get().compareFields(invoiceItem);
				if (CollectionUtils.isNotEmpty(invoiceHistories)) {
					this.invoiceHistoryRepository.saveAll(invoiceHistories);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.error("Error while comparing the new and old objects. Please contact administrator.");
				throw new CustomException("Error while comparing the new and old objects. Please contact administrator.");
			}
			log.info("Invoice Item History is updated successfully");
		} else {
			// Insert in history table as Operation - INSERT 
			this.invoiceHistoryRepository.save(this.prepareInvoiceHistory(invoiceItem.getInvoiceId(), invoiceItem.getInvoiceItemId(),
					AppConstants.INVOICE_ITEM, Operation.CREATE.toString(), invoiceItem.getLastModifiedBy(), null, String.valueOf(invoiceItem.getInvoiceItemId())));
		}
		log.info("Invoice Item History is Completed.");
	}
	
	/**
	 * Prepares the Invoice history object
	 * @param invoiceId
	 * @param childId
	 * @param moduleName
	 * @param operation
	 * @param lastModifiedBy
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public InvoiceHistory prepareInvoiceHistory(Long invoiceId, Long childId, String moduleName, String operation, String lastModifiedBy, String oldValue, String newValue) {
		InvoiceHistory invoiceHistory = new InvoiceHistory();
		invoiceHistory.setInvoiceId(invoiceId);
		invoiceHistory.setChildId(childId);
		invoiceHistory.setModuleName(moduleName);
		invoiceHistory.setChangeType(AppConstants.UI);
		invoiceHistory.setOperation(operation);
		invoiceHistory.setOldValue(oldValue);
		invoiceHistory.setNewValue(newValue);
		invoiceHistory.setLastModifiedBy(lastModifiedBy);
		return invoiceHistory;
	}

	@Override
	public PaginationResponse getInvoices(PaginationRequest paginationRequest) {
		List<Invoice> invoices = null;
		log.info("Get all invoices started.");
		Specification<Invoice> specification = new Specification<Invoice>() {
			@Override
			public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Long subsidiaryId = null, supplierId = null;
				String fromDate = null;
				String toDate = null;
				Map<String, ?> filters = paginationRequest.getFilters();			
				if (filters.containsKey(FilterNames.SUBSIDIARY_ID))
					subsidiaryId = ((Number) filters.get(FilterNames.SUBSIDIARY_ID)).longValue();
				if (filters.containsKey(FilterNames.SUPPLIER_ID))
					supplierId = ((Number) filters.get(FilterNames.SUPPLIER_ID)).longValue();
				if (filters.containsKey(FilterNames.FROM_DATE)) 
					fromDate = (String) filters.get(FilterNames.FROM_DATE);
				if (filters.containsKey(FilterNames.TO_DATE)) 
					toDate = (String) filters.get(FilterNames.TO_DATE);
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (subsidiaryId != null && subsidiaryId != 0)
					predicates.add(criteriaBuilder.equal(root.get("subsidiaryId"), subsidiaryId));
				if (supplierId != null && supplierId != 0)
					predicates.add(criteriaBuilder.equal(root.get("supplierId"), supplierId));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if (fromDate!= null && !fromDate.isEmpty()) {
						predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("invoiceDate"), sdf.parse(fromDate)));
					}
					if (toDate!= null && !toDate.isEmpty()) {
						predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("invoiceDate"), sdf.parse(toDate)));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
			}
		};
		Pageable pageable = null;
		if(paginationRequest.getSortColumn().equals("supplierName") || paginationRequest.getSortColumn().equals("subsidiaryName"))	{
			pageable = PageRequest.of(paginationRequest.getPageNumber(), paginationRequest.getPageSize());
		}
		else	{
			Sort sort = paginationRequest.getSortOrder().equalsIgnoreCase("asc")?
	                Sort.by(paginationRequest.getSortColumn()).ascending(): Sort.by(paginationRequest.getSortColumn()).descending();
			pageable = PageRequest.of(paginationRequest.getPageNumber(), paginationRequest.getPageSize(), sort);
		}
		Page<Invoice> pgInvoices = invoiceRepository.findAll(specification, pageable);
		if(pgInvoices.hasContent())
			invoices = pgInvoices.getContent();
		if(CollectionUtils.isNotEmpty(invoices))
			invoices.forEach(invoice -> {
				Optional<Subsidiary> optSubsidiary = subsidiaryRepository.findByIdAndIsDeleted(invoice.getSubsidiaryId(), false);
				if(optSubsidiary.isPresent())
					invoice.setSubsidiaryName(optSubsidiary.get().getName());
				Optional<Supplier> supOptional = supplierRepository.findByIdAndIsDeleted(invoice.getSupplierId(), false);
				if(supOptional.isPresent())
					invoice.setSupplierName(supOptional.get().getName());
			});
		if(paginationRequest.getSortColumn().equals("supplierName"))	{
			invoices = new ArrayList<Invoice>(invoices);
			Collections.sort(invoices, new Comparator<Invoice>() {

				@Override
				public int compare(Invoice o1, Invoice o2) {
					if(paginationRequest.getSortOrder().equalsIgnoreCase("asc"))
						return o1.getSupplierName().compareTo(o2.getSupplierName());
					else
						return o2.getSupplierName().compareTo(o1.getSupplierName());
				}
			});
		}
		else if(paginationRequest.getSortColumn().equals("subsidiaryName"))	{
			invoices = new ArrayList<Invoice>(invoices);
			Collections.sort(invoices, new Comparator<Invoice>() {

				@Override
				public int compare(Invoice o1, Invoice o2) {
					if(paginationRequest.getSortOrder().equalsIgnoreCase("asc"))
						return o1.getSubsidiaryName().compareTo(o2.getSubsidiaryName());
					else
						return o2.getSubsidiaryName().compareTo(o1.getSubsidiaryName());
				}
			});
		}
		log.info("Get all invoices: " + invoices);
		Long totalRecords = invoiceRepository.count(specification);
		return CommonUtils.setPaginationResponse(paginationRequest.getPageNumber(), paginationRequest.getPageSize(),
				invoices, totalRecords);
	}

	@Override
	public List<Invoice> findBySubsidiaryAndSuppplier(Long subsidiaryId, Long supplierId) {
		List<Invoice> invoices = new ArrayList<Invoice>();
		invoices = this.invoiceRepository
				.getAllInvoiceBySubsidiaryIdAndSupplierId( subsidiaryId, supplierId);
		log.info("Get all invoice by subsidiaryId and supplierId ." + invoices);
		return invoices;
	}

	@Override
	public Invoice getInvoice(Long invoiceId) {
		
		Invoice invoice = null;
		log.info("Get invoice by id started.");
		Optional<Invoice> optInvoice = invoiceRepository.findById(invoiceId);
		if(optInvoice.isPresent())	{
			invoice = optInvoice.get();
			List<InvoiceItem> invoiceItems = invoiceItemRepository.findByInvoiceId(invoice.getInvoiceId());
			List<InvoicePayment> invoicePayment = invoicePaymentRepository.findByInvoiceId(invoice.getInvoiceId());
			invoice.setInvoiceItems(invoiceItems);
			invoice.setInvoicePayments(invoicePayment);
		}
		log.info("Get invoice: " + invoice);
		
		return invoice;
	}

	@Override
	public List<InvoiceHistory> getInvoiceHistory(Long invoiceId, Pageable pageable) {
		List<InvoiceHistory> invoiceHistories = null;
		log.info("Get History by Invoice id started.");		
		Page<InvoiceHistory> pgInvoiceHistories = invoiceHistoryRepository.findByInvoiceId(invoiceId, pageable);
		if(pgInvoiceHistories.hasContent())
			invoiceHistories = pgInvoiceHistories.getContent();
		log.info("Get Invoice History: " + invoiceHistories);
		return invoiceHistories;
	}

	@Override
	public List<InvoicePayment> saveInvoicePayment(List<InvoicePayment> invoicePayments) {
		for (InvoicePayment invoicePayment : invoicePayments) {
			Optional<Invoice> invoice = Optional.empty();
			if (invoicePayment.getInvoicePaymentId() == null) {
				invoicePayment.setCreatedBy(CommonUtils.getLoggedInUsername());
			}
			invoicePayment.setModifiedBy(CommonUtils.getLoggedInUsername());
			invoice = this.invoiceRepository.findByInvoiceId(invoicePayment.getInvoiceId());
			if(!invoice.isPresent()) {
				log.error(" Invoice is not created for id : " + invoicePayment.getInvoiceId());
				throw new CustomException(" Invoice is not created for id : " + invoicePayment.getInvoiceId());
			}
			if(invoice.get().getAmountDue()==0.0) {
				log.error(" There is no due for the invoice id : " + invoicePayment.getInvoiceId());
				throw new CustomException(" There is no due for the invoice id : " + invoicePayment.getInvoiceId());
			}
			Double remainDueAmount = 0.0;
			remainDueAmount = this.invoicePaymentRepository.findTotalAmountByInvoiceIdAndIsDeleted(invoicePayment.getInvoiceId(),false);	
			if(remainDueAmount == null) {
				remainDueAmount = 0.0;
			}
			remainDueAmount= invoicePayment.getAmount() + remainDueAmount;
			log.info("total amount due " + remainDueAmount);
			invoice.get().setTotalPaidAmount(remainDueAmount);
			invoice.get().setAmountDue(invoice.get().getTotalAmount() - invoice.get().getTotalPaidAmount());
			//log.info("Saved invoice payment : " + savedInvoicePayment);
			this.invoiceRepository.save(invoice.get());
			InvoicePayment savedInvoicePayment = this.invoicePaymentRepository.save(invoicePayment);
			log.info("Saved invoice payment : " + savedInvoicePayment);
		}
		return invoicePayments;
	}

	@Override
	public Boolean sendForApproval(Long id) {
		Boolean isSentForApproval = false;

		try {
			/**
			 * Due to single transaction we are getting updated value when we find from repo after the update
			 * hence finding old one first
			 */
			// Get the existing object using the deep copy
			Optional<Invoice> oldInvoice = this.findOldDeepCopiedInvoice(id);

			Optional<Invoice> invoice = Optional.empty();
			invoice = this.findById(id);

			/**
			 * Check routing is active or not
			 */
			boolean isRoutingActive = invoice.get().isApprovalRoutingActive();
			if (!isRoutingActive) {
				log.error("Routing is not active for the Invoice : " + id + ". Please update your configuration. ");
				throw new CustomMessageException("Routing is not active for the Invoice : " + id + ". Please update your configuration. ");
			}
			
			Double transactionalAmount = invoice.get().getTotalAmount();
			log.info("Total estimated transaction amount for Invoice is :: " + transactionalAmount);
			
			// if amount is null then throw error
			if (transactionalAmount == null || transactionalAmount == 0.0) {
				log.error("There is no available Approval Process for this transaction.");
				throw new CustomMessageException("There is no available Approval Process for this transaction.");
			}
			
			ApprovalRequest approvalRequest = new ApprovalRequest();
			approvalRequest.setSubsidiaryId(invoice.get().getSubsidiaryId());
			approvalRequest.setFormName(FormNames.INVOICE.getFormName());
			approvalRequest.setTransactionAmount(transactionalAmount);
			approvalRequest.setLocationId(invoice.get().getLocationId());
			// TODO department
			log.info("Approval object us prepared : " + approvalRequest.toString());

			/**
			 * method will give max level & it's sequence if match otherwise throw error message as no approver process exist
			 * if level or sequence id is null then also throws error message.
			 */
			ApprovalPreference approvalPreference = this.approvalPreferenceService.findApproverMaxLevel(approvalRequest);
			Long sequenceId = approvalPreference.getSequenceId();
			String level = approvalPreference.getLevel();
			Long approverPreferenceId = approvalPreference.getId();
			log.info("Max level & sequence is found :: " + approvalPreference.toString());
			
			invoice.get().setApproverSequenceId(sequenceId);
			invoice.get().setApproverMaxLevel(level);
			invoice.get().setApproverPreferenceId(approverPreferenceId);
			
			String levelToFindRole = "L1";
			if (AppConstants.APPROVAL_TYPE_INDIVIDUAL.equals(approvalPreference.getApprovalType())) {
				levelToFindRole = level;
			}
			approvalRequest = this.approvalPreferenceService.findApproverByLevelAndSequence(approverPreferenceId, levelToFindRole, sequenceId);

			this.updateApproverDetailsInInvoice(invoice, approvalRequest);
			invoice.get().setInvStatus(TransactionStatus.PENDING_APPROVAL.getTransactionStatus());
			log.info("Approver is found and details is updated for Invoice :: " + invoice.get());
			
			this.saveInvoiceForApproval(invoice.get(), oldInvoice);
			log.info("Invoice is saved successfully with Approver details.");

			componentUtility.sendEmailByApproverId(invoice.get().getNextApprover(), FormNames.INVOICE.getFormName());
			
			isSentForApproval = true;
		} catch (Exception e) {
			log.error("Error while sending PR for approval for id - " + id);
			e.printStackTrace();
			throw new CustomMessageException("Error while sending PO for approval for id - " + id + ", Message : " + e.getLocalizedMessage());
		}
		
		return isSentForApproval;
	}
	
	/**
	 * Save Invoice after the approval details change
	 * @param invoice
	 */
	private void saveInvoiceForApproval(Invoice invoice, Optional<Invoice> oldInvoice) {
		invoice.setLastModifiedBy(CommonUtils.getLoggedInUsername());
		invoice = this.invoiceRepository.save(invoice);
		
		if (invoice == null) {
			log.info("Error while saving the Invoice after the Approval.");
			throw new CustomMessageException("Error while saving the Invoice after the Approval.");
		}
		log.info("Invoice saved successfully :: " + invoice.getInvoiceNo());
		
		// update the data in Invoice history table
		this.updateInvoiceHistory(invoice, oldInvoice);
		log.info("Invoice history is updated");		
	}

	/**
	 * Set/Prepares the approver details in the Invoice object
	 * 
	 * @param Invoice
	 * @param approvalRequest
	 */
	private void updateApproverDetailsInInvoice(Optional<Invoice> invoice, ApprovalRequest approvalRequest) {
		invoice.get().setApprovedBy(invoice.get().getNextApprover());
		invoice.get().setNextApprover(approvalRequest.getNextApprover());
		invoice.get().setNextApproverRole(approvalRequest.getNextApproverRole());
		invoice.get().setNextApproverLevel(approvalRequest.getNextApproverLevel());
	}

	private Optional<Invoice> findOldDeepCopiedInvoice(Long id) {
		Optional<Invoice> invoice = this.invoiceRepository.findByInvoiceId(id);
		if (invoice.isPresent()) {
			try {
				invoice = Optional.ofNullable((Invoice) invoice.get().clone());
				log.info("Existing Invoice is copied.");
			} catch (CloneNotSupportedException e) {
				log.error("Error while Cloning the object. Please contact administrator.");
				throw new CustomException("Error while Cloning the object. Please contact administrator.");
			}
		}
		return invoice;
	}
	
	/**
	 * Find PO by it's ID
	 * @param id
	 * @return
	 */
	public Optional<Invoice> findById(Long id) {
		Optional<Invoice> invoice = Optional.empty();
		invoice = this.invoiceRepository.findByInvoiceId(id);

		if (!invoice.isPresent()) {
			log.info("Invoice is not found against the provided Invoice-ID :: " + id);
			throw new CustomMessageException("Invoice is not found against the provided Invoice-ID :: " + id);
		}
		invoice.get().setApprovalRoutingActive(this.findIsApprovalRoutingActive(invoice.get().getSubsidiaryId()));
		log.info("Invoice is found against the Invoice-ID :: " + id);
		return invoice;
	}
	
	private boolean findIsApprovalRoutingActive(Long subsidiaryId) {
		return this.componentUtility.findIsApprovalRoutingActive(subsidiaryId, FormNames.INVOICE.getFormName());
	}

	@Override
	public Boolean approveAllInvoices(List<Long> invoiceIds) {
		Boolean isAllPoApproved = false;
		try {
			for (Long poId : invoiceIds) {
				log.info("Approval Process is started for po-id :: " + poId);

				/**
				 * Due to single transaction we are getting updated value when we find from repo after the update
				 * hence finding old one first
				 */
				// Get the existing object using the deep copy
				Optional<Invoice> oldInvoice = this.findOldDeepCopiedInvoice(poId);

				Optional<Invoice> invoice = Optional.empty();
				invoice = this.findById(poId);

				/**
				 * Check routing is active or not
				 */
				boolean isRoutingActive = invoice.get().isApprovalRoutingActive();
				if (!isRoutingActive) {
					log.error("Routing is not active for the Invoice : " + poId + ". Please update your configuration. ");
					throw new CustomMessageException("Routing is not active for the Invoice : " + poId + ". Please update your configuration. ");
				}
				
				// meta data
				Long approvalPreferenceId = invoice.get().getApproverPreferenceId();
				Long sequenceId = invoice.get().getApproverSequenceId();
				String maxLevel = invoice.get().getApproverMaxLevel();
				
				ApprovalRequest approvalRequest = new ApprovalRequest();
				
				if (!maxLevel.equals(invoice.get().getNextApproverLevel())) {
					Long currentLevelNumber = Long.parseLong(invoice.get().getNextApproverLevel().replaceFirst("L", "")) + 1;
					String currentLevel = "L" + currentLevelNumber;
					approvalRequest = this.approvalPreferenceService.findApproverByLevelAndSequence(approvalPreferenceId, currentLevel, sequenceId);
					invoice.get().setInvStatus(TransactionStatus.PARTIALLY_APPROVED.getTransactionStatus());
				} else {
					invoice.get().setInvStatus(TransactionStatus.APPROVED.getTransactionStatus());
				}
				log.info("Approval Request is found for Invoice :: " + approvalRequest.toString());

				this.updateApproverDetailsInInvoice(invoice, approvalRequest);
				log.info("Approver is found and details is updated :: " + invoice.get());
				
				this.saveInvoiceForApproval(invoice.get(), oldInvoice);
				log.info("Invoice is saved successfully with Approver details.");

				componentUtility.sendEmailByApproverId(invoice.get().getNextApprover(), FormNames.INVOICE.getFormName());
				log.info("Approval Process is Finished for Invoice :: " + invoice.get().getInvoiceNo());
			}
			
			isAllPoApproved = true;
		} catch (Exception e) {
			log.error("Error while approving the Invoice.");
			e.printStackTrace();
			throw new CustomMessageException("Error while approving the Invoice. Message : " + e.getLocalizedMessage());
		}
		return isAllPoApproved;
	}

	@Override
	public byte[] downloadTemplate() {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		try {
			File is = loader.getResource("classpath:/templates/invoice_template.xlsx").getFile();
			return Files.readAllBytes(is.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] upload(MultipartFile file) {
		try {
			return this.importPrsFromExcel(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Something went wrong. Please Contact Administrator. Error : " + e.getLocalizedMessage());
		}
	}

	private byte[] importPrsFromExcel(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}
}
