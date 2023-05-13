//package com.monster.bill.controller;
//
//import java.util.Optional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PostLoad;
//import javax.persistence.PostPersist;
//import javax.persistence.PostRemove;
//import javax.persistence.PostUpdate;
//import javax.persistence.PrePersist;
//import javax.persistence.PreRemove;
//import javax.persistence.PreUpdate;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.monster.bill.models.SupplierAddress;
//import com.monster.bill.repository.SupplierAddressRepository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Component("auditTrailListener")
//@Slf4j
//public class AuditTrailListener {
//
//	@Autowired
//	private SupplierAddressRepository supplierAddressRepository;
//	
//	@PersistenceContext
//	private EntityManager entityManager;
//	
//	@PrePersist
//	@PreUpdate
//	@PreRemove
//	private void beforeAnyUpdate(SupplierAddress supplierAddress) {
//		log.info("[USER AUDIT] About to add a user");
//		Optional<SupplierAddress> sa = Optional.ofNullable(new SupplierAddress());
//		if (supplierAddress.getId() == null) {
//			// add to table
//		} else {
////			entityManager.createNamedQuery("")
//			// sa = this.supplierAddressRepository.findById(supplierAddress.getId());
//		}
//		System.out.println("===");
//	}
//
//	@PostPersist
//	@PostUpdate
//	@PostRemove
//	private void afterAnyUpdate(SupplierAddress user) {
//		log.info("[USER AUDIT] add/update/delete complete for user: " + user.getId());
//	}
//
//	@PostLoad
//	private void afterLoad(SupplierAddress user) {
//		log.info("[USER AUDIT] user loaded from database: " + user.getId());
//	}
//}