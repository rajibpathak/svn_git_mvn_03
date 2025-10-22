package com.omnet.cnt.Model;

public class Rowdata {
	private String IgaRowid;
	private String gangName;
	 private String commitNo;
	    private String gangCode;
	    private String gangPosition;
	    private String gangLeader;
	    private String gangAiInd;
	    private String stgSefRepdStatus;
	    private String gangComments;
	    private String gangDelFlg;
	    
	    
		public String getIgaRowid() {
			return IgaRowid;
		}
		public void setIgaRowid(String igaRowid) {
			IgaRowid = igaRowid;
		}
		public String getGangName() {
			return gangName;
		}
		public void setGangName(String gangName) {
			this.gangName = gangName;
		}
		public String getCommitNo() {
			return commitNo;
		}
		public void setCommitNo(String commitNo) {
			this.commitNo = commitNo;
		}
		public String getGangCode() {
			return gangCode;
		}
		public void setGangCode(String gangCode) {
			this.gangCode = gangCode;
		}
		public String getGangPosition() {
			return gangPosition;
		}
		public void setGangPosition(String gangPosition) {
			this.gangPosition = gangPosition;
		}
		public String getGangLeader() {
			return gangLeader;
		}
		public void setGangLeader(String gangLeader) {
			this.gangLeader = gangLeader;
		}
		public String getGangAiInd() {
			return gangAiInd;
		}
		public void setGangAiInd(String gangAiInd) {
			this.gangAiInd = gangAiInd;
		}
		public String getStgSefRepdStatus() {
			return stgSefRepdStatus;
		}
		public void setStgSefRepdStatus(String stgSefRepdStatus) {
			this.stgSefRepdStatus = stgSefRepdStatus;
		}
		public String getGangComments() {
			return gangComments;
		}
		public void setGangComments(String gangComments) {
			this.gangComments = gangComments;
		}
		public String getGangDelFlg() {
			return gangDelFlg;
		}
		public void setGangDelFlg(String gangDelFlg) {
			this.gangDelFlg = gangDelFlg;
		}
		 @Override
		    public String toString() {
		        return "Rowdata [commitNo=" + commitNo + ", gangCode=" + gangCode + 
		               ", gangPosition=" + gangPosition + ", gangLeader=" + gangLeader + 
		               ", gangAiInd=" + gangAiInd + ", stgSefRepdStatus=" + stgSefRepdStatus + 
		               ", gangComments=" + gangComments + ", gangDelFlg=" + gangDelFlg + "]";
		    }

  
}
