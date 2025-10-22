package com.omnet.cnt.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omnet.cnt.Model.ReportingSource;

import com.omnet.cnt.Repository.ReportingSourceRepository;

@Service
public class ReportingSourceSerivce {
	
	
	@Autowired
	private ReportingSourceRepository reportingSourceRepo;
	
	public List<ReportingSource> sourceDropDown() 
	{
	    List<Object[]> rawList = reportingSourceRepo.reportingDropDown();
	    
	    return rawList.stream().map(obj -> 
	    {
	        ReportingSource rs = new ReportingSource();
	        
	        rs.setReportingSourceCode((String) obj[0]);
	        
	        rs.setReportingDdesc((String) obj[1]);
	        
	        return rs;
	    }).toList();
	}
	
}
