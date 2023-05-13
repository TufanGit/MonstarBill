package com.monster.bill.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_payment_instrument", schema = "setup")
@ToString
public class BankPaymentInstrument implements Cloneable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long bankId;
	
	private String documentName;
	
	private String documentNumberFrom;
	
	private String documentNumberTo;
	
	private String effectiveFrom;
	
	private String effectiveTo;
	
	private Boolean isActive = true;
	
	private Boolean idDeleted;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Date createdDate;
	
	private String createdBy;
	
	@UpdateTimestamp
	private Date lastModifiedDate;
	
	private String lastModifiedBy;
}
