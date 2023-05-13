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
import com.monster.bill.dao.MakePaymentDao;
import com.monster.bill.models.MakePayment;
import com.monster.bill.payload.request.PaginationRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("makePaymentDaoImpl")
public class MakePaymentDaoImpl implements MakePaymentDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public static final String GET_MAKE_PAYMENT = "select new com.monster.bill.models.MakePayment(mp.id, mp.paymentNumber, mp.subsidiaryId, mp.accountId, mp.supplierId, mp.currency, "
			+ " mp.paymentMode, mp.amount, s.name as subsidiaryName, su.name as supplierName, a.code as bankAccountName) "
			+ " FROM MakePayment mp inner join Subsidiary s ON s.id = mp.subsidiaryId inner join Supplier su ON su.id = mp.supplierId inner join Account a ON a.id = mp.accountId " 
			+ " WHERE 1=1 ";
	
	
	public static final String GET_MAKE_PAYMENT_COUNT = "select count(1) FROM MakePayment mp inner join Subsidiary s ON s.id = mp.subsidiaryId inner join Supplier su ON su.id = mp.supplierId inner join Account a ON a.id = mp.accountId WHERE 1=1  ";

	@Override
	public List<MakePayment> findAll(String whereClause, PaginationRequest paginationRequest) {
		List<MakePayment> makePayment = new ArrayList<MakePayment>();
		StringBuilder finalSql = new StringBuilder(GET_MAKE_PAYMENT);
		if (StringUtils.isNotEmpty(whereClause))
			finalSql.append(whereClause.toString());
		finalSql.append(
				CommonUtils.prepareOrderByClause(paginationRequest.getSortColumn(), paginationRequest.getSortOrder()));
		log.info("Final SQL to get all Payment " + finalSql.toString());
		try {
			TypedQuery<MakePayment> sql = this.entityManager.createQuery(finalSql.toString(), MakePayment.class);
			sql.setFirstResult(paginationRequest.getPageNumber() * paginationRequest.getPageSize());
			sql.setMaxResults(paginationRequest.getPageSize());
			makePayment = sql.getResultList();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the list of Payment :: " + ex.toString());
			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();
			throw new CustomException(errorExceptionMessage);
		}
		return makePayment;
	}

	@Override
	public Long getCount(String whereClause) {
		Long count = 0L;

		StringBuilder finalSql = new StringBuilder(GET_MAKE_PAYMENT_COUNT);
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
