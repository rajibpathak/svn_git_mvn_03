	/**
	 * Document: RelationshipRtRepository.java
	 * Author: Jamal Abraar
	 * Date Created: 11-Jun-2024
	 * Last Updated: 
	 */


package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.Relationship;

public interface RelationshipRtRepository extends JpaRepository<Relationship, String> {
	
	@Query("SELECT c.relationshipCode, c.relationshipDesc FROM Relationship c WHERE c.status = 'A' ORDER BY c.relationshipDesc")
    List<Object[]> findRelationshipCodesAndDescriptionsByStatus(@Param("status") String status);

}
