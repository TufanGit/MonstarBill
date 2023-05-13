package com.monster.bill.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.GeneralPreference;
import com.monster.bill.models.GeneralPreferenceHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface PreferencesService {
	
	public GeneralPreference save(GeneralPreference preferences);

	public GeneralPreference getPreferencesById(Long id);
	
	public List<GeneralPreferenceHistory> findAuditById(Long id, Pageable pageable);
	
	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public String findPreferenceNumberByMaster(Long subsidiaryId, String masterName);

	public List<String> findRoutingByStatus(Long subsidiaryId, String formType, String status);

}
