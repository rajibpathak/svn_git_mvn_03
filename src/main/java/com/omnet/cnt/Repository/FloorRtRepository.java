package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.omnet.cnt.Model.InstFloorRt;

@Repository
public interface FloorRtRepository extends JpaRepository<InstFloorRt, String> {
	
	
	@Query(value = "SELECT  floor_desc , floor_num FROM inst_floor_rt " +
            "WHERE inst_num = :instNum " +
            "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
            "AND (unit_id = :unitId OR :unitId IS NULL) " +
            "AND status = 'A' ORDER BY floor_desc", 
     nativeQuery = true)
List<Object[]> findFloors(@Param("instNum") String instNum, 
                        @Param("bldNum") String buildNum, 
                        @Param("unitId") String unitId);
    		
@Query(value = "SELECT  floor_desc , floor_num FROM inst_floor_rt " +
        "WHERE inst_num = :instNum " +
        "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
        "AND (unit_id = :unitId OR :unitId IS NULL) " +
        " ORDER BY floor_desc", 
 nativeQuery = true)
List<Object[]> findFloorsHousingFacility(@Param("instNum") String instNum, @Param("bldNum") String buildNum, @Param("unitId") String unitId);

}
