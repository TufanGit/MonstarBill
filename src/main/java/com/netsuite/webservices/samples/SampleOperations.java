/*
 * package com.netsuite.webservices.samples;
 * 
 * import com.netsuite.suitetalk.client.v2022_1.WsClient; import
 * com.netsuite.suitetalk.client.v2022_1.utils.Utils; import
 * com.netsuite.suitetalk.proxy.v2022_1.documents.filecabinet.File; import
 * com.netsuite.suitetalk.proxy.v2022_1.documents.filecabinet.types.
 * FileAttachFrom; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.accounting.InventoryItem; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.accounting.Price; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.accounting.PriceList; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.accounting.Pricing; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.accounting.PricingMatrix; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.accounting.types.
 * ItemCostingMethod; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.Customer; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.CustomerAddressbook;
 * import com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.
 * CustomerAddressbookList; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.CustomerSearch;
 * import com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.
 * CustomerSearchAdvanced; import
 * com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.CustomerSearchRow;
 * import com.netsuite.suitetalk.proxy.v2022_1.lists.relationships.types.
 * EmailPreference; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.Address; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.CustomRecordSearchBasic;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.CustomerSearchBasic;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.CustomerSearchRowBasic;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.TransactionSearchBasic;
 * import com.netsuite.suitetalk.proxy.v2022_1.platform.common.
 * TransactionSearchRowBasic; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.common.types.Country; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.*; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.Record; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.GetAllRecordType;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.InitializeRefType;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.InitializeType;
 * import com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.RecordType;
 * import com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.
 * SearchEnumMultiSelectFieldOperator; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.
 * SearchMultiSelectFieldOperator; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.
 * SearchStringFieldOperator; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.messages.ReadResponse; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.messages.ReadResponseList;
 * import com.netsuite.suitetalk.proxy.v2022_1.platform.messages.WriteResponse;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.messages.WriteResponseList;
 * import com.netsuite.suitetalk.proxy.v2022_1.setup.customization.CustomRecord;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.ItemFulfillment;
 * import com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.SalesOrder;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.SalesOrderItem;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.SalesOrderItemList;
 * import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.TransactionSearch;
 * import com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.
 * TransactionSearchAdvanced; import
 * com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.TransactionSearchRow;
 * import com.netsuite.suitetalk.proxy.v2022_1.transactions.sales.types.
 * SalesOrderOrderStatus; import
 * com.netsuite.webservices.samples.utils.PrintUtils; import
 * org.apache.axis.AxisFault;
 * 
 * import javax.annotation.ParametersAreNonnullByDefault; import
 * java.io.IOException; import java.nio.file.Files; import java.nio.file.Path;
 * import java.nio.file.Paths; import java.rmi.RemoteException; import
 * java.util.ArrayList; import java.util.Arrays; import java.util.Calendar;
 * import java.util.Collections; import java.util.LinkedHashMap; import
 * java.util.List; import java.util.Map; import java.util.Objects; import
 * java.util.function.Supplier; import java.util.stream.Collectors;
 * 
 * import static
 * com.netsuite.suitetalk.client.common.utils.CommonUtils.isEmptyString; import
 * static com.netsuite.suitetalk.client.v2022_1.utils.Utils.createRecordRef;
 * import static com.netsuite.webservices.samples.Messages.*; import static
 * com.netsuite.webservices.samples.ResponseHandler.*; import static
 * com.netsuite.webservices.samples.io.Console.readLine; import static
 * com.netsuite.webservices.samples.utils.IndentationUtils.getIndentedString;
 * import static com.netsuite.webservices.samples.utils.PrintUtils.*; import
 * static com.netsuite.webservices.samples.utils.StringUtils.getBoolean; import
 * static com.netsuite.webservices.samples.utils.StringUtils.getListItems;
 * import static
 * com.netsuite.webservices.samples.utils.StringUtils.getRandomString; import
 * static java.lang.String.format;
 * 
 *//**
	 * <p>
	 * Displays a list of all sample operations and invokes the selected operation
	 * by the user.
	 * </p>
	 * <p>
	 * © 2019 NetSuite Inc. All rights reserved.
	 * </p>
	 */
/*
 * @ParametersAreNonnullByDefault public class SampleOperations {
 * 
 * private static final int DEFAULT_PAGE_SIZE = 1000; private static final int
 * PAGE_SIZE = 10;
 * 
 * private static final Map<String, Operation> SAMPLE_OPERATIONS = new
 * LinkedHashMap<>();
 * 
 * private WsClient client;
 * 
 *//**
	 * Constructor initializing a list of all sample operations.
	 *
	 * @param client Client used for all SOAP requests
	 */
/*
 * public SampleOperations(WsClient client) { this.client = client; // All
 * possible sample operations addCustomer(); addCustomerWithCustomFields();
 * updateCustomer(); upsertCustomer(); updateListOfCustomers(); getCustomer();
 * getListOfCustomers(); deleteListOfCustomers(); addInventoryItem();
 * addSalesOrder(); updateSalesOrder(); fulfillSalesOrder();
 * searchSalesOrders(); advancedSearchSalesOrders(); addCustomRecord();
 * searchCustomRecord(); deleteCustomRecord(); getOtherListValues();
 * uploadFile(); getSelectFieldValues(); }
 * 
 *//**
	 * Starts selection of sample operation.
	 */
/*
 * public void run() { OptionList<Operation> operationList = new
 * OptionList<>(MAKE_SELECTION, SAMPLE_OPERATIONS);
 * operationList.setQuitOption(QUIT_CHARACTER, QUIT);
 * 
 * Operation operation; while ((operation =
 * operationList.displayAndGetUserChoice()) != null) { try {
 * operation.performOperation(); } catch (AxisFault axisFault) { // Handling of
 * unexpected error printError(axisFault.getFaultString()); } catch
 * (RemoteException e) { printError(e.getMessage()); } waitForEnter(); }
 * 
 * printWithEmptyLine(PRESS_TO_QUIT); readLine(); }
 * 
 *//**
	 * Demonstrates how to add a Customer record into NetSuite using the
	 * {@code add()} operation.
	 */
