package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omnet.cnt.Model.Bed;
import com.omnet.cnt.Model.NotiCategory;

public interface NoticategoryRepository extends JpaRepository<NotiCategory, String>{
	
	@Query(nativeQuery=true,value=" select message_description, short_message from screen_notification_rt where type in ('A','I') order by message_description asc")
	List<NotiCategory> categoryvalues();

}
