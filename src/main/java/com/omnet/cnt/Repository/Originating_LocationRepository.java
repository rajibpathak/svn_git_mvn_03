package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Originating_Location;

@Repository
@EnableJpaRepositories
public interface Originating_LocationRepository extends JpaRepository<Originating_Location,String>{
	
	@Query(nativeQuery=true,value="SELECT COUNT_LOC_DESC, COUNT_LOC_CODE FROM INST_LOCATION WHERE inst_num =:instNum and COUNT_LOC_STATUS = 'A' ORDER BY COUNT_LOC_DESC")
	List<Originating_Location> Originating_Location(String instNum);

}
