package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Template;
import com.monster.bill.payload.request.PaginationRequest;

@Component("templateDao")
public interface TemplateDao {

	List<Template> findAll(String whereClause, PaginationRequest paginationRequest);

	Long getCount(String whereClause);



}
