
package com.omnet.cnt.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.Menu;
import com.omnet.cnt.Model.MenuCategory;

import com.omnet.cnt.Model.SubMenu;
import com.omnet.cnt.Model.User_Roles;
import com.omnet.cnt.Model.Users;
import com.omnet.cnt.Repository.MenuCategoryRepository;
import com.omnet.cnt.Repository.MenuRepository;

import com.omnet.cnt.Repository.SubMenuRepository;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.Repository.User_RolesRepository;
import com.omnet.cnt.Service.LoginService;

@Repository
@Service
public class UserService {

	private static final String String = null;

	@Autowired
	private UserRepository repo;

	@Autowired
	private HttpSession session;

	@Autowired
	private EmailService emailService;

	@Autowired
	private MenuRepository menuRepo;

	@Autowired
	private MenuCategoryRepository categoryrepo;

	@Autowired
	private SubMenuRepository submenurepo;

	@Autowired
	private User_RolesRepository UserRoles;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LoginService LoginService;

	public List<Users> userlist() {
		List<Users> U = repo.findAll();
		return U;
	}

	public Users createUser(Users users) {

		System.out.println("Inside create user method");

		System.out.println("username : " + users.getUserId());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object details = authentication.getDetails();
		Map<?, ?> detailsMap = (Map<?, ?>) details;
		String userId = (String) detailsMap.get("userId");
		
		System.out.println("User check : " + getUserByName(users.getUserId()).size());

		if (getUserByName(users.getUserId()).size() == 0) {

			System.out.println("Insert part");

			users.setInsertedUserId(userId);
			users.setUpdatedUserId(userId);
			// String password = users.getUserLastName() + "@Omnet_2024";

			String password = generateRandomString(8);
			String userID = "";
			if (users.getUserId().length() > 8) {
				userID = users.getUserId().substring(0, 8);
			} else {
				userID = users.getUserId();
			}
			System.out.println("user id is " + userID);

			users.setUserId(userID);
			// users.setUserId("0000008");
			// users.setInsertedUserId("asif");
			// users.setUpdatedUserId("asif");
			//emailService.sendEmail(users.getUserEmailAddress(), users.getUserFirstName(), users.getUserLastName(),
					//users.getUserId(), password);
			users.setEncryptedPassword(LoginService.getHashedPassword(password));
			
			System.out.println("Random Password : " + password);
			System.out.println("Encrpt Password : " + LoginService.getHashedPassword(password));
			
			users.setInitialPassword(password);
			users.setCurrentPassword(password);
		} else {

			System.out.println("Update part");
			users.setInsertedUserId(getUserByName(users.getUserId()).get(0).getInsertedUserId());
			// users.setUpdatedUserId(getUserByName(users.getUserName()).get(0).getUpdatedUserId());
			// users.setUpdatedUserId(session.getAttribute("userid").toString());
			// users.setEncryptedPassword(getUserByName(users.getUserName()).get(0).getEncryptedPassword());

			users.setInitialPassword(getUserByName(users.getUserId()).get(0).getInitialPassword());
			users.setCurrentPassword(getUserByName(users.getUserId()).get(0).getCurrentPassword());
			users.setEncryptedPassword(LoginService.getHashedPassword(getUserByName(users.getUserId()).get(0).getCurrentPassword()));
			users.setUserId(getUserByName(users.getUserId()).get(0).getUserId());

			/*
			 * users.setInitialPassword(getUserByName(users.getUserName()).get(0).
			 * getInitialPassword());
			 * users.setCurrentPassword(getUserByName(users.getUserName()).get(0).
			 * getCurrentPassword());
			 * users.setEncryptedPassword(hash(getUserByName(users.getUserName()).get(0).
			 * getCurrentPassword()));
			 * users.setUserId(getUserByName(users.getUserName()).get(0).getUserId());
			 */

			users.setUpdatedUserId(userId);
			/*
			 * users.setInitialPassword(getUserByName(users.getUserName()).get(0).
			 * getInitialPassword());
			 * users.setCurrentPassword(getUserByName(users.getUserName()).get(0).
			 * getCurrentPassword());
			 * users.setEncryptedPassword(hash(getUserByName(users.getUserName()).get(0).
			 * getCurrentPassword()));
			 * users.setUserId(getUserByName(users.getUserName()).get(0).getUserId());
			 */

		}
		return repo.save(users);
	}

