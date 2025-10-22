package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NOTISearchTotalCount {
	
	@Id
	private Long TOTAL_NO_CATEGORY;

	public Long getTOTAL_NO_CATEGORY() {
		return TOTAL_NO_CATEGORY;
	}

	public void setTOTAL_NO_CATEGORY(Long tOTAL_NO_CATEGORY) {
		TOTAL_NO_CATEGORY = tOTAL_NO_CATEGORY;
	}
	
	

}
