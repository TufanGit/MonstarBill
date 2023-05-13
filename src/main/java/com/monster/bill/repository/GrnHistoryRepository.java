package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.GrnHistory;

public interface GrnHistoryRepository extends JpaRepository<GrnHistory, String> {

	List<GrnHistory> findByGrnNumber(String grnNumber, Pageable pageable);

}
