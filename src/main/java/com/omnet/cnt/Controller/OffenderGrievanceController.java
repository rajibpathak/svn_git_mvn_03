package com.omnet.cnt.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.omnet.cnt.Service.OffenderGrievanceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/OffenderGrievance")
public class OffenderGrievanceController {

    @Autowired
    private OffenderGrievanceService grievanceService;
    
    @GetMapping("/getGrievanceDetails")
    public ResponseEntity<Map<String, Object>> getGrievanceDetailsWithStatus(
        @RequestParam String commitNo, 
        @RequestParam Integer seqNum) {
        
        Map<String, Object> grievanceData = grievanceService.getGrievanceDetails(commitNo, seqNum);
        return ResponseEntity.ok(grievanceData);
    }
    
    @GetMapping("/listWithDatesBySbi")
    public List<Map<String, Object>> getGrievanceListBySbi(
            @RequestParam String sbiNo,
            @RequestParam(required = false) String commitNo) {
        String finalCommitNo = commitNo;
        if (finalCommitNo == null || finalCommitNo.isEmpty()) {
            finalCommitNo = grievanceService.getCommitNoBySbi(sbiNo);
        }
        return grievanceService.getGrievanceListWithDates(finalCommitNo);
    }

    @GetMapping("/getCommitNoBySbi")
    public ResponseEntity<String> getCommitNoBySbi(@RequestParam String sbiNo) {
        String commitNo = grievanceService.getCommitNoBySbi(sbiNo);
        return ResponseEntity.ok(commitNo);
    }
    
    @GetMapping("/getHousingLocation")
    public ResponseEntity<String> getHousingLocation(@RequestParam String commitNo) {
        String housingLocation = grievanceService.getHousingLocation(commitNo);
        return ResponseEntity.ok(housingLocation);
    }

