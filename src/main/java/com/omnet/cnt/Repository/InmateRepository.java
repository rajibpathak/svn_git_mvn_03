package com.omnet.cnt.Repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.omnet.cnt.Model.Inmate;


public interface InmateRepository extends JpaRepository<Inmate, String> {
	
	@Query(value = "Select SF_VALIDATE_SBI_NO_YN(:parameter) from dual ", nativeQuery = true)
	    String findSbiNumber(@Param("parameter") String parameter);
	 @Query(value = "Select  sf_get_commit_no (:parameter) from dual", nativeQuery = true)
	    String findCommitNumber(@Param("parameter") String parameter); 
	 @Query(value = "SELECT sf_get_inmate_cond(:ivc_commit_no) from dual", nativeQuery = true)
	    String getInmateCondition(@Param("ivc_commit_no") String ivc_commit_no);
	 
	    // Query to call the function sf_get_user_inst_num
	 @Query(value = "SELECT sf_get_user_inst_num(:user) FROM dual", nativeQuery = true)
	 String getUserInstNum(@Param("user") String user);
	    // Query to call the function get_inmate_inst
	    @Query(value = "SELECT get_inmate_inst(:commitNo) FROM dual", nativeQuery = true)
	    String getInmateInst(@Param("commitNo") String commitNo);
	    
	    @Query(value = "SELECT 'Y' FROM protected_ofndrs a WHERE a.sbi_no = :ivcSbiNo AND NVL(a.effective_date, TRUNC(SYSDATE)) <= TRUNC(SYSDATE) AND NVL(a.expiry_date, TRUNC(SYSDATE)) >= TRUNC(SYSDATE)", nativeQuery = true)
	    String checkIfOffenderIsProtected(@Param("ivcSbiNo") String sbiNo);

	 
	  
	 @Query(value = "SELECT Sf_Get_Hard_Sbi(:HardSbi) from dual", nativeQuery = true)
	    String FindHardSbiNo( String HardSbi);
	 
	  @Query(value = "SELECT i.INMATE_FULL_NAME,i.commit_lname, i.commit_fname, i.commit_mname, i.commit_sname, i.primary_dob as dob, i.sex, rv.ref_value_desc as race " +
		        "FROM inmate i " +
		        "LEFT JOIN cm_reference_values rv ON i.race_code = rv.ref_value_code " +
		        "WHERE i.commit_no = :commitNo AND i.sbi_mst_sbi_no = :sbiNo", nativeQuery = true)
		List<Object> findCustomInmateData(@Param("commitNo") String commitNo, @Param("sbiNo") String sbiNo);

	  @Query(value = "SELECT i.commit_lname, i.commit_fname, i.commit_mname, i.commit_sname " +
              "FROM inmate i " +
              "WHERE i.commit_no = :commitNo AND i.sbi_mst_sbi_no = :sbiNo", nativeQuery = true)
List<Object> findOffenderName(@Param("commitNo") String commitNo, @Param("sbiNo") String sbiNo);
	  
	  @Query(value = "SELECT "
		        + "sf_get_commit_no(sbi_mst_sbi_no) AS CURRENT_COMMIT_NO,  "
		        + "b.COMMIT_NO, "       
		        + "INMATE_FULL_NAME,  "
		        + "sf_get_inst_desc(INST_NUM) AS Facility, "
		        + "COALESCE(ADMISS_METHOD,'') AS ADMISS_METHOD,a.INST_ADMISS_DATE,  "
		        + "COALESCE(INST_ADMISS_TIME,'') AS INST_ADMISS_TIME, "
		        + "INST_RELEASE_DATE, "
		        + "COALESCE(INST_RELEASE_TIME,'') AS INST_RELEASE_TIME "
		        + "FROM INMATE a JOIN INMATE_DOC_IDS b ON a.COMMIT_NO = b.COMMIT_NO "
		        + "WHERE sbi_mst_sbi_no = :sbiNo "
		        + "ORDER BY b.COMMIT_NO DESC", nativeQuery = true)
		List<Object[]> findInmateDetailsBySbiNo(String sbiNo);
		
		@Query(value = "SELECT i.current_inst_num from inmate i where i.commit_no = :commitNo", nativeQuery = true)
		String getCurrentInstNumByCommitNo(String commitNo);
		
		
		
}



