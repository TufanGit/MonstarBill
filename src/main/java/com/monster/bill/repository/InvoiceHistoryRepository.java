package com.monster.bill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.InvoiceHistory;
@Repository
public interface InvoiceHistoryRepository extends JpaRepository<InvoiceHistory, Long> {
	
	public Page<InvoiceHistory> findByInvoiceId(Long invoiceId, Pageable pageable);

}
