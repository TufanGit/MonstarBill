package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.MakePaymentHistory;

public interface MakePaymentHistoryRepository extends JpaRepository<MakePaymentHistory, String> {

	List<MakePaymentHistory> findByPaymentNumber(String payment, Pageable pageable);


}
