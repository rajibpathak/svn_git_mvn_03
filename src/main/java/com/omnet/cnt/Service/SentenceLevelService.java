package com.omnet.cnt.Service;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.inmatecomcharges;
import com.omnet.cnt.Repository.SentenceLevelRepository;
import com.omnet.cnt.Repository.inmatecomchargerepository;

import oracle.jdbc.OracleTypes;

@Service
public class SentenceLevelService {
	
    private final JdbcTemplate jdbcTemplate;

    public SentenceLevelService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private SentenceLevelRepository sentenceLevelrepository;
   
	@Autowired
    private inmatecomchargerepository inmatecomchargerepository;
	

	 
	 public List<String> getDetentionerByCommitNo(String commitNo) {
	        return inmatecomchargerepository.findDetentionerByCommitNo(commitNo);
	    }
	 
	 public List<Object[]> findByCommitNo1(String commitNo) {
		 List<Object[]> sentenceLevelData = sentenceLevelrepository.findByCommitNo(commitNo);
		  for (Object[] data : sentenceLevelData) {
		        for (Object obj : data) {
		            System.out.println(obj);
		        }
		    }
	        return sentenceLevelData;
	    }
	 public List<Map<String, Object>> getSentenceLevels(String commitNo) {
	        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
	                .withCatalogName("SPKG_CR_STRP") 
	                .withProcedureName("p_sel_sent_level")
	                .withoutProcedureColumnMetaDataAccess()
	                .declareParameters(
	                		new SqlOutParameter("resultset", OracleTypes.CURSOR),
	                        new SqlParameter("p_commit_no", Types.VARCHAR)
	                        
	                );

	        Map<String, Object> result = simpleJdbcCall.execute(Map.of("p_commit_no", commitNo));
	        System.out.println("procedure"+result);

	        return (List<Map<String, Object>>) result.get("resultset"); // Ensure the key matches the cursor name
	    }

	public List<Object[]> findByCommitNo(String commitNumb) {
		// TODO Auto-generated method stub
		return null;
	}
  
}
