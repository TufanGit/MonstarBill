package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.QuotationPr;

@Repository
public interface QuotationPrRepository extends JpaRepository<QuotationPr, String> {

	public List<QuotationPr> findAllByQuotationIdAndIsDeleted(Long quotationId, boolean isDeleted);

//	public List<QuotationPr> findAllByQuotationNumberAndIsDeleted(String quotationNumber, boolean isDeleted);
}
