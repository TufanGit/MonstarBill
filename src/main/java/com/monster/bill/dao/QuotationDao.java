package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Quotation;
import com.monster.bill.payload.request.PaginationRequest;

@Component("quotationDao")
public interface QuotationDao {
	
	public List<Quotation> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);
	
}
