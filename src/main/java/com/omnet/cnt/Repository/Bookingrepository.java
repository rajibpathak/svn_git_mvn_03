package com.omnet.cnt.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.BookingDetail;

@Repository
public interface Bookingrepository extends JpaRepository<BookingDetail, String> {
	 @Query(value = "SELECT SPKG_CBI_BKNG.SF_GET_SNTC_LVL_CD(:ivc_commit_no, UPPER(:p_user_id)) FROM dual", nativeQuery = true)
	    String getSntcLvlCd(@Param("ivc_commit_no") String commitNo, @Param("p_user_id") String userId);
	 
}
