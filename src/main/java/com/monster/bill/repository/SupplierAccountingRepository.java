package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.SupplierAccounting;

@Repository
public interface SupplierAccountingRepository extends JpaRepository<SupplierAccounting, String> {

	public Optional<SupplierAccounting> findByIdAndIsDeleted(Long id, boolean isDeleted);

	public Optional<SupplierAccounting> findBySupplierIdAndIsDeleted(Long supplierId, boolean isDeleted);

}
