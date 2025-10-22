/**
 * Document: HousingEntityWaitlistService.java
 * Author: Jamal Abraar
 * Date Created: 31-Jul-2024
 * Last Updated: 
 */
package com.omnet.cnt.Service;

import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Repository.BedRtRepository;
import com.omnet.cnt.Repository.BuildingRepository;
import com.omnet.cnt.Repository.CellRtRepository;
import com.omnet.cnt.Repository.FloorRtRepository;
import com.omnet.cnt.Repository.InstitutionRepository;
import com.omnet.cnt.Repository.TierRepository;
import com.omnet.cnt.Repository.UnitRtRepository;
import com.omnet.cnt.classes.HousingEntityWaitlist;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;


@Service
public class HousingEntityWaitlistService {
	 
	@Autowired
	private InstitutionRepository institutionRepository;
	
	@Autowired
	private UnitRtRepository unitRtRepository;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired 
	private FloorRtRepository floorRtRepository;
	
	@Autowired 
	private TierRepository tierRepository;
	
	@Autowired
	private CellRtRepository cellRtRepository;
	
	@Autowired 
	private BedRtRepository bedRtRepository;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	private DataSource datasource;
	
	private static final Logger logger = LoggerFactory.getLogger(HousingEntityWaitlistService.class);

	public  Map<String, Object> getFacilities(String typeOfWaitlist) {
		Map<String, Object> response = new HashMap<>();
	   if ("I".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
	       List<Object[]> facility= institutionRepository.findFacilitiesForDropdown(typeOfWaitlist);
	       response.put("status", "SUCCESS_TOWI&B");
           response.put("facility", facility);
	    }
	   else if ("H".equals(typeOfWaitlist)) {
	       List <Object[]> facility=institutionRepository.findIntermediateFacilitiesForDropdown(typeOfWaitlist);
	       response.put("status", "SUCCESS_TOWH");
           response.put("facility", facility);
	    }else {
	    	response.put("status", "NORESULT_FACILITY");
            response.put("facility", Collections.emptyList());
	    }
	    return response; 
	}
	
	public  Map<String, Object> getBuildings(String typeOfWaitlist,String instNum) {
        Map<String, Object> response = new HashMap<>();
 	   if ("I".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
 	       List<Object[]> building= buildingRepository.findBuildingNamesByInstNum(instNum);
 	       response.put("status", "SUCCESS_TOWI&B");
            response.put("building", building);
 	    }
 	   else if ("H".equals(typeOfWaitlist)) {
 	       List <Object[]> building= buildingRepository.findBuildingNamesByInstNum(instNum);
 	       response.put("status", "SUCCESS_TOWH");
            response.put("building", building);
 	    }else {
 	    	response.put("status", "NORESULT_BUILDING");
             response.put("building", Collections.emptyList());
 	    }
 	    return response; 
    }
	
	public Map<String, Object> getUnits(String typeOfWaitlist,String instNum, String bldNum) {
		Map<String, Object> response = new HashMap<>();
		
		if("H".equals(typeOfWaitlist)) {
			int result = callSfReturnCustHouCd(instNum);
	        if (result != 1) {
	        	List<Object[]> units = unitRtRepository.findByInstNumAndBldNum(instNum, bldNum);
	            response.put("status", "SUCCESS_TOWH");
	            response.put("units", units);
	        }else if(result == 1){
	        	response.put("status", "NORESULT_UNIT");
	            response.put("units", Collections.emptyList());
	        }
		}if("B".equals(typeOfWaitlist)) {
			List<Object[]> units = unitRtRepository.findByInstNumAndBldNum(instNum, bldNum);
            response.put("status", "SUCCESSTOWB");
            response.put("units", units);
		}       
        return response;
    }
	
