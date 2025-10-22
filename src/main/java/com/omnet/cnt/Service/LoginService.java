package com.omnet.cnt.Service;

import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import com.omnet.cnt.Model.Login;
import com.omnet.cnt.Model.Users;
import com.omnet.cnt.Repository.LoginRepository;
import com.omnet.cnt.Repository.UserRepository;

@Service
public class LoginService {
	@Autowired
	private LoginRepository loginrepository;
	@Autowired
    private UserRepository userRepository;

	public boolean validateUser(String userId, String currentPassword) {
		if (userId == null || currentPassword == null) {
			return false;
		}
		Login login = loginrepository.findByUserId(userId);
		if (login != null && currentPassword.equals(login.getCurrentPassword())) {
			return true;
		}
		return false;
	}

	public String getUserIdByUsername(String userId) {
		Login user =loginrepository.findByUserId(userId);
		if (user != null) {
			return user.getUserId();
		}
		return null;
	}
	
	 public String getHashedPassword(String plainPassword) {
	        return loginrepository.hashPassword(plainPassword);
	    }
	  public String validatePassword(String userId, String password) {
		  String encryptedpassword =loginrepository.callPasswordCaseSensitiveFunction(userId, password);
			 System.out.println("encryptedpassword"+encryptedpassword); 
	        return encryptedpassword;
	    }
	
    public void updateEncryptedPassword(String userId ,String encryptedPassword) {
    	Optional<Users> optionalUser = userRepository.findByUserId(userId);
    	System.out.println("optionalUserfor"+optionalUser);
    	System.out.println("optionalUserforservice"+encryptedPassword);
    	Users user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found"));
    	
    	user.setEncryptedPassword(encryptedPassword);
    	userRepository.save(user);
    	
    }
    
    @Transactional
    public void updatePasswordExpiryDate(String userId) {
        userRepository.updatePasswordExpiryDate(userId);
        System.out.println("Password expiration date updated for user: " + userId);
    }

    @Transactional
    public void updateCurrentPassword(String userId, String newPassword) throws ParseException {
        Optional<Users> optionalUser = userRepository.findByUserId(userId);
        Users user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found"));
        System.out.println("yueguaegf"+newPassword);
        user.setCurrentPassword(newPassword);
        userRepository.updatePasswordExpiryDate(userId);
        userRepository.save(user);
    }
    
  
}
