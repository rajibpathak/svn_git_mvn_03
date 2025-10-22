package com.omnet.cnt.Controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.omnet.cnt.Model.Enemy;
import com.omnet.cnt.Model.ReportingSource;
import com.omnet.cnt.Model.ReportingType;
import com.omnet.cnt.Service.BedService;
import com.omnet.cnt.Service.InmateService;
import com.omnet.cnt.Service.ReportingSourceSerivce;
import com.omnet.cnt.Service.ReportingTypeService;

@RestController
public class NoContactContorller {
	@Autowired
    private ReportingSourceSerivce reportingSourceSerivce;
	
	@Autowired
	private BedService bedService;
	
	@Autowired
	private InmateService inmateService;
	
    @GetMapping("/sourcedropdown")
    public List<ReportingSource> getDropdownOptions() 
    {
        return reportingSourceSerivce.sourceDropDown();
    }
    
    @Autowired
    private ReportingTypeService reportingTypeService;

    @GetMapping("/typedropdown")
    public List<ReportingType> getTypeDropDown() {
        return reportingTypeService.typeDropDown();
    }
    
    @GetMapping("/getNoContactLocation")
	public ResponseEntity<Map<String, Object>> getLocation(@RequestParam String commitNo) {
	    try {
	        Map<String, Object> locationDetails = bedService.noContactLocation(commitNo); // Use commitNo dynamically
	        if (locationDetails != null) {
	            return ResponseEntity.ok(locationDetails);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@PostMapping(value = "insertNoContactDetails", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> insertNocDetails(@RequestBody List<Enemy> noContactDetails) throws SQLException {
	    bedService.insertNoContactDetails(noContactDetails);
	    return ResponseEntity.ok("Saved successfully");
	}
	
	
	@PostMapping("/updateNoContactDetails")
	@ResponseBody
	public ResponseEntity<String> updateNocDetails(@RequestBody List<Enemy> updatedRows) {
	    bedService.updateNoContactDetails(updatedRows);
	    return ResponseEntity.ok("Updated successfully");
	}
	
	@RequestMapping(value = "/addNoContactsInmateInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> checkSbiNoAndLocation(@RequestParam(name = "parameter") String parameter,
	@RequestParam(required = false) String commitNo) {

	String sbiValidityResult = inmateService.validSbiNo(parameter);
	System.out.println("sbiValidityResultefe" + sbiValidityResult);
	if (!"Y".equals(sbiValidityResult)) {
	return ResponseEntity.status(400).body("SBI Number is not valid");
	}
	commitNo = inmateService.validCommitNo(parameter);
	List<Object> inmateList = inmateService.findCustomInmateData(commitNo, parameter);
	/*
	* Map<String, Object> locationDetails =
	* inmateService.callGetLocationDetails(parameter, commitNo);
	*/
	Map<String, Object> responseMap = new HashMap<>();
	
	responseMap.put("inmateList", inmateList);
	responseMap.put("commitNo", commitNo);
	return ResponseEntity.ok(responseMap);
	}

}
