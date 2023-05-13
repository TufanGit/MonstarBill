package com.monster.bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.BankPaymentInstrument;


public interface BankPaymentInstrumentRepository extends JpaRepository<BankPaymentInstrument, Long>{

}
