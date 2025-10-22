package com.omnet.cnt.Service;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import javax.sql.DataSource;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.warrantCheck;

import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.STRUCT;


@Service
public class warrantCheckService {
	
	@Autowired
    private DataSource dataSource;
	

	 @SuppressWarnings("deprecation")
	public List<warrantCheck> WarrantcheckM(String ivc_sbi_no) {
	        List<warrantCheck> warrantCheck = new ArrayList<>();

	        
	        try (Connection connection = dataSource.getConnection()) {
	            CallableStatement callableStatement = connection.prepareCall("{call SPKG_CBI_WARR.SP_CBI_WARR_QUERY(?, ?)}");
	            callableStatement.registerOutParameter(1, OracleTypes.ARRAY, "TB_CBI_WARR_QUERY");
	            callableStatement.setString(2, ivc_sbi_no);

	            callableStatement.execute();
	            Array array = callableStatement.getArray(1);

	            Object[] arrayData = (Object[]) array.getArray();
	            for (Object obj : arrayData) {

	                if (obj instanceof STRUCT) {
	                    System.out.println("Comments:");
	                    STRUCT struct = (STRUCT) obj;
	                    Object[] attributes = struct.getAttributes();

	                    warrantCheck warrantCheckObject = new warrantCheck();
	                
	                    warrantCheckObject.setSbi_no((String) attributes[0]);
	                    warrantCheckObject.setWarrant_number((String) attributes[1]);
	                    warrantCheckObject.setWarrant_type((String) attributes[2]);
	                    warrantCheckObject.setIssue_date((Timestamp) attributes[3]);
	                    warrantCheckObject.setCharge_seq((String) attributes[4]);
	                    warrantCheckObject.setAgency((String) attributes[5]);
	                    warrantCheckObject.setAgency_county((String) attributes[6]);
	                    warrantCheckObject.setComments((String) attributes[7]);
	                    warrantCheckObject.setPreparer_last((String) attributes[8]);
	                    warrantCheckObject.setPreparer_first((String) attributes[9]);
	                    warrantCheckObject.setPreparer_mi((String) attributes[10]);
	                    warrantCheckObject.setPreparer_suff((String) attributes[11]);

	                    warrantCheck.add(warrantCheckObject);
	                }
	            }
	            callableStatement.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return warrantCheck;
	    }
	


     
	 public Map<String, Object> WarrantCheckExcuteQuery(String ivc_pass, String ivc_program_code, String ivc_sbi_no, 
             String ivc_param1, String ivc_param2, 
             String ivc_param3, String ivc_param4, 
             String ivc_param5) throws SQLException {
         try (Connection connection = dataSource.getConnection();
         CallableStatement callableStatement = connection.prepareCall("{call SPKG_ENTIREXBROKER.Execute_query(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
        	 callableStatement.setString(1, ivc_pass);
        	 callableStatement.setString(2, ivc_program_code);
        	 callableStatement.setString(3, ivc_sbi_no);
        	 callableStatement.registerOutParameter(4, Types.VARCHAR); 
        	 callableStatement.registerOutParameter(5, Types.VARCHAR); 
             callableStatement.setString(6, ivc_param1 != null ? ivc_param1 : null);
             callableStatement.setString(7, ivc_param2 != null ? ivc_param2 : null);
             callableStatement.setString(8, ivc_param3 != null ? ivc_param3 : null);
             callableStatement.setString(9, ivc_param4 != null ? ivc_param4 : null);
             callableStatement.setString(10, ivc_param5 != null ? ivc_param5 : null);

             callableStatement.execute();


           Map<String, Object> resultMap = new HashMap<>();
           resultMap.put("ovc_process", callableStatement.getString(4));
           resultMap.put("ovc_return_data", callableStatement.getString(5));


           return resultMap;
              
          }
}
	   
	   

}