	public Map<String, Object> getFloors(String typeOfWaitlist, String instNum, String bldNum, String unitId) {
	    Map<String, Object> response = new HashMap<>();
	    
	    if ("H".equals(typeOfWaitlist)) {
	        int result = callSfReturnCustHouCd(instNum);
	        if (result != 2) {
	            List<Object[]> floors = floorRtRepository.findFloors(instNum, bldNum, unitId);
	            response.put("status", "SUCCESS_TOWH");
	            response.put("floors", floors);
	        } else {
	            response.put("status", "NORESULT_FLOOR");
	            response.put("floors", Collections.emptyList());
	        }
	    } else if ("B".equals(typeOfWaitlist)) {
	        List<Object[]> floors = floorRtRepository.findFloors(instNum, bldNum, unitId);
	        response.put("status", "SUCCESSTOWB");
	        response.put("floors", floors);
	    }
	    
	    return response;
	}

	public Map<String, Object> getTiers(String typeOfWaitlist, String instNum, String bldNum, String unitId, String floorNum) {
	    Map<String, Object> response = new HashMap<>();
	    
	    if ("H".equals(typeOfWaitlist)) {
	        int result = callSfReturnCustHouCd(instNum);
	        if (result != 3) {
	            List<Object[]> tiers = tierRepository.findTiers(instNum, bldNum, unitId, floorNum);
	            response.put("status", "SUCCESS_TOWH");
	            response.put("tiers", tiers);
	        } else {
	            response.put("status", "NORESULT_TIER");
	            response.put("tiers", Collections.emptyList());
	        }
	    } else if ("B".equals(typeOfWaitlist)) {
	        List<Object[]> tiers = tierRepository.findTiers(instNum, bldNum, unitId, floorNum);
	        response.put("status", "SUCCESSTOWB");
	        response.put("tiers", tiers);
	    }
	    
	    return response;
	}

	public Map<String, Object> getCells(String typeOfWaitlist, String instNum, String bldNum, String unitId, String floorNum, String tierNum) {
	    Map<String, Object> response = new HashMap<>();
	    
	    if ("H".equals(typeOfWaitlist)) {
	        int result = callSfReturnCustHouCd(instNum);
	        if (result != 4) {
	            List<Object[]> cells = cellRtRepository.findCells(instNum, bldNum, unitId, floorNum, tierNum);
	            response.put("status", "SUCCESS_TOWH");
	            response.put("cells", cells);
	        } else {
	            response.put("status", "NORESULT_CELL");
	            response.put("cells", Collections.emptyList());
	        }
	    } else if ("B".equals(typeOfWaitlist)) {
	        List<Object[]> cells = cellRtRepository.findCells(instNum, bldNum, unitId, floorNum, tierNum);
	        response.put("status", "SUCCESSTOWB");
	        response.put("cells", cells);
	    }
	    
	    return response;
	}

	public Map<String, Object> getBeds(String typeOfWaitlist, String instNum, String bldNum, String unitId, String floorNum, String tierNum, String cellNum) {
	    Map<String, Object> response = new HashMap<>();
	    
	    if ("H".equals(typeOfWaitlist)) {
	        int result = callSfReturnCustHouCd(instNum);
	        if (result != 5) {
	            List<Object[]> beds = bedRtRepository.findBeds(instNum, bldNum, unitId, floorNum, tierNum, cellNum);
	            response.put("status", "SUCCESS_TOWH");
	            response.put("beds", beds);
	        } else {
	            response.put("status", "NORESULT_BED");
	            response.put("beds", Collections.emptyList());
	        }
	    } else if ("B".equals(typeOfWaitlist)) {
	        List<Object[]> beds = bedRtRepository.findBeds(instNum, bldNum, unitId, floorNum, tierNum, cellNum);
	        response.put("status", "SUCCESSTOWB");
	        response.put("beds", beds);
	    }
	    
	    return response;
	}
		
	@SuppressWarnings("deprecation")
	private int callSfReturnCustHouCd(String instNum) {
        String sql = "SELECT SPKG_MOV_BEWL.SF_RETURN_CUST_HOU_CD(?) FROM DUAL";
        return jdbcTemplate.queryForObject(sql, new Object[]{instNum}, Integer.class);
    }
	

