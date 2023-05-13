package com.monster.bill.models;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.monster.bill.common.AppConstants;
import com.monster.bill.common.CommonUtils;
import com.monster.bill.enums.Operation;

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
@Table(	name = "account_department")
@ToString
@Audited
@AuditTable("account_department_aud")
public class AccountDepartment implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_id")
	private Long accountId;
	
	@Column(name = "department_id")
	private Long departmentId;
	
	@Column(name = "department_name")
	private String departmentName;

	@Column(name="is_deleted", columnDefinition = "boolean default false")
	private boolean isDeleted;
	
	@CreationTimestamp
	@Column(name="created_date", updatable = false)
	private Date createdDate;

	@CreatedBy
	@Column(name="created_by", updatable = false)
	private String createdBy;

	@UpdateTimestamp
	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@LastModifiedBy
	@Column(name="last_modified_by")
	private String lastModifiedBy;
	
	@Transient
	private String operation;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Compare the fields and values of 2 objects in order to find out the
	 * difference between old and new value
	 * 
	 * @param accountDepartment
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public List<AccountHistory> compareFields(AccountDepartment accountDepartment)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		List<AccountHistory> accountHistories = new ArrayList<AccountHistory>();
		Field[] fields = this.getClass().getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();

			if (!CommonUtils.getUnusedFieldsOfHistory().contains(fieldName.toLowerCase())) {
				Object oldValue = field.get(this);
				Object newValue = field.get(accountDepartment);

				if (oldValue == null) {
					if (newValue != null) {
						accountHistories.add(this.prepareAccountHistory(accountDepartment, field));
					}
				} else if (!oldValue.equals(newValue)) {
					accountHistories.add(this.prepareAccountHistory(accountDepartment, field));
				}
			}
		}
		return accountHistories;
	}

	private AccountHistory prepareAccountHistory(AccountDepartment accountDepartment, Field field) throws IllegalAccessException {
		AccountHistory accountHistory = new AccountHistory();
		accountHistory.setAccountId(accountDepartment.getId());
		accountHistory.setModuleName(AppConstants.ACCOUNT_DEPARTMENT);
		accountHistory.setChangeType(AppConstants.UI);
		accountHistory.setOperation(Operation.UPDATE.toString());
		accountHistory.setFieldName(CommonUtils.splitCamelCaseWithCapitalize(field.getName()));
		if (field.get(this) != null) {
			accountHistory.setOldValue(field.get(this).toString());
		}
		if (field.get(accountDepartment) != null) {
			accountHistory.setNewValue(field.get(accountDepartment).toString());
		}
		accountHistory.setLastModifiedBy(accountDepartment.getLastModifiedBy());
		return accountHistory;
	}
}