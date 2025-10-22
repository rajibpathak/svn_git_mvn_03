package com.omnet.cnt.Controller;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;

@RestController
public class ScreenSetupController {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private HttpSession session;

	@PostMapping("/saveAttr")
	public void saveAttr(@RequestParam(value = "field") String field, @RequestParam(value = "label") String label,
			@RequestParam(value = "mandatory") String mandatory, @RequestParam(value = "readonly") String readonly,
			@RequestParam(value = "key") String key, @RequestParam(value = "updatable") String updatable,
			@RequestParam(value = "screenCode") String screenCode, @RequestParam(value = "tableName") String tableName,
			@RequestParam(value = "columnName") String columnName, @RequestParam(value = "level") String level,
			@RequestParam(value = "lov") String lov, @RequestParam(value = "vld") String vld) {

		ArrayList<String> fields = new ArrayList<String>(Arrays.asList(field.split("!@#!")));
		ArrayList<String> labels = new ArrayList<String>(Arrays.asList(label.split("!@#!")));
		ArrayList<String> mandatories = new ArrayList<String>(Arrays.asList(mandatory.split("!@#!")));
		ArrayList<String> readonlies = new ArrayList<String>(Arrays.asList(readonly.split("!@#!")));
		ArrayList<String> keys = new ArrayList<String>(Arrays.asList(key.split("!@#!")));
		ArrayList<String> updatables = new ArrayList<String>(Arrays.asList(updatable.split("!@#!")));
		ArrayList<String> tables = new ArrayList<String>(Arrays.asList(tableName.split("!@#!")));
		ArrayList<String> columns = new ArrayList<String>(Arrays.asList(columnName.split("!@#!")));
		ArrayList<String> levels = new ArrayList<String>(Arrays.asList(level.split("!@#!")));
		ArrayList<String> lovs = new ArrayList<String>(Arrays.asList(lov.split("!@#!")));
		ArrayList<String> vlds = new ArrayList<String>(Arrays.asList(vld.split("!@#!")));

		for (int i = 0; i < fields.size(); i++) {

			List<Map<String, Object>> exist_data = jdbcTemplate
					.queryForList("SELECT * FROM OMNET_FIELD_ATTR WHERE FIELD_NAME = '" + fields.get(i)
							+ "' AND SCREEN_CODE = '" + screenCode + "'");

			String Query = "";

			if (exist_data.size() == 0 && !fields.get(i).equals("")) {
				Query += "INSERT INTO OMNET_FIELD_ATTR (FIELD_NAME, FIELD_LABEL, MANDATORY_FLAG, READ_ONLY_FLAG, PRIMARY_KEY_FLAG, TABLE_NAME, COLUMN_NAME, FIELD_LEVEL, ALGORITHM_QUERY, VALIDATION_LOGIC, SCREEN_CODE) VALUES ";
				Query += "('" + (!fields.get(i).equals("e") ? fields.get(i) : " ") + "',";
				Query += "'" + (!labels.get(i).equals("e") ? labels.get(i) : " ") + "',";
				Query += "'" + (!mandatories.get(i).equals("e") ? mandatories.get(i) : " ") + "',";
				Query += "'" + (!readonlies.get(i).equals("e") ? readonlies.get(i) : " ") + "',";
				Query += "'" + (!keys.get(i).equals("e") ? keys.get(i) : " ") + "',";
				Query += "'" + (!tables.get(i).equals("e") ? tables.get(i) : " ") + "',";
				Query += "'" + (!columns.get(i).equals("e") ? columns.get(i) : " ") + "',";
				Query += "'" + (!levels.get(i).equals("e") ? levels.get(i) : " ") + "',";
				Query += "'" + (!lovs.get(i).equals("e") ? lovs.get(i) : " ") + "',";
				Query += "'" + (!vlds.get(i).equals("e") ? vlds.get(i) : " ") + "',";
				Query += "'" + screenCode + "'";
				Query += ")";
			} else {
				Query += "UPDATE OMNET_FIELD_ATTR SET ";
				Query += "FIELD_LABEL = '" + (!labels.get(i).equals("e") ? labels.get(i) : " ") + "',";
				Query += "MANDATORY_FLAG = '" + (!mandatories.get(i).equals("e") ? mandatories.get(i) : " ") + "',";
				Query += "READ_ONLY_FLAG = '" + (!readonlies.get(i).equals("e") ? readonlies.get(i) : " ") + "',";
				Query += "PRIMARY_KEY_FLAG = '" + (!keys.get(i).equals("e") ? keys.get(i) : " ") + "',";
				Query += "TABLE_NAME = '" + (!tables.get(i).equals("e") ? tables.get(i) : " ") + "',";
				Query += "COLUMN_NAME = '" + (!columns.get(i).equals("e") ? columns.get(i) : " ") + "',";
				Query += "FIELD_LEVEL = '" + (!levels.get(i).equals("e") ? levels.get(i) : " ") + "',";
				Query += "ALGORITHM_QUERY = '" + (!lovs.get(i).equals("e") ? lovs.get(i) : " ") + "',";
				Query += "VALIDATION_LOGIC = '" + (!vlds.get(i).equals("e") ? vlds.get(i) : " ") + "' ";
				Query += "WHERE FIELD_NAME = '" + fields.get(i) + "' AND SCREEN_CODE = '" + screenCode + "'";
			}

			System.out.println("Final Query " + i + " : " + Query);
			jdbcTemplate.execute(Query);

		}

	}
	
