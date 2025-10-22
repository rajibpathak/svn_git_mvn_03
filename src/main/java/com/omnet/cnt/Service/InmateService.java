package com.omnet.cnt.Service;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import oracle.sql.ARRAY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;


import com.omnet.cnt.Model.Inmate;
import com.omnet.cnt.Model.InmateDocsIds;
import com.omnet.cnt.Repository.InmateRepository;
import com.omnet.cnt.Repository.InmateTypeRepo;
import com.omnet.cnt.Repository.InmatedocidsRepository;
import com.omnet.cnt.Repository.InmatemugshotRepository;

import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;

@Service
public class InmateService {
	
	 @Autowired
	    private EntityManager entityManager;
	
	 @Autowired
	    private  InmateRepository inmateRepository;
	 @Autowired
	 private InmatemugshotRepository inmatemugshotRepository;
	 @Autowired
	 private  InmatedocidsRepository inmatedocidsRepository;
	  @Autowired
	    private DataSource dataSource;  
	  
	  @Autowired
	    private  InmateTypeRepo inmatetyerepo;
	  
	  private final JdbcTemplate jdbcTemplate;
	  
	   @Autowired
	    public InmateService(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }
	   
	    private SimpleJdbcCall simpleJdbcCall;

	  
	  
	public String HardSbi(String HardSbi) {
		String SbiValueHard=inmateRepository.FindHardSbiNo(HardSbi);
		return SbiValueHard;
	}

		public String validSbiNo(String parameter) {
				String result = inmateRepository.findSbiNumber(parameter); 
			   System.out.println(result);
			return result;
		   }
	
		public String validCommitNo(String parameter) {
			
			   String result = inmateRepository.findCommitNumber(parameter);
			   System.out.println(" commitNovalid:"+result);
			return result;
		    }
		 public List<Object> findCustomInmateData(String commitNo, String sbiNo) {
			 List<Object> customInmateData = inmateRepository.findCustomInmateData(commitNo, sbiNo);
			 System.out.println(commitNo);
			 System.out.println(sbiNo);
			 System.out.println(customInmateData);
		        return customInmateData;
		     }
		 public String getInmateCondition(String ivc_commit_no) {
		        return inmateRepository.getInmateCondition(ivc_commit_no);
		    }
		 public List<Object> findOffenderName(String commitNo, String sbiNo) {
		        return inmateRepository.findOffenderName(commitNo, sbiNo);
		    }
		 public List<InmateDocsIds> getInmateDocIdsByCommitNo(String commitNo) {
		        return inmatedocidsRepository.findByCommitNo(commitNo);
		    }
		 
		  public String getUserInstNum(String user) {
		        return inmateRepository.getUserInstNum(user);
		    }
		    public String getInmateInst(String commitNo) {
		        return inmateRepository.getInmateInst(commitNo);
		    }
		    
		    public List<Object[]> findInmateDetailsBySbiNo(String sbiNo) {
		        List<Object[]> HistoryList = inmateRepository.findInmateDetailsBySbiNo(sbiNo);
				return HistoryList;
		    }
		  public Map<String, Object> callGetLocationDetails(String sbiNo, String commitNo) {
		        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("sp_get_location_details");
		        storedProcedureQuery.registerStoredProcedureParameter("ivc_sbi_no", String.class, ParameterMode.IN);
		        storedProcedureQuery.registerStoredProcedureParameter("ivc_commit_no", String.class, ParameterMode.IN);
		        storedProcedureQuery.registerStoredProcedureParameter("ovc_location1", String.class, ParameterMode.OUT);
		        storedProcedureQuery.registerStoredProcedureParameter("ovc_security_lvl1", String.class, ParameterMode.OUT);
		        storedProcedureQuery.registerStoredProcedureParameter("ovc_assigned_officer1", String.class, ParameterMode.OUT);
		        storedProcedureQuery.registerStoredProcedureParameter("ovc_location2", String.class, ParameterMode.OUT);
		        storedProcedureQuery.registerStoredProcedureParameter("ovc_security_lvl2", String.class, ParameterMode.OUT);
		        storedProcedureQuery.registerStoredProcedureParameter("ovc_assigned_officer2", String.class, ParameterMode.OUT);
		        storedProcedureQuery.setParameter("ivc_sbi_no", sbiNo);
		        storedProcedureQuery.setParameter("ivc_commit_no", commitNo);
		        storedProcedureQuery.execute();
		        Map<String, Object> outputParams = new HashMap<>();
		        outputParams.put("ovc_location1", storedProcedureQuery.getOutputParameterValue("ovc_location1"));
		        outputParams.put("ovc_security_lvl1", storedProcedureQuery.getOutputParameterValue("ovc_security_lvl1"));
		        outputParams.put("ovc_assigned_officer1", storedProcedureQuery.getOutputParameterValue("ovc_assigned_officer1"));
		        outputParams.put("ovc_location2", storedProcedureQuery.getOutputParameterValue("ovc_location2"));
		        outputParams.put("ovc_security_lvl2", storedProcedureQuery.getOutputParameterValue("ovc_security_lvl2"));
		        outputParams.put("ovc_assigned_officer2", storedProcedureQuery.getOutputParameterValue("ovc_assigned_officer2"));
		        System.out.println("Output Parameters:");
		        outputParams.forEach((key, value) -> System.out.println(key + ": " + value));	    
		        return outputParams;
		    }
		  
			
			
