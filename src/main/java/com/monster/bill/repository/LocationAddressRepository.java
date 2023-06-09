package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.LocationAddress;

@Repository
public interface LocationAddressRepository extends JpaRepository<LocationAddress, String> {

	public List<LocationAddress> findAll();

	public Optional<LocationAddress> findByIdAndIsDeleted(Long id, boolean isDeleted);

	public Optional<LocationAddress> findByLocationIdAndIsDeleted(Long locationId, boolean isDeleted);
	
}
