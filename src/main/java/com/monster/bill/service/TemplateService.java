package com.monster.bill.service;

import java.util.List;

import com.monster.bill.models.Template;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface TemplateService {

	Template saveTemplate(Template template);
	
	List<Template> getTemplates();

	PaginationResponse findAll(PaginationRequest paginationRequest);
	
Template getTemplate(Long templateId);
}
