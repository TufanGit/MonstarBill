package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Project;
import com.monster.bill.payload.request.PaginationRequest;

@Component("projectDao")
public interface ProjectDao {
	
	public List<Project> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}
