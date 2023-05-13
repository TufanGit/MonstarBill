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
@Table(name = "make_payment")
@ToString
@Audited
@AuditTable("make_payment_aud")
public class MakePayment implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "payment_number")
	private String paymentNumber;

	@Column(name = "subsidiary_id")
	private Long subsidiaryId;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "supplier_id")
	private Long supplierId;

	@Column(name = "payment_date")
	private Date paymentDate;

	@Column(name = "currency")
	private String currency;

	@Column(name = "subsidiary_currency")
	private String subsidiaryCurrency;

	@Column(name = "exchange_rate")
	private Double exchangeRate;

	@Column(name = "payment_mode")
	private String paymentMode;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "bank_transaction_type")
	private String bankTransactionType;

	@Column(name = "bank_reference_number")
	private String bankReferenceNumber;

	@Column(name = "memo")
	private String memo;

	@Column(name = "netsuite_id", unique = true)
	private String netsuiteId;

	@Column(name = "rejected_comments")
	private String rejectedComments;
	
	@Column(name = "note_to_approver")
	private String noteToApprover;

	@Column(name = "void_description")
	private String voidDescription;
	
	@Column(name = "void_date")
	private Date voidDate;
	
	private String type;
	
	//--------approval process----------//
	
	@Column(name = "payment_status")
	private String paymentStatus;

	@Column(name = "approved_by")
	private String approvedBy;
	
	@Column(name = "next_approver")
	private String nextApprover;
	
	@Column(name = "next_approver_role")
	private String nextApproverRole;

	// stores the next approver level i.e. L1,L2,L3 etc.
	@Column(name = "next_approver_level")
	private String nextApproverLevel;
	
	// store's the id of approver preference
	@Column(name = "approver_preference_id")
	private Long approverPreferenceId;
	
	// stores the approver sequence id (useful internally in order to change the approver)
	@Column(name = "approver_sequence_id")
	private Long approverSequenceId;
	
	// stores the max level to approve, after that change status to approve
	@Column(name = "approver_max_level")
	private String approverMaxLevel;
	
	@Transient
	private boolean isApprovalRoutingActive;

	@Column(name = "is_deleted", columnDefinition = "boolean default false")
	private boolean isDeleted;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@UpdateTimestamp
	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Transient
	private String subsidiaryName;

	@Transient
	private String supplierName;

	@Transient
	private String bankAccountName;

	@Transient
	private Double paymentAmount;
	
	@Transient
	private List<MakePaymentList> makePaymentList;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	

	/**
	 * Compare the fields and values of 2 objects in order to find out the
	 * difference between old and new value
	 * 
	 * @param make payment
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public List<MakePaymentHistory> compareFields(MakePayment makePayment)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		List<MakePaymentHistory> makePaymentHistories = new ArrayList<MakePaymentHistory>();
		Field[] fields = this.getClass().getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();

			if (!CommonUtils.getUnusedFieldsOfHistory().contains(fieldName.toLowerCase())) {
				Object oldValue = field.get(this);
				Object newValue = field.get(makePayment);

				if (oldValue == null) {
					if (newValue != null) {
						makePaymentHistories.add(this.prepareMakePaymentHistory(makePayment, field));
					}
				} else if (!oldValue.equals(newValue)) {
					makePaymentHistories.add(this.prepareMakePaymentHistory(makePayment, field));
				}
			}
		}
		return makePaymentHistories;
	}

	private MakePaymentHistory prepareMakePaymentHistory(MakePayment makePayment, Field field)
			throws IllegalAccessException {
		MakePaymentHistory makePaymentHistory = new MakePaymentHistory();
		makePaymentHistory.setPaymentNumber(makePayment.getPaymentNumber());
		makePaymentHistory.setModuleName(AppConstants.MAKE_PAYMENT);
		makePaymentHistory.setChangeType(AppConstants.UI);
		makePaymentHistory.setOperation(Operation.UPDATE.toString());
		makePaymentHistory.setFieldName(CommonUtils.splitCamelCaseWithCapitalize(field.getName()));
		if (field.get(this) != null)
			makePaymentHistory.setOldValue(field.get(this).toString());
		if (field.get(makePayment) != null)
			makePaymentHistory.setNewValue(field.get(makePayment).toString());
		makePaymentHistory.setLastModifiedBy(makePayment.getLastModifiedBy());
		return makePaymentHistory;
	}



	public MakePayment(Long id, String paymentNumber, Long subsidiaryId, Long accountId, Long supplierId,
			String currency, String paymentMode, Double amount, String subsidiaryName, String supplierName,
			String bankAccountName) {
		this.id = id;
		this.paymentNumber = paymentNumber;
		this.subsidiaryId = subsidiaryId;
		this.accountId = accountId;
		this.supplierId = supplierId;
		this.currency = currency;
		this.paymentMode = paymentMode;
		this.amount = amount;
		this.subsidiaryName = subsidiaryName;
		this.supplierName = supplierName;
		this.bankAccountName = bankAccountName;
	}

}
