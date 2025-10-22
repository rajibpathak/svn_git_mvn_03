package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.InstLocation;

@Repository
public interface InstLocationRepository extends JpaRepository<InstLocation, String> {
public static record LocationParamsRecord(String bldNum, String unitId, String floorNum, String tierNum, String cellNo, String bedNo) {}
	
	@Query("Select c.countLocCode,c.countLocDesc,c.locationType from InstLocation c Where c.instNum = :instNum And c.countLocStatus = 'A' Order By c.countLocDesc ASC")
	List<Object[]> findActiveLocationVals(@Param("instNum") String instNum);
	
	@Query("Select c.countLocCode,c.countLocDesc,c.locationType from InstLocation c Where c.instNum = :instNum And c.countLocStatus = 'A' Order By c.countLocDesc ASC")
	List<Object[]> findLocationVals(@Param("instNum") String instNum);
	
	@Query("SELECT i.bldNum, i.unitId, i.floorNum, i.tierNum, i.cellNo, i.bedNo " +
	           "FROM InstLocation i " +
	           //"WHERE i.instNum = :instNum AND i.countLocCode = :countLocCode")
			"WHERE i.instNum = :instNum")
	    //List<Object[]> findLocParams(@Param("instNum") String instNum, @Param("countLocCode") String countLocCode);
	List<Object[]> findLocParams(@Param("instNum") String instNum);
	
	@Query("SELECT i.bldNum, i.unitId, i.floorNum, i.tierNum, i.cellNo, i.bedNo " +
	           "FROM InstLocation i " +
	           "WHERE i.instNum = :instNum AND i.countLocCode = :countLocCode")
	List<Object[]> findLocParamByLoc(@Param("instNum") String instNum, @Param("countLocCode") String countLocCode);
	
	
	

}
