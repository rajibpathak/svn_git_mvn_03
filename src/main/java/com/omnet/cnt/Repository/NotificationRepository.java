package com.omnet.cnt.Repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.NotificationOmnetDesktop;
import com.omnet.cnt.Model.OmnetDesktop;

@EnableJpaRepositories
public interface NotificationRepository extends JpaRepository<NotificationOmnetDesktop, String>{
	
	@Query(value = " SELECT a.user_mess_seq_num AS user_mess_seq_num, \r\n"
			+ " a.short_message AS short_message, \r\n"
			+ " a.date_time_sent AS date_time_sent, \r\n"
			+ " TO_CHAR(a.date_time_sent, 'HH24:MI:SS')access_time,\r\n"
			+ " a.inserted_userid AS created_user_id, \r\n"
			+ " CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(COALESCE(c.user_last_name,''), ' '), COALESCE(c.user_first_name,'')), ' '), COALESCE(c.user_mid_name,'')), ' '), COALESCE(c.user_suffix_name,'')) AS created_user_name, \r\n"
			+ " '2' AS no_of_days, \r\n"
			+ " b.screen_code AS screen_code, \r\n"
			+ " a.message_text AS message_text, \r\n"
			+ " COALESCE(b.Type,'') AS remove, \r\n"
			+ " a.sbi_mst_sbi_no AS sbi_no, \r\n"
			+ " a.commit_no AS commit_no, \r\n"
			+ " '' AS offender_name, \r\n"
			+ " COALESCE(d.Screen_url,'') AS Screen_url, \r\n"
			+ " COALESCE(USER_ID_VIEWED_BY,'') AS USER_ID_VIEWED_BY, \r\n"
			+ " CASE WHEN USER_ID_VIEWED_BY IS NOT NULL THEN 'I' ELSE 'A' END AS Message_Read,   \r\n"
			+ "CONCAT(SUBSTR(c.user_last_name, 1, 1),SUBSTR(c.user_first_name, 1, 1))Name_Prefix\r\n"
			+ " FROM \r\n"
			+ " DACS_USERS_MESSAGE a \r\n"
			+ " JOIN \r\n"
			+ " SCREEN_NOTIFICATION_RT b ON a.short_message = b.short_message \r\n"
			+ " JOIN \r\n"
			+ " OMNET_USERS c ON a.inserted_userid = c.user_id \r\n"
			+ " LEFT OUTER JOIN \r\n"
			+ " OMNET_SCREENS d on a.SCREEN_CODE = d.SCREEN_CODE \r\n"
			+ " WHERE \r\n"
			+ " a.short_message = b.short_message \r\n"
			+ " AND a.user_id = :userId \r\n"
			+ " AND a.inserted_userid = c.user_id \r\n"
			+ " AND TO_CHAR(a.INSERTED_DATE_TIME, 'MM/DD/YYYY')=TO_CHAR(SYSDATE, 'MM/DD/YYYY')\r\n"
			+ " ORDER BY no_of_days", nativeQuery = true)
	List<NotificationOmnetDesktop> getnotify(String userId);
	

	
	  @Modifying
	  @Transactional
      @Query(value="Update OMNET_USERS_MESSAGE set USER_ID_VIEWED_BY=:userId   where  USER_MESS_SEQ_NUM=:user_mess_seq_num ",nativeQuery = true)
      void updatevalue(String userId,String user_mess_seq_num);
	
}
