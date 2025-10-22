package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Omnet_sub_menu")
public class SubMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String Sub_menu_code;
	private String Sub_menu_name;
	private String Menu_code;
	private String Status;
	private String Category_Code;
	
	
	
	public SubMenu(String sub_menu_code, String sub_menu_name, String menu_code, String status, String category_Code) {
		super();
		Sub_menu_code = sub_menu_code;
		Sub_menu_name = sub_menu_name;
		Menu_code = menu_code;
		Status = status;
		Category_Code = category_Code;
	}
	
	public SubMenu() {
		
	}

	public String getSub_menu_code() {
		return Sub_menu_code;
	}

	public void setSub_menu_code(String sub_menu_code) {
		Sub_menu_code = sub_menu_code;
	}

	public String getSub_menu_name() {
		return Sub_menu_name;
	}

	public void setSub_menu_name(String sub_menu_name) {
		Sub_menu_name = sub_menu_name;
	}

	public String getMenu_code() {
		return Menu_code;
	}

	public void setMenu_code(String menu_code) {
		Menu_code = menu_code;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSubmenu_CategoryCode() {
		return Category_Code;
	}

	public void setCategory_Code(String category_Code) {
		Category_Code = category_Code;
	}


	
	
}
