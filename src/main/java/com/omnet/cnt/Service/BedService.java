/*
Document   :BedService
Author     : Sunandhaa
last update: 22/07/2024
*/

package com.omnet.cnt.Service;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;

import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import java.sql.ResultSet;
import java.math.BigDecimal; // ✅ Add this
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.omnet.cnt.Model.BedCell;
import com.omnet.cnt.Model.Building;
import com.omnet.cnt.Model.Enemy;
import com.omnet.cnt.Model.Inmate;
import com.omnet.cnt.Model.ReasonMaster;
import com.omnet.cnt.Model.ReasonUpdate;
import com.omnet.cnt.Model.ReferenceValue;
import com.omnet.cnt.Repository.BedRepository;
import com.omnet.cnt.Repository.BuildingRepository;
import com.omnet.cnt.Repository.CellRtRepository;
import com.omnet.cnt.Repository.FloorRtRepository;
import com.omnet.cnt.Repository.ReasonMstRepository;
import com.omnet.cnt.Repository.ReasonStatusRepo;
import com.omnet.cnt.Repository.ReferenceValueRepository;
import com.omnet.cnt.Repository.TierRepository;
import com.omnet.cnt.Repository.UnitRtRepository;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.Repository.reasonupdatestatus;
import com.omnet.cnt.SecurityConfig.proxyHelper;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.internal.OracleTimestamp;

@Service
public class BedService {

	@Autowired
	private DataSource dataSource;
	 @Autowired
	    private ReasonStatusRepo rhstatusReasonRepository;
	 
	@Autowired
	private ReferenceValueRepository referenceValueRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReasonMstRepository reasonMstRepository;

	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	private BedRepository bedRepository;

	@Autowired
	private FloorRtRepository Floorrtrepository;

	@Autowired
	private TierRepository Tierrepository;

	@Autowired
	private CellRtRepository CellRtrepository;

	@Autowired
	private UnitRtRepository unitRtrepository;

	 @Autowired
	    private reasonupdatestatus repository;

	private SimpleJdbcCall simpleJdbcCall;
	static String jdbcUrl = "jdbc:oracle:thin:@10.78.21.196:1521:devdb";
	static String username = "OMNET_APP";
	static String password = "OmnetP4ssw0rd!";

	public List<Object[]> getActiveBuildingsForInstNum(String instNum) {
		System.out.println("instNum" + instNum);
		List<Object[]> BuildingList = buildingRepository.findBuildingNamesByInstNum(instNum);
		System.out.println(BuildingList);
		return BuildingList;
	}

	public List<Object[]> getUnits(String instNum, String bldNum) {
		List<Object[]> UnitList = unitRtrepository.findByInstNumAndBldNum(instNum, bldNum);
		System.out.println(UnitList);
		return UnitList;
	}

	public List<Object[]> getFloors(String instNum, String bldNum, String unitId) {
		List<Object[]> FloorList = Floorrtrepository.findFloors(instNum, bldNum, unitId);
		System.out.println(FloorList);
		return FloorList;
	}

	public List<Object[]> getCells(String instNum, String bldNum, String unitId, String floorNum, String tierNum) {
		List<Object[]> CellList = CellRtrepository.findCells(instNum, bldNum, unitId, floorNum, tierNum);
		System.out.println(CellList);
		return CellList;
	}

	public List<Object[]> getTiers(String instNum, String bldNum, String unitId, String floorNum) {
		List<Object[]> TiersList = Tierrepository.findTiers(instNum, bldNum, unitId, floorNum);
		System.out.println(TiersList);
		return TiersList;
	}

	public Map<String, String> ReasonDropDownValues() {
		Map<String, String> resultMap = new HashMap<>();
		List<Object[]> results = reasonMstRepository.findValuefortheDropDown("HOU");
		for (Object[] row : results) {
			String resnDesc = (String) row[0];
			String resnCd = (String) row[1];
			resultMap.put(resnCd, resnDesc);
		}
		return resultMap;
	}