	@GetMapping("/fetchStayHistory")
	public List<Map<String, Object>> fetchStayHistory(@RequestParam String sbiNo,
			@RequestParam String docIdFlag) {
		System.out.println("fetchStayHistory sbiNo = " + sbiNo);
		System.out.println("fetchStayHistory docIdFlag = " + docIdFlag);
		List<Map<String, Object>> resultList = new ArrayList<>();
		String sql = "{call SPKG_OMNET_WRAPPER.sp_cbi_stay_history_query(?,?,?)}";

		try (Connection conn = dataSource.getConnection(); 
				CallableStatement stmt = conn.prepareCall(sql)) {

			OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
			ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("TB_CBI_STAY_PREV_STAY", oracleConn);
			ARRAY inArray = new ARRAY(descriptor, oracleConn, new Object[] {});
			OracleCallableStatement ocstmt = stmt.unwrap(OracleCallableStatement.class);
			ocstmt.setArray(1, inArray);
			ocstmt.registerOutParameter(1, OracleTypes.ARRAY, "TB_CBI_STAY_PREV_STAY");
			ocstmt.setString(2, sbiNo);
			ocstmt.setString(3, docIdFlag);
			ocstmt.execute();

			Array oracleArray = ocstmt.getArray(1);
			if (oracleArray != null) {
				Object[] data = (Object[]) oracleArray.getArray();
				for (Object element : data) {
					Struct struct = (Struct) element;
					Object[] attributes = struct.getAttributes();
					Map<String, Object> rowMap = new HashMap<>();
					rowMap.put("commit_no", attributes[0]);  
                    rowMap.put("stay", attributes[1]);    
                    rowMap.put("inst_num", attributes[2]); 
                    rowMap.put("inst_name", attributes[3]);
                    rowMap.put("inmate_name", attributes[4]); 
                    rowMap.put("inmt_tp_cd", attributes[5]);
                    rowMap.put("sent_type_desc", attributes[6]); 
                    rowMap.put("inst_admiss_date", attributes[7]);
                    rowMap.put("inst_admiss_time", attributes[8]); 
                    rowMap.put("inst_release_date", attributes[9]);
                    rowMap.put("inst_release_time", attributes[10]); 
                    rowMap.put("status", attributes[11]); 
					resultList.add(rowMap);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@PostMapping("/getUserAccess")
	public List<Map<String, Object>> getUserAccess(@RequestParam(value = "userid") String userid) {
		String query = "select distinct screen_code, insert_priv_flag, view_priv_flag from omnet_role_screens where ";
		query += "role_seq_num in (select role_seq_num from omnet_user_roles where user_id = '" + userid + "') ";
		query += "and (insert_priv_flag = 'Y' or view_priv_flag = 'Y')";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getScreenName")
	public List<Map<String, Object>> getScreenName(@RequestParam(value = "screenCode") String screenCode) {
		String query = "SELECT SCREEN_NAME FROM OMNET_SCREENS WHERE SCREEN_CODE = '" + screenCode + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getAttr")
	public List<Map<String, Object>> getAttr(@RequestParam(value = "screenCode") String screenCode) {
		String query = "SELECT FIELD_LABEL, FIELD_NAME, MANDATORY_FLAG, READ_ONLY_FLAG, PRIMARY_KEY_FLAG, TABLE_NAME, COLUMN_NAME, FIELD_LEVEL, ALGORITHM_QUERY, VALIDATION_LOGIC FROM OMNET_FIELD_ATTR WHERE SCREEN_CODE = '"
				+ screenCode + "' order by field_level, field_name";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	@PostMapping("/clrSBI")
	public void clrSBI() {
		session.removeAttribute("sbiNumber");
	}

	@PostMapping("/savLOV")
	public List<Map<String, Object>> savLOV(@RequestParam(value = "field_name") String fieldName,
			@RequestParam(value = "screen") String screenCode, @RequestParam(value = "algorithm") String algorithm) {
		String query = "UPDATE OMNET_FIELD_ATTR SET ALGORITHM_QUERY = '" + algorithm + "' WHERE SCREEN_CODE = '"
				+ screenCode + "' AND FIELD_NAME = '" + fieldName + "'";

		System.out.println("Modified query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/savValid")
	public List<Map<String, Object>> savValid(@RequestParam(value = "field_name") String fieldName,
			@RequestParam(value = "screen") String screenCode,
			@RequestParam(value = "validation_rule") String validation_rule) {
		String query = "UPDATE OMNET_FIELD_ATTR SET VALIDATION_LOGIC = '" + validation_rule + "' WHERE SCREEN_CODE = '"
				+ screenCode + "' AND FIELD_NAME = '" + fieldName + "'";

		System.out.println("Modified query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/delAttr")
	public List<Map<String, Object>> delAttr(@RequestParam(value = "screenCode") String screenCode) {
		String query = "DELETE FROM OMNET_FIELD_ATTR WHERE SCREEN_CODE = '" + screenCode + "'";
		System.out.println("Deletion query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getAllTable")
	public List<Map<String, Object>> getAllTable() {
		String query = "SELECT DISTINCT table_name AS NAME FROM omnet_appln_tab_view";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getAllScreens")
	public List<Map<String, Object>> getAllScreens(@RequestParam(value = "filterStr") String filterStr) {
		String query = "SELECT SCREEN_CODE, SCREEN_NAME, SCREEN_URL, STATUS, INSERTED_USER_ID, UPDATED_USER_ID, INSERTED_DATE_TIME, UPDATED_DATE_TIME FROM OMNET_SCREENS where status='A'";

		if (!filterStr.equals("")) {
			query += " AND (upper(SCREEN_CODE) LIKE '%" + filterStr.toUpperCase() + "%' OR upper(SCREEN_NAME) LIKE '%" + filterStr.toUpperCase() + "%')";
			//query += " AND upper(SCREEN_CODE) LIKE '%" + filterStr.toUpperCase() + "%' OR upper(SCREEN_NAME) LIKE '%"
			//		+ filterStr.toUpperCase() + "%'";
		}

		query += " ORDER BY SCREEN_NAME";

		System.out.println("Screen Query " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getCurrentInst")
	public List<Map<String, Object>> getCurrentInst(@RequestParam(value = "user") String user) {
		String query = "select INST_NUM from omnet_users where user_id = '" + user + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getTblCol")
	public List<Map<String, Object>> getTblCol(@RequestParam(value = "tbl") String tbl) {
		String query = "SELECT COLUMN_NAME FROM omnet_appln_tab_view WHERE table_name = '" + tbl + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getLOV")
	public List<Map<String, Object>> getLOV(@RequestParam(value = "screenCode") String screenCode) {
		String query = "SELECT FIELD_NAME, FIELD_LABEL, ALGORITHM_QUERY FROM OMNET_FIELD_ATTR WHERE ALGORITHM_QUERY != ' ' AND SCREEN_CODE = '"
				+ screenCode + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getLOVByField")
	public List<Map<String, Object>> getLOVByField(@RequestParam(value = "screenCode") String screenCode,
			@RequestParam(value = "field") String field) {
		String query = "SELECT FIELD_LABEL, ALGORITHM_QUERY FROM OMNET_FIELD_ATTR WHERE ALGORITHM_QUERY != ' ' AND SCREEN_CODE = '"
				+ screenCode + "' AND FIELD_NAME = '" + field + "--" + screenCode + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getValid")
	public List<Map<String, Object>> getValid(@RequestParam(value = "screenCode") String screenCode) {
		String query = "SELECT FIELD_NAME, FIELD_LABEL, VALIDATION_LOGIC FROM OMNET_FIELD_ATTR WHERE VALIDATION_LOGIC != ' ' AND SCREEN_CODE = '"
				+ screenCode + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getVals")
	public List<Map<String, Object>> getVals(@RequestParam(value = "vals") String vals) throws SQLException {
		String query = vals;
		ResultSetMetaData resultSetMetaData = null;

		// List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		List<Map<String, Object>> rows = null;
		Map<String, Object> row = null;
		try (Connection conn = dataSource.getConnection()) {
			Statement statement = conn.createStatement();
			// CallableStatement stmnt = conn.prepareCall(query);
			ResultSet rs = statement.executeQuery(query);
			resultSetMetaData = rs.getMetaData(); // etc
			rows = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				row = new HashMap<String, Object>();

				// System.out.println("Here : " +
				// rs.getString(resultSetMetaData.getColumnName(1)));

				row.put("OPT", rs.getString(resultSetMetaData.getColumnName(1)));

				if (resultSetMetaData.getColumnCount() > 1) {
					row.put("VAL", rs.getString(resultSetMetaData.getColumnName(2)));
				}
				rows.add(row);
			}

			// System.out.println("Column name: " + resultSetMetaData.getColumnName(1));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rows;
	}

	@PostMapping("/getInmateData")
	public List<Map<String, Object>> getInmateData(@RequestParam(value = "primaryVal") String primaryVal) {

		List<Map<String, Object>> commitno = jdbcTemplate
				.queryForList("Select sf_get_commit_no (?) as Commitno from dual", primaryVal);

		// List<Map<String, Object>> commitno = jdbcTemplate
		// .queryForList("SELECT current_commit_no AS Commitno FROM SBI_MST WHERE sbi_no
		// = ?", primaryVal);

		String commit_no = commitno.get(0).get("Commitno").toString();

		System.out.println("Here commit no is " + commit_no);

		String query = "SELECT * FROM INMATE WHERE COMMIT_NO = '" + commit_no + "'";
		System.out.println("query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getHistory")
	public List<Map<String, Object>> getHistory(@RequestParam(value = "primaryVal") String primaryVal) {

		String query = "SELECT Omnet_App.sf_get_commit_no(SBI_NO) AS CURRENT_COMMIT_NO,  b.COMMIT_NO, INMATE_FULL_NAME,  Omnet_App.sf_get_inst_desc(INST_NUM) AS Facility,";
		query += "COALESCE(ADMISS_METHOD,'''') AS ADMISS_METHOD, b.INST_ADMISS_DATE,  COALESCE(b.INST_ADMISS_TIME,'''') AS INST_ADMISS_TIME, INST_RELEASE_DATE,";
		query += "COALESCE(INST_RELEASE_TIME,'''') AS INST_RELEASE_TIME FROM INMATE a JOIN INMATE_DOC_IDS b ON a.COMMIT_NO = b.COMMIT_NO ";
		query += "WHERE SBI_NO = '" + primaryVal + "' ORDER BY b.COMMIT_NO DESC";

		System.out.println("history query is " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/fetchData4")
	public List<Map<String, Object>> fetchData4(@RequestParam(value = "primaryVal") String primaryVal) {
		String query = "SELECT * FROM INMATE_SSN WHERE COMMIT_NO = '" + primaryVal + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/messageData")
	public List<Map<String, Object>> messageData() {
		String query = "SELECT * FROM OMNET_MESSAGING";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getMsg")
	public List<Map<String, Object>> getMsg(@RequestParam(value = "no") String msg_no) {
		String query = "SELECT DESCRIPTION, TYPE FROM OMNET_MESSAGING WHERE MSG_NUM = '" + msg_no + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getScreenNo")
	public List<Map<String, Object>> getScreenNo(@RequestParam(value = "url") String url) {
		String query = "select * from omnet_screens where screen_url = '" + url + "' and module_name is not null";

		System.out.println("Inside getScreenNo : " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/savScreen")
	public List<Map<String, Object>> savScreen(@RequestParam(value = "screenCode") String screenCode,
			@RequestParam(value = "screenName") String screenName, @RequestParam(value = "screenUrl") String screenUrl,
			@RequestParam(value = "screenStatus") String screenStatus) {

		List<Map<String, Object>> exist_data = jdbcTemplate
				.queryForList("SELECT * FROM OMNET_SCREENS WHERE SCREEN_CODE = '" + screenCode + "'");

		String query = "";

		if (exist_data.size() == 0) {
			query = "INSERT INTO OMNET_SCREENS (SCREEN_CODE, SCREEN_NAME, SCREEN_URL, STATUS) VALUES ('" + screenCode
					+ "','" + screenName + "','" + screenUrl + "', '" + screenStatus + "')";
		} else {
			query = "UPDATE OMNET_SCREENS SET SCREEN_NAME = '" + screenName + "', SCREEN_URL = '" + screenUrl
					+ "', STATUS = '" + screenStatus + "' WHERE SCREEN_CODE = '" + screenCode + "'";
		}

		System.out.println("Modified query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/savMsg")
	public List<Map<String, Object>> savMsg(@RequestParam(value = "msg_no") String msg_no,
			@RequestParam(value = "msg_typ") String msg_typ, @RequestParam(value = "msg_desc") String msg_desc) {

		List<Map<String, Object>> exist_data = jdbcTemplate
				.queryForList("SELECT * FROM OMNET_MESSAGING WHERE MSG_NUM = '" + msg_no + "'");

		String query = "";

		if (exist_data.size() == 0) {
			query = "INSERT INTO OMNET_MESSAGING (MSG_NUM, TYPE, DESCRIPTION) VALUES ('" + msg_no + "','" + msg_typ
					+ "','" + msg_desc + "')";
		} else {
			query = "UPDATE OMNET_MESSAGING SET TYPE = '" + msg_typ + "', DESCRIPTION = '" + msg_desc
					+ "' WHERE MSG_NUM = '" + msg_no + "'";
		}

		System.out.println("Modified query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/fetchDetails")
	public List<Map<String, Object>> fetchDetails(@RequestParam(value = "primaryVal") String primaryVal,
			@RequestParam(value = "screenCode") String screenCode) {

		List<Map<String, Object>> results = jdbcTemplate.queryForList(
				"select field_name, table_name, column_name, primary_key_flag, field_level, read_only_flag, validation_logic, algorithm_query from OMNET_FIELD_ATTR where SCREEN_CODE = ? order by primary_key_flag desc",
				screenCode);

		ArrayList<String> columnlist = new ArrayList<String>();
		String tbl = results.get(0).get("table_name").toString() + " a";
		String keyval = "";

		for (int i = 0; i < results.size(); i++) {

			// if(!results.get(i).get("column_name").toString().equals("")) {

			String fld = results.get(i).get("field_name").toString();
			fld = fld.split("--" + screenCode)[0];
			String col = "a." + results.get(i).get("column_name").toString();
			String valid = results.get(i).get("validation_logic").toString();

			String algorithm = "";

			if (results.get(i).get("algorithm_query") != null) {
				algorithm = results.get(i).get("algorithm_query").toString();
			}

			if (results.get(i).get("primary_key_flag").toString().equals("Y")
					&& results.get(i).get("read_only_flag").toString().equals("Y")) {
				keyval = col;
			}

			if (!results.get(i).get("column_name").toString().equals(" ")) {

				if (valid.contains("date::")) {
					columnlist.add(col + " AS DTFLD_" + fld);
				} else if (valid.contains("checkbox::")) {
					columnlist.add(col + " AS CHFLD_" + fld);
				} else if (algorithm.contains("query::")) {
					columnlist.add(col + " AS DROPFLD_" + fld);
				} else {
					columnlist.add(col + " AS " + fld);
				}

			}

			// }
		}

		if (screenCode.equals("CBI_URNE")) {
			columnlist.add(
					"(SELECT b.FINDING_RESULT_DESC FROM FINDING_RESULT_RT b WHERE a.FINDING_RESULT_CODE = b.FINDING_RESULT_CODE) AS RESULT_DESC");
		}

		if (screenCode.equals("CBI_URNE") || screenCode.equals("CBI_ORTA")) {
			columnlist.add(
					"(SELECT B.USER_LAST_NAME || ' ' || B.USER_FIRST_NAME || ' ' || B.USER_MID_NAME || ' ' || B.USER_SUFFIX_NAME FROM OMNET_USERS B WHERE B.USER_ID = A.USER_ID) AS USER_FULL_NAME");
		}

		String base_sql = "SELECT " + StringUtils.join(columnlist, ",") + " FROM " + tbl + " WHERE a.COMMIT_NO = '"
				+ primaryVal + "' ORDER BY INSERTED_DATE_TIME DESC";

		System.out.println("base_sql " + base_sql);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(base_sql);
		return result;
	}

	@PostMapping("/getFullName")
	public List<Map<String, Object>> getFullName(@RequestParam(value = "userid") String userid) {
		String query = "SELECT USER_FIRST_NAME,USER_LAST_NAME,USER_MID_NAME,USER_SUFFIX_NAME FROM OMNET_USERS ";
		query += "WHERE user_id = '" + userid + "'";

		// System.out.println("getoffice query is " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getOfficer")
	public List<Map<String, Object>> getOfficer(@RequestParam(value = "srchkey") String srchkey) {
		String query = "SELECT USER_FIRST_NAME,USER_LAST_NAME,USER_MID_NAME,USER_SUFFIX_NAME FROM OMNET_USERS ";
		query += "WHERE LOWER(USER_FIRST_NAME) LIKE '%" + srchkey + "%' OR LOWER(USER_LAST_NAME) LIKE '%" + srchkey
				+ "%' ";
		query += "ORDER BY USER_LAST_NAME";

		// System.out.println("getoffice query is " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getOfficer2")
	public List<Map<String, Object>> getOfficer2(@RequestParam(value = "srchkey") String srchkey) {
		String query = "SELECT USER_ID, USER_FIRST_NAME,USER_LAST_NAME,USER_MID_NAME,USER_SUFFIX_NAME FROM OMNET_USERS ";
		query += "WHERE LOWER(USER_FIRST_NAME) LIKE '%" + srchkey + "%' OR LOWER(USER_LAST_NAME) LIKE '%" + srchkey
				+ "%' ";
		query += "ORDER BY USER_LAST_NAME";

		// System.out.println("getoffice query is " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/saveData")
	public void saveData(@RequestBody MultiValueMap<String, String> userFormData) {
		System.out.println("here inside save data");
		queryGenerator(userFormData);
	}

	public String monthWord(String mn) {
		String word = "";
		switch (mn) {
		case "01":
			word = "JAN";
			break;
		case "02":
			word = "FEB";
			break;
		case "03":
			word = "MAR";
			break;
		case "04":
			word = "APR";
			break;
		case "05":
			word = "MAY";
			break;
		case "06":
			word = "JUN";
			break;
		case "07":
			word = "JUL";
			break;
		case "08":
			word = "AUG";
			break;
		case "09":
			word = "SEP";
			break;
		case "10":
			word = "OCT";
			break;
		case "11":
			word = "NOV";
			break;
		case "12":
			word = "DEC";
			break;
		}
		return word;
	}

	public String dtForm(String mydt) {
		String mn = mydt.split("/")[0];
		String dt = mydt.split("/")[1];
		String yy = mydt.split("/")[2];
		yy = yy.substring(2, 4);
		mn = monthWord(mn);
		return dt + "-" + mn + "-" + yy;
	}

	public void queryGenerator2(MultiValueMap<String, String> userFormData) {

		String screenCode = userFormData.get("screenCode").get(0).toString();
		String COMMENTS = userFormData.get("COMMENTS").get(0).toString();

		System.out.println("screenCode 123 is " + screenCode);
		System.out.println("COMMENTS 123 is " + COMMENTS);
	}

	public void queryGenerator(MultiValueMap<String, String> userFormData) {

		String screenCode = userFormData.get("screenCode").get(0).toString();

		System.out.println("screenCode is " + screenCode);

		List<Map<String, Object>> levels = jdbcTemplate.queryForList(
				"select distinct FIELD_LEVEL as lvl from OMNET_FIELD_ATTR where SCREEN_CODE = ? and FIELD_LEVEL > 0 order by FIELD_LEVEL",
				screenCode);

		System.out.println("levels.size() is " + levels.size());

		for (int i = 0; i < levels.size(); i++) {
			System.out.println("levelxy is " + levels.get(i).get("lvl"));
			String level = levels.get(i).get("lvl").toString();

			System.out.println("level is " + level);

			int rowcnt = Integer.parseInt(userFormData.get("row_cnt" + level).get(0));
			boolean seq_field = false;

			System.out.println("rowcnt is " + rowcnt);

			for (int x = 0; x < rowcnt; x++) {

				System.out.println("x is " + x);

				List<Map<String, Object>> result = jdbcTemplate.queryForList(
						"select field_name, table_name, column_name, primary_key_flag, field_level, read_only_flag, validation_logic from OMNET_FIELD_ATTR where SCREEN_CODE = ? and FIELD_LEVEL = ? order by FIELD_LEVEL, primary_key_flag desc",
						screenCode, level);

				String tbl = result.get(0).get("table_name").toString();
				ArrayList<String> columnlist = new ArrayList<String>();
				ArrayList<String> fieldlist = new ArrayList<String>();
				ArrayList<String> dataval = new ArrayList<String>();
				ArrayList<String> primary_col = new ArrayList<String>();
				ArrayList<String> primary_set = new ArrayList<String>();

				System.out.println("tbl is " + tbl);
				System.out.println("result.size() is " + result.size());

				for (int j = 0; j < result.size(); j++) {
					String fld = result.get(j).get("field_name").toString();
					fld = fld.split("--" + screenCode)[0];

					System.out.println("1 j:" + j + " and " + fld);

					String col = result.get(j).get("column_name").toString();

					// System.out.println("2 j:" + j);

					// System.out.println("Here val is " + userFormData.get(fld).get(0).toString());

					String val = userFormData.get(fld).get(0).toString().split("!@#!")[x];

					if (val.equals("{e}")) {
						val = "";
					}

					// System.out.println("3 j:" + j);

					fieldlist.add(fld);
					columnlist.add(col);

					System.out.println("A j:" + j);

					String dt = result.get(j).get("validation_logic").toString();

					// System.out.println("dt before process : " + dt);

					dt = dt.split("::")[0];

					// System.out.println("dt after process : " + dt + " and val is " + val);

					// System.out.println("B j:" + j);

					if (dt.equals("date")) {
						if (!val.equals("")) {
							Date dtobj = new Date(val);
							SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
							dataval.add("'" + df.format(dtobj) + "'");
						} else {
							dataval.add("'" + val + "'");
						}
					} else {
						dataval.add("'" + val + "'");
					}

					// System.out.println("C j:" + j);

					String key = result.get(j).get("primary_key_flag").toString();
					String read_only = result.get(j).get("read_only_flag").toString();

					// System.out.println("D j:" + j);

					if (key.equals("Y")) {

						if (dt.equals("date")) {
							Date dtobj2 = new Date(val);
							SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy");
							primary_set.add(col + " = '" + df2.format(dtobj2) + "'");
						} else {
							primary_set.add(col + " = '" + val + "'");
						}
						primary_col.add("Y");

						if (read_only.equals("Y")) {
							seq_field = true;
						} else {
							seq_field = false;
						}

					} else {
						primary_col.add("N");
					}

					// System.out.println("E j:" + j);

				}

				System.out.println(
						"test 14: " + "select * from " + tbl + " where " + StringUtils.join(primary_set, " and "));

				List<Map<String, Object>> exist_data = jdbcTemplate
						.queryForList("select * from " + tbl + " where " + StringUtils.join(primary_set, " and "));

				String Query = "";

				if (exist_data.size() == 0) {

					System.out.println("here level is " + level);
					/*
					 * if (!level.equals("3")) { columnlist.remove(0); dataval.remove(0); }
					 */

					if (seq_field) {

						System.out.println(
								"check query asp " + "select max(" + columnlist.get(0) + ") + 1 as seq from " + tbl);

						List<Map<String, Object>> new_seq = jdbcTemplate
								.queryForList("select max(" + columnlist.get(0) + ") + 1 as seq from " + tbl);

						String seq = "1";
						if (new_seq.get(0).get("seq") != null) {
							seq = new_seq.get(0).get("seq").toString();
						}

						dataval.remove(0);
						dataval.add(0, seq);
					}

					Query += "insert into " + tbl + " (" + StringUtils.join(columnlist, ",") + ") values ("
							+ StringUtils.join(dataval, ",") + ")";

				} else {
					int k = columnlist.size() - 1;

					Query += "update " + tbl + " set ";

					for (int j = 0; j < columnlist.size(); j++) {
						if (!primary_col.get(j).equals("Y")) {
							Query += columnlist.get(j) + " = " + dataval.get(j) + "";

							if (j != k) {
								Query += ", ";
							}
						}
					}

					Query += " where " + StringUtils.join(primary_set, " and ");
				}

				System.out.println(level + " final query " + Query);

				jdbcTemplate.execute(Query);

			}
		}
	}

	@PostMapping("/saveMapData")
	public void saveMapData(@RequestBody Map<String, List<Map<String, Object>>> jsonData) {

		System.out.println("Here our jsondata : " + jsonData);
	}

}