	public List<Users> getUsers() {

		List<Users> UserList = new ArrayList<>();
		repo.findAll().forEach(user -> UserList.add(user));
		return UserList;
	}

	public List<Menu> getMenu() {
		List<Menu> MenuList = new ArrayList<>();
		menuRepo.findAll().forEach(menu -> MenuList.add(menu));
		return MenuList;
	}

	public List<MenuCategory> getMenuCategory() {
		List<MenuCategory> CategoryList = new ArrayList<>();
		categoryrepo.findAll().forEach((category -> CategoryList.add(category)));
		return CategoryList;
	}

	public List<SubMenu> getSubMenu() {
		List<SubMenu> SubMenuList = new ArrayList<>();
		submenurepo.findAll().forEach(submenu -> SubMenuList.add(submenu));
		return SubMenuList;
	}

	public List<User_Roles> AccessScreen(String userId) {
		userId = (String) session.getAttribute("userid");
		System.out.println(userId + " This is from the userservice class");
		return UserRoles.getscreeninfo(userId);
	}

	public StringBuffer menuContent(String userId) {

		List<User_Roles> userRolesList = UserRoles.getscreeninfo(userId);

		List<Menu> MenuList = new ArrayList<>();
		menuRepo.findAll().forEach(menu -> MenuList.add(menu));

		List<MenuCategory> CategoryList = new ArrayList<>();
		categoryrepo.findAll().forEach((category -> CategoryList.add(category)));

		StringBuffer content = new StringBuffer("<ul id=\"js-nav-menu\" class=\"nav-menu\" >");
		Boolean init_category = false;
		for (int i = 0; i < MenuList.size(); i++) {
			content.append("<li>");
			content.append("<a href=\"\" title=\"" + MenuList.get(i).getMenuName() + "\" data-filter-tags=\""
					+ "\"class=\"showonly\">");
			content.append(
					"<i class=\"fal fa-window\"></i> <span class=\"nav-link-text\">" + MenuList.get(i).getMenuName());
			content.append("</span>");
			content.append("</a>");

			init_category = false;
			int MCList_len = CategoryList.size() - 1;
			int SMList_len = userRolesList.size() - 1;
			for (int j = 0; j < CategoryList.size(); j++) {
				if (MenuList.get(i).getMenuCode().equals(CategoryList.get(j).getCategory_MENUCODE())) {

					if (!init_category) {

						content.append("<ul class=\"dropdown-menu multi-column columns-3\">");
						content.append("<div class=\"d-flex\" id=\"navListMenu\">");
						init_category = true;
					}

					content.append("<li><a  ><b class=\"nav-link-text\" ><u class=\"showonly\">"
							+ CategoryList.get(j).getCATEGORY_NAME());
					content.append("</u></b></a>");

					for (int k = 0; k < userRolesList.size(); k++) {
						if (CategoryList.get(j).getCATEGORY_CODE().equals(userRolesList.get(k).getCATEGORY_CODE())) {
							if ((userRolesList.get(k).getInsert_priv_flag().equals("Y"))
									&& (userRolesList.get(k).getUpdate_priv_flag().equals("Y"))) {
								content.append("<ol style=\"padding-left: 2px\">");
								content.append("<a href=\"" + userRolesList.get(k).getScreen_url() + "\" title=\""
										+ userRolesList.get(k).getScreen_Name()
										+ "\" data-filter-tags=\"ui components alerts\">");
								content.append("<span class=\"nav-link-text\" data-i18n=\"nav.ui_components_alerts\">"
										+ userRolesList.get(k).getScreen_Name() + "</span>");
							}

							else if (userRolesList.get(k).getView_priv_flag().equals("Y")) {
								content.append("<ol style=\"padding-left: 2px\">");
								content.append("<a href=\"" + userRolesList.get(k).getScreen_url() + "\" title=\""
										+ userRolesList.get(k).getScreen_Name()
										+ "\" data-filter-tags=\"ui components alerts\">");
								content.append("<span class=\"nav-link-text\" data-i18n=\"nav.ui_components_alerts\">"
										+ userRolesList.get(k).getScreen_Name() + "</span>");

							}

							else {
								content.append("<ol style=\"padding-left: 2px\">");
								content.append("<a href=\"" + userRolesList.get(k).getScreen_url() + "\" title=\""
										+ userRolesList.get(k).getScreen_Name()
										+ "\" data-filter-tags=\"ui components alerts\" class=\"disabled-link\">");
								content.append("<span class=\"nav-link-text\" data-i18n=\"nav.ui_components_alerts\" >"
										+ userRolesList.get(k).getScreen_Name() + "</span>");

							}

							content.append("</a></ol>");
						}
						if (SMList_len == k) {

						}
					}
				}
				if (MCList_len == j && init_category) {

					content.append("</div></ul>");
				}
			}
			content.append("</li>");

		}

		content.append("</ul>");

		return content;
	}