	public List<Object[]> getBuildingDetail(String instNum, String bedNo) {
		List<Object[]> results = buildingRepository.callSfGetBldDetail(instNum, bedNo);
		System.out.println(results);
		List<Object[]> processedResults = new ArrayList<>();
		for (Object[] row : results) {
			processedResults.add(row);
		}

		return processedResults;
	}
	   public int checkBedSecurity(String commitNo, String bedNo) {
	        return bedRepository.checkForSecurity(commitNo, bedNo);
	    }
	   
	   
	public List<Map<String, Object>> queryInmateDetails(String commitNo, String instNum) {
		System.out.println("commitNo=" + commitNo + ", instNum=" + instNum);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call Spkg_Mov_Aotb.Sp_Query_Cur_Inm_Dtl(?, ?, ?)}";
			try (CallableStatement stmt = conn.prepareCall(sql)) {
				stmt.registerOutParameter(1, OracleTypes.CURSOR);
				stmt.setString(2, commitNo);
				stmt.setString(3, instNum);
				stmt.execute();
				try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						System.out.println("Column " + i + ": " + metaData.getColumnName(i));
					}

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

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}
	 

	    public String getRefDesc(String commitNo) {
	        return rhstatusReasonRepository.findRefValueDescByCommitNo(commitNo);
	    }
	    
	    
	public List<BedCell> selectCurrInmate(String commitNo, String instNum) {
		List<BedCell> bedCells = new ArrayList<>();
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withCatalogName("SPKG_OMNET_WRAPPER")
				.withProcedureName("sp_mov_aotb_select_curr_inm")
				.declareParameters(new SqlOutParameter("resultset", OracleTypes.ARRAY, "TB_MOV_AOTB_BED_CELL"),
						new SqlParameter("i_commit_no", java.sql.Types.VARCHAR),
						new SqlParameter("i_inst_num", java.sql.Types.VARCHAR));
		SqlParameterSource inParams = new MapSqlParameterSource()
				.addValue("i_commit_no", commitNo)
				.addValue("i_inst_num", instNum);
		System.out.println("inParams selcetcuurinmate"+inParams);
		try {

			Map<String, Object> result = jdbcCall.execute(inParams);
			ARRAY array = (ARRAY) result.get("resultset");

			if (array != null) {
				try {
					Object[] arrayData = (Object[]) array.getArray();
					for (Object obj : arrayData) {
						if (obj instanceof STRUCT) {
							STRUCT struct = (STRUCT) obj;
							Object[] attributes = struct.getAttributes();
							BedCell bedCell = new BedCell();
							String bedStatus = (String) attributes[7];
							bedCell.setBedStatus(bedStatus);
							bedCell.setCellDesc((String) attributes[1]);
							bedCell.setBedDesc((String) attributes[2]);
							bedCell.setUnitDesc((String) attributes[3]);
							bedCell.setBedNo((String) attributes[4]);
							bedCell.setUnitId((String) attributes[5]);
							bedCell.setCellNo((String) attributes[6]);
							bedCell.setCovidFlag((String) attributes[8]);
							bedCells.add(bedCell);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error calling stored procedure", e);
		}

		return bedCells;
	}

	public List<BedCell> callSpSelectBedCell(String commitNo, String instNum, String bldNum, String unitId,
			String floorNum, String tierNum, String cellNo, String occupiedFlag) {
		List<BedCell> bedCells = new ArrayList<>();

		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withCatalogName("SPKG_OMNET_WRAPPER")
				.withProcedureName("sp_mov_aotb_select_bed_cell")
				.declareParameters(new SqlOutParameter("resultset", OracleTypes.ARRAY, "TB_MOV_AOTB_BED_CELL"),
						new SqlOutParameter("p_commit_no", java.sql.Types.VARCHAR),
						new SqlParameter("p_cell_desc", java.sql.Types.VARCHAR),
						new SqlParameter("p_bed_desc", java.sql.Types.VARCHAR),
						new SqlParameter("p_unit_desc", java.sql.Types.VARCHAR),
						new SqlParameter("p_bed_no", java.sql.Types.VARCHAR),
						new SqlParameter("p_unit_id", java.sql.Types.VARCHAR),
						new SqlParameter("p_cell_no", java.sql.Types.VARCHAR),
						new SqlParameter("p_covid_flag", java.sql.Types.VARCHAR),
						new SqlOutParameter("ovc_cur_bld", java.sql.Types.VARCHAR),
						new SqlOutParameter("ovc_cur_bed", java.sql.Types.VARCHAR));

		SqlParameterSource inParams = new MapSqlParameterSource().addValue("p_commit_no", commitNo)
				.addValue("p_inst_num", instNum).addValue("p_bld_num", bldNum).addValue("p_unit_id", unitId)
				.addValue("p_floor_num", floorNum).addValue("p_tier_num", tierNum).addValue("p_cell_no", cellNo)
				.addValue("p_occupied_flag", occupiedFlag);

		try {
			Map<String, Object> result = jdbcCall.execute(inParams);
			ARRAY array = (ARRAY) result.get("resultset");

			if (array != null) {
				Object[] arrayData = (Object[]) array.getArray();
				for (Object obj : arrayData) {
					if (obj instanceof STRUCT) {
						STRUCT struct = (STRUCT) obj;
						Object[] attributes = struct.getAttributes();
						  System.out.println("STRUCT attributes count: " + attributes.length);  // 🔹 count

				            for (int i = 0; i < attributes.length; i++) {
				                System.out.println("Attribute[" + i + "]: " + attributes[i]);
				            }
						BedCell bedCell = new BedCell(); 
						bedCell.setCommitNo((String) attributes[0]);
						bedCell.setCellDesc((String) attributes[1]);
						bedCell.setBedDesc((String) attributes[2]);
						bedCell.setUnitDesc((String) attributes[3]);
						bedCell.setCellNo((String) attributes[4]);
						bedCell.setBedNo((String) attributes[5]);
						bedCell.setUnitId((String) attributes[6]);
						String bedStatus = (String) attributes[7];
						bedCell.setBedStatus(bedStatus);
						bedCell.setCovidFlag((String) attributes[8]);

		                
		                
		                // 🔹 Now fetch bedSex from repository using the bedNo
		                String bedSex = getBedSex(bedCell.getBedNo());
		                bedCell.setBedSex(bedSex);
						bedCells.add(bedCell);
					}
				}
			}
			String ovcCurBld = (String) result.get("ovc_cur_bld");
			String ovcCurBed = (String) result.get("ovc_cur_bed");	

		} catch (Exception e) {
			throw new RuntimeException("Error calling stored procedure", e);
		}

		return bedCells;
	}

	public Map<String, String> callPselbedDetails(String bedNo, String bldNum, String unitId, String floorNum,
			String tierNum) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withCatalogName("SPKG_MOV_AOTB")
				.withProcedureName("p_selbed_details").withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlParameter("p_bed_no", java.sql.Types.VARCHAR),
						new SqlOutParameter("p_bld_num", java.sql.Types.VARCHAR),
						new SqlOutParameter("p_unit_id", java.sql.Types.VARCHAR),
						new SqlOutParameter("p_floor_num", java.sql.Types.VARCHAR),
						new SqlOutParameter("p_tier_num", java.sql.Types.VARCHAR));

		SqlParameterSource inParams = new MapSqlParameterSource().addValue("p_bed_no", bedNo)
				.addValue("p_bld_num", bldNum).addValue("p_unit_id", unitId).addValue("p_floor_num", floorNum)
				.addValue("p_tier_num", tierNum);

		Map<String, Object> outParams = jdbcCall.execute(inParams);

		String outBldNum = (String) outParams.get("p_bld_num");
		String outUnitId = (String) outParams.get("p_unit_id");
		String outFloorNum = (String) outParams.get("p_floor_num");
		String outTierNum = (String) outParams.get("p_tier_num");

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("bldNum", outBldNum);
		resultMap.put("unitId", outUnitId);
		resultMap.put("floorNum", outFloorNum);
		resultMap.put("tierNum", outTierNum);

		return resultMap;
	}

	public Map<String, Object> callSelectUnitWaitlist(String commitNo, String instNum, String bldNum, String unitId,
			String floorNum, String tierNum, String cellNo, String bedNo) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource)
	            .withCatalogName("SPKG_OMNET_WRAPPER")
	            .withFunctionName("F_MOV_AOTB_SELECT_UNIT_WAITLIST")
				/* .withoutProcedureColumnMetaDataAccess() */
	            .declareParameters(
	                new SqlParameter("p_commit_no", Types.VARCHAR),
	                new SqlParameter("p_inst_num", Types.VARCHAR),
	                new SqlParameter("p_bld_num", Types.VARCHAR),
	                new SqlParameter("p_unit_id", Types.VARCHAR),
	                new SqlParameter("p_floor_num", Types.VARCHAR),
	                new SqlParameter("p_tier_num", Types.VARCHAR),
	                new SqlParameter("p_cell_no", Types.VARCHAR),
	                new SqlParameter("p_bed_no", Types.VARCHAR)
	            );

	    SqlParameterSource inParams = new MapSqlParameterSource()
	            .addValue("p_commit_no", commitNo)
	            .addValue("p_inst_num", instNum)
	            .addValue("p_bld_num", bldNum)
	            .addValue("p_unit_id", unitId)
	            .addValue("p_floor_num", floorNum)
	            .addValue("p_tier_num", tierNum)
	            .addValue("p_cell_no", cellNo)
	            .addValue("p_bed_no", bedNo);
	
	    BigDecimal result = simpleJdbcCall.executeFunction(BigDecimal.class, inParams);
	    boolean isInWaitlist = result != null && result.intValue() == 1;

	    Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("result", isInWaitlist);

	    return resultMap;
	}
	
	public void unreserve(String bedNo) {
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withCatalogName("SPKG_MOV_AOTB")
				.withProcedureName("sp_unreserve_bed").withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlParameter("p_bed_no", Types.VARCHAR));
		SqlParameterSource parameters = new MapSqlParameterSource().addValue("p_bed_no", bedNo);

		try {
			/* Map<String, Object> result = simpleJdbcCall.execute(parameters); */
			simpleJdbcCall.execute(parameters);
		} catch (DataAccessException e) {
			System.err.println("Error executing stored procedure: " + e.getMessage());
			e.printStackTrace();
		}
	}

	

	public Map<String, String> callSpMovementOfRhLoc(String bldNum, String unitId, String floorNum, String tierNum,
			String commitNo, String instNum) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("Sp_omnet_mvmnt_of_rh_loc")
				.withoutProcedureColumnMetaDataAccess().declareParameters(
						new SqlParameter("p_build_num", Types.VARCHAR), new SqlParameter("p_unit_id", Types.VARCHAR),
						new SqlParameter("p_floor_num", Types.VARCHAR), new SqlParameter("p_tier_num", Types.VARCHAR),
						new SqlParameter("p_commit_no", Types.VARCHAR), new SqlParameter("p_inst_num", Types.VARCHAR),
						new SqlOutParameter("o_heading", Types.VARCHAR), new SqlOutParameter("o_popup", Types.VARCHAR));

		SqlParameterSource inParams = new MapSqlParameterSource().addValue("p_build_num", bldNum)
				.addValue("p_unit_id", unitId).addValue("p_floor_num", floorNum).addValue("p_tier_num", tierNum)
				.addValue("p_commit_no", commitNo).addValue("p_inst_num", instNum);

		Map<String, Object> outParams = jdbcCall.execute(inParams);

		String heading = (String) outParams.get("o_heading");
		String popup = (String) outParams.get("o_popup");

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("heading", heading);
		resultMap.put("popup", popup);

		return resultMap;
	}

	public static Timestamp convertToTimestamp(String dateStr) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yy");
		Date date = inputFormat.parse(dateStr);
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = outputFormat.format(date);
		return Timestamp.valueOf(formattedDate);
	}

	public void insertUnitWaitlist(String commitNo, String instNum, String bldNum, String unitId, String floorNum,
			String tierNum, String cellNo, String bedNo, String stDateTime, String toDate, String reason) {

		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_AOTB")
				.withProcedureName("p_insert_unit_waitlist").withoutProcedureColumnMetaDataAccess().declareParameters(
						new SqlParameter("p_commit_no", Types.VARCHAR), new SqlParameter("p_inst_num", Types.VARCHAR),
						new SqlParameter("p_bld_num", Types.VARCHAR), new SqlParameter("p_unit_id", Types.VARCHAR),
						new SqlParameter("p_floor_num", Types.VARCHAR), new SqlParameter("p_tier_num", Types.VARCHAR),
						new SqlParameter("p_cell_no", Types.VARCHAR), new SqlParameter("p_bed_no", Types.VARCHAR),
						new SqlParameter("p_st_date_time", Types.TIMESTAMP),
						new SqlParameter("p_to_date", Types.TIMESTAMP), new SqlParameter("p_reason", Types.VARCHAR));

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("p_commit_no", commitNo);
		parameters.put("p_inst_num", instNum);
		parameters.put("p_bld_num", bldNum);
		parameters.put("p_unit_id", unitId);
		parameters.put("p_floor_num", floorNum);
		parameters.put("p_tier_num", tierNum);
		parameters.put("p_cell_no", cellNo);
		parameters.put("p_bed_no", bedNo);
		try {
			Timestamp startDateTime = convertToTimestamp(stDateTime);
			Timestamp endDate = convertToTimestamp(toDate);

			parameters.put("p_st_date_time", startDateTime);
			parameters.put("p_to_date", endDate);
			parameters.put("p_reason", reason);

			jdbcCall.execute(parameters);
		} catch (ParseException e) {
			System.err.println("Error parsing date: " + e.getMessage());
			throw new RuntimeException("Error parsing date", e);
		} catch (DataAccessException e) {
			System.err.println("Error executing stored procedure: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error calling stored procedure p_insert_unit_waitlist", e);
		}
	}

	/*
	 * public List<Map<String, Object>> callSpRhReasonQuery(String reasonCode) { //
	 * Initialize SimpleJdbcCall SimpleJdbcCall jdbcCall = new
	 * SimpleJdbcCall(dataSource).withCatalogName("SPKG_MOV_AOTB") // Package name
	 * .withProcedureName("sp_rh_reason_query") // Procedure name
	 * .withoutProcedureColumnMetaDataAccess() .declareParameters(new
	 * SqlParameter("ivc_reason_code", java.sql.Types.VARCHAR), // Input parameter
	 * new SqlOutParameter("iotb_query", OracleTypes.CURSOR, new
	 * ColumnMapRowMapper()) // Output // parameter ); SqlParameterSource inParams =
	 * new MapSqlParameterSource().addValue("ivc_reason_code", reasonCode);
	 * Map<String, Object> outParams = jdbcCall.execute(inParams);
	 * 
	 * @SuppressWarnings("unchecked") List<Map<String, Object>> resultList =
	 * (List<Map<String, Object>>) outParams.get("iotb_query");
	 * 
	 * return resultList; }
	 */
	
	public List<Map<String, Object>> callSpRhReasonQuery(String reasonCode) {

	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
	            .withCatalogName("SPKG_MOV_AOTB") // Package name
	            .withProcedureName("sp_rh_reason_query")
	            .withoutProcedureColumnMetaDataAccess()
	            .declareParameters(
	            	new SqlOutParameter("iotb_query", OracleTypes.CURSOR, new ColumnMapRowMapper()),
	                new SqlParameter("ivc_reason_code", java.sql.Types.VARCHAR)
	                
	            );

	    SqlParameterSource inParams = new MapSqlParameterSource()
	            .addValue("ivc_reason_code", reasonCode);

	    Map<String, Object> outParams = jdbcCall.execute(inParams);

	    @SuppressWarnings("unchecked")
	    List<Map<String, Object>> resultList = (List<Map<String, Object>>) outParams.get("iotb_query");

	    return resultList;
	}

	public void callSpRhReasonUpdate(String commitNo, List<String> reasonCodes, String userId) {
	    if (commitNo == null || reasonCodes == null || reasonCodes.isEmpty()) {
	        throw new RuntimeException("Invalid commitNo or reasonCodes.");
	    }

	    try (Connection conn = proxyHelper.getProxyConnection(dataSource, userId)) {
	        OracleConnection oracleConn = conn.unwrap(OracleConnection.class);

	        // Create STRUCT array from all selected reasons
	        StructDescriptor structDesc = StructDescriptor.createDescriptor("TY_MOV_AOTB_REASON_QUERY", oracleConn);
	        STRUCT[] structs = new STRUCT[reasonCodes.size()];

	        for (int i = 0; i < reasonCodes.size(); i++) {
	            String reasonCode = reasonCodes.get(i);
	            Object[] attrs = new Object[]{"N", reasonCode, "AUTO UPDATE"};
	            structs[i] = new STRUCT(structDesc, oracleConn, attrs);
	        }

	        ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor("TB_MOV_AOTB_REASON_QUERY", oracleConn);
	        ARRAY sqlArray = new ARRAY(arrayDesc, oracleConn, structs);

	        JdbcTemplate proxyJdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));

	        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(proxyJdbcTemplate)
	                .withCatalogName("SPKG_OMNET_WRAPPER") // ✅ FIXED PACKAGE NAME
	                .withProcedureName("sp_mov_aotb_rh_reason_update")
	                .withoutProcedureColumnMetaDataAccess()
	                .declareParameters(
	                        new SqlParameter("IOTB_UPDATE", OracleTypes.ARRAY, "TB_MOV_AOTB_REASON_QUERY"),
	                        new SqlParameter("IVC_COMMIT_NO", OracleTypes.VARCHAR),
	                        new SqlParameter("IVC_REASON_CODE", OracleTypes.VARCHAR)
	                );

	        Map<String, Object> inParams = Map.of(
	                "IOTB_UPDATE", sqlArray,
	                "IVC_COMMIT_NO", commitNo,
	                "IVC_REASON_CODE", reasonCodes.get(0) // can be first reason or a master code
	        );

	        simpleJdbcCall.execute(inParams);
	        
	        System.out.println("RH Reason update procedure executed successfully.");

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to update RH reason.", e);
	    }
	}


	 public List<Object> getRhReasons(String commitNo) {
	        return repository.findRhReasonsByCommitNo(commitNo);
	    }

	/*
	 * @SuppressWarnings("deprecation") private ARRAY
	 * createEmptyReasonUpdateSqlArray(OracleConnection oracleConn) throws
	 * SQLException {
	 * 
	 * ArrayDescriptor arrayDescriptor =
	 * ArrayDescriptor.createDescriptor("TB_MOV_AOTB_REASON_QUERY", oracleConn); //
	 * ARRAY // type STRUCT[] emptyStructs = new STRUCT[0]; return new
	 * ARRAY(arrayDescriptor, oracleConn, emptyStructs); }
	 */

	public void victimaggressor() {
		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_AOTB")
				.withProcedureName("SP_VICTIM_AGGRESSOR_EXIST").withoutProcedureColumnMetaDataAccess()
				.declareParameters(
						// Input parameters
						new SqlParameter("p_commit_no", Types.VARCHAR), new SqlParameter("p_inst_num", Types.VARCHAR),
						new SqlParameter("p_cell_no", Types.VARCHAR), new SqlParameter("p_tier_num", Types.VARCHAR),
						new SqlParameter("p_floor_num", Types.VARCHAR), new SqlParameter("p_unit_id", Types.VARCHAR),
						new SqlParameter("p_bld_num", Types.VARCHAR), new SqlParameter("p_bed_no", Types.VARCHAR),
						new SqlParameter("p_num", Types.NUMERIC),
						// Output parameters
						new SqlOutParameter("ovc_error_type", Types.VARCHAR),
						new SqlOutParameter("ovc_error_message", Types.VARCHAR));
	}

	public Map<String, Object> callSpVictimAggressorExist(String commitNo, String instNum, String cellNo,
			String tierNum, String floorNum, String unitId, String bldNum, String bedNo, int num) {

		SqlParameterSource inParams = new MapSqlParameterSource().addValue("p_commit_no", commitNo)
				.addValue("p_inst_num", instNum).addValue("p_cell_no", cellNo).addValue("p_tier_num", tierNum)
				.addValue("p_floor_num", floorNum).addValue("p_unit_id", unitId).addValue("p_bld_num", bldNum)
				.addValue("p_bed_no", bedNo).addValue("p_num", num);

		Map<String, Object> outParams = simpleJdbcCall.execute(inParams);	
		return outParams;
	}

	public void assignbed(String commitNo, String instNum, String buildNum, String unitId, String floorNum,
			String tierNum, String cellNo, String bedNo, String action, Timestamp stDateTime, String resnCode,
			Timestamp toDate, String nonRhRsnFlag) {
		// Log input values for debugging
		System.out.println("commitNo: " + commitNo);
		System.out.println("instNum: " + instNum);
		System.out.println("buildNum: " + buildNum);
		System.out.println("unitId: " + unitId);
		System.out.println("floorNum: " + floorNum);
		System.out.println("tierNum: " + tierNum);
		System.out.println("cellNo: " + cellNo);
		System.out.println("bedNo: " + bedNo);
		System.out.println("action: " + action);
		System.out.println("stDateTime: " + stDateTime);
		System.out.println("resnCode: " + resnCode);
		System.out.println("toDate: " + toDate);
		System.out.println("nonRhRsnFlag: " + nonRhRsnFlag);
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource).withCatalogName("SPKG_MOV_AOTB")
				.withProcedureName("p_assign_bed").withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlParameter("p_commit_no", Types.VARCHAR),
						new SqlParameter("p_inst_num", Types.VARCHAR), new SqlParameter("p_build_num", Types.VARCHAR),
						new SqlParameter("p_unit_id", Types.VARCHAR), new SqlParameter("p_floor_num", Types.VARCHAR),
						new SqlParameter("p_tier_num", Types.VARCHAR), new SqlParameter("p_cell_no", Types.VARCHAR),
						new SqlParameter("p_bed_no", Types.VARCHAR), new SqlParameter("p_action", Types.VARCHAR),
						new SqlParameter("p_st_date_time", Types.TIMESTAMP),
						new SqlParameter("p_resn_code", Types.VARCHAR), new SqlParameter("p_to_date", Types.TIMESTAMP),
						new SqlParameter("p_non_rh_rsn_flag", Types.VARCHAR));

		SqlParameterSource in = new MapSqlParameterSource().addValue("p_commit_no", commitNo)
				.addValue("p_inst_num", instNum).addValue("p_build_num", buildNum).addValue("p_unit_id", unitId)
				.addValue("p_floor_num", floorNum).addValue("p_tier_num", tierNum).addValue("p_cell_no", cellNo)
				.addValue("p_bed_no", bedNo).addValue("p_action", action).addValue("p_st_date_time", stDateTime)
				.addValue("p_resn_code", resnCode).addValue("p_to_date", toDate)
				.addValue("p_non_rh_rsn_flag", nonRhRsnFlag);

		simpleJdbcCall.execute(in);
	}



	public List<Enemy> callSpEnemyQuery(String sbiNo) {
		List<Enemy> enemies = new ArrayList<>();

		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withCatalogName("SPKG_OMNET_WRAPPER")
				.withProcedureName("sp_cbi_noct_enemy_query").withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlOutParameter("iotb_query", OracleTypes.ARRAY, "TB_CBI_NOCT_ENEMY"),
						new SqlParameter("ivc_sbi_no", OracleTypes.VARCHAR));

		SqlParameterSource inParams = new MapSqlParameterSource().addValue("ivc_sbi_no", sbiNo);

		try {

			Map<String, Object> result = jdbcCall.execute(inParams);

			ARRAY array = (ARRAY) result.get("iotb_query");

			if (array != null) {
				try {
					Object[] arrayData = (Object[]) array.getArray();

					for (Object obj : arrayData) {
						if (obj instanceof STRUCT) {

							STRUCT struct = (STRUCT) obj;
							Object[] attributes = struct.getAttributes();

							Enemy enemy = new Enemy();
							enemy.setCommitNo((String) attributes[0]);
							enemy.setEnemyCommitNo((String) attributes[1]);
							enemy.setSbiNo((String) attributes[2]);
							enemy.setEnemyNameFirst((String) attributes[3]);
							enemy.setEnemyNameLast((String) attributes[4]);
							enemy.setEnemyNameMiddle((String) attributes[5]);
							enemy.setEnemyNameSuffix((String) attributes[6]);
							enemy.setReportingSourceCode((String) attributes[7]);
							enemy.setInmtSourceDt((Date) attributes[8]);
							enemy.setInmtEnmyTp((String) attributes[9]);
							enemy.setInmtActiveFlg((String) attributes[10]);
							enemy.setInmtVrfyFlg((String) attributes[11]);
							enemy.setEnemyComment((String) attributes[12]);
							enemy.setEnmyRowid((String) attributes[13]);
							enemy.setEnemyStatus((String) attributes[14]);

							enemies.add(enemy);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error calling stored procedure", e);
		}

		return enemies;
	}

	public List<ReferenceValue> getMovementReasons() {
		List<Object[]> results = referenceValueRepository.findMovementReasons();
		List<ReferenceValue> movementReasons = new ArrayList<>();

		for (Object[] row : results) {
			String description = (String) row[0];
			String code = (String) row[1];
			movementReasons.add(new ReferenceValue(description, code));
		}

		return movementReasons;
	}

	public List<ReferenceValue> findDisapprovalReasons() {
		List<Object[]> results = referenceValueRepository.findDisapprovalReasons();
		List<ReferenceValue> disapprovereasons = new ArrayList<>();
		for (Object[] row : results) {
			String description = (String) row[0];
			String code = (String) row[1];
			disapprovereasons.add(new ReferenceValue(description, code));
		}

		return disapprovereasons;
	}

	public List<Object[]> getUsersWithFullName(String userId) {
		return userRepository.findUsersWithFullName(userId);
	}

	public void callSpPrtctdOfndrNotification(String notificationType, String sbiNo, String commitNo, String userId,
			String instNum, String bldNum, String unitNum, String floorNum, String tierNum, String cellNo, String bedNo,
			String pobsSeqNum, String promsSeqNum) {
		try {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_AOTB")
					.withProcedureName("sp_prtctd_ofndr_notification").withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("ivc_notification_type", OracleTypes.VARCHAR),
							new SqlParameter("ivc_sbi_no", OracleTypes.VARCHAR),
							new SqlParameter("ivc_commit_no", OracleTypes.VARCHAR),
							new SqlParameter("ivc_user_id", OracleTypes.VARCHAR),
							new SqlParameter("ivc_inst_num", OracleTypes.VARCHAR),
							new SqlParameter("ivc_bld_num", OracleTypes.VARCHAR),
							new SqlParameter("ivc_unit_num", OracleTypes.VARCHAR),
							new SqlParameter("ivc_floor_num", OracleTypes.VARCHAR),
							new SqlParameter("ivc_tier_num", OracleTypes.VARCHAR),
							new SqlParameter("ivc_cell_no", OracleTypes.VARCHAR),
							new SqlParameter("ivc_bed_no", OracleTypes.VARCHAR),
							new SqlParameter("inu_pobs_seq_num", OracleTypes.VARCHAR),
							new SqlParameter("inu_proms_seq_num", OracleTypes.VARCHAR));

			Map<String, Object> inParams = new HashMap<>();
			inParams.put("ivc_notification_type", notificationType);
			inParams.put("ivc_sbi_no", sbiNo);
			inParams.put("ivc_commit_no", commitNo);
			inParams.put("ivc_user_id", userId);
			inParams.put("ivc_inst_num", instNum);
			inParams.put("ivc_bld_num", bldNum);
			inParams.put("ivc_unit_num", unitNum);
			inParams.put("ivc_floor_num", floorNum);
			inParams.put("ivc_tier_num", tierNum);
			inParams.put("ivc_cell_no", cellNo);
			inParams.put("ivc_bed_no", bedNo);
			inParams.put("inu_pobs_seq_num", pobsSeqNum);
			inParams.put("inu_proms_seq_num", promsSeqNum);

// Log parameters
			System.out.println("Executing sp_prtctd_ofndr_notification with parameters: " + inParams);

			simpleJdbcCall.execute(inParams);

// Check if the execution was successful
			System.out.println("sp_prtctd_ofndr_notification executed successfully.");

		} catch (Exception e) {
// Log the exception
			System.err.println("Failed to execute sp_prtctd_ofndr_notification: " + e.getMessage());
			throw new RuntimeException("Failed to execute sp_prtctd_ofndr_notification.", e);
		}
	}

	public void insertProtectedOffenderBedStage(String sbiNo, String commitNo, String bldNum, String unitId,
			String floorNum, String tierNum, String cellNo, String bedNo, String reasonCode, Date startDate,
			String startTime, Date endDate, String endTime) {
		String sql = "INSERT INTO protected_ofndr_bed_stage "
				+ "(pobs_seq_num, sbi_no, commit_no, bld_num, unit_id, floor_num, "
				+ "tier_num, cell_no, bed_no, reason_cd, bed_stay_start_date, "
				+ "bed_stay_start_time, bed_stay_end_date, bed_stay_end_time, processed_flag) " + "VALUES "
				+ "(sf_gen_seq_mdoc('pobs'), :sbi_no, :commit_no, :bld_num, :unit_id, "
				+ ":floor_num, :tier_num, :cell_no, :bed_no, :reason_cd, "
				+ ":bed_stay_start_date, :bed_stay_start_time, :bed_stay_end_date, " + ":bed_stay_end_time, 'N')";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("sbi_no", sbiNo);
		params.addValue("commit_no", commitNo);
		params.addValue("bld_num", bldNum);
		params.addValue("unit_id", unitId);
		params.addValue("floor_num", floorNum);
		params.addValue("tier_num", tierNum);
		params.addValue("cell_no", cellNo);
		params.addValue("bed_no", bedNo);
		params.addValue("reason_cd", reasonCode);
		params.addValue("bed_stay_start_date", startDate);
		params.addValue("bed_stay_start_time", startTime);
		params.addValue("bed_stay_end_date", endDate);
		params.addValue("bed_stay_end_time", endTime);
		try {
			jdbcTemplate.update(sql, params);
			System.out.println("Insert successful.");
		} catch (Exception e) {
			System.err.println("Failed to insert into protected_ofndr_bed_stage: " + e.getMessage());
			throw new RuntimeException("Insert failed.", e);
		}
	}

	public Map<String, Object> checkForHandicap(String commitNo, String instNum, String bldNum, String unitId,
			String floorNum, String tierNum, String cellNo, String bedNo) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_AOTB")
				.withProcedureName("p_check_for_handicap").withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlParameter("P_COMMIT_NO", Types.VARCHAR),
						new SqlParameter("P_INST_NUM", Types.VARCHAR), new SqlParameter("P_BUILD_NUM", Types.VARCHAR),
						new SqlParameter("P_UNIT_ID", Types.VARCHAR), new SqlParameter("P_floor_num", Types.VARCHAR),
						new SqlParameter("P_tier_num", Types.VARCHAR), new SqlParameter("P_cell_no", Types.VARCHAR),
						new SqlParameter("P_bed_no", Types.VARCHAR), new SqlOutParameter("p_hanmatch", Types.VARCHAR),
						new SqlOutParameter("p_sexmatch", Types.VARCHAR),
						new SqlOutParameter("P_juvenile_match", Types.VARCHAR),
						new SqlOutParameter("p_program_match", Types.VARCHAR));
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("P_COMMIT_NO", commitNo);
		inputParams.put("P_INST_NUM", instNum);
		inputParams.put("P_BUILD_NUM", bldNum);
		inputParams.put("P_UNIT_ID", unitId);
		inputParams.put("P_floor_num", floorNum);
		inputParams.put("P_tier_num", tierNum);
		inputParams.put("P_cell_no", cellNo);
		inputParams.put("P_bed_no", bedNo);
		System.out.println("inputParamscheckhandi" + inputParams);

		Map<String, Object> result = jdbcCall.execute(inputParams);
		return result;
	}

	public Map<String, Object> checkLocationAndRaiseAlert(String commitNo, String user) {
		String sql = "SELECT date_time_of_arr, count_loc_code_to, "
				+ "sf_get_loc_for_hou_ent(inst_num, sf_get_hou_loc(commit_no, inst_num)) AS hou_location "
				+ "FROM current_inst_movements " + "WHERE commit_no = ? " + "AND inst_num = sf_get_user_inst_num(?)";

		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, commitNo, user);

		Map<String, Object> response = new HashMap<>();

		if (!results.isEmpty()) {
			Map<String, Object> recLoc = results.get(0);
			Object dateTimeOfArr = recLoc.get("date_time_of_arr");
			Object countLocCodeTo = recLoc.get("count_loc_code_to");
			Object houLocation = recLoc.get("hou_location");

			response.put("dateTimeOfArr", dateTimeOfArr);
			response.put("countLocCodeTo", countLocCodeTo);
			response.put("houLocation", houLocation);
		}

		return response;
	}
	
	  public String getBedSex(String bedNum) {
	        return bedRepository.findBedSexByBedNum(bedNum);
	    }

	public void debugCheckEnemyTable() {
		String sql = "SELECT * FROM CBI_NOCT_ENEMY WHERE ROWNUM <= 5";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : results) {
			System.out.println("Row: " + row);
		}
	}

