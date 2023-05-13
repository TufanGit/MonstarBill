package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.MakePayment;

public interface MakePaymentRepository extends JpaRepository<MakePayment, String> {

	Optional<MakePayment> findByIdAndIsDeleted(Long id, boolean isDeleted);

	List<MakePayment> getByIdAndType(Long paymentId, String type);

}