/*
 * private void addCustomer() { SAMPLE_OPERATIONS.put(ADD_CUSTOMER, () -> {
 * printWithEmptyLine(ENTER_CUSTOMER_INFORMATION + SPACE +
 * FIELDS_ALREADY_POPULATED);
 * 
 * Customer customer = new Customer();
 * 
 * customer.setCompanyName(readLine(getIndentedString(COMPANY_NAME), null));
 * customer.setEntityId(readLine(getIndentedString(ENTITY_NAME), null));
 * customer.setEmail(readLine(getIndentedString(EMAIL), null));
 * customer.setEmailPreference(EmailPreference._hTML);
 * 
 * // Set entity status. The internal ID can be obtained from Setup > Sales >
 * Customer Statuses. // The default status is "Closed Won" which has an
 * internal ID of 13. String statusInternalId =
 * readLine(getIndentedString(ENTITY_STATUS_INTERNAL_ID),
 * DEFAULT_STATUS_INTERNAL_ID);
 * customer.setEntityStatus(createRecordRef(statusInternalId));
 * 
 * // Create address for this customer Address address = new Address();
 * address.setAddressee("William Sanders");
 * address.setAttention("William Sanders");
 * address.setAddr1("4765 Sunset Blvd"); address.setCity("San Francisco");
 * address.setZip("94131"); address.setState("CA");
 * address.setCountry(Country._unitedStates);
 * address.setAddr1(readLine(getIndentedString(ADDRESS_1), null));
 * address.setAddr2(readLine(getIndentedString(ADDRESS_2), null));
 * address.setAddr3(readLine(getIndentedString(ADDRESS_3), null));
 * 
 * setCustomerAddress(customer, address, "Shipping Address", true, true);
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke add() operation WriteResponse response =
 * client.callAddRecord(customer);
 * 
 * // Process the response processCustomerWriteResponse(response, customer); });
 * }
 * 
 *//**
	 * Demonstrates how to use custom fields when adding a Customer record into
	 * NetSuite using the {@code add()} operation. The custom fields need to be
	 * already created in NetSuite and the script IDs for these fields need to be
	 * obtained.
	 */
/*
 * private void addCustomerWithCustomFields() {
 * SAMPLE_OPERATIONS.put(ADD_CUSTOMER_WITH_CUSTOM_FIELDS, () -> {
 * 
 * // Prepare list of options for populating custom fields final Map<String,
 * Supplier<CustomFieldRef>> customFieldPopulation = new LinkedHashMap<>();
 * 
 * // For every option, create a custom field of respective type and read and
 * assign value to it.
 * 
 * customFieldPopulation.put(CUSTOM_FIELD_STRING, () -> { StringCustomFieldRef
 * customFieldRef = new StringCustomFieldRef();
 * customFieldRef.setScriptId(readLine(getIndentedString(SCRIPT_ID)));
 * customFieldRef.setValue(readLine(getIndentedString(CUSTOM_FIELD_VALUE_STRING)
 * , "Test Value")); return customFieldRef; });
 * 
 * customFieldPopulation.put(CUSTOM_FIELD_BOOLEAN, () -> { BooleanCustomFieldRef
 * customFieldRef = new BooleanCustomFieldRef();
 * customFieldRef.setScriptId(readLine(getIndentedString(SCRIPT_ID))); String
 * readValue = readLine(getIndentedString(CUSTOM_FIELD_VALUE_BOOLEAN),
 * TRUE_SHORT_VALUE).trim(); customFieldRef.setValue(getBoolean(readValue));
 * return customFieldRef; });
 * 
 * customFieldPopulation.put(CUSTOM_FIELD_LIST, () -> { SelectCustomFieldRef
 * customFieldRef = new SelectCustomFieldRef();
 * customFieldRef.setScriptId(readLine(getIndentedString(SCRIPT_ID)));
 * ListOrRecordRef listOrRecordRef = new ListOrRecordRef();
 * listOrRecordRef.setInternalId(readLine(getIndentedString(
 * CUSTOM_FIELD_VALUE_LIST)).trim()); customFieldRef.setValue(listOrRecordRef);
 * return customFieldRef; });
 * 
 * customFieldPopulation.put(CUSTOM_FIELD_MULTI_SELECT, () -> {
 * MultiSelectCustomFieldRef customFieldRef = new MultiSelectCustomFieldRef();
 * customFieldRef.setScriptId(readLine(getIndentedString(SCRIPT_ID)));
 * List<String> internalIds =
 * getListItems(readLine(getIndentedString(CUSTOM_FIELD_VALUE_MULTI_SELECT)));
 * ListOrRecordRef[] listOrRecordRefs = internalIds.stream() .map(internalId ->
 * new ListOrRecordRef(null, internalId, null, null))
 * .toArray(ListOrRecordRef[]::new); customFieldRef.setValue(listOrRecordRefs);
 * return customFieldRef; });
 * 
 * // Create customer and fill its fields Customer customer = new Customer();
 * 
 * String randomId = getRandomString(); customer.setEntityId(randomId +
 * " Custom Inc"); customer.setCompanyName(randomId + " Custom, Inc.");
 * 
 * printWithEmptyLine(CUSTOM_FIELDS_WARNING);
 * 
 * // Display options for populating custom fields to user
 * 
 * OptionList<Supplier<CustomFieldRef>> customFieldOptionList = new
 * OptionList<>(MAKE_CUSTOM_FIELD_SELECTION, customFieldPopulation);
 * customFieldOptionList.setQuitOption(QUIT_CHARACTER, QUIT_CUSTOM_FIELDS);
 * 
 * List<CustomFieldRef> customFieldRefs = new ArrayList<>();
 * 
 * Supplier<CustomFieldRef> supplier; while ((supplier =
 * customFieldOptionList.displayAndGetUserChoice()) != null) { printEmptyLine();
 * customFieldRefs.add(supplier.get()); }
 * 
 * if (customFieldRefs.isEmpty()) { printError(NO_CUSTOM_FIELDS_SPECIFIED);
 * return; }
 * 
 * CustomFieldList customFieldList = new CustomFieldList();
 * customFieldList.setCustomField(customFieldRefs.toArray(new
 * CustomFieldRef[customFieldRefs.size()]));
 * customer.setCustomFieldList(customFieldList);
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke add() operation WriteResponse response =
 * client.callAddRecord(customer);
 * 
 * // Process the response processCustomerWriteResponse(response, customer); });
 * }
 * 
 *//**
	 * Demonstrates how to update an existing Customer record in NetSuite using the
	 * {@code update()} operation which uses an internal ID to reference the record
	 * to be updated.
	 */
