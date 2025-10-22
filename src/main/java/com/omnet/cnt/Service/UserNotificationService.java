package com.omnet.cnt.Service;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.sql.Struct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.NOTISearchTotalCount;
import com.omnet.cnt.Model.NOTI_SEARCH_TAB;
import com.omnet.cnt.Model.NotiCategory;
import com.omnet.cnt.Model.NotiUserName;
import com.omnet.cnt.Model.UserNotificationDetails;
import com.omnet.cnt.Model.Users;
import com.omnet.cnt.Repository.NOTITotalCount;
import com.omnet.cnt.Repository.NoticategoryRepository;
import com.omnet.cnt.Repository.OmnetDesktopRepository;
import com.omnet.cnt.Repository.RepositoryNotiUserName;
import com.omnet.cnt.Repository.UserRepository;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;

@Service
public class UserNotificationService {
	
	@Autowired
    private DataSource dataSource;

    @Autowired
    private RepositoryNotiUserName Sentby;
	
	@Autowired
	private NoticategoryRepository cat;
	
	@Autowired
	private NOTITotalCount SearchCount;
	
	
    public List<NotiUserName> SentByUserName(){
    	return Sentby.sentByUserName();
    }
	
	public List<NotiCategory> Category(){
		return cat.categoryvalues();
	}

	public String GetNotiSearchCount(String pUserId,String pShortMsg,String pDateFrom,String pDateTo,String pSentBy,String pSbiNo,String pLastName){
		System.out.println("TotalCount="+pUserId+" "+pDateFrom+" "+pDateTo+" "+pSbiNo);
		return SearchCount.GetNotiCount(pUserId, pShortMsg, pDateFrom, pDateTo, pSentBy, pSbiNo, pLastName);
	}
	
