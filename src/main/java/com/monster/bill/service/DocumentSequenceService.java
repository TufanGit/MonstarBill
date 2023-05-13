package com.monster.bill.service;

import java.util.List;

import com.monster.bill.models.DocumentSequence;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface DocumentSequenceService {
	//public DocumentSequence save(DocumentSequence documentSequence);
	public DocumentSequence getDocumentSequenceById(Long id);
	public String getDocumentSequenceNumbers(String transactionalDate,Long subsidiaryId, String type, boolean isDeleted);
	public boolean deleteById(Long id);
	public List<DocumentSequence> save(List<DocumentSequence> documentSequences);
	public PaginationResponse findAll(PaginationRequest paginationRequest);
}
