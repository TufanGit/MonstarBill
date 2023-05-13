package com.monster.bill.models;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "intigration")
@ToString
@Audited
@AuditTable("intigration_aud")
public class ManageIntegration implements Cloneable{
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	private String intigrationWith;
	
	private String accountId;
	
	private String description;
	
	private Date startDate;
	
	private Date endDate;
	
	private String connection;
	
	private boolean isCompanyMaster;
	
	private boolean isSupplierMaster;
		
	private boolean isTaxCode;
		
	private boolean isApInvoice;
	
	private boolean isDebitNote;
		
	private boolean isAdvancePayment;
	
	private boolean isItem;
	
	private boolean isMakePayment;
	
	private String tbaTokenSecret;
	
	private String tbaTokenId;
	
	private String tbaConsumerSecret;
	
	private String tbaConsumerKey;
	
	@Column(name="is_deleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	@CreationTimestamp
	@Column(name="created_date", updatable = false)
	private Date createdDate;

	@Column(name="created_by")
	private String createdBy;

	@UpdateTimestamp
	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="last_modified_by")
	private String lastModifiedBy;
	

	
	@Transient
	private List<ManageIntegrationSubsidiary> manageIntegrationSubsidiaries;


	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
