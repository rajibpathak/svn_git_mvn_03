package com.omnet.cnt.Service;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import com.omnet.cnt.Model.BookingDetail;
import com.omnet.cnt.Repository.ScreensRepository;

import oracle.jdbc.OracleTypes;

@Service
public class BookingService {
@Autowired
private ScreensRepository omnetScreensRepository;

	 private JdbcTemplate jdbcTemplate;
	    private SimpleJdbcCall simpleJdbcCall;

	    @Autowired
	    public BookingService(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	        this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName("SPKG_CBI_BKNG") // Replace with your package name
	                .withProcedureName("sp_bkng_query") // Replace with your procedure name
	                .withoutProcedureColumnMetaDataAccess()
	                .declareParameters(
	                        new SqlParameter("ivc_level_code", OracleTypes.VARCHAR),
	                        new SqlParameter("ivc_commit_no", OracleTypes.VARCHAR),
	                        new SqlOutParameter("resultset", OracleTypes.CURSOR)
	                );
	    }

	    public List<BookingDetail> callBookingQueryProcedure(String levelCode, String commitNo) {
	        // Input parameters for the stored procedure
	        Map<String, Object> inParams = Map.of(
	                "ivc_level_code", levelCode,
	                "ivc_commit_no", commitNo
	        );
	        Map<String, Object> outParams = simpleJdbcCall.execute(inParams);
	        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) outParams.get("resultset");
	        List<BookingDetail> bookingDetails = new ArrayList<>();
	        for (Map<String, Object> row : resultSet) {
	            BookingDetail bookingDetail = new BookingDetail();
	            bookingDetail.setCommitNo((String) row.get("COMMIT_NO"));
	            bookingDetail.setScreenCode((String) row.get("SCREEN_CODE"));
	            bookingDetail.setScreenName((String) row.get("SCREEN_NAME"));
	            bookingDetail.setUpdatedFlag((String) row.get("UPDATED_FLAG"));
	            bookingDetails.add(bookingDetail);
	        }

	        return bookingDetails;
	    }
	    
	    private SimpleJdbcCall createSimpleJdbcCall() {
	        return new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName("SPKG_CBI_BKNG")  
	                .withProcedureName("sp_update") 
	                .withoutProcedureColumnMetaDataAccess()
	                .declareParameters(
	                    new SqlParameter("ivc_commit_no", Types.VARCHAR),  
	                    new SqlParameter("ivc_scr_cd", Types.VARCHAR),
	                    new SqlParameter("ivc_flg", Types.VARCHAR)   
	                );
	    }


	    public void updateScreenDetails(String commitNo, String screenCode, String updatedFlag) {

	        MapSqlParameterSource parameters = new MapSqlParameterSource()
	            .addValue("ivc_commit_no", commitNo)
	            .addValue("ivc_scr_cd", screenCode)
	            .addValue("ivc_flg", updatedFlag);

	        SimpleJdbcCall simpleJdbcCall = createSimpleJdbcCall();
	        try {
	            simpleJdbcCall.execute(parameters);
	        } catch (Exception e) {	         
	            throw new RuntimeException("Error executing stored procedure sp_update: " + e.getMessage(), e);
	        }
	    }
	    
		/*
		 * public String getSntcLvlCd(String commitNo) { SimpleJdbcCall jdbcCall = new
		 * SimpleJdbcCall(jdbcTemplate) .withCatalogName("SPKG_CBI_BKNG")
		 * .withFunctionName("sf_get_sntc_lvl_cd") .declareParameters( new
		 * SqlParameter("ivc_commit_no",java.sql.Types.VARCHAR) );
		 * 
		 * SqlParameterSource params = new MapSqlParameterSource()
		 * .addValue("ivc_commit_no", commitNo);
		 * 
		 * System.out.println("Params for function: " + params);
		 * 
		 * try { System.out.println("hellow guys  "); String result =
		 * jdbcCall.executeFunction(String.class, params);
		 * System.out.println("hellow guys result"+result); return result; } catch
		 * (Exception e) { e.printStackTrace(); throw e; } }
		 */
	
	    
	    
	    public String getUrlForScreenCode(String screenCode) {
	        String url= omnetScreensRepository.findScreenUrlByScreenCode(screenCode);
	        System.out.println("url"+url);
	        return url;
	    }
	}