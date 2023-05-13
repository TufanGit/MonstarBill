package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.InvoiceItem;
@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
	
	public List<InvoiceItem> findByInvoiceId(Long invoiceId);

}
