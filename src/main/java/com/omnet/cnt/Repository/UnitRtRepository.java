package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.omnet.cnt.Model.InstUnitRt;


@Repository
public interface UnitRtRepository extends JpaRepository<InstUnitRt, String> {

	@Query(value = "SELECT unit_desc, unit_id FROM INST_UNIT_RT " +
            "WHERE inst_num = :instNum " +
            "AND (bld_num = :bldNum OR :bldNum IS NULL) " +
            "AND status = 'A'", 
    nativeQuery = true)
	List<Object[]> findByInstNumAndBldNum(@Param("instNum") String instNum,@Param("bldNum") String bldNum);
  
	@Query(value = "SELECT unit_desc, unit_id FROM INST_UNIT_RT " +
            "WHERE inst_num = :instNum " +
            "AND (bld_num = :bldNum OR :bldNum IS NULL) ",
    nativeQuery = true)
	List<Object[]> findUnitHosuingFacility(@Param("instNum") String instNum,@Param("bldNum") String bldNum);
}