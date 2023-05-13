package com.monster.bill.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Invoice;
import com.monster.bill.models.InvoicePayment;
import com.monster.bill.models.MakePayment;
import com.monster.bill.models.MakePaymentHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface MakePaymentService {

	public MakePayment save(MakePayment makePayments);

	public MakePayment getMakePaymentById(Long id);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public List<MakePaymentHistory> findHistoryById(String payment, Pageable pageable);

	public List<Invoice> getPaymentDetailsBySupplier(Long supplierId);

	public List<InvoicePayment> deleteById(Long paymentId, String type);

	public @Valid MakePayment saveForAdvancePayment(@Valid MakePayment makePayment);

	public Boolean sendForApproval(Long id);

	public Boolean approveAllPayments(List<Long> paymentIds);

	public Boolean rejectAllPayments(List<MakePayment> payments);

	public Boolean selfApprove(Long id);

	//public List<Invoice> getDueAmount(Long invoiceId);

	//public List<AdvancePayment> makePayments(List<AdvancePayment> advancePayment);

}
