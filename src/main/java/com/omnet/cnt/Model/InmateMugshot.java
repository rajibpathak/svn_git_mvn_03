package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class InmateMugshot {
	
	@Id
    @Column(name = "sbi_no")
    private String sbiNo;
    private String user; 

    @Lob
    private byte[] image;

}
