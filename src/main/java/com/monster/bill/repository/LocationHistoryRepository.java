package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.LocationHistory;

public interface LocationHistoryRepository extends JpaRepository<LocationHistory, String>{
	
	public List<LocationHistory> findByLocationId(Long id, Pageable pageable);

}
