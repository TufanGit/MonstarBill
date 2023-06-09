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
import com.monster.bill.dao.AdvancePaymentDao;
import com.monster.bill.models.AdvancePayment;
import com.monster.bill.payload.request.PaginationRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("advancePaymentDaoImpl")
public class AdvancePaymentDaoImpl implements AdvancePaymentDao {

	@PersistenceContext
	private EntityManager entityManager;

	public static final String GET_ADVANCE_PAYMENT = "select new com.monster.bill.models.AdvancePayment(ap.id, ap.advanceAmount, ap.paymentAmount, ap.prePaymentNumber, ap.subsidiaryId, ap.supplierId, ap.prePaymentDate, ap.rejectedComments, ap.memo, ap.status, ap.unappliedAmount, "
			+ "	s.name as subsidiaryName, su.name as supplierName) "
			+ " FROM AdvancePayment ap inner join Subsidiary s ON s.id = ap.subsidiaryId inner join Supplier su ON su.id = ap.supplierId "
			+ " WHERE 1=1 ";

	public static final String GET_ADVANCE_PAYMENT_COUNT = "select count(1) FROM AdvancePayment ap inner join Subsidiary s ON s.id = ap.subsidiaryId inner join Supplier su ON su.id = ap.supplierId WHERE 1=1  ";

	@Override
	public List<AdvancePayment> findAll(String whereClause, PaginationRequest paginationRequest) {
		List<AdvancePayment> advancePayment = new ArrayList<AdvancePayment>();
		StringBuilder finalSql = new StringBuilder(GET_ADVANCE_PAYMENT);
		if (StringUtils.isNotEmpty(whereClause))
			finalSql.append(whereClause.toString());
		finalSql.append(
				CommonUtils.prepareOrderByClause(paginationRequest.getSortColumn(), paginationRequest.getSortOrder()));
		log.info("Final SQL to get all Payment " + finalSql.toString());
		try {
			TypedQuery<AdvancePayment> sql = this.entityManager.createQuery(finalSql.toString(), AdvancePayment.class);
			sql.setFirstResult(paginationRequest.getPageNumber() * paginationRequest.getPageSize());
			sql.setMaxResults(paginationRequest.getPageSize());
			advancePayment = sql.getResultList();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the list of Payment :: " + ex.toString());
			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();
			throw new CustomException(errorExceptionMessage);
		}
		return advancePayment;
	}

	@Override
	public Long getCount(String whereClause) {
		Long count = 0L;

		StringBuilder finalSql = new StringBuilder(GET_ADVANCE_PAYMENT_COUNT);
		// where clause
		if (StringUtils.isNotEmpty(whereClause))
			finalSql.append(whereClause.toString());

		log.info("Final SQL to get all payment Count w/w/o filter :: " + finalSql.toString());
		try {
			TypedQuery<Long> sql = this.entityManager.createQuery(finalSql.toString(), Long.class);
			count = sql.getSingleResult();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the count of payment :: " + ex.toString());

			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();

			throw new CustomException(errorExceptionMessage);
		}
		return count;
	}

}