/*
 * private void updateCustomer() { SAMPLE_OPERATIONS.put(UPDATE_CUSTOMER, () ->
 * { printWithEmptyLine(ENTER_CUSTOMER_INFORMATION_FOR_UPDATE);
 * 
 * Customer customer = new Customer();
 * 
 * // Get internal ID and entity ID for update
 * customer.setInternalId(readLine(getIndentedString(INTERNAL_ID)).trim());
 * customer.setEntityId(readLine(getIndentedString(ENTITY_NAME), null));
 * 
 * // Update some fields String randomString = getRandomString();
 * customer.setCompanyName(randomString + " Updated Company Name, Inc.");
 * customer.setEmail("company" + randomString + "@example.com");
 * 
 * // Add a billing address Address address = new Address();
 * address.setAddr1("4765 Sunset Blvd"); address.setCity("San Mateo");
 * address.setZip("94401"); address.setState("CA");
 * address.setCountry(Country._unitedStates);
 * 
 * setCustomerAddress(customer, address, "Billing Address", true, false);
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke update() operation WriteResponse response =
 * client.callUpdateRecord(customer);
 * 
 * // Process the response processCustomerWriteResponse(response, customer); });
 * }
 * 
 *//**
	 * Demonstrates how to Add/Update a Customer record into NetSuite using the
	 * {@code upsert()} operation.
	 */
/*
 * private void upsertCustomer() { SAMPLE_OPERATIONS.put(UPSERT_CUSTOMER, () ->
 * { printWithEmptyLine(ENTER_CUSTOMER_INFORMATION + SPACE +
 * FIELDS_ALREADY_POPULATED);
 * 
 * Customer customer = new Customer();
 * 
 * customer.setExternalId(readLine(getIndentedString(EXTERNAL_ID)));
 * customer.setCompanyName(readLine(getIndentedString(COMPANY_NAME), null));
 * customer.setEntityId(readLine(getIndentedString(ENTITY_NAME), null));
 * customer.setEmail(readLine(getIndentedString(EMAIL), null));
 * customer.setEmailPreference(EmailPreference._hTML);
 * 
 * // Set entity status. The internal ID can be obtained from Setup > Sales >
 * Customer Statuses. // The default status is "Closed Won" which has an
 * internal ID of 13. String statusInternalId =
 * readLine(getIndentedString(ENTITY_STATUS_INTERNAL_ID),
 * DEFAULT_STATUS_INTERNAL_ID);
 * customer.setEntityStatus(createRecordRef(statusInternalId));
 * 
 * // Create address for this customer Address address = new Address();
 * address.setAddressee("William Sanders");
 * address.setAttention("William Sanders");
 * address.setAddr1("4765 Sunset Blvd"); address.setCity("San Francisco");
 * address.setZip("94131"); address.setState("CA");
 * address.setCountry(Country._unitedStates);
 * 
 * setCustomerAddress(customer, address, "Shipping Address", false, true);
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke upsert() operation WriteResponse response =
 * client.callUpsertRecord(customer);
 * 
 * // Process the response processCustomerWriteResponse(response, customer); });
 * }
 * 
 *//**
	 * Demonstrates how to update a list of existing customer records in NetSuite
	 * using the updateList() operation.
	 */
/*
 * private void updateListOfCustomers() {
 * SAMPLE_OPERATIONS.put(UPDATE_LIST_OF_CUSTOMERS, () -> { printEmptyLine();
 * 
 * List<String> internalIds = getListItems(readLine(ENTER_LIST_OF_CUSTOMERS));
 * List<Customer> customersToUpdate = new ArrayList<>(internalIds.size());
 * 
 * String randomString = getRandomString();
 * 
 * for (int i = 0; i < internalIds.size(); i++) { Customer customer = new
 * Customer(); customer.setInternalId(internalIds.get(i));
 * 
 * // Update name customer.setEntityId(randomString + " XYZ Inc " + (i + 1));
 * customer.setCompanyName(randomString + " XYZ, Inc. " + (i + 1));
 * 
 * customersToUpdate.add(customer); }
 * 
 * if (customersToUpdate.isEmpty()) {
 * printError(NO_VALID_INTERNAL_IDS_FOR_UPDATE_PROVIDED); } else {
 * printSendingRequestMessage();
 * 
 * // Invoke updateList() operation to update customers WriteResponseList
 * responseList = client.callUpdateRecords(customersToUpdate);
 * 
 * // Process the response processCustomerWriteResponseList(responseList,
 * customersToUpdate); } }); }
 * 
 *//**
	 * Demonstrates how to get an existing record in NetSuite using the get()
	 * operation.
	 */
/*
 * private void getCustomer() { SAMPLE_OPERATIONS.put(GET_CUSTOMER, () -> {
 * printEmptyLine();
 * 
 * // Prompt for the internal ID String internalId =
 * readLine(INTERNAL_ID_TO_GET);
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke the get() operation to retrieve the record ReadResponse response =
 * client.callGetRecord(createRecordRef(internalId, RecordType.customer));
 * 
 * // Process the response processCustomerReadResponse(response); }); }
 * 
 *//**
	 * Demonstrates how to get a list of existing records in NetSuite using the
	 * getList() operation.
	 */
