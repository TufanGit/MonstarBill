package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor {

	
	
	List<Invoice> getAllInvoiceBySubsidiaryIdAndSupplierId(@Param("subsidiaryId") Long subsidiaryId,@Param("supplierId") Long supplierId);

	List<Invoice> getAllInvoiceBySupplierId(Long supplierId);

	@Query(" select i from Invoice i where i.supplierId = :supplierId and i.amountDue != 0.0 ")
	List<Invoice> findBySupplierId(Long supplierId);
	
	Optional<Invoice> findByInvoiceId(Long invoiceId);

	Optional<Invoice> getByInvoiceId(Long invoiceId);

}
