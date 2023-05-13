package com.monster.bill.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monster.bill.common.CommonUtils;
import com.monster.bill.common.CustomException;
import com.monster.bill.common.CustomMessageException;
import com.monster.bill.common.FilterNames;
import com.monster.bill.models.ManageIntegration;
import com.monster.bill.models.ManageIntegrationSubsidiary;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;
import com.monster.bill.repository.ManageIntegrationRepository;
import com.monster.bill.repository.ManageIntegrationSubsidiaryRepository;
import com.monster.bill.repository.SubsidiaryRepository;
import com.monster.bill.service.ManageIntegrationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ManageIntegrationServiceImpl implements ManageIntegrationService {
	
	@Autowired
	private ManageIntegrationRepository manageIntegrationRepository;
	
	@Autowired
	private SubsidiaryRepository subsidiaryRepository;

	@Autowired
	private ManageIntegrationSubsidiaryRepository manageIntegrationSubsidiaryRepository;

	@Override
	public List<ManageIntegration> save(List<ManageIntegration> manageIntegrations) {
		for (ManageIntegration manageIntegration : manageIntegrations) {
			String username = CommonUtils.getLoggedInUsername();
			if (manageIntegration.getId() == null) {
				manageIntegration.setCreatedBy(CommonUtils.getLoggedInUsername());
			}
			manageIntegration.setLastModifiedBy(CommonUtils.getLoggedInUsername());
			ManageIntegration savedManageIntegration;
			try {
				savedManageIntegration = this.manageIntegrationRepository.save(manageIntegration);
			}  catch (DataIntegrityViolationException e) {
				log.error(" Manage integration unique constrain violetd." + e.getMostSpecificCause());
				throw new CustomException(" Manage integration unique constrain violetd :" + e.getMostSpecificCause());
			}
			log.info("Manage Integration is updated successfully" + savedManageIntegration);
		
			Long integrationId = savedManageIntegration.getId();
		/**
		 * Save the subsidiary and manage the history
		 */
		List<ManageIntegrationSubsidiary> manageIntegrationSubsidiaries = manageIntegration
				.getManageIntegrationSubsidiaries();
		if (CollectionUtils.isNotEmpty(manageIntegrationSubsidiaries)) {
			this.saveIntegrationSubsidiary(username,integrationId, manageIntegrationSubsidiaries);
			savedManageIntegration.setManageIntegrationSubsidiaries(manageIntegrationSubsidiaries);
		}
	}
		return manageIntegrations;
	}

	private void saveIntegrationSubsidiary(String username, Long integrationId,
			List<ManageIntegrationSubsidiary> manageIntegrationSubsidiaries) {
		for (ManageIntegrationSubsidiary manageIntegrationSubsidiary : manageIntegrationSubsidiaries) {
			if (manageIntegrationSubsidiary.getId() == null) {
				// if account subsidiary is new
				manageIntegrationSubsidiary.setIntigrationId(integrationId);
				manageIntegrationSubsidiary.setCreatedBy(username);
				manageIntegrationSubsidiary.setLastModifiedBy(username);
				// save in table
				manageIntegrationSubsidiary = this.manageIntegrationSubsidiaryRepository.save(manageIntegrationSubsidiary);
				if (manageIntegrationSubsidiary == null) {
					log.info("Error while saving the manage integration Subsidiary.");
					throw new CustomMessageException("Error while saving the manage integration Subsidiary.");
				}			
				log.info("manage integration subsidiary is inserted.");
			} else if (manageIntegrationSubsidiary.isDeleted()) { 
				// if account subsidiary is Delete
				manageIntegrationSubsidiary.setLastModifiedBy(username);
				// save in table
				manageIntegrationSubsidiary = this.manageIntegrationSubsidiaryRepository.save(manageIntegrationSubsidiary);
				if (manageIntegrationSubsidiary == null) {
					log.info("Error while saving the manage integration Subsidiary.");
					throw new CustomMessageException("Error while saving the manage integration Subsidiary.");
				}
				log.info("manage integration subsidiary is Deleted.");
			}
		}	
	}

	@Override
	public ManageIntegration getManageIntegrationService(Long id) {
		Optional<ManageIntegration> manageIntegration = Optional.empty();
		manageIntegration = manageIntegrationRepository.findByIdAndIsDeleted(id, false);
		// get subsidiary
		if (manageIntegration.isPresent()) {
			Long manageIntegrationId = manageIntegration.get().getId();
			log.info("Integration found against given id : " + id);
		
			List<ManageIntegrationSubsidiary> manageIntegrationSubsidiary = manageIntegrationSubsidiaryRepository.findByIntigrationId(manageIntegrationId);
			log.info("Integration subsidiary found against given id : " + manageIntegrationSubsidiary);
			if (CollectionUtils.isNotEmpty(manageIntegrationSubsidiary)) {
				manageIntegration.get().setManageIntegrationSubsidiaries(manageIntegrationSubsidiary);
			}
		} else {
			log.error("Integration Not Found against given Integration id : " + id);
			throw new CustomMessageException("Integration Not Found against given Integration id : " + id);
		}
		return manageIntegration.get();
	}

	@Override
	public PaginationResponse getManageIntegrationServices(PaginationRequest paginationRequest) {
		List<ManageIntegration> manageIntegrations = null;
		log.info("Get all Manage Integration started.");
		Specification<ManageIntegration> specification = new Specification<ManageIntegration>() {
			@Override
			public Predicate toPredicate(Root<ManageIntegration> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				String intigrationWith = null;
				String accountId = null;
				String startDate = null;
				String endDate = null;
				Map<String, ?> filters = paginationRequest.getFilters();			
				if (filters.containsKey(FilterNames.INTEGRATION_WITH))
					intigrationWith = ((String) filters.get(FilterNames.INTEGRATION_WITH));
				if (filters.containsKey(FilterNames.ACCOUNT_ID))
					accountId = ((String) filters.get(FilterNames.ACCOUNT_ID));
				if (filters.containsKey(FilterNames.START_DATE)) 
					startDate = (String) filters.get(FilterNames.START_DATE);
				if (filters.containsKey(FilterNames.END_DATE)) 
					endDate = (String) filters.get(FilterNames.END_DATE);
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(intigrationWith))
					predicates.add(criteriaBuilder.equal(root.get("intigrationWith"), intigrationWith));
				
				if (accountId != null && !accountId.isEmpty())
					predicates.add(criteriaBuilder.equal(root.get("accountId"), accountId));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if (startDate!= null && !startDate.isEmpty()) {
						predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("startDate"), sdf.parse(startDate)));
					}
					if (endDate!= null && !endDate.isEmpty()) {
						predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("endDate"), sdf.parse(endDate)));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
			}
		};
		Pageable pageable = null;
		Sort sort = paginationRequest.getSortOrder().equalsIgnoreCase("asc")?
                Sort.by(paginationRequest.getSortColumn()).ascending(): Sort.by(paginationRequest.getSortColumn()).descending();
		pageable = PageRequest.of(paginationRequest.getPageNumber(), paginationRequest.getPageSize(), sort);
		
		Page<ManageIntegration> pgManageIntegrations = manageIntegrationRepository.findAll(specification, pageable);
		if(pgManageIntegrations.hasContent())
			manageIntegrations = pgManageIntegrations.getContent();
//		if(CollectionUtils.isNotEmpty(manageIntegrations))
//			manageIntegrations.forEach(manageIntegration -> {
//				List<ManageIntegrationSubsidiary> manageIntegrationSubsidiaries = manageIntegrationSubsidiaryRepository.findByIntigrationIdAndIsDeleted(manageIntegration.getId(), false);
//				manageIntegration.setManageIntegrationSubsidiaries(manageIntegrationSubsidiaries);	
//			});
		Long totalRecords = manageIntegrationRepository.count();
		return CommonUtils.setPaginationResponse(paginationRequest.getPageNumber(), paginationRequest.getPageSize(),
				manageIntegrations, totalRecords);
	}
}