/*
 * private void getListOfCustomers() {
 * SAMPLE_OPERATIONS.put(GET_LIST_OF_CUSTOMERS, () -> { printEmptyLine();
 * 
 * // Prompt for list of internal IDs List<String> internalIds =
 * getListItems(readLine(INTERNAL_IDS_TO_GET)); if (internalIds.isEmpty()) {
 * printError(NO_VALID_INTERNAL_IDS_FOR_GET_PROVIDED); return; }
 * 
 * // Create RecordRef for every internal ID RecordRef[] recordRefs =
 * internalIds.stream() .map(internalId -> createRecordRef(internalId,
 * RecordType.customer)) .toArray(RecordRef[]::new);
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke getList() operation to retrieve the records ReadResponseList
 * responseList = client.callGetRecords(recordRefs);
 * 
 * // Process the response processCustomerReadResponseList(responseList,
 * recordRefs); }); }
 * 
 *//**
	 * Demonstrates how to delete a list of existing customer records in NetSuite
	 * using the deleteList() operation.
	 */
/*
 * private void deleteListOfCustomers() {
 * SAMPLE_OPERATIONS.put(DELETE_LIST_OF_CUSTOMERS, () -> { printEmptyLine();
 * 
 * // Prompt for list of internal IDs List<String> internalIds =
 * getListItems(readLine(INTERNAL_IDS_TO_DELETE)); if (internalIds.isEmpty()) {
 * printError(NO_VALID_INTERNAL_IDS_FOR_DELETE_PROVIDED); return; }
 * 
 * // First get the records from NetSuite
 * printInfoWithEmptyLine(CHECKING_EXISTENCE_OF_RECORDS);
 * 
 * List<Customer> records = client.getRecords(internalIds.stream()
 * .map(internalId -> createRecordRef(internalId, RecordType.customer))
 * .toArray(RecordRef[]::new)) // Filter just non-null records .stream()
 * .filter(Objects::nonNull) .map(record -> (Customer) record)
 * .collect(Collectors.toList());
 * 
 * if (records == null || records.isEmpty()) {
 * printError(NO_VALID_INTERNAL_IDS_FOR_DELETE_PROVIDED); return; }
 * 
 * printWithEmptyLine(FOLLOWING_RECORDS_WILL_BE_DELETED);
 * 
 * for (int i = 0; i < records.size(); i++) {
 * printWithEmptyLine(getIndentedString(CUSTOMER_WITH_INDEX), i + 1);
 * printMap(new Fields(records.get(i))); }
 * 
 * printEmptyLine(); boolean isDeletionConfirmed =
 * getBoolean(readLine(DELETE_ALL_RECORDS, "N"));
 * 
 * if (isDeletionConfirmed) { printSendingRequestMessage(); // Invoke
 * deleteList() operation to delete the records RecordRef[] recordRefs =
 * records.stream() .map(record -> createRecordRef(record.getInternalId(),
 * RecordType.customer)) .toArray(RecordRef[]::new); WriteResponseList
 * responseList = client.callDeleteRecords(recordRefs);
 * processCustomerDeleteList(responseList, recordRefs); } else {
 * printInfoWithEmptyLine(RECORDS_WERE_NOT_DELETED); } }); }
 * 
 *//**
	 * Demonstrates how to add an Inventory Item record into NetSuite using the
	 * {@code add()} operation.
	 */
/*
 * private void addInventoryItem() { SAMPLE_OPERATIONS.put(ADD_INVENTORY_ITEM,
 * () -> { printWithEmptyLine(ENTER_INFORMATION_FOR_ITEM);
 * 
 * InventoryItem inventoryItem = new InventoryItem();
 * inventoryItem.setItemId(readLine(getIndentedString(ITEM_NAME), null));
 * 
 * String costingMethodInput =
 * readLine(getIndentedString(COSTING_METHOD)).trim(); ItemCostingMethod
 * itemCostingMethod = null; switch (costingMethodInput) { case "1":
 * itemCostingMethod = ItemCostingMethod._average; break; case "2":
 * itemCostingMethod = ItemCostingMethod._fifo; break; case "3":
 * itemCostingMethod = ItemCostingMethod._lifo; break; }
 * inventoryItem.setCostingMethod(itemCostingMethod);
 * 
 * inventoryItem.setTaxSchedule(createRecordRef("1"));
 * 
 * // Setting pricing matrix String number = null; try { Price price = new
 * Price(); number =
 * readLine(getIndentedString(BASE_PRICE_WITH_EXAMPLE)).trim();
 * price.setValue(Double.parseDouble(number)); number =
 * readLine(getIndentedString(QUANTITY_WITH_EXAMPLE)).trim();
 * price.setQuantity(Double.parseDouble(number));
 * 
 * Pricing pricing = new Pricing(); pricing.setCurrency(createRecordRef("1"));
 * pricing.setDiscount(0.0); pricing.setPriceLevel(createRecordRef("1"));
 * pricing.setPriceList(new PriceList(new Price[]{price}));
 * 
 * PricingMatrix pricingMatrix = new PricingMatrix();
 * pricingMatrix.setPricing(new Pricing[]{pricing});
 * 
 * inventoryItem.setPricingMatrix(pricingMatrix); } catch (NumberFormatException
 * nfe) { printError(format(INVALID_NUMBER + SPACE +
 * ITEM_WITHOUT_PRICING_MATRIX, number)); }
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke add() operation WriteResponse response =
 * client.callAddRecord(inventoryItem);
 * 
 * // Process the response processItemWriteResponse(response, inventoryItem);
 * }); }
 * 
 *//**
	 * Demonstrates how to add a Sales Order record into NetSuite using the
	 * {@code add()} operation.
	 */
