package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vitalcheckbox {

	@Id
	private String No_Contact;
	private String Mental_Health;
	private String Legal_Issues;
	
	public String getNo_Contact() {
		return No_Contact;
	}
	public void setNo_Contact(String no_Contact) {
		No_Contact = no_Contact;
	}
	public String getMental_Health() {
		return Mental_Health;
	}
	public void setMental_Health(String mental_Health) {
		Mental_Health = mental_Health;
	}
	public String getLegal_Issues() {
		return Legal_Issues;
	}
	public void setLegal_Issues(String legal_Issues) {
		Legal_Issues = legal_Issues;
	}
	
	
}
