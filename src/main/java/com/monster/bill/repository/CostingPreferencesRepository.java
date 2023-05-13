package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.CostingPreferences;

@Repository
public interface CostingPreferencesRepository extends JpaRepository<CostingPreferences, String> {
	
	public Optional<CostingPreferences> findByPreferenceIdAndIsDeleted(Long preferenceId, boolean isDeleted);
	
	public Optional<CostingPreferences> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
