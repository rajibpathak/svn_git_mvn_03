package com.omnet.cnt.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.Statute;
import com.omnet.cnt.Repository.StatuteRepository;

@Service
public class StatuteService {
	private StatuteRepository statuteRepo;

	
	public StatuteService(StatuteRepository repository) {
        this.statuteRepo = repository;
    }
	
	public Page<Statute> searchStatute(
	        String title, String sec, String subSec,
	        String type, String statueClass, String keyword, Pageable pageable
	    ){
            System.out.println("Searching Statute....");
		Specification<Statute> spec = Specification.where(null);
		
		/*if (juris != null && !juris.isBlank())
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("STATUTE_JURIS")), "%" + juris.toLowerCase() + "%"));*/

        if (title != null && !title.isBlank())
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("id").get("STATUTE_TITLE")), "%" + title.toLowerCase() + "%"));

        if (sec != null && !sec.isBlank())
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("id").get("STATUTE_SECTION")), "%" + sec.toLowerCase() + "%"));

        if (subSec != null && !subSec.isBlank())
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("id").get("STATUTE_SUBSECTION")), "%" + subSec.toLowerCase() + "%"));
        
        if (type != null && !type.isBlank())
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("id").get("STATUTE_TYPE")), "%" + type.toLowerCase() + "%"));

        if (statueClass != null && !statueClass.isBlank())
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("STATUTE_CLASS")), "%" + statueClass.toLowerCase() + "%"));
        
        /*if (statuteDesc != null && !statuteDesc.isBlank())
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("STATUTE_DESC")), "%" + statuteDesc.toLowerCase() + "%"));*/
        
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("keyword1")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("keyword2")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("keyword3")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("keyword4")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("keyword5")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("keyword6")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("keyword7")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("keyword8")), "%" + keyword.toLowerCase() + "%")
            ));
        }
		
		return statuteRepo.findAll(spec, pageable);
	}
	
	/*public Page<LawFile> searchLaws(String title,
            String section,
            String subsection,
            String type,
            String clazz,
            String keyword,
            Pageable pageable) {
		Page<LawFile> entities = statuteRepo.searchLawFiles(keyword, title, section, subsection, type, clazz, pageable);

        // Map entities -> DTOs
		return entities.map(l -> new LawFile(
                l.statuteTitle(),
                l.statuteSection(),
                l.statuteSubsection(),
                l.statuteType(),
                l.statuteClass(),
                l.statuteDesc()
        ));
    }*/
}
