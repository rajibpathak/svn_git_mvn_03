	/**
	 * Document: Relationship.java
	 * Author: Jamal Abraar
	 * Date Created: 11-Jun-2024
	 * Last Updated: 
	 */

package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "RELATIONSHIP_RT")
public class Relationship {
	
	@Id
	@Column(name = "RELATIONSHIP_CODE")
	private String relationshipCode;
	
	@Column(name = "RELATIONSHIP_DESC")
	private String relationshipDesc;
	
	@Column(name = "RELATIONSHIP_TYPE")
	private String relationshipType;
	
	@Column(name = "STATUS")
	private String status;

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}

	public String getRelationshipDesc() {
		return relationshipDesc;
	}

	public void setRelationshipDesc(String relationshipDesc) {
		this.relationshipDesc = relationshipDesc;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Relationship(String relationshipCode, String relationshipDesc, String relationshipType, String status) {
		super();
		this.relationshipCode = relationshipCode;
		this.relationshipDesc = relationshipDesc;
		this.relationshipType = relationshipType;
		this.status = status;
	}

	public Relationship() {
		super();
	}
	
	
}
