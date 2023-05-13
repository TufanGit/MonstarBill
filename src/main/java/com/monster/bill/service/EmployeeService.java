package com.monster.bill.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Employee;
import com.monster.bill.models.EmployeeAddress;
import com.monster.bill.models.EmployeeHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.IdNameResponse;
import com.monster.bill.payload.response.PaginationResponse;

public interface EmployeeService {
	
	public Employee save(Employee employee);

	public Employee getEmployeeById(Long id);
	
	public List<EmployeeHistory> findAuditById(String employeeNumber, Pageable pageable);
	
	public List<EmployeeAddress> findAddressByEmployeeId(Long employeeId);

	public Boolean deleteEmployeeAddress(EmployeeAddress employeeAddress);
	
	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public Boolean getValidateName(String name);

	public Map<Long, String> getAllEmployees(Long subsidiaryId);
	
	public String getEmailByEmployeeId(Long employeeId);

	public List<Employee> findBySubsidiaryId(Long subsidiaryId);

	public List<IdNameResponse> findByRoleId(Long roleId, Long subsidiaryId);

	public Long getEmployeeIdByAccessMail(String email);

}
