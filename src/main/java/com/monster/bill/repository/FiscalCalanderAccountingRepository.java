package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.monster.bill.models.FiscalCalanderAccounting;

public interface FiscalCalanderAccountingRepository extends JpaRepository<FiscalCalanderAccounting, String> {

	public Optional<FiscalCalanderAccounting> findById(Long id);

	public List<FiscalCalanderAccounting> findByFiscalId(Long id);
	
	@Query(" select new com.monster.bill.models.FiscalCalanderAccounting(id, yearName, fiscalId, startYear ,endYear ,month , fromDate, toDate, isLock, isPeriodOpen, isPeriodClose, isYearCompleted) from FiscalCalanderAccounting ORDER BY id asc ")
	public List<FiscalCalanderAccounting> findAllAccounting();

}
