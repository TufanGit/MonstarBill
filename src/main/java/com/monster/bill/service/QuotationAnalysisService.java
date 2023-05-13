package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Location;
import com.monster.bill.models.QuotationAnalysis;
import com.monster.bill.models.QuotationAnalysisHistory;
import com.monster.bill.models.QuotationAnalysisItem;
import com.monster.bill.models.Supplier;
import com.monster.bill.payload.request.MailRequest;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.IdNameResponse;
import com.monster.bill.payload.response.PaginationResponse;

public interface QuotationAnalysisService {

	public QuotationAnalysis save(QuotationAnalysis quotationAnalysis);

	public QuotationAnalysis findById(Long id);

	public List<QuotationAnalysisHistory> findHistoryById(String qaNumber, Pageable pageable);

	public List<QuotationAnalysis> getQaNumberByPrIds(List<Long> prIds);

	public List<Long> getSuppliersByQaIds(List<Long> qaIds);

	public List<IdNameResponse> findQaNumbersBySubsidiaryId(Long subsidiaryId);

	public List<Supplier> findSupplierByQaId(Long qaId);

	public List<Long> findPrIdsByQaId(Long qaId);

	public List<Location> findLocationsByQaIdAndSupplier(Long qaId, Long supplierId);

	public List<QuotationAnalysisItem> findItemsByQaAndSupplierAndLocation(Long qaId, Long supplierId,
			Long locationId);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public String sendMail(MailRequest mailRequest);

}
