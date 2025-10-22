package com.omnet.cnt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Judges;

@Repository
public interface JudgeRepository extends JpaRepository<Judges, String> {
	
	@Query("""
	        SELECT n FROM Judges n
	        WHERE LOWER(n.judgeFName) LIKE LOWER(CONCAT('%', :search, '%'))
	           OR LOWER(n.judgeLName) LIKE LOWER(CONCAT('%', :search, '%'))
	           OR LOWER(n.judgeMName) LIKE LOWER(CONCAT('%', :search, '%'))
	           OR LOWER(n.judgeSName) LIKE LOWER(CONCAT('%', :search, '%'))
	    """)
	Page<Judges> searchAllFields(@Param("search") String search, Pageable pageable);
	
	Optional<Judges> findByJudgeCode(String judgeCode);
	
}
