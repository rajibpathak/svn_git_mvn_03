package com.omnet.cnt.Controller;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import com.omnet.cnt.Model.Bed;
import com.omnet.cnt.Model.BedCell;
import com.omnet.cnt.Model.BedList;
import com.omnet.cnt.Model.BookingDetail;
import com.omnet.cnt.Model.CSMViewLocation;
import com.omnet.cnt.Model.Court;
import com.omnet.cnt.Model.Enemy;
import com.omnet.cnt.Model.HousingFacilityBedAssignments;
import com.omnet.cnt.Model.Judges;
import com.omnet.cnt.Model.Menu;
import com.omnet.cnt.Model.MenuCategory;
import com.omnet.cnt.Model.NotificationOmnetDesktop;
import com.omnet.cnt.Model.OmnetDesktop;
import com.omnet.cnt.Model.RecentScreen;
import com.omnet.cnt.Model.ReferenceValue;
import com.omnet.cnt.Model.Rowdata;
import com.omnet.cnt.Model.Screens;
import com.omnet.cnt.Model.SecCdRec;
import com.omnet.cnt.Model.Statute;
import com.omnet.cnt.Model.SubMenu;
import com.omnet.cnt.Model.User_Roles;
import com.omnet.cnt.Model.Users;
import com.omnet.cnt.Model.warrantCheck;
import com.omnet.cnt.Repository.Bookingrepository;
import com.omnet.cnt.Repository.InmateRepository;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.Service.BedService;
import com.omnet.cnt.Service.BookingService;
import com.omnet.cnt.Service.CountDetailService;
import com.omnet.cnt.Service.CourtService;
import com.omnet.cnt.Service.EmailService;
import com.omnet.cnt.Service.HousingFacilityBedAssignmentsService;
import com.omnet.cnt.Service.InmateService;
import com.omnet.cnt.Service.JudgeService;
import com.omnet.cnt.Service.LoginService;
import com.omnet.cnt.Service.OffenderHousingDetailsService;
import com.omnet.cnt.Service.OmnetDesktopService;
import com.omnet.cnt.Service.PropertyTrackingService;
import com.omnet.cnt.Service.ScreensService;
import com.omnet.cnt.Service.SentenceLevelService;
import com.omnet.cnt.Service.StatuteService;
import com.omnet.cnt.Service.UserService;
import com.omnet.cnt.Service.UserService.location;
import com.omnet.cnt.Service.VitalService;
import com.omnet.cnt.Service.stgservice;
import com.omnet.cnt.Service.warrantCheckService;
import com.omnet.cnt.classes.CustomUserDetails;
import com.omnet.cnt.classes.InmateData;
import com.omnet.cnt.classes.PropertyTrackingHdr;
import com.omnet.cnt.classes.PropertyTrackingColtItems;

@RestController
public class RestfulController {
	@Autowired
	private InmateService inmateService;	

	@Autowired
	private UserService userService;

	@Autowired
	private OmnetDesktopService Desktops;

	@Autowired
	private HttpSession session;
   
	@Autowired
	private OffenderHousingDetailsService offenderhousingdetailsservice;

	@Autowired
	private ScreensService screensService;

	@Autowired
	private EmailService emailService;


	@Autowired
	public LoginService logindata;

	@Autowired
	private VitalService vitalservice;
	
	@Autowired
	private BedService bedService;
	 


	@Autowired
	private stgservice stgservice;

	@Autowired
	private Bookingrepository bookingrepo;

	@Autowired
	private HousingFacilityBedAssignmentsService HousingFacility;

	@Autowired
	private SentenceLevelService sentencelevelservice;

	@Autowired
	private LoginService loginService;

	@Autowired
	private CountDetailService countdetailservice;

	@Autowired
	private warrantCheckService WC;

	@Autowired
	private InmateRepository inmateRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private BookingService bookingservice;

	@Autowired
	private PropertyTrackingService propTracking;
	// @Autowired
	// private Users myUserCurr;
	
	@Autowired
	private CourtService courtService;
	
	@Autowired
	private JudgeService judgeService;
	
	@Autowired
	private StatuteService statuteService;

	String username;
	String userId;

