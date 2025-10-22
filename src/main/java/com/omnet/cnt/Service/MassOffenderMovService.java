	/**
	 * Document: MassOffenderMovService.java
	 * Author: Jamal Abraar
	 * Date Created: 03-Oct-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.omnet.cnt.classes.IndividualOffMov;
import com.omnet.cnt.classes.MassOffenderMovement;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;



@Service
public class MassOffenderMovService {
	private static final Logger logger = LoggerFactory.getLogger(MassOffenderMovService.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	private DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String, Object>> executeMassMovementDet(String logInOut,String countLocCode,String scheduledFlag, String instNum,String bldNum, String unitId,String floor,String tier,String cell,String bed,String queryType) {
		System.out.println("Gen 1");
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("spkg_omnet_wrapper")
                    .withProcedureName("SP_MOV_MOAM_MMV_QUERY")
                    .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
                    .declareParameters(
                            new SqlParameter("IVC_LOG_IN_OUT", Types.VARCHAR),
                            new SqlParameter("IVC_COUNT_LOC_CODE", Types.VARCHAR),
                            new SqlParameter("IVC_SCHD_FLAG", Types.VARCHAR),
                            new SqlParameter("P_INST_NUM", Types.VARCHAR),
                            new SqlParameter("P_BLD_NUM", Types.VARCHAR),
                            new SqlParameter("P_UNIT_ID", Types.VARCHAR),
                            new SqlParameter("P_FLOOR_NUM", Types.VARCHAR),
                            new SqlParameter("P_TIER_NUM", Types.VARCHAR),
                            new SqlParameter("P_CELL_NO", Types.VARCHAR),
                            new SqlParameter("P_BED_NO", Types.VARCHAR),
                            new SqlParameter("P_QUERY_TYPE", Types.VARCHAR),
                            new SqlOutParameter("RESULTSET", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("IVC_LOG_IN_OUT", logInOut)
                    .addValue("IVC_COUNT_LOC_CODE", countLocCode)
                    .addValue("IVC_SCHD_FLAG", scheduledFlag)
                    .addValue("P_INST_NUM", instNum)
                    .addValue("P_BLD_NUM", bldNum)
                    .addValue("P_UNIT_ID", unitId)
                    .addValue("P_FLOOR_NUM", floor)
                    .addValue("P_TIER_NUM", tier)
                    .addValue("P_CELL_NO", cell)
                    .addValue("P_BED_NO", bed)
                    .addValue("P_QUERY_TYPE", queryType);
                    
            Map<String, Object> result = jdbcCall.execute(in);

            List<MassOffenderMovement> movementData = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("RESULTSET");
            if (resultSet != null) { for (Map<String, Object> rowData : resultSet) { System.out.println("Row Data: " + rowData); } }
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
                    MassOffenderMovement massOffenderMovement = MassOffenderMovement.massOffenderMovement(rowData);
                    movementData.add(massOffenderMovement);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("massOffenderMov", movementData));
        } catch (Exception e) {
            logger.error("Error executing mass offender movement query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
	
	//Method to call procedure SP_MOV_DET_INSERT_OBJ
	@SuppressWarnings("deprecation")
	public ResponseEntity<Map<String, String>> callSpMovMmvUpdateObj(String logInOut, String countLocCode, String movType,List<MassOffenderMovement> movdetails) throws SQLException {
	    try {
	        logger.info("Calling stored procedure callSpMovMmvUpdateObj with input list size: {}", movdetails.size());

	        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName("SPKG_OMNET_WRAPPER") // Catalog (package) name
	                .withProcedureName("SP_MOV_MOAM_MMV_UPDATE") // Procedure name
	                .declareParameters(
	                        new SqlParameter("IVC_LOG_IN_OUT", Types.VARCHAR),
	                        new SqlParameter("IVC_COUNT_LOC_CODE", Types.VARCHAR),
	                        new SqlParameter("P_MOV_TYPE", Types.VARCHAR),
	                        new SqlParameter("UPDATESET", OracleTypes.ARRAY, "TB_MOV_MOAM_DET")
	                );

	        SqlParameterSource in = new MapSqlParameterSource()
	                .addValue("IVC_LOG_IN_OUT", logInOut)
	                .addValue("IVC_COUNT_LOC_CODE", countLocCode)
	                .addValue("P_MOV_TYPE", movType);

	        try (Connection conn = dataSource.getConnection()) {
	            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
	            
	            // Create the SQL array for the UPDATESET table-type parameter
	            ARRAY sqlArray = createSqlArray(movdetails, oracleConn, true);
	            
	            // Add the table-type parameter to the input map
	            in = new MapSqlParameterSource()
	                    .addValue("IVC_LOG_IN_OUT", logInOut)
	                    .addValue("IVC_COUNT_LOC_CODE", countLocCode)
	                    .addValue("P_MOV_TYPE", movType)
	                    .addValue("UPDATESET", sqlArray);  

	            Map<String, Object> result = simpleJdbcCall.execute(in);
	            logger.info("Stored procedure executed successfully, result: {}", result);

	        }
	        return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
	    } catch (Exception e) {
	        logger.error("Error while calling stored procedure callSpMovDetInsertObj: ", e);
	        throw new SQLException("Error inserting main movdetails objects");
	    }
	}

    
    @SuppressWarnings("deprecation")
    private ARRAY createSqlArray(List<MassOffenderMovement> movdetails, OracleConnection oracleConn, boolean isInsert) throws SQLException {
        StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_MOV_MOAM_DET", oracleConn);
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_MOV_MOAM_DET", oracleConn);

        STRUCT[] structs = new STRUCT[movdetails.size()];

        for (int i = 0; i < movdetails.size(); i++) {
            MassOffenderMovement movementDetails = movdetails.get(i);
            
            System.out.println("xyz : " + movementDetails.getInstNum() + " | " + movementDetails.getCountLocCodeFrom() + " | " + movementDetails.getCountLocCodeTo());
          // logger.info("MassOffenderMovement [{}] - countLocCodeFrom: {}", i, movementDetails.getCountLocCodeFrom());
            Object[] attributes = new Object[] {
            	movementDetails.getMovRowid(),
            	movementDetails.getInstNum(),
            	movementDetails.getCommitNo(),
            	movementDetails.getSbiMstSbiNo(),
            	movementDetails.getLastName(),
            	movementDetails.getFirstName(),
            	movementDetails.getMidName(),
            	movementDetails.getSurName(),
            	movementDetails.getDateTimeOfDep(),
            	movementDetails.getDateTimeOfArr(),
            	movementDetails.getCountLocCodeFrom(),
            	movementDetails.getCountLocCodeTo(),
            	movementDetails.getComments(),
            	movementDetails.getInmateMoved(),
            	movementDetails.getActivityTypeDesc(),
            	movementDetails.getHouLocDesc(),
            	movementDetails.getRgHouRel()
            };

            structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
        }

        return new ARRAY(arrayDescriptor, oracleConn, structs);
    }
}
