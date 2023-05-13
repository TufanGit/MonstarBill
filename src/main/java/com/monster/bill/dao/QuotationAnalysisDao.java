package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.QuotationAnalysis;
import com.monster.bill.payload.request.PaginationRequest;

@Component("quotationAnalysisDao")
public interface QuotationAnalysisDao {

	List<QuotationAnalysis> findAll(String whereClause, PaginationRequest paginationRequest);

	Long getCount(String whereClause);
	
	
}
