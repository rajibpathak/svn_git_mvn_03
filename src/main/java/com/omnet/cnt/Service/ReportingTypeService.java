package com.omnet.cnt.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.ReportingType;
import com.omnet.cnt.Repository.ReportingTypeRepo;



@Service
public class ReportingTypeService {
	
	@Autowired
	private ReportingTypeRepo reportingTypeRepo;
	
	public List<ReportingType> typeDropDown()
	{
		List<Object[]> rawList = reportingTypeRepo.typeDropDown();
		
		return rawList.stream().map(obj ->
		{
			ReportingType rt = new ReportingType();
			
			rt.setEnemyTpCd((String) obj[0]);
			rt.setEnemyTpDesc((String) obj[1]);
			return rt;
		}).toList();
	}

}
