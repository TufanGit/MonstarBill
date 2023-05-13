package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.DocumentSequence;
import com.monster.bill.payload.request.PaginationRequest;

@Component("documentSequenceDao")
public interface DocumentSequenceDao {

	public List<DocumentSequence> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

//	public Optional<DocumentSequence> findBySubsidiaryIdAndTypeAndisDeleted(Long subsidiaryId,
//			String type, boolean isDeleted, String transactionalDate);

}
