	/**
	 * Document: HousingDetails.java
	 * Author: Jamal Abraar
	 * Date Created: 16-Jul-2024
	 * Last Updated: 
	 */

package com.omnet.cnt.classes;

import java.sql.Timestamp;
import java.util.Map;

public class HousingDetails {
	
	private String commitNo;
	private String institutionName;
	private String buildingName;
	private String unitDescription;
	private String floorDescription;
	private String tierDescription;
	private String cellDescription;
	private String bedDescription;
	private String housingUnitComment;
	private String medicalProviderFlag;
	private String orientationCheckFlag;
	private String noctInstitutionFlag;
	private String familyIncarceratedFlag;
	private String otherRestrictions;
	private Timestamp currentInstDate;
	private String bookingComments;
	private String medicalNotes;
	private String bedno;
	private String bedType;
	private Timestamp stayStartDate;
	private String timeEnter;	
	private Timestamp stayEndDate;
	private String timeDepart;
	
	public static class ReEnemyDtls {
        private String sbiNo;
        private String enemyCommitNo;
        private String enemyNameLast;
        private String enemyNameFirst;
        private String cellDesc;
        private String claimDesc;
        
		public String getSbiNo() {
			return sbiNo;
		}
		
		public void setSbiNo(String sbiNo) {
			this.sbiNo = sbiNo;
		}
		
		public String getEnemyCommitNo() {
			return enemyCommitNo;
		}
		
		public void setEnemyCommitNo(String enemyCommitNo) {
			this.enemyCommitNo = enemyCommitNo;
		}
		
		public String getEnemyNameLast() {
			return enemyNameLast;
		}
		
		public void setEnemyNameLast(String enemyNameLast) {
			this.enemyNameLast = enemyNameLast;
		}
		
		public String getEnemyNameFirst() {
			return enemyNameFirst;
		}
		
		public void setEnemyNameFirst(String enemyNameFirst) {
			this.enemyNameFirst = enemyNameFirst;
		}
		
		public String getCellDesc() {
			return cellDesc;
		}
		
		public void setCellDesc(String cellDesc) {
			this.cellDesc = cellDesc;
		}
		
		public String getClaimDesc() {
			return claimDesc;
		}
		
		public void setClaimDesc(String claimDesc) {
			this.claimDesc = claimDesc;
		}  
	}
	
	public String getCommitNo() {
		return commitNo;
	}
	
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getUnitDescription() {
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}

	public String getFloorDescription() {
		return floorDescription;
	}

	public void setFloorDescription(String floorDescription) {
		this.floorDescription = floorDescription;
	}

	public String getTierDescription() {
		return tierDescription;
	}

	public void setTierDescription(String tierDescription) {
		this.tierDescription = tierDescription;
	}

	public String getCellDescription() {
		return cellDescription;
	}

	public void setCellDescription(String cellDescription) {
		this.cellDescription = cellDescription;
	}

	public String getBedDescription() {
		return bedDescription;
	}

	public void setBedDescription(String bedDescription) {
		this.bedDescription = bedDescription;
	}

	public String getHousingUnitComment() {
		return housingUnitComment;
	}

	public void setHousingUnitComment(String housingUnitComment) {
		this.housingUnitComment = housingUnitComment;
	}

	public String getMedicalProviderFlag() {
		return medicalProviderFlag;
	}

	public void setMedicalProviderFlag(String medicalProviderFlag) {
		this.medicalProviderFlag = medicalProviderFlag;
	}

	public String getOrientationCheckFlag() {
		return orientationCheckFlag;
	}

	public void setOrientationCheckFlag(String orientationCheckFlag) {
		this.orientationCheckFlag = orientationCheckFlag;
	}

	public String getNoctInstitutionFlag() {
		return noctInstitutionFlag;
	}

	public void setNoctInstitutionFlag(String noctInstitutionFlag) {
		this.noctInstitutionFlag = noctInstitutionFlag;
	}

	public String getFamilyIncarceratedFlag() {
		return familyIncarceratedFlag;
	}

	public void setFamilyIncarceratedFlag(String familyIncarceratedFlag) {
		this.familyIncarceratedFlag = familyIncarceratedFlag;
	}

	public String getOtherRestrictions() {
		return otherRestrictions;
	}

	public void setOtherRestrictions(String otherRestrictions) {
		this.otherRestrictions = otherRestrictions;
	}

