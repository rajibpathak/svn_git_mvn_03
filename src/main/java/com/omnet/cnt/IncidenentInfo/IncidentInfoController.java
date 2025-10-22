package com.omnet.cnt.IncidenentInfo;

import java.sql.Array;
import oracle.jdbc.OracleConnection;
import oracle.sql.ArrayDescriptor;
import oracle.sql.ARRAY;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class IncidentInfoController {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/getInst4Inci")
	public List<Map<String, Object>> getInst() {
		String query = "SELECT DISTINCT i.inst_num, i.inst_name FROM institution I, user_access_inst U WHERE status = 'A' ORDER BY inst_name";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/genSeq")
	public List<Map<String, Object>> genSeq() {
		String query = "Select Sf_Gen_Seq_Mdoc ('0006_03') as AutoSeq from dual";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getTypeOfForce")
	public List<Map<String, Object>> getTypeOfForce() {
		String query = "Select ref_value_code,ref_value_Desc as TYPEFORCE from CM_REFERENCE_VALUES where REF_CATEGORY_MODULE='ING' and ref_category_code='TP_FORCE' Order by REF_VALUE_DESC";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getTypeOfRestraint")
	public List<Map<String, Object>> getTypeOfRestraint() {
		String query = "Select ref_value_code,ref_value_Desc as TYPERESTRAINT from CM_REFERENCE_VALUES where REF_CATEGORY_MODULE='ING' and ref_category_code='TP_RESTRNT' Order by REF_VALUE_DESC";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getIncidentStaff")
	public List<Map<String, Object>> getIncidentStaff(@RequestParam(value = "srchkey") String srchkey) {
		String query = "SELECT USER_ID, USER_FIRST_NAME,USER_LAST_NAME,USER_MID_NAME,USER_SUFFIX_NAME, TITLE FROM OMNET_USERS ";
		query += "WHERE LOWER(USER_FIRST_NAME) LIKE '%" + srchkey + "%' OR LOWER(USER_LAST_NAME) LIKE '%" + srchkey
				+ "%' ";
		query += "ORDER BY USER_LAST_NAME";

		System.out.println("getIncidentStaff query is " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getActionTaken")
	public List<Map<String, Object>> getActionTaken() {
		String query = "Select ref_value_code,ref_value_Desc as ACTIONTAKEN from CM_REFERENCE_VALUES where REF_CATEGORY_MODULE='ING' and ref_category_code='ACTN_TKN' Order by REF_VALUE_DESC";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getHousingUnit")
	public List<Map<String, Object>> getHousingUnit(@RequestParam String instNum) {
		String query = "Select unit_desc as opt, unit_id as val from INST_UNIT_RT where 1=1 AND STATUS='A' AND Upper(INST_NUM)=Upper('"
				+ instNum + "')";
		System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getSuppliment")
	public List<Map<String, Object>> getSuppliment(@RequestParam String incidentNo) {
		String query = "SELECT incident_seq_num FROM incident WHERE initial_incident_seq_num = '" + incidentNo + "'";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getDReport")
	public List<Map<String, Object>> getDReport(@RequestParam String incidentNo) {
		String query = "SELECT COMMIT_NO,D_REPORT_NUM,INCIDENT_SEQ_NUM,USER_ID,D_REPORT_DATE,D_REPORT_TIME,ROWIDTOCHAR(DISCIPLINE.ROWID) FROM DISCIPLINE WHERE INCIDENT_SEQ_NUM = '"
				+ incidentNo + "'";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getType")
	public List<Map<String, Object>> getType() {
		String query = "SELECT Individual_desc as OPT, Individual_num as VAL FROM type_of_indv_rt WHERE STATUS='A' ORDER BY Individual_desc";

		System.out.println("here query " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getTitle")
	public List<Map<String, Object>> getTitle() {
		String query = "SELECT Title_desc as OPT, Title as VAL FROM Title_rt WHERE STATUS='A' ORDER BY title_desc";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getRace")
	public List<Map<String, Object>> getRace() {
		String query = "select race_desc as OPT, race_code as VAL from race_rt";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getCategory")
	public List<Map<String, Object>> getCategory() {
		String query = "Select ref_value_Desc as OPT, ref_value_code as VAL from CM_REFERENCE_VALUES where REF_CATEGORY_MODULE='ING' and ref_category_code='INVDL_CAT' Order by REF_VALUE_DESC";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getWarrantType")
	public List<Map<String, Object>> getWarrantType() {
		String query = "SELECT ref_value_desc as OPT, ref_value_code as VAL FROM cm_reference_values WHERE ref_category_module = 'CSM' AND ref_category_code = 'WRNT_TYPE'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getLocationCategory")
	public List<Map<String, Object>> getLocationCategory() {
		String query = "SELECT ref_value_desc as OPT, ref_value_code as VAL FROM cm_reference_values WHERE  ref_category_module = 'ING' AND ref_category_code = 'LOC_CATE'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getEvType")
	public List<Map<String, Object>> getEvType() {
		String query = "Select ref_value_Desc as OPT, ref_value_code as VAL from CM_REFERENCE_VALUES where REF_CATEGORY_MODULE='ING' and ref_category_code='EVDN_TP' Order by REF_VALUE_DESC";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getNatInj")
	public List<Map<String, Object>> getNatInj() {
		String query = "select ref_value_desc as OPT, ref_value_code as VAL from cm_reference_values where ref_category_module = 'ING' and ref_category_code = 'NATR_INJR'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getNames")
	public List<Map<String, Object>> getNames(@RequestParam String userid) {
		String query = "Select USER_LAST_NAME,USER_FIRST_NAME,USER_MID_NAME,USER_SUFFIX_NAME,TITLE from Omnet_Users where USER_ID = '"
				+ userid + "'";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getInmateNames")
	public List<Map<String, Object>> getInmateNames(@RequestParam String sbiNo) {
		String query = "select * from inmate where commit_no = sf_get_commit_no('" + sbiNo + "')";
		// System.out.println("Seq query : " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getIncidents")
	public List<Map<String, Object>> getIncidents(@RequestParam String incidentNo, @RequestParam String fromDt,
			@RequestParam String toDt, @RequestParam String locCode) {
		// String query = "select incident_seq_num, incident_date, count_loc_code from
		// INCIDENT where incident_date between '31-03-25' and '30-04-25' ";
		String query = "select incident_seq_num, incident_date, count_loc_code, shift_commander_apr_flag from INCIDENT where 1=1";

		// if (!incidentNo.equals("") || !fromDt.equals("") || !toDt.equals("") ||
		// !locCode.equals("")) {
		// query += "where ";

		if (!incidentNo.equals("")) {
			query += " and incident_seq_num = '" + incidentNo + "' ";
		}

		if (!fromDt.equals("") && !toDt.equals("")) {
			query += " and (incident_date between '" + formatMDate(fromDt) + "' and '" + formatMDate(toDt) + "')";
		} else if (!fromDt.equals("")) {
			query += " and incident_date >= '" + formatMDate(fromDt) + "'";
		} else if (!toDt.equals("")) {
			query += " and incident_date <= '" + formatMDate(toDt) + "'";
		}

		if (!locCode.equals("")) {
			query += " and count_loc_code = '" + locCode + "'";
		}

		// }

		query += " order by incident_seq_num desc";

		System.out.println("final query : " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	public static String formatMDate(String inputDate) {
		// Define the input and output date formats
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

		try {
			// Parse the input date
			Date date = inputFormat.parse(inputDate);
			// Format the date to the desired output format
			return outputFormat.format(date).toUpperCase();
		} catch (ParseException e) {
			e.printStackTrace();
			return null; // or handle the error as needed
		}
	}

	@PostMapping("/getGropDtls")
	public List<Map<String, Object>> getGropDtls(@RequestParam String grpSeq) {
		String query = "SELECT  incident_seq_num, incident_date, incident_time, short_description,";
		query += "(select inst_name from institution a where a.inst_num = b.inst_num) as inst ";
		query += "FROM incident b where incident_grp_seq_no = '" + grpSeq + "'";
		System.out.println("final query : " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/fetchIncidentInfo")
	public List<Map<String, Object>> fetchInfo(@RequestParam String incidentNo) {
		System.out.println("alpha incident no=" + incidentNo);
		System.out.println("Take 001 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call spkg_omnet_wrapper.sp_dis_incident_query(?,?)}";

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							Object columnValue = rs.getObject(i);

							if (columnValue instanceof java.sql.Clob) {
								java.sql.Clob clob = (java.sql.Clob) columnValue;
								if (clob != null && clob.length() > 0) {
									resultMap.put(columnName, clob.getSubString(1, (int) clob.length()));
								} else {
									resultMap.put(columnName, null);
								}
							} else {
								resultMap.put(columnName, columnValue);
							}
						}
						resultList.add(resultMap);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping("/fetchIncidentGenInfo")
	public List<Map<String, Object>> fetchIncidentGenInfo(@RequestParam String incidentNo,
			@RequestParam String instNum) {
		System.out.println("fetchIncidentGenInfo incident no = " + incidentNo);
		System.out.println("fetchIncidentGenInfo instNum = " + instNum);
		System.out.println("Take 002 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_incident_query(?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping("/getIncidentIndivdualInfo")
	public List<Map<String, Object>> getIncidentIndivdualInfo(@RequestParam String incidentNo,
			@RequestParam String instNum) {
		System.out.println("fetchIncidentGenInfo incident no = " + incidentNo);
		System.out.println("fetchIncidentGenInfo instNum = " + instNum);
		System.out.println("Take 002 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.individuals_query(?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@PostMapping("/getInciIndvInfoByCell")
	public List<Map<String, Object>> getInciIndvInfoByCell(@RequestParam String cellNo) {
		System.out.println("getInciIndvInfoByCell cellNo = " + cellNo);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call spkg_omnet_wrapper.sp_dis_inci_individuals_query(?,?)}";

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, cellNo);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@PostMapping("/getEviInfoByCell")
	public List<Map<String, Object>> getEviInfoByCell(@RequestParam String cellNo) {
		System.out.println("getEviInfoByCell cellNo = " + cellNo);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call spkg_omnet_wrapper.sp_dis_inci_evidence_query(?,?)}";

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, cellNo);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchIncidentIndivdualInfo")
	public List<Map<String, Object>> fetchIncidentIndivdualInfo(@RequestParam String incidentNo,
			@RequestParam String instNum) {
		System.out.println("fetchIncidentGenInfo incident no = " + incidentNo);
		System.out.println("fetchIncidentGenInfo instNum = " + instNum);
		System.out.println("Take 002 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.individuals_query(?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchStandardCheckList")
	public List<Map<String, Object>> fetchStandardCheckList(@RequestParam String incidentNo,
			@RequestParam String instNum) {
		System.out.println("fetchIncidentGenInfo incident no = " + incidentNo);
		System.out.println("fetchIncidentGenInfo instNum = " + instNum);
		System.out.println("Take 002 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_additional_dtl_qry(?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchRefferalHistory")
	public List<Map<String, Object>> fetchRefferalHistory(@RequestParam String incidentNo) {
		System.out.println("fetchIncidentGenInfo incident no = " + incidentNo);
		System.out.println("Take 002 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.inci_ref_hist_qry(?,?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, "");
				stmt.setString(3, incidentNo);
				stmt.setString(4, "");
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchApproval")
	public List<Map<String, Object>> fetchApproval(@RequestParam String incidentNo) {
		System.out.println("fetchIncidentGenInfo incident no = " + incidentNo);
		System.out.println("Take 002 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.query_staff_rev(?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchInjuredPersons")
	public List<Map<String, Object>> fetchInjuredPersons(@RequestParam String incidentNo) {
		System.out.println("fetchInjuredPersons incident no = " + incidentNo);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.QUERY_VICTIM(?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchEvidence")
	public List<Map<String, Object>> fetchEvidence(@RequestParam String incidentNo) {
		System.out.println("fetchInjuredPersons incident no = " + incidentNo);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.QUERY_EVIDENCE(?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping("/fetchAssignLeadInv")
	public List<Map<String, Object>> fetchAssignLeadInv(@RequestParam String incidentNo, @RequestParam String instNum) {
		System.out.println("fetchAssignLeadInv incident no = " + incidentNo);
		System.out.println("fetchAssignLeadInv inst no = " + instNum);
		List<Map<String, Object>> resultList = new ArrayList<>();
		String sql = "{call spkg_omnet_wrapper.sp_dis_related_inci_qry(?,?,?)}";

		try (Connection conn = dataSource.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

			OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
			ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("TB_DIS_RELATED_INCI", oracleConn);
			ARRAY inArray = new ARRAY(descriptor, oracleConn, new Object[] {});
			OracleCallableStatement ocstmt = stmt.unwrap(OracleCallableStatement.class);
			ocstmt.setArray(1, inArray);
			ocstmt.registerOutParameter(1, OracleTypes.ARRAY, "TB_DIS_RELATED_INCI");
			ocstmt.setString(2, incidentNo);
			ocstmt.setString(3, instNum);
			ocstmt.execute();

			Array oracleArray = ocstmt.getArray(1);
			if (oracleArray != null) {
				Object[] data = (Object[]) oracleArray.getArray();
				for (Object element : data) {
					Struct struct = (Struct) element;
					Object[] attributes = struct.getAttributes();
					Map<String, Object> resultMap = new HashMap<>();
					resultMap.put("INCIDENT_SEQ_NUM", attributes[0]);
					resultMap.put("INCIDENT_DATE", attributes[1]);
					resultMap.put("INCIDENT_TIME", attributes[2]);
					resultMap.put("SHORT_DESCRIPTION", attributes[3]);
					resultList.add(resultMap);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchAuditTrial")
	public List<Map<String, Object>> fetchAuditTrial(@RequestParam String incidentNo, @RequestParam String instNum,
			@RequestParam String typeAction) {
		System.out.println("fetchAuditTrial incident no = " + incidentNo);
		System.out.println("fetchAuditTrial inst no = " + instNum);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_audit_trial_qry(?,?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.setString(4, typeAction);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping("/fetchTypForce")
	public List<Map<String, Object>> fetchTypForce(@RequestParam String incidentNo, @RequestParam String instNum,
			@RequestParam String indSeq) {
		System.out.println("fetchTypForce incident no = " + incidentNo);
		System.out.println("fetchTypForce inst no = " + instNum);
		System.out.println("fetchTypForce indseq = " + indSeq);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_invd_force_tp_qry(?,?,?,?)}";

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.setString(4, indSeq);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping("/fetchTypRestraint")
	public List<Map<String, Object>> fetchTypRestraint(@RequestParam String incidentNo, @RequestParam String instNum,
			@RequestParam String indSeq) {
		System.out.println("fetchTypForce incident no = " + incidentNo);
		System.out.println("fetchTypForce inst no = " + instNum);
		System.out.println("fetchTypForce indseq = " + indSeq);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_invd_restraint_tp_qry(?,?,?,?)}";

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.setString(4, indSeq);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping("/fetchActionTaken")
	public List<Map<String, Object>> fetchActionTaken(@RequestParam String incidentNo, @RequestParam String instNum,
			@RequestParam String indSeq) {
		System.out.println("fetchTypForce incident no = " + incidentNo);
		System.out.println("fetchTypForce inst no = " + instNum);
		System.out.println("fetchTypForce indseq = " + indSeq);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_invd_action_taken_qry(?,?,?,?)}";

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.setString(4, indSeq);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@GetMapping("/fetchGroupSuppliment")
	public List<Map<String, Object>> fetchGroupSuppliment(@RequestParam String incidentNo,
			@RequestParam String instNum) {
		System.out.println("fetchGroupSuppliment incident no = " + incidentNo);
		System.out.println("fetchGroupSuppliment instNum = " + instNum);
		System.out.println("Take 002 xxx");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_related_inci_qry(?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/getCellSearch")
	public List<Map<String, Object>> getCellSearch(@RequestParam String irhSeq, @RequestParam String instNum,
			@RequestParam String dtFrom, @RequestParam String dtTo, @RequestParam String srchLoc,
			@RequestParam String instLoc) {
		System.out.println("getCellSearch incident no = " + irhSeq);
		System.out.println("getCellSearch instNum = " + instNum);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_rc_cell_search_query(?,?,?,?, ?,?,?)}";
			// String sql = "{call SPKG_DIS_INCI.INCIDENT_QUERY(?,?)}";
			// String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			// System.out.println("Expecting sql " + sql);

			System.out.println(dtFrom + " and " + dtTo);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, irhSeq);
				stmt.setString(3, instNum);
				stmt.setString(4, dtFrom);
				stmt.setString(5, dtTo);
				stmt.setString(6, srchLoc);
				stmt.setString(7, instLoc);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					// System.out.println("Here " + columnCount);

					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							// System.out.println("vitalcolum : " + columnName);
							Object columnValue = rs.getObject(i);
							resultMap.put(columnName, columnValue);
						}
						resultList.add(resultMap);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchHousingDtls")
	public List<Map<String, Object>> fetchHousingDtls(@RequestParam String commitNo) {
		System.out.println("fetchHousingDtls commitNo = " + commitNo);
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();
		String outParam1 = null;
		String outParam2 = null;

		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call spkg_cbi_prls.Sp_Get_det(?,?,?,?, ?,?,?,?)}";

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				stmt.setString(1, commitNo); // Input parameter
				stmt.registerOutParameter(2, java.sql.Types.VARCHAR); // For out_param1
				stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
				stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
				stmt.registerOutParameter(5, java.sql.Types.VARCHAR);
				stmt.registerOutParameter(6, java.sql.Types.VARCHAR);
				stmt.registerOutParameter(7, java.sql.Types.VARCHAR);
				stmt.registerOutParameter(8, java.sql.Types.VARCHAR);

				stmt.execute();

				response.put("inst_name", stmt.getString(2));
				response.put("bld_name", stmt.getString(3));
				response.put("unit_desc", stmt.getString(4));
				response.put("floor_desc", stmt.getString(5));
				response.put("tier_desc", stmt.getString(6));
				response.put("cell_desc", stmt.getString(7));
				response.put("bed_desc", stmt.getString(8));

				resultList.add(response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@PostMapping("/fetchAsnMoreRevs")
	public List<Map<String, Object>> fetchAsnMoreRevs(@RequestParam String incidentNo, @RequestParam String instNum) {
		System.out.println("fetchAsnMoreRevs incident no = " + incidentNo);
		System.out.println("fetchAsnMoreRevs instNum = " + instNum);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Dis_Inci.sp_refr_officers_qry(?,?,?)}";
			try (CallableStatement stmt = conn.prepareCall(sql)) {
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, incidentNo);
				stmt.setString(3, instNum);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							resultMap.put(metaData.getColumnName(i), rs.getObject(i));
						}
						resultList.add(resultMap);
					}
				}
			}
			if (!resultList.isEmpty()) {
				List<String> userIds = resultList.stream().map(row -> (String) row.get("REFR_USER_ID"))
						.filter(id -> id != null && !id.isEmpty()).collect(Collectors.toList());
				if (!userIds.isEmpty()) {
					String nameSql = "SELECT USER_ID, USER_LAST_NAME, USER_FIRST_NAME, USER_MID_NAME, USER_SUFFIX_NAME FROM Omnet_Users WHERE USER_ID IN ("
							+ String.join(",", Collections.nCopies(userIds.size(), "?")) + ")";
					Map<String, Map<String, Object>> userNamesMap = new HashMap<>();
					try (PreparedStatement nameStmt = conn.prepareStatement(nameSql)) {
						for (int i = 0; i < userIds.size(); i++) {
							nameStmt.setString(i + 1, userIds.get(i));
						}
						try (ResultSet nameRs = nameStmt.executeQuery()) {
							while (nameRs.next()) {
								Map<String, Object> nameInfo = new HashMap<>();
								nameInfo.put("USER_LAST_NAME", nameRs.getString("USER_LAST_NAME"));
								nameInfo.put("USER_FIRST_NAME", nameRs.getString("USER_FIRST_NAME"));
								nameInfo.put("USER_MID_NAME", nameRs.getString("USER_MID_NAME"));
								nameInfo.put("USER_SUFFIX_NAME", nameRs.getString("USER_SUFFIX_NAME"));
								userNamesMap.put(nameRs.getString("USER_ID"), nameInfo);
							}
						}
					}
					for (Map<String, Object> row : resultList) {
						String userId = (String) row.get("REFR_USER_ID");
						if (userNamesMap.containsKey(userId)) {
							row.putAll(userNamesMap.get(userId));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public void savAssignMoreReviewers(List<Map<String, Object>> data, String incident_no, String inst_no) {

		for (int i = 0; i < data.size(); i++) {

			String exist_sql = "select * from incident_referred_officer where incident_seq_num = '" + incident_no
					+ "' ";
			exist_sql += "and inst_num = '" + inst_no + "' ";
			exist_sql += "and refr_user_id = '" + data.get(i).get("refr_user_id") + "'";

			List<Map<String, Object>> ref_ofc_exist = jdbcTemplate.queryForList(exist_sql);

			System.out.println("print sql : " + exist_sql);

			String procedureCall = "";

			if (ref_ofc_exist.size() == 0) {
				procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_refr_officers_insert(?)}";
				System.out.println("Asn More in insert");
			} else {
				procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_refr_officers_update(?)}";
				System.out.println("Asn More in update");
			}

			String ty_typ = "";
			String tb_typ = "";

			ty_typ = "TY_DIS_INCI_REFR_OFFICERS";
			tb_typ = "TB_DIS_INCI_REFR_OFFICERS";

			exeAsnMore(procedureCall, ty_typ, tb_typ, data.get(i).get("refr_user_id").toString(), incident_no, inst_no,
					data.get(i).get("date").toString(), data.get(i).get("time").toString());
		}

	}

	public void exeAsnMore(String proCall, String ty_typ, String tb_typ, String refr_user_id, String inciNo,
			String instNo, String date, String time) {

		Struct[] structs = new Struct[1];

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(proCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println(refr_user_id + " for " + date + " & " + time);

			structs[0] = oracleConnection.createStruct(ty_typ,

					new Object[] { null, null, inciNo, instNo, EmptyToNull(refr_user_id), EmptyToNull(date),
							EmptyToNull(time), null });

			System.out.println("post procedure: " + proCall);

			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	/*
	 * public void savAssignMoreReviewers(List<Map<String, Object>> data, String
	 * incidentNo, String instNum) { if (data == null || data.isEmpty()) { return; }
	 * List<Map<String, Object>> toInsert = new ArrayList<>(); List<Map<String,
	 * Object>> toUpdate = new ArrayList<>(); for (Map<String, Object> row : data) {
	 * if (Boolean.TRUE.equals(row.get("is_new"))) { toInsert.add(row); } else {
	 * toUpdate.add(row); } } if (!toInsert.isEmpty()) { String insertProcedure =
	 * "{call spkg_omnet_wrapper.sp_dis_inci_refr_officers_insert(?)}";
	 * executeReviewerProcedure(insertProcedure, toInsert, incidentNo, instNum); }
	 * if (!toUpdate.isEmpty()) { String updateProcedure =
	 * "{call spkg_omnet_wrapper.sp_dis_inci_refr_officers_update(?)}";
	 * executeReviewerProcedure(updateProcedure, toUpdate, incidentNo, instNum); } }
	 */

	private void executeReviewerProcedure(String procedureCall, List<Map<String, Object>> data, String incidentNo,
			String instNum) {
		try (Connection connection = dataSource.getConnection();
				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {
				Map<String, Object> row = data.get(i);
				structs[i] = oracleConnection.createStruct("TY_DIS_INCI_REFR_OFFICERS",
						new Object[] { null, EmptyToNull((String) row.get("notify_flg")), incidentNo, instNum,
								EmptyToNull((String) row.get("refr_user_id")), EmptyToNull((String) row.get("date")),
								EmptyToNull((String) row.get("time")), null });
			}
			Array array = oracleConnection.createOracleArray("TB_DIS_INCI_REFR_OFFICERS", structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing reviewer stored procedure: " + procedureCall, e);
		}
	}

	@PostMapping("/delIncident")
	public void delIncident(@RequestBody Map<String, List<Map<String, Object>>> combinedData) {
	    List<Map<String, Object>> data = combinedData.get("tab0");
	    if (data == null || data.isEmpty()) {
	        System.out.println("No individuals data received for deletion.");
	        return;
	    }

	    // This procedure seems to be for both update and delete, so we call the update one
	    // which should handle the delete flag.
	    String procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update_2(?)}";
	    String ty_typ = "TY_DIS_INDIVIDUALS_INVOL_INSUPD";
	    String tb_typ = "TB_DIS_INDIVIDUALS_INVOL_INSUPD";

	    try (Connection connection = dataSource.getConnection();
	         CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
	        OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

	        Struct[] structs = new Struct[data.size()];
	        for (int i = 0; i < data.size(); i++) {
	            Map<String, Object> item = data.get(i);
	            
	            // Create a struct with only the necessary fields for deletion/update
	            // Pass null for fields not needed to avoid buffer overflow
	            structs[i] = oracleConnection.createStruct(ty_typ,
	                new Object[] {
	                    item.get("INCIDENT_SEQ_NUM"),
	                    item.get("INDV_SEQ_NUM"),
	                    null, // INDIVIDUAL_NUM
	                    null, // NAME_FIRST
	                    null, // NAME_LAST
	                    null, // NAME_MIDDLE
	                    null, // NAME_SUFFIX
	                    null, // sex
	                    0,    // age
	                    null, // race
	                    null, // COMMIT_NO
	                    null, // sbi_no
	                    null, // title_prsn
	                    item.get("indv_del_flg"), // *** The important flag ***
	                    null, // inst_desc
	                    item.get("user_id"),
	                    item.get("inst_num"),
	                    null, // individual_category
	                    item.get("ROW_ID")
	                }
	            );
	        }

	        Array array = oracleConnection.createOracleArray(tb_typ, structs);
	        callableStatement.setArray(1, array);
	        System.out.println("Executing update/delete for individuals involved...");
	        callableStatement.execute();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure for individual deletion/update", e);
	    }
	}
	
	@PostMapping("/delInjured")
	public void delInjured(@RequestBody Map<String, List<Map<String, Object>>> combinedData) {
	    List<Map<String, Object>> data = combinedData.get("tab0");
	    if (data == null || data.isEmpty()) {
	        System.out.println("No data received for victim deletion.");
	        return;
	    }

	    String procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_delete_victim(?,?,?,?)}";
	    String ty_typ = "TY_DIS_INCIDENT_VICTIM_REC";
	    String tb_typ = "TB_DIS_INCIDENT_VICTIM_REC";

	    try (Connection connection = dataSource.getConnection()) {
	        
	        for (Map<String, Object> item : data) {
	            if ("Y".equals(item.get("delete_flag"))) {
	                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
	                    
	                    OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

	                    // *** THE FIX IS HERE: Use String.valueOf() for safe conversion ***
	                    String incidentSeqNum = String.valueOf(item.get("INCIDENT_SEQ_NUM"));
	                    String instNum = String.valueOf(item.get("inst_num"));
	                    String victimSeqNum = String.valueOf(item.get("VICTIM_SEQ_NUM"));
	                    String userId = String.valueOf(item.get("USER"));
	                    String dateTime = String.valueOf(item.get("DATE_TIME"));

	                    System.out.println("Preparing to delete Victim Seq: " + victimSeqNum + " for Incident: " + incidentSeqNum);

	                    Struct[] structs = new Struct[1];
	                    
	                    structs[0] = oracleConnection.createStruct(ty_typ,
	                        new Object[] {
	                            victimSeqNum,
	                            null, // INCD_VCTM_FST_NM
	                            null, // INCD_VCTM_MID_NM
	                            null, // INCD_VCTM_LST_NM
	                            null, // INCD_VCTM_SFX_NM
	                            null, // INCD_VCTM_INJR_DESC
	                            null, // INCD_VCTM_HSP_FLG
	                            null, // INCD_VCTM_HSP_LOCN
	                            instNum,
	                            incidentSeqNum,
	                            userId, // INSERTED_USERID
	                            dateTime, // INSERTED_DATE_TIME
	                            userId, // UPDATED_USERID
	                            dateTime, // UPDATED_DATE_TIME
	                            null, // SESSION_ID
	                            null, // TERMINAL
	                            userId, // USER_ID_ENTERED_BY
	                            null, // COUNT_LOC_CODE
	                            null, // INCIDENT_DATE
	                            null, // INCIDENT_TIME
	                            "Y",  // INCD_VCTM_DEL_FLG
	                            null, // NATURE_OF_INJURY
	                            null, // MED_TREATMENT_PROVIDED
	                            null  // INDV_SEQ_NUM
	                        }
	                    );

	                    Array array = oracleConnection.createOracleArray(tb_typ, structs);
	                    
	                    callableStatement.setArray(1, array);
	                    callableStatement.setString(2, incidentSeqNum);
	                    callableStatement.setString(3, instNum);
	                    callableStatement.setString(4, victimSeqNum);
	                    
	                    callableStatement.execute();
	                    System.out.println("Executed delete for Victim Seq: " + victimSeqNum);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure for victim deletion", e);
	    }
	}	
	@PostMapping("/saveIncident")
	public void saveIncident(@RequestBody Map<String, List<Map<String, Object>>> combinedData) {

		List<Map<String, Object>> table0Data = combinedData.get("tab0");
		List<Map<String, Object>> table1Data = combinedData.get("tab1");
		List<Map<String, Object>> table2Data = combinedData.get("tab2");
		List<Map<String, Object>> table3Data = combinedData.get("tab3");
		List<Map<String, Object>> table4Data = combinedData.get("tab4");
		List<Map<String, Object>> table5Data = combinedData.get("tab5");
		// List<Map<String, Object>> typForce = combinedData.get("typForce");
		// List<Map<String, Object>> typRestraint = combinedData.get("typRestraint");
		// List<Map<String, Object>> actionTaken = combinedData.get("actionTaken");
		List<Map<String, Object>> genInfo = combinedData.get("genInfo");

		List<Map<String, Object>> asnOfc = combinedData.get("asnOfc");

		List<Map<String, Object>> assignInvestigatorData = combinedData.get("assignInvestigator");
		List<Map<String, Object>> otherInvestigatorsData = combinedData.get("otherInvestigators");
		// List<Map<String, Object>> assignMoreReviewersData =
		// combinedData.get("assignMoreReviewers");

		System.out.println("Received assignInvestigator: " + assignInvestigatorData);
		System.out.println("Received otherInvestigators: " + otherInvestigatorsData);

		System.out.println("Received data0: " + table0Data);
		System.out.println("Received data1: " + table1Data);
		System.out.println("Received data2: " + table2Data);
		System.out.println("Received data3: " + table3Data);
		System.out.println("Received data4: " + table4Data);
		System.out.println("Received data5: " + table5Data);
		// System.out.println("Received typForce: " + typForce);
		// System.out.println("Received typRestraint: " + typRestraint);
		// System.out.println("Received actionTaken: " + actionTaken);
		System.out.println("Received genInfo: " + genInfo);
		System.out.println("Received Assign More Off: " + asnOfc);

		if (table0Data != null) {
			savBasicIncident(table0Data);
		}

		if (table1Data != null) {

			// savIndv(table1Data);

			List<Map<String, Object>> toInsertIndv = new ArrayList<>();
			List<Map<String, Object>> toUpdateIndv = new ArrayList<>();

			for (Map<String, Object> datum : table1Data) {
				String rowid = (String) datum.get("ROW_ID");

				System.out.println("rowid from initial : " + rowid);

				if (rowid == null || rowid.isEmpty()) {
					toInsertIndv.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateIndv.add(datum); // Existing entry
				}
			}

			System.out.println("toInsertIndv size : " + toInsertIndv.size());
			System.out.println("toUpdateIndv size : " + toUpdateIndv.size());

			if (toInsertIndv.size() > 0) {
				savIndv(toInsertIndv, "I");
			}

			if (toUpdateIndv.size() > 0) {
				savIndv(toUpdateIndv, "U");
			}

		}

		/*
		 * if (table2Data != null) {
		 * 
		 * // savIndv(table1Data);
		 * 
		 * System.out.println("check inside table2Data");
		 * 
		 * List<Map<String, Object>> toInsertVctm = new ArrayList<>(); List<Map<String,
		 * Object>> toUpdateVctm = new ArrayList<>();
		 * 
		 * for (Map<String, Object> datum : table2Data) { String vctmSeq = (String)
		 * datum.get("VICTIM_SEQ_NUM").toString();
		 * 
		 * System.out.println("vctmSeq from initial : " + vctmSeq);
		 * 
		 * if (vctmSeq == null || vctmSeq.isEmpty()) { toInsertVctm.add(datum); // No
		 * row-id means it's a new entry } else { toUpdateVctm.add(datum); // Existing
		 * entry } }
		 * 
		 * System.out.println("toInsertVctm size : " + toInsertVctm.size());
		 * System.out.println("toUpdateVctm size : " + toUpdateVctm.size());
		 * 
		 * if (toInsertVctm.size() > 0) { savVctm(toInsertVctm, "I"); }
		 * 
		 * if (toUpdateVctm.size() > 0) { savVctm(toUpdateVctm, "U"); }
		 * 
		 * }
		 */

		if (table2Data != null) {
			savVctm(table2Data);
		}

		/*
		 * if (table3Data != null) {
		 * 
		 * // savIndv(table1Data);
		 * 
		 * System.out.println("check inside table3Data");
		 * 
		 * List<Map<String, Object>> toInsertEve = new ArrayList<>(); List<Map<String,
		 * Object>> toUpdateEve = new ArrayList<>();
		 * 
		 * for (Map<String, Object> datum : table3Data) { String eveSeq = (String)
		 * datum.get("EVIDENCE_SEQ_NUM").toString();
		 * 
		 * System.out.println("eveSeq from initial : " + eveSeq);
		 * 
		 * if (eveSeq == null || eveSeq.isEmpty()) { toInsertEve.add(datum); // No
		 * row-id means it's a new entry } else { toUpdateEve.add(datum); // Existing
		 * entry } }
		 * 
		 * System.out.println("toInsertEve size : " + toInsertEve.size());
		 * System.out.println("toUpdateIndv size : " + toUpdateEve.size());
		 * 
		 * if (toInsertEve.size() > 0) { savEve(toInsertEve, "I"); }
		 * 
		 * if (toUpdateEve.size() > 0) { savEve(toUpdateEve, "U"); }
		 * 
		 * }
		 */

		if (table3Data != null) {
			savEve(table3Data);
		}

		if (table4Data != null) {
			savApproval(table4Data);
		}

		/*
		 * if (typForce != null) { savTypForce(typForce); }
		 * 
		 * if (typRestraint != null) { savTypRestraint(typRestraint); }
		 * 
		 * if (actionTaken != null) { savActionTaken(actionTaken); }
		 */

		if (table5Data != null) {
			savSupData(table5Data);
		}

		if (genInfo != null) {
			savGenInfo(genInfo, table0Data.get(0).get("INCIDENT_SEQ_NUM").toString(),
					table0Data.get(0).get("INST_NUM").toString());
		}

		if (assignInvestigatorData != null) {
			savAssignInvestigator(assignInvestigatorData, otherInvestigatorsData);
		}

		if (asnOfc != null) {
			savAssignMoreReviewers(asnOfc, table0Data.get(0).get("INCIDENT_SEQ_NUM").toString(),
					table0Data.get(0).get("INST_NUM").toString());
		}

		/*
		 * if (assignMoreReviewersData != null && !assignMoreReviewersData.isEmpty() &&
		 * table0Data != null && !table0Data.isEmpty()) { String incidentNo =
		 * table0Data.get(0).get("INCIDENT_SEQ_NUM").toString(); String instNum =
		 * table0Data.get(0).get("INST_NUM").toString();
		 * savAssignMoreReviewers(assignMoreReviewersData, incidentNo, instNum); }
		 */

	}

	public void savTypForce(List<Map<String, Object>> data) {

		// Struct[] structs = new Struct[data.size()];
		for (int i = 0; i < data.size(); i++) {

			String exist_sql = "select * from inci_invd_force_tp where incident_seq_num = '"
					+ data.get(i).get("INCIDENT_SEQ_NUM") + "' ";
			exist_sql += "and inst_num = '" + data.get(i).get("INST_NUM") + "' ";
			exist_sql += "and indv_seq_num = '" + data.get(i).get("INDV_SEQ_NUM") + "' ";
			exist_sql += "and type_of_force = '" + data.get(i).get("TYPE_OF_FORCE") + "'";

			List<Map<String, Object>> incident_exist = jdbcTemplate.queryForList(exist_sql);

			System.out.println("print sql : " + exist_sql);

			String procedureCall = "";

			/*
			 * if (incident_exist.size() == 0) { System.out.println("for insert typForce");
			 * procedureCall =
			 * "{call spkg_omnet_wrapper.sp_dis_inci_invd_force_tp_insert(?)}"; } else {
			 * System.out.println("for update typForce"); procedureCall =
			 * "{call spkg_omnet_wrapper.sp_dis_inci_invd_force_tp_update(?)}"; }
			 */

			procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_invd_force_tp_update(?)}";

			String ty_typ = "";
			String tb_typ = "";

			ty_typ = "TY_DIS_INCI_INVD_FORCE_TP";
			tb_typ = "TB_DIS_INCI_INVD_FORCE_TP";

			exeCheckBox(procedureCall, ty_typ, tb_typ, data.get(i).get("CB_SELECT").toString(),
					data.get(i).get("INCIDENT_SEQ_NUM").toString(), data.get(i).get("INST_NUM").toString(),
					data.get(i).get("INDV_SEQ_NUM").toString(), data.get(i).get("TYPE_OF_FORCE_DESC").toString(),
					data.get(i).get("TYPE_OF_FORCE").toString());

			/*
			 * try (Connection connection = dataSource.getConnection();
			 * 
			 * CallableStatement callableStatement = connection.prepareCall(procedureCall))
			 * { OracleConnection oracleConnection =
			 * connection.unwrap(OracleConnection.class);
			 * 
			 * String ty_typ = ""; String tb_typ = "";
			 * 
			 * ty_typ = "TY_DIS_INCI_INVD_FORCE_TP"; tb_typ = "TB_DIS_INCI_INVD_FORCE_TP";
			 * 
			 * structs[i] = oracleConnection.createStruct(ty_typ,
			 * 
			 * new Object[] { data.get(i).get("CB_SELECT"),
			 * data.get(i).get("INCIDENT_SEQ_NUM"), data.get(i).get("INST_NUM"),
			 * data.get(i).get("INDV_SEQ_NUM"), data.get(i).get("TYPE_OF_FORCE_DESC"),
			 * data.get(i).get("TYPE_OF_FORCE") });
			 * 
			 * System.out.println("post procedure: " + procedureCall);
			 * 
			 * Array array = oracleConnection.createOracleArray(tb_typ, structs);
			 * callableStatement.setArray(1, array); callableStatement.execute();
			 * 
			 * } catch (SQLException e) { e.printStackTrace(); throw new
			 * RuntimeException("Error executing stored procedure", e); }
			 */
		}

	}

	public void savTypRestraint(List<Map<String, Object>> data) {

		// Struct[] structs = new Struct[data.size()];
		for (int i = 0; i < data.size(); i++) {

			String exist_sql = "select * from inci_invd_restraint_tp where incident_seq_num = '"
					+ data.get(i).get("INCIDENT_SEQ_NUM") + "' ";
			exist_sql += "and inst_num = '" + data.get(i).get("INST_NUM") + "' ";
			exist_sql += "and indv_seq_num = '" + data.get(i).get("INDV_SEQ_NUM") + "' ";
			exist_sql += "and type_of_restraint = '" + data.get(i).get("TYPE_OF_RESTRAINT") + "'";

			List<Map<String, Object>> incident_exist = jdbcTemplate.queryForList(exist_sql);

			System.out.println("print sql : " + exist_sql);

			String procedureCall = "";

			/*
			 * if (incident_exist.size() == 0) {
			 * System.out.println("for insert typRestraint"); procedureCall =
			 * "{call spkg_omnet_wrapper.sp_dis_inci_invd_restraint_tp_insert(?)}"; } else {
			 * System.out.println("for update typRestraint"); procedureCall =
			 * "{call spkg_omnet_wrapper.sp_dis_inci_invd_restraint_tp_update(?)}"; }
			 */

			procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_invd_restraint_tp_update(?)}";

			String ty_typ = "";
			String tb_typ = "";

			ty_typ = "TY_DIS_INCI_INVD_RESTRAINT_TP";
			tb_typ = "TB_DIS_INCI_INVD_RESTRAINT_TP";

			exeCheckBox(procedureCall, ty_typ, tb_typ, data.get(i).get("CB_SELECT").toString(),
					data.get(i).get("INCIDENT_SEQ_NUM").toString(), data.get(i).get("INST_NUM").toString(),
					data.get(i).get("INDV_SEQ_NUM").toString(), data.get(i).get("TYPE_OF_RESTRAINT_DESC").toString(),
					data.get(i).get("TYPE_OF_RESTRAINT").toString());

			/*
			 * try (Connection connection = dataSource.getConnection();
			 * 
			 * CallableStatement callableStatement = connection.prepareCall(procedureCall))
			 * { OracleConnection oracleConnection =
			 * connection.unwrap(OracleConnection.class);
			 * 
			 * String ty_typ = ""; String tb_typ = "";
			 * 
			 * ty_typ = "TY_DIS_INCI_INVD_RESTRAINT_TP"; tb_typ =
			 * "TB_DIS_INCI_INVD_RESTRAINT_TP";
			 * 
			 * structs[i] = oracleConnection.createStruct(ty_typ,
			 * 
			 * new Object[] { data.get(i).get("CB_SELECT"),
			 * data.get(i).get("INCIDENT_SEQ_NUM"), data.get(i).get("INST_NUM"),
			 * data.get(i).get("INDV_SEQ_NUM"), data.get(i).get("TYPE_OF_RESTRAINT_DESC"),
			 * data.get(i).get("TYPE_OF_RESTRAINT") });
			 * 
			 * System.out.println("post procedure: " + procedureCall);
			 * 
			 * Array array = oracleConnection.createOracleArray(tb_typ, structs);
			 * callableStatement.setArray(1, array); callableStatement.execute();
			 * 
			 * } catch (SQLException e) { e.printStackTrace(); throw new
			 * RuntimeException("Error executing stored procedure", e); }
			 */
		}

	}

	public void savActionTaken(List<Map<String, Object>> data) {

		// Struct[] structs = new Struct[data.size()];
		for (int i = 0; i < data.size(); i++) {

			String exist_sql = "select * from inci_invd_action_taken where incident_seq_num = '"
					+ data.get(i).get("INCIDENT_SEQ_NUM") + "' ";
			exist_sql += "and inst_num = '" + data.get(i).get("INST_NUM") + "' ";
			exist_sql += "and indv_seq_num = '" + data.get(i).get("INDV_SEQ_NUM") + "' ";
			exist_sql += "and action_taken = '" + data.get(i).get("ACTION_TAKEN") + "'";

			List<Map<String, Object>> incident_exist = jdbcTemplate.queryForList(exist_sql);

			System.out.println("print sql : " + exist_sql);

			String procedureCall = "";

			/*
			 * if (incident_exist.size() == 0) {
			 * System.out.println("for insert actionTaken"); procedureCall =
			 * "{call spkg_omnet_wrapper.sp_dis_inci_action_taken_insert(?)}"; } else {
			 * System.out.println("for update actionTaken"); procedureCall =
			 * "{call spkg_omnet_wrapper.sp_dis_inci_action_taken_update(?)}"; }
			 */

			procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_action_taken_update(?)}";

			String ty_typ = "";
			String tb_typ = "";

			ty_typ = "TY_DIS_INCI_INVD_ACTION_TAKEN";
			tb_typ = "TB_DIS_INCI_INVD_ACTION_TAKEN";

			exeCheckBox(procedureCall, ty_typ, tb_typ, data.get(i).get("CB_SELECT").toString(),
					data.get(i).get("INCIDENT_SEQ_NUM").toString(), data.get(i).get("INST_NUM").toString(),
					data.get(i).get("INDV_SEQ_NUM").toString(), data.get(i).get("ACTION_TAKEN_DESC").toString(),
					data.get(i).get("ACTION_TAKEN").toString());

			/*
			 * try (Connection connection = dataSource.getConnection();
			 * 
			 * CallableStatement callableStatement = connection.prepareCall(procedureCall))
			 * { OracleConnection oracleConnection =
			 * connection.unwrap(OracleConnection.class);
			 * 
			 * String ty_typ = ""; String tb_typ = "";
			 * 
			 * ty_typ = "TY_DIS_INCI_INVD_ACTION_TAKEN"; tb_typ =
			 * "TB_DIS_INCI_INVD_ACTION_TAKEN";
			 * 
			 * structs[i] = oracleConnection.createStruct(ty_typ,
			 * 
			 * new Object[] { data.get(i).get("CB_SELECT"),
			 * data.get(i).get("INCIDENT_SEQ_NUM"), data.get(i).get("INST_NUM"),
			 * data.get(i).get("INDV_SEQ_NUM"), data.get(i).get("ACTION_TAKEN_DESC"),
			 * data.get(i).get("ACTION_TAKEN") });
			 * 
			 * System.out.println("post procedure: " + procedureCall);
			 * 
			 * System.out.println(data.get(i).get("CB_SELECT"));
			 * 
			 * Array array = oracleConnection.createOracleArray(tb_typ, structs);
			 * callableStatement.setArray(1, array); callableStatement.execute();
			 * 
			 * } catch (SQLException e) { e.printStackTrace(); throw new
			 * RuntimeException("Error executing stored procedure", e); }
			 */
		}

	}

	public void savSupData(List<Map<String, Object>> data) {

		// Struct[] structs = new Struct[data.size()];
		for (int i = 0; i < data.size(); i++) {

			String exist_sql = "select * from invg_incident_chklist_dtl where incident_seq_num = '"
					+ data.get(i).get("INCIDENT_SEQ_NUM") + "' ";
			exist_sql += "and QUES_CODE = '" + data.get(i).get("ques_code") + "' ";

			List<Map<String, Object>> sup_exist = jdbcTemplate.queryForList(exist_sql);

			System.out.println("print sql : " + exist_sql);

			String procedureCall = "";

			if (sup_exist.size() == 0) {
				System.out.println("for insert supdata");
				procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_additional_dtl_insert(?,?,?)}";
			} else {
				System.out.println("for update supdata");
				procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_additional_dtl_insert(?,?,?)}";
			}

			String ty_typ = "";
			String tb_typ = "";

			ty_typ = "TY_DIS_INCI_ADDITIONAL_DTL";
			tb_typ = "TB_DIS_INCI_ADDITIONAL_DTL";

			exeSupData(procedureCall, ty_typ, tb_typ, data.get(i).get("cb_completed").toString(),
					data.get(i).get("INCIDENT_SEQ_NUM").toString(), data.get(i).get("INST_NUM").toString(),
					data.get(i).get("ques_code").toString(), data.get(i).get("ques_desc").toString(),
					data.get(i).get("completed_by").toString(), data.get(i).get("chklist_date").toString(),
					data.get(i).get("comments").toString());

			/*
			 * try (Connection connection = dataSource.getConnection();
			 * 
			 * CallableStatement callableStatement = connection.prepareCall(procedureCall))
			 * { OracleConnection oracleConnection =
			 * connection.unwrap(OracleConnection.class);
			 * 
			 * String ty_typ = ""; String tb_typ = "";
			 * 
			 * ty_typ = "TY_DIS_INCI_INVD_ACTION_TAKEN"; tb_typ =
			 * "TB_DIS_INCI_INVD_ACTION_TAKEN";
			 * 
			 * structs[i] = oracleConnection.createStruct(ty_typ,
			 * 
			 * new Object[] { data.get(i).get("CB_SELECT"),
			 * data.get(i).get("INCIDENT_SEQ_NUM"), data.get(i).get("INST_NUM"),
			 * data.get(i).get("INDV_SEQ_NUM"), data.get(i).get("ACTION_TAKEN_DESC"),
			 * data.get(i).get("ACTION_TAKEN") });
			 * 
			 * System.out.println("post procedure: " + procedureCall);
			 * 
			 * System.out.println(data.get(i).get("CB_SELECT"));
			 * 
			 * Array array = oracleConnection.createOracleArray(tb_typ, structs);
			 * callableStatement.setArray(1, array); callableStatement.execute();
			 * 
			 * } catch (SQLException e) { e.printStackTrace(); throw new
			 * RuntimeException("Error executing stored procedure", e); }
			 */
		}

	}
	
	private void processSubData(List<Map<String, Object>> dataList, String procedure, String ty_typ, String tb_typ) {
	    if (dataList.isEmpty()) {
	        System.out.println("No data to process for this sub-type.");
	        return;
	    }
	    
	    for (Map<String, Object> item : dataList) {
	        String fieldData = (String) item.get("fieldData");
	        String incidentSeqNum = (String) item.get("incidentSeqNum");
	        String instNum = (String) item.get("instNum");
	        String indvSeqNum = (String) item.get("indvSeqNum");
	        
	        // Call splitProcessor with collected params
	        splitProcessor(procedure, ty_typ, tb_typ, fieldData, incidentSeqNum, instNum, indvSeqNum);
	    }
	}

	public void splitProcessor(String proCall, String ty_typ, String tb_typ, String inpVal, String inciNo, String instNo, String invNo) {

		String[] entries = inpVal.split(";;");

		System.out.println("Number of entries: " + entries.length);

		// Step 2: For each entry, split by ":" and print
		for (int i = 0; i < entries.length; i++) {
			String[] fields = entries[i].split(":");
			if (fields.length == 4) { // Validate expected 4 fields
				
				if(fields[0].equals("Y")) {
					exeCheckBox(proCall, ty_typ, tb_typ, fields[1], inciNo, instNo, invNo, fields[2], fields[3]);
				}
			} else {
				System.out.println(
						"Warning: Entry " + (i + 1) + " has " + fields.length + " fields (expected 4): " + entries[i]);
			}
		}

	}

	public void exeCheckBox(String proCall, String ty_typ, String tb_typ, String CB_Sel, String inciNo, String instNo,
			String invNo, String descCont, String code) {

		Struct[] structs = new Struct[1];

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(proCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println("Well execute : " + CB_Sel + " for " + descCont + " & " + code);

			structs[0] = oracleConnection.createStruct(ty_typ,

					new Object[] { CB_Sel, inciNo, instNo, invNo, descCont, code });

			System.out.println("post procedure: " + proCall);

			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	public void exeSupData(String proCall, String ty_typ, String tb_typ, String cb_completed, String inciNo,
			String instNo, String ques_code, String ques_desc, String completedBy, String chk_dt, String cmt) {

		Struct[] structs = new Struct[1];

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(proCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println(cb_completed + " for " + ques_desc + " & " + ques_code);

			structs[0] = oracleConnection.createStruct(ty_typ,

					new Object[] { cb_completed, inciNo, ques_code, ques_desc, completedBy, chk_dt, cmt });

			System.out.println("post procedure: " + proCall);

			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);
			callableStatement.setString(2, inciNo);
			callableStatement.setString(3, instNo);
			callableStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	public void exeCheckGen(String proCall, String ty_typ, String tb_typ, String instNo, String inciNo, String inciCode,
			String inciName, String checkInd, String preaFlag) {

		Struct[] structs = new Struct[1];

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(proCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println(checkInd + " for " + inciName + " & " + inciCode);

			/*
			 * if(preaFlag.equals("")) {
			 * 
			 * } else {
			 * 
			 * }
			 */

			structs[0] = oracleConnection.createStruct(ty_typ,

					new Object[] { instNo, inciNo, inciCode, inciName, checkInd, null });

			System.out.println("post procedure: " + proCall);

			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	public void savGenInfo(List<Map<String, Object>> data, String inciNo, String instNo) {

		// Struct[] structs = new Struct[data.size()];
		for (int i = 0; i < data.size(); i++) {

			/*
			 * String exist_sql =
			 * "select * from INCIDENT_CODE_DETAIL where incident_seq_num = '" +
			 * data.get(i).get("INCIDENT_SEQ_NUM") + "' "; exist_sql += "and inst_num = '" +
			 * data.get(i).get("INST_NUM") + "' "; exist_sql += "and incident_code = '" +
			 * data.get(i).get("INCIDENT_CODE") + "'";
			 * 
			 * List<Map<String, Object>> incident_exist =
			 * jdbcTemplate.queryForList(exist_sql);
			 * 
			 * System.out.println("print sql : " + exist_sql);
			 */

			String procedureCall = "";

			/*
			 * if (incident_exist.size() == 0) { System.out.println("for insert genInfo");
			 * procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_insert(?)}"; } else
			 * { System.out.println("for update genInfo"); procedureCall =
			 * "{call spkg_omnet_wrapper.sp_dis_incident_update(?)}"; }
			 */

			procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update(?)}";

			String ty_typ = "";
			String tb_typ = "";

			ty_typ = "TY_DIS_INCIDENT_CODE_REC";
			tb_typ = "TB_DIS_INCIDENT_CODE_REC";

			String preaFlag = data.get(i).get("PREA_FLAG") != null ? data.get(i).get("PREA_FLAG").toString() : "";

			exeCheckGen(procedureCall, ty_typ, tb_typ, instNo, inciNo, data.get(i).get("INCIDENT_CODE").toString(),
					data.get(i).get("INCIDENT_NAME").toString(), data.get(i).get("CHECK_BOX_IND").toString(), preaFlag);

			/*
			 * try (Connection connection = dataSource.getConnection();
			 * 
			 * CallableStatement callableStatement = connection.prepareCall(procedureCall))
			 * { OracleConnection oracleConnection =
			 * connection.unwrap(OracleConnection.class);
			 * 
			 * String ty_typ = ""; String tb_typ = "";
			 * 
			 * ty_typ = "TY_DIS_INCIDENT_CODE_REC"; tb_typ = "TB_DIS_INCIDENT_CODE_REC";
			 * 
			 * structs[i] = oracleConnection.createStruct(ty_typ,
			 * 
			 * new Object[] { data.get(i).get("INST_NUM"),
			 * data.get(i).get("INCIDENT_SEQ_NUM"), data.get(i).get("INCIDENT_CODE"),
			 * data.get(i).get("INCIDENT_NAME"), data.get(i).get("CHECK_BOX_IND"),
			 * data.get(i).get("PREA_FLAG") });
			 * 
			 * System.out.println("post procedure: " + procedureCall);
			 * 
			 * Array array = oracleConnection.createOracleArray(tb_typ, structs);
			 * callableStatement.setArray(1, array); callableStatement.execute();
			 * 
			 * } catch (SQLException e) { e.printStackTrace(); throw new
			 * RuntimeException("Error executing stored procedure", e); }
			 */
		}

	}

	public void savBasicIncident(List<Map<String, Object>> data) {
		System.out.println("Data: " + data);

		String exist_sql = "SELECT * FROM INCIDENT WHERE INCIDENT_SEQ_NUM = '" + data.get(0).get("INCIDENT_SEQ_NUM")
				+ "'";

		List<Map<String, Object>> incident_exist = jdbcTemplate.queryForList(exist_sql);

		System.out.println("print sql : " + exist_sql);

		String procedureCall = "";

		if (incident_exist.size() == 0) {
			procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_insert_1(?)}";
		} else {
			procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update_1(?,?,?)}";
		}

		System.out.println("procedureCall is " + procedureCall);

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println("Data size is " + data.size());

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				structs[i] = oracleConnection.createStruct("TY_DIS_INCI_INSERTUPDATE",
						new Object[] { data.get(i).get("INCIDENT_SEQ_NUM"), data.get(i).get("INST_NUM"),
								EmptyToNull(data.get(i).get("REFERRED_TO_NUM").toString()),
								EmptyToNull(data.get(i).get("USER_ID").toString()),
								EmptyToNull(data.get(i).get("USER_ID_REPORTED_BY").toString()),
								EmptyToNull(data.get(i).get("USER_ID_SHIFT_COMMANDER").toString()),
								EmptyToNull(data.get(i).get("USER_ID_ENTERED_BY").toString()),
								EmptyToNull(data.get(i).get("COUNT_LOC_CODE").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_DATE").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_TIME").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_DESC").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_OUTCOME").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_FOLOW_REQ_FLG").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_COMNT").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_TP").toString()),
								EmptyToNull(data.get(i).get("USER_ID_REFERRED").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_FORCE_USD_TP").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_RESTN_USD").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_ACTN_TAKEN_DESC").toString()),
								EmptyToNull(data.get(i).get("CONFIDENTIAL_FLAG").toString()),
								EmptyToNull(data.get(i).get("STG_FLAG").toString()),
								EmptyToNull(data.get(i).get("PREA_FLAG").toString()),
								EmptyToNull(data.get(i).get("REPORTED_DATE").toString()),
								EmptyToNull(data.get(i).get("SUPERVISOR_DATE").toString()),
								EmptyToNull(data.get(i).get("SUPERVISOR_COMMENTS").toString()),
								EmptyToNull(data.get(i).get("SHIFT_COMMANDER_DATE").toString()),
								EmptyToNull(data.get(i).get("SHIFT_COMMANDER_COMMENTS").toString()),
								EmptyToNull(data.get(i).get("REFERRED_TO_DATE").toString()),
								EmptyToNull(data.get(i).get("REFERRED_TO_COMMENTS").toString()),
								EmptyToNull(data.get(i).get("COMMON_SEQ_NUM").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_DATE_ENTER").toString()),
								EmptyToNull(data.get(i).get("incident_date_review").toString()),
								EmptyToNull(data.get(i).get("incident_comnt_enter").toString()),
								EmptyToNull(data.get(i).get("incident_comnt_reporter").toString()),
								EmptyToNull(data.get(i).get("incident_comnt_reviewr").toString()),
								EmptyToNull(data.get(i).get("TITLE_ENTER").toString()),
								EmptyToNull(data.get(i).get("TITLE_REFER").toString()),
								EmptyToNull(data.get(i).get("TITLE_REVIEW").toString()),
								EmptyToNull(data.get(i).get("title_reported").toString()),
								EmptyToNull(data.get(i).get("incident_phys_force_flg").toString()),
								EmptyToNull(data.get(i).get("incident_chem_force_flg").toString()),
								EmptyToNull(data.get(i).get("incident_stun_force_flg").toString()),
								EmptyToNull(data.get(i).get("incident_no_force_flg").toString()),
								EmptyToNull(data.get(i).get("ROW_ID").toString()),
								EmptyToNull(data.get(i).get("shift_commander_apr_flag").toString()),
								EmptyToNull(data.get(i).get("incident_location").toString()),
								EmptyToNull(data.get(i).get("send_notification_flag").toString()),
								EmptyToNull(data.get(i).get("incident_capstun_force_flg").toString()),
								EmptyToNull(data.get(i).get("report_category").toString()),
								EmptyToNull(data.get(i).get("short_description").toString()),
								EmptyToNull(data.get(i).get("incident_grp_seq_no").toString()),
								EmptyToNull(data.get(i).get("initial_incident_seq_num").toString()),
								EmptyToNull(data.get(i).get("source_type").toString()),
								EmptyToNull(data.get(i).get("reporting_source").toString()),
								EmptyToNull(data.get(i).get("warrant_type").toString()),
								EmptyToNull(data.get(i).get("warrant_number").toString()),
								EmptyToNull(data.get(i).get("offender_states_rape").toString()),
								EmptyToNull(data.get(i).get("sexual_alleg_ref_code").toString()),
								EmptyToNull(data.get(i).get("location_category").toString()),
								EmptyToNull(data.get(i).get("INSERTED_DATE_TIME").toString()),
								EmptyToNull(data.get(i).get("irh_seq_num").toString()) });
				System.out.println("post procedure1");
			}

			System.out.println("structs is " + structs.toString());

			Array array = oracleConnection.createOracleArray("TB_DIS_INCI_INSERTUPDATE", structs);
			callableStatement.setArray(1, array);

			if (incident_exist.size() > 0) {
				callableStatement.setString(2, data.get(0).get("INCIDENT_SEQ_NUM").toString());
				callableStatement.setString(3, data.get(0).get("INST_NUM").toString());
			}

			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}
	}

	public void savApproval(List<Map<String, Object>> data) {

		String procedureCall = "";

		procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_update_staff_rev(?,?)}";

		System.out.println("procedureCall is " + procedureCall);

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println("Data size is " + data.size());

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				structs[i] = oracleConnection.createStruct("TY_DIS_INCI_STAFF_REV",
						new Object[] { EmptyToNull(data.get(i).get("USER_ID").toString()),
								EmptyToNull(data.get(i).get("DATE_REVIEW").toString()),
								EmptyToNull(data.get(i).get("TITLE_REVIEW").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_COMNT_REVIEWR").toString()),
								EmptyToNull(data.get(i).get("SHIFT_COMMANDER_APR_FLAG").toString()),
								EmptyToNull(data.get(i).get("INCIDENT_FOLOW_REQ_FLG").toString()) });
				System.out.println("post procedure5");
			}

			System.out.println("structs is " + structs.toString());

			Array array = oracleConnection.createOracleArray("TB_DIS_INCI_STAFF_REV", structs);
			callableStatement.setArray(1, array);
			callableStatement.setString(2, data.get(0).get("INCIDENT_SEQ_NUM").toString());

			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}
	}

	public void savIndv(List<Map<String, Object>> data, String typ) {

	    String procedureCall = "";

	    System.out.println("typ is " + typ);

	    if ("U".equals(typ)) {
	        procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update_2(?)}";
	    } else {
	        procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_insert_2(?)}";
	    }

	    try (Connection connection = dataSource.getConnection();
	         CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
	        OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

	        String ty_typ = "TY_DIS_INDIVIDUALS_INVOL_INSUPD";
	        String tb_typ = "TB_DIS_INDIVIDUALS_INVOL_INSUPD";

	        Struct[] structs = new Struct[data.size()];
	        
	        List<Map<String, Object>> forceDataList = new ArrayList<>();
	        List<Map<String, Object>> restraintDataList = new ArrayList<>();
	        List<Map<String, Object>> actionDataList = new ArrayList<>();

	        for (int i = 0; i < data.size(); i++) {
	            Map<String, Object> individualData = data.get(i);

	            String INDV_SEQ_NUM = "";
	            if ("U".equals(typ)) {
	                INDV_SEQ_NUM = getString(individualData, "INDV_SEQ_NUM");
	            } else {
	                String seqQuery = "Select Sf_Gen_Seq_Mdoc ('0222') as AUTOSEQ from dual";
	                List<Map<String, Object>> QueryRes = jdbcTemplate.queryForList(seqQuery);
	                INDV_SEQ_NUM = QueryRes.get(0).get("AUTOSEQ").toString();
	                System.out.println("genSeq is " + INDV_SEQ_NUM);
	            }

	            // Safely parse age
	            int age = 0;
	            String ageStr = getString(individualData, "age");
	            if (!ageStr.isEmpty()) {
	                try {
	                    age = Integer.parseInt(ageStr);
	                } catch (NumberFormatException e) {
	                    System.err.println("Could not parse age: " + ageStr);
	                }
	            }

	            structs[i] = oracleConnection.createStruct(ty_typ,
	                new Object[] {
	                    getString(individualData, "INCIDENT_SEQ_NUM"),
	                    INDV_SEQ_NUM,
	                    getString(individualData, "INDIVIDUAL_NUM"),
	                    getString(individualData, "NAME_FIRST"),
	                    getString(individualData, "NAME_LAST"),
	                    getString(individualData, "NAME_MIDDLE"),
	                    getString(individualData, "NAME_SUFFIX"),
	                    getString(individualData, "sex"),
	                    age,
	                    getString(individualData, "race"),
	                    getString(individualData, "COMMIT_NO"),
	                    getString(individualData, "sbi_no"),
	                    getString(individualData, "title_prsn"),
	                    getString(individualData, "indv_del_flg"),
	                    getString(individualData, "inst_desc"),
	                    getString(individualData, "user_id"),
	                    getString(individualData, "inst_num"),
	                    getString(individualData, "individual_category"),
	                    getString(individualData, "ROW_ID")
	                }
	            );
	            
	            String incidentSeqNum = getString(individualData, "INCIDENT_SEQ_NUM");
	            String instNum = getString(individualData, "inst_num");

	            // *** THE PRIMARY FIX IS HERE: Using null-safe "Y".equals(...) ***
	            if ("Y".equals(individualData.get("typForceFlag"))) {
	                System.out.println("Collecting force vals: " + individualData.get("typForceFld"));
	                Map<String, Object> forceData = new HashMap<>();
	                forceData.put("fieldData", getString(individualData, "typForceFld"));
	                forceData.put("incidentSeqNum", incidentSeqNum);
	                forceData.put("instNum", instNum);
	                forceData.put("indvSeqNum", INDV_SEQ_NUM);
	                forceDataList.add(forceData);
	            }
	            
	            if ("Y".equals(individualData.get("typRestraintFlag"))) {
	                System.out.println("Collecting restraint vals: " + individualData.get("typRestraintFld"));
	                Map<String, Object> restraintData = new HashMap<>();
	                restraintData.put("fieldData", getString(individualData, "typRestraintFld"));
	                restraintData.put("incidentSeqNum", incidentSeqNum);
	                restraintData.put("instNum", instNum);
	                restraintData.put("indvSeqNum", INDV_SEQ_NUM);
	                restraintDataList.add(restraintData);
	            }
	            
	            if ("Y".equals(individualData.get("actionTakenFlag"))) {
	                System.out.println("Collecting action vals: " + individualData.get("actionTakenFld"));
	                Map<String, Object> actionData = new HashMap<>();
	                actionData.put("fieldData", getString(individualData, "actionTakenFld"));
	                actionData.put("incidentSeqNum", incidentSeqNum);
	                actionData.put("instNum", instNum);
	                actionData.put("indvSeqNum", INDV_SEQ_NUM);
	                actionDataList.add(actionData);
	            }   

	            System.out.println("post procedure3");
	        }
	        
	        Array array = oracleConnection.createOracleArray(tb_typ, structs);
	        callableStatement.setArray(1, array);
	        callableStatement.execute();
	        
	        // Process sub-data after the main records are saved
	        processSubData(forceDataList, "{call spkg_omnet_wrapper.sp_dis_inci_invd_force_tp_update(?)}", 
	                       "TY_DIS_INCI_INVD_FORCE_TP", "TB_DIS_INCI_INVD_FORCE_TP");

	        processSubData(restraintDataList, "{call spkg_omnet_wrapper.sp_dis_inci_invd_restraint_tp_update(?)}", 
	                       "TY_DIS_INCI_INVD_RESTRAINT_TP", "TB_DIS_INCI_INVD_RESTRAINT_TP");

	        processSubData(actionDataList, "{call spkg_omnet_wrapper.sp_dis_inci_action_taken_update(?)}", 
	                       "TY_DIS_INCI_INVD_ACTION_TAKEN", "TB_DIS_INCI_INVD_ACTION_TAKEN");

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure", e);
	    }
	}
	
	private String getString(Map<String, Object> map, String key) {
	    Object value = map.get(key);
	    return value == null ? "" : value.toString();
	}

	public void savVctm(List<Map<String, Object>> data) {
	    for (int i = 0; i < data.size(); i++) {
	        Map<String, Object> victimData = data.get(i);
	        String vctmSeq = getString(victimData, "VICTIM_SEQ_NUM");
	        System.out.println("vctmSeq from initial : " + vctmSeq);
	        String procedureCall = "";
	        String storeType = "";
	        if (vctmSeq.isEmpty()) {
	            System.out.println("for insert victim");
	            procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_Insert_Victim(?)}";
	            String seqQuery = "Select Sf_Gen_Seq_Mdoc ('0218') as AUTOSEQ from dual";
	            List<Map<String, Object>> QueryRes = jdbcTemplate.queryForList(seqQuery);
	            vctmSeq = QueryRes.get(0).get("AUTOSEQ").toString();
	            System.out.println("genSeq is " + vctmSeq);
	            storeType = "I";
	        } else {
	            System.out.println("for update victim");
	            procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_Update_victim(?,?,?,?)}";
	            storeType = "U";
	        }
	        String ty_typ = "TY_DIS_INCIDENT_VICTIM_REC";
	        String tb_typ = "TB_DIS_INCIDENT_VICTIM_REC";
	        exeVctmData(procedureCall, ty_typ, tb_typ, vctmSeq,
	                EmptyToNull(getString(victimData, "VCTM_NAME_FIRST")),
	                EmptyToNull(getString(victimData, "VCTM_NAME_MIDDLE")),
	                EmptyToNull(getString(victimData, "VCTM_NAME_LAST")),
	                EmptyToNull(getString(victimData, "VCTM_NAME_SUFFIX")),
	                EmptyToNull(getString(victimData, "INCD_VCTM_INJR_DESC")),
	                EmptyToNull(getString(victimData, "INCD_VCTM_HSP_FLG")),
	                EmptyToNull(getString(victimData, "INCD_VCTM_HSP_LOCN")),
	                EmptyToNull(getString(victimData, "inst_num")),
	                EmptyToNull(getString(victimData, "INCIDENT_SEQ_NUM")),
	                EmptyToNull(getString(victimData, "USER")),
	                EmptyToNull(getString(victimData, "DATE_TIME")),
	                EmptyToNull(getString(victimData, "COUNT_LOC_CODE")),
	                EmptyToNull(getString(victimData, "NATURE_OF_INJURY")),
	                EmptyToNull(getString(victimData, "MED_TREATMENT_PROVIDED")), 
	                storeType
	        );
	    }
	}
	
	public void exeVctmData(String proCall, String ty_typ, String tb_typ, String vctmSeq, String vctmFName,
			String vctmMName, String vctmLName, String vctmSName, String injDesc, String hspFlag, String hspLocn,
			String instNum, String inciNum, String user, String dttm, String locCode, String natureInj,
			String medTreatPro, String storeType) {

		Struct[] structs = new Struct[1];

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(proCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println(vctmLName + " for " + natureInj + " :for new");

			structs[0] = oracleConnection.createStruct(ty_typ,

					new Object[] { vctmSeq, vctmFName, vctmMName, vctmLName, vctmSName, injDesc, hspFlag, hspLocn,
							instNum, inciNum, user, dttm, user, dttm, 0, "", user, locCode, dttm, "", "N", natureInj,
							medTreatPro, 0 });

			System.out.println("post procedure: " + proCall);

			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);

			if (storeType.equals("U")) {
				callableStatement.setString(2, inciNum);
				callableStatement.setString(3, instNum);
				callableStatement.setString(4, vctmSeq);
			}

			callableStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	public void savEve(List<Map<String, Object>> data) {

		for (int i = 0; i < data.size(); i++) {

			String eveSeq = (String) data.get(i).get("EVIDENCE_SEQ_NUM").toString();

			System.out.println("eveSeq from initial : " + eveSeq);

			String procedureCall = "";
			String storeType = "";

			if (eveSeq == null || eveSeq.isEmpty()) {
				System.out.println("for insert evidence");
				procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_Insert_Evidence(?)}";

				String seqQuery = "Select Sf_Gen_Seq_Mdoc ('0222') as AUTOSEQ from dual";
				List<Map<String, Object>> QueryRes = jdbcTemplate.queryForList(seqQuery);
				eveSeq = QueryRes.get(0).get("AUTOSEQ").toString();
				System.out.println("genSeq is " + eveSeq);
				storeType = "I";
			} else {
				System.out.println("for update evidence");
				procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_Update_Evidence(?,?,?,?)}";
				storeType = "U";
			}

			String ty_typ = "";
			String tb_typ = "";

			ty_typ = "TY_DIS_INCIDENT_EVIDENCE_REC";
			tb_typ = "TB_DIS_INCIDENT_EVIDENCE_REC";

			exeEveData(procedureCall, ty_typ, tb_typ, eveSeq,
					EmptyToNull(data.get(i).get("INCIDENT_SEQ_NUM").toString()),
					EmptyToNull(data.get(i).get("COL_DATE").toString()),
					EmptyToNull(data.get(i).get("INCD_EVDN_TP").toString()),
					EmptyToNull(data.get(i).get("COL_NAME_FIRST").toString()),
					EmptyToNull(data.get(i).get("COL_NAME_LAST").toString()),
					EmptyToNull(data.get(i).get("COL_NAME_MIDDLE").toString()),
					EmptyToNull(data.get(i).get("COL_NAME_SUFFIX").toString()),
					EmptyToNull(data.get(i).get("SEC_NAME_FIRST").toString()),
					EmptyToNull(data.get(i).get("SEC_NAME_LAST").toString()),
					EmptyToNull(data.get(i).get("SEC_NAME_MIDDLE").toString()),
					EmptyToNull(data.get(i).get("SEC_NAME_SUFFIX").toString()),
					EmptyToNull(data.get(i).get("inst_num").toString()),
					EmptyToNull(data.get(i).get("USER").toString()),
					EmptyToNull(data.get(i).get("DATE_TIME").toString()),
					EmptyToNull(data.get(i).get("EVIDENCE_CODE").toString()), storeType);

		}

	}

	public void exeEveData(String proCall, String ty_typ, String tb_typ, String eveSeq, String inciNum, String colDt,
			String incd_evdn_tp, String colFName, String colLName, String colMName, String colSName, String secFName,
			String secLName, String secMName, String secSName, String instNum, String user, String dttm,
			String evidenceCode, String storeType) {

		Struct[] structs = new Struct[1];

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(proCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println(colLName + " for " + evidenceCode + " :for new evidence");

			structs[0] = oracleConnection.createStruct(ty_typ,

					new Object[] { inciNum, eveSeq, colDt, incd_evdn_tp, colFName, colLName, colMName, colSName,
							secFName, secMName, secLName, secSName, instNum, user, dttm, user, dttm, 0, "",
							evidenceCode });

			System.out.println("post procedure: " + proCall);

			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);

			if (storeType.equals("U")) {
				callableStatement.setString(2, inciNum);
				callableStatement.setString(3, instNum);
				callableStatement.setString(4, eveSeq);
			}

			callableStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	/*
	 * public void savEve(List<Map<String, Object>> data, String typ) {
	 * 
	 * String procedureCall = "";
	 * 
	 * System.out.println("typ is " + typ);
	 * 
	 * // procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update_2(?)}";
	 * if (typ.equals("U")) { procedureCall =
	 * "{call spkg_omnet_wrapper.sp_dis_incident_Update_Evidence(?,?,?,?)}"; } else
	 * { procedureCall =
	 * "{call spkg_omnet_wrapper.sp_dis_incident_Insert_Evidence(?)}"; }
	 * 
	 * try (Connection connection = dataSource.getConnection();
	 * 
	 * CallableStatement callableStatement = connection.prepareCall(procedureCall))
	 * { OracleConnection oracleConnection =
	 * connection.unwrap(OracleConnection.class);
	 * 
	 * String ty_typ = ""; String tb_typ = "";
	 * 
	 * Struct[] structs = new Struct[data.size()]; for (int i = 0; i < data.size();
	 * i++) {
	 * 
	 * ty_typ = "TY_DIS_INCIDENT_EVIDENCE_REC"; tb_typ =
	 * "TB_DIS_INCIDENT_EVIDENCE_REC";
	 * 
	 * String EVE_SEQ_NUM = "";
	 * 
	 * // procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update_2(?)}";
	 * if (typ.equals("U")) { EVE_SEQ_NUM =
	 * data.get(i).get("EVIDENCE_SEQ_NUM").toString(); } else { String seqQuery =
	 * "Select Sf_Gen_Seq_Mdoc ('0222') as AUTOSEQ from dual"; List<Map<String,
	 * Object>> QueryRes = jdbcTemplate.queryForList(seqQuery); EVE_SEQ_NUM =
	 * QueryRes.get(0).get("AUTOSEQ").toString(); System.out.println("genSeq is " +
	 * EVE_SEQ_NUM); }
	 * 
	 * structs[i] = oracleConnection.createStruct(ty_typ,
	 * 
	 * new Object[] { data.get(i).get("INCIDENT_SEQ_NUM"), EVE_SEQ_NUM,
	 * data.get(i).get("COL_DATE"), data.get(i).get("INCD_EVDN_TP"),
	 * data.get(i).get("COL_NAME_FIRST"), data.get(i).get("COL_NAME_LAST"),
	 * data.get(i).get("COL_NAME_MIDDLE"), data.get(i).get("COL_NAME_SUFFIX"),
	 * data.get(i).get("SEC_NAME_FIRST"), data.get(i).get("SEC_NAME_MIDDLE"),
	 * data.get(i).get("SEC_NAME_LAST"), data.get(i).get("SEC_NAME_SUFFIX"),
	 * data.get(i).get("inst_num"), data.get(i).get("USER"),
	 * data.get(i).get("DATE_TIME"), data.get(i).get("USER"),
	 * data.get(i).get("DATE_TIME"), 0, "", data.get(i).get("EVIDENCE_CODE") });
	 * 
	 * System.out.println("post procedure3");
	 * 
	 * Array array = oracleConnection.createOracleArray(tb_typ, structs);
	 * callableStatement.setArray(1, array);
	 * 
	 * if (typ.equals("U")) { callableStatement.setString(2,
	 * data.get(i).get("INCIDENT_SEQ_NUM").toString());
	 * callableStatement.setString(3, data.get(i).get("inst_num").toString());
	 * callableStatement.setString(4, EVE_SEQ_NUM); }
	 * 
	 * callableStatement.execute(); } } catch (SQLException e) {
	 * e.printStackTrace(); throw new
	 * RuntimeException("Error executing stored procedure", e); }
	 * 
	 * }
	 */

	/*
	 * public void savVctm(List<Map<String, Object>> data, String typ) {
	 * 
	 * String procedureCall = "";
	 * 
	 * System.out.println("typ is " + typ);
	 * 
	 * // procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update_2(?)}";
	 * if (typ.equals("U")) { procedureCall =
	 * "{call spkg_omnet_wrapper.sp_dis_incident_Update_victim(?,?,?,?)}"; } else {
	 * procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_Insert_Victim(?)}";
	 * }
	 * 
	 * try (Connection connection = dataSource.getConnection();
	 * 
	 * CallableStatement callableStatement = connection.prepareCall(procedureCall))
	 * { OracleConnection oracleConnection =
	 * connection.unwrap(OracleConnection.class);
	 * 
	 * String ty_typ = ""; String tb_typ = "";
	 * 
	 * Struct[] structs = new Struct[data.size()]; for (int i = 0; i < data.size();
	 * i++) {
	 * 
	 * ty_typ = "TY_DIS_INCIDENT_VICTIM_REC"; tb_typ = "TB_DIS_INCIDENT_VICTIM_REC";
	 * 
	 * String VICTIM_SEQ_NUM = "";
	 * 
	 * // procedureCall = "{call spkg_omnet_wrapper.sp_dis_incident_update_2(?)}";
	 * if (typ.equals("U")) { VICTIM_SEQ_NUM =
	 * data.get(i).get("VICTIM_SEQ_NUM").toString(); } else { String seqQuery =
	 * "Select Sf_Gen_Seq_Mdoc ('0218') as AUTOSEQ from dual"; List<Map<String,
	 * Object>> QueryRes = jdbcTemplate.queryForList(seqQuery); VICTIM_SEQ_NUM =
	 * QueryRes.get(0).get("AUTOSEQ").toString(); System.out.println("genSeq is " +
	 * VICTIM_SEQ_NUM); }
	 * 
	 * structs[i] = oracleConnection.createStruct(ty_typ,
	 * 
	 * new Object[] { VICTIM_SEQ_NUM, data.get(i).get("VCTM_NAME_FIRST"),
	 * data.get(i).get("VCTM_NAME_MIDDLE"), data.get(i).get("VCTM_NAME_LAST"),
	 * data.get(i).get("VCTM_NAME_SUFFIX"), data.get(i).get("INCD_VCTM_INJR_DESC"),
	 * data.get(i).get("INCD_VCTM_HSP_FLG"), data.get(i).get("INCD_VCTM_HSP_LOCN"),
	 * data.get(i).get("inst_num"), data.get(i).get("INCIDENT_SEQ_NUM"),
	 * data.get(i).get("USER"), data.get(i).get("DATE_TIME"),
	 * data.get(i).get("USER"), data.get(i).get("DATE_TIME"), 0, "",
	 * data.get(i).get("USER"), data.get(i).get("COUNT_LOC_CODE"),
	 * data.get(i).get("DATE_TIME"), "", "N", data.get(i).get("NATURE_OF_INJURY"),
	 * data.get(i).get("MED_TREATMENT_PROVIDED"), 0 });
	 * 
	 * System.out.println("post procedure2");
	 * 
	 * Array array = oracleConnection.createOracleArray(tb_typ, structs);
	 * callableStatement.setArray(1, array);
	 * 
	 * if (typ.equals("U")) { callableStatement.setString(2,
	 * data.get(i).get("INCIDENT_SEQ_NUM").toString());
	 * callableStatement.setString(3, data.get(i).get("inst_num").toString());
	 * callableStatement.setString(4, VICTIM_SEQ_NUM); }
	 * 
	 * callableStatement.execute(); } } catch (SQLException e) {
	 * e.printStackTrace(); throw new
	 * RuntimeException("Error executing stored procedure", e); }
	 * 
	 * }
	 */

	private String EmptyToNull(String value) {
		return (value == null || value.trim().isEmpty()) ? null : value;
	}

	public void savAssignInvestigator(List<Map<String, Object>> assignData, List<Map<String, Object>> otherInvData) {
		if (assignData == null || assignData.isEmpty()) {
			return;
		}
		Map<String, Object> headerData = assignData.get(0);
		String invgSeqNum = EmptyToNull((String) headerData.get("invg_seq_num"));
		String incidentNo = (String) headerData.get("incident_seq_num");
		String instNum = (String) headerData.get("inst_num");
		Object grpSeqNoObj = headerData.get("incident_grp_seq_no");
		String procedureCall;
		boolean isInsert = (invgSeqNum == null);
		final String finalInvgSeqNum;
		if (isInsert) {
			String seqQuery = "Select Sf_Gen_Seq_Mdoc ('0453') as AUTOSEQ from dual";
			List<Map<String, Object>> queryRes = jdbcTemplate.queryForList(seqQuery);
			finalInvgSeqNum = queryRes.get(0).get("AUTOSEQ").toString();
			headerData.put("invg_seq_num", finalInvgSeqNum);
			procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_assign_investigator_insert(?,?,?,?)}";
		} else {
			finalInvgSeqNum = invgSeqNum;
			procedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_assign_investigator_update(?,?,?,?)}";
		}
		try (Connection connection = dataSource.getConnection();
				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			Struct[] structs = new Struct[1];
			structs[0] = oracleConnection.createStruct("TY_DIS_INCI_ASSIGN_INVESTIGATOR",
					new Object[] { headerData.get("user_id_invstgr_assigned"), headerData.get("invstgr_assigned_date"),
							headerData.get("invstgr_assigned_time"), headerData.get("invstgr_assigned_comment"),
							headerData.get("invg_seq_num"), headerData.get("sexual_alleg_ref_code") });
			Array array = oracleConnection.createOracleArray("TB_DIS_INCI_ASSIGN_INVESTIGATOR", structs);
			callableStatement.setArray(1, array);
			callableStatement.setString(2, incidentNo);
			callableStatement.setString(3, instNum);
			if (grpSeqNoObj != null && !grpSeqNoObj.toString().trim().isEmpty()) {
				callableStatement.setInt(4, Integer.parseInt(grpSeqNoObj.toString()));
			} else {
				callableStatement.setNull(4, java.sql.Types.INTEGER);
			}
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error saving investigator header", e);
		}
		if (otherInvData != null && !otherInvData.isEmpty()) {
			String forwardedByUser = (String) otherInvData.get(0).get("forwardedByUser");
			String detailProcedureCall = "{call spkg_omnet_wrapper.sp_dis_inci_invg_forward_to_insert(?,?,?,?)}";
			try (Connection connection = dataSource.getConnection();
					CallableStatement callableStatement = connection.prepareCall(detailProcedureCall)) {
				OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
				Struct[] detailStructs = new Struct[otherInvData.size()];
				for (int i = 0; i < otherInvData.size(); i++) {
					Map<String, Object> detail = otherInvData.get(i);
					detailStructs[i] = oracleConnection.createStruct("TY_DIS_INCI_INVG_FORWARD_TO",
							new Object[] { detail.get("assignedTo"), detail.get("user_id"), detail.get("date"),
									detail.get("time"), detail.get("reasonForForward"), finalInvgSeqNum, null });
				}
				Array detailArray = oracleConnection.createOracleArray("TB_DIS_INCI_INVG_FORWARD_TO", detailStructs);
				callableStatement.setArray(1, detailArray);
				callableStatement.setString(2, finalInvgSeqNum);
				callableStatement.setString(3, instNum);
				callableStatement.setString(4, forwardedByUser);
				callableStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Error saving other investigators", e);
			}
		}
	}

	@GetMapping("/fetchAssignInvestigator")
	public List<Map<String, Object>> fetchAssignInvestigator(@RequestParam String incidentNo,
			@RequestParam String instNum, @RequestParam(required = false) String grpSeqNo) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		Integer groupSeqNoInt = null;
		if (grpSeqNo != null && !grpSeqNo.trim().isEmpty()) {
			try {
				groupSeqNoInt = Integer.parseInt(grpSeqNo);
			} catch (NumberFormatException e) {
				System.err.println("Invalid group sequence number format: " + grpSeqNo);
			}
		}
		String sql = "{call Spkg_Dis_Inci.sp_assign_investigator_qry(?,?,?,?)}";
		try (Connection conn = dataSource.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
			stmt.setString(2, incidentNo);
			stmt.setString(3, instNum);
			if (groupSeqNoInt != null) {
				stmt.setInt(4, groupSeqNoInt);
			} else {
				stmt.setNull(4, java.sql.Types.INTEGER);
			}
			stmt.execute();
			try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while (rs.next()) {
					Map<String, Object> resultMap = new HashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						String columnName = metaData.getColumnName(i);
						Object columnValue = rs.getObject(i);
						resultMap.put(columnName, columnValue);
					}
					resultList.add(resultMap);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;

	}

	@GetMapping("/fetchForwardedTo")
	public List<Map<String, Object>> fetchForwardedTo(@RequestParam String invgSeqNum) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (invgSeqNum == null || invgSeqNum.trim().isEmpty()) {
			return resultList;
		}
		String sql = "{call Spkg_Dis_Inci.sp_invg_forward_to_qry(?,?)}";
		try (Connection conn = dataSource.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
			stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
			stmt.setString(2, invgSeqNum);
			stmt.execute();
			try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while (rs.next()) {
					Map<String, Object> resultMap = new HashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						String columnName = metaData.getColumnName(i);
						Object columnValue = rs.getObject(i);
						resultMap.put(columnName, columnValue);
					}
					resultList.add(resultMap);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching forwarded-to data: " + e.getMessage());
			e.printStackTrace();
		}
		return resultList;
	}

}