	public String getCurrentUserLocation() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			return customUserDetails.getLocation();
		}

		return null;
	}

	public String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			String Userid = customUserDetails.getUserId();
			
			return Userid;
		}

		return null;
	}

	private static final Set<String> MOVEMENT_PAGES = new HashSet<>(
			Arrays.asList("IndividualOffenderMovement", "page2", "page3"));

	public boolean isMovementPage(String page) {
		return MOVEMENT_PAGES.contains(page);
	}

	@PostMapping(value = "/getAllUsers")
	public List<Users> getAllUsers() {
		return userService.getUsers();
	}

	@GetMapping("/UserLocation")
	public List<location> UserLocation() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		List<location> InstName = userService.getLocationsByUserId(userId);
		return InstName;
	}

	@GetMapping(value = "/getAccessScreen")
	public List<User_Roles> getAccessScreen(String userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		return userService.AccessScreen(userId);

	}

	@GetMapping(value = "/recentscreenvalues")
	public List<RecentScreen> getRecentlyAccessedScreen(String userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		return Desktops.AccessedScreen(userId);
	}

	@GetMapping(value = "/notifiaction")
	public List<NotificationOmnetDesktop> getnotificationvalue(String userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		return Desktops.getnotification(userId);
	}

	
		
		
	@PostMapping(value = "/Changedvalue")
	public void GetNotificationupdatedvalue(String userId, String user_mess_seq_num) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		Desktops.Notifyupdate(userId, user_mess_seq_num);
	}

	@GetMapping(value = "/Desktop")
	public List<OmnetDesktop> getOmnetDesktop(String userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		List<OmnetDesktop> deskinfo = Desktops.Deskinfo(userId);
		return deskinfo;
	}
	
	
	@GetMapping(value = "/DesktopCountValues")
	 public Map<String, Object> OMnetDesktop(String p_user_id) {
		
		 Map<String, Object> response = new HashMap<>();
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object details = authentication.getDetails();
		    Map<?, ?> detailsMap = (Map<?, ?>) details;
		    p_user_id = (String) detailsMap.get("userId");
         String p_inst_num=inmateService.getUserInstNum(p_user_id);
		 String Bedcount=Desktops.GetcountBed(p_inst_num);
		 String Population=Desktops.GetPopulation(p_inst_num);
		 String Admission=Desktops.GetAdmission(p_inst_num);
		 String Release=Desktops.GetReleaseCount(p_inst_num);
		 response.put("Bedcount", Bedcount);
		 response.put("Admission", Admission);
		 response.put("Release", Release);
		 response.put("Population", Population);
		return response;
	 }
	
	
	@GetMapping(value = "/Menu")
	public List<Menu> getMenu() {
		return userService.getMenu();
	}

	@GetMapping(value = "/getMenuCategory")
	public List<MenuCategory> getMenuCategory() {
		return userService.getMenuCategory();

	}

	@GetMapping(value = "/getSubMenu")
	public List<SubMenu> getSubMenu() {
		return userService.getSubMenu();

	}

	
	@PostMapping(value = "/getUsers")
	public List<Users> getUserById(@RequestParam(value = "userid", required = false) String userId) {
		return userService.getUserByName(userId);
	}

	

	@PutMapping(value = "/addUser")
	public void createUser(Users user) {
		System.out.println("Here inside put");
		userService.createUser(user);
		System.out.println("Step InsFlag :" + user.getInsFlag());
		if(user.getInsFlag().equals("Y")) {
			Users myuser = userService.createUser(user);
			System.out.println("update date time : " + myuser.getUserEmailAddress());
			System.out.println("initial password : " + myuser.getInitialPassword());
			emailService.sendEmail(myuser.getUserEmailAddress(), myuser.getUserFirstName(), myuser.getUserLastName(),
					myuser.getUserId(), myuser.getInitialPassword());
		}
	
	}

	@GetMapping(value = "/ScreenSetupData")
	public ResponseEntity<List<Screens>> getScreenSetup() {
		List<Screens> screens = screensService.findAllByOrderByScreenCodeAsc();

		if (screens.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(screens);
		}
	}

	@PostMapping(value = "/ScreenData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createOrUpdateScreen(@RequestBody Map<String, String> screens) {
		String screenId = screens.get("screenid");
		String screenName = screens.get("screenName");
		String url = screens.get("url");
		String status = screens.get("status");
		String updid = screens.get("updid");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		String userId = loginService.getUserIdByUsername(username);

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

		Optional<Screens> existingScreenOpt;

		if (!updid.equals("0")) {
			existingScreenOpt = screensService.findByScreenCode(updid);

			if (existingScreenOpt.isPresent()) {
				Screens existingScreen = existingScreenOpt.get();

				if (!screenId.equals(existingScreen.getScreenCode())) {
					Optional<Screens> duplicateScreenOpt = screensService.findByScreenCode(screenId);

					if (duplicateScreenOpt.isPresent()) {
						return ResponseEntity.ok("Screen code already exists.");
					}
				}

				if (!screenId.equals(existingScreen.getScreenCode())
						|| !screenName.equals(existingScreen.getScreenName())
						|| !url.equals(existingScreen.getScreenUrl()) || !status.equals(existingScreen.getStatus())) {

					existingScreen.setScreenCode(screenId);
					existingScreen.setScreenName(screenName);
					existingScreen.setScreenUrl(url);
					existingScreen.setStatus(status);
					existingScreen.setUpdatedDateTime(currentTimestamp);
					existingScreen.setUpdatedUserId(userId);

					screensService.updateScreen(existingScreen, updid);
					return ResponseEntity.ok("Screen updated successfully.");
				} else {
					return ResponseEntity.ok("Screen data already exists.");
				}
			}
		} else {
			Optional<Screens> duplicateScreenOpt = screensService.findByScreenCode(screenId);

			if (duplicateScreenOpt.isPresent()) {
				return ResponseEntity.ok("Screen code already exists.");
			}

			Screens newScreen = new Screens(screenId, screenName, url, status, userId, currentTimestamp);
			screensService.saveScreen(newScreen);
			return ResponseEntity.ok("Screen inserted successfully.");
		}

		return ResponseEntity.ok("Invalid request.");
	}

	
	/* Coded By Sunandhaa Begin */
	@RequestMapping(value = "/HistorySbiNo", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> validateSBINo(@RequestParam("parameter") String parameter,
			@RequestParam(required = false) String commitNo) {

		String sbiResult = inmateService.validSbiNo(parameter);
		Map<String, Object> responseMap = new HashMap<>();

		if ("Y".equals(sbiResult)) {
			commitNo = inmateService.validCommitNo(parameter);
			List<Object> OffenderList = inmateService.findOffenderName(commitNo, parameter);
			List<Object[]> HistoryList = inmateService.findInmateDetailsBySbiNo(parameter);
			responseMap.put("OffenderList", OffenderList);
			responseMap.put("HistoryList", HistoryList);
			responseMap.put("message", "SBI Number is valid");
			return ResponseEntity.ok(responseMap);
		} else {
			responseMap.put("error", "SBI Number is not valid");
			return ResponseEntity.status(400).body(responseMap);
		}
	}

	@RequestMapping(value = "/stayhistory", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> fetchStayHistory(@RequestParam("commit") String commitNumber,
			@RequestParam(required = false) String parameter) {
		{

			List<Object> inmateList = inmateService.findCustomInmateData(commitNumber, parameter);
			List<Map<String, Object>> output = vitalservice.VitalCheckboxProcedure(commitNumber);
			Map<String, Object> locationDetails = inmateService.callGetLocationDetails(parameter, commitNumber);
			String offenderCondition = inmateService.getInmateCondition(commitNumber);
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("output", output);
			responseMap.put("offenderCondition", offenderCondition);
			responseMap.put("locationDetails", locationDetails);
			responseMap.put("inmateList", inmateList);

			return ResponseEntity.ok(responseMap);
		}
	}

	@GetMapping("/clearSession")
	public ResponseEntity<String> clearSession(HttpSession session) {
		try {
			session.removeAttribute("sbiNumber");
			return ResponseEntity.ok("Session invalidated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to invalidate session: " + e.getMessage());
		}
	}


//---------------------Formheader API-----------------------------------------------------------------------------------------------//////	
	
	@RequestMapping(value = "/checkSbiNoAndLocation", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> checkSbiNoAndLocation(@RequestParam(name = "parameter") String parameter,
	                                               @RequestParam(required = false) String commitNo,
	                                               @RequestParam(name = "page") String page) {
	    String hardSbiNo = inmateService.HardSbi(parameter);

	    // If hard SBI number is available, override the parameter with it
	    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
	        parameter = hardSbiNo;
	    }

	    String sbiValidityResult = inmateService.validSbiNo(parameter);
	    System.out.println("sbiValidityResult: " + sbiValidityResult);
	    if (!"Y".equals(sbiValidityResult)) {
	        return ResponseEntity.status(400).body("SBI Number is not valid");
	    }

	    commitNo = inmateService.validCommitNo(parameter);
	    session.setAttribute("sbiNumber", parameter);

	    if (isMovementPage(page)) {
	        String currentUserId = getCurrentUserId();
	        String userLocation = userRepo.findInstNumByUserId(currentUserId);
	        String offenderInst = inmateRepo.getCurrentInstNumByCommitNo(commitNo);

	        if (userLocation.equals(offenderInst)) {
	            String offenderCondition = inmateService.getInmateCondition(commitNo);
	            List<Map<String, Object>> output = vitalservice.VitalCheckboxProcedure(commitNo);
	            List<Object> inmateList = inmateService.findCustomInmateData(commitNo, parameter);
	            Map<String, Object> locationDetails = inmateService.callGetLocationDetails(parameter, commitNo);

	            Map<String, Object> responseMap = new HashMap<>();
	            responseMap.put("output", output);
	            responseMap.put("locationDetails", locationDetails);
	            responseMap.put("inmateList", inmateList);
	            responseMap.put("offenderCondition", offenderCondition);
	            responseMap.put("commitNo", commitNo);
	            responseMap.put("Hard_SBI_NO", hardSbiNo);

	            return ResponseEntity.ok(responseMap);
	        } else {
	            return ResponseEntity.status(400).body("The offender is not in your facility");
	        }
	    }

	    String offenderCondition = inmateService.getInmateCondition(commitNo);
	    List<Map<String, Object>> output = vitalservice.VitalCheckboxProcedure(commitNo);
	    List<Object> inmateList = inmateService.findCustomInmateData(commitNo, parameter);
	    Map<String, Object> locationDetails = inmateService.callGetLocationDetails(parameter, commitNo);

	    Map<String, Object> responseMap = new HashMap<>();
	    responseMap.put("output", output);
	    responseMap.put("locationDetails", locationDetails);
	    responseMap.put("inmateList", inmateList);
	    responseMap.put("offenderCondition", offenderCondition);
	    responseMap.put("commitNo", commitNo);
	    responseMap.put("Hard_SBI_NO", hardSbiNo);

	    return ResponseEntity.ok(responseMap);
	}
	
	@GetMapping("/getStayHistory")
	public ResponseEntity<List<Map<String, Object>>> getStayHistory(@RequestParam String sbiNo,
			@RequestParam String docIdFlag) {
	    String hardSbiNo = inmateService.HardSbi(sbiNo);

	    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
	    	sbiNo = hardSbiNo;
	    }		
		List<Map<String, Object>> stayHistoryList = inmateService.callStayHistoryQuery(sbiNo, docIdFlag);
		return new ResponseEntity<>(stayHistoryList, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/checkSbiNoAndLocationHistory", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> checkSbiNoAndLocationHistory(@RequestParam(name = "parameter") String parameter,
			@RequestParam(required = true) String commitNo, @RequestParam(name = "page") String page) {
		
		 String hardSbiNo = inmateService.HardSbi(parameter);

		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		        parameter = hardSbiNo;
		    }
		    
		String sbiValidityResult = inmateService.validSbiNo(parameter);		
		if (!"Y".equals(sbiValidityResult)) {
			return ResponseEntity.status(400).body("SBI Number is not valid");
		}			
		session.setAttribute("sbiNumber", parameter);		
		if (isMovementPage(page)) {
			String currentUserId = getCurrentUserId();
			String userLocation = userRepo.findInstNumByUserId(currentUserId);
			String offenderInst = inmateRepo.getCurrentInstNumByCommitNo(commitNo);

			if (userLocation.equals(offenderInst)) {
				String offenderCondition = inmateService.getInmateCondition(commitNo);				
				List<Map<String, Object>> output = vitalservice.VitalCheckboxProcedure(commitNo);
				List<Object> inmateList = inmateService.findCustomInmateData(commitNo, parameter);
				Map<String, Object> locationDetails = inmateService.callGetLocationDetails(parameter, commitNo);
				
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("output", output);
				responseMap.put("locationDetails", locationDetails);
				responseMap.put("inmateList", inmateList);
				responseMap.put("offenderCondition", offenderCondition);

				return ResponseEntity.ok(responseMap);
			} else {
				return ResponseEntity.status(400).body("The offender is not in your facility");
			}
		}
		String offenderCondition = inmateService.getInmateCondition(commitNo);
		List<Map<String, Object>> output = vitalservice.VitalCheckboxProcedure(commitNo);
		List<Object> inmateList = inmateService.findCustomInmateData(commitNo, parameter);
		Map<String, Object> locationDetails = inmateService.callGetLocationDetails(parameter, commitNo);
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("output", output);
		responseMap.put("locationDetails", locationDetails);
		responseMap.put("inmateList", inmateList);
		responseMap.put("offenderCondition", offenderCondition);

		return ResponseEntity.ok(responseMap);
	}
//-------------------------------------------------PrintStatusSheet---------------------------------------------------------------------//
	@GetMapping("/Sentencelevel")
	public ResponseEntity<Map<String, Object>> findByCommitNo(@RequestParam String parameter,
	                                                          @RequestParam(required = false) String commitNumber) {
		
		 String hardSbiNo = inmateService.HardSbi(parameter);
		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		        parameter = hardSbiNo;
		    }
		    
	    String idValidityResult = inmateService.validSbiNo(parameter);
	    if ("Y".equals(idValidityResult)) {
	        if (commitNumber == null) {
	            commitNumber = inmateService.validCommitNo(parameter);
	        }	       
	        List<String> detentionerDetails = sentencelevelservice.getDetentionerByCommitNo(commitNumber);
	        List<Map<String, Object>> sentenceLevels = sentencelevelservice.getSentenceLevels(commitNumber); 
	       
	        Map<String, Object> response = new HashMap<>();
	        response.put("alertResult", sentenceLevels);
	        response.put("activeInmates", detentionerDetails);
	       
	        return ResponseEntity.ok(response);
	    } else {
	        throw new IllegalArgumentException("SBI Number is not valid");
	    }
                                                
	}
	
	@GetMapping("/Sentencelevelemg")
	public ResponseEntity<Map<String, Object>> findByCommitNoemg(@RequestParam String parameter,
	                                                          @RequestParam(required = false) String commitNumber) {
		 String hardSbiNo = inmateService.HardSbi(parameter);

		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		        parameter = hardSbiNo;
		    }
		    
	    String idValidityResult = inmateService.validSbiNo(parameter);
	    if ("Y".equals(idValidityResult)) {
	        if (commitNumber == null) {
	            commitNumber = inmateService.validCommitNo(parameter);
	        }	        
	        List<Object[]> printScreenData = sentencelevelservice.findByCommitNo(commitNumber);
	        List<Object[]> results = inmateService.getActiveInmates();	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("alertResult", printScreenData);
	        response.put("activeInmates", results);
	        return ResponseEntity.ok(response);
	    } else {
	        throw new IllegalArgumentException("SBI Number is not valid");
	    }
	}

	@GetMapping("/SentencelevelStay")
	public ResponseEntity<Map<String, Object>> findByCommit(@RequestParam String parameter,
            @RequestParam(required = true) String commitNumber) {
		 String hardSbiNo = inmateService.HardSbi(parameter);

		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		        parameter = hardSbiNo;
		    }
		    
		String IdValidityResult = inmateService.validSbiNo(parameter);
		if ("Y".equals(IdValidityResult)) {
			List<String> detentionerDetails = sentencelevelservice.getDetentionerByCommitNo(commitNumber);
			  List<Map<String, Object>> sentenceLevels = sentencelevelservice.getSentenceLevels(commitNumber);       
		        Map<String, Object> response = new HashMap<>();
		        response.put("alertResult", sentenceLevels);
		        response.put("activeInmates", detentionerDetails);
		        return ResponseEntity.ok(response);
		} else {
			throw new IllegalArgumentException("SBI Number is not valid");
		}
	}
