package com.monster.bill.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monster.bill.common.AppConstants;
import com.monster.bill.common.CommonUtils;
import com.monster.bill.common.CustomException;
import com.monster.bill.common.CustomMessageException;
import com.monster.bill.common.FilterNames;
import com.monster.bill.dao.GrnDao;
import com.monster.bill.enums.FormNames;
import com.monster.bill.enums.Operation;
import com.monster.bill.models.Grn;
import com.monster.bill.models.GrnHistory;
import com.monster.bill.models.GrnItem;
import com.monster.bill.models.Item;
import com.monster.bill.models.PurchaseOrderItem;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.repository.GrnHistoryRepository;
import com.monster.bill.repository.GrnItemRepository;
import com.monster.bill.repository.GrnRepository;
import com.monster.bill.repository.ItemRepository;
import com.monster.bill.repository.PurchaseOrderItemRepository;
import com.monster.bill.service.DocumentSequenceService;
import com.monster.bill.service.GrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class GrnServiceImpl implements GrnService {

	@Autowired
	GrnRepository grnRepository;

	@Autowired
	private GrnItemRepository grnItemRepository;

	@Autowired
	private GrnDao grnDao;
	
	@Autowired
	private PurchaseOrderItemRepository purchaseOrderItemRepository;

	@Autowired
	private GrnHistoryRepository grnHistoryRepository;
	
	@Autowired
	private DocumentSequenceService documentSequenceService;
	
	@Autowired
	private ItemRepository itemRepository;

	@Override
	public List<Grn> save(List<Grn> grns) {
		for (Grn grn : grns) {
			Long grnId = null;
			String poNumber = null;
			String grnNumber = null;
			Optional<Grn> oldGrn = Optional.empty();
			if (grn.getId() == null) {
				grn.setCreatedBy(CommonUtils.getLoggedInUsername());
				String transactionalDate = CommonUtils.convertDateToFormattedString(grn.getGrnDate());
				String documentSequenceNumber = this.documentSequenceService.getDocumentSequenceNumbers(transactionalDate, grn.getSubsidiaryId(), FormNames.GRN.getFormName(), false);
				if (StringUtils.isEmpty(documentSequenceNumber)) {
					throw new CustomMessageException("Please validate your configuration to generate the PO Number");
				}
				 grn.setGrnNumber(documentSequenceNumber);
			} else {
				// Get the existing object using the deep copy
				oldGrn = this.grnRepository.findByIdAndIsDeleted(grn.getId(), false);
				if (oldGrn.isPresent()) {
					try {
						oldGrn = Optional.ofNullable((Grn) oldGrn.get().clone());
					} catch (CloneNotSupportedException e) {
						log.error("Error while Cloning the object. Please contact administrator.");
						throw new CustomException("Error while Cloning the object. Please contact administrator.");
					}
				}
			}
			grn.setLastModifiedBy(CommonUtils.getLoggedInUsername());
			Grn savedGrn;
			try {
				savedGrn = this.grnRepository.save(grn);
			}  catch (DataIntegrityViolationException e) {
				log.error(" Grn unique constrain violetd." + e.getMostSpecificCause());
				throw new CustomException("Grn unique constrain violetd :" + e.getMostSpecificCause());
			}
			log.info("GRN is updated successfully" + savedGrn);
			grnId = savedGrn.getId();
			poNumber = savedGrn.getPoNumber();
			log.info("reatriving the ID of saved GRN" + grnId);
			this.updateGrnHistory(savedGrn, oldGrn);
			// -------saving the item---------
			List<GrnItem> grnItems = grn.getGrnItem();
			PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();

			if (CollectionUtils.isNotEmpty(grnItems)) {
				for (GrnItem grnItem : grnItems) {
					this.saveGrnItem(grnId, grnItem, poNumber, grnNumber);
					purchaseOrderItem = this.purchaseOrderItemRepository.findByPoIdAndItemIdAndId(grnItem.getPoId(), grnItem.getItemId(),grnItem.getPoiId());
					Double remainQuantity = 0.0;
					remainQuantity = this.grnItemRepository.findReciveQuantityByPoIdAndItemId(grnItem.getGrnId(), grnItem.getItemId());
					//remainAmount = grnItem.getRemainQuantity() + remainAmount;
//					log.info("total remain quantity " + remainAmount);
					if(remainQuantity==null) {
						remainQuantity =0.0;
					}
					if(purchaseOrderItem == null) {
						log.error(" combination if purchase order id, item id, purchase order item id is incorrect to save grn " +purchaseOrderItem );
						throw new CustomException(" combination if purchase order id, item id, purchase order item id is incorrect to save grn  " +purchaseOrderItem );
					}
					//if(grnItem.getRemainQuantity()==null) {
						purchaseOrderItem.setRemainQuantity(grnItem.getQuantity() - grnItem.getReciveQuantity());
						grnItem.setRemainQuantity(grnItem.getQuantity() - grnItem.getReciveQuantity());
						this.purchaseOrderItemRepository.save(purchaseOrderItem);
					//}
//					if(grnItem.getRemainQuantity()!=null) {
//					purchaseOrderItem.setRemainQuantity(grnItem.getRemainQuantity() - grnItem.getReciveQuantity() + remainQuantity);
//					grnItem.setRemainQuantity(grnItem.getRemainQuantity() - grnItem.getReciveQuantity() + remainQuantity);
//					this.purchaseOrderItemRepository.save(purchaseOrderItem);
//					}
				}
			}
			System.gc();
			savedGrn = this.getGrnById(savedGrn.getId());
		}
		return grns;

	}

	private void updateGrnHistory(Grn grn, Optional<Grn> oldGrn) {
		if (oldGrn.isPresent()) {
			// insert the updated fields in history table
			List<GrnHistory> grnHistories = new ArrayList<GrnHistory>();
			try {
				grnHistories = oldGrn.get().compareFields(grn);
				if (CollectionUtils.isNotEmpty(grnHistories)) {
					this.grnHistoryRepository.saveAll(grnHistories);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.error("Error while comparing the new and old objects. Please contact administrator.");
				throw new CustomException(
						"Error while comparing the new and old objects. Please contact administrator.");
			}
			log.info("GRN History is updated successfully");
		} else {
			// Insert in history table as Operation - INSERT
			this.grnHistoryRepository.save(this.prepareGrnHistory(grn.getGrnNumber(), null, FormNames.GRN.getFormName(),
					Operation.CREATE.toString(), grn.getLastModifiedBy(), null, grn.getGrnNumber()));
		}
		log.info("GRN History is Completed.");
	}

	/**
	 * Prepares the grn history object
	 * 
	 * @param grnNumber
	 * @param childId
	 * @param moduleName
	 * @param operation
	 * @param lastModifiedBy
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public GrnHistory prepareGrnHistory(String grnNumber, Long childId, String moduleName, String operation,
			String lastModifiedBy, String oldValue, String newValue) {
		GrnHistory grnHistory = new GrnHistory();
		grnHistory.setGrnNumber(grnNumber);
		grnHistory.setChildId(childId);
		grnHistory.setModuleName(moduleName);
		grnHistory.setChangeType(AppConstants.UI);
		grnHistory.setOperation(operation);
		grnHistory.setOldValue(oldValue);
		grnHistory.setNewValue(newValue);
		grnHistory.setLastModifiedBy(lastModifiedBy);
		return grnHistory;

	}

	private void saveGrnItem(Long grnId, GrnItem grnItem, String poNumber, String grnNumber) {
		Optional<GrnItem> oldGrnItem = Optional.empty();
		if (grnItem.getId() == null) {
			grnItem.setCreatedBy(CommonUtils.getLoggedInUsername());
		} else {
			// Get the existing object using the deep copy
			oldGrnItem = this.grnItemRepository.findByIdAndIsDeleted(grnItem.getId(), false);
			if (oldGrnItem.isPresent()) {
				try {
					oldGrnItem = Optional.ofNullable((GrnItem) oldGrnItem.get().clone());
				} catch (CloneNotSupportedException e) {
					log.error("Error while Cloning the object. Please contact administrator.");
					throw new CustomException("Error while Cloning the object. Please contact administrator.");
				}
			}
		}
		grnItem.setGrnId(grnId);
		grnItem.setPoNumber(poNumber);
		grnItem.setGrnNumber(grnNumber);
		grnItem.setLastModifiedBy(CommonUtils.getLoggedInUsername());
//		if(grnItem.getReciveQuantity() > grnItem.getRemainQuantity()) {
//			log.error(" reciving quantity cannot be greater than remaining quantity ");
//			throw new CustomException("reciving quantity cannot be greater than remaining quantity .");
//		}
		
		GrnItem grnItemSaved = this.grnItemRepository.save(grnItem);
		if (grnItemSaved == null) {
			log.info("Error while Saving the Item list in GRN.");
			throw new CustomMessageException("Error while Saving the Item list in GRN.");
		}
		// update the data in Item history table
		this.updateGrnItemHistory(grnItemSaved, oldGrnItem);

	}

	private void updateGrnItemHistory(GrnItem grnItem, Optional<GrnItem> oldGrnItem) {
		if (oldGrnItem.isPresent()) {
			// insert the updated fields in history table
			List<GrnHistory> grnHistories = new ArrayList<GrnHistory>();
			try {
				grnHistories = oldGrnItem.get().compareFields(grnItem);
				if (CollectionUtils.isNotEmpty(grnHistories)) {
					this.grnHistoryRepository.saveAll(grnHistories);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.error("Error while comparing the new and old objects. Please contact administrator.");
				throw new CustomException(
						"Error while comparing the new and old objects. Please contact administrator.");
			}
			log.info("GRN Item History is updated successfully");
		} else {
			// Insert in history table as Operation - INSERT
			this.grnHistoryRepository.save(this.prepareGrnHistory(grnItem.getGrnNumber(), grnItem.getId(),
					"GRN Item", Operation.CREATE.toString(), grnItem.getLastModifiedBy(), null,
					String.valueOf(grnItem.getId())));
		}
	}

	@Override
	public PaginationResponse findAll(PaginationRequest paginationRequest) {
		List<Grn> grn = new ArrayList<Grn>();

		// preparing where clause
		String whereClause = this.prepareWhereClause(paginationRequest).toString();

		// get list
		grn = this.grnDao.findAll(whereClause, paginationRequest);

		// getting count
		Long totalRecords = this.grnDao.getCount(whereClause);

		return CommonUtils.setPaginationResponse(paginationRequest.getPageNumber(), paginationRequest.getPageSize(),
				grn, totalRecords);
	}

	private Object prepareWhereClause(PaginationRequest paginationRequest) {
		Long subsidiaryId = null;
		Long supplierId = null;
		Long locationId = null;
		Map<String, ?> filters = paginationRequest.getFilters();

		if (filters.containsKey(FilterNames.SUBSIDIARY_ID))
			subsidiaryId = ((Number) filters.get(FilterNames.SUBSIDIARY_ID)).longValue();
		if (filters.containsKey(FilterNames.SUPPLIER_ID))
			supplierId = ((Number) filters.get(FilterNames.SUPPLIER_ID)).longValue();
		if (filters.containsKey(FilterNames.LOCATION_ID))
			locationId = ((Number) filters.get(FilterNames.LOCATION_ID)).longValue();

		StringBuilder whereClause = new StringBuilder(" AND gr.isDeleted is false");
		if (subsidiaryId != null && subsidiaryId != 0) {
			whereClause.append(" AND gr.subsidiaryId = ").append(subsidiaryId);
		}
		if (supplierId != null && supplierId != 0) {
			whereClause.append(" AND gr.supplierId = ").append(supplierId);
		}
		if (locationId != null && locationId != 0) {
			whereClause.append(" AND gr.locationId = ").append(locationId);
		}
		return whereClause;
	}

	@Override
	public Grn getGrnById(Long id) {
		Optional<Grn> grn = Optional.empty();
		grn = grnRepository.findByIdAndIsDeleted(id, false);
		if (grn.isPresent()) {
			Long grnId = grn.get().getId();
			log.info("GRN found against given id : " + id);
			// Get Item
			List<GrnItem> grnItems = grnItemRepository.findByGrnId(grnId);
			log.info("GRN items found against given id : " + grnItems);
			if (CollectionUtils.isNotEmpty(grnItems)) {
				grn.get().setGrnItem(grnItems);
			}
		} else {
			log.error("GRN Not Found against given GRN id : " + id);
			throw new CustomMessageException("GRN Not Found against given GRN id : " + id);
		}

		return grn.get();
	}

	@Override
	public boolean deleteById(Long id) {
		Grn grn = new Grn();
		grn = this.getGrnById(id);
		grn.setDeleted(true);
		grn = this.grnRepository.save(grn);
		if (grn == null) {
			log.error("Error while deleting the GRN : " + id);
			throw new CustomMessageException("Error while deleting the GRN : " + id);
		}
		// update the operation in the history
		this.grnHistoryRepository.save(this.prepareGrnHistory(grn.getGrnNumber(), null, FormNames.GRN.getFormName(),
				Operation.DELETE.toString(), grn.getLastModifiedBy(), grn.getGrnNumber(), null));
		return true;
	}

	@Override
	public List<GrnHistory> findHistoryById(String grnNumber, Pageable pageable) {
		List<GrnHistory> grnHistories =  new ArrayList<GrnHistory>();
		grnHistories = this.grnHistoryRepository.findByGrnNumber(grnNumber, pageable);
		log.info("GRN history is running succesfully " + grnHistories);
		 return grnHistories;
	}

	@Override
	public List<GrnItem> findGrnItemsByGrnNumber(String grnNumber) {
		List<GrnItem> grnItems = this.grnItemRepository.findItemsByGrnNumber(grnNumber);
		log.info("GRN items found against given id : " + grnNumber);
		if (CollectionUtils.isEmpty(grnItems)) {
			log.error("GRN Not Found against given GRN id : " + grnNumber);
			throw new CustomMessageException("GRN Not Found against given GRN id : " + grnNumber);
		}
		return grnItems;
	}
	

	@Override
	public Grn getByGrnId(Long grnId) {
		Optional<Grn> grn = grnRepository.findByIdAndIsDeleted(grnId, false);		
		if (grn.isPresent()) {
			List<GrnItem> grnItems = grnItemRepository.findByGrnIdAndIsDeleted(grnId, false);
			if (CollectionUtils.isNotEmpty(grnItems)) {
				grnItems.forEach(c -> {
					Item item = itemRepository.findByIdAndIsDeleted(c.getItemId(), false).get();
					c.setItemName(item.getName());
				});	
			}
			grn.get().setGrnItem(grnItems);
		}
		return grn.get();
	}

	@Override
	public GrnItem getByGrnItemId(Long grnId, Long itemId) {
		GrnItem grnItem = grnItemRepository.findByGrnIdAndItemIdAndIsDeleted(grnId, itemId, false);
		return grnItem;
	}

	@Override
	public List<Grn> getByPoId(Long poId) {
		return grnRepository.findByPoIdAndIsDeleted(poId, false);
	}
	
}
