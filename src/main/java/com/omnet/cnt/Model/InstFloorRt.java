package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INST_FLOOR_RT")
public class InstFloorRt {
	
	@Id
	
	
	 @Column(name = "floor_num")
	    private String floorNum;

	    @Column(name = "floor_desc")
	    private String floorDesc;

	    @Column(name = "floor_status")
	    private String floorStatus;

	    @Column(name = "status")
	    private String status;

	    @Column(name = "inst_num")
	    private String instNum;

	    @Column(name = "floor_sex")
	    private String floorSex;

	    @Column(name = "floor_census")
	    private String floorCensus;

	    @Column(name = "sec_cust_lvl_cd")
	    private String secCustLvlCd;

	    @Column(name = "program_id")
	    private String programId;

	    @Column(name = "rated_capacity")
	    private Integer ratedCapacity;

	    @Column(name = "hanicap_accessible_flag")
	    private Boolean hanicapAccessibleFlag;

	    @Column(name = "juvenile_flag")
	    private Boolean juvenileFlag;

	    @Column(name = "closed_custody_flag")
	    private Boolean closedCustodyFlag;

	    @Column(name = "floor_spcl_cond_desc")
	    private String floorSpclCondDesc;

	    @Column(name = "reason_offline_code")
	    private String reasonOfflineCode;

	    @Column(name = "bld_num")
	    private String bldNum;

	    @Column(name = "unit_id")
	    private String unitId;

	    @Column(name = "current_capacity")
	    private Integer currentCapacity;

	    @Column(name = "dorm_flag")
	    private Boolean dormFlag;

	    @Column(name = "inst_security_level")
	    private String instSecurityLevel;

	    @Column(name = "running_count")
	    private Integer runningCount;

	    @Column(name = "inserted_user_id")
	    private String insertedUserId;

}
