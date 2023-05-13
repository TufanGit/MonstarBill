package com.monster.bill.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.monster.bill.common.CommonUtils;
import com.monster.bill.common.CustomException;
import com.monster.bill.dao.PurchaseRequisitionDao;
import com.monster.bill.enums.TransactionStatus;
import com.monster.bill.models.PrItem;
import com.monster.bill.models.PurchaseRequisition;
import com.monster.bill.payload.request.PaginationRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("purchaseRequisitionDaoImpl")
public class PurchaseRequisitionDaoImpl implements PurchaseRequisitionDao {

	@PersistenceContext
	private EntityManager entityManager;

	public static final String GET_PR = "select new com.monster.bill.models.PurchaseRequisition(p.id, p.prNumber, p.prDate, p.department, p.requestor, p.subsidiaryId, p.prStatus, p.locationId, s.name as subsidiaryName, l.locationName as locationName ) "
			+ " FROM PurchaseRequisition p inner join Subsidiary s ON s.id = p.subsidiaryId inner join Location l ON l.id = p.locationId WHERE 1=1 ";
	
	public static final String GET_PR_COUNT = "select count(p) FROM PurchaseRequisition p inner join Subsidiary s ON s.id = p.subsidiaryId inner join Location l ON l.id = p.locationId WHERE 1=1 ";

	private static final String GET_UNPROCESSED_ITEMS = " select new com.monster.bill.models.PrItem(pri.prId, pri.itemId, i.name, pri.itemDescription, i.integratedId, i.uom, pri.quantity, pri.receivedDate, pri.prNumber, pr.department, pri.rate, "
			+ " case  "
			+ " 	when lower(i.category) = lower('Inventory Item')  "
			+ " 	then i.assetAccountId  "
			+ " 	else i.expenseAccountId end as accountId, pr.locationId, l.locationName) "
			+ " from PrItem pri "
			+ " LEFT JOIN PurchaseOrderItem poi ON pri.prId = poi.prId and pri.itemId = poi.itemId "
			+ " INNER JOIN Item i ON i.id = pri.itemId "
			+ " INNER JOIN PurchaseRequisition pr ON pr.id = pri.prId "
			+ " LEFT JOIN Location l ON l.id = pr.locationId "
			+ " WHERE pri.prId = :prId and poi.itemId is null ";
	
	public static final String GET_PR_APPROVED = "select new com.monster.bill.models.PurchaseRequisition(pr.id, pr.prNumber,pr.subsidiaryId, pr.type, pr.locationId, pr.prDate, pr.currency, pr.rejectedComments, pr.prStatus, s.name as subsidiaryName, l.locationName as locationName ) "
			+ " FROM PurchaseRequisition pr inner join Subsidiary s ON s.id = pr.subsidiaryId inner join Location l ON l.id = pr.locationId " 
			+ " WHERE 1=1 AND pr.prStatus IN ('"+TransactionStatus.APPROVED.getTransactionStatus()+"','"+TransactionStatus.PARTIALLY_PROCESSED.getTransactionStatus()+"')"
			+ " AND pr.subsidiaryId = :subsidiaryId ";

	public static final String GET_APPROVED_PR_COUNT = "select count(1) FROM PurchaseRequisition pr "
			+ " WHERE 1=1 AND pr.prStatus IN ('"+TransactionStatus.APPROVED.getTransactionStatus()+"','"+TransactionStatus.PARTIALLY_PROCESSED.getTransactionStatus()+"')"
			+ " AND pr.subsidiaryId = :subsidiaryId ";;

	@Override
	public List<PurchaseRequisition> findAll(String whereClause, PaginationRequest paginationRequest) {
		List<PurchaseRequisition> purchaseRequisitions = new ArrayList<PurchaseRequisition>();

		StringBuilder finalSql = new StringBuilder(GET_PR);
		// where clause
		if (StringUtils.isNotEmpty(whereClause))finalSql.append(whereClause.toString());
		// order by clause
		finalSql.append(CommonUtils.prepareOrderByClause(paginationRequest.getSortColumn(), paginationRequest.getSortOrder()));

		log.info("Final SQL to get all Purchase Requisition w/w/o filter :: " + finalSql.toString());
		try {
			TypedQuery<PurchaseRequisition> sql = this.entityManager.createQuery(finalSql.toString(), PurchaseRequisition.class);
			// pagination
			sql.setFirstResult(paginationRequest.getPageNumber() * paginationRequest.getPageSize());
			sql.setMaxResults(paginationRequest.getPageSize());
			purchaseRequisitions = sql.getResultList();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the list of Purchase Requisitions :: " + ex.toString());

			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();

			throw new CustomException(errorExceptionMessage);
		}
		return purchaseRequisitions;
	}

