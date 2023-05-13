package com.monster.bill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.monster.bill.models.Invoice;
import com.monster.bill.models.Mail;
import com.monster.bill.service.MailService;

@RestController
@RequestMapping("/mail")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 4800, allowCredentials = "false")
public class MailController {

	@Autowired
	private MailService mailService;

	@PostMapping("/save/all")
	public List<Mail> saveMails(@RequestParam Long employeeId) {
		return mailService.saveMails(employeeId);
	}
	/**
	 * Save File
	 * 
	 * @return File Details
	 */
	@PostMapping("/save/file")
	public Mail saveFile(@RequestParam("file") MultipartFile file, @RequestParam Long employeeId) {
		return mailService.saveFile(file, employeeId);
	}

	@GetMapping("/get/all")
	public List<Mail> getMails(@RequestParam Long employeeId) {
		return mailService.getMails(employeeId);
	}
	/**
	 * Get Attachment File
	 * 
	 * @return File
	 */
	@GetMapping("/get/attachment")
	public ResponseEntity<Object> getAttachment(@RequestParam Long mailId) {

		Mail attachment = mailService.getAttachment(mailId);
		HttpHeaders headers = new HttpHeaders();
		InputStreamResource resource = new InputStreamResource(attachment.getAttachment());
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", attachment.getAttachName()));
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
				.contentType(MediaType.parseMediaType("application/pdf")).body(resource);

		return responseEntity;
	}

	@GetMapping("/get/invoice")
	public Invoice getInvoiceData(@RequestParam Long mailId) {
		return mailService.getInvoiceData(mailId);
	}

}
