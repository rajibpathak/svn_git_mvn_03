package com.omnet.cnt.Service;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.omnet.cnt.classes.PropertyTracking;
import com.omnet.cnt.classes.PropertyTrackingColtItems;
import com.omnet.cnt.classes.PropertyTrackingHdr;

//import com.omnet.cnt.Repository.PropertyTrackingRepository;
//import com.omnet.cnt.classes.InmateData;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

@Service
public class PropertyTrackingService {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyTrackingService.class);
	
	@Autowired
    private DataSource dataSource;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	//private SimpleJdbcCall simpleJdbcCall;
	static String jdbcUrl = "jdbc:oracle:thin:@10.78.21.196:1521:devdb";
    static String username = "OMNET_APP";
    static String password = "OmnetP4ssw0rd!";
	
	public Map<String, Object> getPropertyTrackingHdr(String commitNo, String propType) {
		
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withCatalogName("SPKG_PRTR")
		             .withProcedureName("SP_PRTR_HDR_QUERY")
		             .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
		             .declareParameters(
		            		 new SqlParameter("ICH_COMMIT_NO", Types.VARCHAR),
		                     new SqlParameter("ICH_PROP_TRK_TP", Types.VARCHAR),
		                     new SqlOutParameter("RESULTSET", Types.REF_CURSOR)
		                     );
			Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("ICH_COMMIT_NO", commitNo);
	        inputParams.put("ICH_PROP_TRK_TP", propType);
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
	
	public Map<String, Object> getPropertyItemsCollected(String propTrkSeq) {
		
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withCatalogName("SPKG_PRTR")
		             .withProcedureName("SP_PRTR_ITMS_COLT_QUERY")
		             .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
		             .declareParameters(
		            		 new SqlParameter("INU_PROP_TRK_SEQ", Types.VARCHAR),
		                     new SqlOutParameter("RESULTSET", Types.REF_CURSOR)
		            		 );
			Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("INU_PROP_TRK_SEQ", propTrkSeq);
	        //System.out.println("Property tracking items collected :: " + inputParams);
	        
			Map<String, Object> result = jdbcCall.execute(inputParams);
	        return result;
		} catch (Exception ex) {
	        // Log and rethrow so the controller can handle it
	        System.err.println("Database call failed. Property Tracking Get Collected Items: " + ex.getMessage());
	        ex.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure", ex);
	    }
	}
	
	public Map<String, Object> getPropertyItemsIssued(String propTrkSeq) {
		
		try {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withCatalogName("SPKG_PRTR")
	             .withProcedureName("SP_PRTR_ITMS_ISSD_QUERY")
	             .withoutProcedureColumnMetaDataAccess()  // Skip metadata lookup
	             .declareParameters(
	            		 new SqlParameter("INU_PROP_TRK_SEQ_NO", Types.VARCHAR),
	                     new SqlOutParameter("RESULTSET", Types.REF_CURSOR)
	            		 );
		Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("INU_PROP_TRK_SEQ_NO", propTrkSeq);
        //System.out.println("Property tracking items issued :: ");
        
		Map<String, Object> result = jdbcCall.execute(inputParams);
        return result;
		} catch (Exception ex) {
	        System.err.println("Database call failed. Property Tracking Get Items Issued: " + ex.getMessage());
	        ex.printStackTrace();
	        throw new RuntimeException("Error executing stored procedure", ex);
	    }
	}
	
	public List<Map<String, Object>> getRelationshipTable(){
		String sql = "SELECT RELATIONSHIP_CODE, RELATIONSHIP_DESC FROM RELATIONSHIP_RT where STATUS = 'A' order by RELATIONSHIP_DESC asc";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
	    return result;
	}
	
	public String getUserFullName(String userId) {
		String sql = "SELECT sf_get_user_name('"+userId+"') FROM DUAL";
		String result = jdbcTemplate.queryForObject(sql, String.class);
		return result;
	}
	
	public String getInstitutionNum(String sbiNum, String commitNum) {
		String sql = "select current_inst_num from inmate where sbi_mst_sbi_no = '"+sbiNum+"' and commit_no = '"+commitNum+"' ";
		String result = jdbcTemplate.queryForObject(sql, String.class);
		return result;
	}
	
	@Transactional()
	public void updatePropertyColtHdr(Integer propTrkSeq, PropertyTrackingHdr updateSet){
		
		String procedureCall = "{call spkg_omnet_wrapper.sp_prtr_hdr_update(?, ?)}";
		List<PropertyTrackingHdr> propertyTrackingHdrList = new ArrayList<>();
		propertyTrackingHdrList.add(updateSet);
		//System.out.println(updateSet);
		
		try (Connection connection = dataSource.getConnection();
				
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			
			//connection.setAutoCommit(false);
	        
	        try {
			
			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			Struct[] structs = new Struct[propertyTrackingHdrList.size()];
			for (int i = 0; i < propertyTrackingHdrList.size(); i++) {
				
				Date propTrkDt = (propertyTrackingHdrList.get(i).getPropTrkDt() != null) ? convertToTimestamp(propertyTrackingHdrList.get(i).getPropTrkDt()) : null;
				Date propRetnDt = (propertyTrackingHdrList.get(i).getPropRetnDt() != null) ? convertToTimestamp(propertyTrackingHdrList.get(i).getPropRetnDt()) : null;
				//System.out.println(propTrkDt + " " + propRetnDt + " " + propertyTrackingHdrList.get(i).getInmateCommitNo());

				 structs[i] = oracleConnection.createStruct("TY_PRTR_HDR", new Object[]{
						 propertyTrackingHdrList.get(i).getInmateCommitNo(), 
						 propertyTrackingHdrList.get(i).getInstitutionInstNum(),
						 propertyTrackingHdrList.get(i).getUsersUserId(),
						 propertyTrackingHdrList.get(i).getUserFirstName() != null ? propertyTrackingHdrList.get(i).getUserFirstName() : "",
						 propertyTrackingHdrList.get(i).getUserLastName() != null ? propertyTrackingHdrList.get(i).getUserLastName() : "",
						 propertyTrackingHdrList.get(i).getUserMidName() != null ? propertyTrackingHdrList.get(i).getUserMidName() : "",
						 propertyTrackingHdrList.get(i).getUserSuffixName() != null ? propertyTrackingHdrList.get(i).getUserSuffixName() : "",
						 propertyTrackingHdrList.get(i).getUsersUserIdReturnBy(),
						 //propertyTrackingHdrList.get(i).getUsersUserId(),
			                propertyTrackingHdrList.get(i).getPropTrkSeqNo(),
			                propertyTrackingHdrList.get(i).getPropTrkAmt(),
			                propRetnDt,
			                propTrkDt,
			                //propertyTrackingHdrList.get(i).getPropTrkDt(),
			                //propertyTrackingHdrList.get(i).getPropRetnDt(),
			                propertyTrackingHdrList.get(i).getPropTrkTp(),
			                propertyTrackingHdrList.get(i).getPropBoxNum()
			                //propertyTrackingHdrList.get(i).getInmateCommitNo()
				 });
			}
			/*if (structs != null && structs.length > 0) {
			    // The structs array is not empty
			    System.out.println("Struct array is not empty." + structs.length);
			    for (int i = 0; i < structs.length; i++) {
			        Struct struct = structs[i];
			        Object[] attributes = struct.getAttributes();
			        // Using a loop to print attributes
			        System.out.print("Struct " + i + ": ");
			        for (int j = 0; j < attributes.length; j++) {
			            System.out.print(attributes[j]);
			            if (j < attributes.length - 1) {
			                System.out.print(", "); // Add a comma between elements
			            }
			        }
			        System.out.println(); // New line after each struct
			    }
			}*/
			Array array = oracleConnection.createOracleArray("TB_PRTR_HDR", structs);
			//System.out.println("Array Created Successfully");
			/*if (array != null) {
			    Object[] arrayElements = (Object[]) array.getArray();
			    if (arrayElements != null && arrayElements.length > 0) {
			        // The array is not empty
			        System.out.println("Oracle array is not empty." + arrayElements.length);
			        for (int i = 0; i < arrayElements.length; i++) {
			            Struct arrayStruct = (Struct) arrayElements[i];
			            Object[] attributes = arrayStruct.getAttributes();
			         // Using a loop to print array elements
			            for (int j = 0; j < arrayElements.length; j++) {
			                Struct arrayStruct2 = (Struct) arrayElements[j];
			                Object[] attributes2 = arrayStruct2.getAttributes();
			                
			                // Print attributes of each struct in the array
			                System.out.print("Array Element " + j + ": ");
			                for (int k = 0; k < attributes2.length; k++) {
			                    System.out.print(attributes2[k]);
			                    if (k < attributes2.length - 1) {
			                        System.out.print(", "); // Add a comma between elements
			                    }
			                }
			                System.out.println(); // New line after each array element
			            }
			        }
			    }
			}*/
			
			callableStatement.setInt(1, propTrkSeq);
			callableStatement.setArray(2, array);
			//System.out.println("Params Set Successfully");
            
            //System.out.println("Executing procedure...");
            boolean hasResults = callableStatement.execute();
            //System.out.println("Procedure execution returned: " + hasResults);
            
            // Check if any rows were affected
            int updateCount = callableStatement.getUpdateCount();
            //System.out.println("Update count: " + updateCount);
            
	        } catch (Exception e) {
	            
	            // Improved error handling
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
		}catch (SQLException e) {
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
        }
	}
	
	@Transactional
	public void insertPropertyColtItems(Integer propTrkSeq, List<PropertyTrackingColtItems> updateSet) {
		
		String procedureCall = "{call spkg_omnet_wrapper.sp_prtr_itms_colt_insert(?)}";
		
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			
			try {
				OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			
				Struct[] structs = new Struct[updateSet.size()];
				for (int i = 0; i < updateSet.size(); i++) {
					PropertyTrackingColtItems obj = updateSet.get(i);
					Integer nxtSeq = genNextPropertyColtItemSeq();
					
					structs[i] = oracleConnection.createStruct("TY_PRTR_ITMS_COLT", new Object[]{
							obj.getPropItmRowid(),
							propTrkSeq,
				    		obj.getItemCd(),
				    		obj.getItemCd(),
				    		obj.getPropDesc(),
				    		nxtSeq,//obj.getPropInmtItmSeqNo(),
				    		obj.getPropInmtItmQty(),
				    		obj.getPropInmtItmDesc(),
				    		obj.getPropInmtItmRetnQty(),
				    		obj.getPropInmtRetnFstNm() != null ? obj.getPropInmtRetnFstNm() : "",
				    		obj.getPropInmtRetnLstNm() != null ? obj.getPropInmtRetnLstNm() : "",
				    		obj.getPropInmtRetnMidNm() != null ? obj.getPropInmtRetnMidNm() : "",
				    		obj.getPropInmtRetnSfx() != null ? obj.getPropInmtRetnSfx() : "",
				    		obj.getRelationshipCode()
				    });
				}
				//System.out.println("Struct Created");
				
    			Array array = oracleConnection.createOracleArray("TB_PRTR_ITMS_COLT", structs);
    			//System.out.println("Array Created");
    			
    			callableStatement.setArray(1, array);
    			//System.out.println("Params Set Successfully");
    			
    			//System.out.println("Executing procedure...");
                boolean hasResults = callableStatement.execute();
                
                //System.out.println("Executed Successfully");
			
			}catch (Exception e) {
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
			
		}catch (SQLException e) {
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
        }
	}
	
	@Transactional
	public void insertPropertyIssdItems(Integer propTrkSeq, List<PropertyTrackingColtItems> updateSet) {
		
		String procedureCall = "{call spkg_omnet_wrapper.sp_prtr_itms_issd_insert(?)}";
		
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			
			try {
				OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
				
				Struct[] structs = new Struct[updateSet.size()];
				for (int i = 0; i < updateSet.size(); i++) {
					PropertyTrackingColtItems item = updateSet.get(i);
					Integer nxtSeq = genNextPropertyIssdItemSeq();
					
					structs[i] = oracleConnection.createStruct("TY_PRTR_ITMS_ISSD", new Object[]{
							item.getPropItmRowid(),
							propTrkSeq,//item.getPthPropTrkSeqNo(),
				    		item.getItemCd(),
				    		item.getItemCd(),
				    		item.getPropDesc(),
				    		nxtSeq,//item.getPropInmtItmSeqNo(),
				    		item.getPropInmtItmQty(),
				    		item.getPropInmtItmDesc(),
				    		item.getPropInmtItmRetnQty()
					});
				}
				//System.out.println("Struct Created");
				
    			Array array = oracleConnection.createOracleArray("TB_PRTR_ITMS_ISSD", structs);
    			//System.out.println("Array Created");
    			
    			callableStatement.setArray(1, array);
    			//System.out.println("Params Set Successfully");
    			
    			//System.out.println("Executing procedure...");
                boolean hasResults = callableStatement.execute();
                
                //System.out.println("Executed Successfully");
				
			}catch (Exception e) {
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
			
		}catch (SQLException e) {
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
        }
	}
	
	@Transactional
	public Map<String, Object> insertPropertyHdr(PropertyTrackingHdr propertyObj) {
		
		String procedureCall = "{call spkg_omnet_wrapper.sp_prtr_hdr_insert(?)}";
		
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			try {
				
				OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
				Struct[] structs = new Struct[1];
				
				Date propTrkDt = (propertyObj.getPropTrkDt() != null) ? convertToTimestamp(propertyObj.getPropTrkDt()) : null;
				Date propRetnDt = (propertyObj.getPropRetnDt() != null) ? convertToTimestamp(propertyObj.getPropRetnDt()) : null;
				Integer nxtSeq = genNextPropertySeq();
				System.out.println("Property Tracking Amount: "+propertyObj.getPropTrkAmt());
				structs[0] = oracleConnection.createStruct("TY_PRTR_HDR", new Object[]{
						propertyObj.getInmateCommitNo(), 
						propertyObj.getInstitutionInstNum(),
						propertyObj.getUsersUserId(),
						propertyObj.getUserFirstName() != null ? propertyObj.getUserFirstName() : "",
						propertyObj.getUserLastName() != null ? propertyObj.getUserLastName() : "",
						propertyObj.getUserMidName() != null ? propertyObj.getUserMidName() : "",
						propertyObj.getUserSuffixName() != null ? propertyObj.getUserSuffixName() : "",
						propertyObj.getUsersUserIdReturnBy(),
						 //propertyTrackingHdrList.get(i).getUsersUserId(),
						nxtSeq,//propertyObj.getPropTrkSeqNo(),
						propertyObj.getPropTrkAmt(),
			            propRetnDt,
			            propTrkDt,
			                //propertyTrackingHdrList.get(i).getPropTrkDt(),
			                //propertyTrackingHdrList.get(i).getPropRetnDt(),
			            propertyObj.getPropTrkTp(),
			            propertyObj.getPropBoxNum()
				});
				
				//System.out.println("Struct Created");
				
    			Array array = oracleConnection.createOracleArray("TB_PRTR_HDR", structs);
    			//System.out.println("Array Created");
    			
    			callableStatement.setArray(1, array);
    			//System.out.println("Params Set Successfully");
    			
    			//System.out.println("Executing procedure...");
                boolean hasResults = callableStatement.execute();
                
                //System.out.println("Executed Successfully");
                
                Map<String, Object> response = new HashMap<>();
                response.put("nextSequence", nxtSeq);
                response.put("status", "SUCCESS");
                return response;
				
			}catch (Exception e) {
	            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
	            System.err.println("Error: " + errorMessage);
	            
	            if (e instanceof SQLException) {
	                SQLException sqlEx = (SQLException) e;
	                System.err.println("SQL State: " + sqlEx.getSQLState());
	                System.err.println("Error Code: " + sqlEx.getErrorCode());
	            }
	            
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
	        }
			
		}catch (SQLException e) {
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
        }
	}
	
	// Next Seq for Property Hdr
	public Integer genNextPropertySeq() {
		return jdbcTemplate.queryForObject("select SF_GEN_SEQ_MDOC('0210') from dual", Integer.class);
	}
	
	// Next Seq for Colt Item
	public Integer genNextPropertyColtItemSeq() {
		return jdbcTemplate.queryForObject("select SF_GEN_SEQ_MDOC('0211') from dual", Integer.class);
	}
	
	// Next Seq for Issd Item
	public Integer genNextPropertyIssdItemSeq() {
		return jdbcTemplate.queryForObject("select SF_GEN_SEQ_MDOC('0212') from dual", Integer.class);
	}
	
	public String getPropertyCommitNo(String sbiNo) {
		String sql = "select Sf_get_commit_no(?) from dual";
	    String commitNo = jdbcTemplate.queryForObject(sql, new Object[]{sbiNo}, String.class);
	    return commitNo;
	}
	
	@Transactional
	public void updateColtItems(Integer propTrkSeq, List<PropertyTrackingColtItems> updateSet) {
			for (PropertyTrackingColtItems item : updateSet) {
				String propItmRowId = item.getPropItmRowid();
				
				if (propItmRowId != null && !propItmRowId.trim().isEmpty()) {
					
					String procedureCall = "{call spkg_omnet_wrapper.sp_prtr_itms_colt_update(?, ?, ?, ?)}";
					//System.out.println("<<inside update service>>");
					
					try (Connection connection = dataSource.getConnection();
							
				             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
		        	
		        	String itmItemCd = item.getItemCd();
		            Integer propInmtSno = item.getPropInmtItmSeqNo();
		        	
		        	List<PropertyTrackingColtItems> propertyTrackingColtList = new ArrayList<>();
		        	propertyTrackingColtList.add(item);
		        	
		        	try {
		    			OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
		    			Struct[] structs = new Struct[propertyTrackingColtList.size()];
		    			for (int i = 0; i < propertyTrackingColtList.size(); i++) {
		    				    PropertyTrackingColtItems obj = propertyTrackingColtList.get(i);
		    				    
		    				    structs[i] = oracleConnection.createStruct("TY_PRTR_ITMS_COLT", new Object[]{
		    				    		obj.getPropItmRowid(),
		    				    		obj.getPthPropTrkSeqNo(),
		    				    		obj.getItmItemCd(),
		    				    		obj.getItemCd(),
		    				    		obj.getPropDesc(),
		    				    		obj.getPropInmtItmSeqNo(),
		    				    		obj.getPropInmtItmQty(),
		    				    		obj.getPropInmtItmDesc(),
		    				    		obj.getPropInmtItmRetnQty(),
		    				    		obj.getPropInmtRetnFstNm() != null ? item.getPropInmtRetnFstNm() : "",
		    				    		obj.getPropInmtRetnLstNm() != null ? item.getPropInmtRetnLstNm() : "",
		    				    		obj.getPropInmtRetnMidNm() != null ? item.getPropInmtRetnMidNm() : "",
		    				    		obj.getPropInmtRetnSfx() != null ? item.getPropInmtRetnSfx() : "",
		    				    		obj.getRelationshipCode()
		    				    });
		    				}
		    			//System.out.println("Struct Created");
		    			Array array = oracleConnection.createOracleArray("TB_PRTR_ITMS_COLT", structs);

		    			callableStatement.setInt(1, propTrkSeq);
		    			callableStatement.setInt(2, propInmtSno);
		    			callableStatement.setString(3, itmItemCd);
		    			callableStatement.setArray(4, array);
		    			//System.out.println("Params Set Successfully");
		    			
		    			//System.out.println("Executing procedure...");
		                boolean hasResults = callableStatement.execute();
		                
		                //System.out.println("Executed Successfully");
		    			
		        	}catch (Exception e) {
			            
			            // Improved error handling
			            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
			            System.err.println("Error: " + errorMessage);
			            
			            if (e instanceof SQLException) {
			                SQLException sqlEx = (SQLException) e;
			                System.err.println("SQL State: " + sqlEx.getSQLState());
			                System.err.println("Error Code: " + sqlEx.getErrorCode());
			            }
			            
			            e.printStackTrace();
			            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
			        }
					}
					catch (SQLException e) {
						String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
				        System.err.println("Connection error: " + errorMessage);
				        e.printStackTrace();
				        throw new RuntimeException("Error with database connection: " + errorMessage, e);
			        }
			}
		}
	}
	
	@Transactional
	public void updateIssdItems(Integer propTrkSeq, List<PropertyTrackingColtItems> updateSet) {
		
		String procedureCall = "{call spkg_omnet_wrapper.sp_prtr_itms_issd_update(?, ?, ?, ?)}";
		//System.out.println("<<inside update service>>");
		
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)){
			for (PropertyTrackingColtItems item : updateSet) {
				String itmItemCd = item.getItemCd();
	            Integer propInmtSno = item.getPropInmtItmSeqNo();
				try {
					OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
	    			Struct[] structs = new Struct[1];
	    			structs[0] = oracleConnection.createStruct("TY_PRTR_ITMS_ISSD", new Object[]{
	    					item.getPropItmRowid(),
	    					item.getPthPropTrkSeqNo(),
				    		item.getItmItemCd(),
				    		item.getItemCd(),
				    		item.getPropDesc(),
				    		item.getPropInmtItmSeqNo(),
				    		item.getPropInmtItmQty(),
				    		item.getPropInmtItmDesc(),
				    		item.getPropInmtItmRetnQty()
				    });
	    			//System.out.println("Struct Created");
	    			Array array = oracleConnection.createOracleArray("TB_PRTR_ITMS_ISSD", structs);
	    			
	    			callableStatement.setInt(1, propTrkSeq);
	    			callableStatement.setInt(2, propInmtSno);
	    			callableStatement.setString(3, itmItemCd);
	    			callableStatement.setArray(4, array);
	    			//System.out.println("Params Set Successfully");
	    			
	    			//System.out.println("Executing procedure...");
	                boolean hasResults = callableStatement.execute();
	                
	                //System.out.println("Executed Successfully");	
				}
				catch (Exception e) {
		            
		            // Improved error handling
		            String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown error";
		            System.err.println("Error: " + errorMessage);
		            
		            if (e instanceof SQLException) {
		                SQLException sqlEx = (SQLException) e;
		                System.err.println("SQL State: " + sqlEx.getSQLState());
		                System.err.println("Error Code: " + sqlEx.getErrorCode());
		            }
		            
		            e.printStackTrace();
		            throw new RuntimeException("Error executing stored procedure: " + errorMessage, e);
		        }
			}
		}
		catch (SQLException e) {
			String errorMessage = (e.getMessage() != null) ? e.getMessage() : "Unknown connection error";
	        System.err.println("Connection error: " + errorMessage);
	        e.printStackTrace();
	        throw new RuntimeException("Error with database connection: " + errorMessage, e);
        }
	}
	
	public PropertyTrackingHdr mapToPropertyHdrObj(Map<String, Object> map) {
		PropertyTrackingHdr propertyObj = new PropertyTrackingHdr();
	    
	    propertyObj.setInmateCommitNo((String) map.get("INMATE_COMMIT_NO"));
	    propertyObj.setInstitutionInstNum((String) map.get("INSTITUTION_INST_NUM"));
	    propertyObj.setUsersUserId((String) map.get("USERS_USER_ID"));
	    propertyObj.setUserFirstName((String) map.get("USER_FIRST_NAME"));
	    propertyObj.setUserLastName((String) map.get("USER_LAST_NAME"));
	    propertyObj.setUserMidName((String) map.get("USER_MID_NAME"));
	    propertyObj.setUserSuffixName((String) map.get("USER_SUFFIX_NAME"));
	    propertyObj.setUsersUserIdReturnBy((String) map.get("USERS_USER_ID_RETURN_BY"));
	    propertyObj.setPropTrkSeqNo((Integer) map.get("PROP_TRK_SEQ_NO"));
	    Object amt = map.get("PROP_TRK_AMT");

	    if (amt instanceof Number) {
	        propertyObj.setPropTrkAmt(((Number) amt).doubleValue());
	    } else if (amt instanceof String) {
	    	String amtStr = ((String) amt).trim();
	        if (amtStr.isEmpty()) {
	            propertyObj.setPropTrkAmt(0.0); // or set to 0.0 if that's what you want
	        }else {
		        try {
		            propertyObj.setPropTrkAmt(Double.parseDouble((String) amtStr));
		        } catch (NumberFormatException e) {
		            throw new IllegalArgumentException("Invalid PROP_TRK_AMT value: " + amt);
		        }
	        }
	    } else {
	        propertyObj.setPropTrkAmt(0.0); // Default value or throw an exception
	    }

	    propertyObj.setPropRetnDt((String) map.get("PROP_RETN_DT"));
	    propertyObj.setPropTrkDt((String) map.get("PROP_TRK_DT"));
	    propertyObj.setPropTrkTp((String) map.get("PROP_TRK_TP"));
	    propertyObj.setPropBoxNum((String) map.get("PROP_BOX_NUM"));
	    
	    return propertyObj;
	}

	private Date convertToTimestamp(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
	        return null;
	    }
	    try {
	        // Parse the ISO 8601 format
	        // Example: "2025-01-31T05:00:00.000+00:00"
	        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	        Date parsedDate = isoFormat.parse(dateStr);
	        
	        // Convert to java.sql.Date, which removes the time part
	        return new java.sql.Date(parsedDate.getTime());
	    } catch (ParseException e) {
	        e.printStackTrace();
	        throw new IllegalArgumentException("Invalid date format: " + dateStr);
	    }
	}
	
}
