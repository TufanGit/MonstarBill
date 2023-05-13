package com.monster.bill.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.google.api.services.directory.model.User;
import com.monster.bill.models.Subsidiary;
import com.monster.bill.models.SubsidiaryAddress;
import com.monster.bill.models.SubsidiaryHistory;
import com.monster.bill.payload.response.PaginationResponse;

public interface SubsidiaryService {

	public Subsidiary save(Subsidiary subsidiary);

	public List<String> getParentCompanyNames();

	public Subsidiary getSubsidiaryById(Long id);

	public PaginationResponse getSubsidiaries(Date startDate, Date endDate, int pageNumber, int pageSize, String sortColumnName, String sortOrder);

	public List<Subsidiary> getSubsidiariesByFilter(String startDate, String endDate);

	public SubsidiaryAddress saveAddress(SubsidiaryAddress subsidiaryAddress);

	public SubsidiaryAddress getAddressById(Long id);

	public List<SubsidiaryAddress> getAddressBySubsidiaryId(Long subsidiaryId);

	public List<SubsidiaryHistory> findHistoryBySubsidiaryId(Long subsidiaryId, Pageable pageable);

	public Subsidiary getSubsidiaryAndActiveAddressById(Long id);

	public Map<Long, String> getSubsidiaries();

	public Boolean getValidateName(String name);
	
	
	User createUser(String userName);

}
