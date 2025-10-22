package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.InmateDocsIds;

@Repository
public interface InmatedocidsRepository extends JpaRepository<InmateDocsIds, String> {
	
	@Query("SELECT i FROM InmateDocsIds i WHERE i.commitNo = :commitNo")
	List<InmateDocsIds> findByCommitNo(@Param("commitNo") String commitNo);

	

}
