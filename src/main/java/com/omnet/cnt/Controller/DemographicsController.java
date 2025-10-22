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

import com.omnet.cnt.SecurityConfig.proxyHelper;

import oracle.jdbc.OracleConnection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class DemographicsController {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static List<String[]> dataList = new ArrayList<>();

	static {
		// Populate the list with data
		dataList.add(new String[] { "CBI_DEMO", "1", "SPKG_CBI_DEMO.SP_INMATE_QUERY" });
		dataList.add(new String[] { "CBI_DEMO", "2", "SPKG_CBI_DEMO.SP_ADR_QUERY" });
		dataList.add(new String[] { "CBI_DEMO", "3", "SPKG_CBI_DEMO.SP_VEHL_QUERY" });
		dataList.add(new String[] { "CBI_DEMO", "4", "SPKG_CBI_DEMO.SP_WEAPON_QUERY" });
		dataList.add(new String[] { "CBI_DEMO", "5", "SPKG_CBI_DEMO.SP_ANIMAL_QUERY" });
		dataList.add(new String[] { "CBI_DEMO", "6", "spkg_omnet_wrapper.sp_cbi_demo_inmate_update" });
		dataList.add(new String[] { "CBI_DEMO", "7", "spkg_omnet_wrapper.sp_cbi_demo_adr_update" });
		dataList.add(new String[] { "CBI_DEMO", "8", "spkg_omnet_wrapper.sp_cbi_demo_adr_insert" });
		dataList.add(new String[] { "CBI_DEMO", "9", "spkg_omnet_wrapper.sp_cbi_demo_vehl_update" });
		dataList.add(new String[] { "CBI_DEMO", "10", "spkg_omnet_wrapper.sp_cbi_demo_vehl_insert" });
		dataList.add(new String[] { "CBI_DEMO", "11", "spkg_omnet_wrapper.sp_cbi_demo_weapon_update" });
		dataList.add(new String[] { "CBI_DEMO", "12", "spkg_omnet_wrapper.sp_cbi_demo_weapon_insert" });
		dataList.add(new String[] { "CBI_DEMO", "13", "spkg_omnet_wrapper.sp_cbi_demo_animal_update" });
		dataList.add(new String[] { "CBI_DEMO", "14", "spkg_omnet_wrapper.sp_cbi_demo_animal_insert" });

		dataList.add(new String[] { "13", "1", "SPKG_CBI_DEMO.SP_INMATE_QUERY" });
		dataList.add(new String[] { "13", "2", "SPKG_CBI_DEMO.SP_ADR_QUERY" });
		dataList.add(new String[] { "13", "3", "SPKG_CBI_DEMO.SP_VEHL_QUERY" });
		dataList.add(new String[] { "13", "4", "SPKG_CBI_DEMO.SP_WEAPON_QUERY" });
		dataList.add(new String[] { "13", "5", "SPKG_CBI_DEMO.SP_ANIMAL_QUERY" });
		dataList.add(new String[] { "13", "6", "spkg_omnet_wrapper.sp_cbi_demo_inmate_update" });
		dataList.add(new String[] { "13", "7", "spkg_omnet_wrapper.sp_cbi_demo_adr_update" });
		dataList.add(new String[] { "13", "8", "spkg_omnet_wrapper.sp_cbi_demo_adr_insert" });
		dataList.add(new String[] { "13", "9", "spkg_omnet_wrapper.sp_cbi_demo_vehl_update" });
		dataList.add(new String[] { "13", "10", "spkg_omnet_wrapper.sp_cbi_demo_vehl_insert" });
		dataList.add(new String[] { "13", "11", "spkg_omnet_wrapper.sp_cbi_demo_weapon_update" });
		dataList.add(new String[] { "13", "12", "spkg_omnet_wrapper.sp_cbi_demo_weapon_insert" });
		dataList.add(new String[] { "13", "13", "spkg_omnet_wrapper.sp_cbi_demo_animal_update" });
		dataList.add(new String[] { "13", "14", "spkg_omnet_wrapper.sp_cbi_demo_animal_insert" });
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

	@GetMapping("/getCountyInfo")
	public List<Map<String, Object>> getAllScreens(@RequestParam(value = "filterStr") String filterStr) {
		String query = "SELECT COUNTY_CODE, COUNTY_NAME, STATE_CODE, ZIP_5, ZIP_4 FROM COUNTY_RT where STATUS = 'A'";

		if (!filterStr.equals("")) {
			query += " AND upper(COUNTY_NAME) LIKE '" + filterStr.toUpperCase() + "%'";
			// query += " AND upper(SCREEN_CODE) LIKE '%" + filterStr.toUpperCase() + "%' OR
			// upper(SCREEN_NAME) LIKE '%"
			// + filterStr.toUpperCase() + "%'";
		}

		query += " ORDER BY COUNTY_NAME";

		System.out.println("Screen Query " + query);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/fetchInfo")
	public List<Map<String, Object>> fetchInfo(@RequestParam(value = "primaryVal") String primaryVal,
			@RequestParam(value = "tab") String tab, @RequestParam(value = "screen") String scrn) {
		System.out.println("alpha commit no=" + primaryVal);
		System.out.println("Take 001");
		System.out.println("screen is " + scrn);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			// String sql = "{call SPKG_CBI_ALIS.SP_CBI_ALIS_QUERY(?,?)}";
			String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, primaryVal);
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

	@PostMapping("/fetchInfo2")
	public List<Map<String, Object>> fetchInfo2(@RequestParam(value = "adr_seq") String adrSeq,
			@RequestParam(value = "tab") String tab, @RequestParam(value = "screen") String scrn) {
		System.out.println("Take 001");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			// String sql = "{call SPKG_CBI_ALIS.SP_CBI_ALIS_QUERY(?,?)}";
			String sql = "{call " + getProcedure(scrn, tab) + "(?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, adrSeq);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					System.out.println("Here " + columnCount);

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

	@PostMapping("/fetchInfo3")
	public List<Map<String, Object>> fetchInfo3(@RequestParam(value = "primaryVal") String primaryVal,
			@RequestParam(value = "adr_seq") String adrSeq, @RequestParam(value = "tab") String tab,
			@RequestParam(value = "screen") String scrn) {
		System.out.println("Take 001");
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			// String sql = "{call SPKG_CBI_ALIS.SP_CBI_ALIS_QUERY(?,?)}";
			String sql = "{call " + getProcedure(scrn, tab) + "(?,?,?)}";

			// String sql = "{call " + getProcedure(scrn, tab) + "(?)}";

			System.out.println("Expecting sql " + sql);

			try (CallableStatement stmt = conn.prepareCall(sql)) {
				// stmt.setString(1, primaryVal);
				// stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
				stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
				stmt.setString(2, primaryVal);
				stmt.setString(3, adrSeq);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					System.out.println("Here " + columnCount);

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

	@PostMapping("/saveDemo")
	public void saveAliasName(@RequestBody Map<String, List<Map<String, Object>>> combinedData) {

		List<Map<String, Object>> tableXData = combinedData.get("tabX");
		List<Map<String, Object>> table0Data = combinedData.get("tab0");
		List<Map<String, Object>> table1Data = combinedData.get("tab1");
		List<Map<String, Object>> table2Data = combinedData.get("tab2");
		List<Map<String, Object>> table3Data = combinedData.get("tab3");
		List<Map<String, Object>> table4Data = combinedData.get("tab4");

		System.out.println("Received data0: " + table0Data);
		System.out.println("Received data1: " + table1Data);
		System.out.println("Received data2: " + table2Data);
		System.out.println("Received data3: " + table3Data);
		System.out.println("Received data4: " + table4Data);
		
		String user_id = tableXData.get(0).get("user_id").toString();
		
		System.out.println("user_id 123 is " + user_id);

		if(table0Data != null) {
			savInmate(table0Data);
		}
		
		String new_adr_seq = "";

		if (table1Data != null) {

			System.out.println("Check inside table1Data");

			List<Map<String, Object>> toInsertAdr = new ArrayList<>();
			List<Map<String, Object>> toUpdateAdr = new ArrayList<>();

			for (Map<String, Object> datum : table1Data) {
				String rowid = (String) datum.get("adr_rowid");

				System.out.println("Here rowid in adress : " + rowid);

				if (rowid == null || rowid.isEmpty()) {
					toInsertAdr.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateAdr.add(datum); // Existing entry
				}
			}

			if (toInsertAdr.size() > 0) {

				List<Map<String, Object>> seqs = jdbcTemplate
						.queryForList("SELECT SF_GEN_SEQ_MDOC('0002') AS SEQ FROM DUAL");
				new_adr_seq = seqs.get(0).get("SEQ").toString();

				savAdr(toInsertAdr, "I", new_adr_seq, user_id);
			}

			if (toUpdateAdr.size() > 0) {
				savAdr(toUpdateAdr, "U", new_adr_seq, user_id);
			}
		}

		if (table2Data != null) {

			System.out.println("Check inside table2Data");

			List<Map<String, Object>> toInsertVehl = new ArrayList<>();
			List<Map<String, Object>> toUpdateVehl = new ArrayList<>();

			for (Map<String, Object> datum : table2Data) {
				String rowid = (String) datum.get("vehl_rowid");
				if (rowid == null || rowid.isEmpty()) {
					toInsertVehl.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateVehl.add(datum); // Existing entry
				}
			}

			if (toInsertVehl.size() > 0) {
				savVehl(toInsertVehl, "I", new_adr_seq);
			}

			if (toUpdateVehl.size() > 0) {
				savVehl(toUpdateVehl, "U", new_adr_seq);
			}
		}

		if (table3Data != null) {

			System.out.println("Check inside table3Data");

			List<Map<String, Object>> toInsertAnim = new ArrayList<>();
			List<Map<String, Object>> toUpdateAnim = new ArrayList<>();

			for (Map<String, Object> datum : table3Data) {
				String rowid = (String) datum.get("rowid");
				if (rowid == null || rowid.isEmpty()) {
					toInsertAnim.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateAnim.add(datum); // Existing entry
				}
			}

			if (toInsertAnim.size() > 0) {
				savAnim(toInsertAnim, "I", new_adr_seq);
			}

			if (toUpdateAnim.size() > 0) {
				savAnim(toUpdateAnim, "U", new_adr_seq);
			}
		}

		if (table4Data != null) {

			System.out.println("Check inside table4Data");

			List<Map<String, Object>> toInsertWeap = new ArrayList<>();
			List<Map<String, Object>> toUpdateWeap = new ArrayList<>();

			for (Map<String, Object> datum : table4Data) {
				String seqno = (String) datum.get("seqno");
				if (seqno == null || seqno.isEmpty()) {
					toInsertWeap.add(datum); // No row-id means it's a new entry
				} else {
					toUpdateWeap.add(datum); // Existing entry
				}
			}

			if (toInsertWeap.size() > 0) {
				savWeap(toInsertWeap, "I", new_adr_seq);
			}

			if (toUpdateWeap.size() > 0) {
				savWeap(toUpdateWeap, "U", new_adr_seq);
			}
		}

	}

	public void savAdr(List<Map<String, Object>> data, String typ, String new_adr_seq, String user_id) {
		System.out.println("SavAdr Func Datatype : " + typ + " for data: " + data);

		System.out.println("Here new_adr_seq is " + new_adr_seq);

		String procedureCall = "";
		String seq = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "7") + "(?)}";
		} else {
			// procedureCall = "{call " + getProcedure("CBI_DEMO", "8") + "(?,?)}";
			procedureCall = "{call " + getProcedure("CBI_DEMO", "8") + "(?)}";
		}
		
		System.out.println("Here my user_id : " + user_id);


		//try (Connection connection = proxyHelper.getProxyConnection(dataSource, user_id)) {
			
		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

				String ty_typ = "";
				String tb_typ = "";

				Struct[] structs = new Struct[data.size()];
				for (int i = 0; i < data.size(); i++) {

					if (typ.equals("U")) {
						seq = data.get(i).get("address_seq_num").toString();
						ty_typ = "TY_CBI_DEMO_ADR";
						tb_typ = "TB_CBI_DEMO_ADR";
					} else {
						/*
						 * List<Map<String, Object>> seqs = jdbcTemplate
						 * .queryForList("SELECT SF_GEN_SEQ_MDOC('0036') AS SEQ FROM DUAL");
						 * 
						 * seq = seqs.get(0).get("SEQ").toString();
						 */
						seq = new_adr_seq;
						ty_typ = "TY_CBI_DEMO_ADR";
						tb_typ = "TB_CBI_DEMO_ADR";
					}

					System.out.println("Now seq is " + seq + " for caling " + procedureCall);
					System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);
					
					System.out.println("here address rowid : " + data.get(i).get("adr_rowid"));
					System.out.println("here commit number : " + data.get(i).get("commit_no"));
					System.out.println("here address street 1 : " + data.get(i).get("adr_street_1"));
					System.out.println("here county code : " + data.get(i).get("county_code"));
					System.out.println("here state code : " + data.get(i).get("state_code"));
					System.out.println("here address zip 5 : " + data.get(i).get("adr_zip_5"));
					System.out.println("here address zip 4 : " + data.get(i).get("adr_zip_4"));
					System.out.println("here inmate adult special comment : " + data.get(i).get("inmt_adt_spcl_comnt"));
					System.out.println("here inmate address phone : " + data.get(i).get("inmt_adr_ph"));
					System.out.println("here resides with phone : " + data.get(i).get("resides_with_phone"));
					System.out.println("here inmate address start date : " + data.get(i).get("inmt_adr_st_dt")); 
					System.out.println("here address type : " + data.get(i).get("address_type"));
					System.out.println("here residence type : " + data.get(i).get("residence_type")); 
					System.out.println("here sequence number : " + Integer.parseInt(seq));  // Note: This one uses 'seq' variable, not from data.get(i)
					System.out.println("here county name : " + data.get(i).get("county_name"));
					System.out.println("here hot spot areas : " + data.get(i).get("hot_spot_areas"));
					System.out.println("here cautions : " + data.get(i).get("cautions"));
					System.out.println("here CJIS city state zip : " + data.get(i).get("cjis_city_state_zip"));
					System.out.println("here homeless flag : " + data.get(i).get("homeless_flag"));
					System.out.println("here residence category : " + data.get(i).get("residence_category"));
					System.out.println("here residence category comments : " + data.get(i).get("residence_cat_comments"));

					structs[i] = oracleConnection.createStruct(ty_typ, new Object[] { data.get(i).get("adr_rowid"),
							data.get(i).get("commit_no"), data.get(i).get("adr_street_1"),
							data.get(i).get("county_code"), data.get(i).get("state_code"), data.get(i).get("adr_zip_5"),
							data.get(i).get("adr_zip_4"), data.get(i).get("inmt_adt_spcl_comnt"),
							data.get(i).get("inmt_adr_ph"), data.get(i).get("resides_with_phone"),
							data.get(i).get("inmt_adr_st_dt"), data.get(i).get("address_type"),
							data.get(i).get("residence_type"), Integer.parseInt(seq), data.get(i).get("county_name"),
							data.get(i).get("hot_spot_areas"), data.get(i).get("cautions"),
							data.get(i).get("cjis_city_state_zip"), data.get(i).get("homeless_flag"),
							data.get(i).get("residence_category"), data.get(i).get("residence_cat_comments") });
					System.out.println("post procedure2");
				}
				Array array = oracleConnection.createOracleArray(tb_typ, structs);
				callableStatement.setArray(1, array);

				System.out.println("typ is " + typ);

				/*
				 * if (!typ.equals("U")) {
				 * 
				 * System.out.println("typ for insert"); callableStatement.setString(2,
				 * "BADMMRP"); }
				 */

				callableStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Error executing stored procedure", e);
			}
		/*} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}*/

	}

	public void savVehl(List<Map<String, Object>> data, String typ, String new_adr_seq) {
		System.out.println("SavVehl Func Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String seq = "";
		String adr_seq = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "9") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "10") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			String ty_typ = "";
			String tb_typ = "";

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				if (new_adr_seq.equals("")) {
					adr_seq = data.get(i).get("address_seq_num").toString();
				} else {
					adr_seq = new_adr_seq;
				}

				if (typ.equals("U")) {
					// seq = data.get(i).get("address_seq_num").toString();
					ty_typ = "TY_CBI_DEMO_VEHL_UPDATE";
					tb_typ = "TB_CBI_DEMO_VEHL_UPDATE";
				} else {
					/*
					 * List<Map<String, Object>> seqs = jdbcTemplate
					 * .queryForList("SELECT SF_GEN_SEQ_MDOC('0036') AS SEQ FROM DUAL");
					 * 
					 * seq = seqs.get(0).get("SEQ").toString();
					 */

					ty_typ = "TY_CBI_DEMO_VEHL_INSERT";
					tb_typ = "TB_CBI_DEMO_VEHL_INSERT";
				}

				System.out.println("Now seq is " + seq + " for caling " + procedureCall);
				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);

				structs[i] = oracleConnection.createStruct(ty_typ,
						new Object[] { data.get(i).get("vehl_rowid"), adr_seq, data.get(i).get("inmate_commit_no"),
								data.get(i).get("vehl_make"), data.get(i).get("vehl_color"),
								data.get(i).get("vehl_vin"), data.get(i).get("vehl_model"),
								data.get(i).get("vehl_year") });
				System.out.println("post procedure3");
			}
			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	public void savAnim(List<Map<String, Object>> data, String typ, String new_adr_seq) {
		System.out.println("savAnim Func Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String seq = "";
		String adr_seq = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "13") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "14") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			String ty_typ = "";
			String tb_typ = "";

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				if (new_adr_seq.equals("")) {
					adr_seq = data.get(i).get("adrSeq").toString();
				} else {
					adr_seq = new_adr_seq;
				}

				if (typ.equals("U")) {
					// seq = data.get(i).get("address_seq_num").toString();
					ty_typ = "TY_CBI_DEMO_ANIMAL_UPDATE";
					tb_typ = "TB_CBI_DEMO_ANIMAL_UPDATE";
				} else {
					/*
					 * List<Map<String, Object>> seqs = jdbcTemplate
					 * .queryForList("SELECT SF_GEN_SEQ_MDOC('0036') AS SEQ FROM DUAL");
					 * 
					 * seq = seqs.get(0).get("SEQ").toString();
					 */

					ty_typ = "TY_CBI_DEMO_ANIMAL_INSERT";
					tb_typ = "TB_CBI_DEMO_ANIMAL_INSERT";
				}

				System.out.println("Now seq is " + seq + " for caling " + procedureCall);
				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);

				structs[i] = oracleConnection.createStruct(ty_typ,
						new Object[] { data.get(i).get("rowid"), data.get(i).get("commitNo"),
								data.get(i).get("typeOfAnimal"), data.get(i).get("leashVal"),
								data.get(i).get("comments"), adr_seq });
				System.out.println("post procedure3");
			}
			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	public void savWeap(List<Map<String, Object>> data, String typ, String new_adr_seq) {
		System.out.println("savWeap Func Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String seq = "";
		String adr_seq = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "11") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "12") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			String ty_typ = "";
			String tb_typ = "";

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				if (new_adr_seq.equals("")) {
					adr_seq = data.get(i).get("adrSeq").toString();
				} else {
					adr_seq = new_adr_seq;
				}

				if (typ.equals("U")) {
					seq = data.get(i).get("seqno").toString();
					ty_typ = "TY_CBI_DEMO_WEAPON_UPDATE";
					tb_typ = "TB_CBI_DEMO_WEAPON_UPDATE";
				} else {

					List<Map<String, Object>> seqs = jdbcTemplate
							.queryForList("SELECT SF_GEN_SEQ_MDOC('0039') AS SEQ FROM DUAL");
					seq = seqs.get(0).get("SEQ").toString();

					ty_typ = "TY_CBI_DEMO_WEAPON_INSERT";
					tb_typ = "TB_CBI_DEMO_WEAPON_INSERT";
				}

				System.out.println("Now seq is " + seq + " for caling " + procedureCall);
				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);

				structs[i] = oracleConnection.createStruct(ty_typ,
						new Object[] { data.get(i).get("commitNo"), adr_seq, seq, data.get(i).get("description") });
				System.out.println("post procedure4");
			}
			Array array = oracleConnection.createOracleArray(tb_typ, structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}

	}

	public void savInmate(List<Map<String, Object>> data) {
		System.out.println("Data: " + data);

		String procedureCall = "";

		procedureCall = "{call " + getProcedure("CBI_DEMO", "6") + "(?)}";

		System.out.println("procedureCall is " + procedureCall);

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			System.out.println("Data size is " + data.size());

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				System.out.println("Now rowid is " + data.get(i).get("rowid") + " for caling " + procedureCall);
				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);

				System.out.println(
						"Testing value Facial : " + EmptyToNull(data.get(i).get("facial_hair_code").toString()));

				structs[i] = oracleConnection.createStruct("TY_CBI_DEMO_INMATE_UPDATE",
						new Object[] { data.get(i).get("rowid"), data.get(i).get("commitNo"),
								EmptyToNull(data.get(i).get("hair_color_code").toString()),
								EmptyToNull(data.get(i).get("eye_color_code").toString()),
								Integer.parseInt(EmptyToNull(data.get(i).get("weight").toString())),
								EmptyToNull(data.get(i).get("glasses").toString()),
								EmptyToNull(data.get(i).get("dexterity_code").toString()),
								Integer.parseInt(EmptyToNull(data.get(i).get("height").toString())),
								EmptyToNull(data.get(i).get("sex").toString()),
								EmptyToNull(data.get(i).get("contact_lense").toString()),
								EmptyToNull(data.get(i).get("facial_hair_code").toString()),
								EmptyToNull(data.get(i).get("skin_color_code").toString()),
								EmptyToNull(data.get(i).get("build_code").toString()),
								EmptyToNull(data.get(i).get("race_code").toString()),
								EmptyToNull(data.get(i).get("citizenship_code").toString()),
								EmptyToNull(data.get(i).get("place_of_birth_city").toString()),
								EmptyToNull(data.get(i).get("religion_code").toString()),
								EmptyToNull(data.get(i).get("primary_dob").toString()),
								EmptyToNull(data.get(i).get("place_of_birth_state").toString()),
								EmptyToNull(data.get(i).get("marital_status_code").toString()),
								EmptyToNull(data.get(i).get("comprehend_english").toString()),
								Integer.parseInt(EmptyToNull(data.get(i).get("age").toString())),
								EmptyToNull(data.get(i).get("drv_state_issue").toString()),
								EmptyToNull(data.get(i).get("inmt_prim_sid_no").toString()),
								EmptyToNull(data.get(i).get("drv_license_no").toString()),
								EmptyToNull(data.get(i).get("admiss_date").toString()),
								EmptyToNull(data.get(i).get("admiss_time").toString()), null, null,
								EmptyToNull(data.get(i).get("drv_lic_status").toString()),
								EmptyToNull(data.get(i).get("drv_lic_commercial_status").toString()), null, null,
								EmptyToNull(data.get(i).get("ethnic_origin").toString()),
								EmptyToNull(data.get(i).get("esl_flag").toString()) });
				System.out.println("post procedure1");
			}

			System.out.println("structs is " + structs.toString());

			Array array = oracleConnection.createOracleArray("TB_CBI_DEMO_INMATE_UPDATE", structs);
			callableStatement.setArray(1, array);
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error executing stored procedure", e);
		}
	}

	private String EmptyToNull(String value) {
		return (value == null || value.trim().isEmpty()) ? null : value;
	}

	public void savAddress(List<Map<String, Object>> data, String typ) {
		System.out.println("Datatype : " + typ + " for data: " + data);

		String procedureCall = "";
		String chk = "";

		if (typ.equals("U")) {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "7") + "(?)}";
		} else {
			procedureCall = "{call " + getProcedure("CBI_DEMO", "8") + "(?)}";
		}

		try (Connection connection = dataSource.getConnection();

				CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

			Struct[] structs = new Struct[data.size()];
			for (int i = 0; i < data.size(); i++) {

				System.out.println("Here commitNo is " + data.get(i).get("commitNo") + " for " + i);

				if (data.get(i).get("checkbox").toString().equals("true")) {
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

}
