package com.monster.bill.service;

import org.springframework.stereotype.Service;

import com.monster.bill.models.Bank;


@Service
public interface BankService {

	public Bank save(Bank bank);
}
