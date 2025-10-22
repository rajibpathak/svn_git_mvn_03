package com.omnet.cnt.Repository;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.omnet.cnt.Model.Login;


@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
	

	@Query(value = "SELECT * FROM Omnet_Users WHERE UPPER(USER_ID) = UPPER(:userId)", nativeQuery = true)
	Login findByUserId(@Param("userId") String userId);
	       
	 @Query(value = "SELECT spkg_adm_pasw.sf_password_Casesensitive(:userId, :password) FROM DUAL", nativeQuery = true)
	    String callPasswordCaseSensitiveFunction(@Param("userId") String userId, @Param("password") String password);
	    
	    @Query(value = "SELECT  spkg_adm_pasw.hash_password(:password) FROM dual", nativeQuery = true)
	    String hashPassword(@Param("password") String password);
	    
	/*
	 * @Query(value =
	 * "SELECT * FROM login l WHERE UPPER(l.user_id) = UPPER(:userId)", nativeQuery
	 * = true) Login findByUserId( String userId);
	 */
    
	/*
	 * @Query(value =
	 * "SELECT * FROM Omnet_Users l  WHERE UPPER(user_name) = UPPER(:username) AND current_password = :currentPassword"
	 * , nativeQuery = true) List<Login>
	 * findByUserNameAndPassword(@Param("username") String
	 * username,@Param("currentPassword") String currentPassword);
	 */
    
    @Query(value = "SELECT user_status, PASSWORD_EXPIRED_DATE FROM Omnet_Users WHERE user_name = :username AND current_password = :currentPassword", nativeQuery = true)
	Map<String, Object> findUserStatusAndExpirationByUsername(@Param("username") String username,@Param("currentPassword") String currentPassword);
    
    @Query(value ="SELECT * FROM PASSWORD_EXPIRED_DATE FROM Omnet_Users WHERE user_name = :username", nativeQuery = true)
    String findpasswordExpiryDate(@Param("username" )String username);
    
    @Query("SELECT DISTINCT ur.roleName FROM UserAccess ua JOIN UserRoles ur ON ua.id.roleSeqNum = ur.roleSeqNum WHERE ua.id.userId = :userId")
    List<String> findRoleNamesByUserId(String userId);
	
}