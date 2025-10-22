package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.SentenceLevel;

public interface SentenceLevelRepository extends JpaRepository<SentenceLevel, String> {
	
	
	
	@Query(value = "SELECT c.commit_no, c.case_num, c.cra_num, c.charge_num, a.sntc_lvl_cd, b.sntc_lvl_desc,a.restitution_flag, a.current_serving_flag , a.completion_flag " +
		       "FROM sentence_levels a " +
		       "JOIN sentence_charges c ON a.commit_no = c.commit_no AND a.case_num = c.case_num AND a.cra_num = c.cra_num " +
		       "JOIN sentence_level_mst b ON a.sntc_lvl_cd = b.sntc_lvl_cd " +
		       "WHERE c.commit_no = :commitNo " +
		       "ORDER BY a.sntc_lvl_seq_num, a.sntc_ord_seq_num, a.sntc_lvl_cd DESC, a.cal_start_date", nativeQuery = true)
		List<Object[]> findByCommitNo(@Param("commitNo") String commitNo);

}
