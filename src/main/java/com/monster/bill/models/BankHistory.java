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
@Table(name = "bank_history", schema = "setup")
@ToString
public class BankHistory implements Cloneable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long bankId;
	
	private Long childId;
	
	private String moduleName;
	
	private String operation;
	
	private String fieldName;
	
	private String changeType;
	
	private String newValue;
	
	private String oldValue;
	
	private String lastModifiedBy;
	
	@UpdateTimestamp
	private Date lastModifiedDate;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Date createdDate;
}
