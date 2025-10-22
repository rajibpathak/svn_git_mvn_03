/*
Document   : Screen Setup 
Author     : Jamal Abraar
last update: 11/01/2024
*/

package com.omnet.cnt.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Screens;

@Repository
public interface ScreensRepository extends JpaRepository<Screens, String> {
	List<Screens> findAllByOrderByScreenCodeAsc();
	
	@Query("SELECT s FROM Screens s WHERE s.screenCode = :screenCode")
    Optional<Screens> findByScreenCode(@Param("screenCode") String screenCode);
	
	 @Query("SELECT o.screenUrl FROM Screens o WHERE o.screenCode = :screenCode")
	    String findScreenUrlByScreenCode(@Param("screenCode") String screenCode);
	
	@Modifying
	@Query("UPDATE Screens s " +
	       "SET s.screenCode = :#{#screens.screenCode}, " +
	       "    s.screenName = :#{#screens.screenName}, " +
	       "    s.screenUrl = :#{#screens.screenUrl}, " +
	       "    s.status = :#{#screens.status}, " +
	       "    s.updatedDateTime = :#{#screens.updatedDateTime}, " +
	       "    s.updatedUserId = :#{#screens.updatedUserId} " +
	       "WHERE s.screenCode = :updid")
	void updateScreen(@Param("screens") Screens screens, @Param("updid") String updid);
}
