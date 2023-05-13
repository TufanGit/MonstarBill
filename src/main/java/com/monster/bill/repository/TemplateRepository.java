package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

	public Optional<Template> findByTemplateIdAndIsDeleted(Long templateId, boolean isDeleted);
	
	public List<Template> findByIsDeleted(boolean isDeleted);
	
}
