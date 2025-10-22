/*
Document   : Contact Master Table
Author     : Jamal Abraar
last update: 03/06/2024
*/

package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "CONTACT_TP_MST")
@Immutable
public class ContactTpMst {

    @Id
	@Column(name = "Cont_Tp_Cd")
    private String contactCode;

    @Column(name = "Cont_Tp_Desc")
    private String contactDesc;

    @Column(name = "Status")
    private String status;

    public String getContactCode() {
        return contactCode;
    }

    public String getContactDesc() {
        return contactDesc;
    }

    public String getStatus() {
        return status;
    }

    public ContactTpMst() {
        super();
    }

    public ContactTpMst(String contactCode, String contactDesc, String status) {
        super();
        this.contactCode = contactCode;
        this.contactDesc = contactDesc;
        this.status = status;
    }
}