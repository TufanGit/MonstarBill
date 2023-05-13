package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Grn;
import com.monster.bill.models.GrnHistory;
import com.monster.bill.models.GrnItem;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface GrnService {

	public List<Grn> save(List<Grn> grn);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public Grn getGrnById(Long id);

	public boolean deleteById(Long id);

	public List<GrnHistory> findHistoryById(String grnNumber, Pageable pageable);

	public List<GrnItem> findGrnItemsByGrnNumber(String grnNumber);
	
	
	Grn getByGrnId(Long grnId);
	
	GrnItem getByGrnItemId(Long grnId, Long itemId);
	
	List<Grn> getByPoId(Long poId);

}
