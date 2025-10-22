package com.omnet.cnt.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omnet.cnt.Model.CSMViewLocation;
import com.omnet.cnt.Model.Inmate;
import com.omnet.cnt.Model.SecCdRec;
import java.sql.*;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.*;

@Service
public class CountDetailService {

	  static String jdbcUrl = "jdbc:oracle:thin:@10.78.21.196:1521:devdb";
      static String username = "OMNET_APP";
      static String password = "OmnetP4ssw0rd!";

	

	@Autowired
	private EntityManager entityManager;

	public Map<String, Object> calloffenderactivitystats(String commitNo) {
			 
	        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("Spkg_Csm_View.sp_get_count_detail");

	        storedProcedureQuery.registerStoredProcedureParameter("ivc_commit_no", String.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("io_viol_cnt", String.class, ParameterMode.OUT);
	        storedProcedureQuery.registerStoredProcedureParameter("io_prog_cnt", String.class, ParameterMode.OUT);
	        storedProcedureQuery.registerStoredProcedureParameter("io_arrest_inc", String.class, ParameterMode.OUT);
	        storedProcedureQuery.registerStoredProcedureParameter("io_admin_warr", String.class, ParameterMode.OUT);
	      
	        storedProcedureQuery.setParameter("ivc_commit_no", commitNo);
	        storedProcedureQuery.execute();

	        Map<String, Object> outputParams = new HashMap<>();
	        outputParams.put("io_viol_cnt", storedProcedureQuery.getOutputParameterValue("io_viol_cnt"));
	        outputParams.put("io_prog_cnt", storedProcedureQuery.getOutputParameterValue("io_prog_cnt"));
	        outputParams.put("io_arrest_inc", storedProcedureQuery.getOutputParameterValue("io_arrest_inc"));
	        outputParams.put("io_admin_warr", storedProcedureQuery.getOutputParameterValue("io_admin_warr"));	        
	        System.out.println("Output Parameters:");
	        outputParams.forEach((key, value) -> System.out.println(key + ": " + value));
	    
	        return outputParams;
	    }
	
	public static List<SecCdRec> callSpSecCdQueryObj(String commitNo) {
        List<SecCdRec> securityDetails = new ArrayList<>();        
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password))  {
            CallableStatement callableStatement = connection.prepareCall("{call SPKG_OMNET_WRAPPER.sp_csm_view_sec_cd_query(?, ?)}");    
           
           callableStatement.setString(1, commitNo);  
           callableStatement.registerOutParameter(2, OracleTypes.ARRAY, "TB_CSM_VIEW_SEC_CD_QUERY");
            callableStatement.execute();
            ARRAY array = ((OracleCallableStatement) callableStatement).getARRAY(2);

            Object[] arrayData = (Object[]) array.getArray();

            for (Object obj : arrayData) {

                if (obj instanceof STRUCT) {
                    STRUCT struct = (STRUCT) obj;
                    Object[] attributes = struct.getAttributes();
                    SecCdRec secCdRec = new SecCdRec();
                    secCdRec.setSecCd((String) attributes[0]);
                    securityDetails.add(secCdRec);
                }
            }
            callableStatement.close();
        }catch (SQLException e) {

            e.printStackTrace();
        }

        return securityDetails;
  }
	
	
    public static List<CSMViewLocation> callSpLocation(String sbiNo) {
        List<CSMViewLocation> locationHistory = new ArrayList<>();   
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            CallableStatement callableStatement = connection.prepareCall("{call SPKG_OMNET_WRAPPER.sp_csm_view_location_query(?, ?)}");
            callableStatement.setString(1, sbiNo);

            callableStatement.registerOutParameter(2, OracleTypes.ARRAY, "TB_CSM_VIEW_LOCATION_QUERY");
            callableStatement.execute();
            ARRAY array = (ARRAY) callableStatement.getObject(2);
            Object[] arrayData = (Object[]) array.getArray();

            // Process each struct in the array
            for (Object obj : arrayData) {
                if (obj instanceof STRUCT) {
                    STRUCT struct = (STRUCT) obj;
                    Object[] attributes = struct.getAttributes();
                    CSMViewLocation location = new CSMViewLocation();
                    location.setCommitNo((String) attributes[0]);
                    location.setLocation((String) attributes[1]);
                    location.setArrivalDate(toSqlDate (attributes[2]));
                    location.setArrivalType((String) attributes[3]);
                    if (attributes[4] != null) {
                        location.setDepartDate(toSqlDate(attributes[4]));
                    } else {
                        location.setDepartDate(null); 
                    }

                    if (attributes[5] != null) {
                        location.setDepartType((String) attributes[5]);
                    } else {
                        location.setDepartType(null); 
                    }

                    locationHistory.add(location);
                }
            }

            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locationHistory;
    }
 
    
    
    private static Date toSqlDate(Object attribute) {
        if (attribute instanceof Timestamp) {
            return new Date(((Timestamp) attribute).getTime());
        } else if (attribute instanceof Date) {
            return (Date) attribute;
        } else {
            return null;
        }
    }
    

}


	

