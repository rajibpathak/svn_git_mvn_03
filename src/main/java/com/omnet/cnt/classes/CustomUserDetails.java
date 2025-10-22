	/**
	 * Document: CustomUserDetails.java
	 * Author: Jamal Abraar
	 * Date Created: 09-Sep-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.classes;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User{

    private final String location;
    private final String userId;

    public CustomUserDetails(String userId, String password, 
            boolean enabled, 
            boolean accountNonExpired, 
            boolean credentialsNonExpired, 
            boolean accountNonLocked, 
            Collection<? extends GrantedAuthority> authorities, 
            String location) {
super(userId, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
this.location = location;
this.userId = userId;
}

    public String getLocation() {
        return location;
    }

	public String getUserId() {
		return userId;
	}
	
}
