package com.omnet.cnt.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.omnet.cnt.classes.SentenceLevelDetails;
import com.omnet.cnt.classes.SentenceOrderDetails;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;

@Service
public class SentenceCalc {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyTrackingService.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private DataSource dataSource;
	
	public String getCommitNo(String sbiNo) {
		String sql = "select Sf_get_commit_no(?) from dual";
	    String commitNo = jdbcTemplate.queryForObject(sql, new Object[]{sbiNo}, String.class);
	    return commitNo;
	}
	
	public List<Map<String, Object>> getSentenceType() {
		String sql = "SELECT SENTENCE_TYPE_CODE,SENTENCE_TYPE_DESC FROM sentence_type_mst where STATUS='A' ORDER BY SENTENCE_TYPE_DESC";
		List<Map<String, Object>> sentenceType = jdbcTemplate.queryForList(sql);
		return sentenceType;
	}
	
	public List<Map<String, Object>> getSentenceTo() {
		String sql = "SELECT SNTC_LVL_CD,SNTC_LVL_DESC FROM sentence_level_mst  ORDER BY SNTC_LVL_DESC";
		List<Map<String, Object>> sentenceTo = jdbcTemplate.queryForList(sql);
		return sentenceTo;
	}
	public List<Map<String, Object>> getHeldAt() {
		String sql = "SELECT sntc_lvl_desc, sntc_lvl_cd FROM sentence_level_mst where sntc_lvl_cd not in ('0','1R')order by decode(sntc_lvl_cd,'4H','3.5',sntc_lvl_cd) asc";
		List<Map<String, Object>> heltAt = jdbcTemplate.queryForList(sql);
		return heltAt;
	}
	
	public List<Map<String, Object>> getReasons() {
		String sql = "Select Ref_value_code,Ref_value_desc from cm_reference_values where REF_CATEGORY_CODE='ORDR_MODIF' Order by Ref_value_desc";
		List<Map<String, Object>> reasons = jdbcTemplate.queryForList(sql);
		return reasons;
	}
	
	public Integer getNewSohSeqNum() {
		return jdbcTemplate.queryForObject("select SF_GEN_SEQ_MDOC('0500') from dual", Integer.class);
	}
	
	public List<Map<String, Object>> getConditions(){
		String sql = """
				SELECT ref_value_desc condition, ref_value_code conditionCode
		       FROM cm_reference_values 
		       where ref_category_module = 'SEN' 
		       and ref_category_code   = 'COMP_COND'
		       and nvl(other_value2,'*') <> 'Y'
		       ORDER BY ref_value_desc
				""";
		List<Map<String, Object>> conditions = jdbcTemplate.queryForList(sql);
		return conditions;
	}
	
	public List<Map<String, Object>> getTreatments(){
		String sql = """
				SELECT ref_value_desc as treatment, ref_value_code as treatmentCode
				FROM cm_reference_values
                where ref_category_module = 'SEN'
                and ref_category_code = 'COND_PROG'
                ORDER BY ref_value_desc
				""";
		List<Map<String, Object>> treatments = jdbcTemplate.queryForList(sql);
		return treatments;
	}
	
	public Map<String, Object> getSentenceOrder(String commitNo, String sohSeqNum, String sohCaseNum, String sohCraNum, String instNum) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withCatalogName("SPKG_SEN_ORDR")
		             .withProcedureName("sp_sentence_order_qry")
		             .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
		             .declareParameters(
		            		 new SqlOutParameter("resultset", Types.REF_CURSOR),
		                     new SqlParameter("p_commit_no", Types.VARCHAR),
		                     new SqlParameter("p_soh_seq_num", Types.VARCHAR),
		                     new SqlParameter("p_soh_case_num", Types.VARCHAR),
		                     new SqlParameter("p_soh_cra_num", Types.VARCHAR),
		                     new SqlParameter("p_inst_num", Types.VARCHAR)
		                     );
			Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("p_commit_no", commitNo);//"0047599");
	        inputParams.put("p_soh_seq_num", sohSeqNum);//"116");
	        inputParams.put("p_soh_case_num", sohCaseNum);//"9208014601");
	        inputParams.put("p_soh_cra_num", sohCraNum);//"9208014601001");
	        inputParams.put("p_inst_num", instNum);//"02");
	        //System.out.println("inputParamscheck PropTrk:: "+inputParams);
	        
