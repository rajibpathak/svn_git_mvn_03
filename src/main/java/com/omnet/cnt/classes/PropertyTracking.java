package com.omnet.cnt.classes;

import java.util.List;

public class PropertyTracking {
	
	private List<ColtItems> collectedItems;

	public static class ColtItems {
		private String propItmRowid;
	    private Integer pthPropTrkSeqNo; // Assuming PROP_TRK_INMT_ITM_DTL.PTH_PROP_TRK_SEQ_NO%TYPE maps to String
	    private String itmItemCd; // Assuming PROP_TRK_INMT_ITM_DTL.ITEM_CD%TYPE maps to String
	    private String itemCd; // Assuming ITEM_MST.ITEM_CD%TYPE maps to String
	    private String itemDesc; // Assuming ITEM_MST.ITEM_DESC%TYPE maps to String
	    private Integer propInmtItmSeqNo; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_ITM_SEQ_NO%TYPE maps to String
	    private Integer propInmtItmQty; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_ITM_QTY%TYPE maps to String
	    private String propInmtItmDesc; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_ITM_DESC%TYPE maps to String
	    private Integer propInmtItmRetnQty; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_ITM_RETN_QTY%TYPE maps to String
	    private String propInmtRetnFstNm; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_RETN_FST_NM%TYPE maps to String
	    private String propInmtRetnLstNm; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_RETN_LST_NM%TYPE maps to String
	    private String propInmtRetnMidNm; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_RETN_MID_NM%TYPE maps to String
	    private String propInmtRetnSfx; // Assuming PROP_TRK_INMT_ITM_DTL.PROP_INMT_RETN_SFX%TYPE maps to String
	    private String relationshipCode; // Assuming PROP_TRK_INMT_ITM_DTL.RELATIONSHIP_CODE%TYPE maps to String

	    // Getters and Setters

	    public String getPropItmRowid() {
	        return propItmRowid;
	    }

	    public void setPropItmRowid(String propItmRowid) {
	        this.propItmRowid = propItmRowid;
	    }

	    public Integer getPthPropTrkSeqNo() {
	        return pthPropTrkSeqNo;
	    }

	    public void setPthPropTrkSeqNo(Integer pthPropTrkSeqNo) {
	        this.pthPropTrkSeqNo = pthPropTrkSeqNo;
	    }

	    public String getItmItemCd() {
	        return itmItemCd;
	    }

	    public void setItmItemCd(String itmItemCd) {
	        this.itmItemCd = itmItemCd;
	    }

	    public String getItemCd() {
	        return itemCd;
	    }

	    public void setItemCd(String itemCd) {
	        this.itemCd = itemCd;
	    }

	    public String getItemDesc() {
	        return itemDesc;
	    }

	    public void setItemDesc(String itemDesc) {
	        this.itemDesc = itemDesc;
	    }

	    public Integer getPropInmtItmSeqNo() {
	        return propInmtItmSeqNo;
	    }

	    public void setPropInmtItmSeqNo(Integer propInmtItmSeqNo) {
	        this.propInmtItmSeqNo = propInmtItmSeqNo;
	    }

	    public Integer getPropInmtItmQty() {
	        return propInmtItmQty;
	    }

	    public void setPropInmtItmQty(Integer propInmtItmQty) {
	        this.propInmtItmQty = propInmtItmQty;
	    }

	    public String getPropInmtItmDesc() {
	        return propInmtItmDesc;
	    }

	    public void setPropInmtItmDesc(String propInmtItmDesc) {
	        this.propInmtItmDesc = propInmtItmDesc;
	    }

	    public Integer getPropInmtItmRetnQty() {
	        return propInmtItmRetnQty;
	    }

	    public void setPropInmtItmRetnQty(Integer propInmtItmRetnQty) {
	        this.propInmtItmRetnQty = propInmtItmRetnQty;
	    }

	    public String getPropInmtRetnFstNm() {
	        return propInmtRetnFstNm;
	    }

	    public void setPropInmtRetnFstNm(String propInmtRetnFstNm) {
	        this.propInmtRetnFstNm = propInmtRetnFstNm;
	    }

	    public String getPropInmtRetnLstNm() {
	        return propInmtRetnLstNm;
	    }

	    public void setPropInmtRetnLstNm(String propInmtRetnLstNm) {
	        this.propInmtRetnLstNm = propInmtRetnLstNm;
	    }

	    public String getPropInmtRetnMidNm() {
	        return propInmtRetnMidNm;
	    }

	    public void setPropInmtRetnMidNm(String propInmtRetnMidNm) {
	        this.propInmtRetnMidNm = propInmtRetnMidNm;
	    }

	    public String getPropInmtRetnSfx() {
	        return propInmtRetnSfx;
	    }

	    public void setPropInmtRetnSfx(String propInmtRetnSfx) {
	        this.propInmtRetnSfx = propInmtRetnSfx;
	    }

	    public String getRelationshipCode() {
	        return relationshipCode;
	    }

	    public void setRelationshipCode(String relationshipCode) {
	        this.relationshipCode = relationshipCode;
	    }
	}
	
	public List<ColtItems> getCollectedItms() {
		return collectedItems;
	}

	public void setCollectedItms(List<ColtItems> collectedItems) {
		this.collectedItems = collectedItems;
	}
}
