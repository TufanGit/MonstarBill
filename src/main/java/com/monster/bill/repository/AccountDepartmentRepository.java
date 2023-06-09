package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.AccountDepartment;

@Repository
public interface AccountDepartmentRepository extends JpaRepository<AccountDepartment, String> {

	public List<AccountDepartment> findAllByAccountIdAndIsDeleted(Long accountId, boolean isDeleted);

}
