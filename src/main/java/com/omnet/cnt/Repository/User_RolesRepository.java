package com.omnet.cnt.Repository;

import java.util.List;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.omnet.cnt.Model.Login;
import com.omnet.cnt.Repository.LoginRepository;
import com.omnet.cnt.Model.User_Roles;

@Repository
@EnableJpaRepositories	
public interface User_RolesRepository extends JpaRepository<User_Roles, String> {
	

	@Query(nativeQuery = true,value ="Select DISTINCT  SM.SUB_MENU_NAME as Screen_Name,CATEGORY_CODE,SS.SCREEN_CODE as Sub_menu_code, "
			+ " COALESCE(insert_priv_flag, 'N')insert_priv_flag, "
			+ " COALESCE(update_priv_flag, 'N') update_priv_flag, "
			+ " COALESCE(view_priv_flag, 'N') AS view_priv_flag, "
			+ " COALESCE(screen_url,'#')screen_url, "
			+ " SS.SCREEN_CODE "
			+ " from "
			+ " OMNET_SUB_MENU SM "
			+ " Left outer join OMNET_SCREENS SS ON SM.SCREEN_CODE = SS.SCREEN_CODE "
			+ " Left outer join "
			+ " ( "
			+ " SELECT "
			+ " RS.SCREEN_CODE, "
			+ " COALESCE(RS.insert_priv_flag, 'N') AS insert_priv_flag, "
			+ " COALESCE(RS.update_priv_flag, 'N') AS update_priv_flag, "
			+ " COALESCE(RS.view_priv_flag, 'N') AS view_priv_flag "
			+ " FROM "
			+ " OMNET_ROLE_SCREENS RS "
			+ " JOIN OMNET_USER_ROLES UR ON RS.role_seq_num = UR.role_seq_num "
			+ " AND UPPER(UR.user_id) =UPPER(:userId) "
			+ " )b on SS.SCREEN_CODE=b.SCREEN_CODE "
			+ " Where SM.Status='A' "
			+ "ORDER BY Sub_menu_code")
	List<User_Roles> getscreeninfo(String userId);
	


}