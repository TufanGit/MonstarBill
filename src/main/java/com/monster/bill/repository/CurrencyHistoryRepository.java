package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.CurrencyHistory;

public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, String>{
	
	public List<CurrencyHistory> findByCurrencyId(Long id, Pageable pageable);

}
