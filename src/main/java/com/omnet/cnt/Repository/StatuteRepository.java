package com.omnet.cnt.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.Statute;
import com.omnet.cnt.Model.StatuteId;

@Repository
public interface StatuteRepository extends JpaRepository<Statute, StatuteId>, JpaSpecificationExecutor<Statute> {
	
	/*@Query("""
	        SELECT n FROM law_file n
	        WHERE LOWER(n.STATUTE_TITLE) LIKE LOWER(CONCAT('%', :search, '%'))
	           OR LOWER(n.judgeLName) LIKE LOWER(CONCAT('%', :search, '%'))
	           OR LOWER(n.judgeMName) LIKE LOWER(CONCAT('%', :search, '%'))
	           OR LOWER(n.judgeSName) LIKE LOWER(CONCAT('%', :search, '%'))
	    """)
	Page<Statute> searchAllFields(@Param("search") String search, Pageable pageable);*/
	/*@Query(value = """
		    SELECT statute_title, statute_section, statute_subsection, statute_type, statute_class, statute_desc
		    FROM LAW_FILE lf
		    WHERE (
		        :keyword IS NULL OR 
		        LOWER(:keyword) IN (
		            LOWER(lf.keyword01), LOWER(lf.keyword02), LOWER(lf.keyword03),
		            LOWER(lf.keyword04), LOWER(lf.keyword05), LOWER(lf.keyword06),
		            LOWER(lf.keyword07), LOWER(lf.keyword08)
		        )
		    )
		    AND (:title IS NULL OR lf.statute_title = :title)
		    AND (:section IS NULL OR lf.statute_section = :section)
		    AND (:subsection IS NULL OR lf.statute_subsection = :subsection)
		    AND (:type IS NULL OR lf.statute_type = :type)
		    AND (:class IS NULL OR lf.statute_class = :class)
		    ORDER BY lf.statute_title, lf.statute_section
		    """)
	Page<LawFile> searchLawFiles(
		    @Param("keyword") String keyword,
		    @Param("title") String title,
		    @Param("section") String section,
		    @Param("subsection") String subsection,
		    @Param("type") String type,
		    @Param("class") String statuteClass,
		    Pageable pageable
		);*/
}
