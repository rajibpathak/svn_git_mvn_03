package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omnet.cnt.Model.ReferenceValue;

public interface ReferenceValueRepository extends JpaRepository<ReferenceValue, String> {
	
	 @Query(
		        value = "SELECT ref_value_desc, ref_value_code " +
		                "FROM cm_reference_values " +
		                "WHERE ref_category_module = 'MOV' " +
		                "AND ref_category_code = 'PRTD_OFNDR' " +
		                "ORDER BY ref_value_desc",
		        nativeQuery = true
		    )
		    List<Object[]> findMovementReasons();

		    @Query(
		        value = "SELECT ref_value_desc, ref_value_code " +
		                "FROM cm_reference_values " +
		                "WHERE ref_category_module = 'MOV' " +
		                "AND ref_category_code = 'DSAP_REASN' " +
		                "ORDER BY ref_value_desc",
		        nativeQuery = true
		    )
		    List<Object[]> findDisapprovalReasons();

}
