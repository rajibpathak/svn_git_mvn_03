package com.omnet.cnt.Service;


import com.omnet.cnt.Model.Inmatechargebean;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.sql.*;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Service
public class InmateChargesService{
    private static final Logger logger = LoggerFactory.getLogger(InmateChargesService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
	
    @Autowired
    private DataSource dataSource;


    public void insertCommittedCharges(List<Inmatechargebean> chargesList) {
        for (Inmatechargebean charge : chargesList) {
            System.out.println("getChargeSeqNum: " + charge.getChargeSeqNum());
            if (charge.getChargeSeqNum() == null || charge.getChargeSeqNum() == 0) {
                String seqNum = getNextChargeSeqNum();
                charge.setChargeSeqNum(Integer.parseInt(seqNum));
                logger.info("Generated CHARGE_SEQ_NUM {} for COMMIT_NO {}", seqNum, charge.getCommitNo());
            }
        }
        callCommittedChargesProcedure("SPKG_SEN_DOCK.sp_insert_committed_charges", chargesList);
    }
    @SuppressWarnings("deprecation")
	public String getNextChargeSeqNum() {
        return jdbcTemplate.queryForObject( "SELECT Sf_Gen_Seq_Mdoc(?) FROM dual", 
            new Object[] { "0020" }, // Pass the parameter
            String.class
        );
    }

	// === UPDATE PROCEDURE ===
    public void updateCommittedCharges(List<Inmatechargebean> chargesList) {
        callCommittedChargesProcedure("SPKG_SEN_DOCK.sp_update_committed_charges", chargesList);
    }

    // === COMMON WRAPPER FOR CALLING PROCEDURES ===
    private void callCommittedChargesProcedure(String procedureName, List<Inmatechargebean> chargesList) {
        String procedureCall = "{call " + procedureName + "(?)}";
        System.out.println("requestData"+chargesList);
       
        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
            Struct[] structs = new Struct[chargesList.size()];

            for (int i = 0; i < chargesList.size(); i++) {
                Inmatechargebean charge = chargesList.get(i);
                structs[i] = oracleConnection.createStruct("TY_COMMITTED_CHARGES_INSUP", new Object[]{
                    charge.getCommitNo(),
                    charge.getCaseNum(),
                    charge.getChargeSeqNum(),
                    parseSqlDate(charge.getStartDate()),
                    parseSqlDate(charge.getEndDate()),
                    charge.getChargeDescription(),
                    charge.getActiveFlag(),
                    charge.getBailAmount(),
                    charge.getChargeNum(),
                    charge.getJudgeCode(),
                    charge.getCourtCode(),
                    charge.getChargeJurisdiction(),
                    charge.getStatueTitle(),
                    charge.getStatueSection(),
                    charge.getStatueSubsection(),
                    charge.getStatueType(),
                    charge.getStatueClass(),
                    charge.getComments(),
                    charge.getNcicCode(),
                    charge.getSentacCategory(),
                    charge.getDispositionCode(),
                    charge.getBailType(),
                    charge.getConsolidatedCraNum()
                });
            }

            Array oracleArray = oracleConnection.createOracleArray("TB_COMMITTED_CHARGES_INSUP", structs);
            callableStatement.setArray(1, oracleArray);
            callableStatement.execute();

            logger.info("Procedure {} executed successfully.", procedureName);

        } catch (SQLException e) {
            logger.error("Error executing procedure {}: {}", procedureName, e.getMessage(), e);
            throw new RuntimeException("Error executing stored procedure", e);
        }
    }
    public List<Map<String, Object>> getInmateChargesSearch(Map<String, String> requestData) {
    	SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
    		    .withCatalogName("SPKG_SEN_DOCK")  
    		    .withProcedureName("sp_committed_charges_query") 
    		    .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
            .declareParameters(
                new SqlParameter("ivc_commit_no", Types.VARCHAR),
                new SqlParameter("ivc_case_num", Types.VARCHAR),
                new SqlParameter("ivc_consolidated_case_num", Types.VARCHAR),
                new SqlParameter("ivc_consolidated_cra_num", Types.VARCHAR),
                new SqlParameter("ivc_court_code", Types.VARCHAR),
                new SqlParameter("ivc_judge_code", Types.VARCHAR),
                new SqlParameter("idt_date_from", Types.DATE),
                new SqlParameter("idt_date_to", Types.DATE),
                new SqlParameter("ivc_active_flag", Types.VARCHAR),
                new SqlParameter("ivc_disposition_code", Types.VARCHAR),
                new SqlOutParameter("o_result", OracleTypes.CURSOR, new ColumnMapRowMapper())
            );

        
        
    	java.sql.Date fromDate = parseCustomDate(requestData.get("fromdate"));
        java.sql.Date toDate = parseCustomDate(requestData.get("todate"));
        
        SqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ivc_commit_no", requestData.get("commitNo"))
                .addValue("ivc_case_num", requestData.get("casenum"))
                .addValue("ivc_consolidated_case_num", null)
                .addValue("ivc_consolidated_cra_num", requestData.get("cranum"))
                .addValue("ivc_court_code", requestData.get("code"))
                .addValue("ivc_judge_code", requestData.get("judge"))
                .addValue("idt_date_from", fromDate)
                .addValue("idt_date_to", toDate)
                .addValue("ivc_active_flag", requestData.get("active"))
                .addValue("ivc_disposition_code", requestData.get("disposition"));

        
       System.out.println("requestData"+inParams);
      //  Map<String, Object> result = jdbcCall.execute(inParams);
        Map<String, Object> result = jdbcCall.execute(inParams);
       
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("o_result");

      //  System.out.println("Number of rows returned: " + resultList+""+(resultList != null ? resultList.size() : 0));

        return resultList;
    }
    private java.sql.Date parseCustomDate(String dateStr) {
    	
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            SimpleDateFormat input = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH); // e.g., "05/28/2025"
            java.util.Date parsed = input.parse(dateStr);
            return new java.sql.Date(parsed.getTime()); // convert to java.sql.Date
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected MM/dd/yyyy (e.g., 05/28/2025)", e);
        }
    }
    // === DATE PARSING HELPER ===
    private java.sql.Date parseSqlDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Try ISO 8601
            Instant instant = Instant.parse(dateStr);
            return new java.sql.Date(instant.toEpochMilli());
        } catch (Exception isoEx) {
            try {
                // Try yyyy-MM-dd
                return java.sql.Date.valueOf(dateStr);
            } catch (IllegalArgumentException fallbackEx) {
                try {
                    // Try MM/dd/yyyy (from UI date picker)
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    format.setLenient(false); // optional strict parsing
                    java.util.Date parsed = format.parse(dateStr);
                    return new java.sql.Date(parsed.getTime());
                } catch (ParseException pe) {
                    logger.warn("Invalid date format: {}", dateStr);
                    return null;
                }
            }
        }
   
    }
}
