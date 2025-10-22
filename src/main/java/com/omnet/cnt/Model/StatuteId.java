package com.omnet.cnt.Model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StatuteId implements Serializable {
	@Column(name="STATUTE_TITLE")
	private String STATUTE_TITLE;
	
	@Column(name="STATUTE_SECTION")
	private String STATUTE_SECTION;
	
	@Column(name="STATUTE_SUBSECTION")
	private String STATUTE_SUBSECTION;
	
	@Column(name="STATUTE_TYPE")
	private String STATUTE_TYPE;
	
	
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatuteId)) return false;
        StatuteId that = (StatuteId) o;
        return Objects.equals(STATUTE_TITLE, that.STATUTE_TITLE) &&
        		Objects.equals(STATUTE_SECTION, that.STATUTE_SECTION) &&
               Objects.equals(STATUTE_SUBSECTION, that.STATUTE_SUBSECTION) &&
               Objects.equals(STATUTE_TYPE, that.STATUTE_TYPE);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(STATUTE_TITLE, STATUTE_SECTION, STATUTE_SUBSECTION, STATUTE_TYPE);
    }
    
    public String getSTATUTE_TITLE() {
		return STATUTE_TITLE;
	}

	public void setSTATUTE_TITLE(String sTATUTE_TITLE) {
		STATUTE_TITLE = sTATUTE_TITLE;
	}
    
    public String getSTATUTE_SECTION() {
		return STATUTE_SECTION;
	}

	public void setSTATUTE_SECTION(String sTATUTE_SECTION) {
		STATUTE_SECTION = sTATUTE_SECTION;
	}

	public String getSTATUTE_SUBSECTION() {
		return STATUTE_SUBSECTION;
	}

	public void setSTATUTE_SUBSECTION(String sTATUTE_SUBSECTION) {
		STATUTE_SUBSECTION = sTATUTE_SUBSECTION;
	}

	public String getSTATUTE_TYPE() {
		return STATUTE_TYPE;
	}

	public void setSTATUTE_TYPE(String sTATUTE_TYPE) {
		STATUTE_TYPE = sTATUTE_TYPE;
	}

	
}
