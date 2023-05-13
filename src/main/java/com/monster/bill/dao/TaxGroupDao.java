package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.TaxGroup;
import com.monster.bill.payload.request.PaginationRequest;

@Component("taxGroupDao")
public interface TaxGroupDao {
	
	public List<TaxGroup> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}
