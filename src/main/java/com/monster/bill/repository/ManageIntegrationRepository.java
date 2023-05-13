package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.monster.bill.models.ManageIntegration;

public interface ManageIntegrationRepository extends JpaRepository<ManageIntegration, String>, JpaSpecificationExecutor {

	public Optional<ManageIntegration> findByIdAndIsDeleted(Long id, boolean isDeleted);


	 
}
