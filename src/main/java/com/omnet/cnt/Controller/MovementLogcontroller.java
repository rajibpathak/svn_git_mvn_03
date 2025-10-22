



package com.omnet.cnt.Controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.omnet.cnt.Model.Bed;
import com.omnet.cnt.Model.Menu;
import com.omnet.cnt.Model.MenuAccessScreen;
import com.omnet.cnt.Model.MenuCategory;
import com.omnet.cnt.Model.MoveMentLogActivity_Type;
import com.omnet.cnt.Model.NOTISearchTotalCount;
import com.omnet.cnt.Model.NOTI_SEARCH_TAB;
import com.omnet.cnt.Model.NotiCategory;
import com.omnet.cnt.Model.NotiUserName;
import com.omnet.cnt.Model.NotificationOmnetDesktop;
import com.omnet.cnt.Model.Originating_Location;
import com.omnet.cnt.Model.UserNotificationDetails;
import com.omnet.cnt.Model.User_Roles;
import com.omnet.cnt.Model.Users;
import com.omnet.cnt.Repository.NotificationRepository;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.Service.BedService;
import com.omnet.cnt.Service.CaseNotesService;
import com.omnet.cnt.Service.HousingFacilityBedAssignmentsService;
import com.omnet.cnt.Service.InmateService;
import com.omnet.cnt.Service.MovementLogservice;
import com.omnet.cnt.Service.OmnetDesktopService;
import com.omnet.cnt.Service.SwappingBedService;
import com.omnet.cnt.Service.UserService;
import com.omnet.cnt.Service.VitalService;

import org.springframework.util.StringUtils;

import com.omnet.cnt.Service.UserNotificationService;

@RestController
public class MovementLogcontroller {

	@Autowired
	private MovementLogservice Mov;
	
	@Autowired
	private InmateService inmateService;
	
	@Autowired
	private SwappingBedService Swap;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserNotificationService UserNotificationService;
	
	 @Autowired
	    private ObjectMapper objectMapper;
	 
	 @Autowired
	 private CaseNotesService cs;
	 
	 @Autowired
	 private BedService Bedservice;
	 
	 @Autowired
	 private HousingFacilityBedAssignmentsService housingFacility;
	 
	 @Autowired
	 private OmnetDesktopService OmnetDesktop;
	 
	 @Autowired
	 private com.omnet.cnt.Service.MenuSetupService MenuSetupService;

	  
	 @GetMapping(value="Casenotestest")
	 public ResponseEntity<Map<String, Object>> executeMainQueryCSMSN(String commitNo, String fromDate, String toDate, String relationship, String typeOfContact, String results){
		 commitNo="0045073";
		return cs.executeMainQuery(commitNo, fromDate, toDate, relationship, typeOfContact, results);
		 
	 }
	 
