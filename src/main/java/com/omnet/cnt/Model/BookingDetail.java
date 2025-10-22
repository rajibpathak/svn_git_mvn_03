package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="INMATE_SCREEN")
public class BookingDetail {

	 @Id
	    @Column(name = "commit_no")
	    private String commitNo;  

	    @Column(name = "screen_code")
	    private String screenCode;

	    @Column(name = "screen_name")
	    private String screenName;

	    @Column(name = "nvl(inm.updated_flag,'N')")
	    private String updatedFlag;

		public String getCommitNo() {
			return commitNo;
		}

		public void setCommitNo(String commitNo) {
			this.commitNo = commitNo;
		}

		public String getScreenCode() {
			return screenCode;
		}

		public void setScreenCode(String screenCode) {
			this.screenCode = screenCode;
		}

		public String getScreenName() {
			return screenName;
		}

		public void setScreenName(String screenName) {
			this.screenName = screenName;
		}

		public String getUpdatedFlag() {
			return updatedFlag;
		}

		public void setUpdatedFlag(String updatedFlag) {
			this.updatedFlag = updatedFlag;
		}

		public BookingDetail(String commitNo, String screenCode, String screenName, String updatedFlag) {
			super();
			this.commitNo = commitNo;
			this.screenCode = screenCode;
			this.screenName = screenName;
			this.updatedFlag = updatedFlag;
		}

		public BookingDetail() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
}
