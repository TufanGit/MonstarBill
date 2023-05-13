package com.monster.bill.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
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
import com.monster.bill.dao.DebitNoteDao;
import com.monster.bill.enums.Operation;
import com.monster.bill.models.DebitNote;
import com.monster.bill.models.DebitNoteHistory;
import com.monster.bill.models.DebitNoteItem;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.repository.DebitNoteHistoryRepository;
import com.monster.bill.repository.DebitNoteItemRepository;
import com.monster.bill.repository.DebitNoteRepository;
import com.monster.bill.service.DebitNoteService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class DebitNoteServiceImpl implements DebitNoteService {

	@Autowired
	private DebitNoteRepository debitNoteRepository;

	@Autowired
	private DebitNoteItemRepository debitNoteItemRepository;

	@Autowired
	private DebitNoteHistoryRepository debitNoteHistoryRepository;

	@Autowired
	private DebitNoteDao debitNoteDao;

	@Override
	public DebitNote save(DebitNote debitNote) {

		Optional<DebitNote> oldDebitNote = Optional.empty();
		String username = CommonUtils.getLoggedInUsername();

		try {
			// 1. save the debitNote
			if (debitNote.getId() == null) {
				debitNote.setCreatedBy(username);
			} else {
				// Get the existing object using the deep copy
				oldDebitNote = this.debitNoteRepository.findByIdAndIsDeleted(debitNote.getId(), false);
				if (oldDebitNote.isPresent()) {
					try {
						oldDebitNote = Optional.ofNullable((DebitNote) oldDebitNote.get().clone());
					} catch (CloneNotSupportedException e) {
						log.error("Error while Cloning the object. Please contact administrator.");
						throw new CustomException("Error while Cloning the object. Please contact administrator.");
					}
				}
			}

			debitNote.setLastModifiedBy(username);
			DebitNote savedDebitNote;
			try {
				savedDebitNote = this.debitNoteRepository.save(debitNote);
			} catch (DataIntegrityViolationException e) {
				log.error("Debit note unique constrain violetd." + e.getMostSpecificCause());
				throw new CustomException("Debit note unique constrain violetd :" + e.getMostSpecificCause());
			}
			log.info("Debit note is saved successfully.");

			this.updateDebitNoteHistory(oldDebitNote, savedDebitNote);
			log.info("Debit note History is saved successfully.");

			// ----------------------------------- debit Note Item Started-------------------------------------------------
			log.info("Save debitNote Item Started...");
			List<DebitNoteItem> debitNoteItems = debitNote.getDebitNoteItem();
			if (CollectionUtils.isNotEmpty(debitNoteItems)) {
				Long debitNoteId = savedDebitNote.getId();
				String debitNoteNumber = savedDebitNote.getDebitNoteNumber();

				for (DebitNoteItem debitNoteItem : debitNoteItems) {
					debitNoteItem.setDebitNoteId(debitNoteId);
					debitNoteItem.setDebitNoteNumber(debitNoteNumber);
					this.saveDebitNoteItem(debitNoteItem);
				}
			}
			log.info("Save debitNote Item Finished...");
			// ----------------------------------- debitNote Item Finished -------------------------------------------------
			System.gc();
			savedDebitNote = this.getDebitNoteById(savedDebitNote.getId());
			return savedDebitNote;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.toString());
		}
	}

	public DebitNoteItem saveDebitNoteItem(DebitNoteItem debitNoteItem) {
		Optional<DebitNoteItem> oldDebitNoteItem = Optional.empty();

		if (debitNoteItem.getId() == null) {
			debitNoteItem.setCreatedBy(CommonUtils.getLoggedInUsername());
		} else {
			// Get existing address using deep copy
			oldDebitNoteItem = this.debitNoteItemRepository.findByIdAndIsDeleted(debitNoteItem.getId(), false);
			if (oldDebitNoteItem.isPresent()) {
				try {
					oldDebitNoteItem = Optional.ofNullable((DebitNoteItem) oldDebitNoteItem.get().clone());
				} catch (CloneNotSupportedException e) {
					log.error("Error while Cloning the object. Please contact administrator.");
					throw new CustomException("Error while Cloning the object. Please contact administrator.");
				}
			}
		}
		debitNoteItem.setLastModifiedBy(CommonUtils.getLoggedInUsername());
		debitNoteItem = this.debitNoteItemRepository.save(debitNoteItem);
		
		if (debitNoteItem == null) {
			log.info("Error while Saving the debit Note Item.");
			throw new CustomMessageException("Error while Saving the debit Note Item.");
		}
		log.info("Debit note item is saved successfully.");
		
		// find the updated values and save to history table
		if (oldDebitNoteItem.isPresent()) {
			if (debitNoteItem.isDeleted()) {
				this.debitNoteHistoryRepository.save(this.prepareDebitNoteHistory(debitNoteItem.getDebitNoteNumber(),
						debitNoteItem.getId(), AppConstants.DEBIT_NOTE_ITEM, Operation.DELETE.toString(),
						debitNoteItem.getLastModifiedBy(), String.valueOf(debitNoteItem.getId()), null));
			} else {
				List<DebitNoteHistory> debitNoteHistories = new ArrayList<DebitNoteHistory>();
				try {
					debitNoteHistories = oldDebitNoteItem.get().compareFields(debitNoteItem);
					if (CollectionUtils.isNotEmpty(debitNoteHistories)) {
						this.debitNoteHistoryRepository.saveAll(debitNoteHistories);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Error while comparing the new and old objects. Please contact administrator.");
					throw new CustomException("Error while comparing the new and old objects. Please contact administrator.");
				}
			}
		} else {
			this.debitNoteHistoryRepository.save(this.prepareDebitNoteHistory(debitNoteItem.getDebitNoteNumber(),
					debitNoteItem.getId(), AppConstants.DEBIT_NOTE_ITEM, Operation.CREATE.toString(),
					debitNoteItem.getLastModifiedBy(), null, String.valueOf(debitNoteItem.getId())));
		}
		log.info("Debit Note Item History is updated successfully.");
		return debitNoteItem;
	}

	/**
	 * 18-Oct-2022 Prepares the history objects for all forms and their
	 * child. Use this if you need single object only
	 * 
	 * @param moduleName
	 * @param operation
	 * @param lastModifiedBy
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public DebitNoteHistory prepareDebitNoteHistory(String debitNoteNumber, Long childId, String moduleName, String operation,
			String lastModifiedBy, String oldValue, String newValue) {
		DebitNoteHistory debitNoteHistory = new DebitNoteHistory();
		debitNoteHistory.setDebitNoteNumber(debitNoteNumber);
		debitNoteHistory.setChildId(childId);
		debitNoteHistory.setModuleName(moduleName);
		debitNoteHistory.setChangeType(AppConstants.UI);
		debitNoteHistory.setOperation(operation);
		debitNoteHistory.setOldValue(oldValue);
		debitNoteHistory.setNewValue(newValue);
		debitNoteHistory.setLastModifiedBy(lastModifiedBy);
		return debitNoteHistory;
	}

	// update history of debitNote
	private void updateDebitNoteHistory(Optional<DebitNote> oldDebitNote, DebitNote debitNote) {
		if (debitNote != null) {
			if (oldDebitNote.isPresent()) {
				// insert the updated fields in history table
				List<DebitNoteHistory> debitNoteHistories = new ArrayList<DebitNoteHistory>();
				try {
					debitNoteHistories = oldDebitNote.get().compareFields(debitNote);
					if (CollectionUtils.isNotEmpty(debitNoteHistories)) {
						this.debitNoteHistoryRepository.saveAll(debitNoteHistories);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Error while comparing the new and old objects. Please contact administrator.");
					throw new CustomException(
							"Error while comparing the new and old objects. Please contact administrator.");
				}
			} else {
				// Insert in history table as Operation - INSERT
				this.debitNoteHistoryRepository.save(this.prepareDebitNoteHistory(debitNote.getDebitNoteNumber(), null,
						AppConstants.SUPPLIER, Operation.CREATE.toString(), debitNote.getLastModifiedBy(), null, null));
			}
			log.info("DebitNote History Saved successfully.");
		} else {
			log.error("Error while saving the debit Note.");
			throw new CustomException("Error while saving the debit Note.");
		}
	}

	@Override
	public DebitNote getDebitNoteById(Long id) {
		Optional<DebitNote> debitNote = Optional.ofNullable(new DebitNote());
		debitNote = debitNoteRepository.findByIdAndIsDeleted(id, false);

		if (debitNote.isPresent()) {
			List<DebitNoteItem> debitNoteItems = debitNoteItemRepository.findByDebitNoteIdAndIsDeleted(id, false);
			if (CollectionUtils.isNotEmpty(debitNoteItems)) {
				debitNote.get().setDebitNoteItem(debitNoteItems);
			}
		} else {
			log.error("Debit Note Not Found against given debit Note id : " + id);
			throw new CustomMessageException("Debit Note Not Found against given debit Note id : " + id);
		}

		return debitNote.get();
	}

	@Override
	public List<DebitNoteHistory> findAuditByDebitNoteNumber(String debitNoteNumber, Pageable pageable) {
		return debitNoteHistoryRepository.findByDebitNoteNumber(debitNoteNumber, pageable);
	}

	public PaginationResponse findAll(PaginationRequest paginationRequest) {
		List<DebitNote> debitNotes = new ArrayList<DebitNote>();

		// preparing where clause
		String whereClause = this.prepareWhereClause(paginationRequest).toString();

		// get list
		debitNotes = this.debitNoteDao.findAll(whereClause, paginationRequest);

		// getting count
		Long totalRecords = this.debitNoteDao.getCount(whereClause);

		return CommonUtils.setPaginationResponse(paginationRequest.getPageNumber(), paginationRequest.getPageSize(),
				debitNotes, totalRecords);
	}

	private StringBuilder prepareWhereClause(PaginationRequest paginationRequest) {
		Map<String, ?> filters = paginationRequest.getFilters();

		Long subsidiaryId = null;
		Long supplierId = null;

		if (filters.containsKey(FilterNames.SUBSIDIARY_ID))
			subsidiaryId = ((Number) filters.get(FilterNames.SUBSIDIARY_ID)).longValue();
		if (filters.containsKey(FilterNames.SUPPLIER_ID))
			supplierId = ((Number) filters.get(FilterNames.SUPPLIER_ID)).longValue();

		StringBuilder whereClause = new StringBuilder(" AND d.isDeleted is false ");
		if (subsidiaryId != null) {
			whereClause.append(" AND d.subsidiaryId = ").append(subsidiaryId).append(" ");
		}
		if (supplierId != null) {
			whereClause.append(" AND d.supplierId = ").append(supplierId).append(" ");
		}

		return whereClause;
	}
}
