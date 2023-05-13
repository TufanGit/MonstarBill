package com.monster.bill.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.GeneralPreferenceHistory;

@Repository
public interface PreferencesHistoryRepository extends JpaRepository<GeneralPreferenceHistory, String> {

	public List<GeneralPreferenceHistory> findByPreferenceId(Long id, Pageable pageable);

}
