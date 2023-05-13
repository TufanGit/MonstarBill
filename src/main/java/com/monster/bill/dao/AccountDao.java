package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Account;
import com.monster.bill.payload.request.PaginationRequest;

@Component("accountDao")
public interface AccountDao {
	
	public List<Account> findAll(String whereClause, PaginationRequest paginationRequest);
	public Long getCount(String whereClause);

}




