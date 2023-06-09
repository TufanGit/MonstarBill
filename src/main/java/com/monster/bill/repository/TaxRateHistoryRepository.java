package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.TaxRateHistory;

/**
 * Repository for the Tax-Rate-rules history
 * @author Prashant
 *
 */
@Repository
public interface TaxRateHistoryRepository extends JpaRepository<TaxRateHistory, String> {

	public List<TaxRateHistory> findByTaxRateId(Long id, Pageable pageable);
	
}
