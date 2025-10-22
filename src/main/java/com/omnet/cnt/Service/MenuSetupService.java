package com.omnet.cnt.Service;

import java.util.Map;

import javax.sql.DataSource;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.Menu;
import com.omnet.cnt.Model.MenuAccessScreen;
import com.omnet.cnt.Repository.HousingFacilityBedAssignmentsRepository;
import com.omnet.cnt.Repository.MenuAccessRepository;
import com.omnet.cnt.Repository.MenuRepository;
import com.omnet.cnt.Repository.SubMenuRepository;

@Service
public class MenuSetupService {

	@Autowired
	private SubMenuRepository subMenu;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private MenuRepository menuRepo;
	
	@Autowired
	private MenuAccessRepository MenuAccess;
	
	@Autowired
    private DataSource dataSource;

	
	  public List<Menu> getMenu() {
			List<Menu> MenuList = new ArrayList<>();
			menuRepo.findAll().forEach(menu -> MenuList.add(menu));
			return MenuList;
		}
	  
	  public List<MenuAccessScreen> getMenuAccess(String userId){
		  return MenuAccess.getScreenAccess(userId);
	  }
	  
	  public void insertNewMenu(String p_menu_code, String p_menu_name, String p_status, String p_inserted_user, String p_screen_code, String p_icon_path) {
		  System.out.println("InsertMenuServive="+p_menu_code+" "+p_menu_name+" "+p_status+" "+p_screen_code+" "+p_inserted_user);
		  String procedureSql = "{call SPKG_ADM_MENU.Sp_Menu_insert(?, ?, ?, ?, ?, ?)}";
		  try (
		            Connection conn = dataSource.getConnection();
		            CallableStatement stmt = conn.prepareCall(procedureSql)
		        ){
		            stmt.setString(1, p_menu_code);      
		            stmt.setString(2, p_menu_name);    
		            stmt.setString(3, p_status);      
		            stmt.setString(4, p_inserted_user);      
		            stmt.setString(5, p_screen_code);        
		            stmt.setString(6, p_icon_path); 
		            stmt.execute();
		            System.out.println("Menu inserted");
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
	  }
	  
//	  public void updateMenu(String p_menu_code, String p_menu_name, String p_status, String  p_icon) {
//		  System.out.println("updateMenuServive="+p_menu_code+" "+p_menu_name+" "+p_status);
//		  String procedureSql = "{call SPKG_ADM_MENU.SP_MENU_UPDATE(?, ?, ?)}";
//		  try (
//		            Connection conn = dataSource.getConnection();
//		            CallableStatement stmt = conn.prepareCall(procedureSql)
//		        ){
//		            stmt.setString(1, p_menu_code);      
//		            stmt.setString(2, p_menu_name);    
//		            stmt.setString(3, p_status);   
//		            if (p_icon != null && !p_icon.isEmpty()) {
//		                // Convert the String to bytes (assume UTF-8 or base64-decoded if necessary)
//		                byte[] iconBytes = p_icon.getBytes(StandardCharsets.UTF_8);
//		                ByteArrayInputStream iconStream = new ByteArrayInputStream(iconBytes);
//		                stmt.setBlob(4, iconStream);
//		            } else {
//		                stmt.setNull(4, Types.BLOB);
//		            }
//		           
//		            stmt.execute();
//		            System.out.println("Menu inserted");
//		        } catch (Exception e) {
//		            e.printStackTrace();
//		        }
//	  }
	
	  public void updateMenu(String menuCode, String menuName, String status, byte[] iconBytes) {
		    String procedureSql = "{call SPKG_ADM_MENU.SP_MENU_UPDATE(?, ?, ?, ?)}";
		    		System.out.println("Update main menu");
		    try (
		        Connection conn = dataSource.getConnection();
		        CallableStatement stmt = conn.prepareCall(procedureSql)
		    ) {
		        stmt.setString(1, menuCode);
		        stmt.setString(2, menuName);
		        stmt.setString(3, status);

		        if (iconBytes != null) {
		            stmt.setBlob(4, new ByteArrayInputStream(iconBytes));
		        } else {
		            stmt.setNull(4, Types.BLOB);
		        }

		        stmt.execute();
		        System.out.println("Menu updated");
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

	  
	  public List<Menu> getActiveMenu(){
		  return menuRepo.findByStatus("A");
	  }
	  
		public List<Map<String,Object>> getSubMenu(String p_menu_code) {
		    System.out.println("service SubMenu="+p_menu_code);
			List<Map<String, Object>> resultList = new ArrayList<>();
		    try (Connection conn = dataSource.getConnection()) {
		        String sql = "{call SPKG_ADM_MENU.SP_SUBMENU_QUERY(?, ?)}"; 
		        try (CallableStatement stmt = conn.prepareCall(sql)) {
		        	stmt.setString(1, p_menu_code);
		            stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
		            stmt.execute();
		        	try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
		        		ResultSetMetaData metaData = rs.getMetaData();
		        		int columnCount = metaData.getColumnCount();
		        		while (rs.next()) {
		        		Map<String, Object> resultMap = new HashMap<>();
		        		for (int i = 1; i <= columnCount; i++) {
		        		String columnName = metaData.getColumnName(i);
		        		Object columnValue = rs.getObject(i);
		        		resultMap.put(columnName, columnValue);
		        		}
		        		resultList.add(resultMap);
		        		}
		        		}
		        	
	     
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return resultList;
		}
		
		 public void insertSubMenu(String p_sub_menu_code, String p_sub_menu_name, String p_menu_code, String p_status, String p_menu_icon, String p_screen_code, String p_icon_path) {
			  System.out.println("InsertMenuServive="+p_menu_code+" "+p_sub_menu_code+" "+p_status+" "+p_screen_code+" "+p_sub_menu_name);
			  String procedureSql = "{call SPKG_ADM_MENU.SP_SUBMENU_INSERT(?, ?, ?, ?, ?, ?, ?)}";
			  try (
			            Connection conn = dataSource.getConnection();
			            CallableStatement stmt = conn.prepareCall(procedureSql)
			        ){
			            stmt.setString(1, p_sub_menu_code);      
			            stmt.setString(2, p_sub_menu_name);    
			            stmt.setString(3, p_menu_code);      
			            stmt.setString(4, p_status);      
			            if (p_menu_icon != null && !p_menu_icon.isEmpty()) {
			                // Convert the String to bytes (assume UTF-8 or base64-decoded if necessary)
			                byte[] iconBytes = p_menu_icon.getBytes(StandardCharsets.UTF_8);
			                ByteArrayInputStream iconStream = new ByteArrayInputStream(iconBytes);
			                stmt.setBlob(5, iconStream);
			            } else {
			                stmt.setNull(5, Types.BLOB);
			            }
			            stmt.setString(6, p_screen_code); 
			            stmt.setString(7, p_icon_path); 
			            
			           
			            stmt.execute();
			            System.out.println("Sub Menu inserted");
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
		  }
		 
		  public void updateSubMenu(String p_sub_menu_code, String p_sub_menu_name, String p_status) {
			  System.out.println("updateSubMenuServive="+p_sub_menu_code+" "+p_sub_menu_name+" "+p_status);
			  String procedureSql = "{call SPKG_ADM_MENU.SP_SUBMENU_UPDATE(?, ?, ?)}";
			  try (
			            Connection conn = dataSource.getConnection();
			            CallableStatement stmt = conn.prepareCall(procedureSql)
			        ){
			            stmt.setString(1, p_sub_menu_code);      
			            stmt.setString(2, p_sub_menu_name);    
			            stmt.setString(3, p_status);            
			           
			            stmt.execute();
			            System.out.println("SubMenu inserted");
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
		  }
		  
//		  public List<Map<String,Object>> getMainMenu(String p_menu_code) {
//			    System.out.println("service SubMenu="+p_menu_code);
//				List<Map<String, Object>> resultList = new ArrayList<>();
//			    try (Connection conn = dataSource.getConnection()) {
//			        String sql = "{call SPKG_ADM_MENU.SP_MENU_QUERY(?, ?)}"; 
//			        try (CallableStatement stmt = conn.prepareCall(sql)) {
//			        	stmt.setString(1, p_menu_code);
//			            stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
//			            stmt.execute();
//			        	try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
//			        		ResultSetMetaData metaData = rs.getMetaData();
//			        		int columnCount = metaData.getColumnCount();
//			        		while (rs.next()) {
//			        		Map<String, Object> resultMap = new HashMap<>();
//			        		for (int i = 1; i <= columnCount; i++) {
//			        		String columnName = metaData.getColumnName(i);
//			        		Object columnValue = rs.getObject(i);
//			        		resultMap.put(columnName, columnValue);
//			        		}
//			        		resultList.add(resultMap);
//			        		}
//			        		}
//			        	
//		     
//			        }
//			    } catch (SQLException e) {
//			        e.printStackTrace();
//			    }
//			    return resultList;
//			}
		  
		  public List<Map<String,Object>> getMainMenu(String p_menu_code) {
			    System.out.println("service SubMenu=" + p_menu_code);
			    List<Map<String, Object>> resultList = new ArrayList<>();
			    
			    try (Connection conn = dataSource.getConnection()) {
			        String sql = "{call SPKG_ADM_MENU.SP_MENU_QUERY(?, ?)}"; 
			        try (CallableStatement stmt = conn.prepareCall(sql)) {
			            stmt.setString(1, p_menu_code);
			            stmt.registerOutParameter(2, java.sql.Types.REF_CURSOR);
			            stmt.execute();

			            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
			                ResultSetMetaData metaData = rs.getMetaData();
			                int columnCount = metaData.getColumnCount();

			                while (rs.next()) {
			                    Map<String, Object> resultMap = new HashMap<>();

			                    for (int i = 1; i <= columnCount; i++) {
			                        String columnName = metaData.getColumnName(i);
			                        Object columnValue = rs.getObject(i);

			                        // Handle BLOB column (like SCREEN_ICON)
			                        if (columnValue instanceof Blob) {
			                            Blob blob = (Blob) columnValue;
			                            if (blob != null) {
			                                byte[] blobBytes = blob.getBytes(1, (int) blob.length());
			                                String base64 = Base64.getEncoder().encodeToString(blobBytes);
			                                resultMap.put(columnName, base64);
			                            } else {
			                                resultMap.put(columnName, null);
			                            }
			                        } else {
			                            resultMap.put(columnName, columnValue);
			                        }
			                    }

			                    resultList.add(resultMap);
			                }
			            }
			        }
			    } catch (SQLException  e) {
			        e.printStackTrace();
			    }

			    return resultList;
			}

	  
}
