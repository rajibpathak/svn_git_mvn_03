package com.omnet.cnt.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.SecurityConfig.proxyHelper;

@Service
public class SwappingBedService {
	
	@Autowired
    private DataSource dataSource;
	
	public List<Map<String, Object>> SwapBedGet(String p_sbi_no, String userId) {
	    System.out.println("SwapBedservice=" + userId);
	    List<Map<String, Object>> resultList = new ArrayList<>();

	    try (Connection conn = proxyHelper.getProxyConnection(dataSource, userId)) {
	    	   System.out.println("SwapBedservice=" + userId);
	    	String sql = "{call SPKG_MOV_SWAP.pquery_sbi1(?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.setString(1, p_sbi_no);
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

	
	
	public void SwapBeds(String p_commit_no1, String p_commit_no2, String p_bed_no1, String p_bed_no2) throws SQLException {
		System.out.println("MovementSwap="+p_commit_no1+" "+p_commit_no2+" "+p_bed_no1+" "+p_bed_no2);
		 try (Connection conn = dataSource.getConnection()) {
		        String sql = "{call SPKG_MOV_SWAP.pswap_bed(?, ?, ?, ?)}"; 
		        	try (CallableStatement stmt = conn.prepareCall(sql)) {
			        	stmt.setString(1, p_commit_no1);
			        	stmt.setString(2, p_commit_no2);
			        	stmt.setString(3, p_bed_no1);
			        	stmt.setString(4, p_bed_no2);
			            stmt.execute();
		          }
		
	           }
		 }
	
	

}