	@Override
	public Long getCount(String whereClause) {
		Long count = 0L;
		
		StringBuilder finalSql = new StringBuilder(GET_PR_COUNT);
		// where clause
		if (StringUtils.isNotEmpty(whereClause)) finalSql.append(whereClause.toString());
		
		log.info("Final SQL to get all PR Count w/w/o filter :: " + finalSql.toString());
		try {
			TypedQuery<Long> sql = this.entityManager.createQuery(finalSql.toString(), Long.class);
			count = sql.getSingleResult();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the count of PR :: " + ex.toString());
			
			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null) errorExceptionMessage = ex.toString();
			
			throw new CustomException(errorExceptionMessage);
		}
		return count;
	}

	@Override
	public List<PrItem> findUnprocessedItemsByPrId(Long prId) {
		List<PrItem> prItems = new ArrayList<PrItem>();

		StringBuilder finalSql = new StringBuilder(GET_UNPROCESSED_ITEMS);
		log.info("Final SQL to get all unprocessed Purchase Requisition Items w/w/o filter :: " + finalSql.toString());
		
		try {
			TypedQuery<PrItem> sql = this.entityManager.createQuery(finalSql.toString(), PrItem.class);
			sql.setParameter("prId", prId);
			prItems = sql.getResultList();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the list of Purchase Requisitions Items :: " + ex.toString());

			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();

			throw new CustomException(errorExceptionMessage);
		}
		return prItems;
	}
	
	@Override
	public List<PurchaseRequisition> findAllApprovedPr(String whereClause, PaginationRequest paginationRequest, Long subsidiaryId) {
		List<PurchaseRequisition> purchaseRequisitions = new ArrayList<PurchaseRequisition>();

		StringBuilder finalSql = new StringBuilder(GET_PR_APPROVED);
		// where clause
		if (StringUtils.isNotEmpty(whereClause))finalSql.append(whereClause.toString());
		// order by clause
		finalSql.append(CommonUtils.prepareOrderByClause(paginationRequest.getSortColumn(), paginationRequest.getSortOrder()));

		log.info("Final SQL to get all Purchase Requisition Approved w/w/o filter :: " + finalSql.toString());
		try {
			TypedQuery<PurchaseRequisition> sql = this.entityManager.createQuery(finalSql.toString(), PurchaseRequisition.class);
			sql.setParameter("subsidiaryId", subsidiaryId);
			// pagination
			sql.setFirstResult(paginationRequest.getPageNumber() * paginationRequest.getPageSize());
			sql.setMaxResults(paginationRequest.getPageSize());
			purchaseRequisitions = sql.getResultList();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the list of approved Purchase Requisitions  :: " + ex.toString());

			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();

			throw new CustomException(errorExceptionMessage);
		}
		return purchaseRequisitions;
	}
	
	@Override
	public Long getCountPrApproved(String whereClause, Long subsidiaryId) {
		Long count = 0L;
		
		StringBuilder finalSql = new StringBuilder(GET_APPROVED_PR_COUNT);
		// where clause
		if (StringUtils.isNotEmpty(whereClause)) finalSql.append(whereClause.toString());
		
		log.info("Final SQL to get all Approved PR Count w/w/o filter :: " + finalSql.toString());
		try {
			TypedQuery<Long> sql = this.entityManager.createQuery(finalSql.toString(), Long.class);
			sql.setParameter("subsidiaryId", subsidiaryId);
			count = sql.getSingleResult();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the count of PR :: " + ex.toString());
			
			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null) errorExceptionMessage = ex.toString();
			
			throw new CustomException(errorExceptionMessage);
		}
		return count;
	}
}
