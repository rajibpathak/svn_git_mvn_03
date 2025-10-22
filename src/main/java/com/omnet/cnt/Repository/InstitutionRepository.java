package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
	
	@Query("SELECT i.instName, i.instNum FROM Institution i ORDER BY i.instName")
    List<Object[]> findFacilitiesForDropdown(@Param("typeOfWaitlist") String typeOfWaitlist);
    
    @Query("SELECT i.instName, i.instNum FROM Institution i WHERE i.secHouCd IS NOT NULL ORDER BY i.instName")
    List<Object[]> findIntermediateFacilitiesForDropdown(@Param("typeOfWaitlist") String typeOfWaitlist);
    
    @Query(value = "SELECT DISTINCT i.inst_num, i.inst_name FROM institution I, user_access_inst U WHERE u.user_id = :userId AND status = 'A' ORDER BY inst_name", nativeQuery = true)
    List<Object[]> findInstitutionForMOAM(@Param("userId") String userId);

}
