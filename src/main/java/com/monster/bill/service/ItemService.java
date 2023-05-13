package com.monster.bill.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.monster.bill.models.Item;
import com.monster.bill.models.ItemHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface ItemService {

	public Item save(Item item);

	public Item findById(Long id);

	public List<ItemHistory> findHistoryById(Long id, Pageable pageable);

	public boolean deleteById(Long id);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public List<Item> findBySubsidiaryId(Long subsidiaryId);

	public Boolean getValidateName(String name);

	public byte[] upload(MultipartFile file);

	public List<Item> getByDescriptionMatching(String description, Long subsidiaryId);

	public byte[] downloadTemplate();
	
	public List<Item> findDescriptionLike(String description ,Long SubsidiaryId);
	
	public Map<String, Long> getdashboard(Long subsidiaryId);

}
