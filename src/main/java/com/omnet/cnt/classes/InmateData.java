package com.omnet.cnt.classes;

public class InmateData {

	
	private String commitNo;
    private String inmateRowId;
    private String selfRepStaffAssaultFlag;
    
    
    public InmateData() {
    }

    // Parameterized constructor
    public InmateData(String commitNo, String inmateRowId, String selfRepStaffAssaultFlag) {
        this.commitNo = commitNo;
        this.inmateRowId = inmateRowId;
        this.selfRepStaffAssaultFlag = selfRepStaffAssaultFlag;
    }

    @Override
    public String toString() {
        return "InmateData{" +
                "commitNo='" + commitNo + '\'' +
                ", inmateRowId='" + inmateRowId + '\'' +
                ", selfRepStaffAssaultFlag='" + selfRepStaffAssaultFlag + '\'' +
                '}';
    }
	public String getCommitNo() {
		return commitNo;
	}
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	public String getInmateRowId() {
		return inmateRowId;
	}
	public void setInmateRowId(String inmateRowId) {
		this.inmateRowId = inmateRowId;
	}
	public String getSelfRepStaffAssaultFlag() {
		return selfRepStaffAssaultFlag;
	}
	public void setSelfRepStaffAssaultFlag(String selfRepStaffAssaultFlag) {
		this.selfRepStaffAssaultFlag = selfRepStaffAssaultFlag;
	}
    
    
}
