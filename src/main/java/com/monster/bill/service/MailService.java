package com.monster.bill.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.monster.bill.models.Invoice;
import com.monster.bill.models.Mail;

public interface MailService {
	
	List<Mail> saveMails(Long employeeId);
	
	Mail saveFile(MultipartFile file, Long employeeId);
	
	List<Mail> getMails(Long employeeId);
	
	Mail getAttachment(Long mailId);
	
	Invoice getInvoiceData(Long mailId);

}
