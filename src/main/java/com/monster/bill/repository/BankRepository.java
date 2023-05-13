package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.Bank;


public interface BankRepository extends JpaRepository<Bank, Long>{

	Optional<Bank> findByIdAndIsDeleted(Long id, boolean b);

}