//---------------------------------------------------OffenderInformation---------------------------------------------------------//
	
	@GetMapping("/offenderactivitystats")
	public ResponseEntity<Map<String, Object>> offenderactivity(
	        @RequestParam String parameter, 
	        @RequestParam(required = false) String commitNumber, 
	        @RequestParam String userId) {
		 String hardSbiNo = inmateService.HardSbi(parameter);

		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		        parameter = hardSbiNo;
		    }
	    String IdValidityResult = inmateService.validSbiNo(parameter);
	    if ("Y".equals(IdValidityResult)) {
	        commitNumber = inmateService.validCommitNo(parameter);
	        Map<String, Object> result = countdetailservice.calloffenderactivitystats(commitNumber);
	        List<SecCdRec> resultset = countdetailservice.callSpSecCdQueryObj(commitNumber);
	        ResponseEntity<Map<String, Object>> housingResponse = offenderhousingdetailsservice.executeOHDHQuery(commitNumber);
	        Map<String, Object> housingDetails = housingResponse.getBody();
	        String docIdFlag ="N";
	        List<Map<String, Object>> stayHistoryList = inmateService.callStayHistoryQuery(parameter, docIdFlag);
	        Map<String, Object> responseMap = new HashMap<>();
	        responseMap.put("locationDetails", result);
	        responseMap.put("securitycode", resultset);
	        responseMap.put("stayHistoryList", stayHistoryList);
	        responseMap.put("housingDetails", housingDetails.get("housingDetails")); 
	        return ResponseEntity.ok(responseMap);
	    } else {
	        throw new IllegalArgumentException("SBI Number is not valid");
	    }
	}

	 

	@GetMapping("/Splocation")
	public ResponseEntity<Map<String, Object>> Splocation(@RequestParam String parameter,
			@RequestParam(required = false) String commitNumber) {
		 String hardSbiNo = inmateService.HardSbi(parameter);

		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		        parameter = hardSbiNo;
		    }
		String IdValidityResult = inmateService.validSbiNo(parameter);
		if ("Y".equals(IdValidityResult)) {
			commitNumber = inmateService.validCommitNo(parameter);
			List<CSMViewLocation> resultsetlocation = countdetailservice.callSpLocation(parameter);
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("locationoffenderhistory", resultsetlocation);
			return ResponseEntity.ok(responseMap);

		} else {
			throw new IllegalArgumentException("SBI Number is not valid");
		}
	}

	
	@GetMapping("/offenderinfohistory")
	public ResponseEntity<Map<String, Object>> offenderactivityhistory(
	        @RequestParam String parameter,
	        @RequestParam(required = true) String commitNumber,
	        @RequestParam String userId) {
		 String hardSbiNo = inmateService.HardSbi(parameter);

		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		        parameter = hardSbiNo;
		    }
	    Map<String, Object> result = countdetailservice.calloffenderactivitystats(commitNumber);
	    List<SecCdRec> securityCodes = countdetailservice.callSpSecCdQueryObj(commitNumber);
	    List<CSMViewLocation> resultsetlocation = countdetailservice.callSpLocation(parameter);
	
	    Map<String, Object> responseMap = new HashMap<>();
	    responseMap.put("locationDetails", result);
	    responseMap.put("securitycode", securityCodes);
	    responseMap.put("locationoffenderhistory", resultsetlocation);

	    return ResponseEntity.ok(responseMap);
	}

