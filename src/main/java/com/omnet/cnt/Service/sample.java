package com.omnet.cnt.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class sample {

	public static void main(String args[]) {
		
		System.out.println("ok");
		System.out.println(setUniqueID());
		
	}
	
	public static String  setUniqueID(){
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyddmm");
	    Date date = new Date(0);
	    String dt=String.valueOf(dateFormat.format(date));
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat time = new SimpleDateFormat("HHmm");
	    String tm= String.valueOf(time.format(new Date(0)));//time in 24 hour format
	    String id= dt+tm;
	    System.out.println(id);
	    return id;   
	}
	
}
