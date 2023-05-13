package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.AdvancePaymentHistory;

public interface AdvancePaymentHistoryRepository extends JpaRepository<AdvancePaymentHistory, String> {


	List<AdvancePaymentHistory> findByAdvancePaymentNumberOrderById(String payment, Pageable pageable);


}
