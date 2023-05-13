package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.monster.bill.models.PrItem;
import com.monster.bill.models.PurchaseRequisition;
import com.monster.bill.models.PurchaseRequisitionHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.IdNameResponse;
import com.monster.bill.payload.response.PaginationResponse;

public interface PurchaseRequisitionService {

	public PurchaseRequisition save(PurchaseRequisition purchaseRequisition);

	public PurchaseRequisition findById(Long id);

	public boolean deleteById(Long id);

	public List<PurchaseRequisitionHistory> findHistoryById(String prNumber, Pageable pageable);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public PrItem save(PrItem prItem);

	public boolean deletePrItemMapping(Long id);

//	public PurchaseRequisition findByPrNumber(String prNumber);

	public List<IdNameResponse> findDistinctPrNumbers();

	public List<PrItem> findUnprocessedItemsByPrId(Long prId);

	public List<IdNameResponse> findPendingPrForPo(Long subsidiaryId, Long locationId);
	
	public List<PurchaseRequisition> getPrApproval(String userId);
	
	public PaginationResponse findAllApprovedPr(PaginationRequest paginationRequest, Long subsidiaryId);

	public Boolean sendForApproval(Long id);

	public Boolean approveAllPrs(List<Long> prIds);

	public Boolean rejectAllPrs(List<PurchaseRequisition> prs);

	public PurchaseRequisitionHistory preparePurchaseRequisitionHistory(String prNumber, Long childId, String moduleName,
			String operation, String lastModifiedBy, String oldValue, String newValue);

	public List<PrItem> findUnprocessedItemsByPrIds(List<Long> prNumbers);

	public List<IdNameResponse> findApprovedPrsBySubsidiary(Long subsidiaryId);

	public Boolean updateNextApprover(Long approverId, Long prId);

	public Boolean selfApprove(Long prId);

	public byte[] downloadTemplate();

	public byte[] upload(MultipartFile file);

}
