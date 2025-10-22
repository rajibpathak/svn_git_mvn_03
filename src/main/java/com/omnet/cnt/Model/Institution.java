package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INSTITUTION")
public class Institution {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "INST_NUM")
    private String instNum;

    @Column(name = "INST_NAME")
    private String instName;
    
    @Column(name = "SEC_CUST_HOU_CD")
    private String secHouCd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInstNum() {
		return instNum;
	}

	public void setInstNum(String instNum) {
		this.instNum = instNum;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getSecHouCd() {
		return secHouCd;
	}

	public void setSecHouCd(String secHouCd) {
		this.secHouCd = secHouCd;
	}

	public Institution(Long id, String instNum, String instName, String secHouCd) {
		super();
		this.id = id;
		this.instNum = instNum;
		this.instName = instName;
		this.secHouCd = secHouCd;
	}
}
