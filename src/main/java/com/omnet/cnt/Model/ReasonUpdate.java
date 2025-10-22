package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "INM_RH_RSN_STATUS_STAGE")
public class ReasonUpdate {		
	@Id
	 private String cbSelect;
	 private String rhReasonCode;

		public ReasonUpdate() {
			super();
			// TODO Auto-generated constructor stub
		}
		public ReasonUpdate(String cbSelect, String rhReasonCode) {
			super();
			this.cbSelect = cbSelect;
			this.rhReasonCode = rhReasonCode;
		}
		public String getCbSelect() {
			return cbSelect;
		}
		public void setCbSelect(String cbSelect) {
			this.cbSelect = cbSelect;
		}
		public String getRhReasonCode() {
			return rhReasonCode;
		}
		public void setRhReasonCode(String rhReasonCode) {
			this.rhReasonCode = rhReasonCode;
		}
	    

}