	public Timestamp getCurrentInstDate() {
		return currentInstDate;
	}

	public void setCurrentInstDate(Timestamp currentInstDate) {
		this.currentInstDate = currentInstDate;
	}

	public String getBookingComments() {
		return bookingComments;
	}

	public void setBookingComments(String bookingComments) {
		this.bookingComments = bookingComments;
	}

	public String getMedicalNotes() {
		return medicalNotes;
	}

	public void setMedicalNotes(String medicalNotes) {
		this.medicalNotes = medicalNotes;
	}
	
	public String getBedno() {
		return bedno;
	}

	public void setBedno(String bedno) {
		this.bedno = bedno;
	}

	public String getBedType() {
		return bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}

	public Timestamp getStayStartDate() {
		return stayStartDate;
	}

	public void setStayStartDate(Timestamp stayStartDate) {
		this.stayStartDate = stayStartDate;
	}

	public String getTimeEnter() {
		return timeEnter;
	}

	public void setTimeEnter(String timeEnter) {
		this.timeEnter = timeEnter;
	}

	public Timestamp getStayEndDate() {
		return stayEndDate;
	}

	public void setStayEndDate(Timestamp stayEndDate) {
		this.stayEndDate = stayEndDate;
	}

	public String getTimeDepart() {
		return timeDepart;
	}

	public void setTimeDepart(String timeDepart) {
		this.timeDepart = timeDepart;
	}

	public static HousingDetails housingDetailsData(Map<String, Object> row) {
		HousingDetails housingDetailsData = new HousingDetails();
		housingDetailsData.setCommitNo((String) row.get("COMMIT_NO"));
		housingDetailsData.setInstitutionName((String) row.get("INST_NAME"));
		housingDetailsData.setBuildingName((String) row.get("BLD_NAME"));
		housingDetailsData.setUnitDescription((String) row.get("UNIT_DESC"));
		housingDetailsData.setFloorDescription((String) row.get("FLOOR_DESC"));
		housingDetailsData.setTierDescription((String) row.get("TIER_DESC"));
		housingDetailsData.setCellDescription((String) row.get("CELL_DESC"));
		housingDetailsData.setBedDescription((String) row.get("BED_DESC"));
		housingDetailsData.setHousingUnitComment((String) row.get("HOUSING_UNIT_COMMENT"));
		housingDetailsData.setMedicalProviderFlag((String ) row.get("CB_MEDI_PROV"));
		housingDetailsData.setNoctInstitutionFlag((String) row.get("CB_NOCT_INST"));
		housingDetailsData.setOrientationCheckFlag((String) row.get("CB_ORNT_FLG"));
		housingDetailsData.setFamilyIncarceratedFlag((String ) row.get("CB_FMLY_INCARCERATE"));
		housingDetailsData.setOtherRestrictions((String) row.get("OTHER_RESTRICTIONS"));
		housingDetailsData.setCurrentInstDate((Timestamp) row.get(":B4"));
		housingDetailsData.setBookingComments((String) row.get("BOOKING_COMMENTS"));
		housingDetailsData.setMedicalNotes((String) row.get("MEDICAL_NOTES"));
        return housingDetailsData;
    }
	
	public static HousingDetails housingDetailsHistory(Map<String, Object> row) {
		HousingDetails housingDetailsHistory = new HousingDetails();
		housingDetailsHistory.setCommitNo((String) row.get("COMMIT_NO"));
		housingDetailsHistory.setInstitutionName((String) row.get("INST_NAME"));
		housingDetailsHistory.setBedno((String) row.get("bed_no"));
		housingDetailsHistory.setBedDescription((String) row.get("BED_DESC"));
		housingDetailsHistory.setBedType((String) row.get("BED_TYPE"));
		housingDetailsHistory.setStayStartDate((Timestamp) row.get("STAY_START_DATE"));
		housingDetailsHistory.setTimeEnter((String) row.get("TIME_ENTER"));
		housingDetailsHistory.setStayEndDate((Timestamp) row.get("STAY_END_DATE"));
		housingDetailsHistory.setTimeDepart((String) row.get("TIME_DEPART"));
		housingDetailsHistory.setHousingUnitComment((String) row.get("HOUSING_UNIT_COMMENT"));
        return housingDetailsHistory;
    }
		
}
