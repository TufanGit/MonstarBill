package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.ManageIntegrationSubsidiary;

@Repository
public interface ManageIntegrationSubsidiaryRepository extends JpaRepository<ManageIntegrationSubsidiary, String> {

	

	public List<ManageIntegrationSubsidiary> findByIntigrationIdAndIsDeleted(Long id, boolean isDeleted);

	public List<ManageIntegrationSubsidiary> findByIntigrationId(Long manageIntegrationId);


}