//	public List<Map<String,Object>> otherNoContacts(String sbiNo) {
//	    List<Map<String, Object>> resultList = new ArrayList<>();
//	    try (Connection conn = dataSource.getConnection()) {
//	        String sql = "{call spkg_cbi_noct.sp_enemy_by_query}"; 
//	        try (CallableStatement stmt = conn.prepareCall(sql)) {
//	        	stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
//	        	stmt.setString(2, sbiNo);
//	            stmt.execute();
//	        	try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
//	        		ResultSetMetaData metaData = rs.getMetaData();
//	        		int columnCount = metaData.getColumnCount();
//	        		while (rs.next()) {
//	        		Map<String, Object> resultMap = new HashMap<>();
//	        		for (int i = 1; i <= columnCount; i++) {
//	        		String columnName = metaData.getColumnName(i);
//	        		Object columnValue = rs.getObject(i);
//	        		resultMap.put(columnName, columnValue);
//	        		}
//	        		resultList.add(resultMap);
//	        		}
//	        		}
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    return resultList;
//	}
	
	public List<Map<String, Object>> getOtherNoContacts(String sbiNo) 
	 {
	        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName("spkg_cbi_noct") 
	                .withProcedureName("sp_enemy_by_query")
	                .withoutProcedureColumnMetaDataAccess()
	                .declareParameters(
	                		new SqlOutParameter("resultset", OracleTypes.CURSOR),
	                        new SqlParameter("sbiNo", Types.VARCHAR)
	                        
	                );

	        Map<String, Object> result = simpleJdbcCall.execute(Map.of("sbiNo", sbiNo));	     
	        return (List<Map<String, Object>>) result.get("resultset"); // Ensure the key matches the cursor name
	    }
	
	
	public Map<String, Object> noContactLocation(String commitNo) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withCatalogName("spkg_cbi_noct")
				.withProcedureName("sp_enemy_det")
				.withoutProcedureColumnMetaDataAccess()
				.declareParameters(
						new SqlParameter("ivc_enmy_commit_no", Types.VARCHAR),
						new SqlOutParameter("ovc_location", Types.VARCHAR),
						new SqlOutParameter("ovc_inst_num", Types.VARCHAR),
						new SqlOutParameter("ovc_status_desc", Types.VARCHAR),
						new SqlOutParameter("ovc_incarcerated", Types.VARCHAR),
						new SqlOutParameter("ovc_unit_desc", Types.VARCHAR),
						new SqlOutParameter("ovc_cell_desc", Types.VARCHAR),
						new SqlOutParameter("ovc_bed_desc", Types.VARCHAR),
						new SqlOutParameter("ovc_adr_street_1", Types.VARCHAR),
						new SqlOutParameter("ovc_adr_zip_5", Types.VARCHAR),
						new SqlOutParameter("ovc_Adr_zip_4", Types.VARCHAR),
						new SqlOutParameter("ovc_state_name", Types.VARCHAR),
						new SqlOutParameter("ovc_county_name", Types.VARCHAR),
						new SqlOutParameter("ovc_phone_no", Types.VARCHAR)
						);
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("ivc_enmy_commit_no", commitNo);
		Map<String, Object> result = jdbcCall.execute(inputParams);
		return result;
	}
	
	
	
	public void insertNoContactDetails(List<Enemy> noContactDetails) {
		System.out.println("insertNoContactDetails"+noContactDetails.get(0).getCommitNo());
		String procedureCall = "{call spkg_omnet_wrapper.sp_cbi_noct_enemy_insert(?)}";
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			 OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			 Struct[] structs = new Struct[noContactDetails.size()];
			 for (int i = 0; i < noContactDetails.size(); i++) {
				 structs[i] = oracleConnection.createStruct("TY_CBI_NOCT_ENEMY", new Object[]{
						 
						 noContactDetails.get(i).getCommitNo(),
						 noContactDetails.get(i).getEnemyCommitNo(),
						 noContactDetails.get(i).getSbiNo(),
						 noContactDetails.get(i).getEnemyNameFirst(),
						 noContactDetails.get(i).getEnemyNameLast(),
						 noContactDetails.get(i).getEnemyNameMiddle(),
						 noContactDetails.get(i).getEnemyNameSuffix(),
						 noContactDetails.get(i).getReportingSourceCode(),
						 new java.sql.Date(noContactDetails.get(i).getInmtSourceDt().getTime()),
						 noContactDetails.get(i).getInmtEnmyTp(),
						 noContactDetails.get(i).getInmtActiveFlg(),
						 noContactDetails.get(i).getInmtVrfyFlg(),
						 noContactDetails.get(i).getEnemyComment(),
						 noContactDetails.get(i).getEnmyRowid(),
						 noContactDetails.get(i).getEnemyStatus(),
						 
				 });
					System.out.println("post procedure3"); 
			 }
			 Array array = oracleConnection.createOracleArray("TB_CBI_NOCT_ENEMY", structs);
			 callableStatement.setArray(1, array);
	            callableStatement.execute(); 
	             }catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure", e);
	        }
	}
	

	public void callUpdateLocation(String commitNo, String userId) throws SQLException {
		 System.out.println("proxyhelper"+userId); 
	    try (Connection conn = proxyHelper.getProxyConnection(dataSource, userId);
	    	CallableStatement stmt = conn.prepareCall("{call spkg_mov_aotb.sp_update_location(?)}")) {
	        stmt.setString(1, commitNo);
	        stmt.execute();
	    }
	}
	


	
	public void updateNoContactDetails(List<Enemy> updatedRows) {
	    String procedureCall = "{call spkg_omnet_wrapper.sp_cbi_noct_enemy_update(?, ?)}";

	    try (Connection connection = dataSource.getConnection();
	         CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

	        OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
	        Struct[] structs = new Struct[updatedRows.size()];

	        for (int i = 0; i < updatedRows.size(); i++) {
	            structs[i] = oracleConnection.createStruct("TY_CBI_NOCT_ENEMY", new Object[]{
	                updatedRows.get(i).getCommitNo(),
	                updatedRows.get(i).getEnemyCommitNo(),
	                updatedRows.get(i).getSbiNo(),
	                updatedRows.get(i).getEnemyNameFirst(),
	                updatedRows.get(i).getEnemyNameLast(),
	                updatedRows.get(i).getEnemyNameMiddle(),
	                updatedRows.get(i).getEnemyNameSuffix(),
	                updatedRows.get(i).getReportingSourceCode(),
	                new java.sql.Date(updatedRows.get(i).getInmtSourceDt().getTime()),
	                updatedRows.get(i).getInmtEnmyTp(),
	                updatedRows.get(i).getInmtActiveFlg(),
	                updatedRows.get(i).getInmtVrfyFlg(),
	                updatedRows.get(i).getEnemyComment(),
	                updatedRows.get(i).getEnmyRowid(),
	                updatedRows.get(i).getEnemyStatus()
	            });
	        }

	        Array array = oracleConnection.createOracleArray("TB_CBI_NOCT_ENEMY", structs);
	        callableStatement.setArray(1, array);
	        callableStatement.setString(2, updatedRows.get(0).getEnmyRowid());
	        callableStatement.execute();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error executing update stored procedure", e);
	    }
	}
	
	
	public List<Object[]> getBuildingHierarchy(Integer instNum) {
		
		List<Object[]> buildings = buildingRepository.getBuildingHierarchy(instNum);
		for (Object[] row : buildings) {
		    String bldNum = (row[0] != null) ? row[0].toString() : null;
		    String bldName = (row[1] != null) ? row[1].toString() : null;
		    String unitId = (row[2] != null) ? row[2].toString() : null;
		    String unitDesc = (row[3] != null) ? row[3].toString() : null;
		    String floorNum = (row[4] != null) ? row[4].toString() : null;
		    String floorDesc = (row[5] != null) ? row[5].toString() : null;

		    System.out.println("Building: " + bldNum + " - " + bldName +
		                       ", Unit: " + unitId + " - " + unitDesc +
		                       ", Floor: " + floorNum + " - " + floorDesc);
		}

	    return buildings;
	}



	 
	public String callShowBedRecommendations(
		    String commitNo,
		    String instNum,
		    String buildNum,
		    String unitId,
		    String floorNum,
		    String tierNum,
		    String cellNo) {

		    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
		        .withCatalogName("SPKG_MOV_AOTB")
		        .withProcedureName("sp_show_bedrecommendations")
		        .withoutProcedureColumnMetaDataAccess()
		        .declareParameters(
		            new SqlParameter("p_commit_no", Types.VARCHAR),
		            new SqlParameter("p_inst_num", Types.VARCHAR),
		            new SqlParameter("p_build_num", Types.VARCHAR),
		            new SqlParameter("p_unit_id", Types.VARCHAR),
		            new SqlParameter("p_floor_num", Types.VARCHAR),
		            new SqlParameter("p_tier_num", Types.VARCHAR),
		            new SqlParameter("p_cell_no", Types.VARCHAR),
		            new SqlOutParameter("resultset", Types.VARCHAR) // Adjust type if different
		        );

		    Map<String, Object> inParams = new HashMap<>();
		    inParams.put("p_commit_no", commitNo);
		    inParams.put("p_inst_num", instNum);
		    inParams.put("p_build_num", buildNum);
		    inParams.put("p_unit_id", unitId);
		    inParams.put("p_floor_num", floorNum);
		    inParams.put("p_tier_num", tierNum);
		    inParams.put("p_cell_no", cellNo);

		    Map<String, Object> out = jdbcCall.execute(inParams);

		    return out.get("resultset") != null ? out.get("resultset").toString() : null;
		}

	

}
