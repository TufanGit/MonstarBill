package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.QuotationHistory;

/**
 * Repository for the Quotation and it's child history
 * @author Prashant
 */
@Repository
public interface QuotationHistoryRepository extends JpaRepository<QuotationHistory, String> {

	public List<QuotationHistory> findByRfqNumberOrderById(String rfqNumber, Pageable pageable);
	
}