	 @GetMapping(value="Casenotestest1")
	 public ResponseEntity<Map<String, Object>> Historyofcn(String sbiNo){
		 sbiNo="0045073";
		 return cs.casenotesHistory(sbiNo);
	 }
	 

	 
		@GetMapping(value="MOV_LOSC_Tablec")
		public List<Map<String, Object>> MovTable(String IOVC_SBI_NO, String IOVC_INST_NUM, String IOVC_ACTVY_TYP_CD,
		                                          String IOVC_DEPART_LOC, String IOVC_ARRIV_LOC, Date IOVC_DATE_FROM,
		                                          Date IOVC_DATE_TO, String ivc_from_time, String ivc_to_time,
		                                          String ivc_order_by, String inu_asc_desc) {

		    String Hard_SBI_NO = inmateService.HardSbi(IOVC_SBI_NO);	
		    String ValidSBI = inmateService.validSbiNo(IOVC_SBI_NO);

		    Map<String, Object> result = new HashMap<>();
		    List<Map<String, Object>> movData = null;

		    // Case 1: SBI number is not empty
		    if (IOVC_SBI_NO != null && !IOVC_SBI_NO.isEmpty()) {
		        System.out.println("IOVC_SBI_NO=" + IOVC_SBI_NO);

		        if ("Y".equals(ValidSBI)) {
		            System.out.println("ValidSBI=Y");
		            result.put("ValidSBI", "Y");

		            movData = Mov.MovTablec(IOVC_SBI_NO, IOVC_INST_NUM, IOVC_ACTVY_TYP_CD,
		                                    IOVC_DEPART_LOC, IOVC_ARRIV_LOC, IOVC_DATE_FROM, IOVC_DATE_TO,
		                                    ivc_from_time, ivc_to_time, ivc_order_by, inu_asc_desc);

		        } else if (Hard_SBI_NO != null) {
		            result.put("ValidSBI", Hard_SBI_NO);

		            // Use Hard_SBI_NO in MovTablec call
		            movData = Mov.MovTablec(Hard_SBI_NO, IOVC_INST_NUM, IOVC_ACTVY_TYP_CD,
		                                    IOVC_DEPART_LOC, IOVC_ARRIV_LOC, IOVC_DATE_FROM, IOVC_DATE_TO,
		                                    ivc_from_time, ivc_to_time, ivc_order_by, inu_asc_desc);

		        } else {
		            result.put("ValidSBI", "N");

		            // Still call MovTablec with the original SBI (could be invalid)
		            movData = Mov.MovTablec(IOVC_SBI_NO, IOVC_INST_NUM, IOVC_ACTVY_TYP_CD,
		                                    IOVC_DEPART_LOC, IOVC_ARRIV_LOC, IOVC_DATE_FROM, IOVC_DATE_TO,
		                                    ivc_from_time, ivc_to_time, ivc_order_by, inu_asc_desc);
		        }

		    // Case 2: SBI number is empty but institute is present
		    } else if (IOVC_INST_NUM != null && (IOVC_SBI_NO == null || IOVC_SBI_NO.isEmpty())) {
		        result.put("ValidSBI", "F");  // or "INST_ONLY"
		        
		        movData = Mov.MovTablec(IOVC_SBI_NO, IOVC_INST_NUM, IOVC_ACTVY_TYP_CD,
		                                IOVC_DEPART_LOC, IOVC_ARRIV_LOC, IOVC_DATE_FROM, IOVC_DATE_TO,
		                                ivc_from_time, ivc_to_time, ivc_order_by, inu_asc_desc);

		    }

		    result.put("data", movData);
		    return Collections.singletonList(result);
		}

	
	  @GetMapping(value="MOV_LOSC_Activity_Type")
		public List<MoveMentLogActivity_Type> ActivityType(){
			List<MoveMentLogActivity_Type> activityType=Mov.MovementLogActivityType();
			return activityType;	
		}
	
	  @GetMapping(value="MOV_LOSC_Originating_Location")
			public List<Originating_Location> Originating_Location(String instNum){
				List<Originating_Location> Originating_Location=Mov.Originatinglocation(instNum);
				return Originating_Location;
				
			}
			
			  @GetMapping(value="MOV_LOSC_OFFENDER_HOUSING_DETAILS")
			  public Map<String, Object> Movementlog(String SBi,String ivc_commit_no, String ivc_inst_num) throws SQLException {
				  String hardSbiNo = inmateService.HardSbi(SBi);
				  
				  if (hardSbiNo != null && !hardSbiNo.trim().isEmpty()) {
					  SBi = hardSbiNo;
						}
				  
				  ivc_commit_no = inmateService.validCommitNo(SBi);
				  System.out.println("SBi="+ivc_commit_no);
				  Map<String, Object> MovLog=Mov.OFD(ivc_commit_no, ivc_inst_num);
					        return MovLog;  
		}
	  
