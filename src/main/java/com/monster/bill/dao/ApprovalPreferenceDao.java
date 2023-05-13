package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.ApprovalPreference;
import com.monster.bill.payload.request.PaginationRequest;

@Component("approvalPreferenceDao")
public interface ApprovalPreferenceDao {
	
	public List<ApprovalPreference> findApproverMaxLevel(String whereClause);

	public List<ApprovalPreference> findAll(String whereClauses, PaginationRequest paginationRequest);

	public Long getCount(String whereClauses);

}