	@SuppressWarnings("deprecation")
	public ResponseEntity<Map<String, Object>> executeWaitlistQuery(
	        String institutionNum,
	        String buildingNum,
	        String unitId,
	        String floorNum,
	        String tierNum,
	        String cellNo,
	        String bedNo,
	        String docUnitInd,
	        String waitlistBy,
	        String movementScheduledFlag,
	        Date fromDateStr,
	        Date toDateStr) {
	    try {
	        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(datasource)
	                .withCatalogName("SPKG_OMNET_WRAPPER")
	                .withProcedureName("SP_MOV_BEWL_HOUS_WAIT_QUERY")
	                .declareParameters(
	                        new SqlParameter("ICH_INST_NUM", Types.VARCHAR),
	                        new SqlParameter("ICH_BLD_NUM", Types.VARCHAR),
	                        new SqlParameter("ICH_UNIT_ID", Types.VARCHAR),
	                        new SqlParameter("ICH_FLOOR_NUM", Types.VARCHAR),
	                        new SqlParameter("ICH_TIER_NUM", Types.VARCHAR),
	                        new SqlParameter("ICH_CELL_NO", Types.VARCHAR),
	                        new SqlParameter("ICH_BED_NO", Types.VARCHAR),
	                        new SqlParameter("ICH_DOC_UNIT_IND", Types.VARCHAR),
	                        new SqlParameter("ICH_WAITLIST_BY", Types.VARCHAR),
	                        new SqlParameter("ICH_MOVEMENT_SCHEDULED_FLAG", Types.VARCHAR),
	                        new SqlParameter("IDT_FROM_DATE", Types.DATE),
	                        new SqlParameter("IDT_TO_DATE", Types.DATE),
	                        new SqlOutParameter("RESULTSET", OracleTypes.ARRAY, "TB_MOV_BEWL_HOUSING_WL"));

	        SqlParameterSource in = new MapSqlParameterSource()
	                .addValue("ICH_INST_NUM", institutionNum)
	                .addValue("ICH_BLD_NUM", buildingNum)
	                .addValue("ICH_UNIT_ID", unitId)
	                .addValue("ICH_FLOOR_NUM", floorNum)
	                .addValue("ICH_TIER_NUM", tierNum)
	                .addValue("ICH_CELL_NO", cellNo)
	                .addValue("ICH_BED_NO", bedNo)
	                .addValue("ICH_DOC_UNIT_IND", docUnitInd)
	                .addValue("ICH_WAITLIST_BY", waitlistBy)
	                .addValue("ICH_MOVEMENT_SCHEDULED_FLAG", movementScheduledFlag)
	                .addValue("IDT_FROM_DATE", fromDateStr)
	                .addValue("IDT_TO_DATE", toDateStr);

	        Map<String, Object> result = jdbcCall.execute(in);

	        // Retrieve the Oracle ARRAY
	        ARRAY oracleArray = (ARRAY) result.get("RESULTSET");
	        System.out.println("waitlistItemadsfdg"+oracleArray);	
	        Object[] oracleObjects = (Object[]) oracleArray.getArray();
            
	        for (Object obj : oracleObjects) {
	            if (obj instanceof STRUCT) {
	                STRUCT struct = (STRUCT) obj;
	                Object[] attributes = struct.getAttributes(); // Extract attributes

	                System.out.println("Record values: ");
	                for (Object attr : attributes) {
	                    System.out.println(attr);
	                }
	            } else {
	                System.out.println("Value: " + obj);
	            }
	        }
	        // Convert ARRAY to List<Map<String, Object>>
	        List<Map<String, Object>> resultSet = new ArrayList<>();
	        for (Object obj : oracleObjects) {
	            STRUCT struct = (STRUCT) obj;
	            Map<String, Object> row = new HashMap<>();
	            Object[] attributes = struct.getAttributes();
	            ResultSetMetaData metaData = struct.getDescriptor().getMetaData();
	            for (int i = 0; i < attributes.length; i++) {
	                row.put(metaData.getColumnName(i + 1), attributes[i]);
	                System.out.println("Column Name: " + metaData.getColumnName(i + 1) + ", Value: " + attributes[i]);
	            }
	            resultSet.add(row);
	        }

	        List<HousingEntityWaitlist> resultList = new ArrayList<>();
	        for (Map<String, Object> row : resultSet) {
	            HousingEntityWaitlist waitlistItem = HousingEntityWaitlist.housingEntityWaitList(row);
	            System.out.println("waitlistItemadsfdg"+waitlistItem);	            
	            resultList.add(waitlistItem);
	        }
	        return ResponseEntity.ok(Collections.singletonMap("housingEntityWaitlist", resultList));
	    } catch (SQLException e) {
	        logger.error("Error executing procedure: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Collections.singletonMap("status", "Error: " + e.getMessage()));
	    } catch (Exception e) {
	        logger.error("Error processing result: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Collections.singletonMap("status", "Error: " + e.getMessage()));
	    }
	}
	
	@SuppressWarnings("unchecked")
    @Async
    public CompletableFuture<ResponseEntity<Map<String, Object>>> executeHousingReceivingQuery(String commitNo, Integer docWLSeqNum) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(datasource)
                    .withCatalogName("SPKG_MOV_BEWL")
                    .withProcedureName("SP_HOUSING_RECEIVING_QUERY")
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(
                            new SqlParameter("ICH_COMMIT_NO", Types.VARCHAR),
                            new SqlParameter("p_doc_wl_seq_num", Types.INTEGER),
                            new SqlOutParameter("RESULTSET", Types.REF_CURSOR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ICH_COMMIT_NO", commitNo)
                    .addValue("p_doc_wl_seq_num", docWLSeqNum);

            Map<String, Object> result = jdbcCall.execute(in);

            List<HousingEntityWaitlist> receivingOffenderList = new ArrayList<>();
            List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("RESULTSET");
            if (resultSet != null) {
                for (Map<String, Object> rowData : resultSet) {
                    HousingEntityWaitlist offenderDetails = HousingEntityWaitlist.receivingOffenderQuery(rowData);
                    receivingOffenderList.add(offenderDetails);
                }
            }

            return CompletableFuture.completedFuture(
                ResponseEntity.ok(Collections.singletonMap("receivingOffenderList", receivingOffenderList))
            );
        } catch (Exception e) {
            logger.error("Error executing Housing Receiving query: ", e);
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("status", "Error: " + e.getMessage()))
            );
        }
    }
	
