package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.HousingFacilityBedAssignments;

@Repository
@EnableJpaRepositories
public interface HousingFacilityBedAssignmentsRepository extends JpaRepository<HousingFacilityBedAssignments, String>{
	
	@Query(nativeQuery=true,value="Select inst_name , "
			+ "i.inst_num "
			+ "from institution I, "
			+ "user_access_inst U "
			+ "where i.inst_num = u.Inst_num "
			+ "and u.user_id = :userId "
			+ "and status = 'A' order by inst_name ")
	List<HousingFacilityBedAssignments> Facilityvalue(String userId);
	
	@Query(nativeQuery=true,value=" select sf_Get_rec_rm_cnt(:inst_num) from dual")
	String ReceivingRoomCount(String inst_num);

	
}