			  @GetMapping(value="MOV_SWAP_SBI_INPUT")
				public Map<String, Object> MovTable(String p_sbi_no, String p_user_id){
				  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					Object details = authentication.getDetails();
				    Map<?, ?> detailsMap = (Map<?, ?>) details;
				    p_user_id = (String) detailsMap.get("userId");
				    Map<String, Object> response = new HashMap<>();
				    String Hard_SBI_NO=inmateService.HardSbi(p_sbi_no);
				    if(Hard_SBI_NO!=null) {
				    	String Result =inmateService.validSbiNo(Hard_SBI_NO);
						List<Map<String, Object>> sb=Swap.SwapBedGet(Hard_SBI_NO,p_user_id);
						 String Userinst=inmateService.getUserInstNum(p_user_id);
						 String ivc_commit_no = inmateService.validCommitNo(Hard_SBI_NO);
						 String Inmateinst=inmateService.getInmateInst(ivc_commit_no);
						response.put("sb", sb);
				        response.put("Result", Result);
				        response.put("Hard_SBI_NO", Hard_SBI_NO);
				        response.put("Userinst", Userinst);
				        response.put("Inmateinst", Inmateinst);
				    }
				    else {
				String Result =inmateService.validSbiNo(p_sbi_no);
				List<Map<String, Object>> sb=Swap.SwapBedGet(p_sbi_no,p_user_id);
				 String Userinst=inmateService.getUserInstNum(p_user_id);
				 String ivc_commit_no = inmateService.validCommitNo(p_sbi_no);
				 String Inmateinst=inmateService.getInmateInst(ivc_commit_no);
				response.put("sb", sb);
		        response.put("Result", Result);
		        response.put("Hard_SBI_NO", Hard_SBI_NO);
		        response.put("Userinst", Userinst);
		        response.put("Inmateinst", Inmateinst);
				    }
				    return response;
				}
			  
			  @PostMapping(value="MOV_SWAP_VALUES")
			  public void SwappingOffedervalues(String p_commit_no1, String p_commit_no2, String p_bed_no1, String p_bed_no2) throws SQLException {
				  Swap.SwapBeds(p_commit_no1, p_commit_no2, p_bed_no1, p_bed_no2);
			  }
			  
				@GetMapping("/menudata")
			    public ResponseEntity<Map<String, Object>> MenuIconData(String userId) {
			    Map<String, Object> response = new HashMap<>();
			    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Object details = authentication.getDetails();
			    Map<?, ?> detailsMap = (Map<?, ?>) details;
			    userId = (String) detailsMap.get("userId");
			    List<User_Roles> userRolesList =userService.AccessScreen(userId);   
		        System.out.println("Menu testing");
		        List<Menu> menuList = userService.getMenu();
		        
		        List<MenuCategory> categoryList = userService.getMenuCategory();

		        response.put("menuList", menuList);
		        response.put("categoryList", categoryList);
		        response.put("userRolesList", userRolesList);
			    
			 try {
			            ClassPathResource resource = new ClassPathResource("static/json/MenuIcons.json");
			            
			            
			            List<Map<String, Object>> jsonArray = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Map<String, Object>>>() {});
			            response.put("menuIcons", jsonArray);
			        } catch (IOException e) {
			            
			            response.put("error", "Error reading MenuIcons.json: " + e.getMessage());
			        }
			        
