package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.DocumentSequenceHistory;

public interface DocumentSequenceHistoryRepository extends JpaRepository<DocumentSequenceHistory, String>{
	
	public List<DocumentSequenceHistory> findByDocumentSequenceId(Long id, Pageable pageable);

}
