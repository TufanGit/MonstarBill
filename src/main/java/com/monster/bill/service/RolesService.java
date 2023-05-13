package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.CustomRoles;
import com.monster.bill.models.RolesHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface RolesService {

	public CustomRoles save(CustomRoles role);

	public CustomRoles getRoleById(Long id);

	public List<RolesHistory> findHistoryById(Long id, Pageable pageable);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public List<CustomRoles> getRoleByIds(List<Long> roleIds);

	public List<CustomRoles> findRolesBySubsidiaryId(Long subsidiaryId, String accessType);

	public List<CustomRoles> findBySubsidiaryForEmplyoee(Long subsidiaryId);

}
