package com.omnet.cnt.Service;

import com.omnet.cnt.Model.Inmate;
import com.omnet.cnt.Model.Rowdata;
import com.omnet.cnt.Repository.gangrepository;
import com.omnet.cnt.classes.InmateData;
import java.sql.ResultSet;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import java.util.HashMap;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

@Service
public class stgservice {
	@Autowired
    private DataSource dataSource;
	

    @Autowired
    private gangrepository gangRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Object[]> getGangDetails() {
        return gangRepository.findGangDetails();
    }
    public List<Map<String, Object>> callSpStgQuery(String commitNo) {
        try {
            SimpleJdbcCall spStgQueryCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("Spkg_Cbi_Stg") 
                    .withProcedureName("sp_stg_query")
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(
                            new SqlParameter("i_vc_commit_no", OracleTypes.VARCHAR),
                            new SqlOutParameter("query_set", OracleTypes.CURSOR, 
                                (rs, rowNum) -> {
                                    Map<String, Object> row = new HashMap<>();
                                    row.put("iga_rowid", rs.getString("iga_rowid"));
                                    row.put("gang_name", rs.getString("gang_name"));
                                    row.put("gang_position", rs.getString("gang_position"));
                                    row.put("gang_leader", rs.getString("gang_leader"));
                                    row.put("gang_ai_ind", rs.getString("gang_ai_ind"));
                                    row.put("stg_sef_repd_status", rs.getString("stg_sef_repd_status"));
                                    row.put("gang_comments", rs.getString("gang_comments"));
                                    row.put("gang_code", rs.getString("gang_code"));
                                    row.put("commit_no", rs.getString("commit_no"));
                                    row.put("gang_del_flg", rs.getString("gang_del_flg"));
                                    row.replaceAll((key, value) -> value == null ? "" : value);
                                    return row;
                                }
                            )                          
                    );           
            Map<String, Object> inputParams = Map.of("i_vc_commit_no", commitNo);
            Map<String, Object> result = spStgQueryCall.execute(inputParams);
            return (List<Map<String, Object>>) result.get("query_set");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing sp_stg_query procedure", e);
        }
    }