	public void callproceduredetail(List<UserNotificationDetails> Detailsvalues, String userId) {
		String procedureCall = "{call spkg_omnet_wrapper.sp_cbi_noti_action_req_dtl_update(?, ?)}";
		System.out.println("post procedure"); 
		try (Connection connection = dataSource.getConnection();
			
	             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
			System.out.println("post procedure1"); 
			 OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
			 
			 Struct[] structs = new Struct[Detailsvalues.size()];

			 for (int i = 0; i < Detailsvalues.size(); i++) {
				 
				 structs[i] = oracleConnection.createStruct("TY_CBI_NOTI_MSG_DTL_UPD", new Object[]{
						 Detailsvalues.get(i).getUser_mess_seq_num(),
						 Detailsvalues.get(i).getShort_message(),
						 Detailsvalues.get(i).getDate_time_sent(),
						 Detailsvalues.get(i).getCreated_user_id(),
						 Detailsvalues.get(i).getCreated_user_name(),
						 Detailsvalues.get(i).getNo_of_days(),
						 Detailsvalues.get(i).getScreen_code(),
						 Detailsvalues.get(i).getMessage_text(),
						 Detailsvalues.get(i).getRemove(),
						 Detailsvalues.get(i).getSbi_no(),
						 Detailsvalues.get(i).getCommit_no(),
						 Detailsvalues.get(i).getOffender_name()
						 
				 });
					System.out.println("post procedure3"); 
			 }
			 
			 Array array = oracleConnection.createOracleArray("TB_CBI_NOTI_MSG_DTL_UPD", structs);
			 
			 callableStatement.setArray(1, array);
	            callableStatement.setString(2, userId);
	        	System.out.println("post procedure4"); 
	            callableStatement.execute();
	        	System.out.println("post procedure5"); 
	             }catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error executing stored procedure", e);
	        }
	}
	
	
	public List<Map<String,Object>> GetNoti1(String ivc_noti_type, String p_user_id) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call SPKG_CBI_NOTI.sp_action_req_main_query(?, ?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
	            stmt.setString(2, ivc_noti_type);
	            stmt.setString(3, p_user_id);
	            stmt.execute();
	            
	        	try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
	        		ResultSetMetaData metaData = rs.getMetaData();
	        		int columnCount = metaData.getColumnCount();
	        		while (rs.next()) {
	        		Map<String, Object> resultMap = new HashMap<>();
	        		for (int i = 1; i <= columnCount; i++) {
	        		String columnName = metaData.getColumnName(i);
	        		Object columnValue = rs.getObject(i);
	        		resultMap.put(columnName, columnValue);
	        		}
	        		resultList.add(resultMap);
	        		}
	        		}
	        	
     
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultList;
	}
	
	public List<Map<String,Object>> GetNoti2(String ivc_noti_type,String p_user_id) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call SPKG_CBI_NOTI.sp_info_main_query(?, ?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
	            stmt.setString(2, ivc_noti_type);
	            stmt.setString(3,  p_user_id);
	            stmt.execute();
	            
	        	try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
	        		ResultSetMetaData metaData = rs.getMetaData();
	        		int columnCount = metaData.getColumnCount();
	        		while (rs.next()) {
	        		Map<String, Object> resultMap = new HashMap<>();
	        		for (int i = 1; i <= columnCount; i++) {
	        		String columnName = metaData.getColumnName(i);
	        		Object columnValue = rs.getObject(i);
	        		resultMap.put(columnName, columnValue);
	        		}
	        		resultList.add(resultMap);
	        		}
	        		}
	        	
     
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultList;
	}
	
	public List<Map<String,Object>> Search(String inu_sort_by,String ivc_short_message,String p_user_id) {
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    inu_sort_by="5";
	    try (Connection conn = dataSource.getConnection()) {
	        String sql = "{call spkg_omnet_wrapper.sp_cbi_noti_action_req_dtl_query(?, ?, ?, ?)}"; 
	        try (CallableStatement stmt = conn.prepareCall(sql)) {
	            stmt.registerOutParameter(1, java.sql.Types.REF_CURSOR);
	            stmt.setString(2, inu_sort_by);
	            stmt.setString(3, ivc_short_message);
	            stmt.setString(4, p_user_id);
	            stmt.execute();
	            
	        	try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
	        		ResultSetMetaData metaData = rs.getMetaData();
	        		int columnCount = metaData.getColumnCount();
	        		while (rs.next()) {
	        		Map<String, Object> resultMap = new HashMap<>();
	        		for (int i = 1; i <= columnCount; i++) {
	        		String columnName = metaData.getColumnName(i);
	        		Object columnValue = rs.getObject(i);
	        		resultMap.put(columnName, columnValue);
	        		}

	        		resultList.add(resultMap);
	        		}
	        		}
	        	
     
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultList;
	}
	//GetNoti3
	@SuppressWarnings("deprecation")
	public List<NOTI_SEARCH_TAB> SearchTabVakues(String p_sbi_no, String p_last_name, Date p_date_frm,
			Date p_date_to, int p_sort_by, String p_sort_tp, String p_all_flg, String P_Short_msg, String p_sent_by, String ivc_noti_env , String p_user_id ) {
		

		
		List<NOTI_SEARCH_TAB> SearchTabValues=new ArrayList<>();
		
		try (Connection connection = dataSource.getConnection()) {
			CallableStatement callableStatement = connection.prepareCall("{call spkg_omnet_wrapper.sp_cbi_noti_srch_msg_query(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
		    callableStatement.registerOutParameter(1, OracleTypes.ARRAY, "TB_CBI_NOTI_SRCH_MSG");
			callableStatement.setString(2, p_sbi_no);
			callableStatement.setString(3, p_last_name);
			callableStatement.setDate(4,  p_date_frm);
			callableStatement.setDate(5,  p_date_to);
			
			callableStatement.setInt(6, p_sort_by);
			callableStatement.setString(7, p_sort_tp);
			callableStatement.setString(8, p_all_flg);
			callableStatement.setString(9, P_Short_msg);
			callableStatement.setString(10, p_sent_by);
			callableStatement.setString(11, ivc_noti_env);
			callableStatement.setString(12, p_user_id);
			callableStatement.execute();
			
			
			Array array = (Array)callableStatement.getArray(1);
			 if (array != null) {
			Object[] arrayData = (Object[]) array.getArray();
			
			for (Object obj : arrayData) {

				if (obj instanceof STRUCT) {
					STRUCT struct = (STRUCT) obj;
                    Object[] attributes = struct.getAttributes();
                    NOTI_SEARCH_TAB NOTI_SEARCH_TABObject=new NOTI_SEARCH_TAB();
                    NOTI_SEARCH_TABObject.setUser_mess_seq_num((BigDecimal) attributes[0]);
                    NOTI_SEARCH_TABObject.setUser_id((String) attributes[1]);
                    NOTI_SEARCH_TABObject.setInst_num((String) attributes[2]);
                    NOTI_SEARCH_TABObject.setScreen_code((String) attributes[3]);
                    NOTI_SEARCH_TABObject.setDate_time_sent((Timestamp) attributes[4]);
                    NOTI_SEARCH_TABObject.setMessage_text((String) attributes[5]);
                    NOTI_SEARCH_TABObject.setSent_by_user_id((String) attributes[6]);
                    NOTI_SEARCH_TABObject.setSent_by_user_name((String) attributes[7]);
                    NOTI_SEARCH_TABObject.setNo_of_days((BigDecimal) attributes[8]);
                    NOTI_SEARCH_TABObject.setRemoved_flag((String) attributes[9]);
                    NOTI_SEARCH_TABObject.setCommit_no((String) attributes[10]);
                    NOTI_SEARCH_TABObject.setSbi_no((String) attributes[11]);
                    NOTI_SEARCH_TABObject.setOffender_last_name((String) attributes[12]);
                    NOTI_SEARCH_TABObject.setOffender_full_name((String) attributes[13]);
                    NOTI_SEARCH_TABObject.setShort_message((String) attributes[14]);
                    NOTI_SEARCH_TABObject.setMessage_description((String) attributes[15]);
                    
                    SearchTabValues.add(NOTI_SEARCH_TABObject);
                    
				}
			}
			
			 }
			callableStatement.close();
		}catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
	        e.printStackTrace();
	    }
		
	return SearchTabValues.isEmpty() ? null : SearchTabValues;
	}
	
	
}
