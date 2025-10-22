package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Entity
@Table(name="law_file")
public class Statute {
	
	@EmbeddedId
	@JsonUnwrapped
    private StatuteId id;
	
	@Column(name="STATUTE_JURIS")
	private String STATUTE_JURIS;
	
	/*@Column(name="STATUTE_TITLE")
	private String STATUTE_TITLE;*/
	
	@Column(name="STATUTE_CLASS")
	private String STATUTE_CLASS;
	
	@Column(name="STATUTE_DESC")
	private String STATUTE_DESC;
	
	@Column(name = "KEYWORD01") private String keyword1;
    @Column(name = "KEYWORD02") private String keyword2;
    @Column(name = "KEYWORD03") private String keyword3;
    @Column(name = "KEYWORD04") private String keyword4;
    @Column(name = "KEYWORD05") private String keyword5;
    @Column(name = "KEYWORD06") private String keyword6;
    @Column(name = "KEYWORD07") private String keyword7;
    @Column(name = "KEYWORD08") private String keyword8;

	public String getSTATUTE_JURIS() {
		return STATUTE_JURIS;
	}

	public void setSTATUTE_JURIS(String sTATUTE_JURIS) {
		STATUTE_JURIS = sTATUTE_JURIS;
	}

	/*public String getSTATUTE_TITLE() {
		return STATUTE_TITLE;
	}

	public void setSTATUTE_TITLE(String sTATUTE_TITLE) {
		STATUTE_TITLE = sTATUTE_TITLE;
	}*/
	
	public String getSTATUTE_CLASS() {
		return STATUTE_CLASS;
	}

	public void setSTATUTE_CLASS(String sTATUTE_CLASS) {
		STATUTE_CLASS = sTATUTE_CLASS;
	}

	public String getSTATUTE_DESC() {
		return STATUTE_DESC;
	}

	public void setSTATUTE_DESC(String sTATUTE_DESC) {
		STATUTE_DESC = sTATUTE_DESC;
	}

	public String getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}

	public String getKeyword4() {
		return keyword4;
	}

	public void setKeyword4(String keyword4) {
		this.keyword4 = keyword4;
	}

	public String getKeyword5() {
		return keyword5;
	}

	public void setKeyword5(String keyword5) {
		this.keyword5 = keyword5;
	}

	public String getKeyword6() {
		return keyword6;
	}

	public void setKeyword6(String keyword6) {
		this.keyword6 = keyword6;
	}

	public String getKeyword7() {
		return keyword7;
	}

	public void setKeyword7(String keyword7) {
		this.keyword7 = keyword7;
	}

	public String getKeyword8() {
		return keyword8;
	}

	public void setKeyword8(String keyword8) {
		this.keyword8 = keyword8;
	}
	
	// Convenience getters for JSON serialization with camelCase names
	public String getStatuteJurisdiction() {
		return STATUTE_JURIS;
	}
	
	public String getStatuteTitle() {
		return id != null ? id.getSTATUTE_TITLE() : null;
	}
	
	public String getStatuteSection() {
		return id != null ? id.getSTATUTE_SECTION() : null;
	}
	
	public String getStatuteSubsection() {
		return id != null ? id.getSTATUTE_SUBSECTION() : null;
	}
	
	public String getStatuteType() {
		return id != null ? id.getSTATUTE_TYPE() : null;
	}
	
	public String getStatuteClass() {
		return STATUTE_CLASS;
	}
	
	public String getStatuteDesc() {
		return STATUTE_DESC;
	}

	public StatuteId getId() {
		return id;
	}

	public void setId(StatuteId id) {
		this.id = id;
	}
}
