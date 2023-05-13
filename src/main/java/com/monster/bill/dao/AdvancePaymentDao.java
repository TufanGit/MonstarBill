package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.AdvancePayment;
import com.monster.bill.payload.request.PaginationRequest;

@Component("advancePaymentDao")
public interface AdvancePaymentDao {

	public List<AdvancePayment> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}
