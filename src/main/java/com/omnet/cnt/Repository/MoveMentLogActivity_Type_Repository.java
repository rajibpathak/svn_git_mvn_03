package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.MoveMentLogActivity_Type;

@Repository
@EnableJpaRepositories
public interface MoveMentLogActivity_Type_Repository extends JpaRepository<MoveMentLogActivity_Type, String>{

	@Query(nativeQuery=true,value="SELECT ACTIVITY_TYPE_DESC,ACTIVITY_TYPE_CODE FROM ACTIVITY_TYPE_RT WHERE STATUS= 'A' AND INT_EXT_FLAG = 'I' ORDER BY ACTIVITY_TYPE_DESC")
	List<MoveMentLogActivity_Type> ActivityType();
	
}
