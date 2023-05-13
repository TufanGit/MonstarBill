package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Rtv;
import com.monster.bill.payload.request.PaginationRequest;

@Component("rtvDao")
public interface RtvDao {
	public List<Rtv> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}
