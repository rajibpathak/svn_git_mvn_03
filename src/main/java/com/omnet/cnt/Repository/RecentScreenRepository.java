package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omnet.cnt.Model.RecentScreen;

public interface RecentScreenRepository extends JpaRepository<RecentScreen,String> {
	
	@Query(nativeQuery=true, value="SELECT * "
			+ "FROM ( "
			+ "    SELECT  "
			+ "        a.screen_code, "
			+ "        a.screen_name, "
			+ "        a.user_id, "
			+ "        a.access_date_time, "
			+ "		sf_get_user_name(a.user_id) user_name, "
			+ "		COALESCE(screen_url,'#')screen_url, "
			+ "		ROW_NUMBER() OVER (PARTITION BY a.screen_code ORDER BY a.access_date_time DESC) AS row_num "
			+ "		FROM "
			+ "		doc_audit_dtl a "
			+ "		LEFT OUTER JOIN "
			+ "		OMNET_SCREENS SS ON SS.SCREEN_CODE = a.SCREEN_CODE\r\n"
			+ "		WHERE "
			+ "		a.user_id = :userId "
			+ "		) sub "
			+ "WHERE row_num <= 10 ORDER BY access_date_time DESC ")
	        List<RecentScreen> getRecentScreen(String userId);

}