//-------------------------------------------------------------------------------------------------------------------------------------//	 



	@GetMapping(value = "/CheckboxInformation")
	public List<Map<String, Object>> GetCheckboxvalue(@RequestParam String parameter,
			@RequestParam(required = false) String v_commit_no) {
		String hardSbiNo = inmateService.HardSbi(parameter);
		String FindHardSBI = hardSbiNo;
		System.out.println("FindHardSBI="+FindHardSBI); 
		// If hard SBI number is available, override the parameter with it
		if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		parameter = hardSbiNo;
		}
		String VitalStatisticsResult = inmateService.validSbiNo(parameter);
	
		if ("Y".equals(VitalStatisticsResult)) {
			v_commit_no = inmateService.validCommitNo(parameter);
			List<Map<String, Object>> output = vitalservice.VitalCheckboxProcedure(v_commit_no);
			Map<String, Object> contact = vitalservice.nocontact(v_commit_no);
			Map<String, Object> MentalHealth = vitalservice.MentalHealth(v_commit_no);
			Map<String, Object> LegalIssue = vitalservice.LegalIssue(v_commit_no);
			Map<String, Object> SexOffenderList = vitalservice.VitalSexOffender(v_commit_no);
			Map<String, Object> State = vitalservice.State(v_commit_no);
			Map<String, Object> SexHistory = vitalservice.sexOffenseHistory(v_commit_no);
			List<Map<String, Object>> outputAddress = vitalservice.VitalInmateAddress(v_commit_no);
			output.add(contact);
			output.add(MentalHealth);
			output.add(LegalIssue);
			output.add(SexOffenderList);
			output.add(State);
			output.add(SexHistory);

			Map<String, Object> findHardSbiMap = new HashMap<>();
			findHardSbiMap.put("FindHardSBI", FindHardSBI);
			output.add(findHardSbiMap);
			
			output.addAll(outputAddress);

			return output;
		} else {
			throw new IllegalArgumentException("SBI Number is not valid");
		}
	}

	@GetMapping(value = "/CheckboxInformationHistory")
	public List<Map<String, Object>> GetCheckboxvalueHistory(String v_commit_no) {		
		List<Map<String, Object>> output = vitalservice.VitalCheckboxProcedure(v_commit_no);
		Map<String, Object> contact = vitalservice.nocontact(v_commit_no);
		Map<String, Object> MentalHealth = vitalservice.MentalHealth(v_commit_no);
		Map<String, Object> LegalIssue = vitalservice.LegalIssue(v_commit_no);
		Map<String, Object> SexOffenderList = vitalservice.VitalSexOffender(v_commit_no);
		Map<String, Object> State = vitalservice.State(v_commit_no);
		Map<String, Object> SexHistory = vitalservice.sexOffenseHistory(v_commit_no);
		List<Map<String, Object>> outputAddress = vitalservice.VitalInmateAddress(v_commit_no);
		output.add(contact);
		output.add(MentalHealth);
		output.add(LegalIssue);
		output.add(SexOffenderList);
		output.add(State);
		output.add(SexHistory);
		output.addAll(outputAddress);
		return output;
	}

	@PostMapping(value = "nobd_dtls")
	public List<Map<String, Object>> noBedDetails(@RequestParam String instNum) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			String sql = "{call SPKG_MOV_NOBD.pquery_nobd(?, ?)}";
			CallableStatement stmt = conn.prepareCall(sql);
			stmt.setString(1, instNum);
			stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
			stmt.execute();

			ResultSet rs = (ResultSet) stmt.getObject(2);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			int x = 0;
			while (rs.next()) {
				x++;								
				Map<String, Object> resultMap = new HashMap<>();
				// JSONObject obj = new JSONObject();
				for (int i = 2; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					Object columnValue = rs.getObject(i);
					resultMap.put(columnName, columnValue);
				}
				resultList.add(resultMap);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;

	}

	@PostMapping("/getInst")
	public List<Map<String, Object>> getInst(@RequestParam String userId) {
		String query = "Select  inst_name , i.inst_num from institution I, user_access_inst U where  i.inst_num = u.Inst_num  and  u.user_id = '"
				+ userId + "'  and status = 'A' order by inst_name";

		// String query = "SELECT INST_NUM, INST_NAME FROM INSTITUTION WHERE
		// INST_SECURITY_LEVEL IN ('4','5') ORDER BY INST_NAME";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getInstCount")
	public List<Map<String, Object>> getInstCount(@RequestParam String instNum) {

		String query = "SELECT SF_GET_INST_PHY_CNT('" + instNum + "', 'N', 'Y') AS physical_count FROM DUAL";
		// String query = "SELECT RUNNING_COUNT AS INST_COUNT FROM INSTITUTION WHERE
		// INST_NUM = '" + instNum + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@PostMapping("/getTestResult")
	public List<Map<String, Object>> getTestResult(@RequestParam String TestType) {
		String query = "SELECT FINDING_RESULT_DESC, FINDING_RESULT_CODE FROM FINDING_RESULT_RT WHERE DRUG_TST_TP_CD = '"
				+ TestType + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping("/getTestResult2")
	public List<Map<String, Object>> getTestResult2(@RequestParam String TestType) {
		String query = "SELECT FINDING_RESULT_DESC, FINDING_RESULT_CODE FROM FINDING_RESULT_RT WHERE DRUG_TST_TP_CD = '"
				+ TestType + "'";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		return result;
	}

	@GetMapping(value = "OffenderDetails")
	public List<Map<String, Object>> Offender(@RequestParam String parameter,
			@RequestParam(required = false) String v_commit_no) {
		String hardSbiNo = inmateService.HardSbi(parameter);

		// If hard SBI number is available, override the parameter with it
		if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		parameter = hardSbiNo;
		}
		String IdValidityResult = inmateService.validSbiNo(parameter);
		if ("Y".equals(IdValidityResult)) {

			if (v_commit_no == null) {
				v_commit_no = inmateService.validCommitNo(parameter);
			}
			List<Map<String, Object>> Details = vitalservice.OffenderDetails(v_commit_no);
			return Details;
		} else {
			throw new IllegalArgumentException("SBI Number is not valid");
		}

	}

	@GetMapping(value = "OffenderDetailsHistory")
	public List<Map<String, Object>> OffenderHistory(String v_commit_no) {
		List<Map<String, Object>> Details = vitalservice.OffenderDetails(v_commit_no);
		return Details;

	}

	@GetMapping(value = "WarrantExecuteQuery")
	public Map<String, Object> WarrantExecute_Query(@RequestParam(required = false) String ivc_pass,
			@RequestParam(required = false) String ivc_program_code, String ivc_sbi_no,
			@RequestParam(required = false) String ivc_param1, @RequestParam(required = false) String ivc_param2,
			@RequestParam(required = false) String ivc_param3, @RequestParam(required = false) String ivc_param4,
			@RequestParam(required = false) String ivc_param5) throws SQLException {
		ivc_program_code = "WARRSUM";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		ivc_pass = userService.getCurrentPasswordByUserId(userId);
		return WC.WarrantCheckExcuteQuery(ivc_pass, ivc_program_code, ivc_sbi_no, ivc_param1, ivc_param2, ivc_param3,
				ivc_param4, ivc_param5);

	}

	@GetMapping(value = "Warrantchecks")
	public List<warrantCheck> WarrantCheck(String ivc_sbi_no) {
		ivc_sbi_no = "00460722";
		List<warrantCheck> WarrantCheckScreen = WC.WarrantcheckM(ivc_sbi_no);
		return WarrantCheckScreen;
	}

	@GetMapping(value = "Facility")
	public List<HousingFacilityBedAssignments> Facility(String userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		userId = (String) detailsMap.get("userId");
		List<HousingFacilityBedAssignments> Facility = HousingFacility.Facility(userId);
		return Facility;
	}

	@GetMapping("/Building")
	public ResponseEntity<?> getActiveBuildingsForInstNum(	      
	        @RequestParam String userId,
	        @RequestParam String SbiNo) {
	    String userInstNum = inmateService.getUserInstNum(userId);
	    String commitNo = inmateService.validCommitNo(SbiNo);
	    String inmateInst = inmateService.getInmateInst(commitNo);
	    if (userInstNum.equals(inmateInst)) {	     
	        List<Object[]> buildings = bedService.getActiveBuildingsForInstNum(inmateInst);
	    	Map<String, Object> response = new HashMap<>();
	    	response.put("bedDetailsinmateInst", inmateInst);
			response.put("bedDetails", buildings);
	        return ResponseEntity.ok(response); 
	    } else {	      
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("message", "The Offender is not active in your facility");
	        return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorResponse); // Forbidden status
	    }
	}
	
	

	@GetMapping("/units")
	public ResponseEntity<List<Object[]>> getUnits(@RequestParam String instNum, @RequestParam String bldNum) {
		List<Object[]> units = bedService.getUnits(instNum, bldNum);
		return ResponseEntity.ok(units);
	}

	@GetMapping("/floors")
	public ResponseEntity<List<Object[]>> getFloors(@RequestParam String instNum, @RequestParam String bldNum,
			@RequestParam String unitId) {
		List<Object[]> floors = bedService.getFloors(instNum, bldNum, unitId);
		return ResponseEntity.ok(floors);
	}

	@GetMapping("/cells")
	public ResponseEntity<List<Object[]>> getCells(@RequestParam String instNum, @RequestParam String bldNum,
			@RequestParam String unitId, @RequestParam String floorNum, @RequestParam String tierNum) {
		List<Object[]> cells = bedService.getCells(instNum, bldNum, unitId, floorNum, tierNum);
		return ResponseEntity.ok(cells);
	}

	@GetMapping("/tiers")
	public ResponseEntity<List<Object[]>> getTiers(@RequestParam String instNum, @RequestParam String bldNum,
			@RequestParam String unitId, @RequestParam String floorNum) {
		List<Object[]> tiers = bedService.getTiers(instNum, bldNum, unitId, floorNum);
		return ResponseEntity.ok(tiers);
	}

	@GetMapping("/bedCell")
	public ResponseEntity<List<BedCell>> getBedCells(@RequestParam String Sbino, @RequestParam String instNum,
			@RequestParam String bldNum, @RequestParam String unitId, @RequestParam String floorNum,
			@RequestParam String tierNum, @RequestParam String cellNum, @RequestParam String occupiedFlag) {

		String commitNo = inmateService.validCommitNo(Sbino);
		List<BedCell> bedCells = bedService.callSpSelectBedCell(commitNo, instNum, bldNum, unitId, floorNum, tierNum,
				cellNum, occupiedFlag);
		return ResponseEntity.ok(bedCells);
	}

	@GetMapping(value = "Bed")
	public List<Bed> BedValue(String instNum, String BLDNO, String UNIT_ID, String FloorNo, String TierNo,
			String CellNo) {
		List<Bed> BedValue = HousingFacility.BedValue(instNum, BLDNO, UNIT_ID, FloorNo, TierNo, CellNo);
		return BedValue;
	}
	

    @GetMapping(value="BedListvalues")
	public Map<String, Object> valueOfBedlist(String i_bed_type, String i_inst_num, String i_bld_num, String i_unit_id,
String i_floor_num, String i_tier_num, String i_cell_no, String i_bed_no, String i_bed_status){
	    Map<String, Object> bedList = new HashMap<>();
        List<BedList> BedListValue = HousingFacility.BedListValues(i_bed_type, i_inst_num, i_bld_num, i_unit_id, i_floor_num,
	i_tier_num, i_cell_no, i_bed_no, i_bed_status);
        String receivingRoomCount = HousingFacility.ReceivingRoomCount(i_inst_num);
			bedList.put("BedListValue", BedListValue);
	        bedList.put("receivingRoomCount", receivingRoomCount);
	    return bedList;
	}

	@GetMapping("/reason")
	public ResponseEntity<Map<String, String>> getReasonDropDownValues() {
		Map<String, String> dropdownValues = bedService.ReasonDropDownValues();
		return new ResponseEntity<>(dropdownValues, HttpStatus.OK);
	}

	@GetMapping("/cellinmateDetails")
	public List<Map<String, Object>> getInmateDetails(String commitNo, String instNum) {
		return bedService.queryInmateDetails(commitNo, instNum);
	}

	@GetMapping("/buildingsdetails")
	public List<Object[]> getBuildingDetail(@RequestParam String instNum, @RequestParam String bedNo) {
		return bedService.getBuildingDetail(instNum, bedNo);
	}

	@GetMapping("/selectCurrInmate")
	public List<BedCell> selectCurrInmate(@RequestParam("Sbino") String Sbino,
			@RequestParam("instNum") String instNum) {
		String commitNo = inmateService.validCommitNo(Sbino);
		return bedService.selectCurrInmate(commitNo, instNum);
	}

	@GetMapping("/unreserve")
	public String unreserveBed(@RequestParam String bedNo) {
		bedService.unreserve(bedNo);
		return "Bed unreserved successfully";
	}
	

    @GetMapping("/rhreasonstatusdescription")
    public ResponseEntity<Map<String, String>> getReasonDescription(@RequestParam("SbiNo") String SbiNo) {
    	String commitNo = inmateService.validCommitNo(SbiNo);
        String description = bedService.getRefDesc(commitNo);
        System.out.println("rhreasonstatusdescription"+description);
        Map<String, String> response = new HashMap<>();
        response.put("commitNo", commitNo);
        response.put("reasonDesc", description != null ? description : " ");
        return ResponseEntity.ok(response);
    }

	@GetMapping(value = "vitalcommitNo")
	public String GetcommitNo(String parameter) {
		return inmateService.validCommitNo(parameter);
	}

	@PostMapping("/checkrestricted")
	public ResponseEntity<Map<String, Object>> checkrestricted(@RequestParam String SbiNo, @RequestParam String instNum,
			@RequestParam String bldNum, @RequestParam String unitId, @RequestParam String floorNum,
			@RequestParam String tierNum) {
		String commitNo = inmateService.validCommitNo(SbiNo);
		Map<String, String> result = bedService.callSpMovementOfRhLoc(bldNum, unitId, floorNum, tierNum, commitNo,
				instNum);		
		Map<String, Object> response = new HashMap<>();
		response.put("waitlistResult", result);
		return ResponseEntity.ok(response);
	}
	
	 @GetMapping("/getbeddetails")
	    public ResponseEntity<Map<String, String>> getBedDetails(
	            @RequestParam("bedNo") String bedNo) {
	        Map<String, String> result = bedService.callPselbedDetails(
	                bedNo, null, null, null, null
	        );
	        return ResponseEntity.ok(result);
	    }

	@GetMapping("/addtowaitlist")
	public ResponseEntity<Map<String, Object>> getBedDetails(@RequestParam String bedNo, @RequestParam String sbiNo, 
																														
																														
			@RequestParam String instNum, @RequestParam String cellNo, @RequestParam(required = false) String bldNum,
			@RequestParam(required = false) String unitId, @RequestParam(required = false) String floorNum,
			@RequestParam(required = false) String tierNum

	) {
		String commitNo = inmateService.validCommitNo(sbiNo);
		Map<String, String> bedDetails = bedService.callPselbedDetails(bedNo, bldNum, unitId, floorNum, tierNum);
		String extractedBldNum = bedDetails.getOrDefault("bldNum", bldNum);
		String extractedUnitId = bedDetails.getOrDefault("unitId", unitId);
		String extractedFloorNum = bedDetails.getOrDefault("floorNum", floorNum);
		String extractedTierNum = bedDetails.getOrDefault("tierNum", tierNum);
		Map<String, Object> addToWaitlistResult = bedService.callSelectUnitWaitlist(commitNo, instNum, extractedBldNum,
				extractedUnitId, extractedFloorNum, extractedTierNum, cellNo, bedNo);

		Map<String, Object> response = new HashMap<>();

		response.put("bedDetails", bedDetails);
		response.put("waitlistResult", addToWaitlistResult);
		response.put("extractedBldNum", extractedBldNum);
		response.put("extractedUnitId", extractedUnitId);
		response.put("extractedFloorNum", extractedFloorNum);
		response.put("extractedTierNum", extractedTierNum);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/insert")
	public String insertUnitWaitlist(@RequestParam String sbiNo, @RequestParam("instNum") String instNum,
			@RequestParam("bldNum") String bldNum, @RequestParam("unitId") String unitId,
			@RequestParam("floorNum") String floorNum, @RequestParam("tierNum") String tierNum,
			@RequestParam("cellNo") String cellNo, @RequestParam("bedNo") String bedNo,
			@RequestParam("stDateTime") String stDateTime, @RequestParam("toDate") String toDate,
			@RequestParam("reason") String reason) {

		try {
			String commitNo = inmateService.validCommitNo(sbiNo);
			bedService.insertUnitWaitlist(commitNo, instNum, bldNum, unitId, floorNum, tierNum, cellNo, bedNo,
					stDateTime, toDate, reason);
			return "Unit waitlist inserted successfully.";
		} catch (Exception e) {
			return "Error inserting unit waitlist: " + e.getMessage();
		}

	  

	}
	 @GetMapping("hierarchy")
	    public List<Object[]> getBuildingHierarchy(@RequestParam Integer instNum) {
	        List<Object[]> hierarchyList = bedService.getBuildingHierarchy(instNum);
	    
	        return hierarchyList;
	    }
	@GetMapping("/inmatelocationvalidate")
	public ResponseEntity<String> validateInmate(@RequestParam String userId, @RequestParam String SbiNo) {
		String userInstNum = inmateService.getUserInstNum(userId);

		String commitNo = inmateService.validCommitNo(SbiNo);
		String inmateInst = inmateService.getInmateInst(commitNo);

		if (!userInstNum.equals(inmateInst)) {
			return ResponseEntity.ok("The Offender is not active in your facility");
		} else {
			return ResponseEntity.ok("Validation successful.");
		}
	}

	@GetMapping("/reasonQuery")
	public ResponseEntity<Map<String, Object>> getReasonQuery(
	        @RequestParam("reasonCode") String reasonCode,
	        @RequestParam("commitNo") String commitNo) {
	    try {
	        // Call procedure to get reasons
	        List<Map<String, Object>> resultList = bedService.callSpRhReasonQuery(reasonCode);

	        // Call repository to get RH reasons by commitNo
	        List<Object> rhReasonsRaw = bedService.getRhReasons(commitNo);

	      

	        // Prepare combined response
	        Map<String, Object> response = new HashMap<>();
	        response.put("resonrhrow", resultList);
	        response.put("rhreasonselected", rhReasonsRaw);

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(null);
	    }
	}



	@PostMapping("/updateRhReason")
	public ResponseEntity<String> updateRhReason(@RequestBody Map<String, Object> payload) {
	    String commitNo = (String) payload.get("commitNo");
	    List<String> reasonCodes = (List<String>) payload.get("reasonCodes");
	    String userId = (String) payload.get("userId");

	    // Call your service ONCE with all reason codes
	    bedService.callSpRhReasonUpdate(commitNo, reasonCodes, userId);

	    return ResponseEntity.ok("Success");
	}


	@GetMapping("/checkVictimAggressor")
	public Map<String, Object> checkVictimAggressor(@RequestParam String Sbino, @RequestParam String instNum,
			@RequestParam String cellNo, @RequestParam String tierNum, @RequestParam String floorNum,
			@RequestParam String unitId, @RequestParam String bldNum, @RequestParam String bedNo,
			@RequestParam int num) {

		String commitNo = inmateService.validCommitNo(Sbino);
		Map<String, Object> result = bedService.callSpVictimAggressorExist(commitNo, instNum, cellNo, tierNum, floorNum,
				unitId, bldNum, bedNo, num);
		return result;
	}

	@PostMapping("/assignbed")
	public ResponseEntity<String> assignBed(@RequestParam("sbino") String sbino,
			@RequestParam("instNum") String instNum, @RequestParam("buildNum") String buildNum,
			@RequestParam("unitId") String unitId, @RequestParam("floorNum") String floorNum,
			@RequestParam("tierNum") String tierNum, @RequestParam("cellNo") String cellNo,
			@RequestParam("bedNo") String bedNo, @RequestParam("action") String action,
			@RequestParam("stDateTime") String stDateTimeParam, @RequestParam("resnCode") String resnCode,
			@RequestParam("toDate") String toDateParam, @RequestParam("nonRhRsnFlag") String nonRhRsnFlag) {

		try {

			Timestamp stDateTime = null;
			Timestamp toDate = null;

			if (stDateTimeParam != null && !stDateTimeParam.isEmpty()) {
				stDateTime = convertStringToTimestamp(stDateTimeParam, "MM/dd/yyyy");
			}

			if (toDateParam != null && !toDateParam.isEmpty()) {
				toDate = convertStringToTimestamp(toDateParam, "MM/dd/yyyy");
			}

			String commitNo = inmateService.validCommitNo(sbino);
			bedService.assignbed(commitNo, instNum, buildNum, unitId, floorNum, tierNum, cellNo, bedNo, action,
					stDateTime, resnCode, toDate, nonRhRsnFlag);
			return ResponseEntity.ok("Procedure executed successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error executing procedure: " + e.getMessage());
		}
	}

	private Timestamp convertStringToTimestamp(String dateString, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date parsedDate = dateFormat.parse(dateString);
		return new Timestamp(parsedDate.getTime());
	}



	@GetMapping("/isProtected")
	public ResponseEntity<Boolean> isOffenderProtected(@RequestParam String sbiNo) {
		boolean isProtected = inmateService.isOffenderProtected(sbiNo);
		return new ResponseEntity<>(isProtected, HttpStatus.OK);
	}

	
	@GetMapping("/getEnemies") public ResponseEntity<Map<String, Object>>
	   getEnemies(@RequestParam String sbiNo) 
	   { 
	    try 
	    { 
	     String commitNo = inmateService.validCommitNo(sbiNo);
	     List<Enemy> enemies = bedService.callSpEnemyQuery(sbiNo); 
	     List<Map<String, Object>> otherenemies = bedService.getOtherNoContacts(sbiNo);
	     //Map<String, Object> enemiesLocation = bedService.noContactLocation(commitNo);
	     System.out.println(otherenemies);
	     Map<String, Object> response = new HashMap<>();
	       response.put("otherContact", enemies);
	    response.put("claimedOtherContact", otherenemies);
	    //response.put("enemiesLocation", enemiesLocation);
	          return ResponseEntity.ok(response); 
	     }
	   catch (Exception e) 
	    { 
	    System.err.println("Error fetching enemies: " +
	   e.getMessage()); e.printStackTrace(); return
	   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); } }

	@GetMapping("/referenceValue")
	public List<ReferenceValue> getMovementReasons() {
		return bedService.getMovementReasons();
	}

	@GetMapping("/disapproveValue")
	public List<ReferenceValue> findDisapprovalReasons() {
		return bedService.getMovementReasons();
	}

	@GetMapping("/forwardto")
	public List<Object[]> getUsersFullName(@RequestParam String userId) {
		List<Object[]> results = bedService.getUsersWithFullName(userId);		
		return results;
	}

	
	@PostMapping("/protectedbedstage")
	public ResponseEntity<String> insertProtectedOffenderBedStage(
	    @RequestParam String sbiNo,	  
	    @RequestParam String bldNum,
	    @RequestParam String unitId,
	    @RequestParam String floorNum,
	    @RequestParam String tierNum,
	    @RequestParam String cellNo,
	    @RequestParam String bedNo,
	    @RequestParam String reasonCode,
	    @RequestParam Date bedStayStartDate,  // String input
	    @RequestParam String bedStayStartTime,
	    @RequestParam Date bedStayEndDate,    // String input
	    @RequestParam String bedStayEndTime) {

	    try {
			
	        String commitNo = inmateService.validCommitNo(sbiNo);
	        // Insert into the database
	        bedService.insertProtectedOffenderBedStage(
	            sbiNo, commitNo, bldNum, unitId, floorNum, tierNum, cellNo, bedNo, reasonCode, 
	            bedStayStartDate, bedStayStartTime, bedStayEndDate, bedStayEndTime
	        );

	        return ResponseEntity.status(HttpStatus.CREATED).body("Insert successful.");
	    } catch (Exception e) {
	        System.err.println("Error: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Failed to insert into protected_ofndr_bed_stage.");
	    }
	}


	/**
	 * Converts a time string (e.g., "12:33") to java.sql.Time.
	 */
	private Time convertStringToSqlTime(String timeStr) throws ParseException {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    return new Time(sdf.parse(timeStr).getTime());
	}



	@PostMapping("/sendnotification")
	public ResponseEntity<String> sendNotification(@RequestParam String notificationType, @RequestParam String sbiNo,
			@RequestParam String userId, @RequestParam String instNum, @RequestParam String bldNum,
			@RequestParam String unitNum, @RequestParam String floorNum, @RequestParam String tierNum,
			@RequestParam String cellNo, @RequestParam String bedNo, @RequestParam String pobsSeqNum,
			@RequestParam String promsSeqNum) {
		try {
			String commitNo = inmateService.validCommitNo(sbiNo);
			bedService.callSpPrtctdOfndrNotification(notificationType, sbiNo, commitNo, userId, instNum, bldNum,
					unitNum, floorNum, tierNum, cellNo, bedNo, pobsSeqNum, promsSeqNum);
			return ResponseEntity.ok("Notification sent successfully.");

		} catch (Exception e) {
			System.err.println("Error sending notification: " + e.getMessage());
			return ResponseEntity.status(500).body("Failed to send notification: " + e.getMessage());
		}
	}

	@GetMapping("/checkSecurity")
	public ResponseEntity<Integer> checkSecurity(@RequestParam("sbiNo") String sbiNo,
	                                             @RequestParam("bedNo") String bedNo) {
	    String commitNo = inmateService.validCommitNo(sbiNo);
	    int result = bedService.checkBedSecurity(commitNo, bedNo);

	    return ResponseEntity.ok(result);
	}

	@GetMapping("/checkhandicap")
	public ResponseEntity<Map<String, Object>> checkHandicap(@RequestParam String sbiNo, @RequestParam String instNum,
			@RequestParam String bldNum, @RequestParam String unitId, @RequestParam String floorNum,
			@RequestParam String tierNum, @RequestParam String cellNo, @RequestParam String bedNo) {
		String commitNo = inmateService.validCommitNo(sbiNo);
		Map<String, Object> result = bedService.checkForHandicap(commitNo, instNum, bldNum, unitId, floorNum, tierNum,
				cellNo, bedNo);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/checkLocation")
	public ResponseEntity<Map<String, Object>> checkLocation(@RequestParam("sbiNo") String sbiNo,
			@RequestParam("user") String user) {
		String commitNo = inmateService.validCommitNo(sbiNo);
		Map<String, Object> result = bedService.checkLocationAndRaiseAlert(commitNo, user);
		return ResponseEntity.ok(result);
	}

	  @GetMapping("/spupdatefornotselected")
	    public String updateLocation(@RequestParam("commitNo") String commitNo,@RequestParam("userId") String userId ) {
	        try {
	        	bedService.callUpdateLocation(commitNo,userId);
	            return "Procedure executed successfully for commitNo: " + commitNo;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error while executing procedure: " + e.getMessage();
	        }
	    }

	@GetMapping(value = "BedDetails")
	public Map<String, Object> BED_DETAILS(String ivc_bed_no) throws SQLException {
		Map<String, Object> BEDDETAILS = HousingFacility.BedDetails(ivc_bed_no);	
		return BEDDETAILS;
	}

	@GetMapping("/bookingservice")
	public List<BookingDetail> getBookingDetails(@RequestParam("Sbino") String Sbino,
			@RequestParam("userId") String userId) {
		String commitNo = inmateService.validCommitNo(Sbino);
		String sntcLvlCd = bookingrepo.getSntcLvlCd(commitNo, userId);
		return bookingservice.callBookingQueryProcedure(sntcLvlCd, commitNo);
	}

	@GetMapping("/bookingservicehistory")
	public List<BookingDetail> getBookingDetailshistory(@RequestParam("CommitNo") String commitNo,
			@RequestParam("userId") String userId) {
		String sntcLvlCd = bookingrepo.getSntcLvlCd(commitNo, userId);
		return bookingservice.callBookingQueryProcedure(sntcLvlCd, commitNo);
	}

	@PostMapping("/updateScreen")
	public ResponseEntity<String> updateScreen(@RequestParam String Sbino, @RequestParam String screenCodes,
			@RequestParam String updatedFlag) {

		try {
			String commitNo = inmateService.validCommitNo(Sbino);
			System.out.println("Sbino: " + Sbino);
			System.out.println("Screen Codes: " + screenCodes);
			bookingservice.updateScreenDetails(commitNo, screenCodes, updatedFlag);
			return ResponseEntity.ok("Screen details updated successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error updating screen details: " + e.getMessage());
		}
	}

	@GetMapping("/getUrlForScreenCode")
	public ResponseEntity<Map<String, String>> getUrlForScreenCode(@RequestParam String screenCode) {
		try {
			String screenUrl = bookingservice.getUrlForScreenCode(screenCode);
			System.out.println("screenUrl" + screenUrl);
			if (screenUrl != null) {
				Map<String, String> response = new HashMap<>();
				response.put("url", screenUrl);
				System.out.println("response" + response);
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	//-----------------------------------STG starT------------------------------------------------------------------------------//
	@GetMapping("/gangDetails")
	public List<Object[]> fetchGangDetails() {
		return stgservice.getGangDetails();
	}

	@GetMapping("/callSpStgQuery")
	public ResponseEntity<Map<String, Object>> callSpStgQuery(@RequestParam("sbiNo") String sbiNo) throws SQLException {
		 String hardSbiNo = inmateService.HardSbi(sbiNo);

		    // If hard SBI number is available, override the parameter with it
		    if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
		    	sbiNo = hardSbiNo;
		    }
		String commitNo = inmateService.validCommitNo(sbiNo);
		List<Map<String, Object>> result = stgservice.callSpStgQuery(commitNo);
		List<InmateData> resultset = stgservice.getInmatequery(commitNo);
		Map<String, Object> response = new HashMap<>();
		response.put("mapResult", result);
		response.put("inmateData", resultset);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/callSpStgQuerHistory")
	public ResponseEntity<Map<String, Object>> callSpStgQuerHistory(@RequestParam("commitNo") String commitNo) throws SQLException {
		
		List<Map<String, Object>> result = stgservice.callSpStgQuery(commitNo);
		List<InmateData> resultset = stgservice.getInmatequery(commitNo);
		Map<String, Object> response = new HashMap<>();
		response.put("mapResult", result);
		response.put("inmateData", resultset);
		return ResponseEntity.ok(response);
	}

	/*
	 * @PostMapping("/stgvaluesave") public ResponseEntity<?>
	 * saveStgData(@RequestBody Map<String, Object> requestData) { try { String
	 * sbiNo = (String) requestData.get("sbiNo"); List<Map<String, Object>> dataList
	 * = (List<Map<String, Object>>) requestData.get("data"); if (sbiNo == null ||
	 * dataList == null || dataList.isEmpty()) { return
	 * ResponseEntity.badRequest().body("Invalid input data"); }
	 * System.out.println("Received sbiNo: " + sbiNo);
	 * System.out.println("Received data: " + dataList); List<Rowdata> rowDataList =
	 * new ArrayList<>(); for (Map<String, Object> data : dataList) { String
	 * commitNo = (String) data.get("commitNo"); Rowdata rowData = new Rowdata();
	 * rowData.setCommitNo(commitNo); rowData.setGangName((String)
	 * data.get("gangName")); rowData.setGangPosition((String)
	 * data.get("gangPosition")); rowData.setGangLeader((String)
	 * data.get("gangLeader")); rowData.setGangAiInd((String)
	 * data.get("gangAiInd")); rowData.setStgSefRepdStatus((String)
	 * data.get("stgSefRepdStatus")); rowData.setGangComments((String)
	 * data.get("gangComments")); rowData.setGangCode((String)
	 * data.get("gangCode")); rowData.setGangDelFlg((String)
	 * data.get("gangDelFlg")); rowDataList.add(rowData);
	 * System.out.println("Processed Rowdata: " + rowData); }
	 * System.out.println("Final Rowdata List: " + rowDataList);
	 * stgservice.processData(dataSource, sbiNo, rowDataList); return
	 * ResponseEntity.ok("Data saved successfully!"); } catch (Exception e) { return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	 * body("Error saving data: " + e.getMessage()); } }
	 */
	
	@PostMapping("/stgvaluesave")
	public ResponseEntity<?> saveStgData(@RequestBody Map<String, Object> requestData) {
	    try {
	        String sbiNo = (String) requestData.get("sbiNo");
	        String gangSelectDateTimeStr = (String) requestData.get("gangSelectDateTime"); // <<-- fetch separately
	        List<Map<String, Object>> dataList = (List<Map<String, Object>>) requestData.get("data");

	        if (sbiNo == null || dataList == null || dataList.isEmpty()) {
	            return ResponseEntity.badRequest().body("Invalid input data");
	        }

	        // Parse the datetime string to Timestamp
	        Timestamp gangSelectDateTime = null;
	        if (gangSelectDateTimeStr != null) {
	            gangSelectDateTime = Timestamp.valueOf(gangSelectDateTimeStr); 
	            // Requires "yyyy-MM-dd HH:mm:ss" format
	        }

	        List<Rowdata> rowDataList = new ArrayList<>();
	        for (Map<String, Object> data : dataList) {
	            String commitNo = (String) data.get("commitNo");
	            Rowdata rowData = new Rowdata();
	            rowData.setCommitNo(commitNo);
	            rowData.setGangName((String) data.get("gangName"));
	            rowData.setGangPosition((String) data.get("gangPosition"));
	            rowData.setGangLeader((String) data.get("gangLeader"));
	            rowData.setGangAiInd((String) data.get("gangAiInd"));
	            rowData.setStgSefRepdStatus((String) data.get("stgSefRepdStatus"));
	            rowData.setGangComments((String) data.get("gangComments"));
	            rowData.setGangCode((String) data.get("gangCode"));
	            rowData.setGangDelFlg((String) data.get("gangDelFlg"));
	            rowDataList.add(rowData);
	        }

	        stgservice.processData(dataSource, sbiNo, rowDataList, gangSelectDateTime); // <<-- pass separately
	        return ResponseEntity.ok("Data saved successfully!");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving data: " + e.getMessage());
	    }
	}



	  @PostMapping("/updatestg")
	  public ResponseEntity<?> updateSTG(@RequestBody Map<String, Object> requestData) { 
	      String sbiNo = (String) requestData.get("sbiNo");
	      List<Map<String, Object>> dataList = (List<Map<String, Object>>) requestData.get("data");
	      if (dataList == null || dataList.isEmpty()) {
	          return ResponseEntity.badRequest().body("Data list is empty or null");
	      }
	     
	      List<Rowdata> updateList = new ArrayList<>();
	      for (Map<String, Object> data : dataList) {
	          Rowdata rowData = new Rowdata();
	          System.out.println("setIgaRowid"+data.get("igaRowid"));
	          System.out.println("setGangName"+data.get("gangName"));
	          rowData.setIgaRowid((String) data.get("igaRowid"));
	          rowData.setGangName((String) data.get("gangName"));
	          rowData.setGangPosition((String) data.get("gangPosition"));
	          rowData.setGangLeader((String) data.get("gangLeader"));
	          rowData.setGangAiInd((String) data.get("gangAiInd"));
	          rowData.setStgSefRepdStatus((String) data.get("stgSefRepdStatus"));
	          rowData.setGangComments((String) data.get("gangComments"));
	          rowData.setGangCode((String) data.get("gangCode"));
	          rowData.setCommitNo((String) data.get("commitNo"));
	          rowData.setGangDelFlg((String) data.get("gangDelFlg"));
	          updateList.add(rowData);
	      }
	      stgservice.updatedata(dataSource, updateList);
	      return ResponseEntity.ok("Update successful");
	  }



		/*
		 * @PostMapping("/assaultscheckbox") public ResponseEntity<String>
		 * updateInmates(@RequestBody List<InmateData> inmateDataList) { try {
		 * stgservice.callSpCbiStgInmateUpdate(inmateDataList); return
		 * ResponseEntity.ok("Procedure executed successfully"); } catch (SQLException
		 * e) { // You might want to log the exception as well. return
		 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		 * .body("Error executing update: " + e.getMessage()); } }
		 */
	  
	  @PostMapping("/updateassaultscheckbox")
	    public ResponseEntity<String> updateInmate(@RequestBody List<InmateData> inmateDataList) {
	        try {
	        	stgservice.callSpCbiStgInmateUpdate(inmateDataList);
	            return ResponseEntity.ok("Inmate update successful");
	        } catch (SQLException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Error updating inmate: " + e.getMessage());
	        }
	    }
	
	  
	//-----------------------------------STG END------------------------------------------------------------------------------//
	  /*InamateChargesDocukets screen related query started  maheswari*/ 

	  @PostMapping("/getJudge")

		public List<Map<String, Object>> getJudge() {

			String query = "Select * from judge_mst";

 

			List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

			return result;

		}
	  
	  @GetMapping("/getJudges/{judgeCode}")
		public ResponseEntity<Judges> getById(@PathVariable String judgeCode) {
		    Judges result = judgeService.getNameById(judgeCode);
		    return ResponseEntity.ok(result);
		}
	  
	  @GetMapping("/getJudges")
		public ResponseEntity<Page<Judges>> getJudges(
				@RequestParam(defaultValue = "") String search,
		        @RequestParam(defaultValue = "0") int page,
		        @RequestParam(defaultValue = "500") int size,
		        @RequestParam(defaultValue = "judgeLName") String sortBy,
		        @RequestParam(defaultValue = "asc") String sortDir
		) {
		  //System.out.println("judges req reached");
			Sort sort = sortDir.equalsIgnoreCase("desc") ?
		    Sort.by(sortBy).descending() :
		    Sort.by(sortBy).ascending();
			
			Pageable pageable = PageRequest.of(page, size, sort);
	        Page<Judges> result = judgeService.getFilteredData(search, pageable);
	        return ResponseEntity.ok(result);
		}
	  
	  @GetMapping("/searchUsers")
	  public ResponseEntity<Page<Users>> getUsers(
			  @RequestParam(defaultValue="") String search,
			  @RequestParam(defaultValue="0") int page,
			  @RequestParam(defaultValue="500") int size,
			  @RequestParam(defaultValue="userLastName") String sortBy,
			  @RequestParam(defaultValue="asc") String sortDir
		) {
		  
		  Sort sort = sortDir.equalsIgnoreCase("desc") ?
			Sort.by(sortBy).descending() :
			Sort.by(sortBy).ascending();
		  
		  Pageable pageable = PageRequest.of(page, size, sort);
		  Page<Users> result = userRepo.searchAllFields(search, pageable);
		  return ResponseEntity.ok(result);
	  }

 

		@PostMapping("/getCourtName")

		public List<Map<String, Object>> getCourtName() {

			String query = "select  court_name,court_code from Courts_Rt";

 

			List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

			return result;

		}
		
		@GetMapping("/getCourts")
		public ResponseEntity<Page<Court>> getCourts(
		    @RequestParam(defaultValue = "") String courtName,
		    @RequestParam(defaultValue = "0") int page,
		    @RequestParam(defaultValue = "500") int size
		) {
			System.out.println("courts req reached. pageNO: "+page);
			Sort sort = Sort.by("courtName").ascending();
			
		    Pageable pageable = PageRequest.of(page, size);
		    Page<Court> result = courtService.getCourtByCourtName(courtName, pageable);
		    return ResponseEntity.ok(result);
		}
		
		@GetMapping("/getCourts/{courtCode}")
		public ResponseEntity<Court> getCourtById(@PathVariable String courtCode) {
		    Court result = courtService.getCourtById(courtCode);
		    return ResponseEntity.ok(result);
		}

		@GetMapping("getStatutes")
	    public ResponseEntity<Page<Statute>> getDocuments(
	        /*@RequestParam(required = false) String juris,*/
	        @RequestParam(required = false) String title,
	        @RequestParam(required = false) String sec,
	        @RequestParam(required = false) String subSec,
	        @RequestParam(required = false) String type,
	        @RequestParam(required = false) String statuteClass,
	        /*@RequestParam(required = false) String statuteDesc,*/
	        @RequestParam(required = false) String keyword,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "500") int size,
	        @RequestParam(value = "sort", required = false) List<String> sortParams
	    ){
			Pageable pageable = buildPageable(page, size, sortParams);
			Page<Statute> result = statuteService.searchStatute(title, sec, subSec, type, statuteClass, keyword, pageable);
	        return ResponseEntity.ok(result);
		}
		
		private Pageable buildPageable(int page, int size, List<String> sortParams) {
		    if (sortParams != null && !sortParams.isEmpty()) {
		        List<Sort.Order> orders = sortParams.stream()
		            .map(param -> {
		                String[] parts = param.split(",");
		                return new Sort.Order(
		                    parts.length > 1 && parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
		                    parts[0]
		                );
		            })
		            .toList();
		        return PageRequest.of(page, size, Sort.by(orders));
		    } else {
		        return PageRequest.of(page, size);
		    }
		}

		@PostMapping("/getinmateChargesdockets")

		public List<Map<String, Object>> getinmateChargesdockets(@RequestParam String sbiNumber) {

			String commitNo = inmateService.validCommitNo(sbiNumber);

			System.out.println("commitNoinmatedoc" + commitNo);

			String query = "";

			if (commitNo != null) {

				query = "SELECT DISTINCT start_date, NVL(i.court_code, '') AS court_code, NVL(c.court_name, '') AS court_name, i.judge_code, case_num, "

						+ "j.JUDGE_LNAME || ' ' || j.judge_fname || ' ' || j.judge_mname || ' ' || j.judge_sname AS judge_name, "

						+ "'Y' AS record_type " + "FROM INMATE_COMMITTED_CHARGES i, judge_mst j, Courts_Rt c " + 

						"WHERE j.judge_code(+) = i.JUDGE_CODE " + // Outer join with judge_mst

						"AND c.court_code(+) = i.court_code " + // Outer join with Courts_Rt

						"AND commit_no = '" + commitNo + "' ";

 

			}

			List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

			return result;

 

		}

		@PostMapping("/getinmateChargesdetails")

		public List<Map<String, Object>> getinmateChargesdetails(@RequestParam String caseNumber,@RequestParam String date,@RequestParam String sbiNumber) {

			String commitNo = inmateService.validCommitNo(sbiNumber);

			System.out.println("commitNoinmatedoc" + commitNo);

			String query = "";

			if (commitNo != null) {

				query = "SELECT ROWIDTOCHAR(icc.ROWID) AS coch_dtl_rowid, " +

		                   "icc.commit_no, icc.start_date, icc.end_date, icc.case_num, " +

		                   "icc.charge_seq_num, icc.CONSOLIDATED_CRA_NUM,icc.DISPOSITION_CODE, icc.charge_description, icc.active_flag, " +

		                   "icc.bail_amount, icc.charge_num, icc.judge_code, icc.court_code, " +

		                   "icc.charge_jurisdiction, icc.statue_title, icc.statue_section, " +

		                   "icc.statue_subsection, icc.statue_type, icc.statue_class, " +

		                   "icc.comments, icc.sentac_category, icc.ncic_code, icc.DISPOSITION_CODE, " +

		                   "icc.inserted_userid, icc.inserted_date_time, icc.updated_userid, " +

		                   "icc.updated_date_time, icc.bail_type " +

		                   "FROM inmate_committed_charges icc " +

		                   "WHERE icc.commit_no ='"+commitNo+"' " +

		                   "AND icc.case_num = '"+caseNumber+"' " +

		                   "AND ((icc.bucket_num = icc.bucket_num AND 'Y' = 'Y') OR (icc.bucket_num IS NULL)) " +

		                   "AND icc.start_date = NVL('"+date+"', icc.start_date) " ;

 

			}

			List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

			return result;

 

		}

		

 

		 /*InamateChargesDocukets screen related query started  end */
		
		/** Property Tracking Start */
		@GetMapping("/propertyTrackingHdr")
		public ResponseEntity<Map<String, Object>> getPropertyTrackingHdr(@RequestParam String commitNo, @RequestParam String propType){
			try {
				Map<String, Object> results = propTracking.getPropertyTrackingHdr(commitNo, propType);
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
		
		@GetMapping("/propertyTrackingItmsColt")
		public ResponseEntity<Map<String, Object>> getPropertyTrackingItemsColt(@RequestParam String propTrkSeq){
			try {
				Map<String, Object> results = propTracking.getPropertyItemsCollected(propTrkSeq);
				return ResponseEntity.ok(results);
			} catch (Exception ex) {
		        // Log the error
		        System.err.println("Error fetching property tracking header: " + ex.getMessage());
		        ex.printStackTrace();
		        // Return error response
		        Map<String, Object> errorResponse = new HashMap<>();
		        errorResponse.put("error", "Failed to retrieve property tracking collected items data.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		    }
		}
		
		@GetMapping("/propertyTrackingItmsIssd")
		public ResponseEntity<Map<String, Object>> getPropertyTrackingItemsIssd(@RequestParam String propTrkSeq){
			try {
				Map<String, Object> results = propTracking.getPropertyItemsIssued(propTrkSeq);
				return ResponseEntity.ok(results);
			} catch (Exception ex) {
		        // Log the error
		        System.err.println("Error fetching property tracking header: " + ex.getMessage());
		        ex.printStackTrace();
		        // Return error response
		        Map<String, Object> errorResponse = new HashMap<>();
		        errorResponse.put("error", "Failed to retrieve property tracking issued items data.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		    }
		}
		
		@GetMapping("/getRelationshipTable")
		public List<Map<String, Object>> getRelationshipTable(){
			List<Map<String, Object>> results = propTracking.getRelationshipTable();
			return results;
		}
		
		@GetMapping("/getPropertyUser")
		public List<Map<String, Object>> getPropertyUser(@RequestParam(value = "srchkey") String srchkey) {
			String query = "SELECT USER_ID, USER_FIRST_NAME,USER_LAST_NAME,USER_MID_NAME,USER_SUFFIX_NAME FROM OMNET_USERS ";
			query += "WHERE LOWER(USER_FIRST_NAME) LIKE '%" + srchkey + "%' OR LOWER(USER_LAST_NAME) LIKE '%" + srchkey
					+ "%' ";
			query += "ORDER BY USER_LAST_NAME";

			List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
			return result;
		}
		
		@GetMapping("/getPropertyCommitNo")
		public String getPropertyTrackingCommitNo(@RequestParam String sbiNo){
			String results = propTracking.getPropertyCommitNo(sbiNo);
			return results;
		}
		
		@GetMapping("/getUserFullName")
		public String getUserFullName(@RequestParam String userId){
			String results = propTracking.getUserFullName(userId);
			
			if (results == null || results.isBlank()) {
		        return "";
		    }
			
			return Arrays.stream(results.trim().split("\\s+"))
	                 .filter(part -> !part.isBlank())
	                 .collect(Collectors.joining(", "));
		}
		
		@GetMapping("/getInstitutionNum")
		public String getInstitutionNum(@RequestParam String sbiNum, String commitNo){
			String results = propTracking.getInstitutionNum(sbiNum, commitNo);
			return results;
		}
		
		@GetMapping("/getNextPropSeq")
		public Integer getNextPropSeq(){
			Integer results = propTracking.genNextPropertySeq();
			return results;
		}
		
		@PostMapping("/updatePropertyTrackingHdr")
		public Map<String, Object> updatePropertyTrackingHdr(@RequestBody List<Map<String, Object>> updateSet) throws SQLException{
			
			Map<String, Object> response = new HashMap<>();
		    try {
		    	if (updateSet == null || updateSet.isEmpty()) {
		            response.put("status", "FAILURE");
		            response.put("message", "No data provided for update/insert.");
		            return response;
		        }
			//System.out.println("Ins/Update property hdr");
			Map<String, Object> obj = (Map<String, Object>) updateSet.get(0);
			//System.out.println("update property result: " + obj.get("PROP_TRK_SEQ_NO"));
			
			if(obj.get("PROP_TRK_SEQ_NO") != null) {
				
				//System.out.println("To Update");
				Integer seqNo = (Integer) obj.get("PROP_TRK_SEQ_NO");
				
				PropertyTrackingHdr propertyObj = propTracking.mapToPropertyHdrObj(obj);
				
				propTracking.updatePropertyColtHdr(seqNo, propertyObj);
					
				//System.out.println("***Updated Property Header***");
				//Map<String, Object> response = new HashMap<>();
                response.put("status", "SUCCESS");
                response.put("message", "Property header updated successfully.");
                
			}
			else {
				//System.out.println("To Insert");
				
				PropertyTrackingHdr propertyObj = propTracking.mapToPropertyHdrObj(obj);
				//Map<String, Object> response = new HashMap<>();
				response = propTracking.insertPropertyHdr(propertyObj);
				
				//System.out.println("***Inserted Property Header***");
				if (!response.containsKey("status")) {
	                response.put("status", "SUCCESS");
	            }
	            response.putIfAbsent("message", "Property header inserted successfully.");
			}
			} catch (Exception ex) {
		        System.err.println("Error in updatePropertyTrackingHdr: " + ex.getMessage());
		        ex.printStackTrace();
		        response.put("status", "FAILURE");
		        response.put("message", "Error while updating/inserting property header.");
		        response.put("details", ex.getMessage());
		    }
		    return response;
		}
		
		@PostMapping("/updatePropertyTrackingColtItems")
		public ResponseEntity<Map<String, Object>> updatePropertyTrackingColtItems(@RequestBody Map<String, Object> itmsToUpdate){

			Map<String, Object> response = new HashMap<>();
		    try {
				Integer propTrkSeqNo = (Integer) itmsToUpdate.get("propTrkSeqNo");
				//System.out.println("PropTrkSeqNo: " + propTrkSeqNo);
			    List<Map<String, Object>> updateSet = (List<Map<String, Object>>) itmsToUpdate.get("updateSet");
			    //System.out.println("Update property result: " + updateSet);
			    
			    if (propTrkSeqNo == null || updateSet == null) {
		            response.put("status", "FAILURE");
		            response.put("message", "Missing propTrkSeqNo or updateSet.");
		            return ResponseEntity.badRequest().body(response);
		        }
	
			    List<PropertyTrackingColtItems> prtrItemUpdList = new ArrayList<>();
			    List<PropertyTrackingColtItems> prtrItemInsrtList = new ArrayList<>();
			    for (Map<String, Object> itemMap : updateSet) {
			    	PropertyTrackingColtItems prtrItem = new PropertyTrackingColtItems();
			    	prtrItem.setPropItmRowid((String) itemMap.get("PROP_ITM_ROWID"));
			    	prtrItem.setItemCd((String) itemMap.get("ITEM_CD"));
			    	prtrItem.setItmItemCd((String) itemMap.get("ITM_ITEM_CD"));
			        prtrItem.setPthPropTrkSeqNo(itemMap.get("PTH_PROP_TRK_SEQ_NO") != null ? Integer.valueOf(itemMap.get("PTH_PROP_TRK_SEQ_NO").toString()) : null);
			        prtrItem.setPropInmtItmSeqNo(itemMap.get("PROP_INMT_ITM_SEQ_NO") != null ? Integer.valueOf(itemMap.get("PROP_INMT_ITM_SEQ_NO").toString()) : null);
			        prtrItem.setPropInmtItmQty(itemMap.get("PROP_INMT_ITM_QTY") != null ? Integer.valueOf(itemMap.get("PROP_INMT_ITM_QTY").toString()) : null);
			        prtrItem.setPropInmtItmDesc((String) itemMap.get("PROP_INMT_ITM_DESC"));
			        prtrItem.setPropInmtItmRetnQty(itemMap.get("PROP_INMT_ITM_RETN_QTY") != null ? Integer.valueOf(itemMap.get("PROP_INMT_ITM_RETN_QTY").toString()) : null);
			        prtrItem.setPropInmtRetnFstNm((String) itemMap.get("PROP_INMT_RETN_FST_NM"));
			        prtrItem.setPropInmtRetnLstNm((String) itemMap.get("PROP_INMT_RETN_LST_NM"));
			        prtrItem.setPropInmtRetnMidNm((String) itemMap.get("PROP_INMT_RETN_MID_NM"));
			        prtrItem.setPropInmtRetnSfx((String) itemMap.get("PROP_INMT_RETN_SFX"));
			        prtrItem.setRelationshipCode((String) itemMap.get("RELATIONSHIP_CODE"));
			        if(prtrItem.getPropItmRowid() != null && !prtrItem.getPropItmRowid().trim().isEmpty()) {
			        	prtrItemUpdList.add(prtrItem);
			        }else {
			        	prtrItemInsrtList.add(prtrItem);
			        }
			    }
			    
			    if(!prtrItemUpdList.isEmpty()) {
			    	propTracking.updateColtItems(propTrkSeqNo, prtrItemUpdList);
			    }
			    if(!prtrItemInsrtList.isEmpty()) {
			    	propTracking.insertPropertyColtItems(propTrkSeqNo, prtrItemInsrtList);
			    }
			    
			    response.put("status", "SUCCESS");
		        response.put("message", "Items updated/inserted successfully.");
		        return ResponseEntity.ok(response);
	        
		    } catch (Exception e) {
		        System.err.println("Error in updatePropertyTrackingColtItems: " + e.getMessage());
		        e.printStackTrace();
		        response.put("status", "FAILURE");
		        response.put("message", "An error occurred while processing property items.");
		        response.put("details", e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}
		
		@PostMapping("/updatePropertyTrackingIssdItems")
		public ResponseEntity<Map<String, Object>> updatePropertyTrackingIssdItems(@RequestBody Map<String, Object> itmsToUpdate){

			Map<String, Object> response = new HashMap<>();
		    try {
				Integer propTrkSeqNo = (Integer) itmsToUpdate.get("propTrkSeqNo"); // Extract the PropTrkSeqNo
				//System.out.println("PropTrkSeqNo: " + propTrkSeqNo);
				List<Map<String, Object>> updateSet = (List<Map<String, Object>>) itmsToUpdate.get("updateSet"); // Extract the updateSet
			    //System.out.println("Update property result--: " + updateSet);
			    
				if (propTrkSeqNo == null || updateSet == null) {
		            response.put("status", "FAILURE");
		            response.put("message", "Missing propTrkSeqNo or updateSet.");
		            return ResponseEntity.badRequest().body(response);
		        }
				
			    List<PropertyTrackingColtItems> prtrItemUpdList = new ArrayList<>();
			    List<PropertyTrackingColtItems> prtrItemInsrtList = new ArrayList<>();
			    for (Map<String, Object> itemMap : updateSet) {
			    	PropertyTrackingColtItems prtrItem = new PropertyTrackingColtItems();
			    	prtrItem.setPropItmRowid((String) itemMap.get("PROP_ITM_ROWID"));
			    	prtrItem.setItemCd((String) itemMap.get("ITEM_CD"));
			    	prtrItem.setItmItemCd((String) itemMap.get("ITM_ITEM_CD"));
			    	prtrItem.setPropDesc((String) itemMap.get("ITEM_DESC"));
			        prtrItem.setPthPropTrkSeqNo(itemMap.get("PTH_PROP_TRK_SEQ_NO") != null ? Integer.valueOf(itemMap.get("PTH_PROP_TRK_SEQ_NO").toString()) : null);
			        prtrItem.setPropInmtItmSeqNo(itemMap.get("PROP_DOC_ITM_NO") != null ? Integer.valueOf(itemMap.get("PROP_DOC_ITM_NO").toString()) : null);
			        prtrItem.setPropInmtItmQty(itemMap.get("PROP_DOC_ITM_QTY") != null ? Integer.valueOf(itemMap.get("PROP_DOC_ITM_QTY").toString()) : null);
			        prtrItem.setPropInmtItmDesc((String) itemMap.get("PROP_DOC_ITM_DESC"));
			        prtrItem.setPropInmtItmRetnQty(itemMap.get("PROP_DOC_ITM_RETN_QTY") != null ? Integer.valueOf(itemMap.get("PROP_DOC_ITM_RETN_QTY").toString()) : null);
			        if(prtrItem.getPropItmRowid() != null && !prtrItem.getPropItmRowid().trim().isEmpty()) {
			        	prtrItemUpdList.add(prtrItem);
			        }else {
			        	prtrItemInsrtList.add(prtrItem);
			        }
			    }
			    
			    if(!prtrItemUpdList.isEmpty()) {
			    	propTracking.updateIssdItems(propTrkSeqNo, prtrItemUpdList);
			    }
			    if(!prtrItemInsrtList.isEmpty()) {
			    	propTracking.insertPropertyIssdItems(propTrkSeqNo, prtrItemInsrtList);
			    }
			    
			    response.put("status", "SUCCESS");
		        response.put("message", "Items updated/inserted successfully.");
		        return ResponseEntity.ok(response);
	        
		    } catch (Exception e) {
		        System.err.println("Error in updatePropertyTrackingColtItems: " + e.getMessage());
		        e.printStackTrace();
		        response.put("status", "FAILURE");
		        response.put("message", "An error occurred while processing property items.");
		        response.put("details", e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}
		/** Property Tracking End */
		
		@PostMapping("/insertSearchedInmate")
		public void insertSearchInmate(String p_screen_code,String p_screen_name,String p_sbi_no) {
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			 String ValidSBI = inmateService.validSbiNo(p_sbi_no);
			 System.out.println("ValidSBI="+ValidSBI);
			 if("Y".equals(ValidSBI)) {
				Object details = authentication.getDetails();
			    Map<?, ?> detailsMap = (Map<?, ?>) details;
			    String p_user_id;
			    p_user_id = (String) detailsMap.get("userId");
			    String p_commit_no = inmateService.validCommitNo(p_sbi_no);
			    System.out.println("insertsearch="+p_screen_code+" "+p_screen_name+" "+p_sbi_no+" "+p_user_id);
			Desktops.searchValueInsert(p_screen_code, p_screen_name, p_sbi_no, p_commit_no, p_user_id);
			System.out.println("insertsearch="+p_screen_code+" "+p_screen_name+" "+p_sbi_no+" "+p_user_id+" "+p_commit_no);
			 }else {
				 System.out.println("Invalid SBI");
			 }
		}


}