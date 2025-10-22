package com.omnet.cnt.Controller;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omnet.cnt.Service.SentenceCalc;
import com.omnet.cnt.classes.SentenceLevelDetails;
import com.omnet.cnt.classes.SentenceOrderDetails;

@RestController
public class DocketsAndChargesController {
	
	//@Autowired
	private final SentenceCalc sentenceCalc;
	
	public DocketsAndChargesController(SentenceCalc sentenceCalc) {
        this.sentenceCalc = sentenceCalc;
    }
	
	@GetMapping("/getDocketsCommitNo")
	public String getDocketsCommitNo(@RequestParam String sbiNo){
		return sentenceCalc.getCommitNo(sbiNo);
	}
	
	@GetMapping("/getSentenceTo")
	public List<Map<String, Object>> getSentenceTo() {
		return sentenceCalc.getSentenceTo();
	}
	
	@GetMapping("/getSentenceType")
	public List<Map<String, Object>> getSentenceType() {
		return sentenceCalc.getSentenceType();
	}
	
	@GetMapping("/getHeldAt")
	public List<Map<String, Object>> getHeldAt() {
		return sentenceCalc.getHeldAt();
	}
	
	@GetMapping("/getReasons")
	public List<Map<String, Object>> getReasons() {
		return sentenceCalc.getReasons();
	}
	
	@GetMapping("getConditions")
	public List<Map<String, Object>> getConditions(){
		return sentenceCalc.getConditions();
	}
	
	@GetMapping("getTreatments")
	public List<Map<String, Object>> getTreatments(){
		return sentenceCalc.getTreatments();
	}

