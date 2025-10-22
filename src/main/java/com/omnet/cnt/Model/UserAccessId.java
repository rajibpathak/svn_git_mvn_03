/*
Document   : User Roles
Author     : Jamal Abraar
last update: 01/04/2024
*/

package com.omnet.cnt.Model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserAccessId implements Serializable{
	private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_seq_num")
    private int roleSeqNum;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRoleSeqNum() {
		return roleSeqNum;
	}

	public void setRoleSeqNum(int roleSeqNum) {
		this.roleSeqNum = roleSeqNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserAccessId(String userId, int roleSeqNum) {
		super();
		this.userId = userId;
		this.roleSeqNum = roleSeqNum;
	}

	public UserAccessId() {
		super();
	}
    
}
