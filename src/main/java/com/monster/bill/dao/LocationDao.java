package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Location;
import com.monster.bill.payload.request.PaginationRequest;

@Component("locationDao")
public interface LocationDao {
	public List<Location> findAll(String whereClause,  PaginationRequest paginationRequest);

	public Long getCount(String whereClause);

}
