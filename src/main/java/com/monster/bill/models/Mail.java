package com.monster.bill.models;

import java.io.InputStream;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
public class Mail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailId;
	
	private String fromId, toId, subjectLine, attachName, attachType;
	private Date receiveDate;
	private String createdBy, modifiedBy;
	private boolean isMail;
	
	@CreationTimestamp
    private Date dateCreated;

    @UpdateTimestamp
    private Date lastUpdated;
	
	@Transient
	private InputStream attachment;

}
