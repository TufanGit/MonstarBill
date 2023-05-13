package com.monster.bill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.monster.bill.models.ApprovalPreference;
import com.monster.bill.models.ApprovalPreferenceHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.service.ApprovalPreferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/approval-preference")
@CrossOrigin(origins= "*", allowedHeaders = "*", maxAge = 4800, allowCredentials = "false" )
public class ApprovalPreferenceController {
	
	@Autowired
	private ApprovalPreferenceService approvalPreferenceService;	
	
	
	
	/**
	 * This saves the Approval Preference
	 * @param approvalPreference
	 * @return
	 */
	@PostMapping("/save")
	public ResponseEntity<ApprovalPreference> saveAccount(@RequestBody ApprovalPreference approvalPreference) {
		log.info("Saving the Approval Preference :: " + approvalPreference.toString());
		try {
			approvalPreference = approvalPreferenceService.save(approvalPreference);
			log.info("Approval Preference saved successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Error while saving the Approval Preference.");
		}
		return ResponseEntity.ok(approvalPreference);
	}
	
	/**
	 * get Approval Preference based on it's id
	 * @param id
	 * @return ApprovalPreference
	 * 
	 */
	@GetMapping("/get")
	public ResponseEntity<ApprovalPreference> getApprovalPreference(@RequestParam Long id) {
		return new ResponseEntity<>(approvalPreferenceService.getApprovalPreferenceById(id), HttpStatus.OK);
	}
	
	/**
	 * get list of ApprovalPreference with/without Filter 
	 * @return
	 */
	@PostMapping("/get/all")
	public ResponseEntity<PaginationResponse> findAll(@RequestBody PaginationRequest paginationRequest) {
		log.info("Get all ApprovalPreference started.");
		PaginationResponse paginationResponse = new PaginationResponse();
		paginationResponse = approvalPreferenceService.findAll(paginationRequest);
		log.info("Get all ApprovalPreference completed.");
		return new ResponseEntity<>(paginationResponse, HttpStatus.OK);
	}

	/**
	 * Find history by Account Id
	 * Supported for server side pagination
	 * @param id
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	@GetMapping("/get/history")
	public ResponseEntity<List<ApprovalPreferenceHistory>> findHistoryById(@RequestParam Long id, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "id") String sortColumn) {
		log.info("Get Account Audit  :: " + id);
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortColumn));
		List<ApprovalPreferenceHistory> ApprovalPreferenceHistoris = this.approvalPreferenceService.findHistoryById(id, pageable);
		log.info("Returning from Account Audit by id.");
		return new ResponseEntity<>(ApprovalPreferenceHistoris, HttpStatus.OK);
	}

}
