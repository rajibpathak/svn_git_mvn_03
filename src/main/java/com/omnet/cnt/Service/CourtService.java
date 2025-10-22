package com.omnet.cnt.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.Court;
import com.omnet.cnt.Repository.CourtRepository;

@Service
public class CourtService {

	private final CourtRepository repository;
	
	// Constructor injection, cleanest and testable way
    public CourtService(CourtRepository repository) {
        this.repository = repository;
    }
    
    public Court getCourtById(String courtCode) {
	    return repository.findByCourtCode(courtCode)
	    		.orElse(null);//(() -> new EntityNotFoundException("Name ID not found: " + judgeCode));
	}
    
    public Page<Court> getCourtByCourtName(String courtName, Pageable pageable) {
		if (courtName == null || courtName.isBlank()) {
	        return repository.findAll(pageable);
	    }
	    return repository.findByCourtNameContainingIgnoreCase(courtName, pageable);
	}
}
