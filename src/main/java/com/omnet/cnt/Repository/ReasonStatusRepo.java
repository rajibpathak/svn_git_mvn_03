package com.omnet.cnt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.Reasonstatus;

public interface ReasonStatusRepo extends JpaRepository<Reasonstatus, Long> {
    @Query(value = "SELECT LISTAGG(b.ref_value_desc, ', ') WITHIN GROUP (ORDER BY a.rh_reason_cd) AS ref_value_desc " +
            "FROM inm_rh_rsn_status a " +
            "JOIN cm_reference_values b ON a.rh_reason_cd = b.ref_value_code " +
            "WHERE b.ref_category_module = 'RHU' " +
            "AND b.ref_category_code = 'RH_REASON' " +
            "AND a.inactive_date IS NULL " +
            "AND a.commit_no = :commitNo", 
    nativeQuery = true)
String findRefValueDescByCommitNo(@Param("commitNo") String commitNo);

}
