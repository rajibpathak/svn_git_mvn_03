package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="enemy_tp_desc")
public class ReportingType {
	
	@Id
	private String enemyTpCd;
	private String enemyTpDesc;
	public String getEnemyTpCd() {
		return enemyTpCd;
	}
	public void setEnemyTpCd(String enemyTpCd) {
		this.enemyTpCd = enemyTpCd;
	}
	public String getEnemyTpDesc() {
		return enemyTpDesc;
	}
	public void setEnemyTpDesc(String enemyTpDesc) {
		this.enemyTpDesc = enemyTpDesc;
	}
	
	
	
}
