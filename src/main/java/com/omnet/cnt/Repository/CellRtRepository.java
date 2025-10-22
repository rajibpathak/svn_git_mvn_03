package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.CellRt;

public interface CellRtRepository extends JpaRepository<CellRt, String> {

    @Query(value = "SELECT cell_desc, cell_no FROM inst_cell_rt " +
            "WHERE inst_num = :instNum " +
            "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
            "AND (unit_id = :unitId OR :unitId IS NULL) " +
            "AND (floor_num = :floorNum OR :floorNum IS NULL) " +
            "AND (tier_num = :tierNum OR :tierNum IS NULL) " +
            "AND status = 'A' ORDER BY cell_desc", 
    nativeQuery = true)
    List<Object[]> findCells(@Param("instNum") String instNum, 
                             @Param("bldNum") String bldNum, 
                             @Param("unitId") String unitId, 
                             @Param("floorNum") String floorNum, 
                             @Param("tierNum") String tierNum);
    
    @Query(value = "SELECT cell_desc, cell_no FROM inst_cell_rt " +
            "WHERE inst_num = :instNum " +
            "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
            "AND (unit_id = :unitId OR :unitId IS NULL) " +
            "AND (floor_num = :floorNum OR :floorNum IS NULL) " +
            "AND (tier_num = :tierNum OR :tierNum IS NULL) " +
            " ORDER BY cell_desc", 
    nativeQuery = true)
    List<Object[]> findCellsHousingFacility(@Param("instNum") String instNum, @Param("bldNum") String bldNum, @Param("unitId") String unitId, 
                             @Param("floorNum") String floorNum, @Param("tierNum") String tierNum);
    
}

