package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.AccountSubsidiary;

@Repository
public interface AccountSubsidiaryRepository extends JpaRepository<AccountSubsidiary, String> {

	public List<AccountSubsidiary> findAllByAccountIdAndIsDeleted(Long accountId, boolean isDeleted);

}
