package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.DebitNoteItem;

public interface DebitNoteItemRepository extends JpaRepository<DebitNoteItem, String>{
	
public List<DebitNoteItem> findByDebitNoteId(Long rtvId);
	
	public Optional<DebitNoteItem> findById(Long id);

	public List<DebitNoteItem> findByDebitNoteIdAndIsDeleted(Long rtvId, boolean isDeleted);

	public Optional<DebitNoteItem> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
