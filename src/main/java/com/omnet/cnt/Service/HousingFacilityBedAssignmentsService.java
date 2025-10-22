package com.omnet.cnt.Service;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.HousingFacilityBedAssignments;
import com.omnet.cnt.Model.Bed;
import com.omnet.cnt.Model.BedList;
import com.omnet.cnt.Repository.BedRepository;
import com.omnet.cnt.Repository.BuildingRepository;
import com.omnet.cnt.Repository.CellRtRepository;
import com.omnet.cnt.Repository.FloorRtRepository;
import com.omnet.cnt.Repository.HousingFacilityBedAssignmentsRepository;
import com.omnet.cnt.Repository.TierRepository;
import com.omnet.cnt.Repository.UnitRtRepository;

import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;



@Service
public class HousingFacilityBedAssignmentsService {
	
	@Autowired
	private HousingFacilityBedAssignmentsRepository repo;
	
	@Autowired
	private BedRepository Bed;
	
	@Autowired
	private BuildingRepository building;
	
	@Autowired
	private UnitRtRepository unit;
	
	@Autowired
	private FloorRtRepository floor;
	
	@Autowired
	private TierRepository tier;
	
	@Autowired
	private CellRtRepository cell;
	
	@Autowired
    private DataSource dataSource;
	
	
	public String ReceivingRoomCount(String inst_num) {
		return repo.ReceivingRoomCount(inst_num);
		
	}
	
	public List<Bed> BedValue(String instNum,String BLDNO,String UNIT_ID,String FloorNo,String TierNo,String CellNo){ 
		return Bed.Bed(instNum, BLDNO, UNIT_ID, FloorNo, TierNo, CellNo);
	}
	
	public List<Bed> bedFacility(String instNum,String BLDNO,String UNIT_ID,String FloorNo,String TierNo,String CellNo){  
		return Bed.getBedHousingFacility(instNum, BLDNO, UNIT_ID, FloorNo, TierNo, CellNo);
	}

	public List<HousingFacilityBedAssignments> Facility(String userId){
		return repo.Facilityvalue(userId);
	}
	
	public List<Object[]> getBuildingsForInstNum(String instNum) {
		List<Object[]> BuildingList = building.findBuildingHousingFacility(instNum);
		return BuildingList;
	}
	
	public List<Object[]> getUnitsFacility(String instNum, String bldNum) {
		List<Object[]> UnitList = unit.findUnitHosuingFacility(instNum, bldNum);
		return UnitList;
	}
	
	public List<Object[]> getFloorsFacility(String instNum, String bldNum, String unitId) {
		List<Object[]> FloorList = floor.findFloorsHousingFacility(instNum, bldNum, unitId);
		return FloorList;
	}
	
	public List<Object[]> getTiersFacility(String instNum, String bldNum, String unitId, String floorNum) {
		List<Object[]> TiersList = tier.findTiersHousingFacility(instNum, bldNum, unitId, floorNum);
		return TiersList;
	}
	
	public List<Object[]> getCellsFacility(String instNum, String bldNum, String unitId, String floorNum, String tierNum) {
		List<Object[]> CellList = cell.findCellsHousingFacility(instNum, bldNum, unitId, floorNum, tierNum);
		return CellList;
	}
	
