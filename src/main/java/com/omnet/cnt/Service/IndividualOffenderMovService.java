/**
 * Document: IndividualOffenderMovService.java
 * Author: Jamal Abraar
 * Date Created: 20-Aug-2024
 * Last Updated: 
 */
package com.omnet.cnt.Service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Repository.ActivityTypeRepository;
import com.omnet.cnt.Repository.InstLocationRepository;
import com.omnet.cnt.classes.IndividualOffMov;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Service
public class IndividualOffenderMovService {

	@Autowired
	private ActivityTypeRepository activityTypeRepository;

	@Autowired
	private InstLocationRepository instLocationRepository;

	private static final Logger logger = LoggerFactory.getLogger(IndividualOffenderMovService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public int getRecordCount(String commitNo, Date dateOfDep) {
		String sql = "SELECT COUNT(*) FROM INST_MOVEMENT_LOG WHERE COMMIT_NO = :commitNo AND DATE_TIME_OF_DEP = :dateTimeOfDep";

		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("commitNo", commitNo)
				.addValue("dateTimeOfDep", dateOfDep);

		return namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
	}

	// Method to check if the last location matches the current location
	public String getLastLocation(String commitNo, String instNum) {
		String sql = "SELECT count_loc_code_from FROM Current_Inst_Movements "
				+ "WHERE commit_no = :commitNo AND inst_num = :instNum";

		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("commitNo", commitNo)
				.addValue("instNum", instNum);

		return namedParameterJdbcTemplate.queryForObject(sql, parameters, String.class);
	}

	// Method to check if the arrival date is set for the last movement
	public Timestamp getArrivalDate(String commitNo, String instNum) {
		String sql = "SELECT date_time_of_arr FROM Current_Inst_Movements "
				+ "WHERE commit_no = :commitNo AND inst_num = :instNum";

		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("commitNo", commitNo)
				.addValue("instNum", instNum);

		return namedParameterJdbcTemplate.queryForObject(sql, parameters, Timestamp.class);
	}

	// Method to determine whether it's an insert or an update
	public String checkInsertOrUpdate(IndividualOffMov movementData) {
		String commitNo = movementData.getCommitNo();
		String instNum = movementData.getInstNum();

		// Check if the last known location matches the current location
		String lastLocation = getLastLocation(commitNo, instNum);
		if (!movementData.getCountLocCodeFrom().equals(lastLocation)) {
			return "INSERT";
		}

		// Check if the arrival date is already set
		Timestamp arrivalDate = getArrivalDate(commitNo, instNum);
		if (arrivalDate == null) {
			return "UPDATE";
		}

		// No insert or update required
		return "NO_ACTION";
	}

	@Cacheable(value = "activityTypeCache", key = "#status")
	public List<Object[]> getActiveActivityTypes(String status) {
		return activityTypeRepository.findActiveActivityTypes(status);
	}

	@Cacheable(value = "buildingCache", key = "#instNum")
	public List<Object[]> getActiveLocationVals(String instNum) {
		return instLocationRepository.findActiveLocationVals(instNum);
	}

	public ResponseEntity<Map<String, Object>> executeCurrentLoc(String commitNo, String userId) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_INOM")
					.withProcedureName("SP_GET_CURR_LOC").withoutProcedureColumnMetaDataAccess() // Skip metadata lookup
					.declareParameters(new SqlParameter("IVC_COMMIT_NO", Types.VARCHAR),

							new SqlOutParameter("OVC_COUNT_LOC_CODE_FROM", Types.VARCHAR),
							new SqlOutParameter("OVC_COUNT_LOC_CODE_TO", Types.VARCHAR),
							new SqlOutParameter("OVC_DATE_TIME_OF_DEP", Types.DATE),
							new SqlOutParameter("OVC_DATE_TIME_OF_ARR", Types.DATE),
							new SqlParameter("p_user_id", Types.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("IVC_COMMIT_NO", commitNo)
					.addValue("p_user_id", userId);

			Map<String, Object> result = jdbcCall.execute(in);

			Map<String, Object> response = new HashMap<>();
			response.put("locFrom", result.get("OVC_COUNT_LOC_CODE_FROM"));
			response.put("locTo", result.get("OVC_COUNT_LOC_CODE_TO"));
			response.put("depDate", result.get("OVC_DATE_TIME_OF_DEP"));
			response.put("arrDate", result.get("OVC_DATE_TIME_OF_ARR"));

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error executing current location query: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("status",
					"An error occurred while processing your request. Please try again later."));
		}
	}

