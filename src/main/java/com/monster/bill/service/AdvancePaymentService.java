package com.monster.bill.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.AdvancePayment;
import com.monster.bill.models.AdvancePaymentHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface AdvancePaymentService {

	public AdvancePayment save(AdvancePayment advancePayment);

	public AdvancePayment getAdvancePaymentById(Long id);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public List<AdvancePayment> getPrePaymentApproval();

	public List<AdvancePaymentHistory> findHistoryById(String payment, Pageable pageable);

	public Boolean sendForApproval(Long id);

	public Boolean approveAllAdvancePayments(List<Long> advancePaymentIds);

	public Boolean rejectAllAdvancePayments(List<AdvancePayment> advancePayments);

	public Boolean updateNextApprover(Long approverId, Long advancePaymentId);

	public Boolean selfApprove(Long advancePaymentId);

	public AdvancePayment saveApply(@Valid AdvancePayment advancePayment);

}
