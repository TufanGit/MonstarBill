package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Rtv;
import com.monster.bill.models.RtvHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface RtvService {
	
	public Rtv save(Rtv rtv);

	public Rtv getRtvById(Long id);
	
	public List<RtvHistory> findHistoryByRtvNumber(String rtvNumber, Pageable pageable);
	
	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public Boolean sendForApproval(Long id);

	public Boolean approveAllRtvs(List<Long> rtvIds);

	public Boolean rejectAllRtvs(List<Rtv> rtvs);

	public Boolean updateNextApprover(Long approverId, Long rtvId);

	public Boolean selfApprove(Long rtvId);
	
}
