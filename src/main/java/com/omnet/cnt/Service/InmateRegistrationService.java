package com.omnet.cnt.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.AdmissionRelease;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException; 
import java.text.SimpleDateFormat;


@Service
public class InmateRegistrationService {

    @Autowired
    private DataSource dataSource;

    public List<Map<String, String>> getDistinctRaces() {
        List<Map<String, String>> races = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT DISTINCT Race_Code, Race_Desc FROM race_rt ORDER BY Race_Desc";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> race = new HashMap<>();
                    race.put("raceCode", rs.getString("Race_Code"));
                    race.put("raceDesc", rs.getString("Race_Desc"));
                    races.add(race);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return races;
    }

    
    public List<Map<String, Object>> getCombinedInmateDetailsBySbiNo(String sbiNo) {
        List<Map<String, Object>> resultList = new ArrayList<>();
       
        String sql = "{call DACS.SPKG_INM_BOOKING.sp_inmate_query(?, ?)}";
        try (Connection conn = dataSource.getConnection()) {
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
                stmt.setString(2, sbiNo);
                stmt.execute();

                try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (rs.next()) {
                        Map<String, Object> rowMap = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            rowMap.put(metaData.getColumnName(i), rs.getObject(i));
                        }
                        resultList.add(rowMap);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching combined inmate details for SBI No: " + sbiNo, e);
        }
        return resultList;
    }
    
//    private String getCountyNameByCode(Connection conn, String countyCode) throws SQLException {
//        if (countyCode == null) return null;
//        String query = "SELECT COUNTY_NAME FROM COUNTY_RT WHERE COUNTY_CODE = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, countyCode);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getString("COUNTY_NAME");
//                }
//            }
//        }
//        return null;
//    }
    
