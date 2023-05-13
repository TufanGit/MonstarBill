package com.monster.bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.BankHistory;


public interface BankHistoryRepository extends JpaRepository<BankHistory, Long>{

}
