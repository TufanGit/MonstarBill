package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.RolesDepartment;

@Repository
public interface RolesDepartmentRepository extends JpaRepository<RolesDepartment, String> {

	public List<RolesDepartment> findAllByRoleIdAndIsDeleted(Long accountId, boolean isDeleted);

}
