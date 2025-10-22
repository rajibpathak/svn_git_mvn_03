package com.omnet.cnt.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import com.omnet.cnt.Model.Inmatechargebean;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.omnet.cnt.Service.InmateChargesService;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.omnet.cnt.Service.InmateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collections;



@RestController
@RequestMapping("/charges")
public class InmateChargeController {
	
	@Autowired
    private InmateChargesService committedChargeService;
	@Autowired
    private InmateService inmateService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	@GetMapping("/getCommitNoBySbi") // CHANGED from @PostMapping to @GetMapping
	public ResponseEntity<?> getCommitNoBySbi(@RequestParam String sbiNumber) {
	    if (sbiNumber == null || sbiNumber.trim().isEmpty()) {
	        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "SBI Number is required."));
	    }
	    String commitNo = inmateService.validCommitNo(sbiNumber);
	    if (commitNo == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No valid Commit No found for the given SBI Number."));
	    }
	    return ResponseEntity.ok(Collections.singletonMap("commitNo", commitNo));
	}
	
	@PostMapping("/getbailtype")

	public List<Map<String, Object>> getbailtype() {

		String query = "Select bail_tp_cd,bail_tp_desc from bail_type_mst where status='A' Order by bail_tp_desc";

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

		return result;

	}
	@PostMapping("/getdisposition")

	public List<Map<String, Object>> getdisposition() {

		String query = "Select Disposition_code, Disposition_desc  from  Disposition_rt  where status='A' ";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

		return result;

	}

    @PostMapping("/insert")
    public ResponseEntity<?> insertCommittedCharges(@RequestBody List<Inmatechargebean> insertRows) {
    		 Inmatechargebean first = insertRows.get(0);
    		// System.out.println("Received Inmatechargebean JSON:");
    // System.out.println("COMMIT_NO: " + first.getCommitNo());
  System.out.println("Startdate: " + first.getCourtCode());
    	committedChargeService.insertCommittedCharges(insertRows);
        return ResponseEntity.ok("Charges inserted successfully");
    }

    // PUT: Update committed charges
    @PutMapping("/update")
    public ResponseEntity<String> updateCommittedCharges(@RequestBody List<Inmatechargebean> updateRows) {
    	 Inmatechargebean first = updateRows.get(0);
		// System.out.println("Received Inmatechargebean JSON:");
 System.out.println("Startdate: " + first.getCourtCode());
    	committedChargeService.updateCommittedCharges(updateRows);
        return ResponseEntity.ok("Charges updated successfully");
    }
    
    @PostMapping("/getInmateChargesSearch")
    public List<Map<String, Object>> getInmateChargesSearch(@RequestBody Map<String, String> requestData) {
    	 //System.out.println("Received requestData JSON:"+requestData.get("casenum"));
        return committedChargeService.getInmateChargesSearch(requestData);
    }
    
} 