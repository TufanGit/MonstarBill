package com.monster.bill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monster.bill.models.Template;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.service.TemplateService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/template")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 4800, allowCredentials = "false")
public class TemplateController {

	@Autowired
	private TemplateService templateService;

	/**
	 * Get all Templates
	 * 
	 * @param
	 * @return Templates
	 */
	@GetMapping("/get/all")
	public List<Template> getTemplates() {
		return templateService.getTemplates();
	}
	/**
	 * Save Template with Suppliers
	 * 
	 * @param template
	 * @return Template
	 */
	@PostMapping("/save")
	public Template saveTemplate(@RequestBody Template template) {
		return templateService.saveTemplate(template);
	}
	/**
	 * get the all template value
	 * @return
	 */
	@PostMapping("/get/all-pagination")
	public ResponseEntity<PaginationResponse> findAll(@RequestBody PaginationRequest paginationRequest) {
		log.info("Get all template started.");
		PaginationResponse paginationResponse = new PaginationResponse();
		paginationResponse = templateService.findAll(paginationRequest);
		log.info("Get all template completed.");
		return new ResponseEntity<>(paginationResponse, HttpStatus.OK);
	}

/**
	 * Get Template with Suppliers
	 * 
	 * @param templateId
	 * @return Template
	 */
	@GetMapping("/get")
    public Template getTemplate(@RequestParam Long templateId)
    {
        return templateService.getTemplate(templateId);
    }
}