	@SuppressWarnings("deprecation")
	public List<BedList> BedListValues(String i_bed_type, String i_inst_num, String i_bld_num,
	        String i_unit_id, String i_floor_num, String i_tier_num, String i_cell_no, String i_bed_no, String i_bed_status) {
	    List<BedList> BedvalueList = new ArrayList<>();
	    try (Connection connection = dataSource.getConnection()) {
	        CallableStatement callableStatement = connection.prepareCall("{call spkg_omnet_wrapper.sp_mov_inba_search_query(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
	        callableStatement.registerOutParameter(1, OracleTypes.ARRAY, "TB_MOV_INBA_SEARCH");
	        callableStatement.setString(2, i_bed_type);
	        callableStatement.setString(3, i_inst_num);
	        callableStatement.setString(4, i_bld_num);
	        callableStatement.setString(5, i_unit_id);
	        callableStatement.setString(6, i_floor_num);
	        callableStatement.setString(7, i_tier_num);
	        callableStatement.setString(8, i_cell_no);
	        callableStatement.setString(9, i_bed_no);
	        callableStatement.setString(10, i_bed_status);

	        callableStatement.execute();

	        // Check if the array returned is null
	        Array array = callableStatement.getArray(1);
	        if (array != null) {
	            Object[] arrayData = (Object[]) array.getArray();
	            for (Object obj : arrayData) {

	                if (obj instanceof STRUCT) {

	                    STRUCT struct = (STRUCT) obj;
	                    Object[] attributes = struct.getAttributes();

	                    BedList BedListObject = new BedList();

	                    BedListObject.setCommitNo((String) attributes[0]);
	                    BedListObject.setBedNo((String) attributes[1]);
	                    BedListObject.setBedDescription((String) attributes[2]);
	                    BedListObject.setBedType((String) attributes[3]);
	                    BedListObject.setBedStatus((String) attributes[4]);
	                    BedListObject.setBedStat((String) attributes[5]);
	                    BedListObject.setSBINo((String) attributes[6]);
	                    BedListObject.setCommitLastName((String) attributes[7]);
	                    BedListObject.setCommitFirstName((String) attributes[8]);
	                    BedListObject.setCommitMiddleName((String) attributes[9]);
	                    BedListObject.setCommitSuffixName((String) attributes[10]);
	                    BedListObject.setAvailableDate((Timestamp) attributes[11]);
	                    BedListObject.setAvailableTime((String) attributes[12]);

	                    BedvalueList.add(BedListObject);
	                }

	            }
	        }

	        callableStatement.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return BedvalueList.isEmpty() ? null : BedvalueList;  // If the list is empty, return null.
	}

	
	
	public Map<String, Object> BedDetails(String ivc_bed_no) {
		Map<String, Object> result = new HashMap<>();
		try (Connection conn = dataSource.getConnection()) {
		String sql = "{call SPKG_MOV_INBA.sp_get_housing_det(?,?,?,?,?,?,?,?,?,?,?)}";
		try (CallableStatement stmt = conn.prepareCall(sql)) {
		stmt.setString(1, ivc_bed_no);
		stmt.registerOutParameter(2, Types.VARCHAR);
		stmt.registerOutParameter(3, Types.VARCHAR);
		stmt.registerOutParameter(4, Types.VARCHAR);
		stmt.registerOutParameter(5, Types.VARCHAR);
		stmt.registerOutParameter(6, Types.VARCHAR);
		stmt.registerOutParameter(6, Types.VARCHAR);
		stmt.registerOutParameter(7, Types.VARCHAR);
		stmt.registerOutParameter(8, Types.VARCHAR);
		stmt.registerOutParameter(9, Types.VARCHAR);
		stmt.registerOutParameter(10, Types.VARCHAR);
		stmt.registerOutParameter(11, Types.VARCHAR);
		stmt.execute();
		
		 String ovc_bld_num = stmt.getString(2);
         String ovc_bld_name = stmt.getString(3);
         String ovc_unit_id = stmt.getString(4);
         String ovc_unit_desc = stmt.getString(5);
         String ovc_floor_num = stmt.getString(6);
         String ovc_floor_desc = stmt.getString(7);
         String ovc_tier_num = stmt.getString(8);
         String ovc_tier_desc = stmt.getString(9);
         String ovc_cell_no = stmt.getString(10);
         String ovc_cell_desc = stmt.getString(11);
		
		result.put("ivc_bed_no", ivc_bed_no);
		result.put("ovc_bld_num", ovc_bld_num);
		result.put("ovc_bld_name",ovc_bld_name);
		result.put("ovc_unit_id",ovc_unit_id);
		result.put("ovc_unit_desc",ovc_unit_desc);
		result.put("ovc_floor_num", ovc_floor_num);
		result.put("ovc_floor_desc", ovc_floor_desc);
		result.put("ovc_tier_num", ovc_tier_num);
		result.put("ovc_tier_desc", ovc_tier_desc);
		result.put("ovc_cell_no", ovc_cell_no);
		result.put("ovc_cell_desc", ovc_cell_desc);
		
		}
		} catch (SQLException e) {
		e.printStackTrace();
		}
		return result;
		}
	
	
}
