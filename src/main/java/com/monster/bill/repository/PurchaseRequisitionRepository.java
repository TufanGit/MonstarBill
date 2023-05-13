package com.monster.bill.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.PurchaseRequisition;
import com.monster.bill.payload.response.IdNameResponse;

@Repository
public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisition, String> {

	public Optional<PurchaseRequisition> findByIdAndIsDeleted(Long id, boolean isDeleted);

//	public Optional<PurchaseRequisition> findByPrNumberAndIsDeleted(String prNumber, boolean isDeleted);

	@Query("select new com.monster.bill.payload.response.IdNameResponse(pr.id, pr.prNumber) from PurchaseRequisition pr "
			+ " LEFT JOIN QuotationPr qp ON qp.prId = pr.id "
			+ " WHERE qp.prId is NULL AND pr.isDeleted = :isDeleted order by pr.prNumber asc ")
	public List<IdNameResponse> findDistinctPrNumbers(@Param("isDeleted") boolean isDeleted);

	public List<PurchaseRequisition> findBySubsidiaryIdAndLocationIdAndIsDeletedAndPrStatusIn(Long subsidiaryId, Long locationId, boolean isDeleted, List<String> status);
	
	default Map<Long, String> findPrIdAndNumberMap(Long subsidiaryId, Long locationId, boolean isDeleted, List<String> status) {
        return this.findBySubsidiaryIdAndLocationIdAndIsDeletedAndPrStatusIn(subsidiaryId, locationId, isDeleted, status).stream().collect(Collectors.toMap(PurchaseRequisition::getId, PurchaseRequisition::getPrNumber));
    }
	
	@Query("select new com.monster.bill.models.PurchaseRequisition(p.id, p.subsidiaryId, p.requestor, p.prNumber, p.prDate, p.department, p.rejectedComments, s.name as subsidiaryName, p.locationId, l.locationName as locationName ,p.prStatus,"
			+ " p.approvedBy, p.nextApprover, p.nextApproverRole, e.fullName ) "
			+ " from PurchaseRequisition p "
			+ " inner join Subsidiary s ON s.id = p.subsidiaryId "
			+ " inner join Location l ON l.id = p.locationId "
			+ " left join Employee e ON CAST(e.id as text) = p.approvedBy "
			+ " where p.prStatus in :status AND p.isDeleted is false and p.nextApprover = :userId ")
	public List<PurchaseRequisition> findAllByPrStatus(List<String> status, String userId);

	@Query(" SELECT SUM(estimatedAmount) FROM PrItem WHERE prId = :prId ")
	public Double findTotalEstimatedAmountForPr(@Param("prId") Long id);
	
	public List<PurchaseRequisition> findByNextApprover(String nextApprover);

	@Query(" select new com.monster.bill.payload.response.IdNameResponse(id, prNumber) from PurchaseRequisition pr WHERE subsidiaryId = :subsidiaryId AND isDeleted = :isDeleted AND prStatus IN ('Approved', 'Partially Processed') order by id asc ")
	public List<IdNameResponse> findDistinctApprovedPrNumbersBySubsidiary(Long subsidiaryId, boolean isDeleted);

}
