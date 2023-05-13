package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Grn;
import com.monster.bill.payload.request.PaginationRequest;

@Component("grnDao")
public interface GrnDao {
	
	public List<Grn> findAll(String whereClause, PaginationRequest paginationRequest);
	public Long getCount(String whereClause);

}




