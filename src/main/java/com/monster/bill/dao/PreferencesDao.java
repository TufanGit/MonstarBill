package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.GeneralPreference;
import com.monster.bill.payload.request.PaginationRequest;

@Component("preferencesDao")
public interface PreferencesDao {
	
	public List<GeneralPreference> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}
