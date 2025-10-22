package com.omnet.cnt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Court;

@Repository
public interface CourtRepository extends JpaRepository<Court, String> {
	Page<Court> findByCourtNameContainingIgnoreCase(String courtName, Pageable pageable);
	
	Optional<Court> findByCourtCode(String courtCode);
}
