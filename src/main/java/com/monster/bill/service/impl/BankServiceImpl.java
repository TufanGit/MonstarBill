package com.monster.bill.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.monster.bill.common.AppConstants;
import com.monster.bill.common.CustomException;
import com.monster.bill.enums.Operation;
import com.monster.bill.models.Bank;
import com.monster.bill.models.BankHistory;
import com.monster.bill.models.BankPaymentInstrument;
import com.monster.bill.repository.BankHistoryRepository;
import com.monster.bill.repository.BankPaymentInstrumentRepository;
import com.monster.bill.repository.BankRepository;
import com.monster.bill.service.BankService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankServiceImpl implements BankService{

	@Autowired
	private BankRepository bankRepository;
	
	@Autowired
	private BankPaymentInstrumentRepository bankPaymentInstrumentRepository;
	
	@Autowired
	private BankHistoryRepository bankHistoryRepository;
	
	
	@Override
	public Bank save(Bank bank) {
		Optional<Bank> oldBank = Optional.ofNullable(null);
		
		oldBank=this.bankRepository.findByIdAndIsDeleted(bank.getId(), false);
		if(oldBank.isPresent()) {
			try {
				oldBank=Optional.ofNullable((Bank) oldBank.get().clone());
			} catch (CloneNotSupportedException e) {
				log.error("Error while cloning the object. PLease contact administrator");
				throw new CustomException("Error while cloning the object. Please contact administrator");
			}
		}
		
		Bank bankSaved;
		
		//---------Saving Bank starts---------//
		try {
			bankSaved=this.bankRepository.save(bank);
		}catch(DataIntegrityViolationException e) {
			String specificCause = e.getMostSpecificCause().toString();
			String specificCauseMessage=specificCause.substring(specificCause.indexOf(" Detail" + 1));
			specificCauseMessage.trim();
			log.error(specificCauseMessage);
			throw new CustomException(specificCauseMessage);
		}
		if(bank.getIsActive()==true) {
			bank.setActiveDate(null);
		}
		if(bankSaved==null) {
			log.error("Error while saving the Bank" + bank.toString());
			throw new CustomException("Error while saving the Bank");
		}
		
		//-------------Updating BankHistory--------------------------//
		this.updateBankHistory(bank, oldBank);
		
		//---------Saving BankPaymentInstruments starts-------------//
		
		if(CollectionUtils.isNotEmpty(bank.getBankPaymentInstruments())) {
			for(BankPaymentInstrument bankPaymentInstrument : bank.getBankPaymentInstruments()) {
				bankPaymentInstrument.setBankId(bankSaved.getId());
				this.save(bankPaymentInstrument);
			}
		}
		//----------Saving BankPaymentInstrument is completed---------//
		return bank;
	}


	private void updateBankHistory(Bank bank, Optional<Bank> oldBank) {
		if(oldBank.isPresent()) {
			List<BankHistory> bankHistories = new ArrayList<BankHistory>();
			
			try {
				bankHistories=oldBank.get().compareFields(bank);
				this.bankHistoryRepository.saveAll(bankHistories);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				log.error("Error while comparing the object. Please contact administrator");
				throw new CustomException("Error while comparing the object.please contact administrator");
			}
			log.info("Bank History updated successfully");
		}else{
			this.bankHistoryRepository.save(this.prepareBankHistory(bank.getId(), null, AppConstants.BANK, Operation.CREATE.toString(), bank.getLastModifiedBy(), null,String.valueOf(bank.getId()) ));
		}
		
	}


	private BankPaymentInstrument save(BankPaymentInstrument bankPaymentInstrument) {
		
		  bankPaymentInstrument = this.bankPaymentInstrumentRepository.save(bankPaymentInstrument);
		  
		  if(bankPaymentInstrument == null) {
			  log.error("Error while saving the BankPayementInstrument : " + bankPaymentInstrument);
			  throw new CustomException("Error while saving the BankPaymenInstrument");
		  }
		  
		  this.bankHistoryRepository.save(
				  this.prepareBankHistory(bankPaymentInstrument.getBankId(), 
				  bankPaymentInstrument.getId(),
				  AppConstants.BANK_PAYMENT_INSTRUMENTS, 
				  Operation.CREATE.toString(), 
				  bankPaymentInstrument.getLastModifiedBy(), 
				  null, 
				  String.valueOf(bankPaymentInstrument.getId()))
				  );
		  
		  return bankPaymentInstrument;
	}


	public BankHistory prepareBankHistory(Long bankId, Long childId, String moduleName, String operation, String lastModifiedBy, String oldValue, String newValue) {
		BankHistory bankHistory=new BankHistory();
		bankHistory.setBankId(bankId);
		bankHistory.setChildId(childId);
		bankHistory.setModuleName(moduleName);
		bankHistory.setOperation(operation);
		bankHistory.setLastModifiedBy(lastModifiedBy);
		bankHistory.setOldValue(oldValue);
		bankHistory.setNewValue(newValue);
		
		return bankHistory;
	}

}
