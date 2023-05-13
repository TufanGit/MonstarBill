package com.monster.bill.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monster.bill.common.CommonUtils;
import com.monster.bill.common.CustomException;
import com.monster.bill.common.FilterNames;
import com.monster.bill.dao.TemplateDao;
import com.monster.bill.models.Template;
import com.monster.bill.models.TemplateSupplier;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.repository.TemplateRepository;
import com.monster.bill.repository.TemplateSupplierRepository;
import com.monster.bill.service.TemplateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TemplateRepository templateRepository;
	
	@Autowired
    private TemplateSupplierRepository templateSupplierRepository;

	@Autowired
	private TemplateDao templateDao;

	@Override
	@Transactional
	public Template saveTemplate(Template template) {
		Template templateSaved;
		Long templateId = template.getTemplateId();
		if (templateId == null)
			template.setCreatedBy(CommonUtils.getLoggedInUsername());
		template.setModifiedBy(CommonUtils.getLoggedInUsername());
		template.getTemplateSuppliers().forEach(templateSupplier -> {
			templateSupplier.setTemplate(template);
			templateSupplier.setSubsidiaryId(template.getSubsidiaryId());
			if (templateSupplier.getTemplateSupplierId() == null)
				templateSupplier.setCreatedBy(CommonUtils.getLoggedInUsername());
			templateSupplier.setModifiedBy(CommonUtils.getLoggedInUsername());
		});
		log.info("Save Template started.");
		try {
			templateSaved = templateRepository.save(template);
		} catch (DataIntegrityViolationException e) {
			log.error(" Template unique constrain violetd." + e.getMostSpecificCause());
			throw new CustomException("Template unique constrain violetd :" + e.getMostSpecificCause());
		}
		log.info("Saved Template: " + templateSaved);
		return templateSaved;
	}

	@Override
	public List<Template> getTemplates() {
		List<Template> templates;
		log.info("Get all Templates started.");
		templates = templateRepository.findByIsDeleted(false);
		log.info("Get all Templates: " + templates);
		return templates;
	}

	@Override
	public PaginationResponse findAll(PaginationRequest paginationRequest) {
		List<Template> templates = new ArrayList<Template>();

		// preparing where clause
		String whereClause = this.prepareWhereClause(paginationRequest).toString();

		// get list
		templates = this.templateDao.findAll(whereClause, paginationRequest);

		// getting count
		Long totalRecords = this.templateDao.getCount(whereClause);

		return CommonUtils.setPaginationResponse(paginationRequest.getPageNumber(), paginationRequest.getPageSize(),
				templates, totalRecords);
	}

	private StringBuilder prepareWhereClause(PaginationRequest paginationRequest) {
		Long subsidiaryId = null;
		Map<String, ?> filters = paginationRequest.getFilters();
		if (filters.containsKey(FilterNames.SUBSIDIARY_ID))
			subsidiaryId = ((Number) filters.get(FilterNames.SUBSIDIARY_ID)).longValue();
		StringBuilder whereClause = new StringBuilder();
		if (subsidiaryId != null && subsidiaryId != 0) {
			whereClause.append(" AND t.subsidiaryId = ").append(subsidiaryId);
		}
		return whereClause;
	}

	@Override
	public Template getTemplate(Long templateId) {

		Template template = null;
		log.info("Get Template by id started.");
		Optional<Template> optTemplate = templateRepository.findByTemplateIdAndIsDeleted(templateId, false);
		if (optTemplate.isPresent())	{
			template = optTemplate.get();
			List<TemplateSupplier> templateSuppliers = templateSupplierRepository.findByTemplateAndIsDeleted(template, false);
			template.setTemplateSuppliers(templateSuppliers);
		}
		log.info("Get Template: " + template);

		return template;
	}
}
