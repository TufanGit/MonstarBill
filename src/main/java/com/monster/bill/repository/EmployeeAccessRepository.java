package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.EmployeeAccess;

@Repository
public interface EmployeeAccessRepository extends JpaRepository<EmployeeAccess, String> {
	
	public Optional<EmployeeAccess> findByEmployeeIdAndIsDeleted(Long employeeId, boolean isDeleted);

	public Optional<EmployeeAccess> findByIdAndIsDeleted(Long id, boolean isDeleted);

	@Query("SELECT plainPassword FROM EmployeeAccess WHERE employeeId = :employeeId ")
	public String getPasswordById(Long employeeId);

	public List<EmployeeAccess> findByAccessMail(String email);

}
