package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.PurchaseOrder;
import com.monster.bill.payload.request.PaginationRequest;

@Component("purchaseOrderDao")
public interface PurchaseOrderDao {

	List<PurchaseOrder> findAll(String whereClause, PaginationRequest paginationRequest);

	Long getCount(String whereClause);
	
}