/*
 * private void addSalesOrder() { SAMPLE_OPERATIONS.put(ADD_SALES_ORDER, () -> {
 * printWithEmptyLine(ENTER_CUSTOMER_INFORMATION);
 * 
 * SearchStringField entityId = new SearchStringField();
 * entityId.setOperator(SearchStringFieldOperator.is); String customerName =
 * readLine(getIndentedString(CUSTOMER_NAME));
 * entityId.setSearchValue(customerName);
 * 
 * CustomerSearchBasic customerSearchBasic = new CustomerSearchBasic();
 * customerSearchBasic.setEntityId(entityId);
 * 
 * printSendingRequestMessage();
 * 
 * List<?> foundCustomers = client.search(customerSearchBasic); if
 * (foundCustomers == null || foundCustomers.isEmpty()) {
 * printError(SALES_ORDER_CUSTOMER_NOT_FOUND, customerName); return; } if
 * (foundCustomers.size() > 1) { printError(SALES_ORDER_MORE_CUSTOMERS_FOUND,
 * customerName); return; }
 * 
 * printEmptyLine();
 * 
 * SalesOrder salesOrder = new SalesOrder();
 * salesOrder.setEntity(createRecordRef(((Customer)
 * foundCustomers.get(0)).getInternalId()));
 * 
 * // Set the transaction date and status
 * salesOrder.setTranDate(Calendar.getInstance());
 * salesOrder.setOrderStatus(SalesOrderOrderStatus._pendingFulfillment);
 * 
 * // Enter the internal IDs of inventory items to be added to this Sales Order
 * List<String> itemsInternalIds =
 * getListItems(readLine(ENTER_ITEM_IDS_FOR_SALES_ORDER));
 * salesOrder.setItemList(getSalesOrderItemList(itemsInternalIds));
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke add() operation WriteResponse response =
 * client.callAddRecord(salesOrder);
 * 
 * // Process the response processSalesOrderWriteResponse(response); }); }
 * 
 *//**
	 * Demonstrates how to update an existing Sales Order record in NetSuite using
	 * the {@code update()} operation which uses an internal ID to reference the
	 * record to be updated.
	 */
/*
 * private void updateSalesOrder() { SAMPLE_OPERATIONS.put(UPDATE_SALES_ORDER,
 * () -> { printEmptyLine();
 * 
 * SalesOrder salesOrder = new SalesOrder();
 * 
 * // Get internal ID for update
 * salesOrder.setInternalId(readLine(ENTER_SALES_ORDER_FOR_UPDATE));
 * 
 * printEmptyLine();
 * 
 * // Enter the internal IDs of inventory items to be added to the Sales Order
 * List<String> itemsInternalIds =
 * getListItems(readLine(ENTER_ITEM_IDS_FOR_SALES_ORDER)); SalesOrderItemList
 * salesOrderItemList = getSalesOrderItemList(itemsInternalIds);
 * salesOrderItemList.setReplaceAll(false);
 * salesOrder.setItemList(salesOrderItemList);
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke update() operation WriteResponse response =
 * client.callUpdateRecord(salesOrder);
 * 
 * // Process the response processSalesOrderWriteResponse(response); }); }
 * 
 *//**
	 * Demonstrates how to fulfill a Sales Order using the {@code initialize()} and
	 * {@code add()} operations.
	 */
/*
 * private void fulfillSalesOrder() { SAMPLE_OPERATIONS.put(FULFILL_SALES_ORDER,
 * () -> { printEmptyLine();
 * 
 * InitializeRef initializeRef = new InitializeRef();
 * initializeRef.setType(InitializeRefType.salesOrder); // Get internal ID for
 * initialize() operation
 * initializeRef.setInternalId(readLine(ENTER_SALES_ORDER_FOR_FULFILLING));
 * 
 * InitializeRecord initializeRecord = new InitializeRecord();
 * initializeRecord.setType(InitializeType.itemFulfillment);
 * initializeRecord.setReference(initializeRef);
 * 
 * printSendingRequestMessage();
 * 
 * // Perform initialize() operation to get a copy of ItemFulfillment record
 * ReadResponse initializeResponse = client.callInitialize(initializeRecord);
 * ItemFulfillment itemFulfillment =
 * processInitializeReadResponse(initializeResponse); if (itemFulfillment ==
 * null) { return; }
 * 
 * // You can change any desired properties of retrieved Item Fulfillment //
 * itemFulfillment.setTransferLocation(...);
 * 
 * // Set preference to ignore read-only fields. Some of the fields included in
 * retrieved ItemFulfillment // record cannot be set. Setting this preference
 * solve that problem. client.setIgnoreReadOnlyFields(true);
 * 
 * printSendingRequestMessage();
 * 
 * // Store retrieved Item Fulfillment record into NetSuite WriteResponse
 * response = client.callAddRecord(itemFulfillment);
 * 
 * // Set ignore read-only preference to the default value
 * client.setIgnoreReadOnlyFields(false);
 * 
 * // Process the response processItemFulfillmentWriteResponse(response,
 * initializeRef.getInternalId()); }); }
 * 
 *//**
	 * Demonstrates how to search for Sales Orders for a given customer name.
	 */
/*
 * private void searchSalesOrders() { SAMPLE_OPERATIONS.put(SEARCH_SALES_ORDERS,
 * () -> { printWithEmptyLine(ENTER_CUSTOMER_INFORMATION);
 * 
 * String customerName = readLine(getIndentedString(CUSTOMER_NAME));
 * List<Customer> customers = searchForCustomers(customerName); if
 * (customers.isEmpty()) { printError(NO_CUSTOMERS_FOUND, customerName); return;
 * }
 * 
 * // Search sales order for all found customers SearchMultiSelectField entities
 * = new SearchMultiSelectField();
 * entities.setOperator(SearchMultiSelectFieldOperator.anyOf);
 * entities.setSearchValue(customers.stream() .map(customer ->
 * createRecordRef(customer.getInternalId(), RecordType.customer))
 * .toArray(RecordRef[]::new));
 * 
 * TransactionSearchBasic transactionSearchBasic = new TransactionSearchBasic();
 * transactionSearchBasic.setType(new SearchEnumMultiSelectField( new
 * String[]{RecordType._salesOrder}, SearchEnumMultiSelectFieldOperator.anyOf));
 * transactionSearchBasic.setEntity(entities);
 * 
 * // We want to returned also list of items so we need to set the following
 * preference client.setBodyFieldsOnly(false);
 * 
 * // Set smaller page size in order to demonstrate how searchMoreWithId()
 * operation works client.setPageSize(PAGE_SIZE);
 * 
 * printSendingRequestMessage();
 * 
 * // Search for sales orders SearchResult searchResult =
 * client.callSearch(transactionSearchBasic); final String jobId =
 * client.getLastJobId();
 * 
 * processSearchResult(searchResult, customerName);
 * 
 * // Get next pages of the search result if
 * (isSuccessfulSearchResult(searchResult)) { for (int i = 2; i <=
 * searchResult.getTotalPages(); i++) { printSendingRequestMessage();
 * processSearchResult(client.callSearchMoreWithId(jobId, i), customerName); } }
 * 
 * // We can revert search preferences to the default values now
 * client.setBodyFieldsOnly(true); client.setPageSize(DEFAULT_PAGE_SIZE); }); }
 * 
 *//**
	 * Demonstrates how to use advanced search for searching Sales Orders which
	 * belong to a given customer name.
	 */
