package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omnet.cnt.Model.NotiUserName;

public interface RepositoryNotiUserName extends JpaRepository<NotiUserName, String>{
	
	@Query(nativeQuery=true,value="  select user_id,user_last_name, user_first_name,user_mid_name, user_suffix_name from OMNET_USERS where user_status = 'A' order by user_last_name ")
	    List<NotiUserName> sentByUserName();

}