	@GetMapping("/getSentenceOrder")
	public ResponseEntity<Map<String, Object>> getSentenceOrder(
			@RequestParam String commitNo, 
			@RequestParam String sohSeqNum, 
			@RequestParam String sohCaseNum, 
			@RequestParam String sohCraNum, 
			@RequestParam String instNum
		) {
		try {
			Map<String, Object> results = sentenceCalc.getSentenceOrder(commitNo, sohSeqNum, sohCaseNum, sohCraNum, instNum);
			return ResponseEntity.ok(results);
		} catch (Exception ex) {
	        // Log the error
	        System.err.println("Error fetching property tracking header: " + ex.getMessage());
	        ex.printStackTrace();
	        // Return error response
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Failed to retrieve property tracking data.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}
	
	@GetMapping("/getNewChargesList")
	public ResponseEntity<List<SentenceOrderDetails>> getNewChargesList(@RequestParam String commitNo) {
			return ResponseEntity.ok(sentenceCalc.getNewChargesList(commitNo));
	}
	
	@GetMapping("/getSentenceOrderModifyDetails")
	public ResponseEntity<Map<String, Object>> getSentenceOrderModifyDetails(@RequestParam String commitNo) {
			return ResponseEntity.ok(sentenceCalc.getSentenceOrderModifyDetails(commitNo));
	}
	
	@GetMapping("/getSentenceOrderViewDetails")
	public ResponseEntity<List<SentenceOrderDetails>> getSentenceOrderViewDetails(@RequestParam String commitNo) {
		List<SentenceOrderDetails> results = sentenceCalc.getSentenceOrderViewDetails(commitNo);
			return ResponseEntity.ok(results);
	}
	
	@GetMapping("/getSentenceLevelDetails")
	public ResponseEntity<Map<String, Object>> getSentenceLevelDetails(@RequestParam String sohSeqNum) {
		return ResponseEntity.ok(sentenceCalc.getSentenceLevelDetails(sohSeqNum));
	}
	
	@PostMapping("/insUpdSentenceOrder")
	public ResponseEntity<Map<String, Object>> insUpdSentenceOrder(@RequestBody Map<String, List<Map<String, Object>>> requestData) {
		System.out.println("getting insUpd req");
		
		try {
			List<Map<String, Object>> modifiedRecords = requestData.get("modify");
	        List<Map<String, Object>> newRecords = requestData.get("new");
	        List<Map<String, Object>> lvlDetails = requestData.get("levelDetails");
	        
	        // List to store generated sequence numbers for new records
	        List<BigDecimal> generatedSeqNums = new ArrayList<>();
	        
	        if (modifiedRecords != null && !modifiedRecords.isEmpty()) {
	        	List<SentenceOrderDetails> sntcOrders = mapToSentenceOrderDetails(modifiedRecords);
	        	System.out.println("Maped to POJO");
	        	sentenceCalc.updateSentenceOrder(sntcOrders);
	        }

	        if (newRecords != null && !newRecords.isEmpty()) {
	        	List<SentenceOrderDetails> sntcOrders = mapToSentenceOrderDetails(newRecords);
	        	System.out.println("Maped to POJO");
	        	// Capture the generated sequence numbers from insert
	        	generatedSeqNums = sentenceCalc.insertSentenceOrder(sntcOrders);
	        	System.out.println("Received " + generatedSeqNums.size() + " generated sequence numbers from insert");
	        }
	        
	        
	        if (lvlDetails != null && !lvlDetails.isEmpty()) {
	        	List<SentenceLevelDetails> lvlDetails_toIns = new ArrayList<>();
		        List<SentenceLevelDetails> lvlDetails_toUpd = new ArrayList<>();
		        
	        	System.out.println("Level Details ::: "+lvlDetails.size());
	        	List<SentenceLevelDetails> sntcLvlDetails = mapToSentenceLevelDetails(lvlDetails);
	        	System.out.println("Mapped to POJO");
	        	
	        	for(SentenceLevelDetails lvlDetail: sntcLvlDetails) {
	        		
	        		if(lvlDetail.getSOCC_SEQ_NUM() == null) {
	        			/*if(lvlDetail.getSOH_SEQ_NUM() == null)
	        				lvlDetail.setSOH_SEQ_NUM(toBigDecimal(sentenceCalc.getSohSeqNum()));*/
	        			
	        			lvlDetails_toIns.add(lvlDetail);
	        		}else {
	        			lvlDetails_toUpd.add(lvlDetail);
	        		}
	        		System.out.println("***SOCC && SOH seq nos: "+ lvlDetail.getSOH_SEQ_NUM() + ", "+ lvlDetail.getSOCC_SEQ_NUM());
	        	}
	        	if(!lvlDetails_toIns.isEmpty()) {
	        		System.out.println("Inserting Level Details");
	        		sentenceCalc.insertSentenceLevelDetails(lvlDetails_toIns);
	        	}
	        	if(!lvlDetails_toUpd.isEmpty()) {
	        		sentenceCalc.updateSentenceLevelDetails(lvlDetails_toUpd);
	        	}
	        	
	        }

            System.out.println("Returning InsUpd Response");
            
            // Build response with generated sequence numbers
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sentence orders inserted/updated successfully.");
            response.put("generatedSeqNums", generatedSeqNums);
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error processing sentence orders: " + ex.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
	}
	
	@DeleteMapping("/deleteSentenceLvlDetail")
	public ResponseEntity<String> insUpdSentenceLevelDetails(@RequestBody List<Map<String, Object>> recordsToDel){
		try {
			List<SentenceLevelDetails> toDelete = mapToSentenceLevelDetails(recordsToDel);
			sentenceCalc.deleteSentenceLevelDetails(toDelete);
			
			System.out.println("Returning Delete Response");
            return ResponseEntity.ok("Sentence level deleted successfully.");
		} catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Error processing sentence orders: " + ex.getMessage());
        }
	}
	
	public static List<SentenceLevelDetails> mapToSentenceLevelDetails(List<Map<String, Object>> lvlDetails){
		List<SentenceLevelDetails> mapedLvlDetailsLst = new ArrayList<>();
		System.out.println("=== Starting mapToSentenceLevelDetails ===");
	    System.out.println("Total objects to process: " + lvlDetails.size());
	    
	    for (int i =0; i<lvlDetails.size(); i++) {
	    	Map<String, Object> detail = lvlDetails.get(i);
	    	System.out.println("\n--- Processing object " + (i + 1) + " of " + lvlDetails.size() + " ---");
	    	System.out.println("Available keys in object " + (i + 1) + ": " + detail.keySet());
	    	
	    	SentenceLevelDetails sntcLvlDetail = new SentenceLevelDetails();
	    	
	    	try {
	    		
	    		sntcLvlDetail.setCB_SELECT(safeStringCast(detail.get("CB_SELECT"), "CB_SELECT", i+1));
	    		sntcLvlDetail.setSOH_SEQ_NUM(toBigDecimal(detail.get("SOH_SEQ_NUM")));
	    		sntcLvlDetail.setSOCC_SEQ_NUM(toBigDecimal(detail.get("SOCC_SEQ_NUM"))); // <- for inserting do not need to pass. Procedure handles new ID.
	    		sntcLvlDetail.setCONDITION_CODE(safeStringCast(detail.get("CONDITION_CODE"), "CONDITION_CODE", i+1));
	    		sntcLvlDetail.setSENTENCE_LEVEL(safeStringCast(detail.get("SENTENCE_LEVEL"), "SENTENCE_LEVEL", i+1));
	    		sntcLvlDetail.setLENGTH_YY(toBigDecimal(detail.get("LENGTH_YY")));
	    		sntcLvlDetail.setLENGTH_MM(toBigDecimal(detail.get("LENGTH_MM")));
	    		sntcLvlDetail.setLENGTH_DD(toBigDecimal(detail.get("LENGTH_DD")));
	    		sntcLvlDetail.setHELD_AT(safeStringCast(detail.get("HELD_AT"), "HELD_AT", i+1));
	    		sntcLvlDetail.setTREATMENT(safeStringCast(detail.get("TREATMENT"), "TREATMENT", i+1));
	    		sntcLvlDetail.setSTATUS(safeStringCast(detail.get("STATUS"), "STATUS", i+1));	// <- for inserting do not need to pass. Procedure sets to 'A'
	    		sntcLvlDetail.setSOCC_COND_ORDER_NUM(toBigDecimal(detail.get("SOCC_COND_ORDER_NUM")));	// <- Increment to next value from list of vals.
	    		sntcLvlDetail.setSOCC_OPERATOR(safeStringCast(detail.get("SOCC_OPERATOR"), "SOCC_OPERATOR", i+1));
	    		sntcLvlDetail.setCOMMIT_NO(safeStringCast(detail.get("COMMIT_NO"), "COMMIT_NO", i+1));
	    		sntcLvlDetail.setCASE_NUM(safeStringCast(detail.get("CASE_NUM"), "CASE_NUM", i+1));
	    		sntcLvlDetail.setCRA_NUM(safeStringCast(detail.get("CRA_NUM"), "CRA_NUM", i+1));
	    		sntcLvlDetail.setSNTC_LVL_CD(safeStringCast(detail.get("SNTC_LVL_CD"), "SNTC_LVL_CD", i+1));
	    		/*sntcLvlDetail.setSNTC_LVL_SEQ_NUM(toBigDecimal(detail.get("SNTC_LVL_SEQ_NUM")));
	    		sntcLvlDetail.setSNTC_ORD_SEQ_NUM(toBigDecimal(detail.get("SNTC_ORD_SEQ_NUM")));
	    		sntcLvlDetail.setSOCC_RELEASE_DATE(safeDateCast(detail.get("SOCC_RELEASE_DATE"), "SOCC_RELEASE_DATE", i+1));
	    		sntcLvlDetail.setSOCC_RELEASE_TIME(toBigDecimal(detail.get("SOCC_RELEASE_TIME")));
	    		sntcLvlDetail.setSOCC_COMPL_FLAG(safeStringCast(detail.get("SOCC_COMPL_FLAG"), "SOCC_COMPL_FLAG", i+1));
	    		sntcLvlDetail.setSOCC_COMPL_USERID(safeStringCast(detail.get("SOCC_COMPL_USERID"), "SOCC_COMPL_USERID", i+1));
	    		sntcLvlDetail.setSOCC_COMPL_DATE(safeDateCast(detail.get("SOCC_COMPL_DATE"), "SOCC_COMPL_DATE", i+1));
	    		sntcLvlDetail.setSOCC_COMPL_TIME(safeStringCast(detail.get("SOCC_COMPL_TIME"), "SOCC_COMPL_TIME", i+1));*/
	    		
	    	} catch (Exception e) {
	            System.err.println("ERROR: Exception occurred while processing object " + (i + 1) + ": " + e.getMessage());
	            e.printStackTrace();
	            throw new RuntimeException("Failed to process object " + (i + 1) + " in mapToSentenceOrderDetails", e);
	        }
	    	mapedLvlDetailsLst.add(sntcLvlDetail);
	    }
	    
	    System.out.println("=== Completed mapToSentenceOrderDetails ===");
	    System.out.println("Successfully processed " + mapedLvlDetailsLst.size() + " objects");
	    return mapedLvlDetailsLst;
	}
	
	public static List<SentenceOrderDetails> mapToSentenceOrderDetails(List<Map<String, Object>> dataList) {
	    List<SentenceOrderDetails> detailsList = new ArrayList<>();
	    System.out.println("=== Starting mapToSentenceOrderDetails ===");
	    System.out.println("Total objects to process: " + dataList.size());
	    
	    for (int i = 0; i < dataList.size(); i++) {
	        Map<String, Object> data = dataList.get(i);
	        System.out.println("\n--- Processing object " + (i + 1) + " of " + dataList.size() + " ---");
	        
	        // Log the keys available in this object
	        System.out.println("Available keys in object " + (i + 1) + ": " + data.keySet());
	        
	        SentenceOrderDetails details = new SentenceOrderDetails();
	        
	        try {
	            // Basic fields with enhanced debugging
	            System.out.println("Setting SOH_SEQ_NUM...");
	            logFieldDebug("SOH_SEQ_NUM", data.get("SOH_SEQ_NUM"), i + 1);
	            details.setSohSeqNum(toBigDecimal(data.get("SOH_SEQ_NUM")));
	            
	            System.out.println("Setting COMMIT_NO...");
	            logFieldDebug("COMMIT_NO", data.get("COMMIT_NO"), i + 1);
	            details.setCommitNo(safeStringCast(data.get("COMMIT_NO"), "COMMIT_NO", i + 1));
	            
	            System.out.println("Setting SOH_CAL_SEQ_NUM...");
	            logFieldDebug("SOH_CAL_SEQ_NUM", data.get("SOH_CAL_SEQ_NUM"), i + 1);
	            details.setSohCalSeqNum(toBigDecimal(data.get("SOH_CAL_SEQ_NUM")));
	            
	            System.out.println("Setting CASE_NUM...");
	            logFieldDebug("CASE_NUM", data.get("CASE_NUM"), i + 1);
	            details.setCaseNum(safeStringCast(data.get("CASE_NUM"), "CASE_NUM", i + 1));
	            
	            System.out.println("Setting CRA_NUM...");
	            logFieldDebug("CRA_NUM", data.get("CRA_NUM"), i + 1);
	            details.setCraNum(safeStringCast(data.get("CRA_NUM"), "CRA_NUM", i + 1));
	            
	            System.out.println("Setting CHARGE_NUM...");
	            logFieldDebug("CHARGE_NUM", data.get("CHARGE_NUM"), i + 1);
	            details.setChargeNum(safeStringCast(data.get("CHARGE_NUM"), "CHARGE_NUM", i + 1));
	            
	            System.out.println("Setting SOH_CHARGE_DESC...");
	            logFieldDebug("SOH_CHARGE_DESC/SHORT_DESC", data.get("SHORT_DESC"), i + 1);
	            details.setChargeDesc(safeStringCast(data.get("SHORT_DESC"), "SOH_CHARGE_DESC/SHORT_DESC", i + 1));
	            
	            System.out.println("Setting COURT_CODE...");
	            logFieldDebug("COURT_CODE", data.get("COURT_CODE"), i + 1);
	            details.setCourtCode(safeStringCast(data.get("COURT_CODE"), "COURT_CODE", i + 1));
	            
	            System.out.println("Setting JUDGE_CODE...");
	            logFieldDebug("JUDGE_CODE", data.get("JUDGE_CODE"), i + 1);
	            details.setJudgeCode(safeStringCast(data.get("JUDGE_CODE"), "JUDGE_CODE", i + 1));
	            
	            System.out.println("Setting SOH_SENT_DATE...");
	            logFieldDebug("SOH_SENT_DATE", data.get("SOH_SENT_DATE"), i + 1);
	            details.setSohSentDate(toDate(data.get("SOH_SENT_DATE").toString()));
	            
	            System.out.println("Setting SOH_SNTC_LVL_CD...");
	            logFieldDebug("SOH_SNTC_LVL_CD", data.get("SOH_SNTC_LVL_CD"), i + 1);
	            details.setSohSntcLvlCd(safeStringCast(data.get("SOH_SNTC_LVL_CD"), "SOH_SNTC_LVL_CD", i + 1));
	            
	            System.out.println("Setting SOH_SNTC_LVL_SEQ_NUM...");
	            logFieldDebug("SOH_SNTC_LVL_SEQ_NUM", data.get("SOH_SNTC_LVL_SEQ_NUM"), i + 1);
	            details.setSohSntcLvlSeqNum(toBigDecimal(data.get("SOH_SNTC_LVL_SEQ_NUM")));
	            
	            System.out.println("Setting SOH_SNTC_ORD_SEQ_NUM...");
	            logFieldDebug("SOH_SNTC_ORD_SEQ_NUM", data.get("SOH_SNTC_ORD_SEQ_NUM"), i + 1);
	            details.setSohSntcOrdSeqNum(toBigDecimal(data.get("SOH_SNTC_ORD_SEQ_NUM")));
	            
	            System.out.println("Setting ORDER_TYPE...");
	            logFieldDebug("ORDER_TYPE", data.get("ORDER_TYPE"), i + 1);
	            details.setOrderType(safeStringCast(data.get("ORDER_TYPE"), "ORDER_TYPE", i + 1));
	            
	            // Length fields
	            System.out.println("Setting LENGTH_YY...");
	            logFieldDebug("LENGTH_YY", data.get("LENGTH_YY"), i + 1);
	            details.setLengthYy(toBigDecimal(data.get("LENGTH_YY")));
	            
	            System.out.println("Setting LENGTH_MM...");
	            logFieldDebug("LENGTH_MM", data.get("LENGTH_MM"), i + 1);
	            details.setLengthMm(toBigDecimal(data.get("LENGTH_MM")));
	            
	            System.out.println("Setting LENGTH_DD...");
	            logFieldDebug("LENGTH_DD", data.get("LENGTH_DD"), i + 1);
	            details.setLengthDd(toBigDecimal(data.get("LENGTH_DD")));
	            
	            System.out.println("Setting EFFECTIVE_DATE...");
	            logFieldDebug("EFFECTIVE_DATE", data.get("EFFECTIVE_DATE"), i + 1);
	            details.setEffectiveDate(toDate(data.get("EFFECTIVE_DATE").toString()));
	            logFieldDebug("EFFECTIVE_DATE", details.getEffectiveDate(), i + 1);
	            
	            System.out.println("Setting HELD_AT...");
	            logFieldDebug("HELD_AT", data.get("HELD_AT"), i + 1);
	            details.setHeldAt(safeStringCast(data.get("HELD_AT"), "HELD_AT", i + 1));
	            
	            // Jail credit fields
	            System.out.println("Setting JAIL_CREDIT_YY...");
	            logFieldDebug("JAIL_CREDIT_YY", data.get("JAIL_CREDIT_YY"), i + 1);
	            details.setJailCreditYy(toBigDecimal(data.get("JAIL_CREDIT_YY")));
	            
	            System.out.println("Setting JAIL_CREDIT_MM...");
	            logFieldDebug("JAIL_CREDIT_MM", data.get("JAIL_CREDIT_MM"), i + 1);
	            details.setJailCreditMm(toBigDecimal(data.get("JAIL_CREDIT_MM")));
	            
	            System.out.println("Setting JAIL_CREDIT_DD...");
	            logFieldDebug("JAIL_CREDIT_DD", data.get("JAIL_CREDIT_DD"), i + 1);
	            details.setJailCreditDd(toBigDecimal(data.get("JAIL_CREDIT_DD")));
	            
	            // Min mandatory fields
	            System.out.println("Setting MIN_MAND_YY...");
	            logFieldDebug("MIN_MAND_YY", data.get("MIN_MAND_YY"), i + 1);
	            details.setMinMandYy(toBigDecimal(data.get("MIN_MAND_YY")));
	            
	            System.out.println("Setting MIN_MAND_MM...");
	            logFieldDebug("MIN_MAND_MM", data.get("MIN_MAND_MM"), i + 1);
	            details.setMinMandMm(toBigDecimal(data.get("MIN_MAND_MM")));
	            
	            System.out.println("Setting MIN_MAND_DD...");
	            logFieldDebug("MIN_MAND_DD", data.get("MIN_MAND_DD"), i + 1);
	            details.setMinMandDd(toBigDecimal(data.get("MIN_MAND_DD")));
	            
	            // Absolute length fields
	            System.out.println("Setting ABS_LEN_YY...");
	            logFieldDebug("ABS_LEN_YY", data.get("ABS_LEN_YY"), i + 1);
	            details.setAbsLenYy(toBigDecimal(data.get("ABS_LEN_YY")));
	            
	            System.out.println("Setting ABS_LEN_MM...");
	            logFieldDebug("ABS_LEN_MM", data.get("ABS_LEN_MM"), i + 1);
	            details.setAbsLenMm(toBigDecimal(data.get("ABS_LEN_MM")));
	            
	            System.out.println("Setting ABS_LEN_DD...");
	            logFieldDebug("ABS_LEN_DD", data.get("ABS_LEN_DD"), i + 1);
	            details.setAbsLenDd(toBigDecimal(data.get("ABS_LEN_DD")));
	            
	            // Boolean fields with enhanced debugging
	            System.out.println("Setting DEFERRED_FLAG...");
	            logFieldDebug("DEFERRED_FLAG", data.get("DEFERRED_FLAG"), i + 1);
	            details.setDeferredFlag(safeBooleanToString(data.get("DEFERRED_FLAG"), "DEFERRED_FLAG", i + 1));
	            
	            System.out.println("Setting DEFERRED_DATE...");
	            logFieldDebug("DEFERRED_DATE", data.get("DEFERRED_DATE"), i + 1);
	            details.setDeferredDate(toDate(data.get("DEFERRED_DATE").toString()));
	            
	            System.out.println("Setting TIS_NTIS...");
	            logFieldDebug("TIS_NTIS", data.get("TIS_NTIS"), i + 1);
	            details.setTisNtis(safeStringCast(data.get("TIS_NTIS"), "TIS_NTIS", i + 1));
	            
	            System.out.println("Setting SENTENCE_TYPE...");
	            logFieldDebug("SENTENCE_TYPE", data.get("SENTENCE_TYPE"), i + 1);
	            details.setSentenceType(safeStringCast(data.get("SENTENCE_TYPE"), "SENTENCE_TYPE", i + 1));
	            
	            System.out.println("Setting FLAG_4204K...");
	            logFieldDebug("FLAG_4204K", data.get("FLAG_4204K"), i + 1);
	            details.setFlag4204k(safeBooleanToString(data.get("FLAG_4204K"), "FLAG_4204K", i + 1));
	            
	            // Continue with remaining fields using safe casting
	            System.out.println("Setting SOH_STATUTE_JURISDICTION...");
	            logFieldDebug("SOH_STATUTE_JURISDICTION", data.get("SOH_STATUTE_JURISDICTION"), i + 1);
	            details.setSohStatuteJurisdiction(safeStringCast(data.get("SOH_STATUTE_JURISDICTION"), "SOH_STATUTE_JURISDICTION", i + 1));
	            
	            System.out.println("Setting SOH_STATUTE_TITLE...");
	            logFieldDebug("SOH_STATUTE_TITLE", data.get("SOH_STATUTE_TITLE"), i + 1);
	            details.setSohStatuteTitle(safeStringCast(data.get("SOH_STATUTE_TITLE"), "SOH_STATUTE_TITLE", i + 1));
	            
	            System.out.println("Setting SOH_STATUTE_SECTION...");
	            logFieldDebug("SOH_STATUTE_SECTION", data.get("SOH_STATUTE_SECTION"), i + 1);
	            details.setSohStatuteSection(safeStringCast(data.get("SOH_STATUTE_SECTION"), "SOH_STATUTE_SECTION", i + 1));
	            
	            System.out.println("Setting SOH_STATUTE_SUBSECTION...");
	            logFieldDebug("SOH_STATUTE_SUBSECTION", data.get("SOH_STATUTE_SUBSECTION"), i + 1);
	            details.setSohStatuteSubsection(safeStringCast(data.get("SOH_STATUTE_SUBSECTION"), "SOH_STATUTE_SUBSECTION", i + 1));
	            
	            System.out.println("Setting SOH_STATUTE_TYPE...");
	            logFieldDebug("SOH_STATUTE_TYPE", data.get("SOH_STATUTE_TYPE"), i + 1);
	            details.setSohStatuteType(safeStringCast(data.get("SOH_STATUTE_TYPE"), "SOH_STATUTE_TYPE", i + 1));
	            
	            System.out.println("Setting SOH_STATUTE_CLASS...");
	            logFieldDebug("SOH_STATUTE_CLASS", data.get("SOH_STATUTE_CLASS"), i + 1);
	            details.setSohStatuteClass(safeStringCast(data.get("SOH_STATUTE_CLASS"), "SOH_STATUTE_CLASS", i + 1));
	            
	            System.out.println("Setting SOH_SENTENCE_CATEGORY...");
	            logFieldDebug("SOH_SENTENCE_CATEGORY", data.get("SOH_SENTENCE_CATEGORY"), i + 1);
	            details.setSohSentenceCategory(safeStringCast(data.get("SOH_SENTENCE_CATEGORY"), "SOH_SENTENCE_CATEGORY", i + 1));
	            
	            System.out.println("Setting NCIC_CODE...");
	            logFieldDebug("NCIC_CODE", data.get("NCIC_CODE"), i + 1);
	            details.setNcicCode(safeStringCast(data.get("NCIC_CODE"), "NCIC_CODE", i + 1));
	            
	            // Financial fields
	            System.out.println("Setting FINES...");
	            logFieldDebug("FINES", data.get("FINES"), i + 1);
	            details.setFines(toBigDecimal(data.get("FINES")));
	            
	            System.out.println("Setting COURT_COSTS...");
	            logFieldDebug("COURT_COSTS", data.get("COURT_COSTS"), i + 1);
	            details.setCourtCosts(toBigDecimal(data.get("COURT_COSTS")));
	            
	            System.out.println("Setting RESTITUTION...");
	            logFieldDebug("RESTITUTION", data.get("RESTITUTION"), i + 1);
	            details.setRestitution(toBigDecimal(data.get("RESTITUTION")));
	            
	            System.out.println("Setting DRUG_ALCHL_REHABIT_FUND...");
	            logFieldDebug("DRUG_ALCHL_REHABIT_FUND", data.get("DRUG_ALCHL_REHABIT_FUND"), i + 1);
	            details.setDrugAlchlRehabitFund(toBigDecimal(data.get("DRUG_ALCHL_REHABIT_FUND")));
	            
	            System.out.println("Setting VICTIM_COMPENSATION...");
	            logFieldDebug("VICTIM_COMPENSATION", data.get("VICTIM_COMPENSATION"), i + 1);
	            details.setVictimCompensation(safeDoubleCast(data.get("VICTIM_COMPENSATION"), "VICTIM_COMPENSATION", i + 1));
	            
	            System.out.println("Setting MONEY_OWED...");
	            logFieldDebug("MONEY_OWED", data.get("MONEY_OWED"), i + 1);
	            details.setMoneyOwed(safeDoubleCast(data.get("MONEY_OWED"), "MONEY_OWED", i + 1));
	            
	            System.out.println("Setting MONTHLY_PAYMENT...");
	            logFieldDebug("MONTHLY_PAYMENT", data.get("MONTHLY_PAYMENT"), i + 1);
	            details.setMonthlyPayment(safeDoubleCast(data.get("MONTHLY_PAYMENT"), "MONTHLY_PAYMENT", i + 1));
	            
	            System.out.println("Setting FIRST_DUE_DATE...");
	            logFieldDebug("FIRST_DUE_DATE", data.get("FIRST_DUE_DATE"), i + 1);
	            details.setFirstDueDate(safeDateCast(data.get("FIRST_DUE_DATE"), "FIRST_DUE_DATE", i + 1));
	            
	            System.out.println("Setting FACILITY...");
	            logFieldDebug("FACILITY", data.get("FACILITY"), i + 1);
	            details.setFacility(safeStringCast(data.get("FACILITY"), "FACILITY", i + 1));
	            
	            // Continue with remaining fields...
	            System.out.println("Setting SOH_INTRPT_DATE...");
	            logFieldDebug("SOH_INTRPT_DATE", data.get("SOH_INTRPT_DATE"), i + 1);
	            details.setSohIntrptDate(safeDateCast(data.get("SOH_INTRPT_DATE"), "SOH_INTRPT_DATE", i + 1));
	            
	            System.out.println("Setting SOH_INTRPT_SENT...");
	            logFieldDebug("SOH_INTRPT_SENT", data.get("SOH_INTRPT_SENT"), i + 1);
	            details.setSohIntrptSent(safeStringCast(data.get("SOH_INTRPT_SENT"), "SOH_INTRPT_SENT", i + 1));
	            
	            System.out.println("Setting SOH_DOC_DISCRETION...");
	            logFieldDebug("SOH_DOC_DISCRETION", data.get("SOH_DOC_DISCRETION"), i + 1);
	            details.setSohDocDiscretion(safeBooleanToString(data.get("SOH_DOC_DISCRETION"), "SOH_DOC_DISCRETION", i + 1));
	            
	            System.out.println("Setting SOH_INDEFINITE_FLAG...");
	            logFieldDebug("SOH_INDEFINITE_FLAG", data.get("SOH_INDEFINITE_FLAG"), i + 1);
	            details.setSohIndefiniteFlag(safeBooleanToString(data.get("SOH_INDEFINITE_FLAG"), "SOH_INDEFINITE_FLAG", i + 1));
	            
	            System.out.println("Setting SOH_GDTM_CALC_FLAG...");
	            logFieldDebug("SOH_GDTM_CALC_FLAG", data.get("SOH_GDTM_CALC_FLAG"), i + 1);
	            details.setSohGdtmCalcFlag(safeBooleanToString(data.get("SOH_GDTM_CALC_FLAG"), "SOH_GDTM_CALC_FLAG", i + 1));
	            
	            System.out.println("Setting SOH_MERIT_DAYS...");
	            logFieldDebug("SOH_MERIT_DAYS", data.get("SOH_MERIT_DAYS"), i + 1);
	            details.setSohMeritDays(toBigDecimal(data.get("SOH_MERIT_DAYS")));
	            
	            System.out.println("Setting SOH_RELEASE_DATE...");
	            logFieldDebug("SOH_RELEASE_DATE", data.get("SOH_RELEASE_DATE"), i + 1);
	            details.setSohReleaseDate(toDate(data.get("SOH_RELEASE_DATE").toString()));
	            
	            System.out.println("Setting SOH_RELEASE_TIME...");
	            logFieldDebug("SOH_RELEASE_TIME", data.get("SOH_RELEASE_TIME"), i + 1);
	            details.setSohReleaseTime(safeStringCast(data.get("SOH_RELEASE_TIME"), "SOH_RELEASE_TIME", i + 1));
	            
	            // Parole fields
	            System.out.println("Setting ELIGIBLE_FR_PAROLE_FLAG...");
	            logFieldDebug("ELIGIBLE_FR_PAROLE_FLAG", data.get("ELIGIBLE_FR_PAROLE_FLAG"), i + 1);
	            details.setEligibleFrParoleFlag(safeBooleanToString(data.get("ELIGIBLE_FR_PAROLE_FLAG"), "ELIGIBLE_FR_PAROLE_FLAG", i + 1));
	            
	            System.out.println("Setting SOH_STATUS...");
	            logFieldDebug("SOH_STATUS", data.get("SOH_STATUS"), i + 1);
	            details.setSohStatus(safeBooleanToString(data.get("SOH_STATUS"), "SOH_STATUS", i + 1));
	            
	            System.out.println("Setting PAROLE_V_VIOL_FLAG...");
	            logFieldDebug("PAROLE_V_VIOL_FLAG", data.get("PAROLE_V_VIOL_FLAG"), i + 1);
	            details.setParoleVViolFlag(safeBooleanCast(data.get("PAROLE_V_VIOL_FLAG"), "PAROLE_V_VIOL_FLAG", i + 1));
	            
	            details.setSohWeekenderDays(toBigDecimal(data.get("SOH_WEEKENDER_DAYS")));
	            
	            // Continue with all remaining fields using safe casting...
	            details.setParoleVIncarcDate(safeDateCast(data.get("PAROLE_V_INCARC_DATE"), "PAROLE_V_INCARC_DATE", i + 1));
	            details.setParoleVParoleDate(safeDateCast(data.get("PAROLE_V_PAROLE_DATE"), "PAROLE_V_PAROLE_DATE", i + 1));
	            details.setParoleVOriginalEffDt(safeDateCast(data.get("PAROLE_V_ORIGINAL_EFF_DT"), "PAROLE_V_ORIGINAL_EFF_DT", i + 1));
	            details.setParoleVOrgnlCalSeqNum(toBigDecimal(data.get("PAROLE_V_ORGNL_CAL_SEQ_NUM")));
	            details.setParoleVMed(safeDateCast(data.get("PAROLE_V_MED"), "PAROLE_V_MED", i + 1));
	            details.setParoleVWarrantDate(safeDateCast(data.get("PAROLE_V_WARRANT_DATE"), "PAROLE_V_WARRANT_DATE", i + 1));
	            details.setParoleVAbscondTime(toBigDecimal("PAROLE_V_ABSCOND_TIME"));
	            details.setParoleVAllGdtm(safeBooleanCast(data.get("PAROLE_V_ALL_GDTM"), "PAROLE_V_ALL_GDTM", i + 1));
	            details.setParoleVGoodTime(toBigDecimal(data.get("PAROLE_V_GOOD_TIME")));
	            details.setParoleVEligibleFrParole(safeBooleanCast(data.get("PAROLE_V_ELIGIBLE_FR_PAROLE"), "PAROLE_V_ELIGIBLE_FR_PAROLE", i + 1));
	            
	            // CR fields
	            details.setCrVCrDate(safeDateCast(data.get("crVCrDate"), "crVCrDate", i + 1));
	            details.setCrVOrigninalEffDate(safeDateCast(data.get("crVOrigninalEffDate"), "crVOrigninalEffDate", i + 1));
	            details.setCrVOrgnlCalSeqNum(toBigDecimal(data.get("crVOrgnlCalSeqNum")));
	            details.setCrVCrViolation(safeBooleanToString(data.get("crVCrViolation"), "crVCrViolation", i + 1));
	            details.setCrVIncurcDate(safeDateCast(data.get("crVIncurcDate"), "crVIncurcDate", i + 1));
	            details.setCrVMed(safeDateCast(data.get("crVMed"), "crVMed", i + 1));
	            details.setCrVWarrantDate(safeDateCast(data.get("crVWarrantDate"), "crVWarrantDate", i + 1));
	            details.setCrVAbscondDays(toBigDecimal(data.get("crVAbscondDays")));
	            details.setCrVAllGdtm(safeBooleanToString(data.get("crVAllGdtm"), "crVAllGdtm", i + 1));
	            details.setCrVGoodTime(toBigDecimal(data.get("crVGoodTime")));
	            
	            // Approval fields
	            details.setAprvFlag(safeBooleanToString(data.get("APRV_FLAG"), "APRV_FLAG", i + 1));
	            details.setAprvUserId(safeStringCast(data.get("APRV_USER_ID"), "APRV_USER_ID", i + 1));
	            details.setAprvDate(safeDateCast(data.get("APRV_DATE"), "APRV_DATE", i + 1));
	            details.setAprvTime(safeStringCast(data.get("APRV_TIME"), "APRV_TIME", i + 1));
	            details.setAprvReasonCode(safeStringCast(data.get("APRV_REASON_CODE"), "APRV_REASON_CODE", i + 1));
	            details.setAprvUserComnts(safeStringCast(data.get("APRV_USR_COMNTS"), "APRV_USR_COMNTS", i + 1));
	            details.setSprvDecision(safeStringCast(data.get(""), "", i + 1));
	            details.setSprvUserId(safeStringCast(data.get("SPRV_USER_ID"), "SPRV_USER_ID", i + 1));
	            details.setSprvDate(safeDateCast(data.get("SPRV_DATE"), "SPRV_DATE", i + 1));
	            details.setSprvTime(safeStringCast(data.get("SPRV_TIME"), "SPRV_TIME", i + 1));
	            details.setSprvUserComnts(safeStringCast(data.get("SPRV_COMNTS"), "SPRV_COMNTS", i + 1));
	            
	            // Cancel fields
	            details.setSohCancelFlag(safeBooleanToString(data.get("sohCancelFlag"), "sohCancelFlag", i + 1));
	            details.setSohCancelBy(safeStringCast(data.get("sohCancelBy"), "sohCancelBy", i + 1));
	            details.setSohCancelDate(safeDateCast(data.get("sohCancelDate"), "sohCancelDate", i + 1));
	            details.setSohCancelTime(safeStringCast(data.get("sohCancelTime"), "sohCancelTime", i + 1));
	            details.setSohCancelComnts(safeStringCast(data.get("sohCancelComnts"), "sohCancelComnts", i + 1));
	            
	            // Status fields
	            details.setSohStatus(safeStringCast(data.get("sohStatus"), "sohStatus", i + 1));
	            details.setOrderType(safeStringCast(data.get("orderType"), "orderType", i + 1));
	            details.setSentNewFlag(safeBooleanToString(data.get("sentNewFlag"), "sentNewFlag", i + 1));
	            
	            // Consecutive/Concurrent fields
	            details.setConsConcFlag(safeStringCast(data.get("CONS_CONC_FLAG"), "CONS_CONC_FLAG", i + 1));
	            details.setConsConcSntcOrdSeqNum(toBigDecimal(data.get("CONS_CONC_SNTC_ORD_SEQ_NUM")));
	            details.setConsConcSohSeqNum(toBigDecimal(data.get("CONS_CONC_SOH_SEQ_NUM")));
	            details.setConsConcLvl5(safeBooleanToString(data.get("CONS_CONC_LVL5"), "CONS_CONC_LVL5", i + 1));
	            details.setConsConcLvl4(safeBooleanToString(data.get("CONS_CONC_LVL4"), "CONS_CONC_LVL4", i + 1));
	            details.setConsConcLvl4h(safeBooleanToString(data.get("CONS_CONC_LVL4H"), "CONS_CONC_LVL4H", i + 1));
	            details.setConsConcLvl3(safeBooleanToString(data.get("CONS_CONC_LVL3"), "CONS_CONC_LVL3", i + 1));
	            details.setConsConcLvl2(safeBooleanToString(data.get("CONS_CONC_LVL2"), "CONS_CONC_LVL2", i + 1));
	            details.setConsConcLvl1(safeBooleanToString(data.get("CONS_CONC_LVL1"), "CONS_CONC_LVL1", i + 1));
	            details.setConsConcLvl1r(safeBooleanToString(data.get("CONS_CONC_LVL1R"), "CONS_CONC_LVL1R", i + 1));
	            
	            // Modification fields
	            details.setSohModificationType(safeStringCast(data.get("SOH_MODIFICATION_TYPE"), "SOH_MODIFICATION_TYPE", i + 1));
	            details.setSohSntcLvlSeqNum(toBigDecimal(data.get("sohSntcLvlSeqNum")));
	            details.setSohSntcOrdSeqNum(toBigDecimal(data.get("sohSntcOrdSeqNum")));
	            
	            // Final fields
	            System.out.println("Setting OFFENSE_DATE...");
	            logFieldDebug("OFFENSE_DATE", details.getEffectiveDate(), i + 1);
	            details.setOffenseDate(toDate(data.get("OFFENSE_DATE").toString()));
	            logFieldDebug("OFFENSE_DATE", details.getEffectiveDate(), i + 1);
	            
	            details.setSoh4382aFlag(safeBooleanToString(data.get("SOH_4382A_FLAG"), "SOH_4382A_FLAG", i + 1));
	            
	            System.out.println("Successfully processed object " + (i + 1));
	            
	        } catch (Exception e) {
	            System.err.println("ERROR: Exception occurred while processing object " + (i + 1) + ": " + e.getMessage());
	            e.printStackTrace();
	            throw new RuntimeException("Failed to process object " + (i + 1) + " in mapToSentenceOrderDetails", e);
	        }
	        
	        detailsList.add(details);
	    }
	    
	    System.out.println("=== Completed mapToSentenceOrderDetails ===");
	    System.out.println("Successfully processed " + detailsList.size() + " objects");
	    return detailsList;
	}
	
	// Helper method for detailed field debugging
	private static void logFieldDebug(String fieldName, Object value, int objectIndex) {
	    if (value == null) {
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": null");
	    } else {
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": " + value + " (Type: " + value.getClass().getSimpleName() + ")");
	    }
	}
	
	// Safe string casting with detailed error reporting
	private static String safeStringCast(Object value, String fieldName, int objectIndex) {
	    if (value == null) {
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": null -> returning null");
	        return null;
	    }
	    
	    try {
	        String result = (String) value;
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": Successfully cast to String");
	        return result;
	    } catch (ClassCastException e) {
	        System.err.println("  [Object " + objectIndex + "] ERROR: Cannot cast " + fieldName + " to String. Value: " + value + " (Type: " + value.getClass().getName() + ")");
	        System.err.println("  [Object " + objectIndex + "] Converting to String using toString()");
	        return value.toString();
	    }
	}
	
	// Safe boolean casting with detailed error reporting
	private static Boolean safeBooleanCast(Object value, String fieldName, int objectIndex) {
	    if (value == null) {
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": null -> returning null");
	        return null;
	    }
	    
	    try {
	        Boolean result = (Boolean) value;
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": Successfully cast to Boolean");
	        return result;
	    } catch (ClassCastException e) {
	        System.err.println("  [Object " + objectIndex + "] ERROR: Cannot cast " + fieldName + " to Boolean. Value: " + value + " (Type: " + value.getClass().getName() + ")");
	        // Try to convert common boolean representations
	        String stringValue = value.toString().toLowerCase();
	        if ("true".equals(stringValue) || "1".equals(stringValue) || "y".equals(stringValue)) {
	            System.err.println("  [Object " + objectIndex + "] Converting '" + value + "' to true");
	            return true;
	        } else if ("false".equals(stringValue) || "0".equals(stringValue) || "n".equals(stringValue)) {
	            System.err.println("  [Object " + objectIndex + "] Converting '" + value + "' to false");
	            return false;
	        } else {
	            System.err.println("  [Object " + objectIndex + "] Cannot determine boolean value, returning null");
	            return null;
	        }
	    }
	}
	
	// Safe boolean to string conversion with detailed error reporting
	private static String safeBooleanToString(Object value, String fieldName, int objectIndex) {
	    if (value == null) {
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": null -> returning null");
	        return null;
	    }
	    
	    try {
	        Boolean boolValue = (Boolean) value;
	        String result = boolValue ? "Y" : "N";
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": Successfully converted Boolean to " + result);
	        return result;
	    } catch (ClassCastException e) {
	        System.err.println("  [Object " + objectIndex + "] ERROR: Cannot cast " + fieldName + " to Boolean. Value: " + value + " (Type: " + value.getClass().getName() + ")");
	        // Try to convert common boolean representations
	        String stringValue = value.toString().toLowerCase();
	        if ("true".equals(stringValue) || "1".equals(stringValue) || "y".equals(stringValue)) {
	            System.err.println("  [Object " + objectIndex + "] Converting '" + value + "' to 'Y'");
	            return "Y";
	        } else if ("false".equals(stringValue) || "0".equals(stringValue) || "n".equals(stringValue)) {
	            System.err.println("  [Object " + objectIndex + "] Converting '" + value + "' to 'N'");
	            return "N";
	        } else {
	            System.err.println("  [Object " + objectIndex + "] Cannot determine boolean value, returning null");
	            return null;
	        }
	    }
	}
	
	// Safe double casting with detailed error reporting
	private static Double safeDoubleCast(Object value, String fieldName, int objectIndex) {
	    if (value == null) {
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": null -> returning null");
	        return null;
	    }
	    
	    try {
	        Double result = (Double) value;
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": Successfully cast to Double");
	        return result;
	    } catch (ClassCastException e) {
	        System.err.println("  [Object " + objectIndex + "] ERROR: Cannot cast " + fieldName + " to Double. Value: " + value + " (Type: " + value.getClass().getName() + ")");
	        try {
	            if (value instanceof Number) {
	                Double result = ((Number) value).doubleValue();
	                System.err.println("  [Object " + objectIndex + "] Converted Number to Double: " + result);
	                return result;
	            } else {
	                Double result = Double.parseDouble(value.toString());
	                System.err.println("  [Object " + objectIndex + "] Parsed String to Double: " + result);
	                return result;
	            }
	        } catch (NumberFormatException nfe) {
	            System.err.println("  [Object " + objectIndex + "] Cannot convert to Double, returning null");
	            return null;
	        }
	    }
	}
	
	// Safe date casting with detailed error reporting
	private static Date safeDateCast(Object value, String fieldName, int objectIndex) {
	    if (value == null) {
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": null -> returning null");
	        return null;
	    }
	    
	    try {
	        Date result = (Date) value;
	        System.out.println("  [Object " + objectIndex + "] " + fieldName + ": Successfully cast to Date");
	        return result;
	    } catch (ClassCastException e) {
	        System.err.println("  [Object " + objectIndex + "] ERROR: Cannot cast " + fieldName + " to Date. Value: " + value + " (Type: " + value.getClass().getName() + ")");
	        System.err.println("  [Object " + objectIndex + "] Attempting to use toDate() method");
	        return toDate(value.toString());
	    }
	}

	private static BigDecimal toBigDecimal(Object value) {
	    if (value == null) return null;
	    
	    String stringValue = value.toString().trim();
	    if (stringValue.isEmpty()) return null;
	    
	    try {
	        if (value instanceof Number) {
	            return new BigDecimal(((Number) value).toString());
	        }
	        else if (value instanceof Integer) {
	        	return new BigDecimal(((Integer) value).toString());
	        }
	        /*else if (value instanceof String) {
	        	return new BigDecimal((String) value);
	        }*/
	        return new BigDecimal(stringValue);
	    } catch (NumberFormatException e) {
	        // Log the error for debugging purposes
	        System.err.println("Warning: Could not convert value '" + stringValue + "' to BigDecimal. Returning null.");
	        return null;
	    }
	}

	/*private static Date toDate(Object value) {
	    if (value == null) return null;
	    if (value instanceof Date) return (Date) value;
	    try {
	        return (Date) javax.xml.bind.DatatypeConverter.parseDateTime(value.toString()).getTime();
	    } catch (Exception e) {
	        return null; // or throw custom error if critical
	    }
	}*/
	
	private static Date toDate(String value) {
	    //if (!value.isEmpty()) return null;
	    //if (value instanceof Date) return (Date) value;
		try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	        LocalDate localDate = LocalDate.parse(value, formatter);
	        return (Date) Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    } catch (Exception e) {
	        System.out.println("Exception in converting date: " + e);
	        return null;
	    }
	}

}