/*
 * private void advancedSearchSalesOrders() {
 * SAMPLE_OPERATIONS.put(ADVANCED_SEARCH_SALES_ORDERS, () -> { printEmptyLine();
 * 
 * SearchEnumMultiSelectField recordType = new SearchEnumMultiSelectField();
 * recordType.setOperator(SearchEnumMultiSelectFieldOperator.anyOf);
 * recordType.setSearchValue(new String[]{RecordType._salesOrder});
 * 
 * TransactionSearchBasic transactionSearchBasic = new TransactionSearchBasic();
 * transactionSearchBasic.setType(recordType); // In order to have a particular
 * sales order in search result just once, set the following field.
 * transactionSearchBasic.setMainLine(new SearchBooleanField(true));
 * 
 * final String tranId = readLine(ENTER_TRANSACTION_ID); if
 * (!isEmptyString(tranId)) { SearchStringField tranIdField = new
 * SearchStringField(); tranIdField.setOperator(SearchStringFieldOperator.is);
 * tranIdField.setSearchValue(tranId);
 * transactionSearchBasic.setTranId(tranIdField); }
 * 
 * final List<String> customerIds =
 * getListItems(readLine(ENTER_CUSTOMER_INTERNAL_ID_FOR_SALES_ORDER_SEARCH)); if
 * (!customerIds.isEmpty()) { SearchMultiSelectField entityField = new
 * SearchMultiSelectField();
 * entityField.setOperator(SearchMultiSelectFieldOperator.anyOf);
 * entityField.setSearchValue(customerIds.stream() .map(Utils::createRecordRef)
 * .toArray(RecordRef[]::new)); transactionSearchBasic.setEntity(entityField); }
 * 
 * // Apply search criteria TransactionSearch transactionSearch = new
 * TransactionSearch(); transactionSearch.setBasic(transactionSearchBasic);
 * 
 * // Define columns to be returned TransactionSearchRowBasic
 * transactionSearchRowBasic = new TransactionSearchRowBasic();
 * transactionSearchRowBasic.setInternalId(new SearchColumnSelectField[]{new
 * SearchColumnSelectField()}); transactionSearchRowBasic.setTranId(new
 * SearchColumnStringField[]{new SearchColumnStringField()});
 * transactionSearchRowBasic.setDateCreated(new SearchColumnDateField[]{new
 * SearchColumnDateField()}); transactionSearchRowBasic.setTotal(new
 * SearchColumnDoubleField[]{new SearchColumnDoubleField()});
 * transactionSearchRowBasic.setEntity(new SearchColumnSelectField[]{new
 * SearchColumnSelectField()});
 * 
 * TransactionSearchRow transactionSearchRow = new TransactionSearchRow();
 * transactionSearchRow.setBasic(transactionSearchRowBasic);
 * 
 * TransactionSearchAdvanced transactionSearchAdvanced = new
 * TransactionSearchAdvanced();
 * transactionSearchAdvanced.setCriteria(transactionSearch);
 * transactionSearchAdvanced.setColumns(transactionSearchRow);
 * 
 * if (isEmptyString(tranId) && customerIds.isEmpty()) {
 * printInfoWithEmptyLine(NO_SEARCH_CRITERIA_DEFINED); }
 * 
 * // Set smaller page size in order to demonstrate how searchMoreWithId()
 * operation works client.setPageSize(PAGE_SIZE);
 * 
 * printSendingRequestMessage();
 * 
 * SearchResult searchResult = client.callSearch(transactionSearchAdvanced);
 * final String jobId = client.getLastJobId();
 * 
 * processSearchResult(searchResult, null);
 * 
 * // Get next pages of the search result if
 * (isSuccessfulSearchResult(searchResult)) { for (int i = 2; i <=
 * searchResult.getTotalPages(); i++) { printSendingRequestMessage();
 * processSearchResult(client.callSearchMoreWithId(jobId, i), null); } }
 * 
 * // We can revert search preferences to the default values now
 * client.setPageSize(DEFAULT_PAGE_SIZE); }); }
 * 
 *//**
	 * Demonstrates how to add a certain Custom Record into NetSuite.
	 */
/*
 * private void addCustomRecord() { SAMPLE_OPERATIONS.put(ADD_CUSTOM_RECORD, ()
 * -> { printWithEmptyLine(ENTER_CUSTOM_RECORD_DATA_FOR_ADD);
 * 
 * CustomRecord customRecord = new CustomRecord();
 * customRecord.setRecType(createRecordRef(readLine(getIndentedString(
 * CUSTOM_RECORD_TYPE_ID)).trim()));
 * customRecord.setName(readLine(getIndentedString(NAME)));
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke add() operation WriteResponse response =
 * client.callAddRecord(customRecord);
 * 
 * // Process response processCustomRecordWriteResponse(response, customRecord);
 * }); }
 * 
 *//**
	 * Demonstrates how to search for Custom Record. Since Custom Record
	 * implementations vary vastly, this search is only implemented to search on the
	 * Custom Record field 'Name'.
	 */
