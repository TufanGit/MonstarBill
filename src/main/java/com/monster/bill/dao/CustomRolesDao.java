package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.CustomRoles;
import com.monster.bill.payload.request.PaginationRequest;

@Component("customRolesDao")
public interface CustomRolesDao {

	public Long getCount(String whereClause);

	public List<CustomRoles> findAll(String whereClause, PaginationRequest paginationRequest);
}
