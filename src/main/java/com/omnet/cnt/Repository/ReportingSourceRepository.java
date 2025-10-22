package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.ReportingSource;


@Repository
@EnableJpaRepositories
public interface ReportingSourceRepository extends JpaRepository<ReportingSource, String> {
	
	@Query(nativeQuery = true, value = "SELECT REPORTING_SOURCE_CODE, REPORTING_DESC FROM reporting_source_rt WHERE STATUS='A' ORDER BY REPORTING_DESC")
	List<Object[]> reportingDropDown();
	
}
