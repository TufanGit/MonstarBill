package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.ApprovalPreference;
import com.monster.bill.models.ApprovalPreferenceHistory;
import com.monster.bill.payload.request.ApprovalRequest;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface ApprovalPreferenceService {

	public List<ApprovalPreferenceHistory> findHistoryById(Long id, Pageable pageable);

	public ApprovalPreference save(ApprovalPreference approvalPreference);

	public ApprovalPreference getApprovalPreferenceById(Long id);

	public ApprovalPreference findApproverMaxLevel(ApprovalRequest approvalRequest);

	public ApprovalRequest findApproverByLevelAndSequence(Long id, String level, Long sequenceId);

	public String findPreferenceTypeById(Long approvalPreferenceId);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

}
