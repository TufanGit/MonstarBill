package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.AdvancePaymentApply;

public interface AdvancePaymentApplyRepository extends JpaRepository<AdvancePaymentApply, String> {

	Optional<AdvancePaymentApply> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