    public void saveStgData(String commitNo, List<Map<String, String>> stgRecords, Timestamp gangSelectDateTime) {
        List<Rowdata> stgInfoList = new ArrayList<>();       
        for (Map<String, String> record : stgRecords) {
            Rowdata rowdata = new Rowdata();
            rowdata.setCommitNo(commitNo);
            rowdata.setGangName(record.get("gangName"));
            rowdata.setGangPosition(record.get("gangPosition"));
            rowdata.setGangLeader(record.get("gangLeader"));
            rowdata.setGangAiInd(record.get("gangAiInd"));
            rowdata.setStgSefRepdStatus(record.get("stgSefRepdStatus"));
            rowdata.setGangComments(record.get("gangComments"));
            rowdata.setGangCode(record.get("gangCode"));
            rowdata.setGangDelFlg(record.get("gangDelFlg"));
            stgInfoList.add(rowdata);
            System.out.println("rowdata " + rowdata);           
        }
        processData(dataSource, commitNo, stgInfoList, gangSelectDateTime);
    }

    
    public void processData(DataSource dataSource, String commitNo, List<Rowdata> stgInfoList, Timestamp gangSelectDateTime) {
        try (Connection connection = dataSource.getConnection()) {
            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
            List<Rowdata> validRows = new ArrayList<>();
            for (Rowdata row : stgInfoList) {
                if (row.getIgaRowid() == null) row.setIgaRowid(null);
                if (row.getGangName() == null) row.setGangName(null);
                if (row.getGangPosition() == null) row.setGangPosition(null);
                if (row.getGangLeader() == null) row.setGangLeader(null);
                if (row.getStgSefRepdStatus() == null) row.setStgSefRepdStatus(null);
                if (row.getGangAiInd() == null) row.setGangAiInd(null);
                if (row.getGangComments() == null) row.setGangComments(null);
                if (row.getGangCode() == null) row.setGangCode(null);
                if (row.getGangDelFlg() == null) row.setGangDelFlg(null);
                validRows.add(row);
            }

            if (validRows.isEmpty()) {
                throw new RuntimeException("No valid rows to process.");
            }

            // Create an Oracle Array 
            ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("TB_CBI_STG_REC_STG_INFO", oracleConnection);
            Object[] rowDataArray = validRows.stream()
                    .map(row -> new Object[]{
                            row.getIgaRowid(),
                            row.getGangName(),
                            row.getGangPosition(),
                            row.getGangLeader(),
                            row.getGangAiInd(),
                            row.getStgSefRepdStatus(),
                            row.getGangComments(),
                            row.getGangCode(),
                            row.getCommitNo(),
                            row.getGangDelFlg()
                    })
                    .toArray();
            System.out.println("blockDataArray"+rowDataArray);
            ARRAY blockDataArray = new ARRAY(descriptor, oracleConnection, rowDataArray);
              System.out.println("blockDataArray"+rowDataArray);
            // Calling stored procedure with array parameter
            String sql = "{ call SPKG_OMNET_WRAPPER.sp_cbi_stg_stg_insert(?,?) }";         
            try (CallableStatement callableStatement = oracleConnection.prepareCall(sql)) {
                callableStatement.setArray(1, blockDataArray);
                callableStatement.setTimestamp(2, gangSelectDateTime); // <<-- now it’s used
                callableStatement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing sp_cbi_stg_insert procedure", e);
        }
    }





public void updatedata(DataSource dataSource, List<Rowdata> updateList) {
	   System.out.println("blockDataArrayupdate"+updateList);
   
    try (Connection connection = dataSource.getConnection()) {
        OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
        ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("TB_CBI_STG_INFO_UPD", oracleConnection);
        Object[] rowDataArray = updateList.stream()
            .map(row -> new Object[]{
                row.getIgaRowid(),
                row.getGangName(),
                row.getGangPosition(),
                row.getGangLeader(),
                row.getGangAiInd(),
                row.getStgSefRepdStatus(),
                row.getGangComments(),
                row.getGangCode(),
                row.getCommitNo(),
                row.getGangDelFlg()
            })
            .toArray();
        System.out.println("rowDataArray"+rowDataArray);
        for (Object row : rowDataArray) {
            Object[] rowFields = (Object[]) row;
            System.out.println("blockDataArrayupdate"+rowFields);
        }
        ARRAY blockDataArray = new ARRAY(descriptor, oracleConnection, rowDataArray);
        System.out.println("blockDataArrayupdate123"+blockDataArray);
        String sql = "{call SPKG_OMNET_WRAPPER.sp_cbi_stg_update(?) }";
        try (CallableStatement callableStatement = oracleConnection.prepareCall(sql)) {
        	callableStatement.setArray(1, blockDataArray);
            callableStatement.execute();          
        }
    } catch (SQLException e) {
        e.printStackTrace();
        updateList.forEach(row -> {
            System.out.println("IgaRowid: " + row.getIgaRowid() + " (Check if this is a valid ROWID)");
        });
        throw new RuntimeException("Error executing sp_cbi_stg_update procedure", e);
    }
}


public void callSpCbiStgInmateUpdate(List<InmateData> inmateDataList) throws SQLException {
    // Print each item in the inmateDataList
    System.out.println("Inmate Data List:");
    for (InmateData inmate : inmateDataList) {
        System.out.println(inmate);
    }

    try {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("SPKG_OMNET_WRAPPER") 
                .withProcedureName("sp_cbi_stg_inmate_update") 
                .declareParameters(new SqlParameter("BLOCK_DATA", OracleTypes.ARRAY, "TB_CBI_STG_INMATE"));

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            ARRAY sqlArray = createSqlArray(inmateDataList, oracleConn);
            MapSqlParameterSource inParams = new MapSqlParameterSource()
                    .addValue("BLOCK_DATA", sqlArray, Types.ARRAY);
            Map<String, Object> result = simpleJdbcCall.execute(inParams);          
        }
    } catch (Exception e) {
        throw new SQLException("Error calling sp_cbi_stg_inmate_update: " + e.getMessage(), e);
    }
}



private ARRAY createSqlArray(List<InmateData> inmateDataList, OracleConnection oracleConn) throws SQLException {
    StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_CBI_STG_INMATE", oracleConn);
    ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_CBI_STG_INMATE", oracleConn);
    STRUCT[] structs = new STRUCT[inmateDataList.size()];
    for (int i = 0; i < inmateDataList.size(); i++) {
        InmateData inmate = inmateDataList.get(i);        
        Object[] attributes = new Object[]{
            inmate.getCommitNo(),
            inmate.getInmateRowId(),
            inmate.getSelfRepStaffAssaultFlag()
        };
        structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
    }
    return new ARRAY(arrayDescriptor, oracleConn, structs);
   }



public List<InmateData> getInmatequery(String commitNo) throws SQLException {
    List<InmateData> inmateList = new ArrayList<>();
    String procedureCall = "{ call SPKG_CBI_STG.sp_inmate_query(?, ?) }";
    try (Connection conn = dataSource.getConnection();
         CallableStatement cs = conn.prepareCall(procedureCall)) {
        cs.setString(1, commitNo);
        cs.registerOutParameter(2, OracleTypes.CURSOR);
        cs.execute();
        try (ResultSet rs = (ResultSet) cs.getObject(2)) {
            while (rs.next()) {
                InmateData inmateData = new InmateData();
                inmateData.setCommitNo(rs.getString("commit_no"));
                inmateData.setInmateRowId(rs.getString("inmate_rowid"));
                inmateData.setSelfRepStaffAssaultFlag(rs.getString("self_rep_staff_assault_flag"));
                inmateList.add(inmateData);
            }
        }
    }
    return inmateList;
}
}



