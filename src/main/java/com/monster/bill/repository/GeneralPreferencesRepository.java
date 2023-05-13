package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.GeneralPreference;

public interface GeneralPreferencesRepository extends JpaRepository<GeneralPreference, String> {
	
	public Optional<GeneralPreference> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
