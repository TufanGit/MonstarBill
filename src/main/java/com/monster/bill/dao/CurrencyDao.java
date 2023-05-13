package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Currency;
import com.monster.bill.payload.request.PaginationRequest;

@Component("currencyDao")
public interface CurrencyDao {
	
	public List<Currency> findAll(String whereClause, PaginationRequest paginationRequest);
	public Long getCount(String whereClause);

}




