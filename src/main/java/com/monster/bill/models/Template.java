package com.monster.bill.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
public class Template {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;
	
	private Long subsidiaryId;
	private String templateNo, templateName, invoiceNo, invoiceDate, poNo, item, description;
	private String slNo, uom, unitPrice, billQty, discount, netAmount;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "template")
  //  @JoinColumn(name = "templateId")
    private List<TemplateSupplier> templateSuppliers;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	private String createdBy, modifiedBy;
	
	@CreationTimestamp
    private Date dateCreated;

    @UpdateTimestamp
    private Date lastUpdated;

}