/*
 * private void searchCustomRecord() {
 * SAMPLE_OPERATIONS.put(SEARCH_CUSTOM_RECORD, () -> {
 * printWithEmptyLine(ENTER_CUSTOM_RECORD_DATA_FOR_SEARCH);
 * 
 * CustomRecordSearchBasic customRecordSearchBasic = new
 * CustomRecordSearchBasic();
 * customRecordSearchBasic.setRecType(createRecordRef(readLine(getIndentedString
 * (CUSTOM_RECORD_TYPE_ID)).trim()));
 * 
 * final String name = readLine(getIndentedString(NAME)); if
 * (isEmptyString(name)) { printInfoWithEmptyLine(NO_SEARCH_CRITERIA_DEFINED); }
 * else { SearchStringField nameField = new SearchStringField();
 * nameField.setOperator(SearchStringFieldOperator.contains);
 * nameField.setSearchValue(name); customRecordSearchBasic.setName(nameField); }
 * 
 * // Set smaller page size in order to demonstrate how searchMoreWithId()
 * operation works client.setPageSize(PAGE_SIZE);
 * 
 * printSendingRequestMessage();
 * 
 * // Search for all matching Custom Records SearchResult searchResult =
 * client.callSearch(customRecordSearchBasic); final String jobId =
 * client.getLastJobId();
 * 
 * processSearchResult(searchResult);
 * 
 * // Get next pages of the search result if
 * (isSuccessfulSearchResult(searchResult)) { for (int i = 2; i <=
 * searchResult.getTotalPages(); i++) { printSendingRequestMessage();
 * processSearchResult(client.callSearchMoreWithId(jobId, i)); } }
 * 
 * // We can revert search preferences to the default values now
 * client.setPageSize(DEFAULT_PAGE_SIZE); }); }
 * 
 *//**
	 * Demonstrates how to delete a Custom Record from NetSuite.
	 */
/*
 * private void deleteCustomRecord() {
 * SAMPLE_OPERATIONS.put(DELETE_CUSTOM_RECORD, () -> {
 * printWithEmptyLine(ENTER_CUSTOM_RECORD_DATA_FOR_DELETION);
 * 
 * CustomRecordRef customRecordRef = new CustomRecordRef(); // Prompt user for
 * the internal ID of the Custom Record type
 * customRecordRef.setTypeId(readLine(getIndentedString(CUSTOM_RECORD_TYPE_ID)))
 * ; // Prompt user for internal ID of Custom Record to be deleted
 * customRecordRef.setInternalId(readLine(getIndentedString(INTERNAL_ID)));
 * 
 * printSendingRequestMessage();
 * 
 * // Delete record WriteResponse response =
 * client.callDeleteRecord(customRecordRef, null);
 * 
 * // Process response processCustomRecordWriteResponse(response); }); }
 * 
 *//**
	 * Demonstrates how to get a list of values for an other list type in NetSuite
	 * using the {@code getAll()} operation.
	 */
/*
 * private void getOtherListValues() {
 * SAMPLE_OPERATIONS.put(GET_OTHER_LIST_VALUES, () -> { Map<String,
 * GetAllRecordType> options = new LinkedHashMap<>(4);
 * options.put(BUDGET_CATEGORIES, GetAllRecordType.budgetCategory);
 * options.put(CAMPAIGN_CATEGORIES, GetAllRecordType.campaignCategory);
 * options.put(STATES, GetAllRecordType.state); options.put(CURRENCIES,
 * GetAllRecordType.currency);
 * 
 * // Display list of choices to the user OptionList<GetAllRecordType>
 * optionList = new OptionList<>(SELECT_LIST_TYPE, options); GetAllRecordType
 * selectedRecordType = optionList.displayAndGetUserChoice();
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke getAll() operation GetAllResult allRerults =
 * client.callGetAllRecords(selectedRecordType);
 * 
 * if(! allRerults.getStatus().isIsSuccess()) {
 * printError(allRerults.getStatus().getStatusDetail(0).getMessage()); return; }
 * List<Record> allRecords =
 * Arrays.asList(allRerults.getRecordList().getRecord());
 * 
 * if (allRecords == null || allRecords.isEmpty()) {
 * printWithEmptyLine(NO_RECORDS_FOUND); } else { String stringType =
 * options.entrySet().stream() .filter(entry ->
 * entry.getValue().equals(selectedRecordType)) .map(Map.Entry::getKey)
 * .toArray(String[]::new)[0]; printWithEmptyLine(REQUESTED_LIST_OF_RECORDS,
 * stringType, allRecords.size());
 * allRecords.forEach(PrintUtils::printGetAllRecord); } }); }
 * 
 *//**
	 * Demonstrates how to upload a file from a computer using the {@code add()}
	 * operation.
	 */
/*
 * private void uploadFile() { SAMPLE_OPERATIONS.put(UPLOAD_FILE, () -> {
 * printEmptyLine();
 * 
 * File file = new File(); file.setAttachFrom(FileAttachFrom._computer);
 * 
 * // Prompt for the file name and load the file content Path filePath = null;
 * try { filePath = Paths.get(readLine(FILE_TO_BE_UPLOADED));
 * file.setContent(Files.readAllBytes(filePath)); } catch (IOException e) {
 * printError(e.getClass().getSimpleName() + ": " +
 * filePath.toAbsolutePath().toString()); return; }
 * 
 * // Prompt for the NetSuite file name file.setName(readLine(FILE_NAME));
 * 
 * // Prompt for the folder internal ID
 * file.setFolder(createRecordRef(readLine(FOLDER_INTERNAL_ID).trim()));
 * 
 * printSendingRequestMessage();
 * 
 * // Invoke add() operation to upload the file to NetSuite WriteResponse
 * response = client.callAddRecord(file);
 * 
 * // Process the response processFileUploadWriteResponse(response); }); }
 * 
 *//**
	 * Demonstrates how to get a list of possible values for Select field in
	 * NetSuite using {@code getSelectValue()} operation.
	 */
