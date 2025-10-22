package com.omnet.cnt.Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class UserRoleController {
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/getRoles")
	public List<Map<String, Object>> getRoles(@RequestParam(value = "code") String code, @RequestParam(value = "key") String key) {
		String query = "";
		query = "SELECT ROLE_SEQ_NUM, ROLE_NAME, STATUS FROM OMNET_ROLES ";
		
		if(code.equals("0")) {
			query += "WHERE STATUS = 'A' ";
		}
		
		if(code.equals("1")) {
			if(key != null) {
				query += " where upper(ROLE_SEQ_NUM) like '%" + key.toUpperCase() + "%' or  ";
				query += "upper(ROLE_NAME) like '%" + key.toUpperCase() + "%'";
			}
		}
		
		if(code.equals("2")) {
			if(key != null) {
				query += " where status = 'A' and (upper(ROLE_SEQ_NUM) like '%" + key.toUpperCase() + "%' or  ";
				query += "upper(ROLE_NAME) like '%" + key.toUpperCase() + "%')";
			}
		}
		
		query += "ORDER BY STATUS, ROLE_SEQ_NUM";
		System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	@PostMapping("/getRoleDtls")
	public List<Map<String, Object>> getRoleDtls(@RequestParam(value = "role_seq") String role) {
		String query = "";
		query += "SELECT ROLE_SEQ_NUM, ROLE_NAME, STATUS, INSERTED_USERID, INSERTED_DATE_TIME, UPDATED_USERID, UPDATED_DATE_TIME ";
		query += " FROM OMNET_ROLES WHERE ROLE_SEQ_NUM = '" + role + "'";
		// System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	@GetMapping("/getScreenAccess")
	public List<Map<String, Object>> getScreenAccess(@RequestParam(value = "role_seq") String role) {
		String query = "";
		query += "SELECT A.SCREEN_NAME, A.SCREEN_CODE, ";
		query += "DECODE((SELECT COUNT(ROLE_SEQ_NUM) FROM OMNET_ROLE_SCREENS B WHERE A.SCREEN_CODE = B.SCREEN_CODE AND ";
		query += "B.ROLE_SEQ_NUM = " + role + " AND B.INSERT_PRIV_FLAG = 'Y'),'0','I','A') AS INS_FLAG,";
		query += "DECODE((SELECT COUNT(ROLE_SEQ_NUM) FROM OMNET_ROLE_SCREENS B WHERE A.SCREEN_CODE = B.SCREEN_CODE AND ";
		query += "B.ROLE_SEQ_NUM = " + role + " AND B.UPDATE_PRIV_FLAG = 'Y'),'0','I','A') AS UPD_FLAG,";
		query += "DECODE((SELECT COUNT(ROLE_SEQ_NUM) FROM OMNET_ROLE_SCREENS B WHERE A.SCREEN_CODE = B.SCREEN_CODE AND ";
		query += "B.ROLE_SEQ_NUM = " + role + " AND B.VIEW_PRIV_FLAG = 'Y'),'0','I','A') AS VIEW_FLAG ";
		query += "FROM OMNET_SCREENS A WHERE A.STATUS = 'A' ORDER BY INS_FLAG, UPD_FLAG, VIEW_FLAG, SCREEN_NAME";
		//System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	@PostMapping("/checkRoleExist")
	public List<Map<String, Object>> checkUserExist(@RequestParam(value = "role_seq") String role) {
		String query = "";
		query += "select count(*) as cnt from omnet_roles where role_seq_num = '" + role + "'";
		//System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	@PostMapping("/saveRoles")
	public void saveRoles(@RequestParam(value = "flag") String flag,
			@RequestParam(value = "role") String role_seq, @RequestParam(value = "role_name") String role_name,
			@RequestParam(value = "status") String status) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		String userId = (String) detailsMap.get("userId");
		
		String query = "";
		
		if(flag.equals("I")) {
			query += "INSERT INTO OMNET_ROLES (ROLE_SEQ_NUM, ROLE_NAME, STATUS, INSERTED_USERID, INSERTED_DATE_TIME, ";
			query += "UPDATED_USERID, UPDATED_DATE_TIME) VALUES ('" + role_seq + "','" + role_name + "','" + status + "',";
			query += "'" + userId + "',SYSDATE,'" + userId + "',SYSDATE)";
		} else {
			query += "UPDATE OMNET_ROLES SET ROLE_NAME = '" + role_name + "', STATUS = '" + status + "',";
			query += "UPDATED_USERID = '" + userId + "', UPDATED_DATE_TIME = SYSDATE WHERE ROLE_SEQ_NUM = '" + role_seq + "'";
		}
		//System.out.println("query is " + query);
		jdbcTemplate.execute(query);
	}
	
	@PostMapping("/saveRoleAccess")
	public void saveRoles(@RequestParam(value = "role") String role_seq, @RequestParam(value = "role_access") String role_access) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		String userId = (String) detailsMap.get("userId");
		
		System.out.println("Role access is " + role_access);
		
		//String my_role = "ACA_REPO:Y:Y:N:/ACA_REPO,334:Y:Y:N:/334,ASM_WORK:N:Y:Y:/ASM_WORK,57:N:Y:N:/57,CR_ATRS:Y:N:N:/CR_ATRS";
		String my_role = "ACA_REPO:Y:Y:N:/ACA_REPO,ASM_WORK:N:Y:Y:/ASM_WORK,CR_ATRS:Y:N:N:/CR_ATRS";
		
		//System.out.println("my_role is " + my_role);
		
		ArrayList<String> accesses = new ArrayList<String>(Arrays.asList(role_access.split(",")));
		
		System.out.println("size is " + accesses.size());
		
		for (int i = 0; i < accesses.size(); i++) {
			System.out.println("Here i is " + i);
			System.out.println(accesses.get(i) + " for " + i);
			String screen_code = accesses.get(i).split(":")[0];
			String ins_flag = accesses.get(i).split(":")[1];
			String upd_flag = accesses.get(i).split(":")[2];
			String view_flag = accesses.get(i).split(":")[3];
			System.out.println(screen_code);
			
			List<Map<String, Object>> exist_data = jdbcTemplate
					.queryForList("SELECT ORS_SEQ_NUM FROM OMNET_ROLE_SCREENS WHERE ROLE_SEQ_NUM = '" + role_seq
							+ "' AND SCREEN_CODE = '" + screen_code + "'");
			
			System.out.println(role_seq + " and " + screen_code + " length is " + exist_data.size());
			
			String query = "";
			if (exist_data.size() == 0) {
				query += "INSERT INTO OMNET_ROLE_SCREENS (ORS_SEQ_NUM, ROLE_SEQ_NUM, SCREEN_CODE, INSERT_PRIV_FLAG, UPDATE_PRIV_FLAG, VIEW_PRIV_FLAG, ";
				query += "INSERTED_USER_ID, INSERTED_DATE_TIME, UPDATED_USER_ID, UPDATED_DATE_TIME) VALUES (ORS_SEQ.Nextval, '" + role_seq + "','" + screen_code + "',";
				query += "'" + ins_flag + "','" + upd_flag + "','" + view_flag + "', '" + userId + "', SYSDATE, '" + userId + "', SYSDATE)";
			} else {
				query += "UPDATE OMNET_ROLE_SCREENS SET INSERT_PRIV_FLAG = '" + ins_flag + "', UPDATE_PRIV_FLAG = '" + upd_flag + "', ";
				query += "VIEW_PRIV_FLAG = '" + view_flag + "', UPDATED_USER_ID = '" + userId + "', UPDATED_DATE_TIME = SYSDATE WHERE ";
				query += "ROLE_SEQ_NUM = '" + role_seq + "' AND SCREEN_CODE = '" + screen_code + "'";
			}
			
			//System.out.println("query is " + query);
			
			exec(query);
		}
		
		/*String query = "";
		
		if(flag.equals("I")) {
			query += "INSERT INTO OMNET_ROLES (ROLE_SEQ_NUM, ROLE_NAME, STATUS, INSERTED_USERID, INSERTED_DATE_TIME, ";
			query += "UPDATED_USERID, UPDATED_DATE_TIME) VALUES ('" + role_seq + "','" + role_name + "','" + status + "',";
			query += "'" + userId + "',SYSDATE,'" + userId + "',SYSDATE)";
		} else {
			query += "UPDATE OMNET_ROLES SET ROLE_NAME = '" + role_name + "', STATUS = '" + status + "',";
			query += "UPDATED_USERID = '" + userId + "', UPDATED_DATE_TIME = SYSDATE WHERE ROLE_SEQ_NUM = '" + role_seq + "'";
		}
		//System.out.println("query is " + query);
		jdbcTemplate.execute(query);*/
	}
	
	public void exec(String query) {
		System.out.println("query from exec : " + query);
		jdbcTemplate.execute(query);
	}
	
}
