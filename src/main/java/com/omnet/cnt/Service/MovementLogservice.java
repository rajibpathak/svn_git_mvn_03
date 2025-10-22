package com.omnet.cnt.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.Originating_Location;
import com.omnet.cnt.Repository.MoveMentLogActivity_Type_Repository;
import com.omnet.cnt.Repository.Originating_LocationRepository;
import com.omnet.cnt.Model.MoveMentLogActivity_Type;


@Service
public class MovementLogservice {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Originating_LocationRepository OL;
	
	@Autowired
	private MoveMentLogActivity_Type_Repository ActivityRepo;
	
	public List<MoveMentLogActivity_Type> MovementLogActivityType(){
		return ActivityRepo.ActivityType();
	}
	
	public List<Originating_Location> Originatinglocation(String instNum){
		return OL.Originating_Location(instNum);
	}
	
	public Map<String, Object> OFD(String ivc_commit_no, String ivc_inst_num) {
		System.out.println("MovService="+ivc_commit_no+" "+ivc_inst_num);
		Map<String, Object> result = new HashMap<>();
		try (Connection conn = dataSource.getConnection()) {
		String sql = "{call SPKG_MOV_LOSC.sp_get_housing_details(?,?,?,?,?,?,?,?,?,?)}";
		try (CallableStatement MovLog = conn.prepareCall(sql)) {
			MovLog.setString(1, ivc_commit_no);
			MovLog.setString(2, ivc_inst_num);
			MovLog.registerOutParameter(3, Types.DATE);
			MovLog.registerOutParameter(4, Types.DATE);
			MovLog.registerOutParameter(5, Types.VARCHAR);
			MovLog.registerOutParameter(6, Types.VARCHAR);
			MovLog.registerOutParameter(6, Types.VARCHAR);
			MovLog.registerOutParameter(7, Types.VARCHAR);
			MovLog.registerOutParameter(8, Types.VARCHAR);
			MovLog.registerOutParameter(9, Types.VARCHAR);
			MovLog.registerOutParameter(10, Types.VARCHAR);
			MovLog.execute();
		
         String odt_from_date = MovLog.getString(3);
         String odt_to_date = MovLog.getString(4);
         String ovc_bld = MovLog.getString(5);
         String ovc_unit = MovLog.getString(6);
         String ovc_floor = MovLog.getString(7);
         String ovc_tier = MovLog.getString(8);
         String ovc_cell = MovLog.getString(9);
         String ovc_bed = MovLog.getString(10);

		
		result.put("ivc_commit_no", ivc_commit_no);
		result.put("ivc_inst_num", ivc_inst_num);
		result.put("odt_from_date",odt_from_date);
		result.put("odt_to_date",odt_to_date);
		result.put("ovc_bld",ovc_bld);
		result.put("ovc_unit", ovc_unit);
		result.put("ovc_floor", ovc_floor);
		result.put("ovc_tier", ovc_tier);
		result.put("ovc_cell", ovc_cell);
		result.put("ovc_bed", ovc_bed);
		System.out.println("MovResult"+result); 
		}
		} catch (SQLException e) {
		e.printStackTrace();
		}
		return result;
		}
	
	
	public List<Map<String,Object>> MovTablec(String IOVC_SBI_NO,String IOVC_INST_NUM,String IOVC_ACTVY_TYP_CD,String IOVC_DEPART_LOC,String IOVC_ARRIV_LOC,Date IOVC_DATE_FROM,Date IOVC_DATE_TO,String ivc_from_time,String ivc_to_time,String ivc_order_by,String inu_asc_desc) {
	System.out.println("Movservice="+IOVC_INST_NUM+" "+IOVC_DATE_FROM+" "+IOVC_DATE_TO);
    List<Map<String, Object>> resultList = new ArrayList<>();
    

    
    try (Connection conn = dataSource.getConnection()) {
        String sql = "{call SPKG_MOV_LOSC.SP_MOV_LOSC_QUERY(?, ?, ?, ?, ?, ? , ?, ?, ?, ? , ?, ?)}"; 
        try (CallableStatement stmt = conn.prepareCall(sql)) {
        	stmt.setString(1, IOVC_SBI_NO);
        	stmt.setString(2, IOVC_INST_NUM);
        	stmt.setString(3, IOVC_ACTVY_TYP_CD);
        	stmt.setString(4, IOVC_DEPART_LOC);
        	stmt.setString(5, IOVC_ARRIV_LOC);
        	stmt.setDate(6, IOVC_DATE_FROM);
        	stmt.setDate(7, IOVC_DATE_TO);
        	stmt.setString(8, ivc_from_time);
        	stmt.setString(9, ivc_to_time);
        	stmt.setString(10, ivc_order_by);
        	stmt.setString(11, inu_asc_desc);
            stmt.registerOutParameter(12, java.sql.Types.REF_CURSOR);
            stmt.execute();
        	try (ResultSet rs = (ResultSet) stmt.getObject(12)) {
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
