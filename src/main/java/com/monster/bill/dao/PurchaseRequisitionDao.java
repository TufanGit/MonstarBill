package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.PrItem;
import com.monster.bill.models.PurchaseRequisition;
import com.monster.bill.payload.request.PaginationRequest;

@Component("purchaseRequisitionDao")
public interface PurchaseRequisitionDao {
	public List<PurchaseRequisition> findAll(String whereClause, PaginationRequest paginationRequest);
	public Long getCount(String whereClause);
	public List<PrItem> findUnprocessedItemsByPrId(Long prId);
	public List<PurchaseRequisition> findAllApprovedPr(String whereClause, PaginationRequest paginationRequest, Long subsidiaryId);
	public Long getCountPrApproved(String whereClause, Long subsidiaryId);
}
