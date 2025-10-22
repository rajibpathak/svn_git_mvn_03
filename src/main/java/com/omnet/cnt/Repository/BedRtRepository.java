package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.InstBedRt;

public interface BedRtRepository extends JpaRepository<InstBedRt, String> {
	@Query(value = "SELECT bed_desc, bed_no FROM inst_bed_rt " +
			"WHERE inst_num = :instNum " +
	        "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
	        "AND (unit_id = :unitId OR :unitId IS NULL) " +
	        "AND (floor_num = :floorNum OR :floorNum IS NULL) " +
	        "AND (tier_num = :tierNum OR :tierNum IS NULL) " +
            "AND (cell_no = :cellNum OR :cellNum IS NULL)" +
            "AND status = 'A' ORDER BY bed_desc", 
    nativeQuery = true)
	List<Object[]> findBeds(@Param("instNum") String instNum, 
                       @Param("bldNum") String buildNum, 
                       @Param("unitId") String unitId, 
                       @Param("floorNum") String floorNum, 
                       @Param("tierNum") String tierNum,
					   @Param("cellNum") String cellNum);
	
    @Query("SELECT i.bedSex FROM InstBedRt i WHERE i.bedNum = :bedNum")
    String findBedSexByBedNum(String bedNum);
	
}
