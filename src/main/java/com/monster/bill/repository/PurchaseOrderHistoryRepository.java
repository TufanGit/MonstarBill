package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.PurchaseOrderHistory;

public interface PurchaseOrderHistoryRepository extends JpaRepository<PurchaseOrderHistory, String>{
	
	public List<PurchaseOrderHistory> findByPoNumberOrderById(String poNumber, Pageable pageable);

}
