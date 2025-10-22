package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Bed;

@Repository
@EnableJpaRepositories
public interface BedRepository extends JpaRepository<Bed, String>{
	
	@Query(nativeQuery=true,value=" SELECT * FROM inst_bed_rt "
			+ "                            WHERE  inst_num = :instNum "
			+ "                            AND   nvl(bld_num, '*') = nvl(:BLDNO,'*') "
			+ "                            AND   nvl(unit_id, '*') = nvl(:UNIT_ID,'*') "
			+ "                            AND    nvl(floor_num,'*') = nvl(:FloorNo,'*') "
			+ "                            AND    nvl(tier_num, '*') = nvl(:TierNo, '*') "
			+ "                            AND    nvl(cell_no, '*') = nvl(:CellNo, '*') "
			+ "                            AND    status = 'A' ")
	List<Bed> Bed(String instNum,String BLDNO,String UNIT_ID,String FloorNo,String TierNo,String CellNo);

	@Query(nativeQuery=true,value=" SELECT * FROM inst_bed_rt "
			+ "                            WHERE  inst_num = :instNum "
			+ "                            AND   nvl(bld_num, '*') = nvl(:BLDNO,'*') "
			+ "                            AND   nvl(unit_id, '*') = nvl(:UNIT_ID,'*') "
			+ "                            AND    nvl(floor_num,'*') = nvl(:FloorNo,'*') "
			+ "                            AND    nvl(tier_num, '*') = nvl(:TierNo, '*') "
			+ "                            AND    nvl(cell_no, '*') = nvl(:CellNo, '*') ")
	List<Bed> getBedHousingFacility(String instNum,String BLDNO,String UNIT_ID,String FloorNo,String TierNo,String CellNo);

    @Query(value = "SELECT SPKG_MOV_AOTB.sf_check_for_security(:commitNo, :bedNo) FROM dual", nativeQuery = true)
    int checkForSecurity(@Param("commitNo") String commitNo, 
                         @Param("bedNo") String bedNo);
    
    
    @Query("SELECT i.bedSex FROM InstBedRt i WHERE i.bedNum = :bedNum")
    String findBedSexByBedNum(String bedNum);
}
