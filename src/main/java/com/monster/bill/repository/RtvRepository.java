package com.monster.bill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.Rtv;

public interface RtvRepository extends JpaRepository<Rtv, String>{
	public Optional<Rtv> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