			        return ResponseEntity.ok(response);
			}
				
			    @GetMapping("NOTI")
				public List<Map<String, Object>> UserNT1(String ivc_noti_type, String p_user_id){
			    	ivc_noti_type="A";
			    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					Object details = authentication.getDetails();
				    Map<?, ?> detailsMap = (Map<?, ?>) details;
				    p_user_id = (String) detailsMap.get("userId");
					List<Map<String, Object>> Noti=UserNotificationService.GetNoti1(ivc_noti_type, p_user_id);
					return Noti;
				} 
			    
			    @GetMapping("NOTI_info")
							public List<Map<String, Object>> UserNT2(String ivc_noti_type,String p_user_id){
						    	ivc_noti_type="I";
						    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
								Object details = authentication.getDetails();
							    Map<?, ?> detailsMap = (Map<?, ?>) details;
							    p_user_id = (String) detailsMap.get("userId");
								List<Map<String, Object>> info=UserNotificationService.GetNoti2(ivc_noti_type,p_user_id);
								return info;
							} 

			    @GetMapping("NOTI_dtl")
				public List<Map<String, Object>> Searchvalue(String inu_sort_by,String ivc_short_message,String p_user_id){
			    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					Object details = authentication.getDetails();
				    Map<?, ?> detailsMap = (Map<?, ?>) details;
				    p_user_id = (String) detailsMap.get("userId");
					List<Map<String, Object>> info=UserNotificationService.Search(inu_sort_by, ivc_short_message,p_user_id);
					return info;
				} 
			    
			    
			    @GetMapping(value="CBI_NOTI_Search")
				public Map<String, Object> NOTISEARCHTAB(String p_sbi_no, String p_last_name, Date 
				p_date_frm, Date p_date_to, int p_sort_by, String p_sort_tp, String
		p_all_flg, String P_Short_msg, String p_sent_by, String ivc_noti_env ,String p_user_id){
				    Map<String, Object> NOTISEARCH = new HashMap<>();
				    String Hard_SBI_NO=inmateService.HardSbi(p_sbi_no);
				    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					Object details = authentication.getDetails();
				    Map<?, ?> detailsMap = (Map<?, ?>) details;
				    p_user_id = (String) detailsMap.get("userId");
				    if(Hard_SBI_NO!=null) {
				    	String Result =inmateService.validSbiNo(Hard_SBI_NO);
						List<NOTI_SEARCH_TAB> USN=UserNotificationService.SearchTabVakues(Hard_SBI_NO, p_last_name, p_date_frm, p_date_to, p_sort_by, p_sort_tp, p_all_flg, P_Short_msg, p_sent_by, ivc_noti_env, p_user_id); 
						NOTISEARCH.put("USN", USN);
				        NOTISEARCH.put("Result", Result);
				        NOTISEARCH.put("Hard_SBI_NO", Hard_SBI_NO);
				    }
				    else {
				String Result =inmateService.validSbiNo(p_sbi_no);
				List<NOTI_SEARCH_TAB> USN=UserNotificationService.SearchTabVakues(p_sbi_no, p_last_name, p_date_frm, p_date_to, p_sort_by, p_sort_tp, p_all_flg, P_Short_msg, p_sent_by, ivc_noti_env, p_user_id); 
				NOTISEARCH.put("USN", USN);
		        NOTISEARCH.put("Result", Result);
		        NOTISEARCH.put("Hard_SBI_NO", Hard_SBI_NO);
					
				    }
				    return NOTISEARCH;
				}
							  
							  @GetMapping(value="CBI_NOTI_SentByUser") 
							  public List<NotiUserName> sentByUserName(){
								  List<NotiUserName> LFMS=UserNotificationService.SentByUserName();
								  return  LFMS;
							  }
							  
							  @GetMapping(value="CBI_NOTI_Category") 
							  public List<NotiCategory> Categoryvalues(){
								  List<NotiCategory> Cat=UserNotificationService.Category();
								  return  Cat;
							  }
							  
							  @PostMapping(value="CBI_Noti_VALUES", consumes = "application/json")
							  public void NotiDetailsvalues(@RequestBody List<UserNotificationDetails> detailsValues) throws SQLException {
								  String p_user_id="";
								  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
									Object details = authentication.getDetails();
								    Map<?, ?> detailsMap = (Map<?, ?>) details;
								    p_user_id = (String) detailsMap.get("userId");
								  UserNotificationService.callproceduredetail(detailsValues, p_user_id);
							  }
							  
								@GetMapping("/facilityBuilding")
								public ResponseEntity<?> getBuildingHousingFacility(String instNum) {
									
									List<Object[]> buildings = housingFacility.getBuildingsForInstNum(instNum);
									Map<String, Object> response = new HashMap<>();
									response.put("bedDetails", buildings);
							        return ResponseEntity.ok(response); 

								}

								@GetMapping("/facilityUnit")
								public ResponseEntity<List<Object[]>> getUnitHousingFacility(@RequestParam String instNum, @RequestParam String bldNum) {
									List<Object[]> units = housingFacility.getUnitsFacility(instNum, bldNum);
									return ResponseEntity.ok(units);
								}
								
								@GetMapping("/facilityFloor")
								public ResponseEntity<List<Object[]>> getFloorHousingFacility(String instNum, String bldNum, String unitId) {
									List<Object[]> floors = housingFacility.getFloorsFacility(instNum, bldNum, unitId);
									return ResponseEntity.ok(floors);
								}
							  
								@GetMapping("/facilityTier")
								public ResponseEntity<List<Object[]>> getTierHousingFacility(String instNum, String bldNum, String unitId, String floorNum) {
									List<Object[]> Tier = housingFacility.getTiersFacility(instNum, bldNum, unitId, floorNum);
									return ResponseEntity.ok(Tier);
								}
								
								@GetMapping("/facilityCells")
								public ResponseEntity<List<Object[]>> getCellHousingFacility(String instNum, String bldNum, String unitId, String floorNum, String tierNum) {
									List<Object[]> Cells = housingFacility.getCellsFacility(instNum, bldNum, unitId, floorNum, tierNum);
									return ResponseEntity.ok(Cells);
								}
								
								@GetMapping(value = "facilityBed")
								public List<Bed> BedValue(String instNum, String BLDNO, String UNIT_ID, String FloorNo, String TierNo,
										String CellNo) {
									List<Bed> BedValue = housingFacility.bedFacility(instNum, BLDNO, UNIT_ID, FloorNo, TierNo, CellNo);
									return BedValue;
								}
								
								@GetMapping(value = "/MenuSetupValue")
								public List<Object[]> valueMenuSetup() {
									return OmnetDesktop.getMenuValue();
									
								}
								 
								 @GetMapping(value = "/MenuSetupMenuValue")
								 public List<Menu> getMenuSetupMenu() {
									 return MenuSetupService.getMenu();
								 }
								 
								 @GetMapping(value = "/getMenuAccess")
								 public List<MenuAccessScreen> getMenuAccessScreen(String userId){
									 return MenuSetupService.getMenuAccess(userId);
								 }
								 
								 @PostMapping(value="insertMainMenu")
								  public void insertNewMainMenu(String p_menu_code, String p_menu_name, String p_status, String p_screen_code,String p_icon_path) throws SQLException {
									 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
										Object details = authentication.getDetails();
									    Map<?, ?> detailsMap = (Map<?, ?>) details;
									    String p_inserted_user="";
									    p_inserted_user = (String) detailsMap.get("userId");
									    System.out.println("InsertMenu="+p_menu_code+" "+p_menu_name+" "+p_status+" "+p_screen_code+" "+p_inserted_user);
									 MenuSetupService.insertNewMenu(p_menu_code, p_menu_name, p_status, p_inserted_user, p_screen_code,p_icon_path);;
								  }
								 
								 @PostMapping("/updateMainMenu")
								 public ResponseEntity<String> insertMainMenu(
								     @RequestParam("p_menu_code") String menuCode,
								     @RequestParam("p_menu_name") String menuName,
								     @RequestParam("p_status") String status,
								     @RequestPart(value = "p_icon", required = false) MultipartFile iconFile
								 ) {
								     try {
								         byte[] iconBytes = null;
								         if (iconFile != null && !iconFile.isEmpty()) {
								             iconBytes = iconFile.getBytes();
								         }
								         System.out.println("Update main menu controller");
								         MenuSetupService.updateMenu(menuCode, menuName, status, iconBytes);

								         return ResponseEntity.ok("Menu inserted successfully");
								     } catch (Exception e) {
								         e.printStackTrace();
								         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								                              .body("Insert failed");
								     }
								 }

								 
