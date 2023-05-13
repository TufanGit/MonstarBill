package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.PrItem;

@Repository
public interface PrItemRepository extends JpaRepository<PrItem, String> {

	public Optional<PrItem> findByIdAndIsDeleted(Long id, boolean isDeleted);
	
	public List<PrItem> findAllByIsDeleted(boolean isDeleted);

	@Query("select new com.monster.bill.models.PrItem(pi.id, pi.itemId, pi.prId, pi.prNumber, i.name, pi.itemDescription, i.uom, pi.quantity, pi.rate, pi.estimatedAmount, pi.receivedDate, pi.poId, pi.memo) from PrItem pi INNER JOIN Item i ON i.id = pi.itemId WHERE pi.isDeleted is false AND pi.prId = :prId ")
	public List<PrItem> findByPrId(@Param("prId") Long prId);
	
	@Query("SELECT SUM(pi.estimatedAmount) FROM PrItem pi WHERE pi.isDeleted is false AND pi.prId = :prId")
	public Double findEstimatedAmountForPr(@Param("prId") Long prId);

	@Query(" select count(1) from PrItem WHERE rfqId is null AND prId = :prId ")
	public Long findUnprocessedItemsCountByRfq(Long prId);
	
	public List<PrItem> findByPrIdAndItemId(Long prId, Long itemId);

	@Query(" select count(1) from PrItem WHERE poId is null AND prId = :prId ")
	public Long findUnprocessedItemsCountByPo(Long prId);
}
