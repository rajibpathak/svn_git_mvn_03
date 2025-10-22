/**
 * Document: ReasonMstRepository.java
 * Author: Jamal Abraar
 * Date Created: 19-Jun-2024
 * Last Updated: 
 */

package com.omnet.cnt.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.ReasonMaster;

public interface ReasonMstRepository extends JpaRepository<ReasonMaster, String> {
	
	@Query("Select c.reasonCode, c.reasonDescription from ReasonMaster c Where c.status = 'A' ORDER BY c.reasonDescription")
	List<Object[]> findReasonCodesAndDescriptionsByStatus(@Param("status") String status);
	
	 @Query("SELECT r.reasonDescription, r.reasonCode FROM ReasonMaster r WHERE r.subTpCdResn = :subTpCdResn ORDER BY r.reasonDescription")
	    List<Object[]> findValuefortheDropDown(@Param("subTpCdResn") String subTpCdResn);
}