    public String getCommitNoBySbiNo(String sbiNo) {
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT commit_no FROM INMATE WHERE sbi_mst_sbi_no = ? ";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, sbiNo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("commit_no");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    private java.sql.Date convertToDate(Object dateObj) {
        if (dateObj == null) return null;

        try {
            if (dateObj instanceof Date) {
                return new java.sql.Date(((Date) dateObj).getTime());
            } else if (dateObj instanceof String) {
                String dateStr = (String) dateObj;
                if (dateStr.isEmpty()) return null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                sdf.setLenient(false);
                return new java.sql.Date(sdf.parse(dateStr).getTime());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateObj);
        }

        throw new IllegalArgumentException("Unsupported date type: " + dateObj.getClass().getName());
    }
    
public String saveOrUpdateInmate(Map<String, Object> inmateData, String newFlag) {
        
        // --- UPDATE LOGIC for existing inmates ---
        if ("N".equals(newFlag)) {
            String procedureCall = "{call DACS.SPKG_INM_BOOKING.sp_update_inmate(?)}";
            
            try (Connection conn = dataSource.getConnection();
                 CallableStatement stmt = conn.prepareCall(procedureCall)) {

                OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

                // For an update, the frontend must provide the existing commit_no and address_seq_num.
                // The address_seq_num can be null if the inmate doesn't have an address yet.
                Object commitNo = inmateData.get("commit_no");
                Object addressSeqNum = inmateData.get("address_seq_num");
                
                if (commitNo == null) {
                    throw new IllegalArgumentException("Commit Number (commit_no) is required for updating an inmate.");
                }

                // The attributes array must exactly match the structure of the TY_INM_BOOKING Oracle type.
                // Fields not used by the sp_update_inmate procedure are passed as null.
                Object[] attributes = new Object[]{
                        inmateData.get("sbi_no"),
                        commitNo, // Pass the existing commit_no for the WHERE clause
                        inmateData.get("inmate_first_name"),
                        inmateData.get("inmate_last_name"),
                        inmateData.get("inmate_middle_name"),
                        inmateData.get("inmate_suffix_name"),
                        inmateData.get("race_code"),
                        null, // hair_color_code
                        null, // eye_color_code
                        null, // weight
                        null, // dexterity_code
                        null, // height
                        null, // skin_color_code
                        null, // religion_code
                        convertToDate(inmateData.get("primary_dob")),
                        null, // primary_ssn
                        null, // marital_status_code
                        inmateData.get("gender"),
                        null, // culture_code
                        null, // arr_depart_flag (Not used in sp_update_inmate)
                        null, // admiss_release_code (Not used in sp_update_inmate)
                        null, // admiss_release_date (Not used in sp_update_inmate)
                        null, // admiss_release_time (Not used in sp_update_inmate)
                        null, // admiss_release_comments
                        null, // agency_jurisdiction
                        null, // hispanic_flag
                        null, // inst_num (Not used in sp_update_inmate)
                        null, // from_inst_num (Not used in sp_update_inmate)
                        null, // inmt_tp_cd (Not used in sp_update_inmate)
                        null, // drv_license_no
                        null, // drv_state_issue
                        null, // drv_lic_status
                        null, // drv_lic_commercial_status
                        null, // fbi_no
                        null, // ethnic_origin
                        null, // state_code_from
                        null, // place_of_birth_state
                        null, // place_of_birth_country
                        null, // citizenship_code
                        null, // fpc_code
                        addressSeqNum, // Pass existing address_seq_num to update, or null to insert a new address
                        inmateData.get("adr_street_1"),
                        inmateData.get("county_code"),
                        inmateData.get("state_code"),
                        inmateData.get("adr_zip_5"),
                        inmateData.get("adr_zip_4") // Pass adr_zip_4 as used by the procedure
                };

                Struct inmateStruct = oracleConnection.createStruct("TY_INM_BOOKING", attributes);
                Struct[] structArray = { inmateStruct };
                Array oracleArray = oracleConnection.createOracleArray("TB_INM_BOOKING", structArray);
                
                stmt.setArray(1, oracleArray);
                stmt.execute();

                // The update procedure does not return a value, so we return the original sbi_no passed in.
                return (String) inmateData.get("sbi_no");

            } catch (SQLException e) {
                throw new RuntimeException("Error updating inmate data: " + e.getMessage(), e);
            }
        } 
        // --- INSERT LOGIC for new inmates (Original functionality) ---
        else {
            String procedureCall = "{call DACS.SPKG_INM_BOOKING.sp_insert_upd_inmate(?, ?, ?)}";
            
            try (Connection conn = dataSource.getConnection();
                 CallableStatement stmt = conn.prepareCall(procedureCall)) {

                OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

                // The attributes array must exactly match the structure of the TY_INM_BOOKING Oracle type.
                Object[] attributes = new Object[]{
                        null, // sbi_no is generated by the procedure for new inmates
                        null, // commit_no is generated by the procedure for new inmates
                        inmateData.get("inmate_first_name"),
                        inmateData.get("inmate_last_name"),
                        inmateData.get("inmate_middle_name"),
                        inmateData.get("inmate_suffix_name"),
                        inmateData.get("race_code"),
                        null, // hair_color_code
                        null, // eye_color_code
                        null, // weight
                        null, // dexterity_code
                        null, // height
                        null, // skin_color_code
                        null, // religion_code
                        convertToDate(inmateData.get("primary_dob")),
                        null, // primary_ssn
                        null, // marital_status_code
                        inmateData.get("gender"),
                        null, // culture_code
                        inmateData.get("arr_depart_flag"),
                        inmateData.get("admiss_release_code"),
                        convertToDate(inmateData.get("admiss_release_date")),
                        inmateData.get("admiss_release_time"),
                        null, // admiss_release_comments
                        null, // agency_jurisdiction
                        null, // hispanic_flag
                        inmateData.get("inst_num"),
                        inmateData.get("from_inst_num"),
                        inmateData.get("inmt_tp_cd"),
                        null, // drv_license_no
                        null, // drv_state_issue
                        null, // drv_lic_status
                        null, // drv_lic_commercial_status
                        null, // fbi_no
                        null, // ethnic_origin
                        null, // state_code_from
                        null, // place_of_birth_state
                        null, // place_of_birth_country
                        null, // citizenship_code
                        null, // fpc_code
                        null, // ADDRESS_SEQ_NUM (procedure handles this for new records)
                        inmateData.get("adr_street_1"),
                        inmateData.get("county_code"),
                        inmateData.get("state_code"),
                        inmateData.get("adr_zip_5"),
                        inmateData.get("adr_zip_4") // adr_zip_4 can be passed for inserts too
                };

                Struct inmateStruct = oracleConnection.createStruct("TY_INM_BOOKING", attributes);
                Struct[] structArray = { inmateStruct };
                Array oracleArray = oracleConnection.createOracleArray("TB_INM_BOOKING", structArray);
                
                stmt.setArray(1, oracleArray);
                stmt.setString(2, newFlag);
                stmt.registerOutParameter(3, java.sql.Types.VARCHAR);

                stmt.execute();

                // Return the newly generated SBI No from the procedure's output parameter.
                return stmt.getString(3);

            } catch (SQLException e) {
                throw new RuntimeException("Error saving new inmate data: " + e.getMessage(), e);
            }
        }
    }
    
