package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omnet.cnt.Model.Gangrt;

public interface gangrepository extends JpaRepository<Gangrt, String> {
	
	
	
	@Query("SELECT g.gangCode, g.gangName, g.status FROM Gangrt g ORDER BY g.gangName")
	    List<Object[]> findGangDetails();

	   
	    
	    
}
