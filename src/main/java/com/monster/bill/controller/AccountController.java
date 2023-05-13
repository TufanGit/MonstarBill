package com.monster.bill.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monster.bill.common.CustomException;
import com.monster.bill.models.Account;
import com.monster.bill.models.AccountHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins= "*", allowedHeaders = "*", maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
	
	@Autowired
	private AccountService accountService;	
	
	/**
	 * This saves the account
	 * @param account
	 * @return
	 */
	@PostMapping("/save")
	public ResponseEntity<Account> saveAccount(@Valid @RequestBody Account account) {
		log.info("Saving the Account :: " + account.toString());
		account = accountService.save(account);
		log.info("Account saved successfully");
		return ResponseEntity.ok(account);
	}
	
	/**
	 * get Account based on it's id
	 * @param id
	 * @return Account
	 */
	@GetMapping("/get")
	public ResponseEntity<Account> getAccount(@RequestParam Long id) {
		return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
	}
	
	/**
	 * get all parents which having the account summary is true
	 * @return
	 */
	@GetMapping("/get-all-parents")
	public ResponseEntity<List<Account>> getParentAccounts() {
		return new ResponseEntity<>(accountService.getParentAccounts(), HttpStatus.OK);
	}
	
	/**
	 * get list of Accounts with/without Filter 
	 * @return
	 */
	@PostMapping("/get/all")
	public ResponseEntity<PaginationResponse> findAll(@RequestBody PaginationRequest paginationRequest) {
		log.info("Get all account started.");
		PaginationResponse paginationResponse = new PaginationResponse();
		paginationResponse = accountService.findAll(paginationRequest);
		log.info("Get all Account completed.");
		return new ResponseEntity<>(paginationResponse, HttpStatus.OK);
	}
	
	/**
	 * soft delete the Account by it's id
	 * @param id
	 * @return
	 */
	@GetMapping("/delete")
	public ResponseEntity<Boolean> deleteById(@RequestParam Long id) {
		log.info("Delete Account by ID :: " + id);
		boolean isDeleted = false;
		isDeleted = accountService.deleteById(id);
		log.info("Delete Account by ID Completed.");
		return new ResponseEntity<>(isDeleted, HttpStatus.OK);
	}
	
	/**
	 * Find history by Account Id
	 * Supported for server side pagination
	 * @param id
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	@GetMapping("/get/history")
	public ResponseEntity<List<AccountHistory>> findHistoryById(@RequestParam Long id, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "id") String sortColumn) {
		log.info("Get Account Audit  :: " + id);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortColumn));
		List<AccountHistory> accountHistoris = this.accountService.findHistoryById(id, pageable);
		log.info("Returning from Account Audit by id.");
		return new ResponseEntity<>(accountHistoris, HttpStatus.OK);
	}

	@GetMapping("/get-by-type")
	public ResponseEntity<List<Account>> getAccountsByType(@RequestParam String type) {
		return new ResponseEntity<>(accountService.getAccountsByType(type), HttpStatus.OK);
	}
	
	/**
	 * get only bank account code by subsidiary and type
	 * @param subsidiaryId and type
	 * @return account code
	 */
	@GetMapping("/get-account-by-subsidiary")
	public ResponseEntity<List<Account>> findByLocation(@RequestParam Long subsidiaryId, @RequestParam List<String> type ) {
		List<Account> accounts = new ArrayList<Account>();
		try {
			accounts = accountService.findBySubsidiaryIdAndType(subsidiaryId, type);
			log.info("Getting the account code by subsidiary and type " + accounts);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(
					"Exception while getting the  account code for make payment :: " + e.toString());
		}
		return ResponseEntity.ok(accounts);
	}
}
