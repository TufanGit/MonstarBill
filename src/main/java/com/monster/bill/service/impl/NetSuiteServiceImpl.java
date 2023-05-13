/*
 * package com.monster.bill.service.impl;
 * 
 * import static
 * com.netsuite.suitetalk.client.v2022_1.utils.Utils.createRecordRef; import
 * static com.netsuite.webservices.samples.Messages.ERROR_OCCURRED; import
 * static com.netsuite.webservices.samples.Messages.INVALID_WS_URL; import
 * static com.netsuite.webservices.samples.Messages.WRONG_PROPERTIES_FILE;
 * import static com.netsuite.webservices.samples.utils.PrintUtils.printError;
 * 
 * import java.io.ByteArrayInputStream; import java.io.IOException; import
 * java.net.MalformedURLException; import java.rmi.RemoteException; import
 * java.sql.Date; import java.text.ParseException; import
 * java.text.SimpleDateFormat; import java.util.ArrayList; import
 * java.util.List; import java.util.Optional;
 * 
 * import javax.xml.parsers.DocumentBuilder; import
 * javax.xml.parsers.DocumentBuilderFactory; import
 * javax.xml.parsers.ParserConfigurationException;
 * 
 * import org.apache.axis.AxisFault; import
 * org.apache.commons.lang3.StringUtils; import
 * org.apache.commons.lang3.time.DateUtils; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.dao.DataAccessException; import
 * org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional; import
 * org.w3c.dom.DOMException; import org.w3c.dom.Document; import
 * org.w3c.dom.NodeList; import org.xml.sax.SAXException;
 * 
 * import com.monster.bill.common.CommonUtils; import
 * com.monster.bill.common.CustomMessageException; import
 * com.monster.bill.enums.FormNames; import com.monster.bill.models.Account;
 * import com.monster.bill.models.Currency; import
 * com.monster.bill.models.Invoice; import com.monster.bill.models.InvoiceItem;
 * import com.monster.bill.models.InvoicePayment; import
 * com.monster.bill.models.Item; import com.monster.bill.models.Location; import
 * com.monster.bill.models.MakePayment; import
 * com.monster.bill.models.MakePaymentList; import
 * com.monster.bill.models.Subsidiary; import com.monster.bill.models.Supplier;
 * import com.monster.bill.models.SupplierAddress; import
 * com.monster.bill.models.SupplierSubsidiary; import
 * com.monster.bill.models.TaxGroup; import
 * com.monster.bill.repository.AccountRepository; import
 * com.monster.bill.repository.CurrencyRepository; import
 * com.monster.bill.repository.InvoicePaymentRepository; import
 * com.monster.bill.repository.InvoiceRepository; import
 * com.monster.bill.repository.ItemRepository; import
 * com.monster.bill.repository.LocationRepository; import
 * com.monster.bill.repository.MakePaymentListRepository; import
 * com.monster.bill.repository.MakePaymentRepository; import
 * com.monster.bill.repository.SubsidiaryRepository; import
 * com.monster.bill.repository.SupplierAddressRepository; import
 * com.monster.bill.repository.SupplierRepository; import
 * com.monster.bill.repository.SupplierSubsidiaryRepository; import
 * com.monster.bill.repository.TaxGroupRepository; import
 * com.monster.bill.service.DocumentSequenceService; import
 * com.monster.bill.service.NetSuiteService; import
 * com.netsuite.suitetalk.client.v2022_1.WsClient; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.Record; import
 * com.netsuite.suitetalk.proxy.v2022_1.documents.filecabinet.File; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.Vendor; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.VendorAddressbook;
 * import com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.
 * VendorAddressbookList; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.Address; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.FileSearchBasic; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.types.Country; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.BaseRef; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.CustomFieldList; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.CustomFieldRef; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.
 * GetSelectValueFieldDescription; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.LongCustomFieldRef; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.RecordRef; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.SearchMultiSelectField;
 * import com.netsuite.suitetalk.proxy.v2022_1.platform.core.SearchResult;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.StringCustomFieldRef;
 * import com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.RecordType;
 * import com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.
 * SearchMultiSelectFieldOperator; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.messages.ReadResponse; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.messages.WriteResponse; import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.purchases.VendorBill;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.purchases.VendorBillItem;
 * import com.netsuite.suitetalk.proxy.v2022_1.transactions.purchases.
 * VendorBillItemList; import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.purchases.VendorPayment;
 * import com.netsuite.webservices.samples.Properties; import
 * com.netsuite.webservices.samples.WsClientFactory;
 * 
 * import lombok.extern.slf4j.Slf4j;
 * 
 * @Slf4j
 * 
 * @Service public class NetSuiteServiceImpl implements NetSuiteService {
 * 
 * @Autowired private SupplierRepository supplierRepository;
 * 
 * @Autowired private SubsidiaryRepository subsidiaryRepository;
 * 
 * @Autowired private SupplierSubsidiaryRepository suppSubsidiaryRepository;
 * 
 * @Autowired private SupplierAddressRepository supplierAddressRepository;
 * 
 * @Autowired private InvoiceRepository invoiceRepository;
 * 
 * @Autowired private LocationRepository locationRepository;
 * 
 * @Autowired private ItemRepository itemRepository;
 * 
 * @Autowired private TaxGroupRepository taxGroupRepository;
 * 
 * @Autowired private CurrencyRepository currencyRepository;
 * 
 * @Autowired private DocumentSequenceService documentSequenceService;
 * 
 * @Autowired private MakePaymentRepository makePaymentRepository;
 * 
 * @Autowired private MakePaymentListRepository makePaymentListRepository;
 * 
 * @Autowired private AccountRepository accountRepository;
 * 
 * @Autowired private InvoicePaymentRepository invoicePaymentRepository;
 * 
 * private WsClient client;
 * 
 * public NetSuiteServiceImpl() {
 * 
 * try { client = WsClientFactory.getWsClient(new Properties(), null); } catch
 * (MalformedURLException e) { printError(INVALID_WS_URL, e.getMessage());
 * System.exit(2); } catch (AxisFault e) { printError(ERROR_OCCURRED,
 * e.getFaultString()); System.exit(3); } catch (IOException e) {
 * printError(WRONG_PROPERTIES_FILE, e.getMessage()); System.exit(1); }
 * 
 * }
 * 
 * @Transactional
 * 
 * @Override public Supplier sendSupplier(Long supplierId) {
 * 
 * Supplier supplier = null; Optional<Supplier> optSupplier =
 * supplierRepository.findByIdAndIsDeleted(supplierId, false);
 * if(optSupplier.isPresent()) { supplier = optSupplier.get(); Vendor vendor =
 * new Vendor(); vendor.setCompanyName(supplier.getName());
 * vendor.setEntityId(supplier.getVendorNumber());
 * vendor.setLegalName(supplier.getLegalName()); GetSelectValueFieldDescription
 * fieldDescription = new GetSelectValueFieldDescription();
 * fieldDescription.setRecordType(RecordType.vendor);
 * fieldDescription.setField("category"); try { List<BaseRef> values =
 * client.getSelectValue(fieldDescription); for(BaseRef baseRef : values) {
 * RecordRef recordRef = (RecordRef) baseRef;
 * if(supplier.getVendorType().equals(recordRef.getName())) {
 * vendor.setCategory(recordRef); break; } } } catch (RemoteException e) {
 * e.printStackTrace(); } // vendor.setTaxIdNum(supplier.getUniqueNumber());
 * fieldDescription.setField("terms"); try { List<BaseRef> values =
 * client.getSelectValue(fieldDescription); for(BaseRef baseRef : values) {
 * RecordRef recordRef = (RecordRef) baseRef;
 * if(supplier.getPaymentTerm().equals(recordRef.getName())) {
 * vendor.setTerms(recordRef); break; } } } catch (RemoteException e) {
 * e.printStackTrace(); } vendor.setIsInactive(!supplier.isActive());
 * Optional<SupplierSubsidiary> optSupplierSubsidiary =
 * suppSubsidiaryRepository.findBySupplierIdAndIsDeleted(supplierId, false);
 * if(optSupplierSubsidiary.isPresent()) { Optional<Subsidiary> optSubsidiary =
 * subsidiaryRepository.findByIdAndIsDeleted(optSupplierSubsidiary.get().
 * getSubsidiaryId(), false); if(optSubsidiary.isPresent())
 * vendor.setSubsidiary(createRecordRef(optSubsidiary.get().getIntegratedId()));
 * } // vendor.setEmail("tanmoy28@gmail.com");
 * vendor.setCustomForm(createRecordRef("159")); //53 //
 * vendor.setCurrency(createRecordRef("5")); //
 * vendor.setWorkCalendar(createRecordRef("1")); VendorAddressbookList
 * addressbookList = new VendorAddressbookList(); List<SupplierAddress>
 * supplierAddresses =
 * supplierAddressRepository.findBySupplierIdAndIsDeleted(supplierId, false);
 * VendorAddressbook[] addressBooks = new
 * VendorAddressbook[supplierAddresses.size()]; for(int i=0;
 * i<supplierAddresses.size(); ++i) { SupplierAddress supplierAddress =
 * supplierAddresses.get(i); VendorAddressbook addressBook = new
 * VendorAddressbook();
 * addressBook.setDefaultBilling(supplierAddress.isDefaultBilling());
 * addressBook.setDefaultShipping(supplierAddress.isDefaultShipping()); //
 * addressBook.setLabel("Test Address"); Address address = new Address(); //
 * address.setAddressee("T. Sarkar"); // address.setAttention("T. P. Sarkar");
 * address.setAddr1(supplierAddress.getAddress1());
 * address.setAddr2(supplierAddress.getAddress2());
 * address.setCity(supplierAddress.getCity());
 * address.setZip(supplierAddress.getPin());
 * address.setState(supplierAddress.getState());
 * address.setCountry(Country.fromValue(supplierAddress.getCountry()));
 * addressBook.setAddressbookAddress(address); addressBooks[i] = addressBook; }
 * addressbookList.setAddressbook(addressBooks);
 * vendor.setAddressbookList(addressbookList); WriteResponse response = null;
 * try { response = client.callAddRecord(vendor); } catch (RemoteException e) {
 * e.printStackTrace(); } String internalId = ((RecordRef)
 * response.getBaseRef()).getInternalId(); supplier.setIntegratedId(internalId);
 * supplier = supplierRepository.save(supplier); }
 * 
 * return supplier; }
 * 
 * @Transactional
 * 
 * @Override public Invoice sendInvoice(Long invoiceId) {
 * 
 * Invoice invoice = null; Optional<Invoice> optInvoice =
 * invoiceRepository.findById(invoiceId); if(optInvoice.isPresent()) { invoice =
 * optInvoice.get(); VendorBill vendorBill = new VendorBill();
 * Optional<Supplier> optSupplier =
 * supplierRepository.findByIdAndIsDeleted(invoice.getSupplierId(), false);
 * if(optSupplier.isPresent()) {
 * vendorBill.setEntity(createRecordRef(optSupplier.get().getIntegratedId())); }
 * //vendorBill.setTransactionNumber("Test14112022");
 * vendorBill.setTranId(invoice.getInvoiceNo()); Optional<Subsidiary>
 * optSubsidiary =
 * subsidiaryRepository.findByIdAndIsDeleted(invoice.getSubsidiaryId(), false);
 * if(optSubsidiary.isPresent())
 * vendorBill.setSubsidiary(createRecordRef(optSubsidiary.get().getIntegratedId(
 * ))); Optional<Location> optLocation =
 * locationRepository.findByIdAndIsDeleted(invoice.getLocationId(), false);
 * if(optLocation.isPresent())
 * vendorBill.setLocation(createRecordRef(optLocation.get().getIntegratedId()));
 * vendorBill.setTranDate(DateUtils.toCalendar(invoice.getInvoiceDate()));
 * GetSelectValueFieldDescription fieldDescription = new
 * GetSelectValueFieldDescription();
 * fieldDescription.setRecordType(RecordType.vendorBill);
 * fieldDescription.setField("terms"); try { List<BaseRef> values =
 * client.getSelectValue(fieldDescription); for(BaseRef baseRef : values) {
 * RecordRef recordRef = (RecordRef) baseRef;
 * if(invoice.getPaymentTerm().equals(recordRef.getName())) {
 * vendorBill.setTerms(recordRef);
 * //System.out.println(invoice.getPaymentTerm()); break; } } } catch
 * (RemoteException e) { e.printStackTrace(); } Optional<Currency> optCurrency =
 * currencyRepository.findByNameAndIsDeleted(invoice.getCurrency(), false);
 * if(optCurrency.isPresent())
 * vendorBill.setCurrency(createRecordRef(optCurrency.get().getIntegratedId()));
 * //vendorBill.setExchangeRate(invoice.getFxRate()); // RecordRef
 * approvalStatus = new RecordRef(); //
 * approvalStatus.setName(invoice.getInvStatus());
 * vendorBill.setApprovalStatus(createRecordRef("2")); VendorBillItemList
 * vendorBillItemList = new VendorBillItemList(); List<InvoiceItem> invoiceItems
 * = invoice.getInvoiceItems(); VendorBillItem[] vendorBillItems = new
 * VendorBillItem[invoiceItems.size()]; for(int i=0; i<invoiceItems.size(); ++i)
 * { InvoiceItem invoiceItem = invoiceItems.get(i); VendorBillItem
 * vendorBillItem = new VendorBillItem(); Optional<Item> optItem =
 * itemRepository.findByIdAndIsDeleted(invoiceItem.getItemId(), false);
 * if(optItem.isPresent()) {
 * vendorBillItem.setItem(createRecordRef(optItem.get().getIntegratedId()));
 * vendorBillItem.setDescription(optItem.get().getDescription()); }
 * vendorBillItem.setQuantity(invoiceItem.getBillQty());
 * vendorBillItem.setRate(String.valueOf(invoiceItem.getRate()));
 * //vendorBillItem.setAmount(500.3); Optional<TaxGroup> optTaxGroup =
 * taxGroupRepository.findByIdAndIsDeleted(invoiceItem.getTaxGroupId(), false);
 * if(optTaxGroup.isPresent())
 * vendorBillItem.setTaxCode(createRecordRef(optTaxGroup.get().getIntegratedId()
 * )); // vendorBillItem.setTaxAmount(25.2);
 * fieldDescription.setField("department"); try { List<BaseRef> values =
 * client.getSelectValue(fieldDescription); for(BaseRef baseRef : values) {
 * RecordRef recordRef = (RecordRef) baseRef;
 * if(invoiceItem.getDepartment().equals(recordRef.getName())) {
 * vendorBill.setDepartment(recordRef);
 * System.out.println(invoiceItem.getDepartment()); break; } } } catch
 * (RemoteException e) { e.printStackTrace(); } vendorBillItems[i] =
 * vendorBillItem; } vendorBillItemList.setItem(vendorBillItems);
 * vendorBill.setItemList(vendorBillItemList);
 * 
 * WriteResponse response = null; try { response =
 * client.callAddRecord(vendorBill); } catch (RemoteException e) {
 * e.printStackTrace(); } String internalId = ((RecordRef)
 * response.getBaseRef()).getInternalId(); invoice.setIntegratedId(internalId);
 * invoice = invoiceRepository.save(invoice); }
 * 
 * return invoice; }
 *//**
	 * Import MakePayment XML and DB Insert
	 * 
	 * @return Saved MakePayments
	 *//*
		 * @Override public List<MakePayment> importPaymentXML() { List<MakePayment>
		 * savedMakePayments = new ArrayList<>(); FileSearchBasic fileSearchBasic = new
		 * FileSearchBasic(); SearchMultiSelectField searchMultiSelectField = new
		 * SearchMultiSelectField(new RecordRef[] {createRecordRef("860")},
		 * SearchMultiSelectFieldOperator.anyOf);
		 * fileSearchBasic.setFolder(searchMultiSelectField); SearchResult searchResult
		 * = null; try { searchResult = client.callSearch(fileSearchBasic); } catch
		 * (RemoteException e) { e.printStackTrace(); } Record[] records =
		 * searchResult.getRecordList().getRecord(); if(records!=null) for (Record
		 * record : records) { log.info(((File)record).getInternalId()); String fileId =
		 * ((File) record).getInternalId(); MakePayment savedMakePayment =
		 * saveMakePayment(fileId); savedMakePayments.add(savedMakePayment); } return
		 * savedMakePayments; }
		 * 
		 * @Transactional MakePayment saveMakePayment(String fileId) { MakePayment
		 * savedMakePayment = null; Document document = null; try { ReadResponse
		 * response = client.callGetRecord(fileId, RecordType.file); File file = (File)
		 * response.getRecord(); DocumentBuilderFactory dbf =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder db =
		 * dbf.newDocumentBuilder(); document = db.parse(new
		 * ByteArrayInputStream(file.getContent())); } catch (IOException |
		 * ParserConfigurationException | SAXException e) { e.printStackTrace(); }
		 * document.getDocumentElement().normalize(); log.info("Import XML started.");
		 * MakePayment makePayment = new MakePayment(); NodeList nList =
		 * document.getElementsByTagName("SubsidiaryID"); Optional<Subsidiary>
		 * optSubsidiary =
		 * subsidiaryRepository.findByIntegratedIdAndIsDeleted(nList.item(0).
		 * getTextContent(), false); if(optSubsidiary.isPresent())
		 * makePayment.setSubsidiaryId(optSubsidiary.get().getId()); nList =
		 * document.getElementsByTagName("Date"); SimpleDateFormat sdf = new
		 * SimpleDateFormat("dd/MM/yyyy"); try { makePayment.setPaymentDate(new
		 * Date(sdf.parse(nList.item(0).getTextContent()).getTime())); } catch
		 * (DOMException | ParseException e) { e.printStackTrace(); } nList =
		 * document.getElementsByTagName("VendorId"); Optional<Supplier> optSupplier =
		 * supplierRepository.findByIntegratedIdAndIsDeleted(nList.item(0).
		 * getTextContent(), false); if(optSupplier.isPresent())
		 * makePayment.setSupplierId(optSupplier.get().getId()); nList =
		 * document.getElementsByTagName("BankAccount"); Optional<Account> accOptional =
		 * accountRepository.findByIntegratedIdAndIsDeleted(nList.item(0).getTextContent
		 * (), false); if(accOptional.isPresent())
		 * makePayment.setAccountId(accOptional.get().getId()); nList =
		 * document.getElementsByTagName("TotalAmount");
		 * makePayment.setAmount(Double.parseDouble(nList.item(0).getTextContent()));
		 * nList = document.getElementsByTagName("Currency");
		 * makePayment.setCurrency(nList.item(0).getTextContent()); nList =
		 * document.getElementsByTagName("BillPmtID"); String netsuiteId =
		 * nList.item(0).getTextContent(); makePayment.setNetsuiteId(netsuiteId);
		 * makePayment.setCreatedBy(CommonUtils.getLoggedInUsername());
		 * makePayment.setLastModifiedBy(CommonUtils.getLoggedInUsername()); String
		 * transactionalDate =
		 * CommonUtils.convertDateToFormattedString(makePayment.getPaymentDate());
		 * String documentSequenceNumber =
		 * this.documentSequenceService.getDocumentSequenceNumbers(transactionalDate,
		 * makePayment.getSubsidiaryId(), FormNames.MAKE_PAYMENT.getFormName(), false);
		 * if (StringUtils.isEmpty(documentSequenceNumber)) { throw new
		 * CustomMessageException("Please validate your configuration to generate the Payment Number"
		 * ); } makePayment.setPaymentNumber(documentSequenceNumber);
		 * makePayment.setType(FormNames.MAKE_PAYMENT.getFormName()); String message =
		 * "Success"; try { savedMakePayment = makePaymentRepository.save(makePayment);
		 * } catch (DataAccessException e) {
		 * log.error("Error while saving the Payment :: "+ e.getMostSpecificCause());
		 * message = e.getMostSpecificCause().getMessage(); // throw new
		 * CustomException("Error while saving the Payment: " +
		 * e.getMostSpecificCause()); } List<CustomFieldRef> customFieldRefs = new
		 * ArrayList<>(); if(savedMakePayment != null) { nList =
		 * document.getElementsByTagName("MblId"); NodeList pmtList =
		 * document.getElementsByTagName("PmtAmount"); List<MakePaymentList>
		 * makePaymentLists = new ArrayList<>(), savedMakePaymentLists = null;
		 * List<InvoicePayment> invoicePaymentList = new ArrayList<>(); for (int i = 0;
		 * i < nList.getLength(); i++) { MakePaymentList makePaymentList = new
		 * MakePaymentList(); makePaymentList.setPaymentId(savedMakePayment.getId());
		 * long invoiceId = Long.parseLong(nList.item(i).getTextContent());
		 * makePaymentList.setInvoiceId(invoiceId); Optional<Invoice> optInvoice =
		 * invoiceRepository.findById(invoiceId); if(optInvoice.isPresent()) { Invoice
		 * invoice = optInvoice.get();
		 * makePaymentList.setBillNo(invoice.getInvoiceNo());
		 * makePaymentList.setInvoiceAmount(invoice.getTotalAmount()); Double
		 * totalAmountPaid =
		 * invoicePaymentRepository.findTotalAmountByInvoiceIdAndIsDeleted(invoiceId,
		 * false); totalAmountPaid = (totalAmountPaid!=null? totalAmountPaid : 0) +
		 * Double.parseDouble(pmtList.item(i).getTextContent()); //
		 * invoice.setTotalPaidAmount(totalAmountPaid);
		 * invoice.setAmountDue(invoice.getTotalAmount() - totalAmountPaid);
		 * invoiceRepository.save(invoice); }
		 * makePaymentList.setPaidAmount(Double.parseDouble(pmtList.item(i).
		 * getTextContent())); makePaymentList.setPaymentNumber(documentSequenceNumber);
		 * makePaymentList.setCreatedBy(CommonUtils.getLoggedInUsername());
		 * makePaymentList.setLastModifiedBy(CommonUtils.getLoggedInUsername());
		 * makePaymentLists.add(makePaymentList); InvoicePayment invoicePayment = new
		 * InvoicePayment(); invoicePayment.setPaymentId(savedMakePayment.getId());
		 * invoicePayment.setInvoiceId(invoiceId); if(optInvoice.isPresent()) { Invoice
		 * invoice = optInvoice.get(); invoicePayment.setBillNo(invoice.getInvoiceNo());
		 * }
		 * invoicePayment.setAmount(Double.parseDouble(pmtList.item(i).getTextContent())
		 * ); invoicePayment.setType("Make Payment");
		 * invoicePaymentList.add(invoicePayment); }
		 * invoicePaymentRepository.saveAll(invoicePaymentList); try {
		 * savedMakePaymentLists = makePaymentListRepository.saveAll(makePaymentLists);
		 * } catch (DataAccessException e) {
		 * log.error("Error while saving the Payment List :: "+
		 * e.getMostSpecificCause()); message = e.getMostSpecificCause().getMessage();
		 * //throw new CustomException("Error while saving the Payment List: " +
		 * e.getMostSpecificCause()); } if(savedMakePaymentLists != null) {
		 * savedMakePayment.setMakePaymentList(savedMakePaymentLists); File copyFile =
		 * new File(); copyFile.setInternalId(fileId);
		 * copyFile.setFolder(createRecordRef("861")); try {
		 * client.callUpdateRecord(copyFile); } catch (RemoteException e) {
		 * e.printStackTrace(); } LongCustomFieldRef customFieldRef = new
		 * LongCustomFieldRef(); customFieldRef.setInternalId("4822");
		 * customFieldRef.setValue(savedMakePayment.getId());
		 * customFieldRefs.add(customFieldRef); } log.info("Imported XML: " +
		 * savedMakePayment); } StringCustomFieldRef customFieldRef = new
		 * StringCustomFieldRef(); customFieldRef.setInternalId("4830");
		 * customFieldRef.setValue(message); customFieldRefs.add(customFieldRef);
		 * VendorPayment vendorPayment = new VendorPayment();
		 * vendorPayment.setInternalId(netsuiteId); CustomFieldList customFieldList =
		 * new CustomFieldList();
		 * customFieldList.setCustomField(customFieldRefs.toArray(new
		 * CustomFieldRef[customFieldRefs.size()]));
		 * vendorPayment.setCustomFieldList(customFieldList); try {
		 * client.callUpdateRecord(vendorPayment); } catch (RemoteException e) {
		 * e.printStackTrace(); } return savedMakePayment; }
		 * 
		 * }
		 */