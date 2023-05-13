package com.monster.bill.service;

import java.util.List;

import com.monster.bill.models.ManageIntegration;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface ManageIntegrationService {
	
public	List<ManageIntegration> save(List<ManageIntegration> manageIntegrations);

public ManageIntegration getManageIntegrationService(Long id);

public PaginationResponse getManageIntegrationServices(PaginationRequest paginationRequest);

}
