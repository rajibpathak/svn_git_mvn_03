package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.MenuAccessScreen;
import com.omnet.cnt.Model.User_Roles;

@Repository
@EnableJpaRepositories	
public interface MenuAccessRepository extends JpaRepository<MenuAccessScreen, String>{

	@Query(nativeQuery = true,value ="WITH MenuData AS (\r\n"
			+ "    SELECT DISTINCT\r\n"
			+ "        SM.SUB_MENU_NAME AS Screen_Name,\r\n"
			+ "        SS.SCREEN_CODE AS Sub_menu_code,\r\n"
			+ "        SM.MENU_CODE AS Menu_Code,\r\n"
			+ "        COALESCE(insert_priv_flag, 'N') AS insert_priv_flag,\r\n"
			+ "        COALESCE(update_priv_flag, 'N') AS update_priv_flag,\r\n"
			+ "        COALESCE(view_priv_flag, 'N') AS view_priv_flag,\r\n"
			+ "        COALESCE(screen_url, '#') AS screen_url\r\n"
			+ "    FROM\r\n"
			+ "        OMNET_SUB_MENU SM\r\n"
			+ "    LEFT OUTER JOIN\r\n"
			+ "        OMNET_SCREENS SS ON SM.SCREEN_CODE = SS.SCREEN_CODE\r\n"
			+ "    LEFT OUTER JOIN (\r\n"
			+ "        SELECT\r\n"
			+ "            RS.SCREEN_CODE,\r\n"
			+ "            COALESCE(RS.insert_priv_flag, 'N') AS insert_priv_flag,\r\n"
			+ "            COALESCE(RS.update_priv_flag, 'N') AS update_priv_flag,\r\n"
			+ "            COALESCE(RS.view_priv_flag, 'N') AS view_priv_flag\r\n"
			+ "        FROM\r\n"
			+ "            OMNET_ROLE_SCREENS RS\r\n"
			+ "        JOIN\r\n"
			+ "            OMNET_USER_ROLES UR ON RS.role_seq_num = UR.role_seq_num\r\n"
			+ "            AND UPPER(UR.user_id) = UPPER('BADMMRP')\r\n"
			+ "    ) b ON SS.SCREEN_CODE = b.SCREEN_CODE\r\n"
			+ "    WHERE\r\n"
			+ "        SM.Status = 'A'\r\n"
			+ ")\r\n"
			+ "SELECT\r\n"
			+ "    ROW_NUMBER() OVER (ORDER BY Sub_menu_code) AS S_No,\r\n"
			+ "    Screen_Name,\r\n"
			+ "    Sub_menu_code,\r\n"
			+ "    Menu_Code,\r\n"
			+ "    insert_priv_flag,\r\n"
			+ "    update_priv_flag,\r\n"
			+ "    view_priv_flag,\r\n"
			+ "    screen_url\r\n"
			+ "FROM\r\n"
			+ "    MenuData\r\n"
			+ "ORDER BY\r\n"
			+ "    S_No")
	List<MenuAccessScreen> getScreenAccess(String userId);
	
}
