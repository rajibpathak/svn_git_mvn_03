package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.ReasonUpdate;

public interface reasonupdatestatus extends JpaRepository<ReasonUpdate, Long> {
	
    @Query(
            value = "SELECT b.PROCESSED_FLAG, a.ref_value_code, a.ref_value_desc " +
                    "FROM cm_reference_values a " +
                    "JOIN inm_rh_rsn_status_stage b " +
                    "ON a.ref_value_code = b.RH_REASON_CD " +
                    "WHERE a.ref_category_module = 'RHU' " +
                    "AND a.ref_category_code = 'RH_REASON' " +
                    "AND a.active_y_n IS NULL " +
                    "AND b.commit_no = :commitNo " +
                    "AND NVL(b.delete_flag,'N') != 'Y' " +
                    "ORDER BY b.PROCESSED_FLAG DESC NULLS LAST, a.ref_value_code",
            nativeQuery = true
        )
        List<Object> findRhReasonsByCommitNo(@Param("commitNo") String commitNo);

}
