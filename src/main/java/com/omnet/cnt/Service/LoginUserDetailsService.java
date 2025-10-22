/*
Document   : Login User Details Service Layer
Author     : Jamal Abraar
last update: 16/04/2024
*/

package com.omnet.cnt.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.omnet.cnt.Model.Login;
import com.omnet.cnt.Repository.LoginRepository;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.classes.CustomUserDetails;


@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginRepository loginRepository;
    
    
    @Autowired
    private UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(LoginUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UsernameNotFoundException("User ID is invalid: " + userId);
        }
        Login user = loginRepository.findByUserId(userId);
        System.out.println("userdetail"+user);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userId);
        }
      
        boolean accountNonExpired = "A".equals(user.getUserstatus());
        boolean credentialsNonExpired = checkCredentialsExpiration(user);	
		String location = userRepo.findInstNumByUserId(userId);

        List<String> roleNames = loginRepository.findRoleNamesByUserId(user.getUserId());

        logger.info("Authorities:");
        for (String roleName : roleNames) {
            logger.info(roleName);
        }
        List<GrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());       
        return new CustomUserDetails(
                user.getUserId(),               
                user.getEncryptedPassword(),    
                accountNonExpired, 
                accountNonExpired, 
                credentialsNonExpired, 
                true, 
                authorities, 
                location
        );
    }
    
    private boolean checkCredentialsExpiration(Login user) {
        if (user.getPasswordExpiredDate() == null) {
            return false;
        } else {
                Date expirationDate = user.getPasswordExpiredDate();
                LocalDate localExpirationDate = convertToLocalDate(expirationDate);
                LocalDate today = LocalDate.now();
                return !localExpirationDate.isBefore(today) && !localExpirationDate.isEqual(today); 
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }
   

}


