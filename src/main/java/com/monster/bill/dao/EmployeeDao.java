package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Employee;
import com.monster.bill.payload.request.PaginationRequest;


@Component("employeeDao")
public interface EmployeeDao {
	
	public List<Employee> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);
	
}