    public List<Map<String, String>> fetchCityStateZip(String search) {
        List<Map<String, String>> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT COUNTY_NAME, COUNTY_CODE, STATE_CODE, ZIP_5 FROM COUNTY_RT " +
                         "WHERE UPPER(COUNTY_NAME) LIKE UPPER(?) OR UPPER(STATE_CODE) LIKE UPPER(?) OR ZIP_5 LIKE ? " +
                         "ORDER BY COUNTY_NAME, STATE_CODE, ZIP_5";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + search + "%");
                stmt.setString(2, "%" + search + "%"); 
                stmt.setString(3, "%" + search + "%"); 
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> row = new HashMap<>();
                        row.put("COUNTY_NAME", rs.getString("COUNTY_NAME"));
                        row.put("COUNTY_CODE", rs.getString("COUNTY_CODE"));
                        row.put("STATE_CODE", rs.getString("STATE_CODE"));
                        row.put("ZIP_5", rs.getString("ZIP_5"));
                        result.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public List<Map<String, String>> fetchAllCityStateZip() {
        List<Map<String, String>> cityStateZipData = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT COUNTY_NAME, STATE_CODE, ZIP_5, ZIP_4, COUNTY_CODE FROM COUNTY_RT ORDER BY 1, 2, 3, 4";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> row = new HashMap<>();
                    row.put("city", rs.getString("COUNTY_NAME"));
                    row.put("state", rs.getString("STATE_CODE")); 
                    row.put("zip", rs.getString("ZIP_5")); 
                    row.put("countyCode", rs.getString("COUNTY_CODE")); 
                    cityStateZipData.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityStateZipData;
    }
    
    public List<Map<String, String>> getDistinctAdmissionReleaseCodes() {
        List<Map<String, String>> admissionCodes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT DISTINCT ADMISS_RELEASE_CODE, ADMISS_RELEASE_CODE_DESC " +
                         "FROM admission_release_code_ref " +
                         "WHERE ADMISS_RELEASE_CODE_STATUS='A' " +
                         "ORDER BY ADMISS_RELEASE_CODE_DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> code = new HashMap<>();
                    code.put("admitCode", rs.getString("ADMISS_RELEASE_CODE"));
                    code.put("admitCodeDesc", rs.getString("ADMISS_RELEASE_CODE_DESC"));
                    admissionCodes.add(code);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admissionCodes;
    }
    
