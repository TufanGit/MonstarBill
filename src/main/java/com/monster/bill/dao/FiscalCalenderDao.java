package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.FiscalCalender;
import com.monster.bill.payload.request.PaginationRequest;

@Component("fiscalCalenderDao")
public interface FiscalCalenderDao {
	
	public List<FiscalCalender> findAll(String whereClause,  PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}




