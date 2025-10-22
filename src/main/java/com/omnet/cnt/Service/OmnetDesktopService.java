package com.omnet.cnt.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omnet.cnt.Model.NotificationOmnetDesktop;
import com.omnet.cnt.Model.OmnetDesktop;
import com.omnet.cnt.Model.RecentScreen;
import com.omnet.cnt.Repository.NotificationRepository;
import com.omnet.cnt.Repository.OmnetDesktopRepository;
import com.omnet.cnt.Repository.RecentScreenRepository;
import java.util.Base64;
@Service
public class OmnetDesktopService {

	@Autowired
	private OmnetDesktopRepository OmnetDesk;
	
	@Autowired
	private NotificationRepository notify;
	
	@Autowired
	private RecentScreenRepository ras;
	@Autowired
    private DataSource dataSource;
	

	public String GetcountBed(String p_inst_num) {
		return OmnetDesk.Count(p_inst_num);
	}
	
	public String GetPopulation(String p_inst_num) {
		return OmnetDesk.PopulationCount(p_inst_num);
	}
	
	public String GetAdmission(String p_inst_num) {
		return OmnetDesk.AdmissionCount(p_inst_num);
	}
	
	public String GetReleaseCount(String p_inst_num) {
		return OmnetDesk.ReleaseCount(p_inst_num);
	}
	
	public List<OmnetDesktop> Deskinfo(String userId) {
	    List<OmnetDesktop> deskList = OmnetDesk.getOmnetDesktop(userId);
	    return deskList;
	}

	
	public List<NotificationOmnetDesktop> getnotification(String userId){
		System.out.println("userId="+userId);
		List<NotificationOmnetDesktop> n=notify.getnotify(userId);
		return n;
	}
	
	public void Notifyupdate( String userId,String user_mess_seq_num) {
		notify.updatevalue(userId,user_mess_seq_num);
		
	}

	
	public List<RecentScreen> AccessedScreen(String userId){
		List<RecentScreen> RS=ras.getRecentScreen(userId);
		return RS;
	}
	
	public void searchValueInsert(String p_screen_code,String p_screen_name,String p_sbi_no,String p_commit_no, String p_user_id) {
		String procedureSql = "{call sp_doc_audit_view_dtl(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		String p_access_type="V";
		 try (
		            Connection conn = dataSource.getConnection();
		            CallableStatement stmt = conn.prepareCall(procedureSql)
		        ){
	            stmt.setString(1, p_screen_code);      
	            stmt.setString(2, p_screen_name);    
	            stmt.setNull(3, Types.VARCHAR);      
	            stmt.setNull(4, Types.VARCHAR);      
	            stmt.setString(5, p_access_type);        
	            stmt.setString(6, p_sbi_no);      
	            stmt.setString(7, p_commit_no);      
	            
	            stmt.setNull(8, Types.VARCHAR);
	            stmt.setNull(9, Types.VARCHAR);
	            stmt.setNull(10, Types.VARCHAR);

	            stmt.setNull(11, Types.VARCHAR);
	            stmt.setNull(12, Types.VARCHAR);
	            stmt.setNull(13, Types.VARCHAR);

	            stmt.setNull(14, Types.VARCHAR);
	            stmt.setNull(15, Types.VARCHAR);
	            stmt.setNull(16, Types.VARCHAR);

	            stmt.setNull(17, Types.VARCHAR);
	            stmt.setNull(18, Types.VARCHAR);
	            stmt.setNull(19, Types.VARCHAR);

	            stmt.setNull(20, Types.VARCHAR);
	            stmt.setNull(21, Types.VARCHAR);
	            stmt.setNull(22, Types.VARCHAR);

	            stmt.setNull(23, Types.VARCHAR);
	            stmt.setNull(24, Types.VARCHAR);
	            stmt.setNull(25, Types.VARCHAR);

	            stmt.setNull(26, Types.VARCHAR);
	            stmt.setNull(27, Types.VARCHAR);
	            stmt.setNull(28, Types.VARCHAR);

	            stmt.setNull(29, Types.VARCHAR);
	            stmt.setNull(30, Types.VARCHAR);
	            stmt.setNull(31, Types.VARCHAR);

	            stmt.setNull(32, Types.VARCHAR);
	            stmt.setNull(33, Types.VARCHAR);
	            stmt.setNull(34, Types.VARCHAR);

	            stmt.setNull(35, Types.VARCHAR);
	            stmt.setNull(36, Types.VARCHAR);
	            stmt.setNull(37, Types.VARCHAR);

	            stmt.setString(38, p_user_id);      
	            stmt.setNull(39, Types.VARCHAR);    

	            // Execute the stored procedure
	            stmt.execute();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	public List<Object[]> getMenuValue() {
		return OmnetDesk.MenuSetupValue();
	}
	
}
