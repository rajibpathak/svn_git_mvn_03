package com.omnet.cnt.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omnet.cnt.Repository.VitalcheckboxRepository;

@Service
public class VitalService {
	
	@Autowired
	private VitalcheckboxRepository checkbox;
	

	@Autowired
    private DataSource dataSource;
	
	public Map<String, Object> sexOffenseHistory(String COMMIT_NO) {
		Map<String, Object> SH=checkbox.sexHistory(COMMIT_NO);
		return SH;
	}
	
	public Map<String, Object> State(String v_commit_no){
		Map<String, Object> State=checkbox.state(v_commit_no);
		return State;
	}
	
	public Map<String, Object> nocontact(String COMMIT_NO) {
		Map<String, Object> NC=checkbox.givecontactinfo(COMMIT_NO);
		return NC;
	}
	
	public Map<String, Object> MentalHealth(String COMMIT_NO) {
		Map<String, Object> MH=checkbox.Mentalhealth(COMMIT_NO);
		return MH;
	}
	
	public Map<String, Object> LegalIssue(String COMMIT_NO) {
		Map<String, Object> MH=checkbox.LegalIssue(COMMIT_NO);
		return MH;
	}
	
	public List<Map<String,Object>> VitalCheckboxProcedure(String v_commit_no) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call SPKG_CBI_VSTA.INMATE_COLOR(?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	        	stmt.setString(1, v_commit_no);
	            stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
	            stmt.execute();
	        	try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
	        		ResultSetMetaData metaData = rs.getMetaData();
	        		int columnCount = metaData.getColumnCount();
	        		while (rs.next()) {
	        		Map<String, Object> resultMap = new HashMap<>();
	        		for (int i = 1; i <= columnCount; i++) {
	        		String columnName = metaData.getColumnName(i);
	        		Object columnValue = rs.getObject(i);
	        		resultMap.put(columnName, columnValue);
	        		}
	        		resultList.add(resultMap);
	        		}
	        		}
	        	
     
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultList;
	}
	

	public List<Map<String,Object>> VitalInmateAddress(String v_commit_no) {
	    List<Map<String, Object>> resultset = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call SPKG_CBI_VSTA.INM_ADDRESS(?, ?)}"; 
	        try (CallableStatement Inmate = conn.prepareCall(sql)) {
	        	Inmate.setString(1, v_commit_no);
	        	Inmate.registerOutParameter(2, java.sql.Types.REF_CURSOR);
	        	Inmate.execute();
	        	try (ResultSet rs = (ResultSet) Inmate.getObject(2)) {
	        		ResultSetMetaData metaData = rs.getMetaData();
	        		int columnCount = metaData.getColumnCount();
	        		while (rs.next()) {
	        		Map<String, Object> resultList = new HashMap<>();
	        		for (int i = 1; i <= columnCount; i++) {
	        		String columnName = metaData.getColumnName(i);
	        		Object columnValue = rs.getObject(i);
	        		resultList.put(columnName, columnValue);
	        		}
	        		resultset.add(resultList);
	        		}
	        		}
	        	
     
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultset;
	}
	
	
	public Map<String, Object> VitalSexOffender(String v_commit_no) {
		Map<String, Object> result = new HashMap<>();
		try (Connection conn = dataSource.getConnection()) {
		String sql = "{call SPKG_CBI_VSTA.SP_SEX_REGISTRY_DATA(?,?,?)}";
		try (CallableStatement stmt = conn.prepareCall(sql)) {
		stmt.setString(1, v_commit_no);
		stmt.registerOutParameter(2, Types.VARCHAR);
		stmt.registerOutParameter(3, Types.VARCHAR);
		stmt.execute();
		
		 String v_date_notified = stmt.getString(2);
         String v_sex_registry_agency = stmt.getString(3);
		
		result.put("v_commit_no", v_commit_no);
		result.put("v_date_notified", v_date_notified);
		result.put("v_sex_registry_agency",v_sex_registry_agency);
		}
		} catch (SQLException e) {
		e.printStackTrace();
		}
		return result;
		}
	
	
	public List<Map<String,Object>> OffenderDetails(String v_commit_no) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call SPKG_CBI_VSTA.sp_pos_query(?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.registerOutParameter(1, Types.REF_CURSOR);
	        	stmt.setString(2, v_commit_no);
	            stmt.execute();
	        	try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
	        		ResultSetMetaData metaData = rs.getMetaData();
	        		int columnCount = metaData.getColumnCount();
	        		while (rs.next()) {
	        		Map<String, Object> resultMap = new HashMap<>();
	        		for (int i = 1; i <= columnCount; i++) {
	        		String columnName = metaData.getColumnName(i);
	        		Object columnValue = rs.getObject(i);
	        		resultMap.put(columnName, columnValue);
	        		}
	        		resultList.add(resultMap);
	        		}
	        		}
	        	
     
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultList;
	}
	
	public List<Map<String,Object>> NoContact(String v_commit_no) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call SPKG_CBI_VSTA.INMATE_COLOR(?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	        	stmt.setString(1, v_commit_no);
	            stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
	            stmt.execute();
	        	try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
	        		ResultSetMetaData metaData = rs.getMetaData();
	        		int columnCount = metaData.getColumnCount();
	        		while (rs.next()) {
	        		Map<String, Object> resultMap = new HashMap<>();
	        		for (int i = 1; i <= columnCount; i++) {
	        		String columnName = metaData.getColumnName(i);
	        		Object columnValue = rs.getObject(i);
	        		resultMap.put(columnName, columnValue);
	        		}
	        		resultList.add(resultMap);
	        		}
	        		}
	        	
     
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultList;
	}
	
}