//								 @PostMapping(value="updateMainMenu")
//								  public void updateMainMenu(String p_menu_code, String p_menu_name, String p_status) throws SQLException {
//									    System.out.println("updateMenu="+p_menu_code+" "+p_menu_name+" "+p_status);
//									 MenuSetupService.updateMenu(p_menu_code, p_menu_name, p_status);
//								  }
								 
//								 @GetMapping(value = "/getActiveMenu")
//								 public List<Menu> getActiveMenuNavbar(){
//									 return MenuSetupService.getActiveMenu();
//								 }
								 
								 @GetMapping("/getActiveMenu")
								 public Map<String, Object> getActiveMenuNavbar() throws IOException {
								     List<Menu> activeMenus = MenuSetupService.getActiveMenu();

								     // Load JSON file from resources
								     ClassPathResource resource = new ClassPathResource("static/json/MenuIcons.json");
								     ObjectMapper mapper = new ObjectMapper();
								     Object extraMenu = mapper.readValue(resource.getInputStream(), Object.class);

								     // Build response directly in controller
								     Map<String, Object> response = new HashMap<>();
								     response.put("activeMenus", activeMenus);
								     response.put("extraMenu", extraMenu);

								     return response;
								 }

								 
								 @GetMapping(value="/getSubMenuSetup")
									public List<Map<String,Object>> valueSubMenu(String p_menu_code){
									 List<Map<String,Object>> subMenu= MenuSetupService.getSubMenu(p_menu_code);
									 System.out.println(" SubMenu="+p_menu_code);
									 return subMenu;
								 }
								 
