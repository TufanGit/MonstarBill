package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.AccountLocation;

@Repository
public interface AccountLocationRepository extends JpaRepository<AccountLocation, String> {

	public List<AccountLocation> findAllByAccountIdAndIsDeleted(Long accountId, boolean isDeleted);

}
