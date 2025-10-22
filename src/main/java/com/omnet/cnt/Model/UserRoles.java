/*
Document   : User Roles Table
Author     : Jamal Abraar
last update: 01/04/2024
*/

package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Omnet_Roles")
public class UserRoles {
    @Id
    @Column(name = "role_seq_num")
    private int roleSeqNum;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "status")
    private String status;

	public int getRoleSeqNum() {
		return roleSeqNum;
	}

	public void setRoleSeqNum(int roleSeqNum) {
		this.roleSeqNum = roleSeqNum;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserRoles() {
		super();
	}

	public UserRoles(int roleSeqNum, String roleName, String status) {
		super();
		this.roleSeqNum = roleSeqNum;
		this.roleName = roleName;
		this.status = status;
	}
    
}