	public String getReservedLoc(String commitNo, String instNum) {
		System.out.println("instNum" + instNum);
		System.out.println("commitNo" + commitNo);
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_INOM")
					.withFunctionName("SF_GET_RESERVED_LOC").withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlParameter("P_COMMIT_NO", Types.VARCHAR),
							new SqlParameter("P_INST_NUM", Types.VARCHAR),
							new SqlOutParameter("return", Types.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("P_COMMIT_NO", commitNo).addValue("P_INST_NUM",
					instNum);

			// String reservedLoc = jdbcCall.executeFunction(String.class, in);
			Map<String, Object> result = jdbcCall.execute(in);
			// System.out.println("Result Set: " + result);
			// Extract the return value
			String reservedLoc = (String) result.get("return");
			return reservedLoc != null ? reservedLoc : "No reserved location found";

		} catch (Exception e) {
			logger.error("Error executing function SF_GET_RESERVED_LOC", e);
			throw new RuntimeException("Error retrieving reserved location", e);
		}

	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String, Object>> executeMovementDet(String commitNo, String userId) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_INOM")
					.withProcedureName("SP_QUERY_MOV_DET").withoutProcedureColumnMetaDataAccess()
					.declareParameters(new SqlOutParameter("IO_RESULTSET", Types.REF_CURSOR),
							new SqlParameter("IVC_COMMIT_NO", Types.VARCHAR),
					 new SqlParameter("ivc_user_id", Types.VARCHAR)
					);

			// System.out.println("userId "+userId);
			SqlParameterSource in = new MapSqlParameterSource().addValue("ivc_commit_no", commitNo)
			 .addValue("ivc_user_id", userId);

			Map<String, Object> result = jdbcCall.execute(in);

			List<IndividualOffMov> movementData = new ArrayList<>();
			List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("IO_RESULTSET");
			// System.out.println("Result Set: " + resultSet);
			if (resultSet != null) {
				for (Map<String, Object> rowData : resultSet) {
					IndividualOffMov individualOffMov = IndividualOffMov.movDetailQuery(rowData);
					movementData.add(individualOffMov);

				}
			}

			return ResponseEntity.ok(Collections.singletonMap("offenderMov", movementData));
		} catch (Exception e) {
			logger.error("Error executing movement query: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("status",
					"An error occurred while processing your request. Please try again later."));
		}
	}

	// Method to call procedure SP_MOV_DET_INSERT_OBJ
	@SuppressWarnings("deprecation")
	public ResponseEntity<Map<String, String>> callSpMovDetInsertObj(List<IndividualOffMov> movdetails)
			throws SQLException {
		try {
			logger.info("Calling stored procedure callSpMovDetInsertObj with input list size: {}", movdetails.size());

			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("spkg_omnet_wrapper")
					.withProcedureName("sp_mov_inom_det_insert").withoutProcedureColumnMetaDataAccess() // Skip metadata
																										// lookup
					.declareParameters(new SqlParameter("IO_DMLSET", OracleTypes.ARRAY, "TB_MOV_INOM_MOV_DET"));

			try (Connection conn = dataSource.getConnection()) {
				OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
				ARRAY sqlArray = createSqlArray(movdetails, oracleConn, true);
				Map<String, Object> inParams = Map.of("IO_DMLSET", sqlArray);
				logger.info("Input parameters for stored procedure callSpMovDetInsertObj: {}", inParams);

				simpleJdbcCall.execute(inParams);
			}
			return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
		} catch (Exception e) {
			logger.error("Error while calling stored procedure callSpMovDetInsertObj: ", e);
			throw new SQLException("Error inserting main movdetails objects ");
		}
	}

	@SuppressWarnings("deprecation")
	private ARRAY createSqlArray(List<IndividualOffMov> movdetails, OracleConnection oracleConn, boolean isInsert)
			throws SQLException {
		StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_MOV_INOM_MOV_DET", oracleConn);
		ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_MOV_INOM_MOV_DET", oracleConn);

		STRUCT[] structs = new STRUCT[movdetails.size()];

		for (int i = 0; i < movdetails.size(); i++) {
			IndividualOffMov movementDetails = movdetails.get(i);

			Timestamp depTimestamp = null;

			if (isInsert) {
				depTimestamp = Timestamp.valueOf(LocalDateTime.now());
			} else {
				depTimestamp = movementDetails.getDateTimeOfDep();
			}

			Timestamp arrTimestamp = null;
			String activity = movementDetails.getArrivedCheck();
			if ("Y".equals(activity)) {
				arrTimestamp = Timestamp.valueOf(LocalDateTime.now());
			}

			logger.info("Departure date and time: {}", depTimestamp);
			logger.info("Arrival date and time: {}", arrTimestamp);

			Object[] attributes = new Object[] { movementDetails.getCommitNo(), movementDetails.getInstNum(),
					depTimestamp, arrTimestamp, movementDetails.getCountLocCodeFrom(),
					movementDetails.getCountLocCodeTo(), movementDetails.getComments(),
					movementDetails.getActivityTypeCode(), movementDetails.getRgHousRelationship() };

			structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
		}

		return new ARRAY(arrayDescriptor, oracleConn, structs);
	}

	// Method to call procedure SP_MOV_DET_UPDATE
	@SuppressWarnings("deprecation")
	public ResponseEntity<Map<String, String>> callSpMovDetUpdateObj(List<IndividualOffMov> movdetails)
			throws SQLException {
		try {
			logger.info("Calling stored procedure callSpMovDetUpdateObj with input list size: {}", movdetails.size());

			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("spkg_omnet_wrapper")
					.withProcedureName("sp_mov_inom_det_update").withoutProcedureColumnMetaDataAccess() // Skip metadata
																										// lookup
					.declareParameters(new SqlParameter("IO_UPDATESET", OracleTypes.ARRAY, "TB_MOV_INOM_MOV_DET_UPD"));

			try (Connection conn = dataSource.getConnection()) {
				OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
				ARRAY sqlArray = createSqlArrayupdate(movdetails, oracleConn, false);
				Map<String, Object> inParams = Map.of("IO_UPDATESET", sqlArray);
				logger.info("Input parameters for stored procedure callSpMovDetUpdateObj: {}", inParams);

				simpleJdbcCall.execute(inParams);
			}
			return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
		} catch (Exception e) {
			logger.error("Error while calling stored procedure callSpMovDetInsertObj: ", e);
			throw new SQLException("Error inserting main movdetails objects ");
		}
	}
	@SuppressWarnings("deprecation")
	private ARRAY createSqlArrayupdate(List<IndividualOffMov> movdetails, OracleConnection oracleConn, boolean isInsert)
			throws SQLException {
		StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_MOV_INOM_MOV_DET_UPD", oracleConn);
		ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_MOV_INOM_MOV_DET_UPD", oracleConn);

		STRUCT[] structs = new STRUCT[movdetails.size()];

		for (int i = 0; i < movdetails.size(); i++) {
			IndividualOffMov movementDetails = movdetails.get(i);

			Timestamp depTimestamp = null;

			if (isInsert) {
				depTimestamp = Timestamp.valueOf(LocalDateTime.now());
			} else {
				depTimestamp = movementDetails.getDateTimeOfDep();
			}

			Timestamp arrTimestamp = null;
			String activity = movementDetails.getArrivedCheck();
			if ("Y".equals(activity)) {
				arrTimestamp = Timestamp.valueOf(LocalDateTime.now());
			}

			logger.info("Departure date and time: {}", depTimestamp);
			logger.info("Arrival date and time: {}", arrTimestamp);

			Object[] attributes = new Object[] { movementDetails.getCommitNo(), movementDetails.getInstNum(),
					depTimestamp, arrTimestamp, movementDetails.getCountLocCodeFrom(),
					movementDetails.getCountLocCodeTo(), movementDetails.getComments(),
					movementDetails.getActivityTypeCode(), movementDetails.getRgHousRelationship() };

			structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
		}

		return new ARRAY(arrayDescriptor, oracleConn, structs);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String, Object>> executeScheduledActivity(String commitNo) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("SPKG_MOV_INOM")
					.withProcedureName("SP_QUERY_INMATE_SCHEDULE").withoutProcedureColumnMetaDataAccess() // Skip
																											// metadata
																											// lookup
					.declareParameters(new SqlOutParameter("IO_RESULTSET", Types.REF_CURSOR),
							new SqlParameter("IVC_COMMIT_NO", Types.VARCHAR));

			SqlParameterSource in = new MapSqlParameterSource().addValue("IVC_COMMIT_NO", commitNo);

			Map<String, Object> result = jdbcCall.execute(in);

			List<IndividualOffMov.ScheduledActivity> scheduledActivities = new ArrayList<>();
			List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("IO_RESULTSET");
			System.out.println("resultSetschedule" + resultSet);
			if (resultSet != null) {
				for (Map<String, Object> rowData : resultSet) {
					IndividualOffMov.ScheduledActivity scheduledActivity = IndividualOffMov.ScheduledActivity
							.mapScheduledActivity(rowData);
					scheduledActivities.add(scheduledActivity);
				}
			}

			return ResponseEntity.ok(Collections.singletonMap("scheduledActivity", scheduledActivities));
		} catch (Exception e) {
			logger.error("Error executing scheduled activity query: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("status",
					"An error occurred while processing your request. Please try again later."));
		}
	}
}
