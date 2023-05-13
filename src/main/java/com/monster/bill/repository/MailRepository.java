package com.monster.bill.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.monster.bill.models.Mail;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
	
	@Query(value = "SELECT max(receiveDate) FROM Mail where isMail = ?1 and toId = ?2")
	public Date getMaxReceiveDate(boolean isMail, String toId);
	
	public List<Mail> findByToId(String toId);

}
