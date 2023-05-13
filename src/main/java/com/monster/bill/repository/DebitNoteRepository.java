package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.DebitNote;

public interface DebitNoteRepository extends JpaRepository<DebitNote, String>{
	public Optional<DebitNote> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
