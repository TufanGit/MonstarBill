package com.monster.bill.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monster.bill.common.CustomException;
import com.monster.bill.models.Bank;
import com.monster.bill.service.BankService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bank")
@Slf4j
public class BankController {

	@Autowired
	private BankService bankService;
	
	@PostMapping("/save")
	public ResponseEntity<Bank> save(@Valid @RequestBody Bank  bank){
		log.info("saving bank");
		try {
		bank=bankService.save(bank);
		}catch (Exception e) {
			log.error("Error while saving the bank");
			e.printStackTrace();
			String specificCause=e.getMessage();
			String specificMessage=specificCause.substring(specificCause.indexOf(" constraint" + 1));
			specificMessage.trim();
			throw new CustomException("Error while saving the bank : " + specificMessage);
		}
		log.info("Bank saving completed successfully");
		return ResponseEntity.ok(bank);
	}
	
}
