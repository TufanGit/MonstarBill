package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.monster.bill.models.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

	public Optional<Currency> findByIdAndIsDeleted(Long id, boolean isDeleted);

	@Query("select new com.monster.bill.models.Currency(id, name, code, createdDate, isInactive) from Currency where isDeleted is false ")
	public List<Currency> getActiveCurrencies();

	@Query("select count(id) from Currency where lower(name) = lower(:name) AND isDeleted = false ")
	public Long getCountByName(@Param("name") String name);
	
	
	public Optional<Currency> findByNameAndIsDeleted(String name, boolean isDeleted);

}
