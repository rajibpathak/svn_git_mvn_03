/*
Document   : Contact Master
Author     : Jamal Abraar
last update: 03/06/2024
*/
package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.ContactTpMst;

public interface ContactTpMstRepository extends JpaRepository<ContactTpMst, String> {
	
	@Query("SELECT c.contactCode, c.contactDesc FROM ContactTpMst c WHERE c.status = 'A' ORDER BY CONT_TP_DESC")
    List<Object[]> findContactCodesAndDescriptionsByStatus(@Param("status") String status);
    
    
}
