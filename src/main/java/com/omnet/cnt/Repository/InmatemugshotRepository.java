package com.omnet.cnt.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.InmateMugshot;

public interface InmatemugshotRepository extends JpaRepository<InmateMugshot, String> {
	 @Query(value = "SELECT IMAGE FROM INMATE_MUGSHOT_IMAGE WHERE Accessed_date = trunc(sysdate) " +
	            "AND session_id = userenv('sessionid') " +
	            "AND Inserted_user_id = :user AND sbi_no = :sbiNo FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	    byte[] findImageByAccessedDateAndUserIdAndSbiNo(
	            @Param("user") String user,
	            @Param("sbiNo") String sbiNo);
	}


