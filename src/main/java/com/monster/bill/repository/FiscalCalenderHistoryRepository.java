package com.monster.bill.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.FiscalCalenderHistory;

public interface FiscalCalenderHistoryRepository extends JpaRepository<FiscalCalenderHistory, String>{
	
	public List<FiscalCalenderHistory> findByFiscalCalenderId(Long id, Pageable pageable);

}
