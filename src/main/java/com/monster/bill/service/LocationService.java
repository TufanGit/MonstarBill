package com.monster.bill.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.monster.bill.models.Location;
import com.monster.bill.models.LocationAddress;
import com.monster.bill.models.LocationHistory;
import com.monster.bill.payload.request.PaginationRequest;
import com.monster.bill.payload.response.PaginationResponse;

public interface LocationService {
	
	public Location save(Location location);

	public Location getLocationById(Long id);
	
	public LocationAddress saveAddress(LocationAddress locationAddress);

	public LocationAddress getAddressById(Long id);

	public boolean deleteById(Long id);

	public List<LocationHistory> findHistoryById(Long id, Pageable pageable);

	public PaginationResponse findAll(PaginationRequest paginationRequest);

	public List<Location> getParentLocationNames(Long subsidiaryId);

	public Map<Long, String> getAllLocations(Long subsidiaryId);

	public Boolean getValidateName(String name);

	public List<Location> getLocationNames(List<Long> subsidiaryId);

}
