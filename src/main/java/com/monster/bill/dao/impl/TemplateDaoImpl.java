package com.monster.bill.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.monster.bill.common.CommonUtils;
import com.monster.bill.common.CustomException;
import com.monster.bill.dao.TemplateDao;
import com.monster.bill.models.Template;
import com.monster.bill.payload.request.PaginationRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("templateDao")

public class TemplateDaoImpl implements TemplateDao {

	@PersistenceContext
	private EntityManager entityManager;

	public static final String GET_TEMPLATE = "select t from Template t where 1=1 ";

	public static final String GET_TEMPLATE_COUNT = "select count(*) from Template t";

	@Override
	public List<Template> findAll(String whereClause, PaginationRequest paginationRequest) {
		List<Template> template = new ArrayList<Template>();
		StringBuilder finalSql = new StringBuilder(GET_TEMPLATE);
		if (StringUtils.isNotEmpty(whereClause))
			finalSql.append(whereClause.toString());
		
		finalSql.append(CommonUtils.prepareOrderByClause(paginationRequest.getSortColumn(), paginationRequest.getSortOrder()));
		log.info("Final SQL to get all Template " + finalSql.toString());
		try {
			TypedQuery<Template> sql = this.entityManager.createQuery(finalSql.toString(), Template.class);
			sql.setFirstResult(paginationRequest.getPageNumber() * paginationRequest.getPageSize());
			sql.setMaxResults(paginationRequest.getPageSize());
			template = sql.getResultList();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the list of Template :: " + ex.toString());
			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();
			throw new CustomException(errorExceptionMessage);
		}
		return template;
	}

	@Override
	public Long getCount(String whereClause) {
		Long count = 0L;

		StringBuilder finalSql = new StringBuilder(GET_TEMPLATE_COUNT);
		// where clause
		if (StringUtils.isNotEmpty(whereClause))
			finalSql.append(whereClause.toString());

		log.info("Final SQL to get all Template Count w/w/o filter :: " + finalSql.toString());
		try {
			TypedQuery<Long> sql = this.entityManager.createQuery(finalSql.toString(), Long.class);
			count = sql.getSingleResult();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the count of Template :: " + ex.toString());

			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();

			throw new CustomException(errorExceptionMessage);
		}
		return count;
	}
}
