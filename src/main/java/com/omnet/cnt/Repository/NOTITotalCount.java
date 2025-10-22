package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.NOTISearchTotalCount;

@Repository
@EnableJpaRepositories
public interface NOTITotalCount extends JpaRepository<NOTISearchTotalCount, Long>{
	
	  @Query(value = "SELECT COUNT(DISTINCT A.short_message) AS Total_No_Category " +
              "FROM ( " +
              "   SELECT a.user_mess_seq_num, a.user_id, a.inst_num, a.screen_code, a.date_time_sent, " +
              "          a.MESSAGE_TEXT, a.inserted_user_id SENT_BY_USER_ID, a.inserted_user_id, a.short_message, " +
              "          DECODE(a.user_id_viewed_by, NULL, 'N', 'Y') Removed_Flag, a.commit_no Commit_no, a.SBI_NO sbi_no, " +
              "          DECODE(a.SBI_NO, NULL, a.commit_no, (SELECT Current_Commit_No FROM Sbi_Mst WHERE Sbi_No = a.SBI_NO)) Commit_no1 " +
              "   FROM OMNET_USERS_MESSAGE a " +
              "   WHERE a.user_id = NVL(:p_user_id, user) " +
              "   AND a.short_message = NVL(:p_short_msg, a.short_message) " +
              "   AND a.date_time_sent >= TO_DATE(:p_date_frm, 'YYYY-MM-DD') " +
              "   AND a.date_time_sent < TO_DATE(:p_date_to, 'YYYY-MM-DD') + 1 " +
              "   AND a.inserted_user_id = NVL(:p_sent_by, a.inserted_user_id) " +
              "   AND (:p_sbi_no IS NULL OR a.sbi_no = :p_sbi_no) " +
              "   UNION ALL " +
              "   SELECT a.user_mess_seq_num, a.user_id, a.inst_num, a.screen_code, a.date_time_sent, " +
              "          a.MESSAGE_TEXT, a.inserted_user_id SENT_BY_USER_ID, a.inserted_user_id, a.short_message, " +
              "          DECODE(a.user_id_viewed_by, NULL, 'N', 'Y') Removed_Flag, a.commit_no Commit_no, a.SBI_NO sbi_no, " +
              "          DECODE(a.SBI_NO, NULL, a.commit_no, (SELECT Current_Commit_No FROM Sbi_Mst WHERE Sbi_No = a.SBI_NO)) Commit_no1 " +
              "   FROM OMNET_USERS_MESSAGE a " +
              "   WHERE a.user_id = NVL(:p_user_id, user) " +
              "   AND a.short_message = NVL(:p_short_msg, a.short_message) " +
              "   AND a.date_time_sent >= TO_DATE(:p_date_frm, 'YYYY-MM-DD') " +
              "   AND a.date_time_sent < TO_DATE(:p_date_to, 'YYYY-MM-DD') + 1 " +
              "   AND a.inserted_user_id = NVL(:p_sent_by, a.inserted_user_id) " +
              "   AND (:p_sbi_no IS NULL OR a.sbi_no = :p_sbi_no) " +
              ") A, screen_notification_rt C, Inmate i " +
              "WHERE c.short_message = a.short_message " +
              "AND c.TYPE IN ('A', 'I') " +
              "AND i.commit_no(+) = a.commit_no1 " +
              "AND (:p_last_name IS NULL OR i.commit_lname = :p_last_name)", 
      nativeQuery = true)
	String GetNotiCount( @Param("p_user_id") String pUserId,
	        @Param("p_short_msg") String pShortMsg,
	        @Param("p_date_frm") String pDateFrom,
	        @Param("p_date_to") String pDateTo,
	        @Param("p_sent_by") String pSentBy,
	        @Param("p_sbi_no") String pSbiNo,
	        @Param("p_last_name") String pLastName);

}
