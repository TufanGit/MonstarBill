package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.Template;
import com.monster.bill.models.TemplateSupplier;

@Repository
public interface TemplateSupplierRepository extends JpaRepository<TemplateSupplier, Long> {

	public Optional<TemplateSupplier> findBySupplierEmailAndIsDeleted(String supplierEmail, boolean isDeleted);
	
	public List<TemplateSupplier> findByTemplateAndIsDeleted(Template template, boolean isDeleted);
	
}
