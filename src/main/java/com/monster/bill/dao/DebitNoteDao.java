package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.DebitNote;
import com.monster.bill.payload.request.PaginationRequest;

@Component("rtvDao")
public interface DebitNoteDao {
	public List<DebitNote> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}
