package com.omnet.cnt.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Repository.DisciplinaryInfoRepository;
import com.omnet.cnt.Model.DisciplinaryInfo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalDate;



import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

@Service
public class DisciplinaryInfoService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DisciplinaryInfoRepository disciplinaryInfoRepository;

    public List<Map<String, Object>> getDisciplinaryInfo(String P_COMMIT_NO, String P_D_REPORT_NUM) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{call Spkg_Dis_Info.QUERY_DISCIPLINE(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, Types.REF_CURSOR);
                stmt.setString(2, P_COMMIT_NO);
                stmt.setString(3, P_D_REPORT_NUM);
                stmt.execute();

                try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    while (rs.next()) {
                        Map<String, Object> resultMap = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            resultMap.put(metaData.getColumnName(i), rs.getObject(i));
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

    public List<DisciplinaryInfo> getIncidentDropDown(String commitNo) {
        List<String> rawIds = disciplinaryInfoRepository.getIncidentIdsByCommitNo(commitNo);

        return rawIds.stream().map(idStr -> {
            DisciplinaryInfo info = new DisciplinaryInfo();
            info.setIncidentSeqNum(Long.parseLong(idStr));
            return info;
        }).toList();
    }
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDisciplinaryReportIds(String commitNo) {
        String sql = "SELECT D_REPORT_NUM FROM discipline WHERE commit_no = ?";
        return jdbcTemplate.queryForList(sql, commitNo);
    }
    
    public List<Map<String, Object>> getDisciplinaryTypes() {
        String sql = "SELECT DISTINCT dspn_tp_cd, " +
                     "DECODE(dspn_tp_cd, 'CLS1','Class 1', 'CLS2','Class 2', 'SUMM','Summary') AS DESCRIPTION " +
                     "FROM discipline " +
                     "WHERE dspn_tp_cd IS NOT NULL " +
                     "ORDER BY DESCRIPTION";
        
        return jdbcTemplate.queryForList(sql);
    }
    
    public Map<String, Object> getDisciplinaryFormDetails(String commitNo, long dReportNum) {
        String sql = "SELECT " +
                     "INCIDENT_SEQ_NUM AS \"groupNo\", " +
                     "TO_CHAR(D_REPORT_DATE, 'YYYY-MM-DD') AS \"dateOfOccurrence\", " +
                     "D_REPORT_TIME AS \"timeOfOccurrence\", " +
                     "DREPORT_STATUS AS \"status\", " +
                     "REASON_FOR_EXCLUSION AS \"reasonForNullification\", " +
                     "DSPN_VIOL_SMRY_DESC AS \"dspnViolSmryDesc\" " +
                     "FROM discipline " +
                     "WHERE commit_no = ? AND d_report_num = ?";

        return jdbcTemplate.queryForMap(sql, commitNo, dReportNum);
    }

    
    public Map<String, Object> getViolationSummary(String sbiNo, Date idt_start_dt, Date idt_end_dt) {
        String sql = "{call sp_get_dspn_tp_summary(?, ?, ?, ?, ?, ?)}";

        Map<String, Object> result = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, sbiNo);
            stmt.setDate(2, idt_start_dt);
            stmt.setDate(3, idt_end_dt);
            stmt.registerOutParameter(4, Types.INTEGER); // Class 1 count
            stmt.registerOutParameter(5, Types.INTEGER); // Class 2 count
            stmt.registerOutParameter(6, Types.INTEGER); // Summary count

            stmt.execute();

            result.put("class1", stmt.getInt(4));
            result.put("class2", stmt.getInt(5));
            result.put("summary", stmt.getInt(6));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public List<Map<String, Object>> getDisciplineCodes() {
        String sql = "SELECT code_desc, level_of_code, class_type " +
                     "FROM code_of_conduct_rt " +
                     "WHERE status = 'A' " +
                     "ORDER BY code_desc";
                     
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<Map<String, Object>> getDisciplineCodesByCommitAndReport(String commitNo, String reportNum) {
        String sql = "SELECT * FROM Discipline_Code WHERE COMMIT_NO = ? AND D_REPORT_NUM = ?";
        return jdbcTemplate.queryForList(sql, commitNo, reportNum);
    }
    
    
    public List<DisciplinaryInfo> getWitnessesByCommitAndReport(String commitNo, Long reportNo) {
        return jdbcTemplate.execute(
            (Connection con) -> {
                CallableStatement cs = con.prepareCall("{ call SPKG_DIS_INFO.WITNESS_DET_QUERY(?, ?, ?) }");
                cs.registerOutParameter(1, OracleTypes.CURSOR);         
                cs.setString(2, commitNo);                             
                cs.setLong(3, reportNo);                                
                return cs;
            },
            (CallableStatementCallback<List<DisciplinaryInfo>>) cs -> {
                cs.execute();
                ResultSet rs = (ResultSet) cs.getObject(1);
                List<DisciplinaryInfo> witnesses = new ArrayList<>();
                while (rs.next()) {
                    DisciplinaryInfo info = new DisciplinaryInfo();
                    info.setLastName(rs.getString("WITNESS_LNAME"));
                    info.setFirstName(rs.getString("WITNESS_FNAME"));
                    info.setMi(rs.getString("WITNESS_MNAME"));
                    info.setSuffix(rs.getString("WITNESS_SNAME"));

                    
                    witnesses.add(info);
                }
                return witnesses;
            }
        );
    }
    
    

    
    public List<Map<String, Object>> getAssaultTypeOptions() {
        String sql = "SELECT ASSAULT_TYPE_CODE, ASSAULT_TYPE_DESC " +
                     "FROM ASSAULT_TYPE_RT " +
                     "WHERE status = 'A' " +
                     "ORDER BY ASSAULT_TYPE_DESC";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getWeaponTypeOptions() {
        String sql = "SELECT WEAPON_TYPE_CODE, WEAPON_DESC FROM WEAPON_TYPE_RT WHERE STATUS = 'A' ORDER BY WEAPON_DESC";
        return jdbcTemplate.queryForList(sql);
    }

 
    public Map<String, Object> getEnteredByUserDetails(String userId) {
        String sql = "SELECT USER_LAST_NAME, USER_FIRST_NAME, USER_MID_NAME, USER_SUFFIX_NAME " +
                     "FROM Omnet_Users WHERE USER_ID = ?";
        try {
            return jdbcTemplate.queryForMap(sql, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
    
    
    /*public List<Map<String, Object>> getEnteredByTitles() {
        String sql = "SELECT TITLE_DESC, TITLE FROM TITLE_RT WHERE STATUS = 'A' ORDER BY TITLE_DESC";
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }*/
    
    
   /* public List<Map<String, Object>> getPreHearingTitleOptions() {
        String sql = "SELECT TITLE_DESC, TITLE FROM TITLE_RT WHERE STATUS = 'A' ORDER BY TITLE_DESC";
        return jdbcTemplate.queryForList(sql);
    }*/
    
    public List<Map<String, Object>> getDecisionDropdownOptions() {
        String sql = "SELECT major_tp, sub_tp_desc FROM type_mst WHERE major_tp = 'DISPDESC' ORDER BY sub_tp_desc";
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<Map<String, Object>> getDispositionOptions() {
        String sql = "SELECT DISPOSITION_DESC, DISPOSITION_CODE FROM Disposition_rt WHERE STATUS = 'A' ORDER BY DISPOSITION_DESC";
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<Map<String, String>> getPreHearingRefToDropdownOptions() {
        String sql = "SELECT TITLE_DESC, TITLE FROM TITLE_RT WHERE STATUS = 'A' ORDER BY TITLE_DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, String> map = new HashMap<>();
            map.put("preHearingRefTo", rs.getString("TITLE"));
            map.put("titleDesc", rs.getString("TITLE_DESC"));
            return map;
        });
    }

    
    public List<Map<String, Object>> getTitleDropdown() {
        String sql = "SELECT TITLE, TITLE_DESC FROM TITLE_RT WHERE STATUS = 'A' ORDER BY TITLE_DESC";
        return jdbcTemplate.queryForList(sql);
    }
    
    public List<Map<String, Object>> getOmnetUsers() {
        String sql = "SELECT USER_FIRST_NAME, USER_LAST_NAME, USER_MID_NAME, USER_SUFFIX_NAME " +
                     "FROM OMNET_USERS ORDER BY USER_FIRST_NAME";
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    @PersistenceContext
    private EntityManager entityManager;

    public String getGroupSequenceNumber(String incidentSeqNum) {
        String sql = "SELECT SPKG_DIS_INFO.sf_get_grp_seq_no(:incident_seq_num) FROM dual";
        var query = entityManager.createNativeQuery(sql);
        query.setParameter("incident_seq_num", incidentSeqNum);

        Object result = query.getSingleResult();
        return result != null ? result.toString() : null;
    }

    @Autowired
    private DataSource dataSource1;

    public String getInstitutionNumber(String incidentSeqNum) {
        String sql = "{ ? = call SPKG_DIS_INFO.SF_GET_INST_NUM(?) }";
        String institutionNumber = null;

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, incidentSeqNum);
            stmt.execute();

            institutionNumber = stmt.getString(1);
            System.out.println("[Institution] Resolved: " + institutionNumber);

        } catch (SQLException e) {
            e.printStackTrace(); // Optionally use a logger
        }

        return institutionNumber;
    }
    
    public String generateReportNumber(String incidentSeqNum) {
        String institutionNumber = getInstitutionNumber(incidentSeqNum);
        if (institutionNumber == null || institutionNumber.isBlank()) {
            return null;
        }

        String sequenceKey = switch (institutionNumber) {
            case "01" -> "0027_01";
            case "02" -> "0027_02";
            case "03" -> "0027_03";
            case "10" -> "0027_10";
            // Add more cases if needed
            default -> null;
        };

        if (sequenceKey == null) {
            return null;
        }

        String sql = "{ ? = call SF_GEN_SEQ_MDOC(?) }";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, sequenceKey);
            stmt.execute();

            String dReportNum = stmt.getString(1);
            System.out.println("[Generated D_REPORT_NUM] → " + dReportNum);
            return dReportNum;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveDisciplinaryInfo(List<DisciplinaryInfo> infoList) {
        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Struct[] structs = new Struct[infoList.size()];

            for (int i = 0; i < infoList.size(); i++) {
                DisciplinaryInfo info = infoList.get(i);

                Object[] attributes = new Object[] {
                    info.getIncidentSeqNum(),
                    info.getReportNumber(),
                    info.getCommitNo(),
                    
                    toSqlDate(info.getDateOfOccurrence()),
                    info.getTimeOfOccurrence(),
                    info.getLocationOfOccurrence(),
                    info.getStatus(),
                    info.getReasonForNullification(),
                    info.getHousingUnit(),
                    info.getClass1Violation(),
                    info.getClass2Violation(),
                    info.getSummaryViolation(),
                    info.getComments(),
                    info.getDspnViolSmryDesc(),

                    info.getLastName(),
                    info.getFirstName(),
                    info.getMi(),
                    info.getSuffix(),
                    info.getStaffAssaultFlag(),
                    info.getStaffInjuriesFlag(),
                    info.getIndustrialAccidentFlag(),
                    info.getInmateAssaultFlag(),
                    info.getResultOfIncident(),
                    info.getResultOfSearch(),
                    info.getSexRelatedFlag(),
                    info.getStgRelatedFlag(),
                    info.getAssaultTypeCode(),

                    info.getEvidenceDisposition(),
                    toSqlDate(info.getDispositionDate()),
                    info.getActionTaken(),
                    info.getWeaponNote(),

                    toSqlDate(info.getEnteredByDate()),
                    info.getEnteredByTime(),
                    info.getEnteredByTitle(),
                    toSqlDate(info.getReferredToDate()),
                    info.getReferredToTime(),
                    info.getReferredToComments(),
                    info.getReferredToTitle(),

                    toSqlDate(info.getDecisionDate()),
                    info.getDecisionTime(),
                    info.getDecisionAddComments(),
                    info.getDecisionCode(),
                    info.getDecisionTitle(),

                    toSqlDate(info.getOffenderDispositionDate()),
                    info.getOffenderDispositionTime(),
                    info.getOffenderDispositionReason(),
                    info.getOffenderDispositionCellFlag(),
                    info.getDispositionCode(),

                    toSqlDate(info.getPreHearingRefDate()),
                    info.getPreHearingRefTime(),
                    info.getPreHearingRefComments(),
                    info.getPreHearingRefTitle(),

                    toSqlDate(info.getApprovedByDate()),
                    info.getApprovedByStatus(),
                    info.getApprovedByFollowUp(),
                    info.getApprovedByAddComments(),
                    info.getApprovedByTitle(),

                    toSqlDate(info.getDeliveredByDate()),
                    info.getDeliveredByTime(),
                    info.getDeliveredByAddComments(),
                    info.getDeliveredByTitle(),

                    info.getUserIdEnteredBy(),
                    info.getUserIdReferredTo(),
                    info.getUserIdPreHearing(),
                    info.getUserIdApproved(),
                    info.getUserIdDeliveredBy()
                };

                Struct struct = oracleConn.createStruct("TY_DIS_INFO_INS", attributes);
                structs[i] = struct;
            }

            Array array = oracleConn.createOracleArray("TB_DIS_INFO_INS", structs);

            try (CallableStatement stmt = conn.prepareCall("{ call Spkg_Omnet_Wrapper.Sp_Dis_Info_INSERT_DISCIPLINE(?) }")) {
                stmt.setArray(1, array);
                stmt.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving disciplinary info", e);
        }
    }

    private java.sql.Date toSqlDate(LocalDate localDate) {
        return (localDate != null) ? java.sql.Date.valueOf(localDate) : null;
    }

    public void saveWitnessDetails(List<DisciplinaryInfo> witnessList) {
        try (Connection conn = dataSource.getConnection()) {
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Struct[] structs = new Struct[witnessList.size()];

            for (int i = 0; i < witnessList.size(); i++) {
                DisciplinaryInfo info = witnessList.get(i);
                Object[] attributes = new Object[] {
                    info.getCommitNo(),
                    info.getReportNumber(),
                    info.getWitnessSeqNum(),
                    info.getWitnessFirstName(),
                    info.getWitnessLastName(),
                    info.getWitnessMiddleName(),
                    info.getWitnessSuffix(),
                    info.getDspnWitnessDelFlag() != null ? info.getDspnWitnessDelFlag() : "N"
                };

                structs[i] = oracleConn.createStruct("TY_WITNESS_DET", attributes);
            }

            Array array = oracleConn.createOracleArray("TB_WITNESS_DET", structs);
            try (CallableStatement stmt = conn.prepareCall("{ call SPKG_DIS_INFO.WITNESS_DET_INSERT(?) }")) {
                stmt.setArray(1, array);
                stmt.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving witness details", e);
        }
    }

    
    

}
