package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.classes.Inmatetype;


@Repository
public interface InmateTypeRepo extends JpaRepository<Inmatetype, String> {

	@Query(
		    value = "SELECT inm.sbi_mst_sbi_no, " +
		            "       inm.inmate_full_name, " +
		            "       inm.commit_no, " +
		            "       sf_get_inst_short_desc(inm.current_inst_num) current_location, " +
		            "       itm.inmt_tp_desc  offender_type " +
		            "FROM inmate_tp_mst itm, inmate inm " +
		            "WHERE inm.inmt_tp_cd = itm.inmt_tp_cd " +
		            "  AND itm.inmt_tp_cd in ('01','06','09','16','17') " +
		            "  AND inm.inmate_status_code = 'A'",
		    nativeQuery = true
		)
		List<Object[]> findActiveInmatesByTypeAndStatus();

	
}
