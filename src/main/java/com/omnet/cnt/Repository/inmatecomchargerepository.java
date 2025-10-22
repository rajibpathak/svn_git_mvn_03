package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.inmatecomcharges;

public interface inmatecomchargerepository extends JpaRepository<inmatecomcharges,String> {
	
	@Query(value = "SELECT * FROM inmate_committed_charges WHERE commit_no = :commitNo", nativeQuery = true)
	List<String> findDetentionerByCommitNo(@Param("commitNo") String commitNo);

	
}
