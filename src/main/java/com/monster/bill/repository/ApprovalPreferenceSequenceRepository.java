package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.ApprovalPreferenceSequence;

@Repository
public interface ApprovalPreferenceSequenceRepository extends JpaRepository<ApprovalPreferenceSequence, String> {

	public Optional<ApprovalPreferenceSequence> findByIdAndIsDeleted(Long id, boolean isDeleted);

	public List<ApprovalPreferenceSequence> findByConditionIdAndIsDeleted(Long id, boolean isDeleted);

}
