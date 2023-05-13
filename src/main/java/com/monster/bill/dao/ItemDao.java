package com.monster.bill.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.monster.bill.models.Item;
import com.monster.bill.payload.request.PaginationRequest;

@Component("itemDao")
public interface ItemDao {
	public List<Item> findAll(String whereClause, PaginationRequest paginationRequest);

	public Long getCount(String whereClause);
}
