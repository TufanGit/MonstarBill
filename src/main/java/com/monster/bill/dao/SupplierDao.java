package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Supplier;
import com.monster.bill.payload.request.PaginationRequest;

@Component("supplierDao")
public interface SupplierDao {
	
	public List<Supplier> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);
	
}
