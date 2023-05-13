package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.OtherPreference;

@Repository
public interface OtherPreferenceRepository extends JpaRepository<OtherPreference, String> {
	
	public List<OtherPreference> findByPreferenceIdAndIsDeleted(Long preferenceId, boolean isDeleted);
	
	public Optional<OtherPreference> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
