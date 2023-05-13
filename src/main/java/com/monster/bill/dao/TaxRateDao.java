package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.TaxRate;
import com.monster.bill.payload.request.PaginationRequest;

@Component("taxRateDao")
public interface TaxRateDao {
	public List<TaxRate> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);
}
