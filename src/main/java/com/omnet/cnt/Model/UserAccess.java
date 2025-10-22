/*
Document   : User Roles
Author     : Jamal Abraar
last update: 01/04/2024
*/

package com.omnet.cnt.Model;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Omnet_User_Roles")
public class UserAccess implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
    private UserAccessId id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Login login;

    @ManyToOne
    @JoinColumn(name = "role_seq_num", insertable = false, updatable = false)
    private UserRoles role;

    @Column(name = "role_effective_date")
    private Date effectiveDate;

    @Column(name = "role_expiry_date")
    private Date expiryDate;

	public UserAccessId getId() {
		return id;
	}

	public void setId(UserAccessId id) {
		this.id = id;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public UserRoles getRole() {
		return role;
	}

	public void setRole(UserRoles role) {
		this.role = role;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserAccess(UserAccessId id, Login login, UserRoles role, Date effectiveDate, Date expiryDate) {
		super();
		this.id = id;
		this.login = login;
		this.role = role;
		this.effectiveDate = effectiveDate;
		this.expiryDate = expiryDate;
	}

	public UserAccess() {
		super();
	}
}
