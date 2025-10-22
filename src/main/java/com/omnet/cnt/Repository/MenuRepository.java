package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omnet.cnt.Model.Menu;

public interface MenuRepository extends JpaRepository<Menu, String>{

	@Query(value = "SELECT * FROM OMNET_MENU WHERE status = 'A' ", nativeQuery = true)
	List<Menu> findByStatus(String status);
	
}
