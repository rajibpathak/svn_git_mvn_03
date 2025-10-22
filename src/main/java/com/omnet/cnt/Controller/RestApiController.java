package com.omnet.cnt.Controller;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.omnet.cnt.Model.Amendment;
import com.omnet.cnt.Model.ContactInformationInsert;
import com.omnet.cnt.Model.ContactPersonInsert;
import com.omnet.cnt.Model.ISCDateApprovedCaseNotes;
import com.omnet.cnt.Repository.ContactTpMstRepository;
import com.omnet.cnt.Repository.InstLocationRepository;
import com.omnet.cnt.Repository.InstitutionRepository;
import com.omnet.cnt.Repository.ReasonMstRepository;
import com.omnet.cnt.Repository.RelationshipRtRepository;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.Service.CaseNotesService;
import com.omnet.cnt.Service.EmergencyAttentionService;
import com.omnet.cnt.Service.InmateService;
import com.omnet.cnt.Service.HousingEntityWaitlistService;
import com.omnet.cnt.Service.IndividualOffenderMovService;
import com.omnet.cnt.Service.LoginService;
import com.omnet.cnt.Service.MassOffenderMovService;
import com.omnet.cnt.Service.OffenderHousingDetailsService;
import com.omnet.cnt.classes.CaseNotes;
import com.omnet.cnt.classes.CustomUserDetails;
import com.omnet.cnt.classes.EmergencyAttention;
import com.omnet.cnt.classes.IndividualOffMov;
import com.omnet.cnt.classes.MassOffenderMovement;
import com.omnet.cnt.classes.ReNew;

/**
 * Document: RestApiController.java
 * Author: Jamal Abraar
 * Date Created: 11-Jun-2024
 * Last Updated: 
 */

@RestController
public class RestApiController {
	
	public record CaseNotesInput(String sbiNumber, String commitNo, String dateFrom, String dateTo, String typeOfContact, String relationship, String results) {}
	
	public record ContactPersonRequest(String commitNum, Integer contactSeqNum) {}
	
	public record HousingHistoryParam(String sbiNo, String dateFrom, String dateTo) {}
	
	public record HousingEntityParam(String typeOfWaitlist,String queryWaitlist,String movement,String dateFrom,String dateTo, String instNum, String building, String unit, String floor, String tier, String cell,String bed, Integer docWLSeqNum, String commitNo) {}
	
	public record IndividualOffenderDetail(String commitNo,String instNum,String dateTimeOfDep,String dateTimeOfArr,String countLocCodeFrom,String countLocCodeTo,String comments,String activityTypeCode,String rgHousRelationship) {}
	
	public record MassOffenderDetail(String logInOut,String countLocCode,String scheduleFlag,String instNum,String queryType) {}
	
	public record MassOffenderMovRequest(
		    String logInOut,
		    String countLocCode,
		    List<MassOffenderMovement> saveData
		) {}
	
	public record EmergencyAttentionParams(
			String commitNo,
			String emerSeqNum
		) {}
	
	@Autowired
    private CaseNotesService caseNotesService;
	
	@Autowired
	private OffenderHousingDetailsService offenderHousingDetailsService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private InmateService inmateService;
	
	@Autowired
    private HousingEntityWaitlistService housingEntityWaitlistService;
	
	@Autowired
	private IndividualOffenderMovService individualOffenderMovService;
	
	@Autowired
	private MassOffenderMovService massOffenderMovService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private InstitutionRepository institutionRepo;
	
	@Autowired
	private InstLocationRepository instLocRepo;
	
	@Autowired
	private EmergencyAttentionService emergencyAttentionService;
	
	@Autowired
	 private RelationshipRtRepository Relation;
	
	@Autowired
	 private ContactTpMstRepository contactTpMstRepository;
	
