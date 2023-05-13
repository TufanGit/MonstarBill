package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.DebitNote;
import com.monster.bill.models.DebitNoteHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface DebitNoteService {
	
	public DebitNote save(DebitNote debitNote);

	public DebitNote getDebitNoteById(Long id);
	
	public List<DebitNoteHistory> findAuditByDebitNoteNumber(String debitNoteNumber, Pageable pageable);
	
	public PaginationResponse findAll(PaginationRequest paginationRequest);
	
}
