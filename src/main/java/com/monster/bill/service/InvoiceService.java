package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.monster.bill.models.Invoice;
import com.monster.bill.models.InvoiceHistory;
import com.monster.bill.models.InvoicePayment;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface InvoiceService {
	
	Invoice saveInvoice(Invoice invoice);
	
	PaginationResponse getInvoices(PaginationRequest paginationRequest);

	List<Invoice> findBySubsidiaryAndSuppplier(Long subsidiaryId, Long supplierId);

	Invoice getInvoice(Long invoiceId);
	
	List<InvoiceHistory> getInvoiceHistory(Long invoiceId, Pageable pageable);

	List<InvoicePayment> saveInvoicePayment(List<InvoicePayment> invoicePayment);

	public Boolean sendForApproval(Long id);

	public Boolean approveAllInvoices(List<Long> invoiceIds);

	public byte[] downloadTemplate();

	public byte[] upload(MultipartFile file);
	
}
