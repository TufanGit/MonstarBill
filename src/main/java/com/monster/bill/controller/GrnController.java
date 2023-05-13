package com.monster.bill.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monster.bill.common.CustomException;
import com.monster.bill.models.Grn;
import com.monster.bill.models.GrnHistory;
import com.monster.bill.models.GrnItem;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.service.GrnService;

import lombok.extern.slf4j.Slf4j;

/**
 * All WS's of the Purchase Order and it's child components if any
 * 
 * @author Prithwish 10-09-2022
 */
@Slf4j
@RestController
@RequestMapping("/grn")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 4800, allowCredentials = "false")
public class GrnController {

	@Autowired
	private GrnService grnService;

	/**
	 * Save/update the GRN
	 * 
	 * @param grn
	 * @return
	 */
	@PostMapping("/save")
	public ResponseEntity<List<Grn>> save(@Valid @RequestBody List<Grn> grns) {
		log.info("Saving the GRN :: " + grns.toString());
		try {
			grns = grnService.save(grns);
		} catch (Exception e) {
			log.error("Error while saving the GRN :: ");
			e.printStackTrace();
			throw new CustomException("Error while saving the GRN " + e.toString());
		}
		log.info("GRN saved successfully");
		return ResponseEntity.ok(grns);
	}

	/**
	 * get the all values for GRN 
	 * @return
	 */
	@PostMapping("/get/all")
	public ResponseEntity<PaginationResponse> findAll(@RequestBody PaginationRequest paginationRequest) {
		log.info("Get all GRN started.");
		PaginationResponse paginationResponse = new PaginationResponse();
		paginationResponse = grnService.findAll(paginationRequest);
		log.info("Get all GRN completed.");
		return new ResponseEntity<>(paginationResponse, HttpStatus.OK);
	}
	
	/**
	 * get the GRN by id
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public ResponseEntity<Grn> findById(@RequestParam Long id) {
		log.info("Get GRN for ID :: " + id);
		Grn grn = grnService.getGrnById(id);
		if (grn == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		log.info("Returning from find by id GRN");
		return new ResponseEntity<>(grn, HttpStatus.OK);
	}
	
	/**
	 * delete the id by GRN
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/delete")
	public ResponseEntity<Boolean> deleteById(@RequestParam Long id) {
		log.info("Delete GRN by ID :: " + id);
		boolean isDeleted = false;
		isDeleted = grnService.deleteById(id);
		log.info("GRN by ID Completed.");
		return new ResponseEntity<>(isDeleted, HttpStatus.OK);
	}
	
	/**
	 * Find history by GRN number
	 * Supported for server side pagination
	 * @param grnNumber
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	@GetMapping("/get/history")
	public ResponseEntity<List<GrnHistory>> findHistoryById(@RequestParam String grnNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "id") String sortColumn) {
		log.info("Get GRN Audit for grn ID :: " + grnNumber);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortColumn));
		List<GrnHistory> grnHistories = this.grnService.findHistoryById(grnNumber, pageable);
		log.info("Returning from grn Audit by GRN Number.");
		return new ResponseEntity<>(grnHistories, HttpStatus.OK);
	}
	
	/**
	 * Find all the GRN Items by the GRN Number
	 * @param grnNumber
	 * @return
	 */
	@GetMapping("/find-grn-items")
	public ResponseEntity<List<GrnItem> > findItemsByGrnNumber(@RequestParam String grnNumber) {
		log.info("Get GRN Items for GrnNumber :: " + grnNumber);
		List<GrnItem> grnItems = grnService.findGrnItemsByGrnNumber(grnNumber);
		if (CollectionUtils.isEmpty(grnItems)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		log.info("Find GRN Items by GRN Number completed.");
		return new ResponseEntity<>(grnItems, HttpStatus.OK);
	}
	
	
	@GetMapping("/getByGrnId")
    public Grn getByGrnId(@RequestParam Long grnId)
    {
        return grnService.getByGrnId(grnId);
    }
	
	@GetMapping("/getByGrnItemId")
    public GrnItem getByGrnItemId(@RequestParam Long grnId, @RequestParam Long itemId)
    {
        return grnService.getByGrnItemId(grnId, itemId);
    }
	/**
	 * Get GRNs
	 * 
	 * @param poId
	 * @return GRNs
	 */
	@GetMapping("/getByPoId")
    public List<Grn> getByPoId(@RequestParam Long poId)
    {
        return grnService.getByPoId(poId);
    }
	
}