	public List<Users> getUserByName(String userId) {
		return repo.findAllByUserId(userId);
	}

	public List<String> findByUserId(String userId) {
		return repo.findInstNameByUserId(userId);
	}

	public record location(String institution, String instNum) {
	}

	public List<location> getLocationsByUserId(String userId) {
		List<Object[]> results = repo.findInstNameAndPrimaryInstNumByUserId(userId);
		return results.stream().map(result -> new location((String) result[0], (String) result[1])).toList();
	}

	/*
	 * public List<Users> getUserByNameAndPswd(String userName, String password) {
	 * // TODO Auto-generated method stub return
	 * repo.findAllByUserNameAndEncryptedPasswordAndUserStatus(userName,
	 * hash(password), "A"); }
	 */

	/*
	 * public List<Users> getByName(String userName) { // TODO Auto-generated method
	 * stub return repo.findUserByName(userName); }
	 */
	public String getCurrentPasswordByUserId(String userId) {
		return repo.findCurrentPasswordByUserId(userId);
	}

	/*
	 * @Transactional public List<Login> getAuth(String username, String password) {
	 * return loginRepo.OmnetValidateUserlogin(username, password); }
	 */

	/*
	 * @Transactional public List<Users> setUser(String userName, String userFName,
	 * String userLName, String userMName, String userSName, String userPhone,
	 * String userEmail, String userStatus, String userCaseload, String primaryInst,
	 * String userDistOff, String userUnitNum, String userTitle, String
	 * userParOfcrNum, String supUserId, String userPrimaryMenu, String userEmpId,
	 * String maxNumSessions, String instNum, String lastLoggedDttm, String
	 * loggedinUserid) { return repo.InsupdOmnetUser(userName, userFName, userLName,
	 * userMName, userSName, userPhone, userEmail, userStatus, userCaseload,
	 * primaryInst, userDistOff, userUnitNum, userTitle, userParOfcrNum, supUserId,
	 * userPrimaryMenu, userEmpId, maxNumSessions, instNum, lastLoggedDttm,
	 * loggedinUserid); }
	 */

	public String hash(String plainPassword) {
		/*
		 * MD5 md5 = new MD5(); String enc_pswd = plainPassword; for (int i = 0; i <
		 * 1000; i++) { enc_pswd = md5.getMd5(enc_pswd); }
		 */

		KnowledgeFactorySHA512 sha_code = new KnowledgeFactorySHA512();

		String enc_pswd = sha_code.encryptThisString(plainPassword);
		System.out.println("usersetup" + enc_pswd);

		return enc_pswd;
	}

	public static String convertStringToBinary(String input) {

		StringBuilder result = new StringBuilder();
		char[] chars = input.toCharArray();
		for (char aChar : chars) {
			result.append(String.format("%8s", Integer.toBinaryString(aChar)) // char -> int, auto-cast
					.replaceAll(" ", "0") // zero pads
			);
		}
		return result.toString();

	}

	public String generateRandomString(int length) {
		String randomString = "";

		final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890".toCharArray();
		final Random random = new Random();
		for (int i = 0; i < length; i++) {
			randomString = randomString + chars[random.nextInt(chars.length)];
		}

		return randomString;
	}

	public String generateRanId(int length) {
		String randomString = "";

		final char[] chars = "01234567890".toCharArray();
		final Random random = new Random();
		for (int i = 0; i < length; i++) {
			randomString = randomString + chars[random.nextInt(chars.length)];
		}

		return randomString;
	}

	public boolean isUserExists(String userId) {
		System.out.println("fafgfufuygeaf" + userId);
		Users user = repo.findByUsersid(userId);
		System.out.println("userrr" + user);
		return user != null;
	}

	// method to check the user has from 2 days for expiry password
	public List<Users> getUsersWithExpiryDateInDays(int days) {
		LocalDate targetDate = LocalDate.now().plusDays(days);
		return repo.findUsersByPasswordExpiryDate(targetDate);
	}

}