//								 @PostMapping("/uploadImageMenuSetup")
//								    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile imageFile) {
//								       
//								       String IMAGE_FOLDER = "src/main/resources/static/img/demo/menuicons/";
//
//								        try {
//								            if (imageFile.isEmpty()) {
//								                return ResponseEntity.badRequest().body(Map.of("error", "Empty file"));
//								            }
//
//								            // Clean and get original filename
//								            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
//
//								            // Create folder if not exists
//								             Path uploadPath = Paths.get(IMAGE_FOLDER);
//								            // if (!Files.exists(uploadPath)) {
//								            //     Files.createDirectories(uploadPath);
//								            // }
//
//								            // Save the file
//								            Path filePath = uploadPath.resolve(originalFilename);
//								            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//								            // Construct public URL
//								            String imageUrl = "/img/demo/menuicons/" + originalFilename;
//								            return ResponseEntity.ok(Map.of(
//								                "message", "Image uploaded successfully",
//								                "filename", originalFilename,
//								                "url", imageUrl
//								            ));
//
//								        } catch (IOException e) {
//								            e.printStackTrace();
//								            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//								                    .body(Map.of("error", "Failed to upload image"));
//								        }
//								    }
								 
							
								 @PostMapping("/uploadImageMenuSetup")
								 public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
								     
									 if (file.isEmpty()) {
								         return ResponseEntity.badRequest().body("No file selected!");
								     }
								     try {
								         String projectRoot = System.getProperty("user.dir");
								         // Correct path
								         String srcPath = projectRoot + "/src/main/resources/static/img/demo/menuicons/";
								         File srcDir = new File(srcPath);
								         if (!srcDir.exists()) srcDir.mkdirs();

								         File srcFile = new File(srcDir, file.getOriginalFilename());
								         file.transferTo(srcFile);

								         // Optional: also copy to target if needed for development
								         String targetPath = projectRoot + "/target/classes/static/img/demo/menuicons/";
								         File targetDir = new File(targetPath);
								         if (!targetDir.exists()) targetDir.mkdirs();

								         Files.copy(
								             srcFile.toPath(),
								             new File(targetDir, file.getOriginalFilename()).toPath(),
								             StandardCopyOption.REPLACE_EXISTING
								         );

								         // Correct static URL
								         String imageUrl = "/img/demo/menuicons/" + file.getOriginalFilename();
								         return ResponseEntity.ok(imageUrl); // Just return URL string

								     } catch (IOException e) {
								         e.printStackTrace();
								         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								                              .body("Failed to save the file.");
								     }
								 }

								 
								 @PostMapping(value="insertSubMenu")
								  public void insertNewSubMenu(@RequestParam("p_sub_menu_code") String p_sub_menu_code,@RequestParam("p_sub_menu_name")  String p_sub_menu_name,
@RequestParam("p_menu_code") String p_menu_code, @RequestParam(value = "p_status", required = false) String p_status, @RequestParam(value = "p_menu_icon", required = false) String p_menu_icon, @RequestParam(value = "p_screen_code", required = false) String p_screen_code,@RequestParam(value = "p_icon_path", required = false)String p_icon_path) throws SQLException {
									    System.out.println("InsertSubMenu="+p_menu_code+" "+p_sub_menu_name+" "+p_status);
									 MenuSetupService.insertSubMenu(p_sub_menu_code, p_sub_menu_name, p_menu_code, p_status, p_menu_icon, p_screen_code,p_icon_path);
								  }
								 
								 @PostMapping(value="updateSubMenu")
								  public void updateSubMenu(@RequestParam("p_sub_menu_code") String p_sub_menu_code,@RequestParam("p_sub_menu_name")  String p_sub_menu_name,
@RequestParam("p_status") String p_status) throws SQLException {
									    System.out.println("updateSubMenu="+p_sub_menu_code+" "+p_sub_menu_name+" "+p_status);
									 MenuSetupService.updateSubMenu(p_sub_menu_code, p_sub_menu_name, p_status);
								  }
								 
								 @GetMapping(value="/getMainMenuDisplay")
									public List<Map<String,Object>> valueMenu(String p_menu_code){
									 p_menu_code = null;
									 List<Map<String,Object>> Menu= MenuSetupService.getMainMenu(p_menu_code);
									 System.out.println(" SubMenu="+p_menu_code);
									 return Menu;
								 }
								 
}
