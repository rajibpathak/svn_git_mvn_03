package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.ReportingType;

@Repository
@EnableJpaRepositories
public interface ReportingTypeRepo extends JpaRepository<ReportingType,String > {
	@Query(nativeQuery = true,value = "SELECT ENEMY_TP_CD,ENEMY_TP_DESC FROM enemy_type_mst ORDER BY enemy_tp_desc")
	List<Object[]> typeDropDown();

}
