package com.monster.bill.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "template_supplier", 
uniqueConstraints = { @UniqueConstraint(columnNames = 
                                        { "subsidiaryId", "supplierId" }) })
public class TemplateSupplier {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateSupplierId;
	
	private Long supplierId, subsidiaryId;
	private String supplierEmail;
	@ToString.Exclude
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "templateId")
    private Template template;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	private String createdBy, modifiedBy;
	
	@CreationTimestamp
    private Date dateCreated;

    @UpdateTimestamp
    private Date lastUpdated;

}
