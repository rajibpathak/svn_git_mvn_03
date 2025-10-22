      
 package com.omnet.cnt.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.omnet.cnt.Model.AdmissionRelease;
import com.omnet.cnt.Service.InmateRegistrationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Booking")
public class InmateRegistrationController {

    @Autowired
    private InmateRegistrationService inmateService;
    
    @GetMapping("/combinedDetails")
    public ResponseEntity<List<Map<String, Object>>> getCombinedInmateDetails(@RequestParam String sbiNo) {
        List<Map<String, Object>> inmateData = inmateService.getCombinedInmateDetailsBySbiNo(sbiNo);
        return ResponseEntity.ok(inmateData);
    }
    
    @GetMapping("/getCommitNo")
    public ResponseEntity<String> getCommitNo(@RequestParam String sbiNo) {
        String commitNo = inmateService.getCommitNoBySbiNo(sbiNo);
        return ResponseEntity.ok(commitNo);
    }
   
    @PostMapping("/saveOrUpdate")
    public ResponseEntity<Map<String, String>> saveOrUpdateInmate(@RequestBody Map<String, Object> requestData) {
        try {
            String sbiNo = (String) requestData.get("sbi_no");
            String newFlag = "New".equalsIgnoreCase(sbiNo) ? "Y" : "N";
            
            String generatedSbiNo = inmateService.saveOrUpdateInmate(requestData, newFlag);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Inmate data saved successfully!");
            response.put("sbiNo", generatedSbiNo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full error
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error saving inmate data: " + e.getMessage());
            errorResponse.put("sbiNo", null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    
    @GetMapping("/races")
    public ResponseEntity<List<Map<String, String>>> getRaceCodes() {
        List<Map<String, String>> races = inmateService.getDistinctRaces();
        return ResponseEntity.ok(races);
    }

    @GetMapping("/fetchCityStateZip")
    public ResponseEntity<List<Map<String, String>>> fetchCityStateZip(@RequestParam String search) {
        List<Map<String, String>> cityStateZipData = inmateService.fetchCityStateZip(search);
        return ResponseEntity.ok(cityStateZipData);
    }
    
    @GetMapping("/fetchAllCityStateZip")
    public ResponseEntity<List<Map<String, String>>> fetchAllCityStateZip() {
        List<Map<String, String>> cityStateZipData = inmateService.fetchAllCityStateZip();
        return ResponseEntity.ok(cityStateZipData);
    }
    
    @GetMapping("/admissionReleaseCodes")
    public ResponseEntity<List<Map<String, String>>> getAdmissionReleaseCodes() {
        List<Map<String, String>> admissionCodes = inmateService.getDistinctAdmissionReleaseCodes();
        return ResponseEntity.ok(admissionCodes);
    }
    
    @GetMapping("/offenderTypes")
    public ResponseEntity<List<Map<String, String>>> getOffenderTypes() {
        List<Map<String, String>> offenderTypes = inmateService.getDistinctOffenderTypes();
        return ResponseEntity.ok(offenderTypes);
    }
    
    @GetMapping("/priorIncarceration")
    public ResponseEntity<List<Map<String, Object>>> getPriorIncarceration(@RequestParam String sbiNo) {
        List<Map<String, Object>> priorData = inmateService.getPriorIncarceration(sbiNo);
        return ResponseEntity.ok(priorData);
    }
    
    @GetMapping("/inmateLookup")
    public ResponseEntity<List<Map<String, Object>>> getInmateLookup(
            @RequestParam(required = false) String sbiNo,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String suffixName,
            @RequestParam(required = false) String dob) { 
        List<Map<String, Object>> lookupData = inmateService.getInmateLookupDetails(sbiNo, lastName, firstName, middleName, suffixName, dob);
        return ResponseEntity.ok(lookupData);
    }
    
    @PostMapping("/saveMugshot")
    public ResponseEntity<Map<String, String>> saveMugshot(@RequestBody Map<String, String> payload) {
        try {
            String sbiNo = payload.get("sbiNo");
            String imageData = payload.get("imageData");

            inmateService.saveMugshotImage(sbiNo, imageData);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Mugshot saved successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full error
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error saving mugshot: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
}
