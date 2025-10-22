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
public class UserDataController {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/loadUserData")
	public ResponseEntity<Map<String, Object>> loadUserData(@RequestParam(value = "page") String page,
			@RequestParam(value = "opt") String word, @RequestParam(value = "key") String key) throws SQLException {
		// @RequestParam(value = "inactive") String inactive) throws SQLException {

		String letters = "";
		if (!word.equals("")) {
			switch (word) {
			case "Opt1":
				letters = "Upper(u.user_last_name) like 'A%' or Upper(u.user_last_name) like 'B%' or Upper(u.user_last_name) like 'C%' or Upper(u.user_last_name) like 'D%' ";
				break;
			case "Opt2":
				letters = "Upper(u.user_last_name) like 'E%' or Upper(u.user_last_name) like 'F%' or Upper(u.user_last_name) like 'G%' or Upper(u.user_last_name) like 'H%' ";
				break;
			case "Opt3":
				letters = "Upper(u.user_last_name) like 'I%' or Upper(u.user_last_name) like 'J%' or Upper(u.user_last_name) like 'K%' or Upper(u.user_last_name) like 'L%' ";
				break;
			case "Opt4":
				letters = "Upper(u.user_last_name) like 'M%' or Upper(u.user_last_name) like 'N%' or Upper(u.user_last_name) like 'O%' or Upper(u.user_last_name) like 'P%' ";
				break;
			case "Opt5":
				letters = "Upper(u.user_last_name) like 'Q%' or Upper(u.user_last_name) like 'R%' or Upper(u.user_last_name) like 'S%' or Upper(u.user_last_name) like 'T%' ";
				break;
			case "Opt6":
				letters = "Upper(u.user_last_name) like 'U%' or Upper(u.user_last_name) like 'V%' or Upper(u.user_last_name) like 'W%' or Upper(u.user_last_name) like 'X%' or "
						+ "Upper(u.user_last_name) like 'Y%' or Upper(u.user_last_name) like 'Z%' ";
				break;
			}
		}

		Map<String, Object> response = new HashMap<>();
		int page_num = Integer.parseInt(page);
		page_num = (page_num - 1) * 50;

		// System.out.println(page + " and " + page_num);

		// System.out.println("word is " + word);

		StringBuilder query = new StringBuilder();
		query.append("select u.user_id, u.user_first_name, u.user_last_name, u.user_status, ");
		query.append("t.title_desc AS job_title from omnet_users u ");
		query.append("Left Outer join title_rt t on u.title = t.title ");
		if (!word.equals("")) {
			query.append("where " + letters);
		}
		if (!key.equals("")) {
			query.append("where upper(u.user_last_name) like '%" + key.toUpperCase() + "%' or ");
			query.append("upper(u.user_first_name) like '%" + key.toUpperCase() + "%' or ");
			query.append("upper(u.user_id) like '%" + key.toUpperCase() + "%' or ");
			query.append("upper(t.title_desc) like '%" + key.toUpperCase() + "%' ");
		}
		/*
		 * if(inactive.equals("N")) { query.append("and u.user_status = 'A' "); }
		 */
		query.append("order by u.user_status, u.user_last_name, u.user_first_name asc ");
		query.append("offset " + page_num + " rows fetch next 50 rows only");

		StringBuilder count_sql = new StringBuilder();
		count_sql.append("select count(*) as total from omnet_users u join title_rt t on u.title = t.title ");
		if (!word.equals("")) {
			count_sql.append("where " + letters);
		}
		if (!key.equals("")) {
			count_sql.append("where upper(u.user_last_name) like '%" + key.toUpperCase() + "%' or ");
			count_sql.append("upper(u.user_first_name) like '%" + key.toUpperCase() + "%' or ");
			count_sql.append("upper(u.user_id) like '%" + key.toUpperCase() + "%' or ");
			count_sql.append("upper(t.title_desc) like '%" + key.toUpperCase() + "%' ");
		}

		// System.out.println("count_sql is " + count_sql);
		ResultSetMetaData resultSetMetaData = null;

		Map<String, Object> row = null;
		int rowCount = 0;
		try (Connection conn = dataSource.getConnection()) {
			Statement statement = conn.createStatement();
			System.out.println("query is " + query);
			ResultSet rs0 = statement.executeQuery(count_sql.toString());

			if (rs0.next()) {
				rowCount = rs0.getInt("total");
				// System.out.println("Row count: " + rowCount);
			}

			int pages = 0;
			pages = rowCount / 50;

			if (rowCount % 50 > 0) {
				pages++;
			}

			// System.out.println("page_num is " + page_num + " and " + rowCount);
			// if (page_num < rowCount) {
			response.put("total", rowCount);
			response.put("last_page", pages);

			List<Map<String, Object>> rows = null;
			// System.out.print("final query is " + query);
			ResultSet rs = statement.executeQuery(query.toString());
			resultSetMetaData = rs.getMetaData(); // etc
			rows = new ArrayList<Map<String, Object>>();
			int i = 0;
			while (rs.next()) {
				i++;
				// System.out.println("here i is " + i);
				row = new HashMap<String, Object>();
				row.put("user_id", rs.getString(resultSetMetaData.getColumnName(1)));
				row.put("user_fl_name", rs.getString(resultSetMetaData.getColumnName(3)) + " "
						+ rs.getString(resultSetMetaData.getColumnName(2)));
				row.put("user_status", rs.getString(resultSetMetaData.getColumnName(4)));
				row.put("job_title", rs.getString(resultSetMetaData.getColumnName(5)));
				rows.add(row);
			}

			response.put("data", rows);
			// }

			// System.out.println("Column name: " + resultSetMetaData.getColumnName(1));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/getUserLocation")
	public List<Map<String, Object>> getUserbyKey() {
		String query = "";
		query = "Select INST_NUM,INST_NAME from INSTITUTION Order by INST_NAME";
		// System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getUserTitle")
	public List<Map<String, Object>> getUserTitle() {
		String query = "";
		query = "Select TITLE,TITLE_DESC from TITLE_RT Order by TITLE_DESC";
		// System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getUserRoles")
	public List<Map<String, Object>> getUserRoles(@RequestParam(value = "user") String user) {
		String query = "";
		query += "Select a.Role_Seq_Num,a.Role_Name,b.Role_Active_Date,b.Role_End_Date,";
		query += "decode(b.tmp_role_flag,'Y','A','I') Status ";
		query += "from Omnet_Roles a Left Outer Join Omnet_User_Roles b on a.Role_seq_num=b.Role_Seq_num ";
		query += "and Upper(b.user_id) = Upper('" + user + "') Order by Status,a.Role_Name";
		//System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getUserLocation")
	public List<Map<String, Object>> getUserLocation(@RequestParam(value = "user") String user) {
		String query = "";
		query += "select a.inst_num, a.inst_name, decode((select count(*) from ";
		query += "user_institution b where Upper(b.USER_ID) = Upper('" + user + "') and b.current_inst_flag = 'Y' ";
		query += "and b.inst_num = a.inst_num), 0, 'I', 'A') as status ";
		query += "from institution a order by status, inst_name";
		//System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	@PostMapping("/checkUserExist")
	public List<Map<String, Object>> checkUserExist(@RequestParam(value = "user") String user) {
		String query = "";
		query += "select count(*) as cnt from omnet_users where user_id = '" + user + "'";
		//System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/saveInstRole")
	public void saveAttr(@RequestParam(value = "do_i") String do_i, @RequestParam(value = "un_i") String un_i,
			@RequestParam(value = "do_r") String do_r, @RequestParam(value = "un_r") String un_r,
			@RequestParam(value = "userid") String userid) {

		ArrayList<String> do_is = new ArrayList<String>(Arrays.asList(do_i.split(",")));
		ArrayList<String> un_is = new ArrayList<String>(Arrays.asList(un_i.split(",")));
		ArrayList<String> do_rs = new ArrayList<String>(Arrays.asList(do_r.split(",")));
		ArrayList<String> un_rs = new ArrayList<String>(Arrays.asList(un_r.split(",")));

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		String OfficerId = (String) detailsMap.get("userId");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println("Current Time: " + dtf.format(now));
		String current_time = dtf.format(now);

		System.out.println("OfficerId is " + OfficerId);

		System.out.println("do_is.size() is " + do_is.size());

		if (!do_i.isEmpty()) {
			for (int i = 0; i < do_is.size(); i++) {
				System.out.println("do_is is " + do_is.get(i));

				List<Map<String, Object>> exist_data = jdbcTemplate
						.queryForList("SELECT * FROM USER_INSTITUTION WHERE USER_ID = '" + userid + "' AND INST_NUM = '"
								+ do_is.get(i) + "' AND CURRENT_INST_FLAG = 'Y'");

				if (exist_data.size() == 0) {
					String Query = "";
					Query += "INSERT INTO USER_INSTITUTION ";
					Query += "(USER_ID, INST_NUM, CURRENT_INST_FLAG, ARRIVAL_DATE, ARRIVAL_TIME, INSERTED_USERID, INSERTED_DATE_TIME, UPDATED_USERID, UPDATED_DATE_TIME) ";
					Query += "VALUES ('" + userid + "','" + do_is.get(i) + "','Y',SYSDATE,'" + current_time + "','"
							+ OfficerId + "',SYSDATE,'" + OfficerId + "',SYSDATE)";

					System.out.println("Final Do Inst Query : " + Query);
					jdbcTemplate.execute(Query);
				}
			}
		}

		if (!un_i.isEmpty()) {
			for (int i = 0; i < un_is.size(); i++) {
				System.out.println("un_is is " + un_is.get(i));

				List<Map<String, Object>> exist_data = jdbcTemplate
						.queryForList("SELECT * FROM USER_INSTITUTION WHERE USER_ID = '" + userid + "' AND INST_NUM = '"
								+ un_is.get(i) + "' AND CURRENT_INST_FLAG = 'Y'");

				if (exist_data.size() > 0) {
					String Query = "";
					Query += "UPDATE USER_INSTITUTION SET CURRENT_INST_FLAG = 'N', UPDATED_USERID = '" + OfficerId
							+ "', ";
					Query += "UPDATED_DATE_TIME = SYSDATE WHERE USER_ID = '" + userid + "' and INST_NUM = '"
							+ un_is.get(i) + "'";

					System.out.println("Final Undo Inst Query : " + Query);
					jdbcTemplate.execute(Query);
				}
			}
		}

		if (!do_r.isEmpty()) {
			for (int i = 0; i < do_rs.size(); i++) {
				System.out.println("do_rs is " + do_rs.get(i));

				List<Map<String, Object>> exist_data = jdbcTemplate
						.queryForList("SELECT * FROM OMNET_USER_ROLES WHERE USER_ID = '" + userid
								+ "' AND ROLE_SEQ_NUM = '" + do_rs.get(i) + "'");

				if (exist_data.size() == 0) {
					String Query = "";
					Query += "INSERT INTO OMNET_USER_ROLES ";
					Query += "(USER_ID, ROLE_SEQ_NUM, TMP_ROLE_FLAG, ROLE_ACTIVE_DATE, INSERTED_USERID, INSERTED_DATE_TIME, UPDATED_USERID, UPDATED_DATE_TIME) ";
					Query += "VALUES ('" + userid + "','" + do_rs.get(i) + "','Y',SYSDATE,'" + OfficerId + "',SYSDATE,'"
							+ OfficerId + "',SYSDATE)";

					System.out.println("Final Do Role Query : " + Query);
					jdbcTemplate.execute(Query);
				} else {
					String Query = "";
					Query += "UPDATE OMNET_USER_ROLES SET TMP_ROLE_FLAG = 'Y', ROLE_ACTIVE_DATE = SYSDATE, UPDATED_USERID = '"
							+ OfficerId + "', ";
					Query += "UPDATED_DATE_TIME = SYSDATE WHERE USER_ID = '" + userid + "' and ROLE_SEQ_NUM = '"
							+ do_rs.get(i) + "'";

					System.out.println("Final Undo Role Query : " + Query);
					jdbcTemplate.execute(Query);
				} 
			}
		}

		if (!un_r.isEmpty()) {
			for (int i = 0; i < un_rs.size(); i++) {
				System.out.println("un_rs is " + un_rs.get(i));

				List<Map<String, Object>> exist_data = jdbcTemplate
						.queryForList("SELECT * FROM OMNET_USER_ROLES WHERE USER_ID = '" + userid
								+ "' AND ROLE_SEQ_NUM = '" + un_rs.get(i) + "' AND TMP_ROLE_FLAG = 'Y'");

				if (exist_data.size() > 0) {
					String Query = "";
					Query += "UPDATE OMNET_USER_ROLES SET TMP_ROLE_FLAG = 'N', ROLE_END_DATE = SYSDATE, UPDATED_USERID = '"
							+ OfficerId + "', ";
					Query += "UPDATED_DATE_TIME = SYSDATE WHERE USER_ID = '" + userid + "' and ROLE_SEQ_NUM = '"
							+ un_rs.get(i) + "'";

					System.out.println("Final Undo Role Query : " + Query);
					jdbcTemplate.execute(Query);
				}
			}
		}

	}

}