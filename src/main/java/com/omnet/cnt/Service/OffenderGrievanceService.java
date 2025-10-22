package com.omnet.cnt.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OffenderGrievanceService {

    @Autowired
    private DataSource dataSource;
    
    private static final Logger logger = LoggerFactory.getLogger(OffenderGrievanceService.class);

    private final Object grvnRewDtlLock = new Object();
    
    
    /**
     * Retrieves the main and detailed information for a specific grievance.
     * This service calls two stored procedures to fetch comprehensive data based on the commit number and sequence number.
     * @param commitNo The offender's commit number.
     * @param seqNum The unique sequence number of the grievance.
     * @return A map containing all details of the specified grievance.
     */
    public Map<String, Object> getGrievanceDetails(String commitNo, Integer seqNum) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String mainSql = "{call DACS.SPKG_GRV_INFO.SP_MAIN_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(mainSql)) {
                stmt.setInt(1, seqNum);
                stmt.setString(2, commitNo);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    if (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            Object value = rs.getObject(i);
                            
                            if (value instanceof Clob) {
                                Clob clob = (Clob) value;
                                value = clob.getSubString(1, (int) clob.length());
                            }
                            
                            result.put(columnName, value);
                            System.out.println("Column " + columnName + " = " + 
                                    (value != null ? value.toString() : "NULL"));
                        }
                        result.put("EMERGENCY_GRV_FLAG", rs.getString("EMERGENCY_GRV_FLAG"));
                        result.put("CONF_GRV_FLAG", rs.getString("CONF_GRV_FLAG"));
                        result.put("grvn_med_rel_flg", rs.getString("grvn_med_rel_flg"));
                        result.put("grvn_med_rcpt_dt", rs.getDate("grvn_med_rcpt_dt"));
                    }
                }
            }
            String detailSql = "{call DACS.SPKG_GRV_INFO.SP_DTL_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(detailSql)) {
                stmt.setInt(1, seqNum);
                stmt.setString(2, commitNo);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    if (rs.next()) {
                        result.put("GRIEVANCE_STATUS", rs.getString("grievance_status"));
                        result.put("GRVN_LVL_RESL", rs.getString("grvn_lvl_resl"));
                        result.put("GRVN_AMOUNT", rs.getBigDecimal("GRVN_AMOUNT"));
                        result.put("RESOLUTION_DATE", rs.getDate("resolution_date"));
                        result.put("reason_for_abandoned", rs.getString("reason_for_abandoned"));
                        result.put("abandoned_comments", rs.getString("abandoned_comments"));
                        result.put("resn_retrn_vul_thread_lang", rs.getString("resn_retrn_vul_thread_lang"));
                        result.put("resn_retrn_dscp_act", rs.getString("resn_retrn_dscp_act"));
                        result.put("resn_retrn_dscp_act_num", rs.getBigDecimal("resn_retrn_dscp_act_num"));
                        result.put("resn_retrn_dscp_act_par_dcsn", rs.getString("resn_retrn_dscp_act_par_dcsn"));
                        result.put("resn_retrn_dscp_act_clsfn_act", rs.getString("resn_retrn_dscp_act_clsfn_act"));
                        result.put("resn_retrn_act_prohb_mail", rs.getString("resn_retrn_act_prohb_mail"));
                        result.put("resn_retrn_dupl_grv", rs.getString("resn_retrn_dupl_grv"));
                        result.put("resn_retrn_dupl_grv_num", rs.getBigDecimal("resn_retrn_dupl_grv_num"));
                        result.put("resn_retrn_need_orig_grv", rs.getString("resn_retrn_need_orig_grv"));
                        result.put("resn_retrn_othr_inmate_enq", rs.getString("resn_retrn_othr_inmate_enq"));
                        result.put("resn_retrn_expired", rs.getString("resn_retrn_expired"));
                        result.put("resn_retrn_abuse", rs.getString("resn_retrn_abuse"));
                        result.put("resn_retrn_other", rs.getString("resn_retrn_other"));
                        result.put("resn_retrn_other_desc", rs.getString("resn_retrn_other_desc"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * Fetches the most recent commit number for an offender using their SBI number.
     * It orders the records by admission date to find the latest active record.
     * @param sbiNo The 8-digit SBI number of the offender.
     * @return The latest commit number as a String, or null if not found.
     */
    public String getCommitNoBySbi(String sbiNo) {
        if (sbiNo == null || sbiNo.length() != 8) {
            return null;
        }
        
        String sql = "SELECT commit_no FROM (" +
                     "  SELECT commit_no, ROW_NUMBER() OVER (ORDER BY inst_admiss_date DESC, commit_no DESC) as rn " +
                     "  FROM inmate WHERE sbi_mst_sbi_no = ?" +
                     ") WHERE rn = 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sbiNo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Retrieves a list of all grievances filed by a specific offender.
     * The list includes the grievance sequence number and the date it was filed.
     * @param commitNo The offender's commit number.
     * @return A list of maps, where each map represents a grievance record.
     */
    public List<Map<String, Object>> getGrievanceListWithDates(String commitNo) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (commitNo == null) return resultList;
        
        try (Connection conn = dataSource.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT grievance_seq_num, date_of_grievance, commit_no FROM INMATE_GRIEVANCE WHERE commit_no = ? ORDER BY grievance_seq_num")) {
                stmt.setString(1, commitNo);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("grievance_seq_num", rs.getInt("grievance_seq_num"));
                        row.put("date_of_grievance", rs.getDate("date_of_grievance"));
                        row.put("commit_no", rs.getString("commit_no")); 
                        resultList.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    
    /**
     * Gets the current housing location of an offender.
     * This service calls a stored function to retrieve the formatted location string.
     * @param commitNo The offender's commit number.
     * @return The housing location as a String, or an empty string if not found.
     */
    public String getHousingLocation(String commitNo) {
        if (commitNo == null) return "";
        
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(
                 "{ ? = call DACS.SPKG_GRV_INFO.SF_GET_HOUSING_LOCATION(?) }")) {
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, commitNo);
            stmt.execute();
            return stmt.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Fetches a list of all possible grievance statuses from the reference table.
     * This is used to populate dropdowns or lists in the user interface.
     * @return A list of maps, with each map containing the status description and code.
     */
    public List<Map<String, Object>> getGrievanceStatuses() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT ref_value_desc, ref_value_code FROM cm_reference_values " +
                     "WHERE ref_category_code = 'GRV_STATUS' AND ref_category_module = 'GRV' order by ref_value_desc ASC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ref_value_desc", rs.getString("ref_value_desc"));
                row.put("ref_value_code", rs.getString("ref_value_code"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Retrieves a list of all active grievance types.
     * This data is typically used for populating selection controls in the UI.
     * @return A list of maps, where each map contains a grievance type code and its description.
     */
    public List<Map<String, Object>> getGrievanceTypes() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT GRIEVANCE_CODE, GRIEVANCE_DESC FROM GRIEVANCE_TYPE_RT " +
                     "WHERE STATUS = 'A' ORDER BY GRIEVANCE_DESC ASC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("GRIEVANCE_CODE", rs.getString("GRIEVANCE_CODE"));
                row.put("GRIEVANCE_DESC", rs.getString("GRIEVANCE_DESC"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Gets a list of facilities that a specific user has access to.
     * The query joins institution and user access tables to filter facilities.
     * @param userId The ID of the user for whom to fetch facilities.
     * @return A list of maps, each containing the facility name and number.
     */
    public List<Map<String, Object>> getFacilities(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT i.inst_name, i.inst_num FROM institution i, user_access_inst u " +
                     "WHERE i.inst_num = u.Inst_num AND u.user_id = ? AND i.status = 'A' " +
                     "ORDER BY i.inst_name";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("inst_name", rs.getString("inst_name"));
                    row.put("inst_num", rs.getString("inst_num"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Fetches all active building locations within a specific institution.
     * This service is used to populate location-based dropdowns.
     * @param instNum The institution number to search within.
     * @return A list of maps, each containing a building's name and number.
     */
    public List<Map<String, Object>> getLocations(String instNum) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT b.bld_name, b.bld_num FROM Building b " +
                     "WHERE b.inst_num = ? AND b.status = 'A' " +
                     "ORDER BY b.bld_name";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, instNum);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("bld_name", rs.getString("bld_name"));
                    row.put("bld_num", rs.getString("bld_num"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Retrieves the list of reasons for abandoning a grievance.
     * This data is fetched from a reference table and used for UI dropdowns.
     * @return A list of maps, each representing an abandonment reason with its code and description.
     */
    public List<Map<String, Object>> getAbandonmentReasons() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT ref_value_desc, ref_value_code FROM cm_reference_values " +
                     "WHERE ref_category_module = 'GRV' AND ref_category_code = 'GRV_TP_ABD' " +
                     "ORDER BY ref_value_desc ASC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ref_value_desc", rs.getString("ref_value_desc"));
                row.put("ref_value_code", rs.getString("ref_value_code"));
                result.add(row);
            }
        } catch (SQLException e) {
            logger.error("Error fetching abandonment reasons", e);
        }
        return result;
    }
    
    /**
     * Fetches the name and address information for a given facility.
     * This is useful for displaying contact or location details in reports or UI screens.
     * @param instNum The number of the institution.
     * @return A map containing the facility's name, street, city, state, zip, and phone number.
     */
    public Map<String, Object> getFacilityAddressInfo(String instNum) {
        Map<String, Object> result = new HashMap<>();
        if (instNum == null || instNum.isEmpty()) {
            logger.warn("getFacilityAddressInfo called with null or empty instNum.");
            return result;
        }
        
        String sql = "SELECT a.inst_name, a.inst_adr_street_1, b.county_name, a.state_code, a.inst_adr_zip_5, a.inst_phone_1 " +
                     "FROM institution a JOIN county_rt b ON a.county_code = b.county_code " +
                     "WHERE a.inst_num = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, instNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result.put("inst_name", rs.getString("inst_name"));
                    result.put("street", rs.getString("inst_adr_street_1"));
                    result.put("city", rs.getString("county_name")); // Assuming county_name is the city (e.g., GEORGETOWN)
                    result.put("state", rs.getString("state_code"));
                    result.put("zip", rs.getString("inst_adr_zip_5"));
                    result.put("phone", rs.getString("inst_phone_1"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching facility address info for instNum: {}", instNum, e);
        }
        return result;
    }
    
    /**
     * Searches for users by their first or last name.
     * This service provides a type-ahead or search functionality for finding system users.
     * @param searchTerm The partial or full name to search for.
     * @return A list of maps, each containing details of a matching user.
     */
    public List<Map<String, Object>> getUsers(String searchTerm) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT user_first_name, user_last_name, user_mid_name, user_suffix_name, user_id " +
                     "FROM omnet_users " +
                     "WHERE UPPER(user_first_name) LIKE UPPER(?) OR UPPER(user_last_name) LIKE UPPER(?) " +
                     "ORDER BY user_last_name ASC, user_first_name ASC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeTerm = "%" + (searchTerm != null ? searchTerm : "") + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("user_first_name", rs.getString("user_first_name"));
                    row.put("user_last_name", rs.getString("user_last_name"));
                    row.put("user_mid_name", rs.getString("user_mid_name"));
                    row.put("user_suffix_name", rs.getString("user_suffix_name"));
                    row.put("user_id", rs.getString("user_id"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Retrieves the full name details for a single user based on their user ID.
     * This is used to display a user's name when only the ID is available.
     * @param userId The ID of the user to look up.
     * @return A map containing the user's first, last, middle, and suffix names.
     */
    public Map<String, String> getUserDetails(String userId) {
        Map<String, String> userDetails = new HashMap<>();
        if (userId == null || userId.isEmpty()) return userDetails;
        
        String sql = "SELECT user_first_name, user_last_name, user_mid_name, user_suffix_name " +
                     "FROM USERS WHERE user_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userDetails.put("user_first_name", rs.getString("user_first_name"));
                    userDetails.put("user_last_name", rs.getString("user_last_name"));
                    userDetails.put("user_mid_name", rs.getString("user_mid_name"));
                    userDetails.put("user_suffix_name", rs.getString("user_suffix_name"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching user details for userId: " + userId, e);
        }
        return userDetails;
    }
    
    /**
     * Validates if a given SBI (State Bureau of Identification) number exists in the inmate table.
     * This service helps ensure data integrity before performing operations related to an inmate.
     * @param sbiNo The 8-digit SBI number to validate.
     * @return True if the SBI number is valid and exists, false otherwise.
     */
    public boolean isSbiValid(String sbiNo) {
        if (sbiNo == null || sbiNo.length() != 8) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM INMATE WHERE SBI_MST_SBI_NO = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sbiNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // If the count is greater than 0, the SBI number exists and is valid.
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error validating SBI number: " + sbiNo, e);
        }
        // Return false if the SBI is not found or an error occurs.
        return false;
    }

    /**
     * Fetches a list of persons (staff or inmates) involved in a grievance for a specific committee type.
     * This service calls a stored procedure to retrieve the relevant individuals.
     * @param commitNo The offender's commit number.
     * @param seqNum The sequence number of the grievance.
     * @param cmtyType The committee type (e.g., 'IND', 'RGC') to filter by.
     * @return A map where the key is the committee type and the value is a list of involved persons.
     */
    public Map<String, List<Map<String, Object>>> getPersonsInvolvedByType(String commitNo, Integer seqNum, String cmtyType) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        if (commitNo == null || seqNum == null) return result;
        
        // Default to IND if cmtyType is not provided
        String effectiveCmtyType = cmtyType != null ? cmtyType : "IND";
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_PERSONS_INVOLVED_QUERY(?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.setString(3, effectiveCmtyType); 
                stmt.registerOutParameter(4, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                List<Map<String, Object>> persons = new ArrayList<>();
                try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("grievance_seq_num", rs.getInt("grievance_seq_num"));
                        row.put("commit_no", rs.getString("commit_no"));
                        row.put("invol_seq_num", rs.getInt("invol_seq_num"));
                        row.put("user_id", rs.getString("user_id"));
                        row.put("last_name", rs.getString("DECODE(a.inmt_commit_no,NULL,c.user_last_name,b.commit_lname)"));
                        row.put("first_name", rs.getString("DECODE(a.inmt_commit_no,NULL,c.user_first_name,b.commit_fname)"));
                        row.put("mid_name", rs.getString("DECODE(a.inmt_commit_no,NULL,c.user_mid_name,b.commit_mname)"));
                        row.put("suffix_name", rs.getString("DECODE(a.inmt_commit_no,NULL,c.user_suffix_name,b.commit_sname)"));
                        row.put("grvn_staff_tp", rs.getString("grvn_staff_tp"));
                        row.put("inmt_commit_no", rs.getString("inmt_commit_no"));
                        row.put("cmty_type", rs.getString("cmty_type") != null ? rs.getString("cmty_type") : effectiveCmtyType);
                        row.put("vote_type", rs.getString("vote_type"));
                        row.put("sbi_no", rs.getString("DECODE(a.inmt_commit_no,NULL,NULL,b.sbi_mst_sbi_no)"));
                        row.put("tie_breaker_flag", rs.getString("tie_breaker_flag"));
                        persons.add(row);
                    }
                }
                result.put(effectiveCmtyType, persons);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Searches for staff members by their first or last name.
     * This is a generalized search function used to find and select staff members.
     * @param searchTerm The name or partial name to search for.
     * @return A list of maps, where each map contains details for a matching staff member.
     */
    public List<Map<String, Object>> getStaffList(String searchTerm) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT user_id, user_first_name, user_last_name, user_mid_name, user_suffix_name " +
                     "FROM USERS WHERE UPPER(user_first_name) LIKE UPPER(?) OR UPPER(user_last_name) LIKE UPPER(?) " +
                     "ORDER BY user_last_name, user_first_name";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeTerm = "%" + (searchTerm != null ? searchTerm : "") + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("user_id", rs.getString("user_id"));
                    row.put("user_first_name", rs.getString("user_first_name"));
                    row.put("user_last_name", rs.getString("user_last_name"));
                    row.put("user_mid_name", rs.getString("user_mid_name"));
                    row.put("user_suffix_name", rs.getString("user_suffix_name"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Retrieves details related to the investigation of a grievance.
     * This service fetches information about investigators and resolution flags.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @return A map containing investigation details, including a list of investigators (LSME members).
     */
    public Map<String, Object> getInvestigationDetails(String commitNo, Integer seqNum) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String investigationSql = "{call DACS.SPKG_GRV_INFO.SP_INVESTIGATION_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(investigationSql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                List<Map<String, Object>> lsmeList = new ArrayList<>();
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    while (rs.next()) {
                        Map<String, Object> lsme = new HashMap<>();
                        lsme.put("user_id", rs.getString("user_id"));
                        lsme.put("user_first_name", rs.getString("user_first_name"));
                        lsme.put("user_last_name", rs.getString("user_last_name"));
                        lsme.put("user_mid_name", rs.getString("user_mid_name"));
                        lsme.put("user_suffix_name", rs.getString("user_suffix_name"));
                        lsme.put("grvn_invst_dt", rs.getDate("grvn_invst_dt"));
                        lsme.put("grvn_invst_desc", rs.getString("grvn_invst_desc"));
                        lsme.put("grvn_resn_comnt", rs.getString("grvn_resn_comnt"));
                        lsme.put("date_sent", rs.getDate("date_sent"));
                        lsmeList.add(lsme);
                    }
                    result.put("lsmeList", lsmeList);
                }
            }

            String mainUpdSql = "{call DACS.SPKG_GRV_INFO.SP_MAIN_UPD_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(mainUpdSql)) {
                stmt.setInt(1, seqNum);
                stmt.setString(2, commitNo);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    if (rs.next()) {
                        result.put("inFormal_resl_flag", rs.getString("inFormal_resl_flag"));
                        result.put("frwd_to_icg_flag", rs.getString("frwd_to_icg_flag"));
                        result.put("frwd_to_igc_date", rs.getDate("frwd_to_igc_date"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Fetches the list of LSME (Local Subject Matter Expert) members for a facility and grievance type.
     * This is used to manage and display the committee members responsible for investigations.
     * @param instNum The institution number.
     * @param grievanceType The code for the grievance type.
     * @return A list of maps, where each map contains details of an LSME member.
     */
    public List<Map<String, Object>> getLSMEMembers(String instNum, String grievanceType) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (instNum == null || grievanceType == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_LSME_BCM_DTL_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, grievanceType);
                stmt.setString(2, instNum);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("lsme_bcme_dtl_seq_num", rs.getInt("lsme_bcme_dtl_seq_num"));
                        row.put("user_id", rs.getString("user_id"));
                        row.put("user_last_name", rs.getString("user_last_name"));
                        row.put("user_first_name", rs.getString("user_first_name"));
                        row.put("user_mid_name", rs.getString("user_mid_name"));
                        row.put("user_suffix_name", rs.getString("user_suffix_name"));
                        row.put("lsme_flag", rs.getString("lsme_flag"));
                        row.put("bcme_flag", rs.getString("bcme_flag"));
                        row.put("start_date", rs.getDate("start_date"));
                        row.put("end_date", rs.getDate("end_date"));
                        row.put("inst_num", rs.getString("inst_num"));
                        row.put("grievance_type", rs.getString("grievance_type"));
                        row.put("status", rs.getString("status"));
                        result.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching LSME members", e);
        }
        return result;
    }
    
    /**
     * Retrieves details of the IGC (Institutional Grievance Coordinator) review for a specific grievance.
     * It fetches review comments, dates, flags, and any additional comments made after resolution.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @return A map containing IGC review details and a list of post-resolution comments.
     */
    public Map<String, Object> getIGCReviewDetails(String commitNo, Long seqNum) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null) return result;

        try (Connection conn = dataSource.getConnection()) {
            String committeeType = "IGC";
            
            String igcSql = "{call DACS.SPKG_GRV_INFO.SP_GRV_IGC_QUERY(?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(igcSql)) {
                stmt.setString(1, commitNo);
                stmt.setLong(2, seqNum);
                stmt.setString(3, committeeType);
                stmt.registerOutParameter(4, java.sql.Types.REF_CURSOR);
                stmt.execute();

                try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                    if (rs.next()) {
                        result.put("grvn_rew_recm_comnt", rs.getString("grvn_rew_recm_comnt"));
                        result.put("frwd_med_provider_flg", rs.getString("frwd_med_provider_flg"));
                        result.put("grvn_rew_frwd_to_flg", rs.getString("grvn_rew_frwd_to_flg"));
                        result.put("frwd_med_provider_date", rs.getDate("frwd_med_provider_date"));
                        result.put("grvn_decn_ntf_flg", rs.getString("grvn_decn_ntf_flg"));
                        result.put("grvn_decn_ntf_dt", rs.getDate("grvn_decn_ntf_dt"));
                        result.put("grvn_rew_recm_dt", rs.getDate("grvn_rew_recm_dt"));

                        Map<String, String> userDetails = new HashMap<>();
                        userDetails.put("user_first_name", rs.getString("user_first_name"));
                        userDetails.put("user_last_name", rs.getString("user_last_name"));
                        userDetails.put("user_mid_name", rs.getString("user_mid_name"));
                        userDetails.put("user_suffix_name", rs.getString("user_suffix_name"));

                        String fullName = Stream.of(
                            rs.getString("user_first_name"),
                            rs.getString("user_mid_name"),
                            rs.getString("user_last_name"),
                            rs.getString("user_suffix_name")
                        )
                        .filter(Objects::nonNull)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(" "));

                        userDetails.put("user_full_name", fullName);
                        result.putAll(userDetails);
                    }
                }
            }

            String commentsSql = "{call DACS.SPKG_GRV_INFO.SP_CMNTS_AFT_RESLV_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(commentsSql)) {
                stmt.setString(1, commitNo);
                stmt.setLong(2, seqNum);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();

                List<Map<String, Object>> commentsList = new ArrayList<>();
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    while (rs.next()) {
                        Map<String, Object> comment = new HashMap<>();
                        comment.put("grv_aft_resl_seq_num", rs.getString("grv_aft_resl_seq_num"));
                        comment.put("commit_no", rs.getString("commit_no"));
                        comment.put("grievance_seq_num", rs.getString("grievance_seq_num"));
                        comment.put("user_id", rs.getString("user_id"));
                        comment.put("user_name", rs.getString("user_name"));
                        comment.put("date_entered", rs.getDate("date_entered"));
                        comment.put("comments", rs.getString("comments"));
                        commentsList.add(comment);
                    }
                }
                result.put("commentsList", commentsList);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching IGC review details: " + e.getMessage());
        }

        return result;
    }

    
    private Integer getNextGrievanceSeqNum(Connection conn, String commitNo) throws SQLException {
        logger.info("Getting next grievance sequence number for commitNo: {}", commitNo);
        String sql = "SELECT NVL(MAX(GRIEVANCE_SEQ_NUM), 0) + 1 FROM INMATE_GRIEVANCE WHERE COMMIT_NO = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nextSeq = rs.getInt(1);
                    logger.info("Next sequence number for commitNo {} is {}", commitNo, nextSeq);
                    return nextSeq;
                }
            }
        }
        logger.warn("Could not determine next sequence number for commitNo {}, returning default of 1.", commitNo);
        return 1; 
    }

    private void saveGrievanceTypeChangeReason(Connection conn, Map<String, Object> typeChangeData, String userId) throws SQLException {
        logger.info("Saving grievance type change reason within transaction.");
        String commitNo = (String) typeChangeData.get("commitNo");
        Integer seqNum = (Integer) typeChangeData.get("grievanceSeqNum");

        if (commitNo == null || seqNum == null) {
            logger.error("Commit number or sequence number is missing for saving type change reason.");
            throw new IllegalArgumentException("Commit number and sequence number are required.");
        }

        OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
        
        Object[] fields = new Object[]{
            null, 
            seqNum,
            commitNo,
            typeChangeData.get("oldGrievanceType"),
            typeChangeData.get("newGrievanceType"),
            userId,
            convertToDate(typeChangeData.get("changedDate")),
            (String) typeChangeData.get("reason")
        };
        
        StructDescriptor structDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_TYPE_HIST", oracleConnection);
        Struct typeHistStruct = new STRUCT(structDesc, oracleConnection, fields);
        
        ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_TYPE_HIST", oracleConnection);
        ARRAY typeHistArray = new ARRAY(arrayDesc, oracleConnection, new Struct[]{typeHistStruct});

        try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_type_hist_insert(?)}")) {
            stmt.setArray(1, typeHistArray);
            stmt.execute();
            logger.info("Grievance type change reason saved successfully for commitNo {}, seqNum {}.", commitNo, seqNum);
        }
    }


    /**
     * Saves or updates a grievance record, including its main details and associated data.
     * This service handles both creation of new grievances and updates to existing ones.
     * @param grievanceData A map containing all the data for the grievance.
     * @param userId The ID of the user performing the save operation.
     * @return A map containing the new sequence number and commit number of the saved grievance.
     */
    public Map<String, Object> saveGrievance(Map<String, Object> grievanceData, String userId) {
        logger.info("Starting saveGrievance method");
        String commitNo = (String) grievanceData.get("commitNo");
        Object seqNumObj = grievanceData.get("grievanceSeqNum");
        boolean isNew = (boolean) grievanceData.get("isNew");
        String investigatorUserId = (String) grievanceData.get("investigatorUserId");
        
        Integer finalSeqNum;
        Map<String, Object> result = new HashMap<>();

        logger.info("Extracted basic grievance data - commitNo: {}, seqNumObj: {}, isNew: {}", 
            commitNo, seqNumObj, isNew);

        if (commitNo == null || commitNo.isEmpty()) {
            logger.error("CommitNo cannot be empty");
            throw new IllegalArgumentException("CommitNo cannot be empty");
        }

        try (Connection conn = dataSource.getConnection()) {
            logger.info("Database connection established");
            
            if (isNew) {
                finalSeqNum = getNextGrievanceSeqNum(conn, commitNo);
            } else {
                finalSeqNum = Integer.parseInt(String.valueOf(seqNumObj));
            }
            
            logger.info("Final sequence number to be used: {}", finalSeqNum);

            Map<String, String> investigatorDetails = getInvestigatorDetails(conn, investigatorUserId);
            logger.info("Retrieved investigator details: {}", investigatorDetails);
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
            logger.info("Unwrapped Oracle connection");

            Date dateOfGrievance = convertToDate(grievanceData.get("dateOfGrievance"));
            Date dateOfIncident = convertToDate(grievanceData.get("dateOfIncident"));
            Date dateReceived = convertToDate(grievanceData.get("dateReceived"));
            Date grvnMedRcptDt = convertToDate(grievanceData.get("grvnMedRcptDt"));
            
            Clob descClob = createClob(conn, (String) grievanceData.get("descOfComplaint"));
            Clob remedyClob = createClob(conn, (String) grievanceData.get("remedyRequested"));
            Clob emergencyCommentsClob = createClob(conn, (String) grievanceData.get("emergency_comments"));

            Object[] grievanceFields = new Object[]{
                commitNo,
                finalSeqNum, 
                dateOfGrievance,
                grievanceData.get("instNum"),
                grievanceData.get("grvnCatg"),
                null,
                dateOfIncident,
                grievanceData.get("grvnTm"),
                grievanceData.get("grievanceCode"),
                dateReceived,
                descClob,
                remedyClob,
                grievanceData.get("medRelFlag"),
                grvnMedRcptDt,
                grievanceData.get("countLocCode"),
                null, null,
                investigatorUserId,
                investigatorDetails.get("lastName"),
                investigatorDetails.get("firstName"),
                investigatorDetails.get("middleName"),
                investigatorDetails.get("suffixName"),
                null, null,
                "Y".equals(grievanceData.get("emergencyGrvFlag")) ? "Y" : "N",
                "Y".equals(grievanceData.get("confGrvFlag")) ? "Y" : "N",
                "Y".equals(grievanceData.get("emergency_grv_reqst_deny")) ? "Y" : "N",
                grievanceData.get("emergency_grv_resn_for_deny"),
                "Y".equals(grievanceData.get("emergency_frwd_to_igc")) ? "Y" : "N",
                emergencyCommentsClob,
                null, null, null, null, null, null
            };
            
            Struct grievanceStruct = oracleConnection.createStruct("TY_GRV_INFO_MAIN_INS", grievanceFields);
            Array grievanceArray = oracleConnection.createOracleArray("TB_GRV_INFO_MAIN_INS", new Struct[]{grievanceStruct});

            String procedureName = isNew ? "spkg_omnet_wrapper.sp_grv_main_insert" : "spkg_omnet_wrapper.sp_grv_main_update";
            
            try (CallableStatement stmt = conn.prepareCall("{call " + procedureName + "(?)}")) {
                stmt.setArray(1, grievanceArray);
                stmt.execute();
            }

            updateGrievanceDetails(conn, commitNo, finalSeqNum, grievanceData);

            if (grievanceData.containsKey("grievanceTypeChangeData")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> typeChangeData = (Map<String, Object>) grievanceData.get("grievanceTypeChangeData");
                typeChangeData.put("commitNo", commitNo);
                typeChangeData.put("grievanceSeqNum", finalSeqNum);
                saveGrievanceTypeChangeReason(conn, typeChangeData, userId);
            }

            if (grievanceData.containsKey("personsData")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> personsData = (List<Map<String, Object>>) grievanceData.get("personsData");
                savePersonsInvolved(commitNo, finalSeqNum, personsData, isNew);
            }

            if (grievanceData.containsKey("investigationData")) {
                 @SuppressWarnings("unchecked")
                List<Map<String, Object>> investigationData = (List<Map<String, Object>>) grievanceData.get("investigationData");
                saveInvestigationDetails(commitNo, finalSeqNum, investigationData, userId);
            }
            
            if (grievanceData.containsKey("lsmeData")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> lsmeData = (Map<String, Object>) grievanceData.get("lsmeData");
                String instNum = (String) grievanceData.get("instNum");
                String grievanceType = (String) grievanceData.get("grievanceCode");
                if (instNum != null && grievanceType != null && lsmeData.containsKey("members")) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> members = (List<Map<String, Object>>) lsmeData.get("members");
                    saveLSMEMembers(instNum, grievanceType, members, userId);
                }
            }
            result.put("newSeqNum", finalSeqNum);
            result.put("commitNo", commitNo);

        } catch (SQLException e) {
            logger.error("Error saving grievance data", e);
            throw new RuntimeException("Error saving grievance data: " + e.getMessage(), e);
        }
        logger.info("Completed saveGrievance method");
        return result;
    }


    /**
     * Saves the investigation details for a grievance, including investigators and comments.
     * This service first deletes existing records and then inserts the new data to ensure consistency.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param investigationData A list of maps, where each map represents an investigator's record.
     * @param userId The ID of the user saving the details.
     */
    public void saveInvestigationDetails(String commitNo, Integer seqNum,
            List<Map<String, Object>> investigationData, String userId) {
        logger.info("Starting saveInvestigationDetails method");
        
        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        if (investigationData == null || investigationData.isEmpty()) {
            logger.info("No investigation data to save");
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            logger.info("Database connection established");
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
            
            // First delete all existing investigation records for this grievance
            logger.info("Deleting existing investigation records");
            try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM GRIEVANCE_INVST_DTL WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ?")) {
                stmt.setString(1, commitNo);
                stmt.setString(2, String.valueOf(seqNum));
                stmt.executeUpdate();
            }

            // Get next sequence number for new records
            Integer nextSeqNo = getNextInvestigationSeqNo(conn);
            logger.info("Next investigation sequence number: {}", nextSeqNo);

            List<Struct> investigationStructs = new ArrayList<>();
            
            for (Map<String, Object> data : investigationData) {
                Integer seqNo = nextSeqNo++;
                if (seqNo == null) continue;

                // Validate user_id exists
                String userIdFromData = (String) data.get("user_id");
                if (userIdFromData == null || userIdFromData.isEmpty()) {
                    logger.error("User ID cannot be empty for investigation record");
                    continue;
                }

                Clob descClob = createClob(conn, (String) data.get("grvn_invst_desc"));
                Clob resnComntClob = createClob(conn, (String) data.get("grvn_resn_comnt"));

                Object[] fields = new Object[]{
                    seqNo,
                    seqNum,
                    commitNo,
                    userIdFromData,
                    userId,
                    data.get("user_last_name"),
                    data.get("user_first_name"),
                    data.get("user_mid_name"),
                    data.get("user_suffix_name"),
                    convertToDate(data.get("grvn_invst_dt")),
                    descClob,
                    resnComntClob,
                    convertToDate(data.get("grvn_invst_frwd_dt")),
                    convertToDate(data.get("date_sent"))
                };
                
                Struct struct = oracleConnection.createStruct("TY_GRV_INFO_INVESTIGATION", fields);
                investigationStructs.add(struct);
            }

            if (!investigationStructs.isEmpty()) {
                Array investigationArray = oracleConnection.createOracleArray(
                    "TB_GRV_INFO_INVESTIGATION",
                    investigationStructs.toArray(new Struct[0])
                );

                try (CallableStatement stmt = conn.prepareCall(
                    "{call spkg_omnet_wrapper.sp_grv_investigation_insert(?)}")) {
                    stmt.setArray(1, investigationArray);
                    stmt.execute();
                }
            }

            // Prepare main update fields if we have data
            if (!investigationData.isEmpty()) {
                Map<String, Object> firstRecord = investigationData.get(0);
                Object[] mainUpdateFields = new Object[]{
                    commitNo,
                    seqNum,
                    firstRecord.get("inFormal_resl_flag"),
                    firstRecord.get("frwd_to_icg_flag"),
                    convertToDate(firstRecord.get("frwd_to_igc_date"))
                };

                Struct mainUpdateStruct = oracleConnection.createStruct(
                    "TY_GRV_INFO_MAIN_UPD_UPDATE", mainUpdateFields);
                Array mainUpdateArray = oracleConnection.createOracleArray(
                    "TB_GRV_INFO_MAIN_UPD_UPDATE", new Struct[]{mainUpdateStruct});

                try (CallableStatement stmt = conn.prepareCall(
                    "{call spkg_omnet_wrapper.sp_grv_main_upd_update(?)}")) {
                    stmt.setArray(1, mainUpdateArray);
                    stmt.execute();
                }
            }

        } catch (SQLException e) {
            logger.error("Error saving investigation details", e);
            throw new RuntimeException("Error saving investigation details: " + e.getMessage(), e);
        }
    }


    private Clob createClob(Connection conn, String text) throws SQLException {
        if (text == null) return null;
        Clob clob = conn.createClob();
        clob.setString(1, text);
        return clob;
    }

    private Integer getNextInvestigationSeqNo(Connection conn) throws SQLException {
        logger.info("Getting next investigation sequence number from table");
        String maxSeqQuery = "SELECT NVL(MAX(GRVN_INVST_SEQ_NO), 0) + 1 FROM GRIEVANCE_INVST_DTL";
        
        try (PreparedStatement stmt = conn.prepareStatement(maxSeqQuery)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nextSeq = rs.getInt(1);
                    logger.debug("Next sequence number from table: {}", nextSeq);
                    return nextSeq;
                }
            }
        }
        logger.debug("No records found in table, returning default sequence number 1");
        return 1;
    }
 
    /**
     * Saves the list of LSME (Local Subject Matter Expert) members for a facility and grievance type.
     * The service handles inserts for new members, updates for existing ones, and deactivates removed members.
     * @param instNum The institution number.
     * @param grievanceType The code for the grievance type.
     * @param members A list of maps representing the members to be saved.
     * @param userId The ID of the user performing the operation.
     */
    public void saveLSMEMembers(String instNum, String grievanceType, List<Map<String, Object>> members, String userId) {
        logger.info("Starting saveLSMEMembers method for instNum: {}, grievanceType: {}", instNum, grievanceType);
        
        // Filter for members that have a valid user_id.
        List<Map<String, Object>> validMembers = members.stream()
            .filter(m -> m != null && m.get("user_id") != null && !m.get("user_id").toString().isEmpty())
            .collect(Collectors.toList());

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            
            // Get all members currently configured in the database for this facility/type.
            List<Map<String, Object>> existingMembers = getLSMEMembers(instNum, grievanceType);
            
            List<Map<String, Object>> membersToInsert = new ArrayList<>();
            List<Map<String, Object>> membersToUpdate = new ArrayList<>();

            Set<String> existingUserIds = existingMembers.stream()
                .map(m -> (String) m.get("user_id"))
                .collect(Collectors.toSet());

            for (Map<String, Object> member : validMembers) {
                if (existingUserIds.contains((String) member.get("user_id"))) {
                    membersToUpdate.add(member);
                } else {
                    membersToInsert.add(member);
                }
            }

            if (!membersToInsert.isEmpty()) {
                // ... (code for inserting new members)
                logger.info("Inserting {} new LSME members.", membersToInsert.size());
                StructDescriptor insertDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_LSME_BCM_DTL", oracleConn);
                ArrayDescriptor insertArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_LSME_BCM_DTL", oracleConn);
                List<Struct> insertStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToInsert) {
                    Object[] attributes = new Object[] { null, member.get("user_id"), member.get("user_last_name"), member.get("user_first_name"), member.get("user_mid_name"), member.get("user_suffix_name"), "Y", "N", convertToDate(member.get("start_date")), convertToDate(member.get("end_date")), instNum, grievanceType, "A" };
                    Struct struct = new STRUCT(insertDesc, oracleConn, attributes);
                    insertStructs.add(struct);
                }
                ARRAY insertArray = new ARRAY(insertArrayDesc, oracleConn, insertStructs.toArray(new Struct[0]));
                try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_lsme_bcm_dtl_insert(?)}")) {
                    stmt.setArray(1, insertArray);
                    stmt.execute();
                }
            }

            if (!membersToUpdate.isEmpty()) {
                // ... (code for updating existing members)
                 logger.info("Updating {} existing LSME members.", membersToUpdate.size());
                StructDescriptor updateDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_LSME_BCM_DTL", oracleConn);
                ArrayDescriptor updateArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_LSME_BCM_DTL", oracleConn);
                List<Struct> updateStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToUpdate) {
                    existingMembers.stream().filter(em -> em.get("user_id").equals(member.get("user_id"))).findFirst().ifPresent(existingMember -> {
                        try {
                            java.sql.Date endDate = convertToDate(member.get("end_date"));
                            String status = "A";
                            if (endDate != null) {
                                long currentTime = System.currentTimeMillis();
                                java.sql.Date today = new java.sql.Date(currentTime - (currentTime % 86400000));
                                if (endDate.before(today)) {
                                    status = "I";
                                }
                            }
                            Object[] attributes = new Object[] { existingMember.get("lsme_bcme_dtl_seq_num"), member.get("user_id"), member.get("user_last_name"), member.get("user_first_name"), member.get("user_mid_name"), member.get("user_suffix_name"), "Y", "N", convertToDate(member.get("start_date")), endDate, instNum, grievanceType, status };
                            updateStructs.add(new STRUCT(updateDesc, oracleConn, attributes));
                        } catch (SQLException e) { throw new RuntimeException(e); }
                    });
                }
                if (!updateStructs.isEmpty()) {
                    ARRAY updateArray = new ARRAY(updateArrayDesc, oracleConn, updateStructs.toArray(new Struct[0]));
                    try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_lsme_bcm_dtl_update(?)}")) {
                        stmt.setArray(1, updateArray);
                        stmt.execute();
                    }
                }
            }

            Set<String> validUserIds = validMembers.stream()
                .map(m -> (String) m.get("user_id"))
                .collect(Collectors.toSet());
            List<Map<String, Object>> membersToDeactivate = existingMembers.stream()
                .filter(existing -> "A".equals(existing.get("status")) && !validUserIds.contains((String) existing.get("user_id")))
                .collect(Collectors.toList());

            if (!validMembers.isEmpty() && !membersToDeactivate.isEmpty()) {
                logger.info("Deactivating {} LSME members because they were not in the submitted list.", membersToDeactivate.size());
                StructDescriptor deactivateDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_LSME_BCM_DTL", oracleConn);
                ArrayDescriptor deactivateArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_LSME_BCM_DTL", oracleConn);
                List<Struct> deactivateStructs = new ArrayList<>();

                for (Map<String, Object> member : membersToDeactivate) {
                    Object[] attributes = new Object[] {
                        member.get("lsme_bcme_dtl_seq_num"),
                        member.get("user_id"),
                        member.get("user_last_name"),
                        member.get("user_first_name"),
                        member.get("user_mid_name"),
                        member.get("user_suffix_name"),
                        "Y", "N",
                        member.get("start_date"),
                        new java.sql.Date(System.currentTimeMillis()), 
                        instNum,
                        grievanceType,
                        "I" 
                    };
                    deactivateStructs.add(new STRUCT(deactivateDesc, oracleConn, attributes));
                }

                ARRAY deactivateArray = new ARRAY(deactivateArrayDesc, oracleConn, deactivateStructs.toArray(new Struct[0]));
                try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_lsme_bcm_dtl_update(?)}")) {
                    stmt.setArray(1, deactivateArray);
                    stmt.execute();
                }
            } else {
                logger.info("Skipping LSME deactivation logic because the submitted list was empty or no members were marked for deactivation.");
            }
        
        } catch (SQLException e) {
            logger.error("Error saving LSME members", e);
            throw new RuntimeException("Error saving LSME members: " + e.getMessage(), e);
        }
    }

    
    private Map<String, String> getInvestigatorDetails(Connection conn, String userId) throws SQLException {
        Map<String, String> details = new HashMap<>();
        String sql = "SELECT user_first_name, user_last_name, user_mid_name, user_suffix_name FROM omnet_users WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.put("firstName", rs.getString("user_first_name"));
                    details.put("lastName", rs.getString("user_last_name"));
                    details.put("middleName", rs.getString("user_mid_name"));
                    details.put("suffixName", rs.getString("user_suffix_name"));
                } else {
                    details.put("firstName", "");
                    details.put("lastName", "");
                    details.put("middleName", "");
                    details.put("suffixName", "");
                }
            }
        }
        return details;
    }

    private void updateGrievanceDetails(Connection conn, String commitNo, Integer seqNum, Map<String, Object> grievanceData) throws SQLException {
        logger.info("Starting updateGrievanceDetails for commitNo: {}, seqNum: {}", commitNo, seqNum);
        OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

        Clob abandonedCommentsClob = createClob(conn, (String) grievanceData.get("abandoned_comments"));
        Clob otherDescClob = createClob(conn, (String) grievanceData.get("resn_retrn_other_desc"));

        BigDecimal dscpActNum = grievanceData.get("resn_retrn_dscp_act_num") != null && !grievanceData.get("resn_retrn_dscp_act_num").toString().isEmpty()
            ? new BigDecimal(grievanceData.get("resn_retrn_dscp_act_num").toString()) : null;
        BigDecimal duplGrvNum = grievanceData.get("resn_retrn_dupl_grv_num") != null && !grievanceData.get("resn_retrn_dupl_grv_num").toString().isEmpty()
            ? new BigDecimal(grievanceData.get("resn_retrn_dupl_grv_num").toString()) : null;
        BigDecimal grvnAmount = grievanceData.get("grvnAmount") != null && !grievanceData.get("grvnAmount").toString().isEmpty()
            ? new BigDecimal(grievanceData.get("grvnAmount").toString()) : null;

        Object[] detailFields = new Object[]{
            commitNo,
            seqNum,
            grievanceData.get("grievanceStatus"),
            grievanceData.get("grvnLvlResl"),
            convertToDate(grievanceData.get("resolutionDate")),
            null, // request_to_igc
            null, // req_to_igc_comments
            null, // igc_comments
            grvnAmount,
            grievanceData.get("reason_for_abandoned"),
            abandonedCommentsClob,
            grievanceData.get("resn_retrn_vul_thread_lang"),
            grievanceData.get("resn_retrn_dscp_act"),
            dscpActNum,
            grievanceData.get("resn_retrn_dscp_act_par_dcsn"),
            grievanceData.get("resn_retrn_dscp_act_clsfn_act"),
            grievanceData.get("resn_retrn_act_prohb_mail"),
            null, // resn_retrn_request
            grievanceData.get("resn_retrn_dupl_grv"),
            duplGrvNum,
            grievanceData.get("resn_retrn_need_orig_grv"),
            grievanceData.get("resn_retrn_othr_inmate_enq"),
            grievanceData.get("resn_retrn_expired"),
            null, // resn_retrn_staff_invst
            grievanceData.get("resn_retrn_other"),
            otherDescClob,
            grievanceData.get("resn_retrn_abuse")
        };
        
        Struct detailStruct = oracleConnection.createStruct("TY_GRV_INFO_DTL_UPDATE", detailFields);
        Array detailArray = oracleConnection.createOracleArray("TB_GRV_INFO_DTL_UPDATE", new Struct[]{detailStruct});

        try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_dtl_update(?)}")) {
            stmt.setArray(1, detailArray);
            stmt.execute();
            logger.info("Grievance details (status, etc.) updated successfully.");
        }
    }

    private Date convertToDate(Object dateObj) {
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

    /**
     * Saves the list of persons (staff or inmates) involved in a grievance.
     * For updates, it first deletes existing records before inserting the new list.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param personsData A list of maps, where each map contains data for one involved person.
     * @param isNew A boolean flag indicating if this is a new grievance.
     */
    public void savePersonsInvolved(String commitNo, Integer seqNum, List<Map<String, Object>> personsData, boolean isNew) {
        if (commitNo == null || seqNum == null || personsData == null) {
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            if (!isNew) {
                try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM GRIEVANCE_STAFF_INVOLVED WHERE commit_no = ? AND grievance_seq_num = ?")) {
                    stmt.setString(1, commitNo);
                    stmt.setInt(2, seqNum);
                    stmt.executeUpdate();
                }
            }

            // Get the next sequence number for new inserts (across all records)
            int nextSeqNo = getNextInvolSeqNo(conn);
            
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
            List<Struct> personStructs = new ArrayList<>();

            for (Map<String, Object> person : personsData) {
                if (person == null || person.get("type") == null) continue;

                String type = person.get("type").toString();
                if (!"S".equals(type) && !"I".equals(type)) continue;

                String cmtyType = person.containsKey("cmty_type") ? person.get("cmty_type").toString() : "IND";

                Object[] personFields = new Object[]{
                    seqNum,
                    commitNo,
                    nextSeqNo++, // Use the globally unique sequence number
                    "S".equals(type) ? person.get("user_id") : null,
                    "S".equals(type) ? person.get("last_name") : null,
                    "S".equals(type) ? person.get("first_name") : null,
                    "S".equals(type) ? person.get("mid_name") : null,
                    "S".equals(type) ? person.get("suffix_name") : null,
                    type,
                    "I".equals(type) ? person.get("commit_no") : null,
                    cmtyType,
                    null, 
                    "I".equals(type) ? person.get("sbi_no") : null,
                    null 
                };

                Struct personStruct = oracleConnection.createStruct("TY_GRV_INFO_PERSONS_INVOLVED", personFields);
                personStructs.add(personStruct);
            }

            if (!personStructs.isEmpty()) {
                Array personArray = oracleConnection.createOracleArray(
                    "TB_GRV_INFO_PERSONS_INVOLVED",
                    personStructs.toArray(new Struct[0])
                );

                try (CallableStatement stmt = conn.prepareCall(
                    "{call spkg_omnet_wrapper.sp_grv_persons_involved_insert(?)}")) {
                    stmt.setArray(1, personArray);
                    stmt.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving persons involved data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Saves the details of an IGC (Institutional Grievance Coordinator) review.
     * This service inserts or updates the review record in the database.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param igcData A map containing the IGC review data to be saved.
     */
    public void saveIGCDetails(String commitNo, Integer seqNum, Map<String, Object> igcData) {
        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
            
            Clob recmCmntClob = createClob(conn, (String) igcData.get("grvn_rew_recm_comnt"));
            Object[] igcFields = new Object[]{
                commitNo,
                seqNum,
                igcData.get("grvn_rew_seq_no"),
                "IGC",
                igcData.get("user_id"),
                igcData.get("user_last_name"),
                igcData.get("user_first_name"),
                igcData.get("user_mid_name"),
                igcData.get("user_suffix_name"),
                convertToDate(igcData.get("grvn_rew_asgn_dt")),
                igcData.get("medi_prvd_cd"),
                convertToDate(igcData.get("grvn_rew_recm_dt")),
                recmCmntClob,
                igcData.get("grvn_decn_ntf_flg"),
                convertToDate(igcData.get("grvn_decn_ntf_dt")),
                igcData.get("frwd_med_provider_flg"),
                convertToDate(igcData.get("frwd_med_provider_date")),
                igcData.get("frwd_to_medicalprovider_flg2"),
                convertToDate(igcData.get("grvn_rew_rcpt_dt")),
                igcData.get("grvn_rew_frwd_to_flg"),
                igcData.get("notify_warden_flg")
            };
            
            Struct igcStruct = oracleConnection.createStruct("TY_GRV_INFO_IGC_INSUPD", igcFields);
            Array igcArray = oracleConnection.createOracleArray("TB_GRV_INFO_IGC_INSUPD", new Struct[]{igcStruct});

            try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_igc_insert(?)}")) {
                stmt.setArray(1, igcArray);
                stmt.execute();
            }
        } catch (SQLException e) {
            logger.error("Error saving IGC details", e);
            throw new RuntimeException("Error saving IGC details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Saves additional comments added after a grievance has been resolved.
     * The service deletes all existing comments for the grievance and inserts the new set.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param commentsData A list of maps, where each map is a comment to be saved.
     * @param userId The ID of the user adding the comments.
     */
    public void saveAdditionalComments(String commitNo, Integer seqNum, List<Map<String, Object>> commentsData, String userId) {
        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
            try (PreparedStatement deleteStmt = conn.prepareStatement(
                "DELETE FROM GRIEVANCE_ADTL_COMNTS_AFT_RESL WHERE commit_no = ? AND grievance_seq_num = ?")) {
                deleteStmt.setString(1, commitNo);
                deleteStmt.setInt(2, seqNum);
                deleteStmt.executeUpdate();
            }
            
            // Now insert all comments fresh
            if (!commentsData.isEmpty()) {
                List<Struct> commentStructs = new ArrayList<>();
                StructDescriptor structDesc = StructDescriptor.createDescriptor(
                    "TY_GRV_INFO_AFT_RESLV_INSUPD", oracleConnection);
                ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor(
                    "TB_GRV_INFO_AFT_RESLV_INSUPD", oracleConnection);
                
                for (Map<String, Object> comment : commentsData) {
                    Clob commentClob = createClob(conn, (String) comment.get("comments"));
                    
                    Object[] fields = new Object[]{
                        null, 
                        commitNo,
                        seqNum,
                        userId,
                        comment.get("user_name"),
                        convertToDate(comment.get("date_entered")),
                        commentClob
                    };
                    
                    Struct struct = new STRUCT(structDesc, oracleConnection, fields);
                    commentStructs.add(struct);
                }
                
                ARRAY commentsArray = new ARRAY(arrayDesc, oracleConnection, 
                    commentStructs.toArray(new Struct[0]));

                try (CallableStatement stmt = conn.prepareCall(
                    "{call spkg_omnet_wrapper.sp_grv_cmnts_aft_reslv_insert(?, ?)}")) {
                    stmt.setArray(1, commentsArray);
                    stmt.setString(2, userId);
                    stmt.execute();
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving additional comments", e);
            throw new RuntimeException("Error saving additional comments: " + e.getMessage(), e);
        }
    }
   
    /**
     * Retrieves the details of the RGC (Regional Grievance Coordinator) review for a grievance.
     * This service fetches recommendation details and review dates associated with the RGC.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param cmtyType The committee type, which should be 'RGC'.
     * @return A map containing the RGC review details.
     */
    public Map<String, Object> getRGCDetails(String commitNo, Integer seqNum, String cmtyType) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null || cmtyType == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_GRV_RGC_QUERY(?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.setString(3, cmtyType);
                stmt.registerOutParameter(4, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                    if (rs.next()) {
                        result.put("grvn_rew_rcpt_dt", rs.getDate("grvn_rew_rcpt_dt"));
                        result.put("grvn_rew_recm_dt", rs.getDate("grvn_rew_recm_dt"));
                        result.put("grvn_rew_recm_desc", rs.getString("grvn_rew_recm_desc"));
                        result.put("grvn_rew_frwd_to_flg", rs.getString("grvn_rew_frwd_to_flg"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching RGC details", e);
        }
        return result;
    }
    
    /**
     * Saves the details of an RGC (Regional Grievance Coordinator) review.
     * It handles both inserting a new review and updating an existing one.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param rgcData A map containing the RGC review details and member list.
     * @param userId The ID of the user performing the save.
     */
    public void saveRGCDetails(String commitNo, Integer seqNum, Map<String, Object> rgcData, String userId) {
    	 synchronized (grvnRewDtlLock) {
        logger.info("Starting saveRGCDetails method for commitNo: {}, seqNum: {}", commitNo, seqNum);

        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null (commitNo, seqNum, userId)");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

            Integer grvnRewSeqNo = getExistingRGCRewSeqNo(conn, commitNo, seqNum);
            boolean isNew = (grvnRewSeqNo == null);

            if (isNew) {
                grvnRewSeqNo = getNextGrvnRewSeqNo(conn); // Changed to use the new method
                logger.info("No existing RGC record found. Performing INSERT with new grvn_rew_seq_no: {}", grvnRewSeqNo);
            } else {
                logger.info("Existing RGC record found with grvn_rew_seq_no: {}. Performing UPDATE.", grvnRewSeqNo);
            }

            Clob recmCmntClob = createClob(conn, (String) rgcData.get("grvn_rew_recm_desc"));

            Object[] rgcFields = new Object[]{
                commitNo,
                seqNum,
                grvnRewSeqNo,
                "RGC",
                convertToDate(rgcData.get("grvn_rew_rcpt_dt")),
                convertToDate(rgcData.get("grvn_rew_recm_dt")),
                recmCmntClob,
                rgcData.get("grvn_rew_frwd_to_flg"),
                convertToDate(rgcData.get("grvn_rew_frwd_dt"))
            };

            Struct rgcStruct = oracleConnection.createStruct("TY_GRV_RGC_INSUPD", rgcFields);
            Array rgcArray = oracleConnection.createOracleArray("TB_GRV_RGC_INSUPD", new Struct[]{rgcStruct});

            String procedureName = isNew ? "spkg_omnet_wrapper.sp_grv_info_rgc_insert" 
                                       : "spkg_omnet_wrapper.sp_grv_info_rgc_update";
            
            try (CallableStatement stmt = conn.prepareCall("{call " + procedureName + "(?)}")) {
                stmt.setArray(1, rgcArray);
                stmt.execute();
            }

            if (rgcData.containsKey("members")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> members = (List<Map<String, Object>>) rgcData.get("members");
                saveRGCMembers(commitNo, seqNum, members, isNew);
            }

        } catch (SQLException e) {
            logger.error("Error saving RGC details", e);
            throw new RuntimeException("Error saving RGC details: " + e.getMessage(), e);
        }
    }
    }

    private Integer getExistingRGCRewSeqNo(Connection conn, String commitNo, Integer seqNum) throws SQLException {
        logger.info("Checking for existing RGC record for commitNo: {}, seqNum: {}", commitNo, seqNum);
        String sql = "SELECT GRVN_REW_SEQ_NO FROM GRIEVANCE_REW_DTL " +
                     "WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ? AND GRVN_REW_CMTY_TP = 'RGC'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            stmt.setInt(2, seqNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int seqNo = rs.getInt(1);
                    logger.info("Found existing RGC record with GRVN_REW_SEQ_NO: {}", seqNo);
                    return seqNo;
                }
            }
        }
        logger.info("No existing RGC record found.");
        return null;
    }
    
    
    private void saveRGCMembers(String commitNo, Integer seqNum, List<Map<String, Object>> members, boolean isNew) {
        logger.info("Starting saveRGCMembers method");
        
        if (commitNo == null || seqNum == null || members == null) {
            logger.error("Required parameters cannot be null");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            // First delete existing RGC members if this is an update
            if (!isNew) {
                try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM GRIEVANCE_STAFF_INVOLVED WHERE commit_no = ? AND grievance_seq_num = ? AND cmty_type = 'RGC'")) {
                    stmt.setString(1, commitNo);
                    stmt.setInt(2, seqNum);
                    stmt.executeUpdate();
                }
            }

            // Get the next sequence number for new inserts (across all records)
            int nextSeqNo = getNextInvolSeqNo(conn);
            
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
            List<Struct> memberStructs = new ArrayList<>();

            for (Map<String, Object> member : members) {
                if (member == null || member.get("type") == null) continue;

                String type = member.get("type").toString();
                if (!"S".equals(type) && !"I".equals(type)) continue;

                Object[] memberFields = new Object[]{
                    seqNum,
                    commitNo,
                    nextSeqNo++, // Use the sequence number and increment
                    "S".equals(type) ? member.get("user_id") : null,
                    "S".equals(type) ? member.get("last_name") : null,
                    "S".equals(type) ? member.get("first_name") : null,
                    "S".equals(type) ? member.get("mid_name") : null,
                    "S".equals(type) ? member.get("suffix_name") : null,
                    type,
                    "I".equals(type) ? member.get("commit_no") : null,
                    "RGC", // cmty_type
                    member.get("vote_type"),
                    "I".equals(type) ? member.get("sbi_no") : null,
                    member.get("tie_breaker_flag")
                };

                Struct memberStruct = oracleConnection.createStruct("TY_GRV_INFO_PERSONS_INVOLVED", memberFields);
                memberStructs.add(memberStruct);
            }

            if (!memberStructs.isEmpty()) {
                Array memberArray = oracleConnection.createOracleArray(
                    "TB_GRV_INFO_PERSONS_INVOLVED",
                    memberStructs.toArray(new Struct[0])
                );

                try (CallableStatement stmt = conn.prepareCall(
                    "{call spkg_omnet_wrapper.sp_grv_persons_involved_insert(?)}")) {
                    stmt.setArray(1, memberArray);
                    stmt.execute();
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving RGC members", e);
            throw new RuntimeException("Error saving RGC members: " + e.getMessage(), e);
        }
    }
    
    private int getNextInvolSeqNo(Connection conn) throws SQLException {
        String sql = "SELECT NVL(MAX(invol_seq_num), 0) + 1 FROM GRIEVANCE_STAFF_INVOLVED";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 1;
    }
    
    /**
     * Fetches the list of all configured RGC (Regional Grievance Coordinator) members for an institution.
     * This data is used for managing RGC committee assignments.
     * @param instNum The institution number.
     * @return A list of maps, where each map represents an RGC member's details.
     */
    public List<Map<String, Object>> getRGCMembers(String instNum) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (instNum == null) return result;
    
        String sql = "{call DACS.SPKG_GRV_INFO.Sp_Rgc_Dtl_Query(?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
    
            stmt.setString(1, instNum);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
    
            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("rgc_mgc_dtl_seq_num", rs.getInt("rgc_mgc_dtl_seq_num"));
                    row.put("type_of_person", rs.getString("type_of_person"));
                    row.put("sbi_no", rs.getString("sbi_no"));
                    row.put("user_id", rs.getString("user_id"));
                    row.put("user_last_name", rs.getString("user_last_name"));
                    row.put("user_first_name", rs.getString("user_first_name"));
                    row.put("user_mid_name", rs.getString("user_mid_name"));
                    row.put("user_suffix_name", rs.getString("user_suffix_name"));
                    row.put("rgc_flag", rs.getString("rgc_flag"));
                    row.put("mgc_flag", rs.getString("mgc_flag"));
                    row.put("start_date", rs.getDate("start_date"));
                    row.put("end_date", rs.getDate("end_date"));
                    row.put("inst_num", rs.getString("inst_num"));
                    row.put("status", rs.getString("status"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching RGC members for instNum: " + instNum, e);
        }
        return result;
    }
    
    /**
     * Saves the master list of RGC (Regional Grievance Coordinator) members for an institution.
     * The service adds new members, updates existing ones, and deactivates members not in the provided list.
     * @param instNum The institution number.
     * @param members A list of maps containing the details of all members to be configured.
     * @param userId The ID of the user performing the save.
     */
    public void saveRGCMembers(String instNum, List<Map<String, Object>> members, String userId) {
        logger.info("Starting saveRGCMembers for instNum: {}", instNum);
        
        // Filter for members that have a valid type and identifier (user_id or sbi_no)
        List<Map<String, Object>> validMembers = members.stream()
            .filter(m -> m != null && 
                       (m.get("type") != null && !m.get("type").toString().isEmpty()) &&
                       ((m.get("user_id") != null && !m.get("user_id").toString().isEmpty()) || 
                        (m.get("sbi_no") != null && !m.get("sbi_no").toString().isEmpty())))
            .collect(Collectors.toList());

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            
            // Get all members currently configured in the database for this facility.
            List<Map<String, Object>> existingMembers = getRGCMembers(instNum);
            
            List<Map<String, Object>> membersToInsert = new ArrayList<>();
            List<Map<String, Object>> membersToUpdate = new ArrayList<>();

            for (Map<String, Object> member : validMembers) {
                boolean isExisting = existingMembers.stream().anyMatch(existing -> {
                    String existingUserId = (String) existing.get("user_id");
                    String existingSbiNo = (String) existing.get("sbi_no");
                    String newUserId = (String) member.get("user_id");
                    String newSbiNo = (String) member.get("sbi_no");
                    
                    return ("S".equals(existing.get("type_of_person")) && existingUserId != null && existingUserId.equals(newUserId)) ||
                           ("I".equals(existing.get("type_of_person")) && existingSbiNo != null && existingSbiNo.equals(newSbiNo));
                });
                
                if (isExisting) {
                    membersToUpdate.add(member);
                } else {
                    membersToInsert.add(member);
                }
            }
            
            // Handle inserts for new members
            if (!membersToInsert.isEmpty()) {
                logger.info("Inserting {} new RGC members.", membersToInsert.size());
                StructDescriptor insertDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_RE_RGC_DTL", oracleConn);
                ArrayDescriptor insertArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_RE_RGC_DTL", oracleConn);
                
                List<Struct> insertStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToInsert) {
                    Object[] attributes = new Object[] {
                        null, // RGC_MGC_DTL_SEQ_NUM (generated by DB)
                        member.get("type"),
                        member.get("sbi_no"),
                        member.get("user_id"),
                        member.get("last_name"),
                        member.get("first_name"),
                        member.get("mid_name"),
                        member.get("suffix_name"),
                        "Y", // RGC_FLAG
                        "N", // MGC_FLAG
                        convertToDate(member.get("start_date")),
                        convertToDate(member.get("end_date")),
                        instNum,
                        "A"  // STATUS
                    };
                    insertStructs.add(new STRUCT(insertDesc, oracleConn, attributes));
                }
                
                ARRAY insertArray = new ARRAY(insertArrayDesc, oracleConn, insertStructs.toArray(new Struct[0]));
                try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_rgc_dtl_insert(?)}")) {
                    stmt.setArray(1, insertArray);
                    stmt.execute();
                }
            }
            
            // Handle updates for existing members
            if (!membersToUpdate.isEmpty()) {
                logger.info("Updating {} existing RGC members.", membersToUpdate.size());
                StructDescriptor updateDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_RE_RGC_DTL", oracleConn);
                ArrayDescriptor updateArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_RE_RGC_DTL", oracleConn);
                
                List<Struct> updateStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToUpdate) {
                    existingMembers.stream().filter(em -> 
                        (em.get("user_id") != null && em.get("user_id").equals(member.get("user_id"))) ||
                        (em.get("sbi_no") != null && em.get("sbi_no").equals(member.get("sbi_no")))
                    ).findFirst().ifPresent(existingMember -> {
                        try {
                            Object[] attributes = new Object[] {
                                existingMember.get("rgc_mgc_dtl_seq_num"),
                                member.get("type"),
                                member.get("sbi_no"),
                                member.get("user_id"),
                                member.get("last_name"),
                                member.get("first_name"),
                                member.get("mid_name"),
                                member.get("suffix_name"),
                                "Y", "N",
                                convertToDate(member.get("start_date")),
                                convertToDate(member.get("end_date")),
                                instNum, "A"
                            };
                            updateStructs.add(new STRUCT(updateDesc, oracleConn, attributes));
                        } catch (SQLException e) { throw new RuntimeException(e); }
                    });
                }
                
                if (!updateStructs.isEmpty()) {
                    ARRAY updateArray = new ARRAY(updateArrayDesc, oracleConn, updateStructs.toArray(new Struct[0]));
                    try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_rgc_dtl_update(?)}")) {
                        stmt.setArray(1, updateArray);
                        stmt.execute();
                    }
                }
            }
            
            // Handle deactivations (members present in DB but not in submitted valid list)
            Set<String> validUserIds = validMembers.stream().filter(m -> "S".equals(m.get("type"))).map(m -> (String) m.get("user_id")).collect(Collectors.toSet());
            Set<String> validSbiNos = validMembers.stream().filter(m -> "I".equals(m.get("type"))).map(m -> (String) m.get("sbi_no")).collect(Collectors.toSet());

            List<Map<String, Object>> membersToDeactivate = existingMembers.stream()
                .filter(existing -> "A".equals(existing.get("status")) && 
                    (("S".equals(existing.get("type_of_person")) && !validUserIds.contains((String) existing.get("user_id"))) ||
                     ("I".equals(existing.get("type_of_person")) && !validSbiNos.contains((String) existing.get("sbi_no"))))
                ).collect(Collectors.toList());
            
            if (!membersToDeactivate.isEmpty()) {
                logger.info("Deactivating {} RGC members.", membersToDeactivate.size());
                StructDescriptor deactivateDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_RE_RGC_DTL", oracleConn);
                ArrayDescriptor deactivateArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_RE_RGC_DTL", oracleConn);
                
                List<Struct> deactivateStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToDeactivate) {
                    Object[] attributes = new Object[] {
                        member.get("rgc_mgc_dtl_seq_num"),
                        member.get("type_of_person"),
                        member.get("sbi_no"),
                        member.get("user_id"),
                        member.get("user_last_name"),
                        member.get("user_first_name"),
                        member.get("user_mid_name"),
                        member.get("user_suffix_name"),
                        "Y", "N",
                        member.get("start_date"),
                        new java.sql.Date(System.currentTimeMillis()), // Set end date to today
                        instNum,
                        "I" // Inactive status
                    };
                    deactivateStructs.add(new STRUCT(deactivateDesc, oracleConn, attributes));
                }
                
                ARRAY deactivateArray = new ARRAY(deactivateArrayDesc, oracleConn, deactivateStructs.toArray(new Struct[0]));
                try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_rgc_dtl_update(?)}")) {
                    stmt.setArray(1, deactivateArray);
                    stmt.execute();
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving RGC members", e);
            throw new RuntimeException("Error saving RGC members: " + e.getMessage(), e);
        }
    }
  
    /**
     * Retrieves details of the MGC (Medical Grievance Coordinator) review for a grievance.
     * It fetches key information like receipt dates, recommendations, and forwarding status.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param cmtyType The committee type, which should be 'MGC'.
     * @return A map containing the MGC review details.
     */
    public Map<String, Object> getMGCDetails(String commitNo, Integer seqNum, String cmtyType) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null || cmtyType == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_MGC_QUERY(?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.setString(3, cmtyType);
                stmt.registerOutParameter(4, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                    if (rs.next()) {
                        result.put("grvn_rew_rcpt_dt", rs.getDate("grvn_rew_rcpt_dt"));
                        result.put("grvn_rew_recm_dt", rs.getDate("grvn_rew_recm_dt"));
                        result.put("grvn_rew_recm_desc", rs.getString("grvn_rew_recm_desc"));
                        result.put("grvn_rew_frwd_to_flg", rs.getString("grvn_rew_frwd_to_flg"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching MGC details", e);
        }
        return result;
    }
    
    /**
     * Saves the details of an MGC (Medical Grievance Coordinator) review.
     * This service handles both the creation of new MGC review records and updates to existing ones.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param mgcData A map containing the MGC review details and member list.
     * @param userId The ID of the user performing the save.
     */
    public void saveMGCDetails(String commitNo, Integer seqNum, Map<String, Object> mgcData, String userId) {
    	 synchronized (grvnRewDtlLock) {
        logger.info("Starting saveMGCDetails method for commitNo: {}, seqNum: {}", commitNo, seqNum);

        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null (commitNo, seqNum, userId)");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

            Integer grvnRewSeqNo = getExistingMGCRewSeqNo(conn, commitNo, seqNum);
            boolean isNew = (grvnRewSeqNo == null);

            if (isNew) {
                grvnRewSeqNo = getNextGrvnRewSeqNo(conn); // Changed to use the new method
                logger.info("No existing MGC record found. Performing INSERT with new grvn_rew_seq_no: {}", grvnRewSeqNo);
            } else {
                logger.info("Existing MGC record found with grvn_rew_seq_no: {}. Performing UPDATE.", grvnRewSeqNo);
            }

            Clob recmCmntClob = createClob(conn, (String) mgcData.get("grvn_rew_recm_desc"));

            Object[] mgcFields = new Object[]{
                commitNo,
                seqNum,
                grvnRewSeqNo,
                "MGC",
                convertToDate(mgcData.get("grvn_rew_rcpt_dt")),
                convertToDate(mgcData.get("grvn_rew_recm_dt")),
                recmCmntClob,
                mgcData.get("grvn_rew_frwd_to_flg"),
                convertToDate(mgcData.get("grvn_rew_frwd_dt"))
            };

            Struct mgcStruct = oracleConnection.createStruct("TY_GRV_INFO_MGC_INSUPD", mgcFields);
            Array mgcArray = oracleConnection.createOracleArray("TB_GRV_INFO_MGC_INSUPD", new Struct[]{mgcStruct});

            String procedureName = isNew ? "spkg_omnet_wrapper.sp_grv_info_mgc_insert" 
                                       : "spkg_omnet_wrapper.sp_grv_info_mgc_update";
            
            try (CallableStatement stmt = conn.prepareCall("{call " + procedureName + "(?)}")) {
                stmt.setArray(1, mgcArray);
                stmt.execute();
            }

            if (mgcData.containsKey("members")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> members = (List<Map<String, Object>>) mgcData.get("members");
                saveMGCMembers(commitNo, seqNum, members, isNew);
            }

        } catch (SQLException e) {
            logger.error("Error saving MGC details", e);
            throw new RuntimeException("Error saving MGC details: " + e.getMessage(), e);
        }
    }
    }

    private Integer getExistingMGCRewSeqNo(Connection conn, String commitNo, Integer seqNum) throws SQLException {
        logger.info("Checking for existing MGC record for commitNo: {}, seqNum: {}", commitNo, seqNum);
        String sql = "SELECT GRVN_REW_SEQ_NO FROM GRIEVANCE_REW_DTL " +
                     "WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ? AND GRVN_REW_CMTY_TP = 'MGC'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            stmt.setInt(2, seqNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int seqNo = rs.getInt(1);
                    logger.info("Found existing MGC record with GRVN_REW_SEQ_NO: {}", seqNo);
                    return seqNo;
                }
            }
        }
        logger.info("No existing MGC record found.");
        return null;
    }

    private void saveMGCMembers(String commitNo, Integer seqNum, List<Map<String, Object>> members, boolean isNew) {
        logger.info("Starting saveMGCMembers method");
        
        if (commitNo == null || seqNum == null || members == null) {
            logger.error("Required parameters cannot be null");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            // First delete existing MGC members if this is an update
            if (!isNew) {
                try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM GRIEVANCE_STAFF_INVOLVED WHERE commit_no = ? AND grievance_seq_num = ? AND cmty_type = 'MGC'")) {
                    stmt.setString(1, commitNo);
                    stmt.setInt(2, seqNum);
                    stmt.executeUpdate();
                }
            }

            // Get the next sequence number for new inserts (across all records)
            int nextSeqNo = getNextInvolSeqNo(conn);
            
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);
            List<Struct> memberStructs = new ArrayList<>();

            for (Map<String, Object> member : members) {
                if (member == null || member.get("type") == null) continue;

                String type = member.get("type").toString();
                if (!"S".equals(type) && !"I".equals(type)) continue;

                Object[] memberFields = new Object[]{
                    seqNum,
                    commitNo,
                    nextSeqNo++, // Use the sequence number and increment
                    "S".equals(type) ? member.get("user_id") : null,
                    "S".equals(type) ? member.get("last_name") : null,
                    "S".equals(type) ? member.get("first_name") : null,
                    "S".equals(type) ? member.get("mid_name") : null,
                    "S".equals(type) ? member.get("suffix_name") : null,
                    type,
                    "I".equals(type) ? member.get("commit_no") : null,
                    "MGC", // cmty_type
                    member.get("vote_type"),
                    "I".equals(type) ? member.get("sbi_no") : null,
                    member.get("tie_breaker_flag")
                };

                Struct memberStruct = oracleConnection.createStruct("TY_GRV_INFO_PERSONS_INVOLVED", memberFields);
                memberStructs.add(memberStruct);
            }

            if (!memberStructs.isEmpty()) {
                Array memberArray = oracleConnection.createOracleArray(
                    "TB_GRV_INFO_PERSONS_INVOLVED",
                    memberStructs.toArray(new Struct[0])
                );

                try (CallableStatement stmt = conn.prepareCall(
                    "{call spkg_omnet_wrapper.sp_grv_persons_involved_insert(?)}")) {
                    stmt.setArray(1, memberArray);
                    stmt.execute();
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving MGC members", e);
            throw new RuntimeException("Error saving MGC members: " + e.getMessage(), e);
        }
    }
    
    /**
     * Fetches the list of all configured MGC (Medical Grievance Coordinator) members for an institution.
     * This is used to manage the members of the medical grievance committee.
     * @param instNum The institution number.
     * @return A list of maps, with each map containing the details of an MGC member.
     */
    public List<Map<String, Object>> getMGCMembers(String instNum) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (instNum == null) return result;

        String sql = "{call DACS.SPKG_GRV_INFO.Sp_Mgc_Dtl_Query(?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, instNum);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("rgc_mgc_dtl_seq_num", rs.getInt("rgc_mgc_dtl_seq_num"));
                    row.put("type_of_person", rs.getString("type_of_person"));
                    row.put("sbi_no", rs.getString("sbi_no"));
                    row.put("user_id", rs.getString("user_id"));
                    row.put("user_last_name", rs.getString("user_last_name"));
                    row.put("user_first_name", rs.getString("user_first_name"));
                    row.put("user_mid_name", rs.getString("user_mid_name"));
                    row.put("user_suffix_name", rs.getString("user_suffix_name"));
                    row.put("rgc_flag", rs.getString("rgc_flag"));
                    row.put("mgc_flag", rs.getString("mgc_flag"));
                    row.put("start_date", rs.getDate("start_date"));
                    row.put("end_date", rs.getDate("end_date"));
                    row.put("inst_num", rs.getString("inst_num"));
                    row.put("status", rs.getString("status"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching MGC members for instNum: " + instNum, e);
        }
        return result;
    }

    /**
     * Saves the master list of MGC (Medical Grievance Coordinator) members for an institution.
     * This service synchronizes the database by adding, updating, and deactivating members based on the input list.
     * @param instNum The institution number.
     * @param members A list of maps representing the MGC members to be saved.
     * @param userId The ID of the user performing the save operation.
     */
    public void saveMGCMembers(String instNum, List<Map<String, Object>> members, String userId) {
        logger.info("Starting saveMGCMembers for instNum: {}", instNum);
        
        // Filter for members that have a valid type and identifier (user_id or sbi_no)
        List<Map<String, Object>> validMembers = members.stream()
            .filter(m -> m != null &&
                       (m.get("type") != null && !m.get("type").toString().isEmpty()) &&
                       ((m.get("user_id") != null && !m.get("user_id").toString().isEmpty()) || 
                        (m.get("sbi_no") != null && !m.get("sbi_no").toString().isEmpty())))
            .collect(Collectors.toList());

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            
            // Get all members currently configured in the database for this facility.
            List<Map<String, Object>> existingMembers = getMGCMembers(instNum);
            
            List<Map<String, Object>> membersToInsert = new ArrayList<>();
            List<Map<String, Object>> membersToUpdate = new ArrayList<>();

            for (Map<String, Object> member : validMembers) {
                boolean isExisting = existingMembers.stream().anyMatch(existing -> {
                    String existingUserId = (String) existing.get("user_id");
                    String existingSbiNo = (String) existing.get("sbi_no");
                    String newUserId = (String) member.get("user_id");
                    String newSbiNo = (String) member.get("sbi_no");
                    
                    return ("S".equals(existing.get("type_of_person")) && existingUserId != null && existingUserId.equals(newUserId)) ||
                           ("I".equals(existing.get("type_of_person")) && existingSbiNo != null && existingSbiNo.equals(newSbiNo));
                });
                
                if (isExisting) {
                    membersToUpdate.add(member);
                } else {
                    membersToInsert.add(member);
                }
            }
            
            // Handle inserts for new members
            if (!membersToInsert.isEmpty()) {
                logger.info("Inserting {} new MGC members.", membersToInsert.size());
                StructDescriptor insertDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_RE_MGC_DTL", oracleConn);
                ArrayDescriptor insertArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_RE_MGC_DTL", oracleConn);
                
                List<Struct> insertStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToInsert) {
                    Object[] attributes = new Object[] {
                        null, // RGC_MGC_DTL_SEQ_NUM (generated by DB)
                        member.get("type"),
                        member.get("sbi_no"),
                        member.get("user_id"),
                        member.get("last_name"),
                        member.get("first_name"),
                        member.get("mid_name"),
                        member.get("suffix_name"),
                        "N", // RGC_FLAG
                        "Y", // MGC_FLAG
                        convertToDate(member.get("start_date")),
                        convertToDate(member.get("end_date")),
                        instNum,
                        "A"  // STATUS
                    };
                    insertStructs.add(new STRUCT(insertDesc, oracleConn, attributes));
                }
                
                ARRAY insertArray = new ARRAY(insertArrayDesc, oracleConn, insertStructs.toArray(new Struct[0]));
                try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_mgc_dtl_insert(?)}")) {
                    stmt.setArray(1, insertArray);
                    stmt.execute();
                }
            }
            
            // Handle updates for existing members
            if (!membersToUpdate.isEmpty()) {
                logger.info("Updating {} existing MGC members.", membersToUpdate.size());
                StructDescriptor updateDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_RE_MGC_DTL", oracleConn);
                ArrayDescriptor updateArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_RE_MGC_DTL", oracleConn);
                
                List<Struct> updateStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToUpdate) {
                     existingMembers.stream().filter(em -> 
                        (em.get("user_id") != null && em.get("user_id").equals(member.get("user_id"))) ||
                        (em.get("sbi_no") != null && em.get("sbi_no").equals(member.get("sbi_no")))
                    ).findFirst().ifPresent(existingMember -> {
                        try {
                            Object[] attributes = new Object[] {
                                existingMember.get("rgc_mgc_dtl_seq_num"),
                                member.get("type"),
                                member.get("sbi_no"),
                                member.get("user_id"),
                                member.get("last_name"),
                                member.get("first_name"),
                                member.get("mid_name"),
                                member.get("suffix_name"),
                                "N", "Y",
                                convertToDate(member.get("start_date")),
                                convertToDate(member.get("end_date")),
                                instNum, "A"
                            };
                            updateStructs.add(new STRUCT(updateDesc, oracleConn, attributes));
                        } catch (SQLException e) { throw new RuntimeException(e); }
                    });
                }
                
                if (!updateStructs.isEmpty()) {
                    ARRAY updateArray = new ARRAY(updateArrayDesc, oracleConn, updateStructs.toArray(new Struct[0]));
                    try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_mgc_dtl_update(?)}")) {
                        stmt.setArray(1, updateArray);
                        stmt.execute();
                    }
                }
            }
            
            // Handle deactivations (members present in DB but not in submitted valid list)
            Set<String> validUserIds = validMembers.stream().filter(m -> "S".equals(m.get("type"))).map(m -> (String) m.get("user_id")).collect(Collectors.toSet());
            Set<String> validSbiNos = validMembers.stream().filter(m -> "I".equals(m.get("type"))).map(m -> (String) m.get("sbi_no")).collect(Collectors.toSet());

            List<Map<String, Object>> membersToDeactivate = existingMembers.stream()
                .filter(existing -> "A".equals(existing.get("status")) && 
                    (("S".equals(existing.get("type_of_person")) && !validUserIds.contains((String) existing.get("user_id"))) ||
                     ("I".equals(existing.get("type_of_person")) && !validSbiNos.contains((String) existing.get("sbi_no"))))
                ).collect(Collectors.toList());
            
            if (!membersToDeactivate.isEmpty()) {
                logger.info("Deactivating {} MGC members.", membersToDeactivate.size());
                StructDescriptor deactivateDesc = StructDescriptor.createDescriptor("TY_GRV_INFO_RE_MGC_DTL", oracleConn);
                ArrayDescriptor deactivateArrayDesc = ArrayDescriptor.createDescriptor("TB_GRV_INFO_RE_MGC_DTL", oracleConn);
                
                List<Struct> deactivateStructs = new ArrayList<>();
                for (Map<String, Object> member : membersToDeactivate) {
                    Object[] attributes = new Object[] {
                        member.get("rgc_mgc_dtl_seq_num"),
                        member.get("type_of_person"),
                        member.get("sbi_no"),
                        member.get("user_id"),
                        member.get("user_last_name"),
                        member.get("user_first_name"),
                        member.get("user_mid_name"),
                        member.get("user_suffix_name"),
                        "N", "Y",
                        member.get("start_date"),
                        new java.sql.Date(System.currentTimeMillis()), // Set end date to today
                        instNum,
                        "I" // Inactive status
                    };
                    deactivateStructs.add(new STRUCT(deactivateDesc, oracleConn, attributes));
                }
                
                ARRAY deactivateArray = new ARRAY(deactivateArrayDesc, oracleConn, deactivateStructs.toArray(new Struct[0]));
                try (CallableStatement stmt = conn.prepareCall("{call spkg_omnet_wrapper.sp_grv_info_mgc_dtl_update(?)}")) {
                    stmt.setArray(1, deactivateArray);
                    stmt.execute();
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving MGC members", e);
            throw new RuntimeException("Error saving MGC members: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves all referral details associated with a specific grievance.
     * This includes who the grievance was referred to, dates, and response information.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @return A list of maps, where each map contains the details of a single referral.
     */
    public List<Map<String, Object>> getReferralDetails(String commitNo, Integer seqNum) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (commitNo == null || seqNum == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_REFERRAL_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("grv_refl_dtl_seq_num", rs.getInt("grv_refl_dtl_seq_num"));
                        row.put("refr_date", rs.getDate("refr_date"));
                        row.put("refr_to", rs.getString("refr_to"));
                        row.put("refr_to_user_name", rs.getString("refr_to_user_name"));
                        row.put("refr_to_comt_type", rs.getString("refr_to_comt_type"));
                        row.put("refr_to_external_agency", rs.getString("refr_to_external_agency"));
                        row.put("refr_by_user_id", rs.getString("refr_by"));
                        row.put("refr_by_user_name", rs.getString("refr_by_user_name"));
                        row.put("refr_to_user_id", rs.getString("refr_to_user_id"));
                        row.put("due_date", rs.getDate("due_date"));
                        row.put("response_date", rs.getDate("response_date"));
                        row.put("confidential_flag", rs.getString("confidential_flag"));
                        row.put("completed_flag", rs.getString("completed_flag"));
                        row.put("information_reqst", rs.getString("information_reqst"));
                        row.put("response_received", rs.getString("response_received"));
                        result.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching referral details", e);
        }
        return result;
    }
    
    /**
     * Saves the referral details for a grievance.
     * The service first deletes all existing referrals for the grievance and then inserts the new set.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param referralData A list of maps, with each map representing a referral record to be saved.
     * @param userId The ID of the user saving the referrals.
     */
    public void saveReferralDetails(String commitNo, Integer seqNum, List<Map<String, Object>> referralData, String userId) {
        logger.info("Starting saveReferralDetails method for commitNo: {}, seqNum: {}", commitNo, seqNum);

        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null (commitNo, seqNum, userId)");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            logger.info("Database connection established");
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

            // First delete all existing referral records for this grievance
            try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM GRIEVANCE_REFERRAL_DTL WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ?")) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.executeUpdate();
            }

            // Get next sequence number for new records
            Integer nextSeqNo = getNextReferralSeqNo(conn);
            logger.info("Next referral sequence number: {}", nextSeqNo);

            List<Struct> referralStructs = new ArrayList<>();
            
            for (Map<String, Object> data : referralData) {
                // Skip empty or invalid rows
                if (!data.containsKey("refdate") || data.get("refdate") == null || 
                    String.valueOf(data.get("refdate")).isEmpty()) {
                    continue;
                }

                Integer seqNo = nextSeqNo++;
                if (seqNo == null) continue;

                String refTo = "N";
                String refToUserId = null;
                String refToComtType = null;
                
                if ("Y".equals(data.get("radioPerson"))) {
                    refTo = "P";
                    refToUserId = (String) data.get("assignedUserId");
                    logger.info("Saving referral to person with user ID: {}", refToUserId); 
                } else if ("Y".equals(data.get("radioCommittee"))) {
                    refTo = "C";
                    String committeeName = (String) data.get("nameCommittee");
                    if ("MGC".equalsIgnoreCase(committeeName)) {
                        refToComtType = "MGC";
                    } else if ("RGC/SME".equalsIgnoreCase(committeeName) || "RGC".equalsIgnoreCase(committeeName)) {
                        refToComtType = "RGC";
                    } else {
                        refToComtType = "RGC";
                        logger.warn("Unrecognized committee name '{}', defaulting to RGC", committeeName);
                    }
                }

               String referredByUserId = (String) data.get("referredByUserId");
                if (referredByUserId == null || referredByUserId.trim().isEmpty()) {
                    logger.warn("referredByUserId not found for a referral row, falling back to session user ID.");
                    referredByUserId = userId; 
                }
                
                Clob infoReqClob = createClob(conn, (String) data.get("informationRequested"));
                Clob respRecClob = createClob(conn, (String) data.get("responseToRequest"));

                Object[] fields = new Object[]{
                    seqNo,
                    commitNo,
                    seqNum,
                    convertToDate(data.get("refdate")),
                    refTo,
                    refToUserId,
                    data.get("nameCommittee"),
                    refToComtType,
                    data.get("externalAgencies"),
                    referredByUserId, // <-- Use the corrected user ID here
                    data.get("referredBy"),
                    convertToDate(data.get("due_date")),
                    convertToDate(data.get("response_date")),
                    "Y".equals(data.get("confidential_flag")) ? "Y" : "N",
                    "Y".equals(data.get("completed_flag")) ? "Y" : "N",
                    infoReqClob,
                    respRecClob,
                    "N"  // grv_addressed_flag
                };
                
                Struct struct = oracleConnection.createStruct("TY_GRV_INFO_REFERRAL_INSUPD", fields);
                referralStructs.add(struct);
            }

            if (!referralStructs.isEmpty()) {
                Array referralArray = oracleConnection.createOracleArray(
                    "TB_GRV_INFO_REFERRAL_INSUPD",
                    referralStructs.toArray(new Struct[0])
                );

                try (CallableStatement stmt = conn.prepareCall(
                    "{call spkg_omnet_wrapper.sp_grv_info_referral_insert(?)}")) {
                    stmt.setArray(1, referralArray);
                    stmt.execute();
                }
            }

        } catch (SQLException e) {
            logger.error("Error saving referral details", e);
            throw new RuntimeException("Error saving referral details: " + e.getMessage(), e);
        }
    }

    private Integer getNextReferralSeqNo(Connection conn) throws SQLException {
        logger.info("Getting next referral sequence number");
        String maxSeqQuery = "SELECT NVL(MAX(GRV_REFL_DTL_SEQ_NUM), 0) + 1 FROM GRIEVANCE_REFERRAL_DTL";
        
        try (PreparedStatement stmt = conn.prepareStatement(maxSeqQuery)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nextSeq = rs.getInt(1);
                    logger.debug("Next sequence number from table: {}", nextSeq);
                    return nextSeq;
                }
            }
        }
        logger.debug("No records found in table, returning default sequence number 1");
        return 1;
    }
    
    /**
     * Retrieves details of the Warden/Designee review for a grievance.
     * It fetches decision information, comments, and dates related to the warden's review.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param cmtyType The committee type, which should be 'WRD'.
     * @return A map containing the details of the Warden/Designee review.
     */
    public Map<String, Object> getWardenDetails(String commitNo, Integer seqNum, String cmtyType) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null || cmtyType == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String wardenSql = "{call DACS.SPKG_GRV_INFO.SP_GRV_WARDEN_DESIGNEE_QUERY(?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(wardenSql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.setString(3, cmtyType);
                stmt.registerOutParameter(4, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                    if (rs.next()) {
                    	result.put("warden_grvn_rew_rcpt_dt", rs.getDate("grvn_rew_rcpt_dt"));
                        result.put("warden_grvn_rew_ud_ind", rs.getString("grvn_rew_ud_ind"));
                        result.put("warden_grvn_decn_dt", rs.getDate("grvn_decn_dt"));
                        result.put("warden_grvn_rew_frwd_to_flg", rs.getString("grvn_rew_frwd_to_flg"));
                        result.put("warden_user_id_decision", rs.getString("user_id_decision"));
                        result.put("warden_user_id_decision_desc", rs.getString("user_id_decision_desc"));
                        result.put("warden_grvn_decn_comnt", rs.getString("grvn_decn_comnt"));
                    }
                }
            }
            
            String mainSql = "{call DACS.SPKG_GRV_INFO.SP_MAIN_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(mainSql)) {
                stmt.setInt(1, seqNum);
                stmt.setString(2, commitNo);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    if (rs.next()) {
                        result.put("emergency_grv_reqst_deny", rs.getString("emergency_grv_reqst_deny"));
                        result.put("emergency_grv_resn_for_deny", rs.getString("emergency_grv_resn_for_deny"));
                        result.put("emergency_frwd_to_igc", rs.getString("emergency_frwd_to_igc"));
                        result.put("emergency_comments", rs.getString("emergency_comments"));
                        //result.put("request_to_igc", rs.getString("request_to_igc"));
                        //result.put("req_to_igc_comments", rs.getString("req_to_igc_comments"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching Warden details", e);
        }
        return result;
    }
    
    private Integer getExistingWardenRewSeqNo(Connection conn, String commitNo, Integer seqNum) throws SQLException {
        logger.info("Checking for existing warden record for commitNo: {}, seqNum: {}", commitNo, seqNum);
        String sql = "SELECT GRVN_REW_SEQ_NO FROM GRIEVANCE_REW_DTL " +
                     "WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ? AND GRVN_REW_CMTY_TP = 'WRD'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            stmt.setInt(2, seqNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int seqNo = rs.getInt(1);
                    logger.info("Found existing warden record with GRVN_REW_SEQ_NO: {}", seqNo);
                    return seqNo;
                }
            }
        }
        logger.info("No existing warden record found.");
        return null;
    }

    /**
     * Saves the details of the Warden/Designee review for a grievance.
     * This service creates a new review record or updates an existing one.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param wardenData A map containing the review data to be saved.
     * @param userId The ID of the user (Warden/Designee) performing the action.
     */
    public void saveWardenDetails(String commitNo, Integer seqNum, Map<String, Object> wardenData, String userId) {
    	 synchronized (grvnRewDtlLock) {
        logger.info("Starting saveWardenDetails method for commitNo: {}, seqNum: {}", commitNo, seqNum);

        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null (commitNo, seqNum, userId)");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

            Integer grvnRewSeqNo = getExistingWardenRewSeqNo(conn, commitNo, seqNum);
            boolean isNew = (grvnRewSeqNo == null);

            if (isNew) {
                grvnRewSeqNo = getNextGrvnRewSeqNo(conn); // Changed to use the new method
                logger.info("No existing warden record found. Performing INSERT with new grvn_rew_seq_no: {}", grvnRewSeqNo);
            } else {
                logger.info("Existing warden record found with grvn_rew_seq_no: {}. Performing UPDATE.", grvnRewSeqNo);
            }

            Map<String, String> userDetails = getUserDetails(userId);
            if (userDetails.isEmpty()) {
                throw new IllegalArgumentException("User details not found for userId: " + userId);
            }

            Object[] wardenFields = new Object[]{
                commitNo,
                seqNum,
                grvnRewSeqNo,
                "WRD",
                convertToDate(wardenData.get("warden_grvn_rew_rcpt_dt")),
                wardenData.get("warden_grvn_req_desc"),
                null,
                null,
                userId,
                userDetails.get("user_last_name"),
                userDetails.get("user_first_name"),
                userDetails.get("user_mid_name"),
                userDetails.get("user_suffix_name"),
                null,
                convertToDate(wardenData.get("warden_grvn_decn_dt")),
                wardenData.get("warden_grvn_rew_ud_ind"),
                wardenData.get("warden_grvn_decn_comnt"),
                wardenData.get("warden_grvn_rew_frwd_to_flg"),
                wardenData.get("warden_requested_info"),
                wardenData.get("warden_user_id_decision"),
                wardenData.get("warden_user_id_decision_desc")
            };

            Struct wardenStruct = oracleConnection.createStruct("TY_GRV_INFO_WARDEN_DESIGNEE", wardenFields);
            Array wardenArray = oracleConnection.createOracleArray("TB_GRV_INFO_WARDEN_DESIGNEE", new Struct[]{wardenStruct});

            String procedureName = isNew ? "spkg_omnet_wrapper.sp_grv_info_warden_designee_insert" 
                                         : "spkg_omnet_wrapper.sp_grv_info_warden_designee_update";

            try (CallableStatement stmt = conn.prepareCall("{call " + procedureName + "(?)}")) {
                stmt.setArray(1, wardenArray);
                stmt.execute();
            }
        } catch (SQLException e) {
            logger.error("Error saving warden details", e);
            throw new RuntimeException("Error saving warden details: " + e.getMessage(), e);
        }
    }
    }

    private Integer getNextGrvnRewSeqNo(Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT NVL(MAX(GRVN_REW_SEQ_NO), 0) + 1 FROM GRIEVANCE_REW_DTL")) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Unable to generate sequence number");
    }

    
    /**
     * Retrieves the appeal details for a specific grievance.
     * This includes the appeal date, description, and forwarding flags.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @return A map containing the appeal details, if an appeal exists.
     */
    public Map<String, Object> getAppealDetails(String commitNo, Integer seqNum) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_GRV_APPEAL_QUERY(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.registerOutParameter(3, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                    if (rs.next()) {
                        result.put("grvn_appeal_dt", rs.getDate("grvn_appeal_dt"));
                        result.put("grvn_appeal_desc", rs.getString("grvn_appeal_desc"));
                        result.put("frwd_to_bgo_flag", rs.getString("frwd_to_bgo_flag"));
                        result.put("frwd_to_bc_flag", rs.getString("frwd_to_bc_flag"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching appeal details", e);
        }
        return result;
    }
    
 
    /**
     * Saves the appeal details for a grievance.
     * This service will insert a new appeal record or update an existing one.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param appealData A map containing the appeal information to be saved.
     * @param userId The ID of the user saving the appeal.
     */
    public void saveAppealDetails(String commitNo, Integer seqNum, Map<String, Object> appealData, String userId) {
        logger.info("Starting saveAppealDetails method for commitNo: {}, seqNum: {}", commitNo, seqNum);

        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null (commitNo, seqNum, userId)");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            logger.info("Database connection established");
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

            Integer grvnAppealSeqNo = getExistingAppealSeqNo(conn, commitNo, seqNum);
            boolean isNew = (grvnAppealSeqNo == null);

            if (isNew) {
                grvnAppealSeqNo = getMaxAppealSeqNo(conn, commitNo, seqNum);
                logger.info("No existing appeal record found. Performing INSERT with new grvn_appeal_seq_no: {}", grvnAppealSeqNo);
            } else {
                logger.info("Existing appeal record found with grvn_appeal_seq_no: {}. Performing UPDATE.", grvnAppealSeqNo);
            }

            Object[] appealFields = new Object[]{
                commitNo,
                seqNum,
                grvnAppealSeqNo,
                convertToDate(appealData.get("grvn_appeal_dt")),
                appealData.get("grvn_appeal_desc"),
                appealData.get("grvn_appeal_remdy_req"),
                "Y".equals(appealData.get("frwd_to_bgo_flag")) ? "Y" : "N",
                "Y".equals(appealData.get("frwd_to_bc_flag")) ? "Y" : "N"
            };

            logger.debug("Creating struct with fields: {}", Arrays.toString(appealFields));

            Struct appealStruct = oracleConnection.createStruct("TY_GRV_INFO_APPEAL", appealFields);
            Array appealArray = oracleConnection.createOracleArray("TB_GRV_INFO_APPEAL", new Struct[]{appealStruct});

            String procedureName = isNew ? "spkg_omnet_wrapper.sp_grv_info_appeal_insert" 
                                       : "spkg_omnet_wrapper.sp_grv_info_appeal_update";

            try (CallableStatement stmt = conn.prepareCall("{call " + procedureName + "(?)}")) {
                stmt.setArray(1, appealArray);
                logger.debug("Executing stored procedure: {}", procedureName);
                stmt.execute();
                logger.info("Appeal details saved successfully using procedure: {}", procedureName);
            }
        } catch (SQLException e) {
            logger.error("Error saving appeal details", e);
            throw new RuntimeException("Error saving appeal details: " + e.getMessage(), e);
        }
    }
    
    /**
     * Fetches the sequence number for an existing appeal record.
     * This is used to determine whether to perform an insert or an update operation.
     * @param conn The database connection.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @return The appeal sequence number if a record exists, otherwise null.
     */
    public Integer getExistingAppealSeqNo(Connection conn, String commitNo, Integer seqNum) throws SQLException {
        logger.info("Checking for existing appeal record for commitNo: {}, seqNum: {}", commitNo, seqNum);
        String sql = "SELECT GRVN_APPEAL_SEQ_NO FROM GRIEVANCE_APPEAL_DTL " +
                     "WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            stmt.setInt(2, seqNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int seqNo = rs.getInt(1);
                    logger.info("Found existing appeal record with GRVN_APPEAL_SEQ_NO: {}", seqNo);
                    return seqNo;
                }
            }
        }
        logger.info("No existing appeal record found.");
        return null;
    }

    private Integer getMaxAppealSeqNo(Connection conn, String commitNo, Integer seqNum) throws SQLException {
        logger.info("Starting getMaxAppealSeqNo method for commitNo: {}, seqNum: {}", commitNo, seqNum);
        String sql = "SELECT NVL(MAX(GRVN_APPEAL_SEQ_NO), 0) + 1 " +
                     "FROM GRIEVANCE_APPEAL_DTL " +
                     "WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ?";
        logger.info("SQL query to get next sequence number: {}", sql);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            stmt.setInt(2, seqNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nextSeqNo = rs.getInt(1);
                    logger.info("Retrieved next sequence number: {}", nextSeqNo);
                    return nextSeqNo;
                }
            }
        }
        logger.info("No existing sequence numbers found for this grievance, returning default sequence 1");
        return 1;
    }

    /**
     * Retrieves details of the BGO (Bureau of Grievance and Ombudsman) review.
     * This service fetches decision data, comments, and dates for the BGO review stage.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param cmtyType The committee type, which should be 'BGO'.
     * @return A map containing the details of the BGO review.
     */
    public Map<String, Object> getBGODetails(String commitNo, Integer seqNum, String cmtyType) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null || cmtyType == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_GRV_BGO_QUERY(?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.setString(3, cmtyType);
                stmt.registerOutParameter(4, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                    if (rs.next()) {
                    	result.put("bgo_grvn_rew_rcpt_dt", rs.getDate("grvn_rew_rcpt_dt"));
                        result.put("bgo_grvn_rew_ud_ind", rs.getString("grvn_rew_ud_ind"));
                        result.put("bgo_grvn_decn_dt", rs.getDate("grvn_decn_dt"));
                        result.put("bgo_grvn_rew_frwd_to_flg", rs.getString("grvn_rew_frwd_to_flg"));
                        result.put("bgo_user_id_decision", rs.getString("user_id_decision"));
                        result.put("bgo_grvn_decn_comnt", rs.getString("grvn_decn_comnt"));
                        result.put("bgo_grvn_rew_frwd_dt", rs.getDate("grvn_rew_frwd_dt"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching BGO details", e);
        }
        return result;
    }
    
    /**
     * Saves the details of a BGO (Bureau of Grievance and Ombudsman) review.
     * The service handles both the creation of a new BGO record and updates to an existing one.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param bgoData A map containing the BGO review details.
     * @param userId The ID of the user saving the information.
     */
    public void saveBGODetails(String commitNo, Integer seqNum, Map<String, Object> bgoData, String userId) {
    	 synchronized (grvnRewDtlLock) {
        logger.info("Starting saveBGODetails method for commitNo: {}, seqNum: {}", commitNo, seqNum);

        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null (commitNo, seqNum, userId)");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

            Integer grvnRewSeqNo = getExistingBGORewSeqNo(conn, commitNo, seqNum);
            boolean isNew = (grvnRewSeqNo == null);

            if (isNew) {
                grvnRewSeqNo = getNextGrvnRewSeqNo(conn); // Changed to use the new method
                logger.info("No existing BGO record found. Performing INSERT with new grvn_rew_seq_no: {}", grvnRewSeqNo);
            } else {
                logger.info("Existing BGO record found with grvn_rew_seq_no: {}. Performing UPDATE.", grvnRewSeqNo);
            }

            Map<String, String> userDetails = getUserDetails(userId);
            if (userDetails.isEmpty()) {
                throw new IllegalArgumentException("User details not found for userId: " + userId);
            }

            Object[] bgoFields = new Object[]{
                commitNo,
                seqNum,
                grvnRewSeqNo,
                "BGO",
                convertToDate(bgoData.get("bgo_grvn_rew_rcpt_dt")),
                bgoData.get("bgo_grvn_req_desc"),
                bgoData.get("bgo_grvn_refr_to_pc_ind"),
                convertToDate(bgoData.get("bgo_grvn_refr_due_dt")),
                userId,
                userDetails.get("user_last_name"),
                userDetails.get("user_first_name"),
                userDetails.get("user_mid_name"),
                userDetails.get("user_suffix_name"),
                bgoData.get("bgo_grvn_refr_cmty"),
                convertToDate(bgoData.get("bgo_grvn_decn_dt")),
                bgoData.get("bgo_grvn_rew_ud_ind"),
                bgoData.get("bgo_grvn_decn_comnt"),
                bgoData.get("bgo_grvn_rew_frwd_to_flg"),
                convertToDate(bgoData.get("bgo_grvn_rew_frwd_dt")),
                bgoData.get("bgo_requested_info"),
                bgoData.get("bgo_user_id_decision"),
                bgoData.get("bgo_user_id_decision_desc")
            };

            Struct bgoStruct = oracleConnection.createStruct("TY_GRV_INFO_BGO_INSUPD", bgoFields);
            Array bgoArray = oracleConnection.createOracleArray("TB_GRV_INFO_BGO_INSUPD", new Struct[]{bgoStruct});

            String procedureName = isNew ? "spkg_omnet_wrapper.sp_grv_info_bgo_insert" 
                                       : "spkg_omnet_wrapper.sp_grv_info_bgo_update";

            try (CallableStatement stmt = conn.prepareCall("{call " + procedureName + "(?)}")) {
                stmt.setArray(1, bgoArray);
                stmt.execute();
            }
        } catch (SQLException e) {
            logger.error("Error saving BGO details", e);
            throw new RuntimeException("Error saving BGO details: " + e.getMessage(), e);
        }
    }
    }

    private Integer getExistingBGORewSeqNo(Connection conn, String commitNo, Integer seqNum) throws SQLException {
        logger.info("Checking for existing BGO record for commitNo: {}, seqNum: {}", commitNo, seqNum);
        String sql = "SELECT GRVN_REW_SEQ_NO FROM GRIEVANCE_REW_DTL " +
                     "WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ? AND GRVN_REW_CMTY_TP = 'BGO'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            stmt.setInt(2, seqNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int seqNo = rs.getInt(1);
                    logger.info("Found existing BGO record with GRVN_REW_SEQ_NO: {}", seqNo);
                    return seqNo;
                }
            }
        }
        logger.info("No existing BGO record found.");
        return null;
    }
    
    /**
     * Retrieves details of the Bureau Chief review for a grievance.
     * This fetches final decision information, comments, and relevant dates.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param cmtyType The committee type, which should be 'BUR'.
     * @return A map containing the Bureau Chief's review details.
     */
    public Map<String, Object> getBureauChiefDetails(String commitNo, Integer seqNum, String cmtyType) {
        Map<String, Object> result = new HashMap<>();
        if (commitNo == null || seqNum == null || cmtyType == null) return result;
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call DACS.SPKG_GRV_INFO.SP_GRV_BUREAU_CHIEF_QUERY(?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, commitNo);
                stmt.setInt(2, seqNum);
                stmt.setString(3, cmtyType);
                stmt.registerOutParameter(4, java.sql.Types.REF_CURSOR);
                stmt.execute();
                
                try (ResultSet rs = (ResultSet) stmt.getObject(4)) {
                    if (rs.next()) {
                    	result.put("bur_grvn_rew_rcpt_dt", rs.getDate("grvn_rew_rcpt_dt"));
                        result.put("bur_grvn_rew_ud_ind", rs.getString("grvn_rew_ud_ind"));
                        result.put("bur_grvn_decn_dt", rs.getDate("grvn_decn_dt"));
                        result.put("bur_grvn_decn_comnt", rs.getString("grvn_decn_comnt"));
                        result.put("bur_grvn_rew_frwd_to_flg", rs.getString("grvn_rew_frwd_to_flg"));
                        result.put("bur_grvn_rew_frwd_dt", rs.getDate("grvn_rew_frwd_dt"));
                        result.put("bur_user_id_decision", rs.getString("user_id_decision"));
                        result.put("bur_user_id_decision_desc", rs.getString("user_id_decision_desc"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching Bureau Chief details", e);
        }
        return result;
    }
    
    /**
     * Saves the details of a Bureau Chief review for a grievance.
     * It either inserts a new review record or updates the existing one.
     * @param commitNo The offender's commit number.
     * @param seqNum The grievance sequence number.
     * @param bureauData A map containing the review details to be saved.
     * @param userId The ID of the user (Bureau Chief) performing the save.
     */
    public void saveBureauChiefDetails(String commitNo, Integer seqNum, Map<String, Object> bureauData, String userId) {
    	 synchronized (grvnRewDtlLock) {
        logger.info("Starting saveBureauChiefDetails method for commitNo: {}, seqNum: {}", commitNo, seqNum);

        if (commitNo == null || seqNum == null || userId == null) {
            logger.error("Required parameters cannot be null (commitNo, seqNum, userId)");
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConnection = conn.unwrap(OracleConnection.class);

            Integer grvnRewSeqNo = getExistingBureauChiefRewSeqNo(conn, commitNo, seqNum);
            boolean isNew = (grvnRewSeqNo == null);

            if (isNew) {
                grvnRewSeqNo = getNextGrvnRewSeqNo(conn); // Changed to use the new method
                logger.info("No existing Bureau Chief record found. Performing INSERT with new grvn_rew_seq_no: {}", grvnRewSeqNo);
            } else {
                logger.info("Existing Bureau Chief record found with grvn_rew_seq_no: {}. Performing UPDATE.", grvnRewSeqNo);
            }

            Map<String, String> userDetails = getUserDetails(userId);
            if (userDetails.isEmpty()) {
                throw new IllegalArgumentException("User details not found for userId: " + userId);
            }

            Object[] bureauFields = new Object[]{
                commitNo,
                seqNum,
                grvnRewSeqNo,
                "BUR",
                convertToDate(bureauData.get("bur_grvn_decn_dt")),
                bureauData.get("bur_grvn_rew_ud_ind"),
                bureauData.get("bur_grvn_decn_comnt"),
                bureauData.get("bur_grvn_rew_frwd_to_flg"),
                convertToDate(bureauData.get("bur_grvn_rew_rcpt_dt")),
                convertToDate(bureauData.get("bur_grvn_rew_frwd_dt")),
                bureauData.get("bur_user_id_decision"),
                bureauData.get("bur_user_id_decision_desc")
            };

            Struct bureauStruct = oracleConnection.createStruct("TY_GRV_INFO_BUREAU_CHIEF_INSUPD", bureauFields);
            Array bureauArray = oracleConnection.createOracleArray("TB_GRV_INFO_BUREAU_CHIEF_INSUPD", new Struct[]{bureauStruct});

            String procedureName = isNew ? "spkg_omnet_wrapper.sp_grv_info_bureau_chief_insert" 
                                       : "spkg_omnet_wrapper.sp_grv_info_bureau_chief_update";

            try (CallableStatement stmt = conn.prepareCall("{call " + procedureName + "(?)}")) {
                stmt.setArray(1, bureauArray);
                stmt.execute();
            }
        } catch (SQLException e) {
            logger.error("Error saving Bureau Chief details", e);
            throw new RuntimeException("Error saving Bureau Chief details: " + e.getMessage(), e);
        }
    }
    }

    private Integer getExistingBureauChiefRewSeqNo(Connection conn, String commitNo, Integer seqNum) throws SQLException {
        logger.info("Checking for existing Bureau Chief record for commitNo: {}, seqNum: {}", commitNo, seqNum);
        String sql = "SELECT GRVN_REW_SEQ_NO FROM GRIEVANCE_REW_DTL " +
                     "WHERE COMMIT_NO = ? AND GRIEVANCE_SEQ_NUM = ? AND GRVN_REW_CMTY_TP = 'BUR'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commitNo);
            stmt.setInt(2, seqNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int seqNo = rs.getInt(1);
                    logger.info("Found existing Bureau Chief record with GRVN_REW_SEQ_NO: {}", seqNo);
                    return seqNo;
                }
            }
        }
        logger.info("No existing Bureau Chief record found.");
        return null;
    }
    
}    