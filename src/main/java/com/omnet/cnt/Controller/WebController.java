package com.omnet.cnt.Controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.omnet.cnt.Model.ForgetPassword;
import com.omnet.cnt.Model.Otp;
import com.omnet.cnt.Repository.ForgetPasswordRepository;
import com.omnet.cnt.Repository.OtpRepository;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.Service.ForgetPasswordService;
import com.omnet.cnt.Service.LoginService;
import com.omnet.cnt.Service.OtpService;
import com.omnet.cnt.Service.UserService;
import com.omnet.cnt.classes.TokenManager;

@Controller
public class WebController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private OtpRepository otpRepository;
	
	@Autowired
	private LoginController logincontroller;
	@Autowired
	private UserRepository userepo;
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ForgetPasswordRepository forgetpasswordrepository;
	
	@Autowired
	private ForgetPasswordService forgetpasswordservice;
	
	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getScreenData(String URL) {
		String query = "select * from omnet_screens where screen_url = '" + URL + "' and status = 'A'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	public String screenName(String URL) {
		List<Map<String, Object>> screenData = getScreenData(URL);
		String screen = screenData.get(0).get("SCREEN_NAME").toString().toUpperCase();
		return screen;
	}
	
	public String screenCode(String URL) {
		List<Map<String, Object>> screenData = getScreenData(URL);
		String screencode = screenData.get(0).get("SCREEN_CODE").toString();
		return screencode;
	}
	
	@GetMapping("/")
	public String viewIndexPage(Model model) {
		model.addAttribute("header", "Maven Generate War");
		return "index";
	}
	
	@GetMapping(value= "demotemplate")
	public String demotemplate(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
    	model.addAttribute("username", username);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "demotemplate";
	}
	
	@GetMapping(value = "/LoginValidation-{token}")
	  public String LoginValidation(@PathVariable String token, Model model) {
	      if (otpService.isValidToken(token)) {
	    	  String userId = otpService.getuserIdForToken(token);
	    	  System.out.println("webcontrollerusernamesdfs"+userId);
	          model.addAttribute("userId", userId);	          
	    	  String username = otpService.getuserIdForToken(token);
	    	  System.out.println("webcontrollerusernamesdfs"+username);

	    	  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	          System.out.println("fullName"+fullName);
	           String result = fullName.orElse("");
	       	model.addAttribute("username", result);         
	    	  String usernamee = otpService.getuserIdForToken(token);
	    	  System.out.println("webcontrollerusernamesdfs"+usernamee);
	    	       
	          return "/LoginValidation";
	      } else {
	          return "redirect:/"; 
	      }
	  }
	
	@GetMapping(value = "/PasswordChange-{token}")
	public String PasswordChange(@PathVariable String token, Model model) {
		if (tokenManager.isValidPasswordToken(token)) {

			String userId = otpService.getuserIdForToken(token);
	          model.addAttribute("userId", userId);	  
			String username = otpService.getuserIdForToken(token);

			  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
		      System.out.println("fullName"+fullName);
		       String result = fullName.orElse("");
		   	model.addAttribute("username", result);
			String username1 = otpService.getuserIdForToken(token);
	          model.addAttribute("username", username1);	  
	          return "/PasswordChange";
	    } else {
	          return "redirect:/"; 
	    }
	}
		
	@GetMapping("/removeOtpValidationAttribute")
	public ResponseEntity<String> removeOtpValidationAttribute(HttpSession session) {
		List<Otp> otpList = otpRepository.findOtpsByStatus("A");
		for (Otp otp : otpList) {
	        otp.setStatus("I");
	        Date currentDate = new Date();
			otp.setUpdatedDateTime(currentDate);
	        otpService.saveOrUpdate(otp); 
	    }
		
	    session.removeAttribute("Otp Validation");
	    return ResponseEntity.ok("Session attribute removed");
	}
	
	@GetMapping("/removeOtpValidation")
	public ResponseEntity<String> removeOtpValidation(HttpSession session){
		List<ForgetPassword> ForgetPasswordList = forgetpasswordrepository.findOtpsByStatus("A");
		for (ForgetPassword ForgetPassword : ForgetPasswordList) {
			ForgetPassword.setStatus("I");
	        Date currentDate = new Date();
	        ForgetPassword.setUpdatedDateTime(currentDate);
	        forgetpasswordservice.saveOrUpdate(ForgetPassword); 
	    }

		 session.removeAttribute("ForgetPassword");
		    return ResponseEntity.ok("Session attribute removed");
		
	}
	
	@GetMapping(value = "/ChangePassword")
	public String ChangePassword(Model model) {
		return "ChangePassword";
	}
             

    @GetMapping(value = "/OmnetDesktop")
	public String OmnetDesktop(Model model, HttpSession session) {
    	session.removeAttribute("sbiNumber");
    	String username = logincontroller.getUsernamesecurity();

  	  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
        System.out.println("fullName"+fullName);
         String result = fullName.orElse("");
     	model.addAttribute("username", result);
		return "OmnetDesktop";
	}
    
   

	@GetMapping(value = "/IncidentInformation")
	public String IncidentInformation(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("IncidentInformation"));
	    model.addAttribute("screenCode", screenCode("IncidentInformation"));
		return "IncidentInformation";
	}

	@GetMapping(value = "/VitalStatistics")
	public String VitalStatistics(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("VitalStatistics"));
	    model.addAttribute("screenCode", screenCode("VitalStatistics"));
	     return "VitalStatistics";
	}
	
	@GetMapping(value = "/HousingEntityWaitlist")
	public String Housingentitywaitlist(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		/*
		 * String sbiNumber = (String) session.getAttribute("sbiNumber");
		 * model.addAttribute("sbiNumber", sbiNumber);
		 */
	    model.addAttribute("screenName", screenName("HousingEntityWaitlist"));
	     return "HousingEntityWaitlist";
	}
  
	
	@GetMapping("/UserSetup")
	public String UserSetup(Model model) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "UserSetup";		
	}

	
	@GetMapping("/AddImages")
	public String AddImages(Model model) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "AddImages";		
	}
	
	@GetMapping(value = "/RoleSetup")
	public String RoleSetup(Model model) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "RoleSetup";
	}


	@GetMapping(value = "IntakeMenu")
	public String IntakeMenu(Model model) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "IntakeMenu";
	}


	
	private boolean isExcludedPage(String pageName) {
	    List<String> excludedPages = Arrays.asList("ChangePassword", "PasswordChange","LoginValidation","MedicalInformationESC_CRME","ForgotPassword","UserSetup","RoleSetup");
	    return excludedPages.contains(pageName);

	}
	
	@GetMapping(value = "/{pageName}")
	public String dynamicPageLoader(@PathVariable("pageName") String pageName, Model model, HttpSession session) {
		if (isExcludedPage(pageName)) {
	        return "redirect:/OmnetDesktop"; 
	    }
	    String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
	    String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    return pageName; 
	}

    
    @GetMapping(value = "/FamilyEmergencyContactInfo")
 	public String FamilyEmergencyContactInfo(Model model, HttpSession session) {
     	session.removeAttribute("sbiNumber");
     	String username = logincontroller.getUsernamesecurity();

  	  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
        System.out.println("fullName"+fullName);
         String result = fullName.orElse("");
     	model.addAttribute("username", result);
 		return "FamilyEmergencyContactInfo";
 	}


	@GetMapping(value = "ScreenAccess")
	public String ScreenAccess(Model model) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "ScreenAccess";
	}
	



	@GetMapping(value = "/ScreenSetup")
	public String screenSetup(Model model) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "ScreenSetup";
	}
	




	@GetMapping(value = "MenuSetup")
	public String MenuSetup(Model model) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "MenuSetup";
	}





	@GetMapping(value = "ForgotPassword")
	public String ForgetPassword(Model model) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "ForgotPassword";
	}

	@GetMapping(value = "AddCategory")
	public String AddCategory(Model model) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
		return "AddCategory";
	}

	@GetMapping(value = "AliasInformation")
	public String AliasInformation(Model model, HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
		System.out.println("fullName" + fullName);
		String result = fullName.orElse("");
		System.out.println("result is " + result);
		model.addAttribute("username", result);
		String sbiNumber = (String) session.getAttribute("sbiNumber");
		model.addAttribute("sbiNumber", sbiNumber);
		model.addAttribute("screenName", screenName("AliasInformation"));
		
		System.out.println("Before return");
		return "AliasInformation";
	}
	
	@GetMapping(value= "DrugDnaTest")
	public String DrugDnaTest(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("userId", username);
		return "DrugDnaTest";
	}
	
	@GetMapping(value= "SentenceOrder")
	public String SentenceOrder(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "SentenceOrder";
	}

	@GetMapping(value= "SentenceCalc")
	public String SentenceCalc(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "sentenceCalc";
	}

	@GetMapping(value = "Demographics")
	public String Demographics(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("currUser", username);
	    model.addAttribute("screenName", screenName("Demographics"));
		return "Demographics";
	}
	
	@GetMapping(value = "BookingNavigation")
	public String BookingNavigation(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("BookingNavigation"));
		return "BookingNavigation";
	}

	@GetMapping(value = "CSMCaseNotes")
	public String CSMCaseNotes(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("CSMCaseNotes"));
	    model.addAttribute("screenCode", screenCode("CSMCaseNotes"));
		return "CSMCaseNotes";
	}
	
	@GetMapping(value = "PrintStatusSheet")
	public String PrintStatusSheet(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	 String sbiNumber = (String) session.getAttribute("sbiNumber");
    	    model.addAttribute("sbiNumber", sbiNumber);
    	    System.out.println("sbiNumbersfse"+  sbiNumber);
    	    model.addAttribute("screenName", screenName("PrintStatusSheet"));
    	    model.addAttribute("screenCode", screenCode("PrintStatusSheet"));
		return "PrintStatusSheet";
	}

	@GetMapping(value = "OffenderHousingDetailsHistory")
	public String OffenderHousingDetailsHistory(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "OffenderHousingDetailsHistory";
	}
	@GetMapping(value = "IndividualOffenderMovement")
	public String IndividualOffenderMovement(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "IndividualOffenderMovement";
	}
	
	@GetMapping(value = "HousingEntityDetailsHistory")
	public String HousingEntityDetailsHistory(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "HousingEntityDetailsHistory";
	}
	
	@GetMapping(value = "OffenderGrievance")
	public String OffenderGrievance(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("OffenderGrievance"));
	    model.addAttribute("screenCode", screenCode("OffenderGrievance"));
		return "OffenderGrievance";
	}
	
	@GetMapping(value = "Booking")
	public String Booking(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("Booking"));
	    model.addAttribute("screenCode", screenCode("Booking"));
		return "Booking";
	}
	
	@GetMapping(value = "InmateChargesDockets")
	public String InmateChargesDockets(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("InmateChargesDockets"));
	    model.addAttribute("screenCode", screenCode("InmateChargesDockets"));
		return "InmateChargesDockets";
	}
	
	
	@GetMapping(value = "OffenderInformation")
	public String offenderInformaion(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("offenderInformation"));
	    model.addAttribute("screenCode", screenCode("offenderInformation"));
		return "offenderInformation";
	}


	
	
	@GetMapping(value = "MedicalInformationESC_"
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ "         "
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ ""
			+ "CRME")
	public String MedicalInformationESC_CRME(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "MedicalInformationESC_CRME";
	}

	@GetMapping(value = "OrientationChecklistCBI_ORTA")
	public String OrientationChecklistCBI_ORTA(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "OrientationChecklistCBI_ORTA";
	}
	
	@GetMapping(value = "NoContactsCBINOCT")
	public String NoContactsCBINOCT(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("NoContactsCBINOCT"));
		return "NoContactsCBINOCT";
	}
	
	@GetMapping(value = "OffenderSummary")
	public String OffenderSummary(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "OffenderSummary";
	}
	
	@GetMapping(value = "/DisciplinaryHearing")
	public String DisciplinaryHearing(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "DisciplinaryHearing";
	}
	
	@GetMapping(value = "VOPViolationReport")
	public String VOPViolationReport(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "VOPViolationReport";
	}

	@GetMapping(value = "AssignOffenderBed")
	public String AssignOffenderBed(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("AssignOffenderBed"));
	    model.addAttribute("screenCode", screenCode("AssignOffenderBed"));
		return "AssignOffenderBed";
	}

	@GetMapping(value = "/IncidentInformationDIS")
	public String IncidentInformationDIS(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "IncidentInformationDIS";
	}

	@GetMapping(value = "/OffenderWithoutBedAssignment")
	public String OffenderWithoutBedAssignment(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "OffenderWithoutBedAssignment";
	}
	
	@GetMapping(value = "WarrantCheck")
	public String WarrantCheck(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "WarrantCheck";
	}
	
	@GetMapping(value = "ScarsMarksTattoo")
	public String ScarsMarksTattoo(Model model, HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
		System.out.println("fullName" + fullName);
		String result = fullName.orElse("");
		model.addAttribute("username", result);
		String sbiNumber = (String) session.getAttribute("sbiNumber");
		model.addAttribute("sbiNumber", sbiNumber);
		return "ScarsMarksTattoos";
	}

	
	@GetMapping(value = "/HousingFacilityBedAssignment")
	public String HousingFacilityBedAssignment(Model model) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
	   	model.addAttribute("screenName", screenName("HousingFacilityBedAssignment"));
		return "HousingFacilityBedAssignment";
	}

	
	
	@GetMapping(value = "/PropertyTracking")
	public String PropertyTracking(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
  	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    //System.out.println(screenName("propertyTracking"));
	    model.addAttribute("screenName", screenName("PropertyTracking"));
		return "propertyTracking";
	}
	


	@GetMapping(value = "MovementLogScreen")
	public String MovementLogScreen(Model model) {
	String username = logincontroller.getUsernamesecurity();
	  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
    System.out.println("fullName"+fullName);
     String result = fullName.orElse("");
 	model.addAttribute("username", result);
 	model.addAttribute("screenName", screenName("MovementLogScreen"));
	return "MovementLogScreen";
	}
	
	@GetMapping(value = "SwapOffenderBeds")
	public String SwapOffenderBeds(Model model) {
	String username = logincontroller.getUsernamesecurity();
	  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
    System.out.println("fullName"+fullName);
     String result = fullName.orElse("");
 	model.addAttribute("username", result);
 	model.addAttribute("screenName", screenName("SwapOffenderBeds"));
	return "SwapOffenderBeds";
	}
	
	@GetMapping(value = "securityThreatGroup")
	public String SecurityThreatGroup(Model model,HttpSession session) {
	String username = logincontroller.getUsernamesecurity();
	  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
       String result = fullName.orElse("");
       String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
   	model.addAttribute("username", result);
    model.addAttribute("screenName", screenName("securityThreatGroup"));
    model.addAttribute("screenCode", screenCode("securityThreatGroup"));
	return "securityThreatGroup";
	}


	@GetMapping(value = "UserNotifications")
	public String UserNotifications(Model model) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
	   	model.addAttribute("screenName", screenName("UserNotifications"));
	return "UserNotifications";
}


	@GetMapping(value= "Disciplinaryinformation")
	public String Disciplinaryinformation(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
    	model.addAttribute("username", username);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "Disciplinaryinformation";
	}
	
	@GetMapping(value= "ClassificationSummary")
	public String ClassificationSummary(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
    	model.addAttribute("username", username);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "ClassificationSummary";
	}
	
	@GetMapping(value = "offenderInfo")
	public String offenderInfo(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "offenderInfo";
	}
	
	@GetMapping(value = "disciplinaryInfo")
	public String disciplinaryInfo(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();
    	model.addAttribute("username", username);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
		return "disciplinaryInfo";
	}

	@GetMapping(value = "EmergencyAttention")
	public String EmergencyAttention(Model model,HttpSession session) {
		String username = logincontroller.getUsernamesecurity();

		  Optional<String> fullName = userepo.findConcatenatedNameByUserId(username);
	      System.out.println("fullName"+fullName);
	       String result = fullName.orElse("");
	   	model.addAttribute("username", result);
    	String sbiNumber = (String) session.getAttribute("sbiNumber");
	    model.addAttribute("sbiNumber", sbiNumber);
	    model.addAttribute("screenName", screenName("EmergencyAttention"));
	    model.addAttribute("screenCode", screenCode("EmergencyAttention"));
		return "EmergencyAttention";
	}
}