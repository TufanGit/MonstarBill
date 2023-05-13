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
import com.monster.bill.dao.DocumentSequenceDao;
import com.monster.bill.models.DocumentSequence;
import com.monster.bill.payload.request.PaginationRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("documentSequenceDaoImpl")
public class DocumentSequenceDaoImpl implements DocumentSequenceDao {

	@PersistenceContext
	private EntityManager entityManager;

//	@Autowired
//	private DocumentSequenceRepository documentSequenceRepository;

	public static final String GET_DOCUMENT_SEQUENCE = "select new com.monster.bill.models.DocumentSequence(ds.id, ds.type, ds.initialNumber, ds.prefix,"
			+ " ds.suffix, ds.minimumDigit, ds.startDate, ds.endDate, ds.subsidiaryId, s.name as subsidiaryName) "
			+ " FROM DocumentSequence ds inner join Subsidiary s ON s.id = ds.subsidiaryId " + " WHERE 1=1 ";

	public static final String GET_DOCUMENT_SEQUENCE_COUNT = "select count(1) FROM DocumentSequence ds inner join Subsidiary s ON s.id = ds.subsidiaryId WHERE 1=1  ";

//	public static final String GET_DOCUMENT_SEQUENCE_NUMBER = "SELECT c from DocumentSequence c where "
//			+ " subsidiaryId = :subsidiaryId " + " and type = :type and isDeleted = :isDeleted AND "
//			+ " (to_char(startDate, 'yyyy-MM-dd') <= :transactionalDate) ";

	@Override
	public List<DocumentSequence> findAll(String whereClause, PaginationRequest paginationRequest) {
		List<DocumentSequence> documentSequence = new ArrayList<DocumentSequence>();
		StringBuilder finalSql = new StringBuilder(GET_DOCUMENT_SEQUENCE);
		if (StringUtils.isNotEmpty(whereClause))
			finalSql.append(whereClause.toString());
		finalSql.append(
				CommonUtils.prepareOrderByClause(paginationRequest.getSortColumn(), paginationRequest.getSortOrder()));
		log.info("Final SQL to get all Document sequence " + finalSql.toString());
		try {
			TypedQuery<DocumentSequence> sql = this.entityManager.createQuery(finalSql.toString(),
					DocumentSequence.class);
			sql.setFirstResult(paginationRequest.getPageNumber() * paginationRequest.getPageSize());
			sql.setMaxResults(paginationRequest.getPageSize());
			documentSequence = sql.getResultList();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the list of Document sequence :: " + ex.toString());
			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();
			throw new CustomException(errorExceptionMessage);
		}
		return documentSequence;
	}

	@Override
	public Long getCount(String whereClause) {
		Long count = 0L;

		StringBuilder finalSql = new StringBuilder(GET_DOCUMENT_SEQUENCE_COUNT);
		// where clause
		if (StringUtils.isNotEmpty(whereClause))
			finalSql.append(whereClause.toString());

		log.info("Final SQL to get all Document sequence Count w/w/o filter :: " + finalSql.toString());
		try {
			TypedQuery<Long> sql = this.entityManager.createQuery(finalSql.toString(), Long.class);
			count = sql.getSingleResult();
		} catch (Exception ex) {
			log.error("Exception occured at the time of fetching the count of Document sequence :: " + ex.toString());

			String errorExceptionMessage = ex.getLocalizedMessage();
			if (errorExceptionMessage == null)
				errorExceptionMessage = ex.toString();

			throw new CustomException(errorExceptionMessage);
		}
		return count;
	}

//	@Override
//	public Optional<DocumentSequence> findBySubsidiaryIdAndTypeAndisDeleted(Long subsidiaryId, String type,
//			boolean isDeleted, String transactionalDate) {
//		Optional<DocumentSequence> documentSequences = Optional.empty();
//		documentSequences = this.documentSequenceRepository.getBySubsidiaryIdAndTypeAndIsDeleted(subsidiaryId, type,
//				isDeleted);
//		if (!documentSequences.isPresent()) {
//			log.error("Document Sequence the correct value is not present against the subsidiary  : " + subsidiaryId
//					+ " : and type : " + type);
//			throw new CustomException("Document Sequence the correct value is not present against the subsidiary  : "
//					+ subsidiaryId + " : and type : " + type);
//		}
//		StringBuilder finalSql = new StringBuilder(GET_DOCUMENT_SEQUENCE_NUMBER);
//		if (documentSequences.get().getEndDate() != null) {
//			finalSql.append(" AND (to_char(endDate, 'yyyy-MM-dd') >= :transactionalDate) ");
//		}
//		try {
//			TypedQuery<DocumentSequence> sql = this.entityManager.createQuery(finalSql.toString(),
//					DocumentSequence.class);
//			sql.setParameter("subsidiaryId", subsidiaryId);
//			sql.setParameter("type", type);
//			sql.setParameter("isDeleted", isDeleted);
//			sql.setParameter("transactionalDate", transactionalDate);
//			try {
//				DocumentSequence ds = sql.getSingleResult();
//				if (ds != null) {
//					documentSequences = Optional.of(ds);	
//				}
//			} catch (Exception e) {
//				throw new CustomException("Document Sequence configuration is not found against selected subsidiary.");
//			}
//		} catch (Exception ex) {
//			log.error("Exception occured at the time of fetching the object for number genaration :: " + ex.toString());
//
////			String errorExceptionMessage = ex.getLocalizedMessage();
////			if (errorExceptionMessage == null)
////				errorExceptionMessage = ex.toString();
//
//			throw new CustomException(ex.getLocalizedMessage());
//		}
//		return documentSequences;
//	}
}
