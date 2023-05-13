package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.MakePayment;
import com.monster.bill.payload.request.PaginationRequest;

@Component("makePaymentDao")
public interface MakePaymentDao {
	
	public List<MakePayment> findAll(String whereClause, PaginationRequest paginationRequest);
	public Long getCount(String whereClause);

}