    @GetMapping("/getGrievanceStatuses")
    public ResponseEntity<List<Map<String, Object>>> getGrievanceStatuses() {
        List<Map<String, Object>> statuses = grievanceService.getGrievanceStatuses();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/getGrievanceTypes")
    public ResponseEntity<List<Map<String, Object>>> getGrievanceTypes() {
        List<Map<String, Object>> types = grievanceService.getGrievanceTypes();
        return ResponseEntity.ok(types);
    }
    
    @GetMapping("/getFacilities")
    public ResponseEntity<List<Map<String, Object>>> getFacilities(@RequestParam String userId) {
        List<Map<String, Object>> facilities = grievanceService.getFacilities(userId);
        return ResponseEntity.ok(facilities);
    }

    @GetMapping("/getLocations")
    public ResponseEntity<List<Map<String, Object>>> getLocations(@RequestParam String instNum) {
        List<Map<String, Object>> locations = grievanceService.getLocations(instNum);
        return ResponseEntity.ok(locations);
    }
    
    @GetMapping("/getAbandonmentReasons")
    public ResponseEntity<List<Map<String, Object>>> getAbandonmentReasons() {
        List<Map<String, Object>> reasons = grievanceService.getAbandonmentReasons();
        return ResponseEntity.ok(reasons);
    }
    
    @GetMapping("/getUsers")
    public List<Map<String, Object>> getUsers(@RequestParam(required = false) String searchTerm) {
        return grievanceService.getUsers(searchTerm != null ? searchTerm : "");
    }
 
    @GetMapping("/validateSbi")
    public ResponseEntity<Map<String, Boolean>> validateSbi(@RequestParam String sbiNo) {
        boolean isValid = grievanceService.isSbiValid(sbiNo);
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/getFacilityAddressInfo") // <--- This annotation is crucial
    public ResponseEntity<Map<String, Object>> getFacilityAddressInfo(@RequestParam String instNum) {
        Map<String, Object> addressInfo = grievanceService.getFacilityAddressInfo(instNum);
        if (addressInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(addressInfo);
    }

    @GetMapping("/getPersonsInvolved")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getPersonsInvolved(
        @RequestParam String commitNo, 
        @RequestParam Integer seqNum,
        @RequestParam(required = false) String cmtyType) {
        
        Map<String, List<Map<String, Object>>> persons = grievanceService.getPersonsInvolvedByType(commitNo, seqNum, cmtyType);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/getStaffList")
    public ResponseEntity<List<Map<String, Object>>> getStaffList(
        @RequestParam(required = false) String searchTerm) {
        
        List<Map<String, Object>> staff = grievanceService.getStaffList(searchTerm);
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/getUserDetails")
    public ResponseEntity<Map<String, String>> getUserDetails(@RequestParam String userId) {
        Map<String, String> userDetails = grievanceService.getUserDetails(userId);
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/saveGrievance")
    public ResponseEntity<Map<String, Object>> saveGrievance(
        @RequestBody Map<String, Object> grievanceData,
        @RequestHeader("userId") String userId) {  
        
        try {
            Map<String, Object> saveResult = grievanceService.saveGrievance(grievanceData, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Grievance saved successfully");
            response.put("newSeqNum", saveResult.get("newSeqNum"));
            response.put("commitNo", saveResult.get("commitNo"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error saving grievance: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/savePersonsInvolved")
    public ResponseEntity<Map<String, Object>> savePersonsInvolved(
        @RequestBody Map<String, Object> requestData) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            Boolean isNew = (Boolean) requestData.get("isNew");
            List<Map<String, Object>> personsData = (List<Map<String, Object>>) requestData.get("personsData");
            
            if (commitNo == null || seqNum == null || isNew == null || personsData == null) {
                throw new IllegalArgumentException("Missing required parameters");
            }
            
            grievanceService.savePersonsInvolved(commitNo, seqNum, personsData, isNew);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Persons involved saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error saving persons involved: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/saveInvestigationDetails")
    public ResponseEntity<Map<String, Object>> saveInvestigationDetails(
            @RequestBody Map<String, Object> requestData,
            @RequestHeader("userId") String userId) {
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> investigationData = 
                (List<Map<String, Object>>) requestData.get("investigationData");
            
            grievanceService.saveInvestigationDetails(commitNo, seqNum, investigationData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Investigation details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/saveLSMEMembers")
    public ResponseEntity<Map<String, Object>> saveLSMEMembers(
            @RequestBody Map<String, Object> requestData,
            @RequestHeader("userId") String userId) {
        try {
            String instNum = (String) requestData.get("instNum");
            String grievanceType = (String) requestData.get("grievanceType");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> members = 
                (List<Map<String, Object>>) requestData.get("members");
            
            grievanceService.saveLSMEMembers(instNum, grievanceType, members, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "LSME members saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
      
    
    @GetMapping("/getInvestigationDetails")
    public ResponseEntity<Map<String, Object>> getInvestigationDetails(
        @RequestParam String commitNo, 
        @RequestParam Integer seqNum) {
        
        Map<String, Object> investigationData = grievanceService.getInvestigationDetails(commitNo, seqNum);
        return ResponseEntity.ok(investigationData);
    }
    
    @GetMapping("/getLSMEMembers")
    public ResponseEntity<List<Map<String, Object>>> getLSMEMembers(
        @RequestParam String instNum,
        @RequestParam String grievanceType) {
        
        List<Map<String, Object>> members = grievanceService.getLSMEMembers(instNum, grievanceType);
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/getIGCReviewDetails")
    public ResponseEntity<Map<String, Object>> getIGCReviewDetails(
        @RequestParam String commitNo, 
        @RequestParam Long seqNum) {
        
        Map<String, Object> igcData = grievanceService.getIGCReviewDetails(commitNo, seqNum);
        return ResponseEntity.ok(igcData);
    }
    
    @PostMapping("/saveIGCDetails")
    public ResponseEntity<Map<String, Object>> saveIGCDetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            Map<String, Object> igcData = (Map<String, Object>) requestData.get("igcData");
            
            grievanceService.saveIGCDetails(commitNo, seqNum, igcData);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "IGC details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/saveAdditionalComments")
    public ResponseEntity<Map<String, Object>> saveAdditionalComments(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> commentsData = (List<Map<String, Object>>) requestData.get("commentsData");
            
            grievanceService.saveAdditionalComments(commitNo, seqNum, commentsData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Additional comments saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getRGCDetails")
    public ResponseEntity<Map<String, Object>> getRGCDetails(
            @RequestParam String commitNo,
            @RequestParam Integer seqNum,
            @RequestParam String cmtyType) {
    	
            Map<String, Object> rgcDetails = grievanceService.getRGCDetails(commitNo, seqNum, cmtyType);
            return ResponseEntity.ok(rgcDetails);
    }
    
    @PostMapping("/saveRGCDetails")
    public ResponseEntity<Map<String, Object>> saveRGCDetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            Map<String, Object> rgcData = (Map<String, Object>) requestData.get("rgcData");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> members = (List<Map<String, Object>>) requestData.get("members");
            
            rgcData.put("members", members);
            
            grievanceService.saveRGCDetails(commitNo, seqNum, rgcData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "RGC details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getRGCMembers")
    public ResponseEntity<List<Map<String, Object>>> getRGCMembers(@RequestParam String instNum) {
        List<Map<String, Object>> members = grievanceService.getRGCMembers(instNum);
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/saveRGCMembers")
    public ResponseEntity<Map<String, Object>> saveRGCMembers(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String instNum = (String) requestData.get("instNum");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> members = (List<Map<String, Object>>) requestData.get("members");
            
            grievanceService.saveRGCMembers(instNum, members, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "RGC members saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getMGCDetails")
    public ResponseEntity<Map<String, Object>> getMGCDetails(
            @RequestParam String commitNo,
            @RequestParam Integer seqNum,
            @RequestParam String cmtyType) {
        
        Map<String, Object> mgcDetails = grievanceService.getMGCDetails(commitNo, seqNum, cmtyType);
        return ResponseEntity.ok(mgcDetails);
    }
    
    @PostMapping("/saveMGCDetails")
    public ResponseEntity<Map<String, Object>> saveMGCDetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            Map<String, Object> mgcData = (Map<String, Object>) requestData.get("mgcData");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> members = (List<Map<String, Object>>) requestData.get("members");
            
            mgcData.put("members", members);
            
            grievanceService.saveMGCDetails(commitNo, seqNum, mgcData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "MGC details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getMGCMembers")
    public ResponseEntity<List<Map<String, Object>>> getMGCMembers(@RequestParam String instNum) {
        List<Map<String, Object>> members = grievanceService.getMGCMembers(instNum);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/saveMGCMembers")
    public ResponseEntity<Map<String, Object>> saveMGCMembers(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String instNum = (String) requestData.get("instNum");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> members = (List<Map<String, Object>>) requestData.get("members");
            
            grievanceService.saveMGCMembers(instNum, members, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "MGC members saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getReferralDetails")
    public ResponseEntity<List<Map<String, Object>>> getReferralDetails(
        @RequestParam String commitNo,
        @RequestParam Integer seqNum) {
        
        List<Map<String, Object>> referralData = grievanceService.getReferralDetails(commitNo, seqNum);
        return ResponseEntity.ok(referralData);
    }
    
    @PostMapping("/saveReferralDetails")
    public ResponseEntity<Map<String, Object>> saveReferralDetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> referralData = (List<Map<String, Object>>) requestData.get("referralData");
            
            grievanceService.saveReferralDetails(commitNo, seqNum, referralData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Referral details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getWardenDetails")
    public ResponseEntity<Map<String, Object>> getWardenDetails(
        @RequestParam String commitNo,
        @RequestParam Integer seqNum,
        @RequestParam String cmtyType) {
        
        Map<String, Object> wardenData = grievanceService.getWardenDetails(commitNo, seqNum, cmtyType);
        return ResponseEntity.ok(wardenData);
    }
    
    @PostMapping("/saveWardenDetails")
    public ResponseEntity<Map<String, Object>> saveWardenDetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            Map<String, Object> wardenData = (Map<String, Object>) requestData.get("wardenData");
            
            grievanceService.saveWardenDetails(commitNo, seqNum, wardenData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Warden details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getAppealDetails")
    public ResponseEntity<Map<String, Object>> getAppealDetails(
        @RequestParam String commitNo, 
        @RequestParam Integer seqNum) {
        
        Map<String, Object> appealData = grievanceService.getAppealDetails(commitNo, seqNum);
        return ResponseEntity.ok(appealData);
    }
    
    @PostMapping("/saveAppealDetails")
    public ResponseEntity<Map<String, Object>> saveAppealDetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            Map<String, Object> appealData = (Map<String, Object>) requestData.get("appealData");
            
            grievanceService.saveAppealDetails(commitNo, seqNum, appealData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Appeal details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/getBGODetails")
    public ResponseEntity<Map<String, Object>> getBGODetails(
        @RequestParam String commitNo,
        @RequestParam Integer seqNum,
        @RequestParam String cmtyType) {
        
        Map<String, Object> bgoData = grievanceService.getBGODetails(commitNo, seqNum, cmtyType);
        return ResponseEntity.ok(bgoData);
    }
    
    @PostMapping("/saveBGODetails")
    public ResponseEntity<Map<String, Object>> saveBGODetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            Map<String, Object> bgoData = (Map<String, Object>) requestData.get("bgoData");
            
            grievanceService.saveBGODetails(commitNo, seqNum, bgoData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "BGO details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/getBureauChiefDetails")
    public ResponseEntity<Map<String, Object>> getBureauChiefDetails(
        @RequestParam String commitNo,
        @RequestParam Integer seqNum,
        @RequestParam String cmtyType) {
        
        Map<String, Object> bureauData = grievanceService.getBureauChiefDetails(commitNo, seqNum, cmtyType);
        return ResponseEntity.ok(bureauData);
    }

    @PostMapping("/saveBureauChiefDetails")
    public ResponseEntity<Map<String, Object>> saveBureauChiefDetails(
        @RequestBody Map<String, Object> requestData,
        @RequestHeader("userId") String userId) {
        
        try {
            String commitNo = (String) requestData.get("commitNo");
            Integer seqNum = (Integer) requestData.get("seqNum");
            @SuppressWarnings("unchecked")
            Map<String, Object> bureauData = (Map<String, Object>) requestData.get("bureauData");
            
            grievanceService.saveBureauChiefDetails(commitNo, seqNum, bureauData, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Bureau Chief details saved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

}