/*
 * private void getSelectFieldValues() {
 * SAMPLE_OPERATIONS.put(GET_SELECT_FIELD_VALUES, () -> { Map<String,
 * RecordType> options = new LinkedHashMap<>(); options.put(CUSTOMER,
 * RecordType.customer); options.put(VENDOR, RecordType.vendor);
 * options.put(INVENTORY_ITEM, RecordType.inventoryItem); options.put(ACCOUNT,
 * RecordType.account);
 * 
 * GetSelectValueFieldDescription fieldDescription = new
 * GetSelectValueFieldDescription(); fieldDescription.setRecordType(new
 * OptionList<>(SELECT_RECORD_TYPE_OF_THE_FIELD,
 * options).displayAndGetUserChoice()); printEmptyLine(); String fieldName =
 * readLine(WRITE_FIELD_NAME, SUBSIDIARY); fieldDescription.setField(fieldName);
 * 
 * printSendingRequestMessage();
 * 
 * List<BaseRef> values = client.getSelectValue(fieldDescription);
 * 
 * if (values == null || values.isEmpty()) {
 * printWithEmptyLine(NO_VALUES_FOUND_FOR_THE_FIELD, fieldName); } else {
 * printWithEmptyLine(VALUES_FOUND_FOR_THE_FIELD, values.size(), fieldName);
 * values.forEach(baseRef -> { RecordRef recordRef = (RecordRef) baseRef; Fields
 * fields = new Fields(); fields.put(INTERNAL_ID, recordRef.getInternalId());
 * fields.put(NAME, recordRef.getName().replaceAll(NON_BREAKING_SPACE, SPACE));
 * if (recordRef.getType() != null) { fields.put(TYPE,
 * recordRef.getType().toString()); } printMapInline(fields); }); } }); }
 * 
 * 
 * /////////////////////////////////// Auxiliary methods
 * ///////////////////////////////////
 * 
 * private static void setCustomerAddress(Customer customer, Address address,
 * String addressBookLabel, boolean defaultBilling, boolean defaultShipping) {
 * // Populate the address list for the customer. You can put in as many
 * addresses as you like. CustomerAddressbook addressBook = new
 * CustomerAddressbook(); addressBook.setDefaultBilling(defaultBilling);
 * addressBook.setDefaultShipping(defaultShipping);
 * addressBook.setLabel(addressBookLabel);
 * addressBook.setAddressbookAddress(address);
 * 
 * // Attach the address to the customer CustomerAddressbookList addressBookList
 * = customer.getAddressbookList(); if (addressBookList == null) {
 * addressBookList = new CustomerAddressbookList(); } CustomerAddressbook[]
 * addressBooks = addressBookList.getAddressbook(); if (addressBooks == null ||
 * addressBooks.length == 0) { addressBooks = new
 * CustomerAddressbook[]{addressBook}; } else { addressBooks =
 * Arrays.copyOf(addressBooks, addressBooks.length + 1); }
 * addressBookList.setAddressbook(addressBooks);
 * customer.setAddressbookList(addressBookList); }
 * 
 * private static SalesOrderItemList getSalesOrderItemList(List<String>
 * itemsInternalIds) { List<SalesOrderItem> salesOrderItems = new
 * ArrayList<>(itemsInternalIds.size());
 * 
 * // Create the sales order items and populate the quantity for (String
 * internalId : itemsInternalIds) { SalesOrderItem item = new SalesOrderItem();
 * item.setItem(createRecordRef(internalId)); String number = null; try { number
 * = readLine(getIndentedString(format(ENTER_QUANTITY_FOR_ITEM, internalId)));
 * item.setQuantity(Double.parseDouble(number)); } catch (NumberFormatException
 * nfe) { printError(format(INVALID_NUMBER, number) + SPACE +
 * ITEM_NOT_ADDED_TO_SALES_ORDER); continue; } item.setAmount(1.0);
 * 
 * salesOrderItems.add(item); }
 * 
 * SalesOrderItemList salesOrderItemList = new SalesOrderItemList();
 * salesOrderItemList.setItem(salesOrderItems.toArray(new
 * SalesOrderItem[salesOrderItems.size()]));
 * 
 * return salesOrderItemList; }
 * 
 *//**
	 * Search for all customers whose names contains string entered by the user.
	 * Since we need just an internal ID of Customer record it is much faster to use
	 * advanced search for this particular purpose.
	 *
	 * @param customerName Customer's name
	 *//*
		 * private List<Customer> searchForCustomers(String customerName) throws
		 * RemoteException { // Setting criteria SearchStringField entityId = new
		 * SearchStringField();
		 * entityId.setOperator(SearchStringFieldOperator.contains);
		 * entityId.setSearchValue(customerName);
		 * 
		 * CustomerSearchBasic customerSearchBasic = new CustomerSearchBasic();
		 * customerSearchBasic.setEntityId(entityId);
		 * 
		 * CustomerSearch customerSearch = new CustomerSearch();
		 * customerSearch.setBasic(customerSearchBasic);
		 * 
		 * // Setting returned columns CustomerSearchRowBasic customerSearchRowBasic =
		 * new CustomerSearchRowBasic(); customerSearchRowBasic.setInternalId(new
		 * SearchColumnSelectField[]{new SearchColumnSelectField()});
		 * 
		 * CustomerSearchRow customerSearchRow = new CustomerSearchRow();
		 * customerSearchRow.setBasic(customerSearchRowBasic);
		 * 
		 * CustomerSearchAdvanced customerSearchAdvanced = new CustomerSearchAdvanced();
		 * customerSearchAdvanced.setCriteria(customerSearch);
		 * customerSearchAdvanced.setColumns(customerSearchRow);
		 * 
		 * printSendingRequestMessage();
		 * 
		 * List<?> returnedRows = client.searchAll(customerSearchAdvanced);
		 * 
		 * if (returnedRows == null || returnedRows.isEmpty()) { return
		 * Collections.emptyList(); }
		 * 
		 * // Convert returned rows to list of customers return
		 * returnedRows.stream().map(row -> { CustomerSearchRowBasic searchRow =
		 * ((CustomerSearchRow) row).getBasic(); Customer customer = new Customer();
		 * customer.setInternalId(searchRow.getInternalId(0).getSearchValue().
		 * getInternalId()); return customer; }).collect(Collectors.toList()); }
		 * 
		 * private boolean isSuccessfulSearchResult(SearchResult searchResult) { return
		 * searchResult.getStatus() != null && searchResult.getStatus().isIsSuccess(); }
		 * }
		 */