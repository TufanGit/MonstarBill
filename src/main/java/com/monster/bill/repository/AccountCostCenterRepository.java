package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.AccountCostCenter;

@Repository
public interface AccountCostCenterRepository extends JpaRepository<AccountCostCenter, String> {

	public List<AccountCostCenter> findAllByAccountIdAndIsDeleted(Long accountId, boolean isDeleted);

}