			 public boolean isOffenderProtected(String sbiNo) {
			        String result = inmateRepository.checkIfOffenderIsProtected(sbiNo);
			        System.out.println("resulteew:"+result);
			        return "Y".equals(result);
			    }	 
		
			 public List<Map<String, Object>> callStayHistoryQuery(String sbiNo, String docIdFlag) {
				    if (sbiNo == null || docIdFlag == null) {
				        throw new IllegalArgumentException("Input parameters cannot be null");
				    }

				    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
				            .withCatalogName("SPKG_OMNET_WRAPPER")
				            .withProcedureName("sp_cbi_stay_history_query")
				            .declareParameters(
				                    new SqlOutParameter("resultset", OracleTypes.ARRAY, "TB_CBI_STAY_PREV_STAY"),
				                    new SqlParameter("P_sbi_no", java.sql.Types.VARCHAR),
				                    new SqlParameter("p_docid_flag", java.sql.Types.VARCHAR)
				            );
				    SqlParameterSource inParams = new MapSqlParameterSource()
				            .addValue("P_sbi_no", sbiNo)
				            .addValue("p_docid_flag", docIdFlag);
				    List<Map<String, Object>> outputList = new ArrayList<>();
				    try {				      
				        Map<String, Object> result = jdbcCall.execute(inParams);
				        ARRAY array = (ARRAY) result.get("resultset");				        
				        if (array != null) {
				            Object[] arrayData = (Object[]) array.getArray();			            
				            for (Object obj : arrayData) {
				                if (obj instanceof STRUCT) {
				                    STRUCT struct = (STRUCT) obj;
				                    Object[] attributes = struct.getAttributes();
				                    Map<String, Object> rowMap = new HashMap<>();
				                    rowMap.put("commit_no", attributes[0]);  
				                    rowMap.put("stay", attributes[1]);    
				                    rowMap.put("inst_num", attributes[2]); 
				                    rowMap.put("inst_name", attributes[3]);
				                    rowMap.put("inmate_name", attributes[4]); 
				                    rowMap.put("inmt_tp_cd", attributes[5]);
				                    rowMap.put("sent_type_desc", attributes[6]); 
				                    rowMap.put("inst_admiss_date", attributes[7]);
				                    rowMap.put("inst_admiss_time", attributes[8]); 
				                    rowMap.put("inst_release_date", attributes[9]);
				                    rowMap.put("inst_release_time", attributes[10]); 
				                    rowMap.put("status", attributes[11]); 
				                    outputList.add(rowMap);
				                }
				            }
				        }
				    } catch (SQLException e) {
				        e.printStackTrace();
				        throw new RuntimeException("Error processing the result set", e);
				    }
				    outputList.forEach(row -> {
				        row.forEach((key, value) -> System.out.println(key + ": " + value));
				    });

				    return outputList;
				}
			 
			   public List<Object[]> getActiveInmates() {
			        List<Object[]> results = inmatetyerepo.findActiveInmatesByTypeAndStatus();
			        System.out.println("dfafaff"+results);
					return results;
			    }
			   
			   
	
			   public byte[] getInmateImage(String sbiNo, String user) {
				    System.out.println("resultimagesbino" + sbiNo);
				    System.out.println("resultimage" + user);

				    // Set up the JDBC call
				    SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				            .withCatalogName("Spkg_Heterogenous")
				            .withProcedureName("Sp_Get_Image")
				            .withoutProcedureColumnMetaDataAccess()
				            .declareParameters(
				                    new SqlParameter("P_Sbi_No", Types.VARCHAR),
				                    new SqlParameter("p_user_id", Types.VARCHAR)
				            );

				    // Execute stored procedure
				    Map<String, Object> result = simpleJdbcCall.execute(Map.of(
				            "P_Sbi_No", sbiNo,
				            "p_user_id", user
				    ));

				    byte[]  imageData = inmatemugshotRepository.findImageByAccessedDateAndUserIdAndSbiNo(user, sbiNo);
				    
				    System.out.println("imageData: " + (imageData != null ? "Data found" : "No data"));

				    return (imageData != null && imageData.length > 0) ? imageData : null;
				}

			   
			   
}
	


