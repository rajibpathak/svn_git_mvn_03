package com.omnet.cnt.Controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.omnet.cnt.Service.DisciplinaryInfoService;
import com.omnet.cnt.Model.DisciplinaryInfo;

@RestController
public class DisciplinaryInfoController {

    @Autowired
    private DisciplinaryInfoService disciplinaryInfoService;

    @GetMapping("/DisciplinaryReportIds")
    public List<Map<String, Object>> getDisciplinaryReportIds(@RequestParam("P_COMMIT_NO") String commitNo) {
        return disciplinaryInfoService.getDisciplinaryReportIds(commitNo);
    }

    @GetMapping("/Incidentdropdown")
    public List<DisciplinaryInfo> getIncidentDropdown(@RequestParam String commitNo) {
        return disciplinaryInfoService.getIncidentDropDown(commitNo);
    }
    
    @GetMapping("/DisciplinaryTypes")
    public List<Map<String, Object>> getDisciplinaryTypes() {
        return disciplinaryInfoService.getDisciplinaryTypes();
    }
    
    @GetMapping("/DisciplinaryFormDetails")
    public Map<String, Object> getFormDetails(
            @RequestParam("commitNo") String commitNo,
            @RequestParam("dReportNum") long dReportNum) {
        return disciplinaryInfoService.getDisciplinaryFormDetails(commitNo, dReportNum);
    }


    

    @GetMapping("/MOV_SWAP_VALUESDisplinary")
    public List<Map<String, Object>> OffederDisplinaryvalues(String P_COMMIT_NO, String P_D_REPORT_NUM) throws SQLException {
        //P_COMMIT_NO = "0045073";  
       // P_D_REPORT_NUM = "924";
        return disciplinaryInfoService.getDisciplinaryInfo(P_COMMIT_NO, P_D_REPORT_NUM);
    }
    
    @GetMapping("/ViolationSummary")
    public Map<String, Object> getViolationSummary(@RequestParam("sbiNo") String sbiNo,
    		@RequestParam("idt_start_dt") Date idt_start_dt,
    		@RequestParam("idt_end_dt") Date idt_end_dt
    		) {
        return disciplinaryInfoService.getViolationSummary(sbiNo, idt_start_dt, idt_end_dt);
    }
    
    @GetMapping("/DisciplineCodeList")
    public List<Map<String, Object>> getDisciplineCodes() {
        return disciplinaryInfoService.getDisciplineCodes();
    }
    
    @GetMapping("/DisciplineCodes")
    public List<Map<String, Object>> getDisciplineCodes(
            @RequestParam("P_COMMIT_NO") String commitNo,
            @RequestParam("P_D_REPORT_NUM") String reportNum) {
        return disciplinaryInfoService.getDisciplineCodesByCommitAndReport(commitNo, reportNum);
    }
   
    @GetMapping("/WitnessDetails")
    public List<DisciplinaryInfo> getWitnessDetails(@RequestParam("commitNo") String commitNo,
                                                    @RequestParam("reportNo") Long reportNo) {
        return disciplinaryInfoService.getWitnessesByCommitAndReport(commitNo, reportNo);
    }
    
    @GetMapping("/AssaultTypes")
    public List<Map<String, Object>> getAssaultTypeDropdown() {
        return disciplinaryInfoService.getAssaultTypeOptions();
    }
    
    @GetMapping("/WeaponTypes")
    public List<Map<String, Object>> getWeaponTypeDropdown() {
        return disciplinaryInfoService.getWeaponTypeOptions();
    }
    
   
    
    @GetMapping("/LoggedInUserNameFields")
    @ResponseBody
    public Map<String, Object> getLoggedInUserNameFields(@RequestParam("userId") String userId) {
        return disciplinaryInfoService.getEnteredByUserDetails(userId);
    }
    
    
    /*@GetMapping("/enteredByTitles")
    public List<Map<String, Object>> getEnteredByTitles() {
    	return disciplinaryInfoService.getEnteredByTitles();
    }*/
    
    
//    @GetMapping("/PreHearingTitleDropdown")
//    @ResponseBody
//    public List<Map<String, Object>> getPreHearingTitleDropdown() {
//        return disciplinaryInfoService.getPreHearingTitleOptions();
//    }
    
    @GetMapping("/DecisionDropdown")
    public List<Map<String, Object>> getDecisionDropdown() {
        return disciplinaryInfoService.getDecisionDropdownOptions();
    }
    
    @GetMapping("/DispositionDropdown")
    public List<Map<String, Object>> getDispositionDropdown() {
        return disciplinaryInfoService.getDispositionOptions();
    }
    
    @GetMapping("/PreHearingRefToDropdown")
    public List<Map<String, String>> getPreHearingRefToDropdown() {
        return disciplinaryInfoService.getPreHearingRefToDropdownOptions();
    }
    
    /*@GetMapping("/DeliveredByDropdown")
    public List<Map<String, Object>> getgetDeliveredByTitlesDropdown() {
        return disciplinaryInfoService.getDeliveredByTitles();
    }*/
    
    @GetMapping("/ApprovedDeliveredTitleDropdown")
    public List<Map<String, Object>> getAllApprovedByTitles() {
        return disciplinaryInfoService.getTitleDropdown();
    }
    
    @GetMapping("/getOmnetUsers")
    public List<Map<String, Object>> getOmnetUsers() {
        return disciplinaryInfoService.getOmnetUsers();
    }
    
    
   

    @GetMapping("/getGroupSeqNo")
    public ResponseEntity<String> getGroupSeqNo(@RequestParam String incidentSeqNum) {
        String groupSeqNo = disciplinaryInfoService.getGroupSequenceNumber(incidentSeqNum);
        return ResponseEntity.ok(groupSeqNo);
    }
    
    
    
    
    

    @GetMapping("/institutionNumber")
    public ResponseEntity<String> getInstitutionNumber(@RequestParam("incidentSeqNum") String incidentSeqNum) {
        if (incidentSeqNum == null || incidentSeqNum.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Missing incidentSeqNum");
        }

        String institutionNumber = disciplinaryInfoService.getInstitutionNumber(incidentSeqNum);

        if (institutionNumber == null || institutionNumber.trim().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(institutionNumber);
    }
    
    @GetMapping("/generateReportNumber")
    public ResponseEntity<?> generateReportNumber(@RequestParam("incidentSeqNum") String incidentSeqNum) {
        if (incidentSeqNum == null || incidentSeqNum.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("incidentSeqNum is required");
        }

        String reportNum = disciplinaryInfoService.generateReportNumber(incidentSeqNum);
        if (reportNum == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Institution number not found or unsupported");
        }

        Map<String, String> response = new HashMap<>();
        response.put("reportNum", reportNum);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/insertDisciplinaryInfo")
    public String saveDisciplinaryInfo(@RequestBody List<DisciplinaryInfo> infoList) {
        disciplinaryInfoService.saveDisciplinaryInfo(infoList);
        return "Saved successfully";
    }
    
    public ResponseEntity<String> saveWitnessDetails(@RequestBody List<DisciplinaryInfo> witnessList) {
        try {
            disciplinaryInfoService.saveWitnessDetails(witnessList);
            return ResponseEntity.ok("Witness details saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to save witness details");
        }
    }
    
    
}