    public List<Map<String, String>> getDistinctOffenderTypes() {
        List<Map<String, String>> offenderTypes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
        	String sql = "SELECT INMT_TP_CD, INMT_TP_DESC FROM inmate_tp_mst WHERE Status='A' ORDER BY INMT_TP_DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> type = new HashMap<>();
                    type.put("offenderTypeCode", rs.getString("INMT_TP_CD"));
                    type.put("offenderTypeDesc", rs.getString("INMT_TP_DESC"));
                    offenderTypes.add(type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offenderTypes;
    }
    
    public List<Map<String, Object>> getPriorIncarceration(String sbiNo) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call SPKG_OMNET_WRAPPER.sp_cbi_reg_prior_incarceration_query(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, OracleTypes.ARRAY, "TB_CBI_REG_PRIOR_INCARCERATION_QUERY");
                stmt.setString(2, sbiNo);
                stmt.setString(3, "N");
                stmt.execute();

                Array array = stmt.getArray(1);
                if (array != null) {
                    Object[] arrayElements = (Object[]) array.getArray();
                    
                    for (Object element : arrayElements) {
                        if (element instanceof Struct) {
                            Struct struct = (Struct) element;
                            Object[] attributes = struct.getAttributes();
                           
                            Map<String, Object> rowMap = new HashMap<>();

                            rowMap.put("COMMIT_NO", attributes[0]);
                            rowMap.put("STAY", attributes[1]);
                            rowMap.put("INST_NUM", attributes[2]);
                            rowMap.put("INST_NAME", attributes[3]);
                            rowMap.put("INMATE_NAME", attributes[4]);
                            rowMap.put("INMT_TP_CD", attributes[5]);
                            rowMap.put("SENT_TYPE_DESC", attributes[6]);
                            rowMap.put("INST_ADMISS_DATE", attributes[7]);
                            rowMap.put("INST_ADMISS_TIME", attributes[8]);
                            rowMap.put("INST_RELEASE_DATE", attributes[9]);
                            rowMap.put("INST_RELEASE_TIME", attributes[10]);
                            rowMap.put("STATUS", attributes[11]);
                            
                            resultList.add(rowMap);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching prior incarceration data", e);
        }
        return resultList;
    }
    
    public List<Map<String, Object>> getInmateLookupDetails(String sbiNo, String lastName, String firstName, String middleName, String suffixName, String dob) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{call DACS.SPKG_INM_BOOKING.sp_inmate_lookup(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (sbiNo != null && !sbiNo.isEmpty()) ? sbiNo : null);
            stmt.setString(3, (lastName != null && !lastName.isEmpty()) ? lastName : null);
            stmt.setString(4, (firstName != null && !firstName.isEmpty()) ? firstName : null);
            stmt.setString(5, (middleName != null && !middleName.isEmpty()) ? middleName : null);
            stmt.setString(6, (suffixName != null && !suffixName.isEmpty()) ? suffixName : null);

            if (dob != null && !dob.isEmpty()) {
                try {
                    SimpleDateFormat frontendFormat = new SimpleDateFormat("MM/dd/yyyy");
                    java.util.Date utilDate = frontendFormat.parse(dob);
                    stmt.setDate(7, new java.sql.Date(utilDate.getTime()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format for DOB lookup: " + dob, e);
                }
            } else {
                stmt.setNull(7, java.sql.Types.DATE);
            }

            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Map<String, Object> rowMap = new HashMap<>();
                    rowMap.put("sbiNo", rs.getString("SBI_NO"));
                    rowMap.put("lastName", rs.getString("OFFENDER_LAST_NAME"));
                    rowMap.put("firstName", rs.getString("OFFENDER_FIRST_NAME"));
                    rowMap.put("mi", rs.getString("OFFENDER_MID_NAME"));
                    rowMap.put("suffix", rs.getString("OFFENDER_SUFFIX_NAME"));

                    Date birthDate = rs.getDate("INMATE_BIRTH_DATE");
                    if (birthDate != null) {
                        rowMap.put("dob", new SimpleDateFormat("MM/dd/yyyy").format(birthDate));
                    } else {
                        rowMap.put("dob", "");
                    }

                    double matchPercent = rs.getDouble("MATCH_PERCENT");
                    rowMap.put("matchPercentage", String.format("%.2f%%", matchPercent));
                    
                    rowMap.put("checkbox", "t");
                    rowMap.put("mugshot", "");
                    rowMap.put("fingerPrint", "");

                    resultList.add(rowMap);
                }
            }
        } catch (SQLException | IllegalArgumentException e) { 
            e.printStackTrace();
            String message = e instanceof IllegalArgumentException ? e.getMessage() : "Error fetching inmate lookup details";
            throw new RuntimeException(message, e);
        }
        return resultList;
    }
    
    public void saveMugshotImage(String sbiNo, String base64Image) {
        if (sbiNo == null || sbiNo.isEmpty() || base64Image == null || base64Image.isEmpty()) {
            // Do not proceed if there is no SBI number or image data
            return;
        }

        // This procedure call matches the provided definition: sp_insert_mugshot_image(sbi_no, mugshot_blob)
        String procedureCall = "{call DACS.SPKG_INM_BOOKING.sp_insert_mugshot_image(?, ?)}";
        
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(procedureCall)) {

            // Decode the Base64 string from the frontend into a byte array
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);

            // Parameter 1: ivc_sbi_no (VARCHAR2)
            stmt.setString(1, sbiNo);
            // Parameter 2: iblb_mugshot (BLOB)
            stmt.setBytes(2, imageBytes); // The JDBC driver handles converting the byte array to a BLOB

            stmt.execute();

        } catch (SQLException | IllegalArgumentException e) {
            // IllegalArgumentException can be thrown by the Base64 decoder for invalid input
            throw new RuntimeException("Error saving mugshot image for SBI No: " + sbiNo, e);
        }
    }
     
}              