	 @Autowired
	    private ReasonMstRepository reasonMstRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);
	
	String username;
	String userId;
	
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return customUserDetails.getUserId();
        }
        
        return null; 
    }
	
	private String parseDate(String dateString) {
	    if (dateString == null || !dateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
	        return null;
	    }
	    
	    String[] dateComponents = dateString.split("/");
	    
	    if (dateComponents.length != 3) {
	        return null;
	    }
	    
	    String month = dateComponents[0];
	    String day = dateComponents[1];
	    String year = dateComponents[2];
	    
	    String formattedDate = day + "-" + month + "-" + year;
	    
	    return formattedDate;
	}
	
	public static Date convertStringToSqlDate(String dateStr) {
	    if (dateStr == null || dateStr.isEmpty()) {
	        return null; 
	    }
	    String[] formats = { "yyyy-MM-dd", "MM/dd/yyyy" };

	    for (String formatStr : formats) {
	        try {
	            SimpleDateFormat format = new SimpleDateFormat(formatStr);
	            java.util.Date parsedDate = format.parse(dateStr);
	            return new Date(parsedDate.getTime());
	        } catch (ParseException e) {
	        }
	    }
	    throw new IllegalArgumentException("Invalid date format. Supported formats are: yyyy-MM-dd, MM/dd/yyyy.");
	}
	
	@PostMapping(value = "/CaseAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> saveCaseNotesAmendment(@RequestBody List<CaseNotes> caseNotesList) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    String userId = loginService.getUserIdByUsername(username);

	    List<ReNew> validReNewList = new ArrayList<>();
	    List<Map<String, String>> errors = new ArrayList<>();

	    for (CaseNotes caseNotes : caseNotesList) {
	        try {
	            // Validate each case note (you can add your own validation logic here)
	            if (caseNotes.getCommitNo() == null || caseNotes.getCsmnContSeqNum() == null || caseNotes.getCsmnContComnt() == null) {
	                throw new IllegalArgumentException("Invalid case note data");
	            }

	            ReNew reNew = new ReNew();
	            reNew.setCommitNo(caseNotes.getCommitNo());
	            reNew.setContSeqNum(caseNotes.getCsmnContSeqNum());
	            reNew.setCsmnContComnt(caseNotes.getCsmnContComnt());
	            reNew.setUserIdInit(userId);
	            reNew.setCorrectionFlag("Y");

	            validReNewList.add(reNew);
	        } catch (Exception e) {
	            errors.add(Collections.singletonMap("error", "Error processing commitNo: " + caseNotes.getCommitNo() + ", sequence: " + caseNotes.getCsmnContSeqNum() + ", error: " + e.getMessage()));
	        }
	    }

	    // Save valid ReNew objects
	    List<ReNew> failedReNewList = new ArrayList<>();
	    if (!validReNewList.isEmpty()) {
	        failedReNewList = caseNotesService.updateCaseNotesWithAmendment(validReNewList);
	    }

	    // Prepare response
	    Map<String, Object> response = new HashMap<>();
	    int successfulCount = validReNewList.size() - failedReNewList.size();
	    response.put("success", successfulCount);
	    response.put("total", validReNewList.size());
	    response.put("errors", errors);
	    response.put("failedObjects", failedReNewList);

	    return ResponseEntity.ok(response);
	}
    
    @PostMapping(value = "/SaveCaseNotes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> savedCaseNotes(@RequestBody ReNew tbNew) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String userId = loginService.getUserIdByUsername(username);

        tbNew.setEnteredByUserId(userId);
        tbNew.setUserIdInit(userId);
        tbNew.setEnteredByUserName(username);
        tbNew.setInsertedDate(null);
        tbNew.setReportNum(null);
        tbNew.setCorrectionFlag("N");
        
        // Check commitNo and sbiNo
        if (tbNew.getCommitNo() == null || tbNew.getCommitNo().isEmpty()) {
            String sbiNo = tbNew.getSbiNo();
            if (sbiNo != null && !sbiNo.isEmpty()) {
            	String commitNo = inmateService.validCommitNo(sbiNo);
            	tbNew.setCommitNo(commitNo);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body(Collections.singletonMap("status", "Error: Both commitNo and sbiNo are missing"));
            }
        }
              
        System.out.println(tbNew);

        return caseNotesService.performCombinedTransaction(Collections.singletonList(tbNew));
    } 
    
    @PostMapping(value = "/CaseNotesData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCaseNotesData(@RequestBody CaseNotesInput caseNotesInput) {
        String sbiNumber = caseNotesInput.sbiNumber();
        String commitNo = caseNotesInput.commitNo();
        String fromDate = parseDate(caseNotesInput.dateFrom());
        String toDate = parseDate(caseNotesInput.dateTo());
        String relationship = caseNotesInput.relationship();
        String typeOfContact = caseNotesInput.typeOfContact();
        String results = caseNotesInput.results();

        if (sbiNumber == null || sbiNumber.isEmpty() || sbiNumber == "") {
            return caseNotesService.executeMainQuery(commitNo, fromDate, toDate, relationship, typeOfContact, results);
        } else if (commitNo == null || commitNo.isEmpty() || commitNo == "") {
            commitNo = inmateService.validCommitNo(sbiNumber);
            return caseNotesService.executeMainQuery(commitNo, fromDate, toDate, relationship, typeOfContact, results);
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("status", "Invalid input"));
    }
    
    @PostMapping(value = "/CaseNotesHistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCaseNotesHistory(@RequestBody String sbiNumber) {
        return caseNotesService.casenotesHistory(sbiNumber);
    }
    
    @PostMapping(value = "/IscRequestedData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getIscData(@RequestBody String sbiNumber) {
    	String commitNo = inmateService.validCommitNo(sbiNumber);
        return caseNotesService.getIscFlagandDateBox(commitNo);
    }
    
    @PostMapping(value = "/ContactedPersons", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getContactedPersons(@RequestBody ContactPersonRequest request) {
        String commitNum = request.commitNum();
        Integer contactSeqNum = request.contactSeqNum();
        return caseNotesService.executeContactPersonQuery(commitNum, contactSeqNum);
    }
	
    @GetMapping("/getContactTpDesc")
    public ResponseEntity<Map<String, Object>> getActiveContacts() {
        return caseNotesService.getActiveContactCodesAndDescriptions();
    }
    
    @GetMapping("/getRelationshipDesc")
    public ResponseEntity<Map<String,Object>> getActiveRelationships(){
    	return caseNotesService.getActiveRelationshipCodesAndDescriptions();
    }
    
    @GetMapping("/getReasonCodes")
    public ResponseEntity<Map<String,Object>> getActiveReasons(){
    	return caseNotesService.getActiveReasonCodesAndDescriptions();
    }
    
    //Offender Housing Details and History Screen
    @PostMapping(value = "/offenderHousingDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getOffenderHousingDetails(@RequestBody String sbiNumber) {
    	String commitNo = inmateService.validCommitNo(sbiNumber);
        return offenderHousingDetailsService.executeOHDHQuery(commitNo);
    }
    
    @PostMapping(value = "/offenderHousingHistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getOffenderHousingHistory(@RequestBody HousingHistoryParam housingHistoryParam) {
    	String sbiNumber = housingHistoryParam.sbiNo();
    	String fromDate = housingHistoryParam.dateFrom();
    	String toDate = housingHistoryParam.dateTo();
    	
    	String commitNo = inmateService.validCommitNo(sbiNumber);
        return offenderHousingDetailsService.executeMovHistQuery(commitNo, fromDate, toDate);
    }
    
    @PostMapping(value = "/noContactInFacility", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getNoContactInfacility(@RequestBody String sbiNumber) {
        return offenderHousingDetailsService.callSpMovEnemyQuery(sbiNumber);
    }
    
    @PostMapping(value = "/getFacilityhousing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getFacility(@RequestBody String typeOfWaitlist) {
    	if(typeOfWaitlist != null) {
    		return housingEntityWaitlistService.getFacilities(typeOfWaitlist);
    	}
    	return null;
    }
    
    @PostMapping(value = "/getBuilding", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getBuilding(@RequestBody HousingEntityParam housingEntityParam) {
    	String instNum = housingEntityParam.instNum();
    	String typeOfWaitlist = housingEntityParam.typeOfWaitlist();
    	if(instNum != null) {
    		if("H".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
        		return housingEntityWaitlistService.getBuildings(typeOfWaitlist,instNum);
        	}
    	}
    	return null;
    }
    
    @PostMapping(value = "/getUnits", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getUnits(@RequestBody HousingEntityParam housingEntityParam) {
    	String typeOfWaitlist = housingEntityParam.typeOfWaitlist();
    	String instNum = housingEntityParam.instNum();
    	String building = housingEntityParam.building();
    	if(instNum != null) {
        	if("H".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
        		return housingEntityWaitlistService.getUnits(typeOfWaitlist,instNum, building);
        	}
    	}

    	return null;
    }
    
    @PostMapping(value = "/getFloors", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getFloors(@RequestBody HousingEntityParam housingEntityParam) {
    	String typeOfWaitlist = housingEntityParam.typeOfWaitlist();
    	String instNum = housingEntityParam.instNum();
    	String building = housingEntityParam.building();
    	String unit = housingEntityParam.unit();
    	
    	if(instNum != null) {
    		if("H".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
        		return housingEntityWaitlistService.getFloors(typeOfWaitlist,instNum, building, unit);
        	}
    	}
    	return null;
    }
    
    @PostMapping(value = "/getTiers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getTiers(@RequestBody HousingEntityParam housingEntityParam) {
    	String typeOfWaitlist = housingEntityParam.typeOfWaitlist();
    	String instNum = housingEntityParam.instNum();
    	String building = housingEntityParam.building();
    	String unit = housingEntityParam.unit();
    	String floor = housingEntityParam.floor();
    	
    	if(instNum != null) {
        	if("H".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
        		return housingEntityWaitlistService.getTiers(typeOfWaitlist,instNum, building, unit, floor);
        	}
    	}
    	return null;
    }
    
    @PostMapping(value = "/getCells", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getCells(@RequestBody HousingEntityParam housingEntityParam) {
    	String typeOfWaitlist = housingEntityParam.typeOfWaitlist();
    	String instNum = housingEntityParam.instNum();
    	String building = housingEntityParam.building();
    	String unit = housingEntityParam.unit();
    	String floor = housingEntityParam.floor();
    	String tier = housingEntityParam.tier();
    	
    	if(instNum != null) {
        	if("H".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
        		return housingEntityWaitlistService.getCells(typeOfWaitlist, instNum, building, unit, floor, tier);
        	}
    	}
    	return null;
    }
    
    @PostMapping(value = "/getBeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getBeds(@RequestBody HousingEntityParam housingEntityParam) {
    	String typeOfWaitlist = housingEntityParam.typeOfWaitlist();
    	String instNum = housingEntityParam.instNum();
    	String building = housingEntityParam.building();
    	String unit = housingEntityParam.unit();
    	String floor = housingEntityParam.floor();
    	String tier = housingEntityParam.tier();
    	String cell = housingEntityParam.cell();
    	
    	if(instNum != null) {
        	if("H".equals(typeOfWaitlist) || "B".equals(typeOfWaitlist)) {
        		return housingEntityWaitlistService.getBeds(typeOfWaitlist, instNum, building, unit, floor, tier, cell);
        	}
    	}
    	return null;
    }
    
    @PostMapping(value = "/getHousingEntityWaitlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> getHousingEntityWaitlist(@RequestBody HousingEntityParam housingEntityParam) {
    	String typeOfWaitlist = housingEntityParam.typeOfWaitlist();
    	String queryWaitlist = housingEntityParam.queryWaitlist();
    	String movement = housingEntityParam.movement();
    	Date dateFrom = convertStringToSqlDate(housingEntityParam.dateFrom());
    	Date dateTo = convertStringToSqlDate(housingEntityParam.dateTo());
    	String instNum = housingEntityParam.instNum();
    	String building = housingEntityParam.building();
    	String unit = housingEntityParam.unit();
    	String floor = housingEntityParam.floor();
    	String tier = housingEntityParam.tier();
    	String cell = housingEntityParam.cell();
    	String bed = housingEntityParam.bed();
    	
    	if(instNum != null) {
        	return housingEntityWaitlistService.executeWaitlistQuery(instNum, building, unit, floor, tier, cell, bed, typeOfWaitlist, queryWaitlist, movement, dateFrom, dateTo);
    	}
    	
    	return null;
    }
    
    @PostMapping(value = "/getOffenderHousingAssignment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getOffenderHousingAssignment(@RequestBody HousingEntityParam housingEntityParam) {
        Integer docWLSeqNum = housingEntityParam.docWLSeqNum();
        String commitNo = housingEntityParam.commitNo();

        if (commitNo != null && docWLSeqNum != null) {
            CompletableFuture<ResponseEntity<Map<String, Object>>> receivingFuture =
                    housingEntityWaitlistService.executeHousingReceivingQuery(commitNo, docWLSeqNum);

            CompletableFuture<ResponseEntity<Map<String, Object>>> currentFuture =
                    housingEntityWaitlistService.executeHousingCurrentQuery(commitNo);

            CompletableFuture.allOf(receivingFuture, currentFuture).join();

            try {
                Map<String, Object> offenderAssignment = new HashMap<>();
                offenderAssignment.putAll(receivingFuture.get().getBody());
                offenderAssignment.putAll(currentFuture.get().getBody());
                return ResponseEntity.ok(offenderAssignment);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body(Collections.singletonMap("status", "Error: " + e.getMessage()));
            }
        }
        return null;
    }
    
    //Individual offender Assignment Screen
    
    @PostMapping(value = "/getActivityType",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object[]> getActivityTypevalues(@RequestBody String active){
    	if ("A".equals(active)) {
    		return individualOffenderMovService.getActiveActivityTypes("A");
    	}
		return null;   	
    }
    
    @PostMapping(value = "/getArrivingAndDepart",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object[]> getLocationVals(@RequestBody String instNum){
    	String currentUserId = getCurrentUserId();
        String userLocation = userRepo.findInstNumByUserId(currentUserId); 
    	if(userLocation != null) {
    		return individualOffenderMovService.getActiveLocationVals(userLocation);
    	}
		return null;   	
    }    
    
    @PostMapping(value = "/getOffenderMovDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getOffenderMov(@RequestBody String sbiNumber) {
        if (sbiNumber != null) {
            String userId = getCurrentUserId();
            String commitNo = inmateService.validCommitNo(sbiNumber);

            CompletableFuture<ResponseEntity<Map<String, Object>>> currentLoc = CompletableFuture.supplyAsync(() -> individualOffenderMovService.executeCurrentLoc(commitNo, userId));
            CompletableFuture<ResponseEntity<Map<String, Object>>> movDetail = CompletableFuture.supplyAsync(() -> individualOffenderMovService.executeMovementDet(commitNo, userId));

            return currentLoc.thenCombine(movDetail, (locResponse, movResponse) -> {
                if (!locResponse.getStatusCode().is2xxSuccessful() || !movResponse.getStatusCode().is2xxSuccessful()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Collections.singletonMap("status", "An error occurred"));
                }      
                String instNum = userRepo.findInstNumByUserId(userId);                
                String reservedLoc = individualOffenderMovService.getReservedLoc(commitNo, instNum);

                Map<String, Object> combinedResponse = new HashMap<>();
                combinedResponse.putAll(locResponse.getBody());
                combinedResponse.putAll(movResponse.getBody());
                combinedResponse.put("reservedLocation", reservedLoc);
                
                return ResponseEntity.ok(combinedResponse);
            });
        }
        return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(Collections.singletonMap("status", "Invalid SBI number")));
    }
    
    @PostMapping(value = "/SaveIndOffMov", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> indOffenderMov(@RequestBody IndividualOffMov individualOffMov) {
        if (Objects.isNull(individualOffMov.getCommitNo()) || Objects.isNull(individualOffMov.getInstNum())) {
        	String sbiNumber = individualOffMov.getSbiNumber();
        	String commitNo = inmateService.validCommitNo(sbiNumber);
        	String userId = getCurrentUserId();
        	String instNum = userRepo.findInstNumByUserId(userId);
        	
        	individualOffMov.setCommitNo(commitNo);
        	individualOffMov.setInstNum(instNum);
        }
        try {
            String action = individualOffenderMovService.checkInsertOrUpdate(individualOffMov);
            List<IndividualOffMov> individualOffMovList = Collections.singletonList(individualOffMov);

            if ("INSERT".equals(action)) {
                return individualOffenderMovService.callSpMovDetInsertObj(individualOffMovList);
            } else if ("UPDATE".equals(action)) {
                return individualOffenderMovService.callSpMovDetUpdateObj(individualOffMovList);
            } 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("status", "Invalid action"));

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("status", "Error: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/ScheduledActivities",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getScheduledActivity(@RequestBody String sbiNumber){    	
    	String commitNo = inmateService.validCommitNo(sbiNumber);
    	if(commitNo != null) {
    		return individualOffenderMovService.executeScheduledActivity(commitNo);
    	}
    	return null;
    }
    
    //Mass Offender Movement Screen
    @PostMapping(value = "/getInstitutionMOAM",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object[]> getInstitutionVals(@RequestBody String instNum){
    	String currentUserId = getCurrentUserId();
    	if(currentUserId != null) {
    		return institutionRepo.findInstitutionForMOAM(currentUserId);
    	}
		return null;   	
    }
    
    @PostMapping(value = "/getBuildingMOAM",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object[]> getActiveBld(@RequestBody String instNumber){
    	if(instNumber != null) {
    		return individualOffenderMovService.getActiveLocationVals(instNumber);
    	}
		return null;   	
    }
    
    @PostMapping(value = "/getMassMovData",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> getMassMovData(@RequestBody MassOffenderDetail massOffenderDetail){
    	String logInOut = massOffenderDetail.logInOut();
    	String countLocCode = massOffenderDetail.countLocCode();
    	String scheduleFlag = massOffenderDetail.scheduleFlag();
    	String instNum = massOffenderDetail.instNum();
    	
    	System.out.println("instNum xyz : " + instNum);
    	System.out.println("countLocCode xyz : " + countLocCode);
    	
    	System.out.println("step 01");
    	//List<Object[]> locationParams = instLocRepo.findLocParams(instNum, countLocCode);
    	List<Object[]> locationParams = null;
    	if(countLocCode.equals("")) {
    		locationParams = instLocRepo.findLocParams(instNum);
    	} else {
    		locationParams = instLocRepo.findLocParamByLoc(instNum, countLocCode);
    	}
    	
    	System.out.println("step 02");
    	
    	String bldNum = (String) locationParams.get(0)[0];
    	String unitId = (String) locationParams.get(0)[1];
    	String floor = (String) locationParams.get(0)[2];
        String tier = (String) locationParams.get(0)[3];
        String cell = (String) locationParams.get(0)[4];
        String bed = (String) locationParams.get(0)[5];
        
        System.out.println("step 03");
        
        String queryType = massOffenderDetail.queryType();
        
        System.out.println("step 04");
        
        System.out.println(bldNum+" "+unitId+" "+floor+" "+tier+" "+cell+" "+bed);
        
        if(instNum != null && countLocCode != null && scheduleFlag != null && logInOut != null) {
    		return massOffenderMovService.executeMassMovementDet(logInOut, countLocCode, scheduleFlag, instNum, bldNum, unitId, floor, tier, cell, bed, queryType);
    	}
    	
		return null;   	
    }
    
    @PostMapping(value = "/SaveMassOffMov", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> massOffenderMov(@RequestBody MassOffenderMovRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");

        try {
            String logInOut = request.logInOut();
            String countLocCode = request.countLocCode();
            List<MassOffenderMovement> saveData = request.saveData();
            
            System.out.println("kamal check " + saveData.get(0).getCountLocCodeFrom());
            
            countLocCode = saveData.get(0).getCountLocCodeFrom();
            
            //System.out.println(saveData.);
            
            System.out.println("Testing logInOut : " + logInOut);
            System.out.println("Testing countLocCode : " + countLocCode);

            return massOffenderMovService.callSpMovMmvUpdateObj(logInOut, countLocCode, "", saveData);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Failed to process data");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }    
    
    //Emergency attention
    @PostMapping(value = "/EmergencyAttentionData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getEmergencyAttention(@RequestBody EmergencyAttentionParams params) {
        String commitNo = params.commitNo();
        String emerSeqNum = params.emerSeqNum();
        
        if (commitNo != null) {
            try {
                CompletableFuture<ResponseEntity<Map<String, Object>>> inmateDetailsFuture = 
                    CompletableFuture.supplyAsync(() -> emergencyAttentionService.executeGetInmateDet(commitNo));
                
                CompletableFuture<ResponseEntity<Map<String, Object>>> emergencyDetailsFuture = 
                    CompletableFuture.supplyAsync(() -> emergencyAttentionService.executeSpQuery(commitNo, emerSeqNum));

                CompletableFuture<Map<String, Object>> combinedResult = inmateDetailsFuture.thenCombine(
                    emergencyDetailsFuture,
                    (inmateDetailsResponse, emergencyDetailsResponse) -> {
                        Map<String, Object> combinedMap = new HashMap<>();
                        combinedMap.put("inmateDetails", inmateDetailsResponse.getBody());
                        combinedMap.put("emergencyDetails", emergencyDetailsResponse.getBody());
                        return combinedMap;
                    });

                Map<String, Object> response = combinedResult.join();
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                logger.error("Error executing emergency attention query: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body(Collections.singletonMap("status", "An error occurred while processing your request. Please try again later."));
            }
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("status", "Invalid request: commitNo is required."));
    }
    
    @PostMapping(value = "/SaveEmgyAttention", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> emgyAttData(@RequestBody List<EmergencyAttention> emergencyAttentionList) {
        try {
            for (EmergencyAttention emergencyAttention : emergencyAttentionList) {
                if (emergencyAttention.getEmergencySeqNum() == null) {
                    Integer emergencySeqNum = emergencyAttentionService.getNextEmrgSeqNum();
                    emergencyAttention.setEmergencySeqNum(emergencySeqNum);
                    return emergencyAttentionService.callSpInsertOmnet(emergencyAttentionList);
                } else {
                	return emergencyAttentionService.callSpUpdateOmnet(emergencyAttentionList);
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("status", "Error: One of the parameters is absent"));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("status", "Error: " + e.getMessage()));
        }
    }
    
    
//////////////////////////////////////////////////////////////////////////////////////////
    
	@GetMapping(value="CSMNOSC")
	public List<Map<String, Object>> NOSC(String ivc_commit_no){
//	String ivc_commit_no;
//	ivc_commit_no = inmateService.validCommitNo(ivc_sbi_no);
	List<Map<String, Object>> CSMTable=caseNotesService.CaseNoteProcedure(ivc_commit_no);
	return CSMTable;
	}
	
	@GetMapping(value="CSMNOSChistory")
	public List<Map<String, Object>> NOSCHistory(String ivc_sbi_no){
	//ivc_sbi_no="00249414";
	List<Map<String, Object>> CSMTableH=caseNotesService.CaseNoteProcedureHistory(ivc_sbi_no);
	return CSMTableH;
	}
	
	@GetMapping(value="CaseNotesData")
	public List<Map<String, Object>> NOSCMainQuery(String ivc_commit_no, Date idt_cont_from_dt, Date idt_cont_to_dt,String ivc_relationship_code, String ivc_results, String ivc_cont_tp_cd){
	//  ivc_commit_no="0053369";
//	String ivc_commit_no = inmateService.validCommitNo(ivc_sbi_no);
	System.out.println(" Testing ");
	System.out.println(" getting values "+ivc_commit_no+" "+idt_cont_from_dt+" "+idt_cont_to_dt+" "+ivc_relationship_code+" "+ivc_results+" "+ivc_cont_tp_cd);
	System.out.println(" Testing ");
	List<Map<String, Object>> CSMTableM=caseNotesService.CaseNoteProcedureMainquery(ivc_commit_no, idt_cont_from_dt, idt_cont_to_dt, ivc_relationship_code, ivc_results, ivc_cont_tp_cd);
	return CSMTableM;
	}
	
	@GetMapping("/ContactPersonvalue")
	public ResponseEntity<Map<String, Object>> ContactPersonvalue(String userId,String ivc_commit_no, String inu_csmn_cont_seq_no) {
	Map<String, Object> response = new HashMap<>();
	List<Map<String, Object>> CSMTableCP=caseNotesService.CSMContactperson(ivc_commit_no, inu_csmn_cont_seq_no);
	List<Object[]> CPCodeDesc=Relation.findRelationshipCodesAndDescriptionsByStatus("A");
	response.put("CSMTableCP", CSMTableCP);		
	response.put("CPCodeDesc", CPCodeDesc);	
	
	return ResponseEntity.ok(response);
	}
	
	
	@PostMapping(value="CSM_Amendment_VALUES", consumes = "application/json")
	public void CSMUpdateamendment(@RequestBody List<Amendment> DetailsvaluesfoCSMAmendment) throws SQLException {
	System.out.println("CSM_Amendment_VALUES="+ DetailsvaluesfoCSMAmendment.get(0).getCOMMIT_NO()+" "+ DetailsvaluesfoCSMAmendment.get(0).getCsmn_cont_comnt_amendment()+" "+ DetailsvaluesfoCSMAmendment.get(0).getCSMN_CONT_SEQ_NO()); 
	caseNotesService.CSMAmendment(DetailsvaluesfoCSMAmendment);
	}
	
	@GetMapping("/getNextContractSequence")
	public Map<String, Object> CSMCaseNotesvalues(String parameter){
	Map<String, Object> CSMValues = new HashMap<>();
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	Object details = authentication.getDetails();
	Map<?, ?> detailsMap = (Map<?, ?>) details;
	String p_user_id = (String) detailsMap.get("userId");
	Integer Seqval=caseNotesService.getNContSeqNumforCI();
	Integer SeqvalCP=caseNotesService.getNContSeqNumforContactPerson();
	String commitNo = inmateService.validCommitNo(parameter);
	//  String fullName = cs.UserNameLFMS(p_user_id);
	Optional<String> fullName = userRepo.findConcatenatedNameByLFMS(p_user_id);
	CSMValues.put("p_user_id", p_user_id);
	CSMValues.put("Seqval", Seqval);
	CSMValues.put("SeqvalCP", SeqvalCP);
	CSMValues.put("commitNo", commitNo);
	CSMValues.put("fullName", fullName);
	return CSMValues;
	}
	
	
	@PostMapping(value="CSM_Contact_Information", consumes = "application/json")
	public void CSMUpdateContactInformation(@RequestBody List<ContactInformationInsert> CSMCI) throws SQLException {
	//  System.out.println("CSMCI"+CSMCI);
	caseNotesService.CSMContactInformationInsert(CSMCI);
	}
	
	
	@PostMapping(value="CSM_Contact_Person", consumes = "application/json")
	public void CSMInsertContactPerson(@RequestBody List<ContactPersonInsert>  CSMCPI) throws SQLException {
	System.out.println("CSMCI="+CSMCPI.get(0).getCSMN_PRSN_SEQ_NO()+" "+CSMCPI.get(0).getCSMN_CONT_SEQ_NO());
	caseNotesService.CSMContactPersonInsert(CSMCPI);
	}
	    
	@GetMapping("/CSM_DROPDOWN")
	public ResponseEntity<Map<String, Object>> caseNotesDropdown() {
	Map<String, Object> responseDropdown = new HashMap<>();
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	Object details = authentication.getDetails();
	Map<?, ?> detailsMap = (Map<?, ?>) details;
	String p_user_id = (String) detailsMap.get("userId");
	List<Object[]> relationshipDropdown=Relation.findRelationshipCodesAndDescriptionsByStatus("A");
	List<Object[]> contacttypeDropdown=contactTpMstRepository.findContactCodesAndDescriptionsByStatus("A");
	List<Object[]> resonmstDropdown=reasonMstRepository.findReasonCodesAndDescriptionsByStatus("A");
	Integer Seqval=caseNotesService.getNContSeqNumforCI();
	Integer SeqvalCP=caseNotesService.getNContSeqNumforContactPerson();
	Optional<String> fullName = userRepo.findConcatenatedNameByLFMS(p_user_id);
	responseDropdown.put("relationshipDropdown", relationshipDropdown);	
	responseDropdown.put("contacttypeDropdown", contacttypeDropdown);
	responseDropdown.put("resonmstDropdown", resonmstDropdown);
	responseDropdown.put("Seqval", Seqval);
	responseDropdown.put("SeqvalCP", SeqvalCP);
	responseDropdown.put("p_user_id", p_user_id);
	responseDropdown.put("fullName", fullName);
	return ResponseEntity.ok(responseDropdown);
	}
	
	@GetMapping("/getCurrentCommitNo")
	public String gettheCurrentCommitNo(String sbiNumber){
		String commitNo = inmateService.validCommitNo(sbiNumber);
		return commitNo;
	}
	
	@PostMapping(value="updateFlagDateISC", consumes = "application/json")
	public void updateISCDateFlag(@RequestBody List<ISCDateApprovedCaseNotes>  dateISCCSM) throws SQLException {
	System.out.println("CSMCI="+dateISCCSM.get(0).getCOMMIT_NO()+" "+dateISCCSM.get(0).getIsc_approved_flag()+" "+dateISCCSM.get(0).getIsc_requested_date_box());
	caseNotesService.CSMUpdateISCTranfer(dateISCCSM);
	}
	
	}

	

