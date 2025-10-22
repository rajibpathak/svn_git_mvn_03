package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omnet.cnt.Model.DisciplinaryInfo;

public interface DisciplinaryInfoRepository extends JpaRepository<DisciplinaryInfo, Long> {

    @Query(
        value = "SELECT DISTINCT TO_CHAR(A.INCIDENT_SEQ_NUM) " +
                "FROM INCIDENT A, INDIVIDUALS_INVOLVED B " +
                "WHERE A.INCIDENT_SEQ_NUM = B.INCIDENT_SEQ_NUM " +
                "AND COMMIT_NO = ?1 " +
                "AND NVL(INDV_DEL_FLG, 'N') <> 'Y' " +
                "ORDER BY A.INCIDENT_SEQ_NUM",
        nativeQuery = true
    )
    List<String> getIncidentIdsByCommitNo(String commitNo);
}
