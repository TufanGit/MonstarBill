package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Currency;
import com.monster.bill.models.CurrencyHistory;

public interface CurrencyService {
	
	public Currency getCurrencyById(Long id);
	
	public List<CurrencyHistory> findHistoryById(Long id, Pageable pageable);
	
	public List<Currency> save(List<Currency> currencies);

	public Boolean getValidateName(String name);

	public List<Currency> getAllCurrencies();

}
