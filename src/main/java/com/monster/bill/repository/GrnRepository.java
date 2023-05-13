package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monster.bill.models.Grn;

public interface GrnRepository extends JpaRepository<Grn, String> {

	 Optional<Grn> findByIdAndIsDeleted(Long id, boolean isDeleted);
	 
	 
	 List<Grn> findByPoIdAndIsDeleted(Long poId, boolean isDeleted);

}
