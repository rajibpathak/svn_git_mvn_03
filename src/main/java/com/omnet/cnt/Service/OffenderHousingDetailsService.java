/**
 * Document: OffenderHousingDetailsService.java
 * Author: Jamal Abraar
 * Date Created: 16-Jul-2024
 * Last Updated: 
 */
package com.omnet.cnt.Service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.omnet.cnt.classes.HousingDetails;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;


@Service
public class OffenderHousingDetailsService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private DataSource dataSource;
	
	private static final Logger logger = LoggerFactory.getLogger(OffenderHousingDetailsService.class);
	
	//Method to call procedure SP_MOV_OHDH_QUERY
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> executeOHDHQuery(String commitNo) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_MOV_OHDH")
                    .withProcedureName("SP_MOV_OHDH_QUERY")
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(
                            new SqlParameter("ivc_commit_no", Types.VARCHAR),
                            new SqlOutParameter("resultset", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ivc_commit_no", commitNo);

            Map<String, Object> result = jdbcCall.execute(in);

            List<HousingDetails> housingDetailsList = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("resultset");
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
                	System.out.println("rowDatasefatCzx"+rowData);
                	HousingDetails housingDetails = HousingDetails.housingDetailsData(rowData);
                	System.out.println("rowDatasefatCzxdsk"+housingDetails);
                	housingDetailsList.add(housingDetails);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("housingDetails", housingDetailsList));
        } catch (Exception e) {
            logger.error("Error executing main query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "Error: " + e.getMessage()));
        }
    }
    
    //Method to call procedure SP_MOV_HIST_QUERY
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> executeMovHistQuery(String commitNo, String fromDate, String toDate) {
        try {
            // Convert String dates to java.sql.Date with the format MM/dd/yyyy
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date sqlFromDate = new Date(dateFormat.parse(fromDate).getTime());
            Date sqlToDate = new Date(dateFormat.parse(toDate).getTime());

            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_MOV_OHDH")
                    .withProcedureName("SP_MOV_HIST_QUERY")
                    .declareParameters(
                            new SqlParameter("IVC_COMMIT_NO", Types.VARCHAR),
                            new SqlParameter("IDT_FROM_DATE", Types.DATE),
                            new SqlParameter("IDT_TO_DATE", Types.DATE),
                            new SqlOutParameter("RESULTSET", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("IVC_COMMIT_NO", commitNo)
                    .addValue("IDT_FROM_DATE", sqlFromDate)
                    .addValue("IDT_TO_DATE", sqlToDate);

            Map<String, Object> result = jdbcCall.execute(in);

            List<HousingDetails> housingDetailsHistory = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("RESULTSET");
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
                    HousingDetails housingHistory = HousingDetails.housingDetailsHistory(rowData);
                    housingDetailsHistory.add(housingHistory);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("housingDetailsHistory", housingDetailsHistory));
        } catch (ParseException e) {
            logger.error("Error parsing date: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("status", "Error: Invalid date format"));
        } catch (Exception e) {
            logger.error("Error executing main query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
    
    @SuppressWarnings("deprecation")
    public ResponseEntity<Map<String, Object>> callSpMovEnemyQuery(String sbiNo) {   	
        try (Connection connection = dataSource.getConnection()) {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_MOV_OHDH")
                    .withProcedureName("SP_MOV_ENEMY_QUERY_OBJ")
                    .declareParameters(
                            new SqlParameter("IVC_SBI_NO", Types.VARCHAR),
                            new SqlOutParameter("IOTB_QUERY", OracleTypes.ARRAY, "TB_RE_ENEMY_DTLS")
                    );

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("IVC_SBI_NO", sbiNo);

            Map<String, Object> result = jdbcCall.execute(in);

            ARRAY array = (ARRAY) result.get("IOTB_QUERY");

            List<HousingDetails.ReEnemyDtls> enemyDetails = new ArrayList<>();
            if (array != null) {
                Object[] arrayData = (Object[]) array.getArray();
                for (Object obj : arrayData) {
                    if (obj instanceof STRUCT) {
                        STRUCT struct = (STRUCT) obj;
                        Object[] attributes = struct.getAttributes();
                        HousingDetails.ReEnemyDtls enemyDtls = new HousingDetails.ReEnemyDtls();
                        enemyDtls.setSbiNo((String) attributes[0]);
                        enemyDtls.setEnemyCommitNo((String) attributes[1]);
                        enemyDtls.setEnemyNameLast((String) attributes[2]);
                        enemyDtls.setEnemyNameFirst((String) attributes[3]);
                        enemyDtls.setCellDesc((String) attributes[4]);
                        enemyDtls.setClaimDesc((String) attributes[5]);
                        enemyDetails.add(enemyDtls);
                    }
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("enemyDetails", enemyDetails));
        } catch (SQLException e) {
        	logger.error("Error executing main query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
}
