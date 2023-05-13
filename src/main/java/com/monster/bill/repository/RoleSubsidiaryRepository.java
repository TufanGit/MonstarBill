package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.RoleSubsidiary;

@Repository
public interface RoleSubsidiaryRepository extends JpaRepository<RoleSubsidiary, String> {

	public List<RoleSubsidiary> findAllByRoleIdAndIsDeleted(Long roleId, boolean isDeleted);

}
