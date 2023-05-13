package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Account;
import com.monster.bill.models.AccountHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface AccountService {
	
	public Account save(Account account);

	public Account getAccountById(Long id);
	
	public boolean deleteById(Long id);
	
	public List<AccountHistory> findHistoryById(Long id, Pageable pageable);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public List<Account> getAccountsByType(String type);

	public List<Account> getParentAccounts();

	public List<Account> findBySubsidiaryIdAndType(Long subsidiaryId, List<String> type);
	
}
