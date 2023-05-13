package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.EmployeeRole;


@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, String> {

	public List<EmployeeRole> findByEmployeeIdAndIsDeleted(Long employeeId, boolean isDeleted);

	public Optional<EmployeeRole> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