	/*
	 * @SuppressWarnings("deprecation")
	 * 
	 * @Async public CompletableFuture<ResponseEntity<Map<String, Object>>>
	 * executeHousingCurrentQuery(String commitNo) { try { SimpleJdbcCall jdbcCall =
	 * new SimpleJdbcCall(datasource) .withCatalogName("SPKG_OMNET_WRAPPER")
	 * .withProcedureName("SP_MOV_BEWL_HOUSING_CURRENT_QUERY")
	 * .withoutProcedureColumnMetaDataAccess() .declareParameters( new
	 * SqlParameter("ICH_COMMIT_NO", Types.VARCHAR), new
	 * SqlOutParameter("RESULTSET", OracleTypes.ARRAY,
	 * "TB_MOV_BEWL_HOUSING_ASSIGNMENT"));
	 * 
	 * SqlParameterSource in = new MapSqlParameterSource()
	 * .addValue("ICH_COMMIT_NO", commitNo);
	 * 
	 * Map<String, Object> result = jdbcCall.execute(in);
	 * 
	 * // Retrieve the Oracle ARRAY ARRAY oracleArray = (ARRAY)
	 * result.get("RESULTSET"); Object[] oracleObjects = (Object[])
	 * oracleArray.getArray();
	 * 
	 * // Convert ARRAY to List<Map<String, Object>> List<Map<String, Object>>
	 * resultSet = new ArrayList<>(); for (Object obj : oracleObjects) { STRUCT
	 * struct = (STRUCT) obj; Map<String, Object> row = new HashMap<>(); Object[]
	 * attributes = struct.getAttributes(); ResultSetMetaData metaData =
	 * struct.getDescriptor().getMetaData();
	 * 
	 * for (int i = 0; i < attributes.length; i++) {
	 * row.put(metaData.getColumnName(i + 1), attributes[i]); } resultSet.add(row);
	 * }
	 * 
	 * List<HousingEntityWaitlist> currentOffenderList = new ArrayList<>(); for
	 * (Map<String, Object> row : resultSet) { HousingEntityWaitlist offenderData =
	 * HousingEntityWaitlist.currentOffenderQuery(row);
	 * currentOffenderList.add(offenderData); }
	 * 
	 * return CompletableFuture.completedFuture(
	 * ResponseEntity.ok(Collections.singletonMap("currentOffenderList",
	 * currentOffenderList)) ); } catch (Exception e) {
	 * logger.error("Error executing Housing Current query: ", e); return
	 * CompletableFuture.completedFuture(
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body(Collections.singletonMap("status", "Error: " + e.getMessage())) ); } }
	 */
	
	
	@SuppressWarnings("deprecation")
	@Async
	public CompletableFuture<ResponseEntity<Map<String, Object>>> executeHousingCurrentQuery(String commitNo) {
	    try {
	        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(datasource)
	                .withCatalogName("SPKG_OMNET_WRAPPER")
	                .withProcedureName("SP_MOV_BEWL_HOUSING_CURRENT_QUERY")
	                .withoutProcedureColumnMetaDataAccess()
	                .declareParameters(
	                        new SqlParameter("ICH_COMMIT_NO", Types.VARCHAR),
	                        new SqlOutParameter("RESULTSET", OracleTypes.ARRAY, "TB_MOV_BEWL_HOUSING_ASSIGNMENT"));

	        SqlParameterSource in = new MapSqlParameterSource()
	                .addValue("ICH_COMMIT_NO", commitNo);

	        Map<String, Object> result = jdbcCall.execute(in);

	        // Retrieve the Oracle ARRAY
	        ARRAY oracleArray = (ARRAY) result.get("RESULTSET");
	        
	        // Check if oracleArray is null
	        if (oracleArray == null) {
	          
	            return CompletableFuture.completedFuture(
	                ResponseEntity.ok(Collections.singletonMap("currentOffenderList", Collections.emptyList()))
	            );
	        }

	        Object[] oracleObjects = (Object[]) oracleArray.getArray();

	        // Convert ARRAY to List<Map<String, Object>>
	        List<Map<String, Object>> resultSet = new ArrayList<>();
	        for (Object obj : oracleObjects) {
	            STRUCT struct = (STRUCT) obj;
	            Map<String, Object> row = new HashMap<>();
	            Object[] attributes = struct.getAttributes();
	            ResultSetMetaData metaData = struct.getDescriptor().getMetaData();

	            for (int i = 0; i < attributes.length; i++) {
	                row.put(metaData.getColumnName(i + 1), attributes[i]);
	            }
	            resultSet.add(row);
	        }

	        List<HousingEntityWaitlist> currentOffenderList = new ArrayList<>();
	        for (Map<String, Object> row : resultSet) {
	            HousingEntityWaitlist offenderData = HousingEntityWaitlist.currentOffenderQuery(row);
	            currentOffenderList.add(offenderData);
	        }

	        return CompletableFuture.completedFuture(
	            ResponseEntity.ok(Collections.singletonMap("currentOffenderList", currentOffenderList))
	        );
	    } catch (Exception e) {
	        logger.error("Error executing Housing Current query: ", e);
	        return CompletableFuture.completedFuture(
	            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Collections.singletonMap("status", "Error: " + e.getMessage()))
	        );
	    }
	}

}
