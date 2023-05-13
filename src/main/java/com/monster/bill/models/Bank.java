package com.monster.bill.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.monster.bill.common.AppConstants;
import com.monster.bill.common.CommonUtils;
import com.monster.bill.enums.Operation;

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
@ToString
@Table(name = "bank", schema = "setup")
public class Bank implements Cloneable{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Subsidiary id is mendatory")
	@Column(nullable = false, updatable = false)
	private Long subsidiaryId;
	
	@Column(updatable = false)
	private String name;
	
	private String branch;
	
	@Column(columnDefinition = "text")
	private String address;
	
	@NotBlank(message = "Account number is mendatory")
	@Column(unique = true)
	private String accountNumber;
	
	@NotBlank(message = "Account type is mendatory")
	private String accountype;
	
	@NotBlank(message = "Currency is mendatory")
	private String currency;
	
	private String branchCode;
	
	private String ifscCode;
	
	private String iban;
	
	private String swiftCode;
	
	private String sortCode;
	
	private String micrCode;
	
	@NotBlank(message = "GL Bank is mendatory")
	private String glBank;
	
	private String glExchange;
	
	@NotNull(message = "Effective from is mendatory")
	private Date effectiveFrom;
	
	private Date effectiveTo;
	
	private String integratedId;
	 
	@Transient
	private String status;
	
	private Boolean isActive = true;
	
	private Date activeDate;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isDeleted;
	
	@CreationTimestamp
	@Column(updatable = true)
	private Date createdDate;
	
	private String createdBy;
	
	@UpdateTimestamp
	private Date lastModifiedDate;
	
	private String lastModifiedBy;
	
	@Transient
	private String subsidiaryName;
	
	@Transient
	private List<BankPaymentInstrument> bankPaymentInstruments;
	
	@Override 
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
	public List<BankHistory> compareFields(Bank bank) throws IllegalArgumentException, IllegalAccessException{
		List<BankHistory> bankHistories=new ArrayList<BankHistory>();
		
		Field[] fields=this.getClass().getDeclaredFields();
		
		for(Field field : fields) {
			String fieldName=field.getName();
			
			if(!CommonUtils.getUnusedFieldsOfHistory().contains(fieldName.toLowerCase())) {
				Object oldValue = field.get(this);
				Object  newValue = field.get(bank);
				
				if(oldValue==null) {
					if(newValue!=null) {
						bankHistories.add(this.prepareBankHistory(bank, field));
					}
				}else {
					if(!oldValue.equals(newValue)) {
						bankHistories.add(this.prepareBankHistory(bank, field));
					}
				}
			}
		}
		return bankHistories;
	}

	private BankHistory prepareBankHistory(Bank bank, Field field) throws IllegalArgumentException, IllegalAccessException {
		BankHistory bankHistory= new BankHistory();
		
		bankHistory.setBankId(bank.getId());
		bankHistory.setModuleName(AppConstants.BANK);
		bankHistory.setChangeType(AppConstants.UI);
		bankHistory.setOperation(Operation.UPDATE.toString());
		bankHistory.setFieldName(CommonUtils.splitCamelCaseWithCapitalize(field.getName()));
		if(field.get(this)!=null) {
			bankHistory.setOldValue(field.get(this).toString());
		}
		if(field.get(bank)!=null) {
			bankHistory.setNewValue(field.get(bank).toString());
		}
		bankHistory.setLastModifiedBy(bank.getLastModifiedBy());
		return null;
	}
}
