package com.omnet.cnt.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Repository.JudgeRepository;
import com.omnet.cnt.Model.Judges;

@Service
public class JudgeService {
	private final JudgeRepository repository;
	
	public JudgeService(JudgeRepository repository) {
		this.repository = repository;
	}
	
	public Judges getNameById(String judgeCode) {
	    return repository.findByJudgeCode(judgeCode)
	    		.orElse(null);//(() -> new EntityNotFoundException("Name ID not found: " + judgeCode));
	}

	
	public Page<Judges> getFilteredData(String search, Pageable pageable){
		if (search == null || search.isBlank()) {
            return repository.findAll(pageable);
        }
		
		return repository.searchAllFields(search, pageable);
	}
}
