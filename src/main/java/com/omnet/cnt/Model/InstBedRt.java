	/**
	 * Document: InstBedRt.java
	 * Author: Jamal Abraar
	 * Date Created: 31-Jul-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INST_BED_RT")
public class InstBedRt {
	
	@Id	
	   @Column(name = "BED_NO")
	    private String bedNum;

	    @Column(name = "BED_DESC")
	    private String bedDesc;

	    @Column(name = "STATUS")
	    private String status;

	    @Column(name = "INST_NUM")
	    private String instNum;
	    
	    @Column(name = "BLD_NUM")
	    private String bldNum;
	    
	    @Column(name = "UNIT_ID")
	    private String unitId;
	    
	    @Column(name = "FLOOR_NUM")
	    private String floorNum;
	    
	    @Column(name = "TIER_NUM")
	    private String tierNum;
	    
	    @Column(name = "CELL_NO")
	    private String cellNum;
      
	    @Column(name = "BED_SEX")
	    private String bedSex;

	    public String getBedSex() {
	        return bedSex;
	    }

	    public void setBedSex(String bedSex) {
	        this.bedSex = bedSex;
	    }
		public String getBedNum() {
			return bedNum;
		}

		public void setBedNum(String bedNum) {
			this.bedNum = bedNum;
		}

		public String getBedDesc() {
			return bedDesc;
		}

		public void setBedDesc(String bedDesc) {
			this.bedDesc = bedDesc;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getInstNum() {
			return instNum;
		}

		public void setInstNum(String instNum) {
			this.instNum = instNum;
		}

		public String getBldNum() {
			return bldNum;
		}

		public void setBldNum(String bldNum) {
			this.bldNum = bldNum;
		}

		public String getUnitId() {
			return unitId;
		}

		public void setUnitId(String unitId) {
			this.unitId = unitId;
		}

		public String getFloorNum() {
			return floorNum;
		}

		public void setFloorNum(String floorNum) {
			this.floorNum = floorNum;
		}

		public String getTierNum() {
			return tierNum;
		}

		public void setTierNum(String tierNum) {
			this.tierNum = tierNum;
		}

		public String getCellNum() {
			return cellNum;
		}

		public void setCellNum(String cellNum) {
			this.cellNum = cellNum;
		}

		public InstBedRt(String bedNum, String bedDesc, String status, String instNum, String bldNum, String unitId,
				String floorNum, String tierNum, String cellNum) {
			super();
			this.bedNum = bedNum;
			this.bedDesc = bedDesc;
			this.status = status;
			this.instNum = instNum;
			this.bldNum = bldNum;
			this.unitId = unitId;
			this.floorNum = floorNum;
			this.tierNum = tierNum;
			this.cellNum = cellNum;
		}

		public InstBedRt() {
			super();
		}	    
}

   

