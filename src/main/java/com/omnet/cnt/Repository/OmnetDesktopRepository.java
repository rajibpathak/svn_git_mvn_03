package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.OmnetDesktop;

@Repository
@EnableJpaRepositories	
public interface OmnetDesktopRepository extends JpaRepository<OmnetDesktop, String>{
	

	@Query(nativeQuery = true,value ="SELECT "
			+ "    sbi_no,inmate_full_name,screen_name,access_date_time, access_time, "
			+ "    (SELECT image FROM inmate_mugshot_image f WHERE f.sbi_no = e.sbi_no AND ROWNUM = 1) AS mug_shot, "
			+ "    Name_Prefix,Screen_url, "
			+ "    ROW_NUMBER() OVER (ORDER BY access_date_time DESC) AS ID "
			+ "    FROM ( "
			+ "    SELECT "
			+ "        a.sbi_no, "
			+ "        b.inmate_full_name, "
			+ "        CONCAT(SUBSTR(b.Commit_LName, 1, 1), SUBSTR(b.Commit_FName, 1, 1)) AS Name_Prefix, "
			+ "        a.screen_code, "
			+ "        a.screen_name, "
			+ "        a.user_id, "
			+ "        sf_get_user_name(a.user_id) AS user_name, "
			+ "        a.access_date_time, "
			+ "        TO_CHAR(a.access_date_time, 'HH24:MI:SS') AS access_time, "
			+ "        ROW_NUMBER() OVER (PARTITION BY a.sbi_no ORDER BY a.access_date_time DESC) AS rn, "
			+ "        d.Screen_url "
			+ "    FROM "
			+ "        doc_audit_dtl a "
			+ "        JOIN inmate b ON a.commit_no = b.commit_no "
			+ "        JOIN Omnet_Screens d ON d.Screen_Code = a.screen_code "
			+ "    WHERE "
			+ "        a.user_id = :userId "
			+ ") e "
			+ "WHERE rn = 1 "
			+ "ORDER BY access_date_time DESC "
			+ "FETCH FIRST 10 ROWS ONLY")
	        List<OmnetDesktop> getOmnetDesktop(String userId);
	
	
	@Query(nativeQuery = true,value =" select count(*)  "
			+ "  from inmate_bed_stay ibs, "
			+ "       inst_bed_rt ibr \r\n"
			+ " where ibs.inst_num = ibr.inst_num  "
			+ "   and ibs.bed_no = ibr.bed_no "
			+ "   and ibr.status = 'A' "
			+ "   and ibr.bed_status = 'O' "
			+ "   and ibs.stay_end_date is null  "
			+ "   and trunc(ibs.stay_start_date) = to_date(to_char(sysdate,'mm/dd/yyyy'),'mm/dd/yyyy') "
			+ "   and ibs.inst_num = :p_inst_num")
	String  Count(String p_inst_num);
	
	
	@Query(nativeQuery = true,value ="select count(ids.commit_no) "
			+ "          from inmate_doc_ids ids, "
			+ "               sentence_oa_rel_fr_levels oa, "
			+ "               inmate i "
			+ "         where ids.status != 'I' "
			+ "           and doc_id_type = 'P' "
			+ "           and ids.commit_no = i.commit_no "
			+ "           and ids.doc_id  = oa.doc_id(+) "
			+ "           and ids.commit_no = oa.commit_no(+) "
			+ "           and ids.inst_num  = :p_inst_num")
	String PopulationCount(String p_inst_num);
	
	
	@Query(nativeQuery = true,value ="  select count(*) "
			+ "  from inmate inm, "
			+ "       outer_control_log ocl "
			+ " where inm.commit_no = ocl.commit_no "
			+ "   and inm.current_inst_num = ocl.inst_num "
			+ "   and ocl.in_out_ind = 'I'  "
			+ "   and nvl(ocl.entry_exit_comments,'*') != 'CHANGEOVER' "
			+ "   and trunc(ocl.entry_exit_date) = to_date(to_char(sysdate,'mm/dd/yyyy'),'mm/dd/yyyy') "
			+ "   and (ocl.admiss_release_code not in ('A500','A810','A820','A830','A840','A850','A920') "
			+ "     or \r\n"
			+ "       (ocl.admiss_release_code = 'A500' "
			+ "        and (nvl(ocl.destination,'~') not in ('01','02','03','04','05','06','09','10','11','12','17') "
			+ "          or ocl.entry_exit_comments = 'Intial Load'))) "
			+ "   and ocl.inst_num = :p_inst_num"
			+ "   ")
	String AdmissionCount(String p_inst_num);
	
	@Query(nativeQuery = true,value ="select count(*) "
			+ "  from inmate inm, "
			+ "       outer_control_log ocl "
			+ " where inm.commit_no = ocl.commit_no "
			+ "   and inm.current_inst_num = ocl.inst_num "
			+ "   and ocl.in_out_ind = 'O'  "
			+ "   and nvl(ocl.entry_exit_comments,'*') != 'CHANGEOVER' "
			+ "   and trunc(ocl.entry_exit_date) = to_date(to_char(sysdate,'mm/dd/yyyy'),'mm/dd/yyyy') "
			+ "   and (ocl.admiss_release_code not in ('R500','R810','R820','R830','R840','R850') "
			+ "     or "
			+ "       (ocl.admiss_release_code = 'R500' and ocl.destination not in ('01','02','03','04','05','06','09','10','11','12','17'))) "
			+ "   and ocl.inst_num = :p_inst_num")
	String ReleaseCount(String p_inst_num);
	
	@Query(nativeQuery = true,value ="SELECT \r\n"
			+ "    m.menu_code,\r\n"
			+ "    m.menu_name,\r\n"
			+ "    m.status AS menu_status,\r\n"
			+ "    s.sub_menu_code,\r\n"
			+ "    s.sub_menu_name,\r\n"
			+ "    s.status AS sub_menu_status\r\n"
			+ "FROM \r\n"
			+ "    omnet_menu m\r\n"
			+ "JOIN \r\n"
			+ "    omnet_sub_menu s \r\n"
			+ "    ON m.menu_code = s.menu_code\r\n"
			+ "\r\n"
			+ "ORDER BY \r\n"
			+ "    m.menu_code, s.sub_menu_code")
	List<Object[]> MenuSetupValue();
	
}
