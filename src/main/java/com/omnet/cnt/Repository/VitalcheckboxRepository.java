package com.omnet.cnt.Repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Vitalcheckbox;

@Repository
@EnableJpaRepositories
public interface VitalcheckboxRepository extends JpaRepository<Vitalcheckbox,String>{

	@Query(value="SELECT COUNT(*) as No_Contact  FROM INMATE_ENEMY a WHERE A.COMMIT_NO = :COMMIT_NO ",nativeQuery = true)
	Map<String, Object> givecontactinfo(String COMMIT_NO);

	@Query(value="SELECT COUNT (*) as Mental_Health FROM INMATE_EVALUATION_DTL WHERE COMMIT_NO = :COMMIT_NO ",nativeQuery=true)
	Map<String, Object> Mentalhealth(String COMMIT_NO);
	
	@Query(value="SELECT COUNT(*) as Legal_Issues FROM OUTSTAND_LEGAL_ISSUE A  WHERE A.COMMIT_NO = :COMMIT_NO AND OUTSTANDING_STATUS = 'OPEN' ",nativeQuery=true) 
	Map<String, Object> LegalIssue(String COMMIT_NO);
	
	@Query(value = "SELECT Spkg_Cbi_Vsta.sf_get_state(:v_commit_no) from dual", nativeQuery = true)
	Map<String, Object> state(String v_commit_no);
	
	@Query(value = "select  sf_get_sex_offender_flag(:COMMIT_NO) from dual", nativeQuery = true)
	Map<String, Object> sexHistory(String COMMIT_NO);
	
}
