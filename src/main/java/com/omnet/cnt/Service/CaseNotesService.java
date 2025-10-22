/*
Document   : Case Notes Service
Author     : Jamal Abraar
last update: 03/06/2024
*/

package com.omnet.cnt.Service;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.omnet.cnt.Model.Amendment;
import com.omnet.cnt.Model.ContactInformationInsert;
import com.omnet.cnt.Model.ContactPersonInsert;
import com.omnet.cnt.Model.ISCDateApprovedCaseNotes;
import com.omnet.cnt.Repository.ContactTpMstRepository;
import com.omnet.cnt.Repository.ReasonMstRepository;
import com.omnet.cnt.Repository.RelationshipRtRepository;
import com.omnet.cnt.classes.CaseNotes;
import com.omnet.cnt.classes.ReNew;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Service
public class CaseNotesService {
    private static final Logger logger = LoggerFactory.getLogger(CaseNotesService.class);

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    private ContactTpMstRepository contactTpMstRepository;
    
    @Autowired
    private RelationshipRtRepository relationshipRepository;
    
    @Autowired
    private ReasonMstRepository reasonMstRepository;
    
    public CaseNotesService(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    private Integer getNextContSeqNum() {
        return jdbcTemplate.queryForObject("SELECT CCSH_SEQ.NEXTVAL FROM dual", Integer.class);
    }
    
    public Integer getNContSeqNumforCI() {
        return jdbcTemplate.queryForObject("select max(CSMN_CONT_SEQ_NO) + 1  from CSMN_CONTACT_SPRV_HDR", Integer.class);
    }
    
    public String UserNameLFMS(String RCODE) {
        return jdbcTemplate.queryForObject("SELECT RELATIONSHIP_DESC FROM RELATIONSHIP_RT WHERE RELATIONSHIP_CODE=:RCODE", String.class);
    }
    
    public Integer getNContSeqNumforContactPerson() {
        return jdbcTemplate.queryForObject("select max(CSMN_PRSN_SEQ_NO) + 1  from CSMN_CONTACT_PRSN_DTL", Integer.class);
    }
    
    @SuppressWarnings("deprecation")
	private List<Integer> getNextPrsnSeqNums(int count) {
        return jdbcTemplate.query(
            "SELECT CCPD_SEQ.NEXTVAL FROM dual CONNECT BY LEVEL <= ?",
            new Object[]{count},
            (rs, rowNum) -> rs.getInt(1)
        );
    }
    
    @Transactional
    public ResponseEntity<Map<String, String>> performCombinedTransaction(List<ReNew> reNewList) {
        try {
            if (reNewList.isEmpty()) {
                logger.info("Empty list provided, skipping processing.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("status", "Empty list provided"));
            }

            logger.info("Processing ReNew objects with input list size: {}", reNewList.size());

            // Generate a common contSeqNum for all ReNew objects
            Integer commonContSeqNum = getNextContSeqNum();
            for (ReNew reNew : reNewList) {
                reNew.setContSeqNum(commonContSeqNum);
            }

            // Insert the main ReNew objects
            ResponseEntity<Map<String, String>> mainInsertResult = callSpNewInsertObj(reNewList);
            if (!HttpStatus.OK.equals(mainInsertResult.getStatusCode())) {
                throw new SQLException("Error inserting Case Note, rolling back transaction.");
            }

            // Insert the contacted persons
            ResponseEntity<Map<String, String>> contactPersonResult = insertContactPersons(reNewList);
            if (!HttpStatus.OK.equals(contactPersonResult.getStatusCode())) {
                throw new SQLException("Error inserting Contacted Persons, rolling back transaction.");
            }

            return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
        } catch (Exception e) {
            logger.error("Error during processing: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }

    //Method to call procedure sp_new_insert_obj
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Map<String, String>> callSpNewInsertObj(List<ReNew> reNewList) throws SQLException {
        try {
            logger.info("Calling stored procedure sp_new_insert_obj with input list size: {}", reNewList.size());

            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("spkg_csm_nosc")
                    .withProcedureName("sp_new_insert_obj")
                    .declareParameters(new SqlParameter("itb_csmn_contact_hdr", OracleTypes.ARRAY, "TB_CSMN_CONTACT_HDR"));

            try (Connection conn = dataSource.getConnection()) {
                OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
                ARRAY sqlArray = createSqlArray(reNewList, oracleConn);
                Map<String, Object> inParams = Map.of("itb_csmn_contact_hdr", sqlArray);
                logger.info("Input parameters for stored procedure sp_new_insert_obj: {}", inParams);

                simpleJdbcCall.execute(inParams);
            }
            return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
        } catch (Exception e) {
            logger.error("Error while calling stored procedure sp_new_insert_obj: ", e);
            throw new SQLException("Error inserting main ReNew objects ");
        }
    }

    // Struct construction for method insert case notes
    @SuppressWarnings("deprecation")
    private ARRAY createSqlArray(List<ReNew> reNewList, OracleConnection oracleConn) throws SQLException {
        StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_CSMN_CONTACT_HDR", oracleConn);
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_CSMN_CONTACT_HDR", oracleConn);

        STRUCT[] structs = new STRUCT[reNewList.size()];

        for (int i = 0; i < reNewList.size(); i++) {
            ReNew reNew = reNewList.get(i);
            Object[] attributes = new Object[]{
                reNew.getCommitNo(), reNew.getContSeqNum(), reNew.getContTpCd(),
                reNew.getCsmnContDt(), reNew.getCsmnContTm(), reNew.getCsmnContSprvNegFlg(),
                reNew.getCorrectionFlag(), reNew.getResnCdNegCont(), reNew.getEnteredByUserId(),
                reNew.getEnteredByUserName(), reNew.getCsmnContComnt(), reNew.getCsmnContInitDdocFlg(),
                reNew.getUserIdInit(), reNew.getReportNum(), reNew.getInterventionTp()
            };
            structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
        }

        return new ARRAY(arrayDescriptor, oracleConn, structs);
    }
    
    
    
    // Method to call procedure sp_contact_person_insert_obj
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Map<String, String>> insertContactPersons(List<ReNew> reNewList) throws SQLException {
        try {
            logger.info("Calling stored procedure sp_contact_person_insert_obj with input list size: {}", reNewList.size());

            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("spkg_csm_nosc")
                    .withProcedureName("sp_contact_person_insert_obj")
                    .declareParameters(new SqlParameter("iotb_insert", OracleTypes.ARRAY, "tb_contact_person_obj"));

            try (Connection conn = dataSource.getConnection()) {
                OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
                ARRAY sqlArray = createContactPersonArray(reNewList, oracleConn);
                Map<String, Object> inParams = Map.of("iotb_insert", sqlArray);
                logger.info("Input parameters for stored procedure sp_contact_person_insert_obj: {}", inParams);

                simpleJdbcCall.execute(inParams);
            }
            return ResponseEntity.ok(Collections.singletonMap("status", "Success"));
        } catch (Exception e) {
            logger.error("Error while calling stored procedure sp_contact_person_insert_obj: ", e);
            throw new SQLException("Error inserting ContactedPersons: ");
        }
    }
    
    @SuppressWarnings("deprecation")
    private ARRAY createContactPersonArray(List<ReNew> reNewList, OracleConnection oracleConn) throws SQLException {
        StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_CONTACT_PERSON", oracleConn);
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_CONTACT_PERSON_OBJ", oracleConn);

        List<STRUCT> structList = new ArrayList<>();

        for (ReNew reNew : reNewList) {
            if (reNew.getContactedPersons() != null) {
                int count = reNew.getContactedPersons().size();
                List<Integer> prsnSeqNums = getNextPrsnSeqNums(count);
                int index = 0;

                for (ReNew.ContactedPerson contactedPerson : reNew.getContactedPersons()) {
                    contactedPerson.setPrsnSeqNum(prsnSeqNums.get(index++)); // Assign unique prsnSeqNum
                    contactedPerson.setRelationshipSeqNum(null);
                    
                    Object[] attributes = new Object[]{
                            reNew.getCommitNo(),
                            reNew.getContSeqNum(),
                            contactedPerson.getPrsnSeqNum(),  // Unique prsnSeqNum for each ContactedPerson
                            contactedPerson.getRelationshipSeqNum(),
                            contactedPerson.getContLname(),
                            contactedPerson.getContFname(),
                            contactedPerson.getContMname(),
                            contactedPerson.getContSname(),
                            contactedPerson.getRelationshipCode()
                    };
                    structList.add(new STRUCT(structDescriptor, oracleConn, attributes));
                }
            }
        }

        return new ARRAY(arrayDescriptor, oracleConn, structList.toArray());
    }

    //Method to call procedure sp_main_query
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> executeMainQuery(String commitNo, String fromDate, String toDate, String relationship, String typeOfContact, String results) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CSM_NOSC")
                    .withProcedureName("SP_main_query")
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(
                            new SqlParameter("ivc_commit_no", Types.VARCHAR),
                            new SqlParameter("idt_cont_from_dt", Types.VARCHAR),
                            new SqlParameter("idt_cont_to_dt", Types.VARCHAR),
                            new SqlParameter("ivc_relationship_code", Types.VARCHAR),
                            new SqlParameter("ivc_cont_tp_cd", Types.VARCHAR),
                            new SqlParameter("ivc_results", Types.VARCHAR),
                            new SqlParameter("inu_order_by", Types.INTEGER),
                            new SqlParameter("ivc_sort_by", Types.VARCHAR),
                            new SqlOutParameter("iotb_query", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ivc_commit_no", commitNo)
                    .addValue("idt_cont_from_dt", fromDate)
                    .addValue("idt_cont_to_dt", toDate)
                    .addValue("ivc_relationship_code", relationship)
                    .addValue("ivc_cont_tp_cd", typeOfContact)
                    .addValue("ivc_results", results)
                    .addValue("inu_order_by", 1)
                    .addValue("ivc_sort_by", "ASC");

            Map<String, Object> result = jdbcCall.execute(in);

            List<CaseNotes> caseNotesList = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("iotb_query");
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
                    CaseNotes caseNotes = CaseNotes.caseNotesData(rowData);
                    caseNotesList.add(caseNotes);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("caseNotes", caseNotesList));
        } catch (Exception e) {
            logger.error("Error executing main query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }

    //Method for insertion of case amendment
    @Transactional
    public void updateCaseNotesAmendment(List<ReNew> reNewList) {
        if (reNewList.isEmpty()) {
            throw new RuntimeException("No valid case notes to amend.");
        }

        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CSM_NOSC")
                    .withProcedureName("sp_main_update_amendment_obj")
                    .declareParameters(new SqlParameter("iotb_update", OracleTypes.ARRAY, "TB_CSMN_HDR_AMD"));

            try (Connection conn = dataSource.getConnection()) {
                OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
                ARRAY sqlArray = createAmendmentSqlArray(reNewList, oracleConn);
                Map<String, Object> inParams = Map.of("iotb_update", sqlArray);
                simpleJdbcCall.execute(inParams);
            }
        } catch (Exception e) {
        	logger.error("Error executing updateCaseNotesAmendment: ", e);
            throw new RuntimeException("Failed to update case notes amendment");
        }
    }

    //Struct construction for updateCaseNotesAmendment method
    @SuppressWarnings("deprecation")
    private ARRAY createAmendmentSqlArray(List<ReNew> reNewList, OracleConnection oracleConn) throws SQLException {
        StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_CSMN_HDR_AMD", oracleConn);
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_CSMN_HDR_AMD", oracleConn);

        STRUCT[] structs = new STRUCT[reNewList.size()];

        for (int i = 0; i < reNewList.size(); i++) {
            ReNew reNew = reNewList.get(i);
            Object[] attributes = new Object[]{
                reNew.getCommitNo(),
                reNew.getContSeqNum(),
                reNew.getCsmnContComnt()
            };
            structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
        }

        return new ARRAY(arrayDescriptor, oracleConn, structs);
    }

    //Method to update case notes     
    @Transactional
    public void updateCaseNotesSingle(ReNew reNew) {
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CSM_NOSC")
                    .withProcedureName("sp_main_update_obj")
                    .declareParameters(new SqlParameter("itb_csmn_contact_hdr", OracleTypes.ARRAY, "TB_CSMN_CONTACT_HDR_UP"));

            try (Connection conn = dataSource.getConnection()) {
                OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
                ARRAY sqlArray = createUpdateSqlArray(List.of(reNew), oracleConn);
                Map<String, Object> inParams = Map.of("itb_csmn_contact_hdr", sqlArray);
                simpleJdbcCall.execute(inParams);
            }
        } catch (Exception e) {
        	logger.error("Error executing updateCaseNotesSingle: ", e);
            throw new RuntimeException("Failed to update case notes for commitNo: " + reNew.getCommitNo() + ", sequence: " + reNew.getContSeqNum());
        }
    }

    //Struct construction for update case notes method
    @SuppressWarnings("deprecation")
    private ARRAY createUpdateSqlArray(List<ReNew> reNewList, OracleConnection oracleConn) throws SQLException {
        StructDescriptor structDescriptor = StructDescriptor.createDescriptor("TY_CSMN_CONTACT_HDR_UP", oracleConn);
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("TB_CSMN_CONTACT_HDR_UP", oracleConn);

        STRUCT[] structs = new STRUCT[reNewList.size()];

        for (int i = 0; i < reNewList.size(); i++) {
            ReNew reNew = reNewList.get(i);
            Object[] attributes = new Object[]{
                reNew.getCommitNo(),reNew.getContSeqNum(),reNew.getContTpCd(),reNew.getContTpDesc(),
                reNew.getCsmnContDt(),reNew.getCsmnContTm(),reNew.getInsertedDate(),reNew.getInsertedTime(),
                reNew.getCsmnContSprvNegFlg(),reNew.getCorrectionFlag(),reNew.getResnCdNegCont(),reNew.getResnDesc(),
                reNew.getEnteredByUserId(),reNew.getEnteredByUserName(),reNew.getCsmnContComnt(),
                reNew.getCsmnContInitDdocFlg(),reNew.getUserIdInit(),reNew.getUserInitLname(),
                reNew.getUserInitFname(),reNew.getUserInitMname(),reNew.getUserInitSname(),
                reNew.getIscRequestedDateBox(),reNew.getIscApprovedFlag(),reNew.getReportNum(),
                reNew.getInterventionTp()
            };
            structs[i] = new STRUCT(structDescriptor, oracleConn, attributes);
        }

        return new ARRAY(arrayDescriptor, oracleConn, structs);
    }
    
    //Transactional procedure method for Case Notes Amendment API
    @Transactional
    public List<ReNew> updateCaseNotesWithAmendment(List<ReNew> reNewList) {
        List<ReNew> failedReNewList = new ArrayList<>();
        List<ReNew> processedReNewList = new ArrayList<>();

        for (ReNew reNew : reNewList) {
            try {
                updateCaseNotesSingle(reNew);
                processedReNewList.add(reNew);
            } catch (Exception e) {
                logger.error("Error while updating case notes for commitNo: " + reNew.getCommitNo() + ", sequence: " + reNew.getContSeqNum(), e);
                failedReNewList.add(reNew);
            }
        }

        try {
            updateCaseNotesAmendment(processedReNewList);
        } catch (Exception e) {
            logger.error("Error while updating case notes amendment: ", e);
            failedReNewList.addAll(processedReNewList);
        }

        return failedReNewList;
    }

    //Method to return Case Notes History record for /CaseNotesHistory API
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> casenotesHistory(String sbiNo) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CSM_NOSC")
                    .withProcedureName("sp_history_query")
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(
                            new SqlParameter("ivc_sbi_no", Types.VARCHAR),
                            new SqlOutParameter("iotb_query", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ivc_sbi_no", sbiNo);

            Map<String, Object> result = jdbcCall.execute(in);

            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("iotb_query");
            List<CaseNotes> caseNotesList = new ArrayList<>();

            if (resultSet != null) {
                for (Map<String, Object> row : resultSet) {
                    CaseNotes caseNotes = CaseNotes.CaseHistory(row);
                    caseNotesList.add(caseNotes);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("caseNotesHistory", caseNotesList));
        } catch (Exception e) {
            logger.error("Error retrieving case notes history: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
    
    //Method to rerurn Isc flag and date box
    @SuppressWarnings("unchecked")
	public ResponseEntity<Map<String,Object>> getIscFlagandDateBox(String commitNo){
    	try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CSM_NOSC")
                    .withProcedureName("sp_inmate_query")
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(
                            new SqlParameter("ivc_commit_no", Types.VARCHAR),
                            new SqlOutParameter("iotb_query", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ivc_commit_no", "0045073");

            Map<String, Object> result = jdbcCall.execute(in);

            List<CaseNotes.ISCData> iscFlagAndDate = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("iotb_query");
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
          //          CaseNotes.ISCData caseNotes = CaseNotes.iscRequestedFlagAndDate(rowData);
                	CaseNotes.ISCData caseNotes = CaseNotes.iscRequestedFlagAndDate(rowData);
                    iscFlagAndDate.add(caseNotes);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("ISC", iscFlagAndDate));
        } catch (Exception e) {
            logger.error("Error executing inmate query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
    
    //Method to return Type of Contact values
    public ResponseEntity<Map<String, Object>> getActiveContactCodesAndDescriptions() {
        try {
            List<Object[]> result = contactTpMstRepository.findContactCodesAndDescriptionsByStatus("A");
            return ResponseEntity.ok(Collections.singletonMap("activeContactCodesAndDescriptions", result));
        } catch (Exception e) {
            logger.error("Error retrieving active contact codes and descriptions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status","An error occurred while processing your request. Please try again later."));
        }
    }
    
    //Method to return Relationship values
    public ResponseEntity<Map<String, Object>> getActiveRelationshipCodesAndDescriptions() {
        try {
            List<Object[]> result = relationshipRepository.findRelationshipCodesAndDescriptionsByStatus("A");
            return ResponseEntity.ok(Collections.singletonMap("activeRelationCodesAndDescriptions", result));
        } catch (Exception e) {
            logger.error("Error retrieving active relationship codes and descriptions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
    
    //Method to return Reason values
    public ResponseEntity<Map<String, Object>> getActiveReasonCodesAndDescriptions() {
        try {
            List<Object[]> result = reasonMstRepository.findReasonCodesAndDescriptionsByStatus("A");
            return ResponseEntity.ok(Collections.singletonMap("activeReasonCodesAndDescriptions", result));
        } catch (Exception e) {
            logger.error("Error retrieving active reason codes and descriptions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
    
  //Method to call procedure sp_contact_person_query
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> executeContactPersonQuery(String commitNo, Integer contactSeqNum) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("SPKG_CSM_NOSC")
                    .withProcedureName("sp_contact_person_query")
                    .declareParameters(
                            new SqlParameter("ivc_commit_no", Types.VARCHAR),
                            new SqlParameter("inu_CSMN_CONT_SEQ_NUM", Types.INTEGER),
                            new SqlOutParameter("iotb_query", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ivc_commit_no", commitNo)
                    .addValue("inu_CSMN_CONT_SEQ_NUM", contactSeqNum);

            Map<String, Object> result = jdbcCall.execute(in);

            List<CaseNotes.ContactedPerson> contactedPersonsList = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("iotb_query");
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
                    CaseNotes.ContactedPerson caseNotes = CaseNotes.contactedPersonsData(rowData);
                    contactedPersonsList.add(caseNotes);
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("ContactedPersons", contactedPersonsList));
        } catch (Exception e) {
            logger.error("Error executing main query: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Map<String,Object>> CaseNoteProcedure(String ivc_commit_no) {
		System.out.println("CSM3="+ivc_commit_no);
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call Spkg_Csm_Nosc.sp_inmate_query(?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
	            stmt.setString(2, ivc_commit_no);
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
	
	
	public List<Map<String,Object>> CaseNoteProcedureHistory(String ivc_sbi_no) {
		System.out.println("CSM2="+ivc_sbi_no);
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call Spkg_Csm_Nosc.sp_history_query(?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
	            stmt.setString(2, ivc_sbi_no);
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
	
	
//	public List<Map<String,Object>> CaseNoteProcedureMainquery(String ivc_commit_no, Date idt_cont_from_dt, Date idt_cont_to_dt,String ivc_relationship_code, String ivc_results, String ivc_cont_tp_cd, String inu_order_by, String ivc_sort_by) {
//		System.out.println("CSM1="+ivc_commit_no);
//		ivc_sort_by="DESC";
//		inu_order_by="1";
//	    List<Map<String, Object>> resultList = new ArrayList<>();
//	    try (Connection conn = dataSource.getConnection()) {
//	        String sql = "{call Spkg_Csm_Nosc.SP_main_query(?, ?, ?, ?, ?, ?, ?, ?, ?)}"; 
//	        try (CallableStatement stmt = conn.prepareCall(sql)) {
//	            stmt.setString(1, ivc_commit_no);
//	            stmt.setDate(2, idt_cont_from_dt);
//	            stmt.setDate(3, idt_cont_to_dt);
//	            stmt.setString(4, ivc_relationship_code);
//	            stmt.setString(5, ivc_results);
//	            stmt.setString(6, ivc_cont_tp_cd);
//	            stmt.setString(7, inu_order_by);
//	            stmt.setString(8, ivc_sort_by);
//	            stmt.registerOutParameter(9, java.sql.Types.REF_CURSOR);
//	            stmt.execute();
//	        	try (ResultSet rs = (ResultSet) stmt.getObject(9)) {
//	        		ResultSetMetaData metaData = rs.getMetaData();
//	        		int columnCount = metaData.getColumnCount();
//	        		while (rs.next()) {
//	        		Map<String, Object> resultMap = new HashMap<>();
//	        		for (int i = 1; i <= columnCount; i++) {
//	        		String columnName = metaData.getColumnName(i);
//	        		Object columnValue = rs.getObject(i);
//	        		resultMap.put(columnName, columnValue);
//	        		}
//	        		resultList.add(resultMap);
//	        		}
//	        		}
//	        	
//     
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    return resultList;
//	}
	
	public List<Map<String,Object>> CaseNoteProcedureMainquery(String ivc_commit_no, Date idt_cont_from_dt, Date idt_cont_to_dt,String ivc_relationship_code, String ivc_results, String ivc_cont_tp_cd) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call spkg_omnet_wrapper.sp_csm_nosc_main_query(?, ?, ?, ?, ?, ?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.setString(1, ivc_commit_no);
	            stmt.setDate(2, idt_cont_from_dt);
	            stmt.setDate(3, idt_cont_to_dt);
	            stmt.setString(4, ivc_relationship_code);
	            stmt.setString(5, ivc_results);
	            stmt.setString(6, ivc_cont_tp_cd);

	            stmt.registerOutParameter(7, java.sql.Types.REF_CURSOR);
	            stmt.execute();
	        	try (ResultSet rs = (ResultSet) stmt.getObject(7)) {
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
	
	public List<Map<String,Object>> CSMContactperson(String ivc_commit_no, String inu_csmn_cont_seq_no) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call Spkg_Csm_Nosc.sp_contact_person_query(?, ?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	        	stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
	            stmt.setString(2, ivc_commit_no);
	            stmt.setString(3, inu_csmn_cont_seq_no);
	       
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
	
	public void CSMAmendment(List<Amendment> DetailsvaluesfoCSMAmendment) {
		String procedureCall = "{call spkg_omnet_wrapper.sp_mov_nosc_update_amendment(?)}";
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			 OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			 Struct[] structs = new Struct[DetailsvaluesfoCSMAmendment.size()];
			 for (int i = 0; i < DetailsvaluesfoCSMAmendment.size(); i++) {
				 structs[i] = oracleConnection.createStruct("TY_CSM_NOSC_UPDATE_AMENDMENT", new Object[]{
						 DetailsvaluesfoCSMAmendment.get(i).getCOMMIT_NO(),
						 DetailsvaluesfoCSMAmendment.get(i).getCSMN_CONT_SEQ_NO(),
						 DetailsvaluesfoCSMAmendment.get(i).getCsmn_cont_comnt_amendment()
						 
				 });
					System.out.println("post procedure3"); 
			 }
			 Array array = oracleConnection.createOracleArray("TB_CSM_NOSC_UPDATE_AMENDMENT", structs);
			 callableStatement.setArray(1, array);
	            callableStatement.execute(); 
	             }catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure", e);
	        }
	}
	
	
	public void CSMContactInformationInsert(List<ContactInformationInsert> CSMCI) {
		String procedureCall = "{call spkg_omnet_wrapper.sp_csm_nosc_new_insert(?)}";
		System.out.println("post procedure="+CSMCI.get(0).getCommit_no()); 
		try (Connection connection = dataSource.getConnection();
			
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			System.out.println("post procedure1"); 
			 OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			 
			 Struct[] structs = new Struct[CSMCI.size()];

			 for (int i = 0; i < CSMCI.size(); i++) {
				 
				 structs[i] = oracleConnection.createStruct("TY_CSM_NOSC_NEW", new Object[]{
						 CSMCI.get(i).getCommit_no(),
						 
						 CSMCI.get(i).getCsmn_cont_seq_no(),
						 CSMCI.get(i).getCont_tp_cd(),
						 CSMCI.get(i).getCsmn_cont_dt(),
						 CSMCI.get(i).getCsmn_cont_tm(),
						 CSMCI.get(i).getInserted_date(),
						 CSMCI.get(i).getInserted_time(),
						 CSMCI.get(i).getCsmn_cont_sprv_neg_flg(),
						 CSMCI.get(i).getCorrection_flag(),
						 CSMCI.get(i).getResn_cd_neg_cont(),
						 CSMCI.get(i).getEntered_by_user_id(),
						 CSMCI.get(i).getEntered_by_user_name(),
						 CSMCI.get(i).getCsmn_cont_comnt(),
						 CSMCI.get(i).getCsmn_cont_init_ddoc_flg(),
						 CSMCI.get(i).getUser_id_init(),
						 CSMCI.get(i).getREPORT_NUM(),
						 CSMCI.get(i).getIntervention_tp(),
						 CSMCI.get(i).getVerify_addr_flag()
						 
				 });
					System.out.println("post procedure3"); 
			 }
			 
			 Array array = oracleConnection.createOracleArray("TB_CSM_NOSC_NEW", structs);
			 
			 callableStatement.setArray(1, array);
	        	System.out.println("post procedure4"); 
	            callableStatement.execute();
	        	System.out.println("post procedure5"); 
	             }catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure", e);
	        }
	}
	
	public void CSMContactPersonInsert(List<ContactPersonInsert> CSMCPI) {
		String procedureCall = "{call spkg_omnet_wrapper.sp_csm_nosc_contact_person_insert(?)}";
		System.out.println("post procedure="+CSMCPI.get(0).getCommit_no()+" "+CSMCPI.get(0).getCSMN_PRSN_SEQ_NO()+" "+CSMCPI.get(0).getCSMN_CONT_SEQ_NO()+" "+CSMCPI.get(0).getCont_fname()); 
		try (Connection connection = dataSource.getConnection();
			
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			System.out.println("post procedure1"); 
			 OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			 
			 Struct[] structs = new Struct[CSMCPI.size()];

			 for (int i = 0; i < CSMCPI.size(); i++) {
				 
				 structs[i] = oracleConnection.createStruct("TY_CSM_NOSC_CONTACT_PERSON", new Object[]{
						 CSMCPI.get(i).getCommit_no(),
						 CSMCPI.get(i).getCSMN_CONT_SEQ_NO(),
						 CSMCPI.get(i).getCSMN_PRSN_SEQ_NO(),
						 CSMCPI.get(i).getRELATIONSHIP_SEQ_NUM(),
						 CSMCPI.get(i).getCont_lname(),
						 CSMCPI.get(i).getCont_fname(),
						 CSMCPI.get(i).getCont_mname(),
						 CSMCPI.get(i).getCont_sname(),
						 CSMCPI.get(i).getRELATIONSHIP_CODE()
						 
				 });
					System.out.println("post procedure3"); 
			 }
			 
			 Array array = oracleConnection.createOracleArray("TB_CSM_NOSC_CONTACT_PERSON", structs);
			 
			 callableStatement.setArray(1, array);
	        	System.out.println("post procedure4"); 
	            callableStatement.execute();
	        	System.out.println("post procedure5"); 
	             }catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure", e);
	        }
	}
	
	public void CSMUpdateISCTranfer(List<ISCDateApprovedCaseNotes> dateISCCSM) {
		String procedureCall = "{call spkg_omnet_wrapper.sp_csm_nosc_inmate_update(?)}";
		try (Connection connection = dataSource.getConnection();
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			 OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			 Struct[] structs = new Struct[dateISCCSM.size()];
			 for (int i = 0; i < dateISCCSM.size(); i++) {
				 structs[i] = oracleConnection.createStruct("TY_CSM_NOSC_INMATE_UPDATE", new Object[]{
						 dateISCCSM.get(i).getCOMMIT_NO(),
						 dateISCCSM.get(i).getIsc_requested_date_box(),
						 dateISCCSM.get(i).getIsc_approved_flag()
				 });
					System.out.println("post procedure3"); 
			 }
			 Array array = oracleConnection.createOracleArray("TB_CSM_NOSC_INMATE_UPDATE", structs);
			 callableStatement.setArray(1, array);
	            callableStatement.execute(); 
	             }catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure", e);
	        }
	}
	
}
