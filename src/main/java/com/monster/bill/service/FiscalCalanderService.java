package com.monster.bill.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.FiscalCalanderAccounting;
import com.monster.bill.models.FiscalCalender;
import com.monster.bill.models.FiscalCalenderHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface FiscalCalanderService {
	
	public FiscalCalender save(FiscalCalender fiscalCalender) throws ParseException;

	public FiscalCalender getFiscalCalanderById(Long id);
	
	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public  List<FiscalCalanderAccounting> getFiscalCalanderAccounting(int startYear, int endYear, String startMonth, String endMonth)throws ParseException;

	public List<FiscalCalanderAccounting> findAccountingByFiscalId(Long fiscalId);

	public boolean deleteById(Long id);


	public List<FiscalCalanderAccounting> findAllAccounting();

	public List<FiscalCalenderHistory> findHistoryById(Long id, Pageable pageable);


}
