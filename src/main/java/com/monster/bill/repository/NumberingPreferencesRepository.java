package com.monster.bill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.monster.bill.models.NumberingPreferences;

@Repository
public interface NumberingPreferencesRepository extends JpaRepository<NumberingPreferences, String> {
	
	public List<NumberingPreferences> findByPreferenceIdAndIsDeleted(Long preferenceId, boolean isDeleted);
	
	public Optional<NumberingPreferences> findByIdAndIsDeleted(Long id, boolean isDeleted);

	@Query(" SELECT new com.monster.bill.models.NumberingPreferences(np.id, np.preferenceId, gp.subsidiaryId, np.masterName, np.masterPrefix, np.masterMaxDigit, np.masterStartsWith, np.masterCurrentNumber) "
			+ " FROM NumberingPreferences np "
			+ " INNER JOIN GeneralPreference gp ON np.preferenceId = gp.id "
			+ " WHERE gp.isActive is true AND gp.isDeleted is false AND np.isDeleted is false "
			+ " AND gp.subsidiaryId = :subsidiaryId AND np.masterName = :masterName ")
	public Optional<NumberingPreferences> findBySubsidiaryAndMaster(Long subsidiaryId, String masterName);

}
