package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.TierRt;

public interface TierRepository extends JpaRepository<TierRt, String> {
    
    @Query(value = "SELECT tier_desc, tier_num FROM inst_tier_rt " +
                   "WHERE inst_num = :instNum " +
                   "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
                   "AND (unit_id = :unitId OR :unitId IS NULL) " +
                   "AND (floor_num = :floorNum OR :floorNum IS NULL) " +
                   "AND status = 'A' ORDER BY tier_desc", 
           nativeQuery = true)
    List<Object[]> findTiers(@Param("instNum") String instNum, 
                             @Param("bldNum") String bldNum, 
                             @Param("unitId") String unitId, 
                             @Param("floorNum") String floorNum);
    
    @Query(value = "SELECT tier_desc, tier_num FROM inst_tier_rt " +
            "WHERE inst_num = :instNum " +
            "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
            "AND (unit_id = :unitId OR :unitId IS NULL) " +
            "AND (floor_num = :floorNum OR :floorNum IS NULL) " +
            " ORDER BY tier_desc", 
    nativeQuery = true)
List<Object[]> findTiersHousingFacility(@Param("instNum") String instNum, @Param("bldNum") String bldNum, @Param("unitId") String unitId, 
		@Param("floorNum") String floorNum);
}