	        Map<String, Object> result = jdbcCall.execute(inputParams);
	        return result;
		} catch (Exception ex) {
	        // Log and rethrow so the controller can handle it
	        System.err.println("Database call failed. Property Tracking Get Header: " + ex.getMessage());
	        ex.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure", ex);
	    }
	}
	
	public List<Map<String, Object>> getSentenceOrderDetails2(String commitNo) {
		String query = "select soh_seq_num soh_seq_num, commit_no commit_no, soh_cal_seq_num cal_seq_num, soh_case_num case_num, soh_cra_num cra_num, soh_charge_num charge_num,\r\n"
				+ "                        soh_charge_desc  charge_desc, soh_sntc_lvl_cd sentence_to, soh_sntc_tp_cd sentence_type, NVL(soh_tis_ntis,'D') tis_ntis,\r\n"
				+ "                        soh_eff_date effective_date, order_type, aprv_flag from sentence_order_hdr where commit_no = '"+commitNo+"' ";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}
	
	public Map<String, Object> getSentenceOrderModifyDetails(String commitNo) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withCatalogName("SPKG_SEN_ORDR")
		             .withProcedureName("sp_modify_chrg_query")
		             .withoutProcedureColumnMetaDataAccess()
		             .declareParameters(
		            		 new SqlOutParameter("resultset", Types.REF_CURSOR),
		                     new SqlParameter("p_commit_no", Types.VARCHAR),
		                     new SqlParameter("ivc_manual_flag", Types.VARCHAR)
		                     );
			
			Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("p_commit_no", commitNo);//"0047599");
	        inputParams.put("ivc_manual_flag", "N");
	        
	        Map<String, Object> result = jdbcCall.execute(inputParams);
	        return result;
			
		} catch (Exception ex) {
	        // Log and rethrow so the controller can handle it
	        System.err.println("Database call failed. Property Tracking Get Header: " + ex.getMessage());
	        ex.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure", ex);
	    }
	}
	
	public List<SentenceOrderDetails> getNewChargesList(String commitNo) {
		String query = "select commit_no, case_num, charge_num, charge_seq_num, charge_description, charge_jurisdiction, statue_title, STATUE_SUBSECTION, statue_section, statue_type, STATUE_CLASS \r\n"
				+ "from INMATE_COMMITTED_CHARGES WHERE COMMIT_NO = '"+commitNo+"' ";
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		List<SentenceOrderDetails> newOrderList = new ArrayList<>();
		
		for (Map<String, Object> row : result) {
			SentenceOrderDetails orderDetail = new SentenceOrderDetails();
			
			orderDetail.setCommitNo((String) row.get("COMMIT_NO")); 
		    orderDetail.setCaseNum((String) row.get("CASE_NUM"));
		    orderDetail.setChargeNum((String) row.get("CHARGE_NUM"));
		    orderDetail.setSeqNum((String) row.get("CHARGE_SEQ_NUM"));
		    orderDetail.setChargeDesc((String) row.get("CHARGE_DESCRIPTION"));
		    
		    orderDetail.setSohStatuteJurisdiction((String) row.get("CHARGE_JURISDICTION"));
            orderDetail.setSohStatuteTitle((String) row.get("STATUE_TITLE"));
            orderDetail.setSohStatuteSection((String) row.get("STATUE_SECTION"));
            orderDetail.setSohStatuteSubsection((String) row.get("STATUE_SUBSECTION"));
            orderDetail.setSohStatuteType((String) row.get("STATUE_TYPE"));
            orderDetail.setSohStatuteClass((String) row.get("STATUE_CLASS"));

		    newOrderList.add(orderDetail);
		}
		return newOrderList;
	}
	
	@SuppressWarnings("deprecation")
	public List<SentenceOrderDetails> getSentenceOrderViewDetails(String commitNo) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		try (Connection conn = dataSource.getConnection()) {
			
			//String sql = "{call spkg_omnet_wrapper.sp_send_ordr_view_order_query(?, ?, ?)}";
			CallableStatement callableStatement = conn.prepareCall("{call spkg_omnet_wrapper.sp_send_ordr_view_order_query(?, ?, ?)}");
			
			/*String query = "SELECT  'N', hdr.commit_no, hdr.soh_seq_num, hdr.soh_cal_seq_num, hdr.soh_sntc_lvl_seq_num, hdr.soh_sntc_ord_seq_num, hdr.order_type, DECODE(hdr.order_type,'NEW_ORDER',decode(hdr.soh_modification_type,null,'New Order',\r\n"
					+ " decode(hdr.ref_soh_seq_num,null,'New Order','Modified Order')), 'MODIFY','Modified Order', 'RELEASE_CURRENT','Release Level', 'RELEASE_CHARGE','Release Charge', 'DELETE_CHARGE','Delete Charge',\r\n"
					+ " 'FUTURE_CALC','Future Level Calculation', 'CONDRELEASE','Conditional Release', 'PAROLERELEASE','Parole Release', 'CONDVIOLATION','Conditional Release Violation',\r\n"
					+ " 'PAROLEVIOLATION','Parole Violation', 'RECALCULATE','Recalculate Order', 'MANUAL','Manual Order', 'RELEASE_FUTURE','Release Future level', 'FUTURE','Future Level',\r\n"
					+ " 'OLD_SENT_CALC','Manually Entered from Old Screen', 'DELETE_DUP', 'Delete Duplicate (system generated)', hdr.order_type) order_type_desc, hdr.soh_case_num,\r\n"
					+ "    hdr.soh_charge_desc, hdr.soh_cra_num, hdr.soh_charge_num, hdr.court_code, hdr.judge_code, hdr.soh_offense_date, hdr.soh_ncic_code, hdr.soh_sent_date, hdr.soh_sntc_lvl_cd,\r\n"
					+ "    hdr.soh_sen_len_yy, hdr.soh_sen_len_mm, hdr.soh_sen_len_dd, hdr.soh_eff_date, hdr.soh_held_at_lvl, hdr.soh_jail_credit_yy, hdr.soh_jail_credit_mm, hdr.soh_jail_credit_dd,\r\n"
					+ "    hdr.soh_min_mand_yy, hdr.soh_min_mand_mm, hdr.soh_min_mand_dd, hdr.soh_abs_len_yy, hdr.soh_abs_len_mm, hdr.soh_abs_len_dd , hdr.soh_deferred_flag, hdr.soh_deferred_date,\r\n"
					+ "    hdr.soh_tis_ntis, hdr.soh_sntc_tp_cd, hdr.flag_4204k , hdr.soh_statute_jurisdiction, hdr.soh_statute_title, hdr.soh_statute_section , hdr.soh_statute_subsection,\r\n"
					+ "    hdr.soh_statute_type, hdr.soh_statute_class, hdr.soh_sentence_category, hdr.soh_deferred_inst_num, hdr.soh_weekender_days , hdr.soh_indefinite_flag , hdr.soh_gdtm_calc_flag , hdr.soh_merit_days,\r\n"
					+ "    hdr.soh_release_date, hdr.soh_release_time, hdr.eligible_fr_parole_flag, hdr.soh_status , hdr.aprv_flag  , nvl(hdr.aprv_user_id,hdr.inserted_userid) aprv_user_id, nvl(hdr.aprv_date,hdr.inserted_date_time) aprv_date,\r\n"
					+ "    nvl(hdr.aprv_time,to_char(hdr.inserted_date_time,'HH24:MI')) aprv_time , hdr.aprv_reason_code, hdr.aprv_user_comnts , hdr.sprv_decision, hdr.sprv_user_id , hdr.sprv_date  ,\r\n"
					+ "    hdr.sprv_time  , hdr.sprv_user_comnts, hdr.ref_soh_seq_num, hdr.soh_modification_type, hdr.sent_new_flag, hdr.soh_release_comnts, hdr.soh_release_by  , hdr.soh_release_method,\r\n"
					+ "    hdr.soh_admiss_method, hdr.soh_cancel_flag, hdr.soh_cancel_by, hdr.soh_cancel_date, hdr.soh_cancel_time, hdr.soh_cancel_comments, aprv_flag \r\n"
					+ "    FROM   sentence_order_hdr hdr WHERE  hdr.commit_no = '"+commitNo+"' AND   Order_Type not in ('DELETE_DUP')\r\n"
					+ "    ORDER BY to_date( (to_char(nvl(hdr.aprv_date,trunc(hdr.inserted_date_time)),'MM/DD/RRRR')||' '||\r\n"
					+ "    nvl(hdr.aprv_time,to_char(hdr.inserted_date_time,'HH24:MI')) ),'MM/DD/RRRR HH24:MI') desc, DECODE(soh_status,NULL,'1', 'PN','2', 'CA','3', 'DF','4', 'CL','5','6'), soh_seq_num desc";
			
			List<Map<String, Object>> result = jdbcTemplate.queryForList(query);*/
			
			callableStatement.registerOutParameter(1, OracleTypes.ARRAY, "TB_SEN_ORDR_VIEW_ORDER_QUERY");
			callableStatement.setString(2, commitNo);
			callableStatement.setString(3, "N");
			callableStatement.execute();
			
			Array array = (Array)callableStatement.getArray(1);
			Object[] arrayData = (Object[]) array.getArray();
			List<SentenceOrderDetails> viewOrderList = new ArrayList<>();
			
			for (Object obj : arrayData) {
				
				if (obj instanceof STRUCT) {
					STRUCT struct = (STRUCT) obj;
	                Object[] attributes = struct.getAttributes();
	                SentenceOrderDetails orderDetail = new SentenceOrderDetails();
	                
	                int i = 0;
	                System.out.println("blabla");
	                orderDetail.setN((String) attributes[i++]);
	                orderDetail.setCommitNo((String) attributes[i++]);
	                orderDetail.setSohSeqNum((BigDecimal) attributes[i++]);
	                orderDetail.setSohCalSeqNum((BigDecimal) attributes[i++]);
	                orderDetail.setSohSntcLvlSeqNum((BigDecimal) attributes[i++]);
	                orderDetail.setSohSntcOrdSeqNum((BigDecimal) attributes[i++]);
	                System.out.println("0");
	                orderDetail.setOrderType((String) attributes[i++]);
	                orderDetail.setOrderTypeDesc((String) attributes[i++]);
	                System.out.println("1");
	                orderDetail.setCaseNum((String) attributes[i++]);
	                System.out.println("2");
	                orderDetail.setChargeDesc((String) attributes[i++]);
	                System.out.println("3");
	                orderDetail.setCraNum((String) attributes[i++]);
	                System.out.println("4");
	                orderDetail.setChargeNum((String) attributes[i++]);
	                System.out.println("5");
	                orderDetail.setCourtCode((String) attributes[i++]);
	                System.out.println("6");
	                orderDetail.setJudgeCode((String) attributes[i++]);
	                System.out.println("7");
	                orderDetail.setOffenseDate((Date) attributes[i++]);
	                System.out.println("8");
	                orderDetail.setNcicCode((String) attributes[i++]);
	                System.out.println("9");
	                
	                System.out.println("10");
	                
	                System.out.println("11");
	                orderDetail.setSohSentDate((Date) attributes[i++]);
	                System.out.println("12");
	                orderDetail.setSohSntcLvlCd((String) attributes[i++]);
	                
	                orderDetail.setLengthYy((BigDecimal) attributes[i++]);
	                
	                orderDetail.setLengthMm((BigDecimal) attributes[i++]);
	                orderDetail.setLengthDd((BigDecimal) attributes[i++]);
	                orderDetail.setEffectiveDate((Date) attributes[i++]);
	                orderDetail.setHeldAt((String) attributes[i++]);
	                orderDetail.setJailCreditYy((BigDecimal) attributes[i++]);
	                orderDetail.setJailCreditMm((BigDecimal) attributes[i++]);
	                orderDetail.setJailCreditDd((BigDecimal) attributes[i++]);
	                orderDetail.setMinMandYy((BigDecimal) attributes[i++]);
	                orderDetail.setMinMandMm((BigDecimal) attributes[i++]);
	                orderDetail.setMinMandDd((BigDecimal) attributes[i++]);
	                orderDetail.setAbsLenYy((BigDecimal) attributes[i++]);
	                orderDetail.setAbsLenMm((BigDecimal) attributes[i++]);
	                orderDetail.setAbsLenDd((BigDecimal) attributes[i++]);
	                System.out.println("13");
	                orderDetail.setDeferredFlag((String) attributes[i++]);
	                orderDetail.setDeferredDate((Date) attributes[i++]);
	                orderDetail.setTisNtis((String) attributes[i++]);
	                orderDetail.setSentenceTypeCd((String) attributes[i++]);
	                System.out.println("14");
	                orderDetail.setFlag4204k((String) attributes[i++]);
	                
	                orderDetail.setSohStatuteJurisdiction((String) attributes[i++]);
	                orderDetail.setSohStatuteTitle((String) attributes[i++]);
	                orderDetail.setSohStatuteSection((String) attributes[i++]);
	                orderDetail.setSohStatuteSubsection((String) attributes[i++]);
	                orderDetail.setSohStatuteType((String) attributes[i++]);
	                orderDetail.setSohStatuteClass((String) attributes[i++]);
	                orderDetail.setSohSentenceCategory((String) attributes[i++]);
	                
	                orderDetail.setDeferredInstNum((String) attributes[i++]);
	                orderDetail.setSohWeekenderDays((BigDecimal) attributes[i++]);
	                System.out.println("15");
	                orderDetail.setSohIndefiniteFlag((String) attributes[i++]);
	                System.out.println("16");
	                orderDetail.setSohGdtmCalcFlag((String) attributes[i++]);
	                orderDetail.setSohMeritDays((BigDecimal) attributes[i++]);
	                orderDetail.setSohReleaseDate((Date) attributes[i++]);
	                orderDetail.setSohReleaseTime((String) attributes[i++]);
	                System.out.println("17");
	                orderDetail.setEligibleFrParoleFlag((String) attributes[i++]);
	                orderDetail.setSohStatus((String) attributes[i++]);
	                System.out.println("18");
	                orderDetail.setAprvFlag((String) attributes[i++]);
	                orderDetail.setAprvUserId((String) attributes[i++]);
	                orderDetail.setAprvDate((Date) attributes[i++]);
	                orderDetail.setAprvTime((String) attributes[i++]);
	                orderDetail.setAprvReasonCode((String) attributes[i++]);
	                orderDetail.setAprvUserComnts((String) attributes[i++]);
	                
	                orderDetail.setSprvDecision((String) attributes[i++]);
	                orderDetail.setSprvUserId((String) attributes[i++]);
	                orderDetail.setSprvDate((Date) attributes[i++]);
	                orderDetail.setSprvTime((String) attributes[i++]);
	                orderDetail.setSprvUserComnts((String) attributes[i++]);
	                orderDetail.setRefSohSeqNum((BigDecimal) attributes[i++]);
	                orderDetail.setSohModificationType((String) attributes[i++]);
	                orderDetail.setSentNewFlag((String) attributes[i++]);
	                orderDetail.setSohReleaseComnts((String) attributes[i++]);
	                orderDetail.setSohReleaseBy((String) attributes[i++]);
	                orderDetail.setSohReleaseMethod((String) attributes[i++]);
	                orderDetail.setSohAdmissMethod((String) attributes[i++]);
	                
	                orderDetail.setSohCancelFlag((String) attributes[i++]);
	                orderDetail.setSohCancelBy((String) attributes[i++]);
	                orderDetail.setSohCancelDate((Date) attributes[i++]);
	                orderDetail.setSohCancelTime((String) attributes[i++]);
	                orderDetail.setSohCancelComnts((String) attributes[i++]);// only 500 chars
	                
	                viewOrderList.add(orderDetail);
                }
			}
			
			callableStatement.close();
				
	        return viewOrderList;
			
		}catch (SQLException e) {
	        String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown SQL error";
	        System.err.println("Error: " + errorMessage);
	        e.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	    }
	}
	
	public Map<String, Object> getSentenceLevelDetails(String sohSeqNum) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withCatalogName("SPKG_SEN_ORDR")
		             .withProcedureName("sp_sntc_comp_cond_qry")
		             .withoutProcedureColumnMetaDataAccess()
		             .declareParameters(
		            		 new SqlOutParameter("resultset", Types.REF_CURSOR),
		                     new SqlParameter("p_soh_seq_num", Types.VARCHAR)
		                     );
			
			Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("p_soh_seq_num", sohSeqNum);
	        
	        Map<String, Object> result = jdbcCall.execute(inputParams);
	        return result;
			
		} catch (Exception ex) {
	        // Log and rethrow so the controller can handle it
	        System.err.println("Database call failed. Sentence Calc get level details: " + ex.getMessage());
	        ex.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure", ex);
	    }
	}
	
	public void updateSentenceOrder(List<SentenceOrderDetails> sntcOrders){
		System.out.println("=== Starting updateSentenceOrder ===");
		System.out.println("Number of sentence orders to process: " + sntcOrders.size());
		
		String procedureCall = "{call spkg_omnet_wrapper.sp_sen_ordr_sentence_order_Update(?)}";
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			
			System.out.println("Database connection established successfully");
			
			try {
    			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
    			Struct[] structs = new Struct[sntcOrders.size()];
    			System.out.println("Creating Oracle structs for " + sntcOrders.size() + " sentence orders");
    			
    			for (int i = 0; i < sntcOrders.size(); i++) {
    				SentenceOrderDetails ordr = sntcOrders.get(i);
    				System.out.println("\n--- Processing sentence order " + (i + 1) + " of " + sntcOrders.size() + " ---");
    				System.out.println("Commit No: " + ordr.getCommitNo());
    				System.out.println("SOH Seq Num: " + ordr.getSohSeqNum());
    				
    				try {
    					// Create struct with proper field mapping according to Oracle type definition
    					Object[] structData = createStructData(ordr, i + 1);
    					structs[i] = oracleConnection.createStruct("TY_SEN_ORDR_INSUPD", structData);
    					System.out.println("Successfully created struct for sentence order " + (i + 1));
    					
    				} catch (Exception e) {
    					System.err.println("ERROR: Failed to create struct for sentence order " + (i + 1));
    					System.err.println("Commit No: " + ordr.getCommitNo());
    					System.err.println("Error: " + e.getMessage());
    					e.printStackTrace();
    					throw new RuntimeException("Failed to create struct for sentence order " + (i + 1), e);
    				}
    			}
    			
    			System.out.println("All structs created successfully");
    			System.out.println("Creating Oracle array...");
    			Array array = oracleConnection.createOracleArray("TB_SEN_ORDR_INSUPD", structs);
    			System.out.println("Oracle array created successfully");
    			//System.out.println("Oracle Array as JSON-like:\n" +oracleArrayToJsonLike(array, SentenceOrderDetails.class));
    			
    			System.out.println("Setting array parameter for stored procedure...");
    			callableStatement.setArray(1, array);
    			System.out.println("Array parameter set successfully");
    			
    			System.out.println("Executing stored procedure...");
                boolean hasResults = callableStatement.execute();
                System.out.println("Stored procedure executed successfully");
                System.out.println("Has results: " + hasResults);
                
			} catch (Exception e) {
	            System.err.println("=== ERROR in updateSentenceOrder ===");
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error message: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	                
	                // Specific handling for ORA-17059
	                if (sqlEx.getErrorCode() == 17059) {
	                	System.err.println("ORA-17059: Failed to convert to internal representation");
	                	System.err.println("This typically indicates a data type mismatch between Java and Oracle types");
	                	System.err.println("Check the field types and values being passed to the Oracle struct");
	                }
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
			
		} catch (SQLException e) {
			System.err.println("=== DATABASE CONNECTION ERROR ===");
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        System.err.println("SQL State: " + e.getSQLState());
	        System.err.println("Error Code: " + e.getErrorCode());
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
	    }
		
		System.out.println("=== updateSentenceOrder completed successfully ===");
	}
	
	public List<BigDecimal> insertSentenceOrder(List<SentenceOrderDetails> sntcOrders) {
		System.out.println("=== Starting insertSentenceOrder ===");
		System.out.println("Number of sentence orders to process: " + sntcOrders.size());
		String procedureCall = "{call spkg_omnet_wrapper.sp_sen_ordr_sentence_order_insert(?)}";
		
		// List to store generated sequence numbers
		List<BigDecimal> generatedSeqNums = new ArrayList<>();
		
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			System.out.println("Database connection established successfully");
			
			try {
    			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
    			Struct[] structs = new Struct[sntcOrders.size()];
    			System.out.println("Creating Oracle structs for " + sntcOrders.size() + " sentence orders");
    			
    			for (int i = 0; i < sntcOrders.size(); i++) {
    				SentenceOrderDetails ordr = sntcOrders.get(i);
    				Integer newSohSeq = getNewSohSeqNum();
    				System.out.println("\n--- Processing sentence order " + (i + 1) + " of " + sntcOrders.size() + " ---");
    				try {
    					// Create struct with proper field mapping according to Oracle type definition
    					BigDecimal sohSeqNum = convertToBigDecimal(newSohSeq, "SOh_SEQ_NUM", i);
    					ordr.setSohSeqNum(sohSeqNum);
    					ordr.setSprvDecision("D");
    					System.out.println("New SOH_SEQ_NUM generated for inserting Sentence: "+ ordr.getSohSeqNum());
    					
    					// Store the generated sequence number
    					generatedSeqNums.add(sohSeqNum);
    					
    					Object[] structData = createStructData(ordr, i + 1);
    					structs[i] = oracleConnection.createStruct("TY_SEN_ORDR_INSUPD", structData);
    					System.out.println("Successfully created struct for sentence order " + (i + 1));
    					
    				} catch (Exception e) {
    					System.err.println("ERROR: Failed to create struct for sentence order " + (i + 1));
    					System.err.println("Commit No: " + ordr.getCommitNo());
    					System.err.println("Error: " + e.getMessage());
    					e.printStackTrace();
    					throw new RuntimeException("Failed to create struct for sentence order " + (i + 1), e);
    				}
    			}
    			
    			System.out.println("All structs created successfully");
    			System.out.println("Creating Oracle array...");
    			Array array = oracleConnection.createOracleArray("TB_SEN_ORDR_INSUPD", structs);
    			System.out.println("Oracle array created successfully");
    			
    			System.out.println("Setting array parameter for stored procedure...");
    			callableStatement.setArray(1, array);
    			System.out.println("Array parameter set successfully");
    			
    			System.out.println("Executing stored procedure...");
                boolean hasResults = callableStatement.execute();
                System.out.println("Stored procedure executed successfully");
                System.out.println("Has results: " + hasResults);
    			
			} catch (Exception e) {
	            System.err.println("=== ERROR in insertSentenceOrder ===");
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error message: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	                
	                // Specific handling for ORA-17059
	                if (sqlEx.getErrorCode() == 17059) {
	                	System.err.println("ORA-17059: Failed to convert to internal representation");
	                	System.err.println("This typically indicates a data type mismatch between Java and Oracle types");
	                	System.err.println("Check the field types and values being passed to the Oracle struct");
	                }
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
			
		} catch (SQLException e) {
			System.err.println("=== DATABASE CONNECTION ERROR ===");
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        System.err.println("SQL State: " + e.getSQLState());
	        System.err.println("Error Code: " + e.getErrorCode());
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
	    }
		System.out.println("=== insertSentenceOrder completed successfully ===");
		System.out.println("Returning " + generatedSeqNums.size() + " generated sequence numbers");
		return generatedSeqNums;
	}
	
	public void updateSentenceLevelDetails(List<SentenceLevelDetails> lvlDetails) {

		System.out.println("=== Starting updateSentenceLevelDetails ===");
		System.out.println("Number of level Details to process: " + lvlDetails.size());
		String procedureCall = "{call spkg_omnet_wrapper.sp_sen_ordr_comp_cond_Update(?)}";

		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){

			try {
				OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
    			Struct[] structs = new Struct[lvlDetails.size()];
    			System.out.println("Creating Oracle structs for " + lvlDetails.size() + " sentence orders");
				
				for (int i = 0; i < lvlDetails.size(); i++) {
					SentenceLevelDetails detail = lvlDetails.get(i);
					try{
						Object[] structData = createLvlDetailsStructData(detail, i + 1);
						structs[i] = oracleConnection.createStruct("TY_SEN_ORDR_COMP_COND", structData);
						System.out.println("Successfully created struct for sentence LEVEL detail " + (i + 1));

					} catch (Exception e) {
    					System.err.println("ERROR: Failed to create struct for sentence lvl detail " + (i + 1));
    					System.err.println("Commit No: " + detail.getCOMMIT_NO());
    					System.err.println("Error: " + e.getMessage());
    					e.printStackTrace();
    					throw new RuntimeException("Failed to create struct for sentence order " + (i + 1), e);
    				}
				}

				System.out.println("All structs created successfully");
    			System.out.println("Creating Oracle array...");
    			Array array = oracleConnection.createOracleArray("TB_SEN_ORDR_COMP_COND", structs);
    			System.out.println("Oracle array created successfully");
    			

				System.out.println("Setting array parameter for stored procedure...");
    			callableStatement.setArray(1, array);
    			System.out.println("Array parameter set successfully");

				System.out.println("Executing stored procedure...");
                boolean hasResults = callableStatement.execute();
                System.out.println("Stored procedure executed successfully");
                System.out.println("Has results: " + hasResults);

			} catch (Exception e) {
	            System.err.println("=== ERROR in updateSentenceLevelDetails ===");
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error message: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	                
	                // Specific handling for ORA-17059
	                if (sqlEx.getErrorCode() == 17059) {
	                	System.err.println("ORA-17059: Failed to convert to internal representation");
	                	System.err.println("This typically indicates a data type mismatch between Java and Oracle types");
	                	System.err.println("Check the field types and values being passed to the Oracle struct");
	                }
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
		} catch (SQLException e) {
			System.err.println("=== DATABASE CONNECTION ERROR ===");
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        System.err.println("SQL State: " + e.getSQLState());
	        System.err.println("Error Code: " + e.getErrorCode());
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
	    }
	}

	public void insertSentenceLevelDetails(List<SentenceLevelDetails> lvlDetails) {

		System.out.println("=== Starting insertSentenceLevelDetails ===");
		System.out.println("Number of level Details to process: " + lvlDetails.size());
		String procedureCall = "{call spkg_omnet_wrapper.sp_sen_ordr_comp_cond_Insert(?)}";

		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){

			try{
				OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
    			Struct[] structs = new Struct[lvlDetails.size()];
    			System.out.println("Creating Oracle structs for " + lvlDetails.size() + " sentence orders");

				for (int i = 0; i < lvlDetails.size(); i++) {
					SentenceLevelDetails detail = lvlDetails.get(i);
					try{
						Object[] structData = createLvlDetailsStructData(detail, i + 1);
						structs[i] = oracleConnection.createStruct("TY_SEN_ORDR_COMP_COND", structData);
						System.out.println("Successfully created struct for sentence LEVEL detail " + (i + 1));
					} catch (Exception e) {
    					System.err.println("ERROR: Failed to create struct for sentence lvl detail " + (i + 1));
    					System.err.println("Commit No: " + detail.getCOMMIT_NO());
    					System.err.println("Error: " + e.getMessage());
    					e.printStackTrace();
    					throw new RuntimeException("Failed to create struct for sentence order " + (i + 1), e);
    				}
				}

				System.out.println("All structs created successfully");
    			System.out.println("Creating Oracle array...");
    			Array array = oracleConnection.createOracleArray("TB_SEN_ORDR_COMP_COND", structs);
    			System.out.println("Oracle array created successfully");

				System.out.println("Setting array parameter for stored procedure...");
    			callableStatement.setArray(1, array);
    			System.out.println("Array parameter set successfully");

				System.out.println("Executing stored procedure...");
                boolean hasResults = callableStatement.execute();
                System.out.println("Stored procedure executed successfully");
                System.out.println("Has results: " + hasResults);
				
			} catch (Exception e) {
	            System.err.println("=== ERROR in updateSentenceLevelDetails ===");
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error message: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	                
	                // Specific handling for ORA-17059
	                if (sqlEx.getErrorCode() == 17059) {
	                	System.err.println("ORA-17059: Failed to convert to internal representation");
	                	System.err.println("This typically indicates a data type mismatch between Java and Oracle types");
	                	System.err.println("Check the field types and values being passed to the Oracle struct");
	                }
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
		} catch (SQLException e) {
			System.err.println("=== DATABASE CONNECTION ERROR ===");
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        System.err.println("SQL State: " + e.getSQLState());
	        System.err.println("Error Code: " + e.getErrorCode());
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
	    }
	}
	
	public void deleteSentenceLevelDetails(List<SentenceLevelDetails> lvlDetails) {
		System.out.println("=== Starting deleteSentenceLevelDetails ===");
		System.out.println("Number of level Details to process: " + lvlDetails.size());
		String procedureCall = "{call spkg_omnet_wrapper.sp_sen_ordr_comp_cond_delete(?)}";
		
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			try{
				OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
    			Struct[] structs = new Struct[lvlDetails.size()];
    			System.out.println("Creating Oracle structs for " + lvlDetails.size() + " sentence orders");
    			
    			for (int i = 0; i < lvlDetails.size(); i++) {
    				SentenceLevelDetails detail = lvlDetails.get(i);
					try{
						Object[] structData = createLvlDetailsStructData(detail, i + 1);
						structs[i] = oracleConnection.createStruct("TY_SEN_ORDR_COMP_COND", structData);
						System.out.println("Successfully created struct for sentence LEVEL detail " + (i + 1));
					} catch (Exception e) {
    					System.err.println("ERROR: Failed to create struct for sentence lvl detail " + (i + 1));
    					System.err.println("Commit No: " + detail.getCOMMIT_NO());
    					System.err.println("Error: " + e.getMessage());
    					e.printStackTrace();
    					throw new RuntimeException("Failed to create struct for sentence order " + (i + 1), e);
    				}
    			}
    			
    			System.out.println("All structs created successfully");
    			System.out.println("Creating Oracle array...");
    			Array array = oracleConnection.createOracleArray("TB_SEN_ORDR_COMP_COND", structs);
    			System.out.println("Oracle array created successfully");

				System.out.println("Setting array parameter for stored procedure...");
    			callableStatement.setArray(1, array);
    			System.out.println("Array parameter set successfully");

				System.out.println("Executing stored procedure...");
                boolean hasResults = callableStatement.execute();
                System.out.println("Stored procedure executed successfully");
                System.out.println("Has results: " + hasResults);
    			
			} catch (Exception e) {
	            System.err.println("=== ERROR in updateSentenceLevelDetails ===");
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error message: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	                
	                // Specific handling for ORA-17059
	                if (sqlEx.getErrorCode() == 17059) {
	                	System.err.println("ORA-17059: Failed to convert to internal representation");
	                	System.err.println("This typically indicates a data type mismatch between Java and Oracle types");
	                	System.err.println("Check the field types and values being passed to the Oracle struct");
	                }
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
		} catch (SQLException e) {
			System.err.println("=== DATABASE CONNECTION ERROR ===");
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        System.err.println("SQL State: " + e.getSQLState());
	        System.err.println("Error Code: " + e.getErrorCode());
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
	    }
	}

	private Object[] createLvlDetailsStructData(SentenceLevelDetails lvlDetail, int detailIndx){
		try{
			Object[] structData = new Object[]{
				lvlDetail.getCB_SELECT(),
				lvlDetail.getSOH_SEQ_NUM(),
				lvlDetail.getSOCC_SEQ_NUM(),
				lvlDetail.getCONDITION_CODE(),
				lvlDetail.getSENTENCE_LEVEL(),
				lvlDetail.getLENGTH_YY(),
				lvlDetail.getLENGTH_MM(),
				lvlDetail.getLENGTH_DD(),
				lvlDetail.getHELD_AT(),
				lvlDetail.getTREATMENT(),
				lvlDetail.getSTATUS(),
				lvlDetail.getSOCC_COND_ORDER_NUM(),
				lvlDetail.getSOCC_OPERATOR(),
				lvlDetail.getCOMMIT_NO(),
				lvlDetail.getCASE_NUM(),
				lvlDetail.getCRA_NUM(),
				lvlDetail.getSNTC_LVL_CD(),
				lvlDetail.getSNTC_LVL_SEQ_NUM(),
				lvlDetail.getSNTC_ORD_SEQ_NUM(),
				lvlDetail.getSOCC_RELEASE_DATE(),
				lvlDetail.getSOCC_RELEASE_TIME(),
				lvlDetail.getSOCC_COMPL_FLAG(),
				lvlDetail.getSOCC_COMPL_USERID(),
				lvlDetail.getSOCC_COMPL_DATE(),
				lvlDetail.getSOCC_COMPL_TIME()
			};

			System.out.println("  Struct data created successfully for order " + detailIndx);
			return structData;

		} catch (Exception e) {
			System.err.println("  ERROR: Failed to create struct data for order " + detailIndx);
			System.err.println("  Error: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to create struct data for order " + detailIndx, e);
		}
	}
	
	/**
	 * Creates the struct data array with proper field mapping and type conversion
	 * according to the Oracle TY_SEN_ORDR_INSUPD type definition
	 */
	private Object[] createStructData(SentenceOrderDetails ordr, int orderIndex) {
		System.out.println("  Creating struct data for order " + orderIndex);
		
		try {
			// Convert dates to java.sql.Date if needed
			java.sql.Date sohSentDate = convertToSqlDate(ordr.getSohSentDate(), "SOH_SENT_DATE", orderIndex);
			System.out.println(ordr.getEffectiveDate());
			java.sql.Date effectiveDate = convertToSqlDate(ordr.getEffectiveDate(), "EFFECTIVE_DATE", orderIndex);
			System.out.println(effectiveDate);
			java.sql.Date deferredDate = convertToSqlDate(ordr.getDeferredDate(), "DEFERRED_DATE", orderIndex);
			java.sql.Date firstDueDate = convertToSqlDate(ordr.getFirstDueDate(), "FIRST_DUE_DATE", orderIndex);
			java.sql.Date sohIntrptDate = convertToSqlDate(ordr.getSohIntrptDate(), "SOH_INTRPT_DATE", orderIndex);
			java.sql.Date sohReleaseDate = convertToSqlDate(ordr.getSohReleaseDate(), "SOH_RELEASE_DATE", orderIndex);
			java.sql.Date paroleVIncarcDate = convertToSqlDate(ordr.getParoleVIncarcDate(), "PAROLE_V_INCARC_DATE", orderIndex);
			java.sql.Date paroleVParoleDate = convertToSqlDate(ordr.getParoleVParoleDate(), "PAROLE_V_PAROLE_DATE", orderIndex);
			java.sql.Date paroleVOriginalEffDt = convertToSqlDate(ordr.getParoleVOriginalEffDt(), "PAROLE_V_ORIGINAL_EFF_DT", orderIndex);
			java.sql.Date paroleVMed = convertToSqlDate(ordr.getParoleVMed(), "PAROLE_V_MED", orderIndex);
			java.sql.Date paroleVWarrantDate = convertToSqlDate(ordr.getParoleVWarrantDate(), "PAROLE_V_WARRANT_DATE", orderIndex);
			java.sql.Date crVCrDate = convertToSqlDate(ordr.getCrVCrDate(), "CR_V_CR_DATE", orderIndex);
			java.sql.Date crVOrigninalEffDate = convertToSqlDate(ordr.getCrVOrigninalEffDate(), "CR_V_ORIGNINAL_EFF_DATE", orderIndex);
			java.sql.Date crVIncurcDate = convertToSqlDate(ordr.getCrVIncurcDate(), "CR_V_INCURC_DATE", orderIndex);
			java.sql.Date crVMed = convertToSqlDate(ordr.getCrVMed(), "CR_V_MED", orderIndex);
			java.sql.Date crVWarrantDate = convertToSqlDate(ordr.getCrVWarrantDate(), "CR_V_WARRANT_DATE", orderIndex);
			java.sql.Date aprvDate = convertToSqlDate(ordr.getAprvDate(), "APRV_DATE", orderIndex);
			java.sql.Date sprvDate = convertToSqlDate(ordr.getSprvDate(), "SPRV_DATE", orderIndex);
			java.sql.Date sohCancelDate = convertToSqlDate(ordr.getSohCancelDate(), "SOH_CANCEL_DATE", orderIndex);
			java.sql.Date offenseDate = convertToSqlDate(ordr.getOffenseDate(), "SOH_OFFENSE_DATE", orderIndex);
			
			// Convert BigDecimal values to proper Oracle NUMBER format
			BigDecimal victimCompensation = convertToBigDecimal(ordr.getVictimCompensation(), "VICTIM_COMPENSATION", orderIndex);
			BigDecimal moneyOwed = convertToBigDecimal(ordr.getMoneyOwed(), "MONEY_OWED", orderIndex);
			BigDecimal monthlyPayment = convertToBigDecimal(ordr.getMonthlyPayment(), "MONTHLY_PAYMENT", orderIndex);
			
			// Convert Boolean fields to VARCHAR2(1) - Oracle expects Y/N strings, not Boolean objects
			String sohIndefiniteFlag = convertBooleanToString(ordr.getSohIndefiniteFlag(), "SOH_INDEFINITE_FLAG", orderIndex);
			String paroleVAllGdtm = convertBooleanToString(ordr.getParoleVAllGdtm(), "PAROLE_V_ALL_GDTM", orderIndex);
			String paroleVEligibleFrParole = convertBooleanToString(ordr.getParoleVEligibleFrParole(), "PAROLE_V_ELIGIBLE_FR_PAROLE", orderIndex);
			
			// Create the struct data array in the exact order as defined in Oracle type
			Object[] structData = new Object[]{
				// Basic sentence order fields
				ordr.getSohSeqNum(),                    // soh_seq_num NUMBER
				ordr.getCommitNo(),                     // commit_no VARCHAR2(8)
				ordr.getSohCalSeqNum(),                 // cal_seq_num NUMBER
				ordr.getCaseNum(),                      // case_num VARCHAR2(15)
				ordr.getCraNum(),                       // cra_num VARCHAR2(15)
				ordr.getChargeNum(),                    // charge_num VARCHAR2(13)
				ordr.getChargeDesc(),                   // charge_desc VARCHAR2(30)
				null,                                   // court_name VARCHAR2(45)
				ordr.getCourtCode(),                    // court_code VARCHAR2(10)
				ordr.getJudgeCode(),                    // judge_code VARCHAR2(10)
				null,                                   // judge_name VARCHAR2(50)
				sohSentDate,                           // sentence_date DATE
				ordr.getSohSntcLvlCd(),                // sentence_to VARCHAR2(2)
				
				// Length fields
				ordr.getLengthYy(),                     // length_yy NUMBER
				ordr.getLengthMm(),                     // length_mm NUMBER
				ordr.getLengthDd(),                     // length_dd NUMBER
				effectiveDate,                          // effective_date DATE
				ordr.getHeldAt(),                       // held_at VARCHAR2(2)
				
				// Jail credit fields
				ordr.getJailCreditYy(),                 // jail_credit_yy NUMBER
				ordr.getJailCreditMm(),                 // jail_credit_mm NUMBER
				ordr.getJailCreditDd(),                 // jail_credit_dd NUMBER
				
				// Mandatory minimum fields
				ordr.getMinMandYy(),                    // min_mand_yy NUMBER
				ordr.getMinMandMm(),                    // min_mand_mm NUMBER
				ordr.getMinMandDd(),                    // min_mand_dd NUMBER
				
				// Absolute length fields
				ordr.getAbsLenYy(),                     // abs_len_yy NUMBER
				ordr.getAbsLenMm(),                     // abs_len_mm NUMBER
				ordr.getAbsLenDd(),                     // abs_len_dd NUMBER
				
				// Deferred and sentence type fields
				ordr.getDeferredFlag(),                 // deferred_flag VARCHAR2(1)
				deferredDate,                          // deferred_date DATE
				ordr.getTisNtis(),                     // tis_ntis VARCHAR2(10)
				ordr.getSentenceType(),                // sentence_type VARCHAR2(3)
				ordr.getFlag4204k(),                   // flag_4204k VARCHAR2(1)
				
				// Statute fields
				ordr.getSohStatuteJurisdiction(),      // soh_statute_jurisdiction VARCHAR2(2)
				ordr.getSohStatuteTitle(),             // soh_statute_title VARCHAR2(4)
				ordr.getSohStatuteSection(),           // soh_statute_section VARCHAR2(4)
				ordr.getSohStatuteSubsection(),        // soh_statute_subsection VARCHAR2(4)
				ordr.getSohStatuteType(),              // soh_statute_type VARCHAR2(1)
				ordr.getSohStatuteClass(),             // soh_statute_class VARCHAR2(1)
				ordr.getSohSentenceCategory(),         // soh_sentence_category VARCHAR2(2)
				ordr.getNcicCode(),                    // soh_ncic_code VARCHAR2(2)
				
				// Financial fields
				ordr.getFines(),                       // fines NUMBER(12,2)
				ordr.getCourtCosts(),                  // court_costs NUMBER(12,2)
				ordr.getRestitution(),                 // restitution NUMBER(12,2)
				ordr.getDrugAlchlRehabitFund(),        // drug_alchl_rehabit_fund NUMBER(12,2)
				victimCompensation,                    // victim_compensation NUMBER(12,2)
				moneyOwed,                             // money_owed NUMBER(12,2)
				monthlyPayment,                        // monthly_payment NUMBER(12,2)
				firstDueDate,                          // first_due_date DATE
				ordr.getFacility(),                    // facility VARCHAR2(6)
				ordr.getSohWeekenderDays(),            // soh_weekender_days NUMBER
				
				// Additional sentence fields
				sohIntrptDate,                         // soh_intrpt_date DATE
				ordr.getSohIntrptSent(),               // soh_intrpt_sent VARCHAR2(1)
				ordr.getSohDocDiscretion(),            // soh_doc_discretion VARCHAR2(1)
				sohIndefiniteFlag,                     // soh_indefinite_flag VARCHAR2(1)
				ordr.getSohGdtmCalcFlag(),             // soh_gdtm_calc_flag VARCHAR2(1)
				ordr.getSohMeritDays(),                // soh_merit_days NUMBER
				sohReleaseDate,                        // soh_release_date DATE
				ordr.getSohReleaseTime(),              // soh_release_time VARCHAR2(5)
				
				// Parole fields
				ordr.getEligibleFrParoleFlag(),        // eligible_fr_parole_flag VARCHAR2(1)
				ordr.getParoleVViolFlag(),             // parole_v_viol_flag VARCHAR2(1) - Note: this should be String, not Boolean
				paroleVIncarcDate,                     // parole_v_incarc_date DATE
				paroleVParoleDate,                     // parole_v_parole_date DATE
				paroleVOriginalEffDt,                  // parole_v_original_eff_dt DATE
				ordr.getParoleVOrgnlCalSeqNum(),       // parole_v_orgnl_cal_seq_num NUMBER
				paroleVMed,                            // parole_v_med DATE
				paroleVWarrantDate,                    // parole_v_warrant_date DATE
				ordr.getParoleVAbscondTime(),          // parole_v_abscond_time NUMBER
				paroleVAllGdtm,                        // parole_v_all_gdtm VARCHAR2(1)
				ordr.getParoleVGoodTime(),             // parole_v_good_time NUMBER
				paroleVEligibleFrParole,               // parole_v_eligible_fr_parole VARCHAR2(1)
				
				// CR violation fields
				crVCrDate,                             // cr_v_cr_date DATE
				crVOrigninalEffDate,                   // cr_v_origninal_eff_date DATE
				ordr.getCrVOrgnlCalSeqNum(),           // cr_v_orgnl_cal_seq_num NUMBER
				ordr.getCrVCrViolation(),              // cr_v_cr_violation VARCHAR2(1)
				crVIncurcDate,                         // cr_v_incurc_date DATE
				crVMed,                                // cr_v_med DATE
				crVWarrantDate,                        // cr_v_warrant_date DATE
				ordr.getCrVAbscondDays(),              // cr_v_abscond_days NUMBER
				ordr.getCrVAllGdtm(),                  // cr_v_all_gdtm VARCHAR2(1)
				ordr.getCrVGoodTime(),                 // cr_v_good_time NUMBER
				
				// Approval fields
				ordr.getAprvFlag(),                    // aprv_flag VARCHAR2(1)
				ordr.getAprvUserId(),                  // aprv_user_id VARCHAR2(8)
				null,                                  // aprv_user_name VARCHAR2(70)
				aprvDate,                              // aprv_date DATE
				ordr.getAprvTime(),                    // aprv_time VARCHAR2(5)
				ordr.getAprvReasonCode(),              // aprv_reason_code VARCHAR2(10)
				null,                                  // aprv_cancel_flag VARCHAR2(1)
				ordr.getAprvUserComnts(),              // aprv_user_comnts VARCHAR2(250)
				null,                                  // aprv_sent_to_userid VARCHAR2(8)
				null,                                  // aprv_sent_to_user_name VARCHAR2(70)
				
				// Supervisor fields
				ordr.getSprvDecision(),                // sprv_decision VARCHAR2(1)
				ordr.getSprvUserId(),                  // sprv_user_id VARCHAR2(8)
				null,                                  // sprv_user_name VARCHAR2(70)
				sprvDate,                              // sprv_date DATE
				ordr.getSprvTime(),                    // sprv_time VARCHAR2(5)
				ordr.getSprvUserComnts(),              // sprv_user_comnts VARCHAR2(250)
				
				// Cancel fields
				ordr.getSohCancelFlag(),               // soh_cancel_flag VARCHAR2(1)
				ordr.getSohCancelBy(),                 // soh_cancel_by VARCHAR2(8)
				null,                                  // soh_cancel_by_name VARCHAR2(70)
				sohCancelDate,                         // soh_cancel_date DATE
				ordr.getSohCancelTime(),               // soh_cancel_time VARCHAR2(5)
				null,                                  // soh_cancel_reason_cd VARCHAR2(10)
				ordr.getSohCancelComnts(),             // soh_cancel_comments VARCHAR2(500)
				
				// Status and order fields
				ordr.getSohStatus(),                   // soh_status VARCHAR2(2)
				null,                                  // inserted_userid VARCHAR2(8)
				null,                                  // ref_soh_seq_num NUMBER
				null,                                  // curr_cal_start_date DATE
				null,                                  // curr_cal_me_date DATE
				ordr.getSohCalSeqNum(),                // soh_calc_seq_num NUMBER
				ordr.getOrderType(),                   // order_type VARCHAR2(15)
				ordr.getSentNewFlag(),                 // sent_new_flag VARCHAR2(1)
				null,                                  // include_abscond_days VARCHAR2(1)
				null,                                  // strd_acceleration_yy NUMBER
				null,                                  // strd_acceleration_mm NUMBER
				null,                                  // strd_acceleration_dd NUMBER
				
				// Consecutive/Concurrent fields
				ordr.getConsConcFlag(),                // cons_conc_flag VARCHAR2(1)
				ordr.getConsConcSntcOrdSeqNum(),       // cons_conc_sntc_ord_seq_num NUMBER
				ordr.getConsConcSohSeqNum(),           // cons_conc_soh_seq_num NUMBER
				ordr.getConsConcLvl5(),                // cons_conc_lvl5 VARCHAR2(1)
				ordr.getConsConcLvl4(),                // cons_conc_lvl4 VARCHAR2(1)
				ordr.getConsConcLvl4h(),               // cons_conc_lvl4h VARCHAR2(1)
				ordr.getConsConcLvl3(),                // cons_conc_lvl3 VARCHAR2(1)
				ordr.getConsConcLvl2(),                // cons_conc_lvl2 VARCHAR2(1)
				ordr.getConsConcLvl1(),                // cons_conc_lvl1 VARCHAR2(1)
				ordr.getConsConcLvl1r(),               // cons_conc_lvl1r VARCHAR2(1)
				
				// Modification fields
				ordr.getSohModificationType(),         // soh_modification_type VARCHAR2(5)
				null,                                  // reason_for_modification VARCHAR2(10)
				ordr.getSohSntcLvlSeqNum(),            // soh_sntc_lvl_seq_num NUMBER
				ordr.getSohSntcOrdSeqNum(),            // soh_sntc_ord_seq_num NUMBER
				
				// Time served and override fields
				null,                                  // time_served_flag VARCHAR2(1)
				null,                                  // jlcr_reason_for_override_user VARCHAR2(10)
				null,                                  // jlcr_user_comnts VARCHAR2(2000)
				null,                                  // tm_reason_for_override_user VARCHAR2(10)
				null,                                  // tm_user_comnts VARCHAR2(2000)
				null,                                  // tm_reason_for_override_suprv VARCHAR2(10)
				null,                                  // tm_suprv_comnts VARCHAR2(2000)
				null,                                  // jlcr_user_decn_no_override VARCHAR2(1)
				null,                                  // tm_user_decn_no_override VARCHAR2(1)
				null,                                  // jlcr_msg_txt VARCHAR2(1)
				null,                                  // tm_msg_txt VARCHAR2(4000)
				offenseDate,                           // soh_offense_date DATE
				
				// Merit and loss fields
				null,                                  // merit_denied_by VARCHAR2(7)
				null,                                  // merit_denied_date_time DATE
				null,                                  // LOSS_GT_OVERRIDE_REASON_CD VARCHAR2(10)
				null,                                  // LOSS_OF_MERIT_DAYS_CONFIRM NUMBER
				null,                                  // LOSS_OF_MERIT_DAYS NUMBER
				null,                                  // LOSS_OF_STATUTORY_DAYS_CONFIRM NUMBER
				null,                                  // LOSS_OF_STATUTORY_DAYS NUMBER
				ordr.getSoh4382aFlag(),                // SOH_4382A_FLAG VARCHAR2(1)
				null,                                  // LOSS_GT_ENTERED_BY VARCHAR2(8)
				null,                                  // LOSS_GT_ENTERED_DATE DATE
				null,                                  // LOSS_GT_ENTERED_TIME VARCHAR2(5)
				null,                                  // LOSS_GT_OVERRIDE VARCHAR2(1)
				null,                                  // adjust_LOSS_OF_MERIT_DAYS NUMBER
				null                                   // adjust_LOSS_OF_STATUTORY_DAYS NUMBER
			};
			
			System.out.println("  Struct data created successfully for order " + orderIndex);
			return structData;
			
		} catch (Exception e) {
			System.err.println("  ERROR: Failed to create struct data for order " + orderIndex);
			System.err.println("  Error: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to create struct data for order " + orderIndex, e);
		}
	}
	
	public static String oracleArrayToJsonLike(Array oracleArray, Class<?> pojoClass) throws SQLException {
	    if (oracleArray == null) {
	        return "null";
	    }

	    Object arrayObj = oracleArray.getArray();
	    if (!(arrayObj instanceof Object[])) {
	        return "[]";
	    }

	    Object[] structs = (Object[]) arrayObj;
	    List<Map<String, Object>> list = new ArrayList<>();

	    // get declared fields of the POJO (order matters)
	    Field[] fields = pojoClass.getDeclaredFields();

	    for (Object obj : structs) {
	        if (obj instanceof Struct) {
	            Struct struct = (Struct) obj;
	            Object[] attrs = struct.getAttributes();

	            Map<String, Object> row = new LinkedHashMap<>();
	            for (int i = 0; i < attrs.length; i++) {
	                String key = (i < fields.length) ? fields[i].getName() : "col" + (i + 1);
	                row.put(key, attrs[i]);
	            }

	            list.add(row);
	        }
	    }

	    return list.toString();
	}
	
	/**
	 * Converts a Date object to java.sql.Date with proper null handling and logging
	 */
	private java.sql.Date convertToSqlDate(Object dateValue, String fieldName, int orderIndex) {
		if (dateValue == null) {
			System.out.println("    [Order " + orderIndex + "] " + fieldName + ": null -> returning null");
			return null;
		}
		
		try {
			if (dateValue instanceof java.sql.Date) {
				System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Already java.sql.Date");
				return (java.sql.Date) dateValue;
			} else if (dateValue instanceof java.util.Date) {
				java.sql.Date sqlDate = new java.sql.Date(((java.util.Date) dateValue).getTime());
				System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Converted util.Date to sql.Date");
				return sqlDate;
			} else {
				System.err.println("    [Order " + orderIndex + "] WARNING: " + fieldName + " is not a Date type: " + dateValue.getClass().getName());
				return null;
			}
		} catch (Exception e) {
			System.err.println("    [Order " + orderIndex + "] ERROR: Failed to convert " + fieldName + " to sql.Date: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Converts a Double to BigDecimal with proper null handling and logging
	 */
	private BigDecimal convertToBigDecimal(Object value, String fieldName, int orderIndex) {
		if (value == null) {
			System.out.println("    [Order " + orderIndex + "] " + fieldName + ": null -> returning null");
			return null;
		}
		
		try {
			if (value instanceof BigDecimal) {
				System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Already BigDecimal");
				return (BigDecimal) value;
			} else if (value instanceof Number) {
				BigDecimal result = new BigDecimal(value.toString());
				System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Converted Number to BigDecimal: " + result);
				return result;
			} else {
				BigDecimal result = new BigDecimal(value.toString());
				System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Parsed String to BigDecimal: " + result);
				return result;
			}
		} catch (Exception e) {
			System.err.println("    [Order " + orderIndex + "] ERROR: Failed to convert " + fieldName + " to BigDecimal: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Converts a Boolean to VARCHAR2(1) string with proper null handling and logging
	 */
	private String convertBooleanToString(Object value, String fieldName, int orderIndex) {
		if (value == null) {
			System.out.println("    [Order " + orderIndex + "] " + fieldName + ": null -> returning null");
			return null;
		}
		
		try {
			if (value instanceof Boolean) {
				String result = ((Boolean) value) ? "Y" : "N";
				System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Converted Boolean to " + result);
				return result;
			} else if (value instanceof String) {
				String stringValue = ((String) value).toLowerCase();
				if ("true".equals(stringValue) || "y".equals(stringValue) || "1".equals(stringValue)) {
					System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Converted String '" + value + "' to Y");
					return "Y";
				} else if ("false".equals(stringValue) || "n".equals(stringValue) || "0".equals(stringValue)) {
					System.out.println("    [Order " + orderIndex + "] " + fieldName + ": Converted String '" + value + "' to N");
					return "N";
				} else {
					System.err.println("    [Order " + orderIndex + "] WARNING: Unknown boolean string value for " + fieldName + ": " + value);
					return null;
				}
			} else {
				System.err.println("    [Order " + orderIndex + "] WARNING: " + fieldName + " is not a Boolean or String: " + value.getClass().getName());
				return null;
			}
		} catch (Exception e) {
			System.err.println("    [Order " + orderIndex + "] ERROR: Failed to convert " + fieldName + " to boolean string: " + e.getMessage());
			return null;
		}
	}

}
