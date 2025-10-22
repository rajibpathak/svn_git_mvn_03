package com.omnet.cnt.Service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Document: EmergencyAttentionService.java
 * Author: Jamal Abraar
 * Date Created: 19-Nov-2024
 * Last Updated: 
 */

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

import com.omnet.cnt.classes.EmergencyAttention;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Service
public class EmergencyAttentionService {
private static final Logger logger = LoggerFactory.getLogger(EmergencyAttentionService.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	private DataSource dataSource;
	
	public Integer getNextEmrgSeqNum() {
        return jdbcTemplate.queryForObject("select max(EMERG_ATTN_SEQ_NUM)+1 from INMATE_EMERG_ATTN", Integer.class);
    }
	
	public ResponseEntity<Map<String, Object>> executeGetInmateDet(String commitNo) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CBI_EMGY")
                    .withProcedureName("SP_GET_INMT_DET")
                    .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
                    .declareParameters(
                            new SqlParameter("IVC_COMMIT_NO", Types.VARCHAR),
                            new SqlOutParameter("OVC_INMT_TP_DESC", Types.VARCHAR),
                            new SqlOutParameter("OVC_INST_NAME", Types.VARCHAR),
                            new SqlOutParameter("ODT_ADMISS_DATE", Types.VARCHAR),
                            new SqlOutParameter("OVC_ADMISS_TIME", Types.VARCHAR),
                            new SqlOutParameter("OVC_CURRENT_INST_NUM", Types.VARCHAR),
                            new SqlOutParameter("OVC_INMT_TP_CD", Types.VARCHAR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("IVC_COMMIT_NO", commitNo);
                    
            Map<String, Object> result = jdbcCall.execute(in);

	        Map<String, Object> response = new HashMap<>();
	        response.put("description", result.get("OVC_INMT_TP_DESC"));
	        response.put("instName", result.get("OVC_INST_NAME"));
	        response.put("admissDate", result.get("ODT_ADMISS_DATE"));
	        response.put("admissTime", result.get("OVC_ADMISS_TIME"));
	        response.put("currentInstNum", result.get("OVC_CURRENT_INST_NUM"));
	        response.put("tpCd", result.get("OVC_INMT_TP_CD"));
	        
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("Error executing emergency attention query: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
	    }
    }
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String, Object>> executeSpQuery(String commitNo, String emerSeqNum) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CBI_EMGY")
                    .withProcedureName("SP_QUERY")
                    .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
                    .declareParameters(
                            new SqlParameter("IVC_COMMIT_NO", Types.VARCHAR),
                            new SqlOutParameter("INU_EMERG_ATTN_SEQ_NUM", Types.VARCHAR),
                            new SqlOutParameter("RESULTSET", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("IVC_COMMIT_NO", commitNo)
                    .addValue("INU_EMERG_ATTN_SEQ_NUM", emerSeqNum);
                    
            Map<String, Object> result = jdbcCall.execute(in);

            List<EmergencyAttention> emergencyData = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("RESULTSET");
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
                	EmergencyAttention emergencyAttention = EmergencyAttention.emergencyAttention(rowData);
                	emergencyData.add(emergencyAttention);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("emergencyAttentionData", emergencyData));
        } catch (Exception e) {
            logger.error("Error executing emergency attention query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
	
	//Method to call procedure SP_INSERT_OMNET
	@SuppressWarnings("deprecation")
	public ResponseEntity<Map<String, String>> callSpInsertOmnet(List<EmergencyAttention> emergencyData) throws SQLException {
	    try {
	        logger.info("Calling stored procedure callSpInsertOmnet with input list size: {}", emergencyData.size());

	        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName("spkg_omnet_wrapper") // Catalog (package) name
	                .withProcedureName("SPKG_CBI_EMGY_INSERT") // Procedure name
	                .declareParameters(
	                        new SqlParameter("insert_Set", OracleTypes.ARRAY, "TB_CBI_EMGY")
	                );

	        SqlParameterSource in = new MapSqlParameterSource();

	        try (Connection conn = dataSource.getConnection()) {
	            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
	            
	            // Create the SQL array for the p_insert_set table-type parameter
	            ARRAY sqlArray = createSqlArray(emergencyData, oracleConn, true);
	            
	            // Add the table-type parameter to the input map
	            in = new MapSqlParameterSource()
	                    .addValue("insert_Set", sqlArray);  

	            Map<String, Object> result = simpleJdbcCall.execute(in);
	            logger.info("Stored procedure executed successfully, result: {}", result);

	        }
	        return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
	    } catch (Exception e) {
	        logger.error("Error while calling stored procedure callSpInsertOmnet: ", e);
	        throw new SQLException("Error inserting sp emergency details objects");
	    }
	}
	
	//Method to call procedure SP_INSERT_OMNET
	@SuppressWarnings("deprecation")
	public ResponseEntity<Map<String, String>> callSpUpdateOmnet(List<EmergencyAttention> emergencyData) throws SQLException {
	    try {
	        logger.info("Calling stored procedure callSpUpdateOmnet with input list size: {}", emergencyData.size());

	        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName("spkg_omnet_wrapper") 
	                .withProcedureName("SPKG_CBI_EMGY_UPDATE") 
	                .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
	                .declareParameters(
	                        new SqlParameter("UPDATE_SET", OracleTypes.ARRAY, "TB_CBI_EMGY_UPD")
	                );

	        SqlParameterSource in = new MapSqlParameterSource();

	        try (Connection conn = dataSource.getConnection()) {
	            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
	            
	            ARRAY sqlArray = createSqlArrayupd(emergencyData, oracleConn, true);
	            
	            in = new MapSqlParameterSource()
	                    .addValue("UPDATE_SET", sqlArray);  

	            Map<String, Object> result = simpleJdbcCall.execute(in);
	            logger.info("Stored procedure executed successfully, result: {}", result);

	        }
	        return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
	    } catch (Exception e) {
	        logger.error("Error while calling stored procedure callSpUpdateOmnet: ", e);
	        throw new SQLException("Error updating sp emergency details objects");
	    }
	}
	
    @SuppressWarnings("deprecation")
    private ARRAY createSqlArray(List<EmergencyAttention> emergencyData, OracleConnection oracleConn, boolean isInsert) throws SQLException {
        StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_CBI_EMGY", oracleConn);
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_CBI_EMGY", oracleConn);

        STRUCT[] structs = new STRUCT[emergencyData.size()];

        for (int i = 0; i < emergencyData.size(); i++) {
        	EmergencyAttention emergencyDetails = emergencyData.get(i);
       
            Object[] attributes = new Object[] {
            	emergencyDetails.getCommitNo(),
            	emergencyDetails.getEmergencySeqNum(),
            	emergencyDetails.getDateLogged(),
            	emergencyDetails.getImmediateAttnFlag(),
            	emergencyDetails.getMentalHealthFlag(),
            	emergencyDetails.getSuicideFlag(),
            	emergencyDetails.getThreatToStaff(),
            	emergencyDetails.getMedicalComments(),
            	emergencyDetails.getThreatComments()
            };

            structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
        }

        return new ARRAY(arrayDescriptor, oracleConn, structs);
    }
    
    
    @SuppressWarnings("deprecation")
    private ARRAY createSqlArrayupd(List<EmergencyAttention> emergencyData, OracleConnection oracleConn, boolean isInsert) throws SQLException {
        StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_CBI_EMGY_UPD", oracleConn);
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_CBI_EMGY_UPD", oracleConn);

        STRUCT[] structs = new STRUCT[emergencyData.size()];

        for (int i = 0; i < emergencyData.size(); i++) {
        	EmergencyAttention emergencyDetails = emergencyData.get(i);
       
            Object[] attributes = new Object[] {
            	emergencyDetails.getCommitNo(),
            	emergencyDetails.getEmergencySeqNum(),
            	emergencyDetails.getDateLogged(),
            	emergencyDetails.getImmediateAttnFlag(),
            	emergencyDetails.getMentalHealthFlag(),
            	emergencyDetails.getSuicideFlag(),
            	emergencyDetails.getThreatToStaff(),
            	emergencyDetails.getMedicalComments(),
            	emergencyDetails.getThreatComments()
            };

            structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
        }

        return new ARRAY(arrayDescriptor, oracleConn, structs);
    }

    
}

