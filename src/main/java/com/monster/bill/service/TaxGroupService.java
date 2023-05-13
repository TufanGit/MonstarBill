package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.TaxRateRule;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.models.TaxGroupHistory;
import com.monster.bill.models.TaxGroup;

public interface TaxGroupService {
	
	public TaxGroup save(TaxGroup taxGroup);
	
	public TaxGroup getTaxGroupById(Long id);
	
	public PaginationResponse findAll(PaginationRequest paginationRequest);
	
	public boolean deleteById(Long id);
	
	public List<TaxGroupHistory> findHistoryById(Long id, Pageable pageable);
	
	public TaxRateRule save(TaxRateRule taxRateRule);
	
	public boolean deleteTaxRateRuleById(Long id);

	public List<TaxGroup> getTaxGroupBySubsidiaryId(Long subsidiaryId);

}
