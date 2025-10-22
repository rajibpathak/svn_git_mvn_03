/*
Document   : Screen Setup
Author     : Jamal Abraar
last update: 04/04/2024
*/

package com.omnet.cnt.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omnet.cnt.Model.Screens;
import com.omnet.cnt.Repository.ScreensRepository;

@Service
public class ScreensService {
		@Autowired 
		private ScreensRepository screensRepo;
		
		@Autowired
	    private JdbcTemplate jdbcTemplate;

		public List<Screens> findAllByOrderByScreenCodeAsc() {
	        return screensRepo.findAll(Sort.by("screenCode").ascending());
	    }
		
		public Optional<Screens> findByScreenCode(String screenCode) {
	        return screensRepo.findByScreenCode(screenCode);
	    }
		
		public Screens saveScreen(Screens screens) {
	        return screensRepo.save(screens);
	    }
		
		@Transactional
		public void updateScreen(Screens screens, String updid) {
		    screensRepo.updateScreen(screens, updid);
		}
		
		public ResponseEntity<List<String>> getTableNames() {
		    String sql = "Select * " +
		                 "from [Vw_Omnet_TableList] " +
		                 "Order by Name";
		    List<String> tableNames = jdbcTemplate.queryForList(sql, String.class);
		    return ResponseEntity.ok(tableNames);
		}

		public ResponseEntity<List<String>> getColumnNamesForTable(String tableName) {
			System.out.println("Received table name in service: " + tableName);
		    String sql = "SELECT COLUMN_NAME " +
		                 "FROM Vw_Omnet_Table_ColumnList " +
		                 "WHERE TABLE_NAME = ?";
		    List<String> columnNames = jdbcTemplate.queryForList(sql, String.class, tableName);
		    return ResponseEntity.ok(columnNames);
		}
}