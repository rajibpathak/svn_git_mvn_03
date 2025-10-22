package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "cm_reference_values")
public class ReferenceValue {
	@Id
	
	 @Column(name = "ref_value_desc")
    private String refValueDesc;

    @Column(name = "ref_value_code")
    private String refValueCode;

    @Column(name = "ref_category_module")
    private String refCategoryModule;

    @Column(name = "ref_category_code")
    private String refCategoryCode;

	public String getRefValueDesc() {
		return refValueDesc;
	}

	public void setRefValueDesc(String refValueDesc) {
		this.refValueDesc = refValueDesc;
	}

	public String getRefValueCode() {
		return refValueCode;
	}

	public void setRefValueCode(String refValueCode) {
		this.refValueCode = refValueCode;
	}

	public String getRefCategoryModule() {
		return refCategoryModule;
	}

	public void setRefCategoryModule(String refCategoryModule) {
		this.refCategoryModule = refCategoryModule;
	}

	public String getRefCategoryCode() {
		return refCategoryCode;
	}

	public void setRefCategoryCode(String refCategoryCode) {
		this.refCategoryCode = refCategoryCode;
	}

	/*
	 * public ReferenceValue(String refValueDesc, String refValueCode, String
	 * refCategoryModule, String refCategoryCode) { super(); this.refValueDesc =
	 * refValueDesc; this.refValueCode = refValueCode; this.refCategoryModule =
	 * refCategoryModule; this.refCategoryCode = refCategoryCode; }
	 */

	// Default constructor (required by JPA)
    public ReferenceValue() {
    }

    // Constructor with parameters for refValueDesc and refValueCode
    public ReferenceValue(String refValueDesc, String refValueCode) {
        this.refValueDesc = refValueDesc;
        this.refValueCode = refValueCode;
    }
    
    

}
