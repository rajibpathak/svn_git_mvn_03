package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.omnet.cnt.Model.Building;

@EnableJpaRepositories
@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {
	
	 @Query(value = "SELECT b.bld_name ,b.bld_num FROM Building b WHERE b.inst_num = :instNum AND b.status = 'A' ORDER BY b.bld_name", nativeQuery = true)
	    List<Object[]> findBuildingNamesByInstNum(@Param("instNum") String instNum);
	    
	    @Query(value = "SELECT Spkg_Mov_Aotb.sf_get_bld_detail(:p_inst_num, :p_bed_no) FROM dual", nativeQuery = true)
	    List<Object[]> callSfGetBldDetail(
	        @Param("p_inst_num") String instNum,
	        @Param("p_bed_no") String bedNo
	    );

	    @Query(value = "SELECT b.bld_name ,b.bld_num FROM Building b WHERE b.inst_num = :instNum ORDER BY b.bld_name", nativeQuery = true)
	    List<Object[]> findBuildingHousingFacility(@Param("instNum") String instNum);

	    @Query(value = 
	    	    "SELECT " +
	    	    "    b.bld_num, " +
	    	    "    b.bld_name, " +
	    	    "    u.unit_id, " +
	    	    "    u.unit_desc, " +
	    	    "    f.floor_num, " +
	    	    "    f.floor_desc, " +
	    	    "    t.tier_num, " +
	    	    "    t.tier_desc " +
	    	    "FROM Building b " +
	    	    "LEFT JOIN inst_unit_rt u " +
	    	    "    ON b.inst_num = u.inst_num " +
	    	    "   AND b.bld_num = u.bld_num " +
	    	    "   AND u.status = 'A' " +
	    	    "LEFT JOIN inst_floor_rt f " +
	    	    "    ON b.inst_num = f.inst_num " +
	    	    "   AND b.bld_num = f.bld_num " +
	    	    "   AND f.status = 'A' " +
	    	    "   AND (f.unit_id = u.unit_id OR f.unit_id IS NULL) " +
	    	    "LEFT JOIN inst_tier_rt t " +
	    	    "    ON b.inst_num = t.inst_num " +
	    	    "   AND b.bld_num = t.bld_num " +
	    	    "   AND t.status = 'A' " +
	    	    "   AND (t.unit_id = u.unit_id OR t.unit_id IS NULL) " +
	    	    "   AND (t.floor_num = f.floor_num OR t.floor_num IS NULL) " +
	    	    "WHERE b.inst_num = :instNum " +
	    	    "  AND b.status = 'A' " +
	    	    "ORDER BY b.bld_name, u.unit_desc, f.floor_desc, t.tier_desc",
	    	    nativeQuery = true)
	    	List<Object[]> getBuildingHierarchy(@Param("instNum") Integer instNum);









	
}
