package com.omnet.cnt.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.NotiUserName;
import com.omnet.cnt.Model.Users;

public interface UserRepository extends JpaRepository<Users, String> {

	List<Users> findAllByUserId(String userId);

	/*
	 * List<Users> findAllByUserNameAndEncryptedPasswordAndUserStatus(String
	 * userName, String encryptedPassword, String userStatus);
	 */
	/*
	 * @Query(nativeQuery = true,value =
	 * "SELECT * FROM Omnet_Users u WHERE u.user_name = ?1") List<Users>
	 * findUserByName(String userName);
	 */
	
	 Optional<Users> findByUserId(String userId);
	 
	 @Query(value = "SELECT Inst_Name FROM Omnet_Users a JOIN INSTITUTION b ON a.PRIMARY_INST_NUM = b.INST_NUM WHERE a.USER_ID = :userId", nativeQuery = true)
	 List<String> findInstNameByUserId(@Param("userId") String userId);
	 
	 @Query(value = "SELECT a.INST_NUM FROM Omnet_Users a WHERE a.USER_ID = :userId", nativeQuery = true)
	 String findInstNumByUserId(@Param("userId") String userId);
	 
	 @Query(value = "SELECT b.Inst_Name, a.INST_NUM " +
             "FROM Omnet_Users a JOIN INSTITUTION b ON a.INST_NUM = b.INST_NUM " +
             "WHERE UPPER(a.USER_ID) = UPPER(:userId)", nativeQuery = true)
	 List<Object[]> findInstNameAndPrimaryInstNumByUserId(@Param("userId") String userId);
	
	@Query(nativeQuery = true, value = "SELECT u.user_email_address FROM Omnet_users u WHERE UPPER(u.user_id) = UPPER(:userId)")
	String findEmailByUserId(String userId);
	
	@Query(value = "SELECT CURRENT_PASSWORD FROM OMNET_USERS u WHERE u.user_id = ?", nativeQuery = true)
	String findCurrentPasswordByUserId(String userId);
	
	/*
	 * @Procedure(value = "InsUpd_Omnet_User") List<Users> InsupdOmnetUser(String
	 * userName, String userFName, String userLName, String userMName, String
	 * userSName, String userPhone, String userEmail, String userStatus, String
	 * userCaseload, String primaryInst, String userDistOff, String userUnitNum,
	 * String userTitle, String userParOfcrNum, String supUserId, String
	 * userPrimaryMenu, String userEmpId, String maxNumSessions, String instNum,
	 * String lastLoggedDttm, String loggedinUserid);
	 */
  
	@Query(nativeQuery = true, value = "SELECT * FROM omnet_users u WHERE UPPER(u.user_id) = UPPER(?1)")
	Users findByUsersid(@Param("userId") String user_Id);
	
	
	Users findUserNameByUserId(String userId);
    @Query(value = "SELECT * FROM Omnet_Users WHERE PASSWORD_EXPIRED_DATE = :expiryDate", nativeQuery = true)
    List<Users> findUsersByPasswordExpiryDate(@Param("expiryDate") LocalDate expiryDate);

    @Query("SELECT CONCAT(COALESCE(u.userFirstName, ''), ' ', " +
 	       "COALESCE(u.userMidName, ''), ' ', " +
 	       "COALESCE(u.userLastName, ''), ' ', " +
 	       "COALESCE(u.userSuffixName, '')) " +
 	       "FROM Users u WHERE u.userId = :userId")
    Optional<String> findConcatenatedNameByUserId(@Param("userId") String userId);
    
    @Query("SELECT CONCAT(COALESCE(u.userLastName, ''), ' ', " +
 	       "COALESCE(u.userFirstName, ''), ' ', " +
 	       "COALESCE(u.userMidName, ''), ' ', " +
 	       "COALESCE(u.userSuffixName, '')) " +
 	       "FROM Users u WHERE u.userId = :userId")
 	Optional<String> findConcatenatedNameByLFMS(@Param("userId") String userId);
    
    @Query(value = "SELECT a.user_id, " +
            "       a.user_last_name || ', ' || " +
            "       a.user_first_name || " +
            "       DECODE(a.user_mid_name, NULL, '', ' ' || a.user_mid_name || '.') || " +
            "       DECODE(a.user_suffix_name, NULL, '', ' ' || a.user_suffix_name) AS user_full_name " +
            "FROM omnet_users a " +
            "WHERE a.primary_inst_num = sf_get_user_inst_num(:userId) " +
            "AND sf_get_cmref_user_profile_yn(a.user_id, 'MOV', 'AUTH_USERS') = 'Y' " +
            "AND a.user_status = 'A' " +
            "ORDER BY a.user_last_name", 
    nativeQuery = true)
List<Object[]> findUsersWithFullName(String userId);

	@Query("""
        SELECT new Users(n.userId, n.userFirstName, n.userLastName, n.userMidName, n.userSuffixName) FROM Users n
        WHERE LOWER(n.userFirstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(n.userLastName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(n.userMidName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(n.userSuffixName) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
	/*@Query(value="SELECT n.user_id, n.user_first_name, n.user_last_name, n.user_mid_name, n.user_suffix_name FROM omnet_users n " +
	        "WHERE LOWER(n.user_first_name) LIKE LOWER('%' || :search || '%') " +
	           "OR LOWER(n.user_last_name) LIKE LOWER('%' || :search || '%') " +
	           "OR LOWER(n.user_mid_name) LIKE LOWER('%' || :search || '%') " +
	           "OR LOWER(n.user_suffix_name) LIKE LOWER('%' || :search || '%')", nativeQuery=true)*/
	Page<Users> searchAllFields(@Param("search") String search, Pageable pageable);


@Modifying
@Query("UPDATE Users u SET u.passwordExpiredDate = SYSDATE + 30 WHERE u.userId = :userId")
void updatePasswordExpiryDate(@Param("userId") String userId);


}


