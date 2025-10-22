package com.omnet.cnt.Controller;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oracle.jdbc.OracleConnection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class AliasController {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static List<String[]> dataList = new ArrayList<>();

	static {
		// Populate the list with data
		dataList.add(new String[] { "CBI_ALIS", "1", "SPKG_CBI_ALIS.SP_CBI_ALIS_QUERY" });
		dataList.add(new String[] { "CBI_ALIS", "2", "SPKG_CBI_ALIS.SP_DOB_QUERY" });
		dataList.add(new String[] { "CBI_ALIS", "3", "SPKG_CBI_ALIS.SP_SSN_QUERY" });
		dataList.add(new String[] { "CBI_ALIS", "4", "SPKG_CBI_ALIS.SP_SBI_QUERY" });
		dataList.add(new String[] { "CBI_ALIS", "5", "spkg_omnet_wrapper.sp_cbi_alis_update" });
		dataList.add(new String[] { "CBI_ALIS", "6", "spkg_omnet_wrapper.sp_cbi_alis_insert" });
		dataList.add(new String[] { "CBI_ALIS", "7", "spkg_omnet_wrapper.sp_cbi_alis_dob_update" });
		dataList.add(new String[] { "CBI_ALIS", "8", "spkg_omnet_wrapper.sp_cbi_alis_dob_insert" });
		dataList.add(new String[] { "CBI_ALIS", "9", "spkg_omnet_wrapper.sp_cbi_alis_ssn_update" });
		dataList.add(new String[] { "CBI_ALIS", "10", "spkg_omnet_wrapper.sp_cbi_alis_ssn_insert" });
		dataList.add(new String[] { "CBI_ALIS", "11", "spkg_omnet_wrapper.sp_cbi_alis_sbi_update" });
		dataList.add(new String[] { "CBI_ALIS", "12", "spkg_omnet_wrapper.sp_cbi_alis_sbi_insert" });
	}

	// Method to get the third value based on the first two parameters
	public static String getProcedure(String screen, String tab) {
		for (String[] entry : dataList) {
			if (entry[0].equals(screen) && entry[1].equals(tab)) {
				return entry[2]; // Return the third value
			}
		}
		return null; // Return null if no match is found
	}

	@PostMapping("/fetchData")
	public List<Map<String, Object>> fetchData(@RequestParam(value = "primaryVal") String primaryVal,
			@RequestParam(value = "tab") String tab, @RequestParam(value = "screen") String scrn) {
		System.out.println("alpha commit no=" + primaryVal);
		System.out.println("Take 001");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			// String sql = "{call SPKG_CBI_ALIS.SP_CBI_ALIS_QUERY(?,?)}";
			String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				stmt.setString(1, primaryVal);
				stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							String columnName = metaData.getColumnName(i);
							System.out.println("vitalcolum : " + columnName);
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

	@PostMapping("/getRptSrc")
	public List<Map<String, Object>> getRptSrc() {
		String query = "SELECT REPORTING_DESC AS OPT,REPORTING_SOURCE_CODE AS VAL FROM REPORTING_SOURCE_RT ORDER BY REPORTING_DESC";

		System.out.println("here query " + query);

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getNmTyp")
	public List<Map<String, Object>> getNmTyp() {
		String query = "SELECT NAME_TYPE_DESC AS OPT, NAME_TYPE AS VAL FROM NAME_TYPE_RT WHERE STATUS ='A' ORDER BY NAME_TYPE_DESC";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/saveAliasName")
	public void saveAliasName(@RequestBody Map<String, List<Map<String, Object>>> combinedData) {

		List<Map<String, Object>> table1Data = combinedData.get("tab1");
		List<Map<String, Object>> table2Data = combinedData.get("tab2");
		List<Map<String, Object>> table3Data = combinedData.get("tab3");
		List<Map<String, Object>> table4Data = combinedData.get("tab4");

		System.out.println("Received data1: " + table1Data);
		System.out.println("Received data2: " + table2Data);
		System.out.println("Received data3: " + table3Data);
		System.out.println("Received data4: " + table4Data);

		if (table1Data != null) {

			List<Map<String, Object>> toInsertName = new ArrayList<>();
			List<Map<String, Object>> toUpdateName = new ArrayList<>();

			for (Map<String, Object> datum : table1Data) {
				String rowid = (String) datum.get("rowid");
				if (rowid == null || rowid.isEmpty()) {
					toInsertName.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateName.add(datum); // Existing entry
				}
			}

			savName(toInsertName, "I");
			savName(toUpdateName, "U");

		}
		
		if (table2Data != null) {

			List<Map<String, Object>> toInsertDob = new ArrayList<>();
			List<Map<String, Object>> toUpdateDob = new ArrayList<>();

			for (Map<String, Object> datum : table2Data) {
				String rowid = (String) datum.get("rowid");
				if (rowid == null || rowid.isEmpty()) {
					toInsertDob.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateDob.add(datum); // Existing entry
				}
			}

			savDob(toInsertDob, "I");
			savDob(toUpdateDob, "U");

		}
		
		if (table3Data != null) {

			List<Map<String, Object>> toInsertSsn = new ArrayList<>();
			List<Map<String, Object>> toUpdateSsn = new ArrayList<>();

			for (Map<String, Object> datum : table3Data) {
				String rowid = (String) datum.get("rowid");
				if (rowid == null || rowid.isEmpty()) {
					toInsertSsn.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateSsn.add(datum); // Existing entry
				}
			}

			savSsn(toInsertSsn, "I");
			savSsn(toUpdateSsn, "U");

		}
		
		if (table4Data != null) {

			List<Map<String, Object>> toInsertSbi = new ArrayList<>();
			List<Map<String, Object>> toUpdateSbi = new ArrayList<>();

			for (Map<String, Object> datum : table4Data) {
				String rowid = (String) datum.get("rowid");
				if (rowid == null || rowid.isEmpty()) {
					toInsertSbi.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateSbi.add(datum); // Existing entry
				}
			}

			savSbi(toInsertSbi, "I");
			savSbi(toUpdateSbi, "U");

		}

	}

	public void savName(List<Map<String, Object>> data, String typ) {
		System.out.println("Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String seq = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "5") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "6") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				if (typ.equals("U")) {
					seq = data.get(i).get("seq").toString();
				} else {
					List<Map<String, Object>> seqs = jdbcTemplate
							.queryForList("SELECT SF_GEN_SEQ_MDOC('0036') AS SEQ FROM DUAL");

					seq = seqs.get(0).get("SEQ").toString();
				}

				System.out.println("Now seq is " + seq + " for caling " + procedureCall);
				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);

				structs[i] = oracleConnection.createStruct("TY_CBI_ALIS_HDR",
						new Object[] { data.get(i).get("rowid"), data.get(i).get("commitNo"), seq,
								data.get(i).get("src_val"), data.get(i).get("firstName"), data.get(i).get("lastName"),
								data.get(i).get("nmtyp_val"), data.get(i).get("mi"), data.get(i).get("suffix"),
								data.get(i).get("mi2"), data.get(i).get("lnameUp"), data.get(i).get("fnameUp"),
								data.get(i).get("comments"), data.get(i).get("actual_dt") });
				System.out.println("post procedure1");
			}
			Array array = oracleConnection.createOracleArray("TB_CBI_ALIS_HDR", structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}
	}
	
	public void savDob(List<Map<String, Object>> data, String typ) {
		System.out.println("Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String chk = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "7") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "8") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);
				
				if(data.get(i).get("checkbox").toString().equals("true")) {
					chk = "Y";
				} else {
					chk = "N";
				}

				structs[i] = oracleConnection.createStruct("TY_CBI_ALIS_INMATE_DOB",
						new Object[] { data.get(i).get("rowid"), data.get(i).get("commitNo"),
								data.get(i).get("actual_dtBirth"), chk, data.get(i).get("comments"),
								data.get(i).get("src_val"), data.get(i).get("actual_dt") });
				System.out.println("post procedure2");
			}
			Array array = oracleConnection.createOracleArray("TB_CBI_ALIS_INMATE_DOB", structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}
	}
	
	public void savSsn(List<Map<String, Object>> data, String typ) {
		System.out.println("Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String seq = "";
		String chk = "";
		String chk1 = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "9") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "10") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				if (typ.equals("U")) {
					seq = data.get(i).get("seq").toString();
				} else {
					List<Map<String, Object>> seqs = jdbcTemplate
							.queryForList("SELECT SF_GEN_SEQ_MDOC('0035') AS SEQ FROM DUAL");

					seq = seqs.get(0).get("SEQ").toString();
				}

				System.out.println("Now seq is " + seq + " for caling " + procedureCall);
				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);
				
				if(data.get(i).get("checkbox").toString().equals("true")) {
					chk = "Y";
				} else {
					chk = "N";
				}
				
				if(data.get(i).get("checkbox1").toString().equals("true")) {
					chk1 = "Y";
				} else {
					chk1 = "N";
				}

				structs[i] = oracleConnection.createStruct("TY_CBI_ALIS_INMATE_SSN",
						new Object[] { data.get(i).get("rowid"), data.get(i).get("commitNo"), seq,
								data.get(i).get("socialSecurity"), chk, data.get(i).get("comments"),
								data.get(i).get("src_val"), data.get(i).get("actual_dt"), chk1 });
				System.out.println("post procedure3");
			}
			Array array = oracleConnection.createOracleArray("TB_CBI_ALIS_INMATE_SSN", structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}
	}
	
	public void savSbi(List<Map<String, Object>> data, String typ) {
		System.out.println("Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String seq = "";
		String chk = ""; 

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "11") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_ALIS", "12") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				if (typ.equals("U")) {
					seq = data.get(i).get("seq").toString();
				} else {
					List<Map<String, Object>> seqs = jdbcTemplate
							.queryForList("SELECT SF_GEN_SEQ_MDOC('0036') AS SEQ FROM DUAL");

					seq = seqs.get(0).get("SEQ").toString();
				}

				System.out.println("Now seq is " + seq + " for caling " + procedureCall);
				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);
				
				if(data.get(i).get("checkbox").toString().equals("true")) {
					chk = "Y";
				} else {
					chk = "N";
				}

				structs[i] = oracleConnection.createStruct("TY_CBI_ALIS_INMATE_SBI",
						new Object[] { data.get(i).get("rowid"), data.get(i).get("commitNo"),
								data.get(i).get("src_val"), seq, data.get(i).get("sbinumber"), chk,
								data.get(i).get("comments"), data.get(i).get("actual_dt") });
				System.out.println("post procedure4");
			}
			Array array = oracleConnection.createOracleArray("TB_CBI_ALIS_INMATE_SBI", structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}
	}

}
