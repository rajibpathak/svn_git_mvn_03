	/**
	 * Document: ActivityTypeRepository.java
	 * Author: Jamal Abraar
	 * Date Created: 20-Aug-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.ActivityType;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, String> {
	
    @Query("SELECT a.activityTypeCode, a.activityTypeDesc FROM ActivityType a WHERE a.status = :status ORDER BY a.activityTypeDesc ASC")
    List<Object[]> findActiveActivityTypes(@Param("status") String status);
    
	  
}
