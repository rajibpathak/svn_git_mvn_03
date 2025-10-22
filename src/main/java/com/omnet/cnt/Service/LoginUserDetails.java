/*
 * package com.omnet.cnt.Service;
 * 
 * import java.util.Collection; import java.util.Collections;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.core.GrantedAuthority; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.stereotype.Component;
 * 
 * import com.omnet.cnt.Model.Login;
 * 
 * 
 * @Component public class LoginUserDetails implements UserDetails {
 * 
 * @Autowired private Login login;
 * 
 * 
 * @Autowired public LoginUserDetails(Login login) { this.login = login; }
 * 
 * @Override public Collection<? extends GrantedAuthority> getAuthorities() { //
 * TODO Auto-generated method stub return Collections.singleton(new
 * SimpleGrantedAuthority(login.getUserName())); }
 * 
 * @Override public String getPassword() { // TODO Auto-generated method stub
 * return login.getcurrentPassword(); }
 * 
 * @Override public String getUsername() { // TODO Auto-generated method stub
 * return login.getUserName(); }
 * 
 * @Override public boolean isAccountNonExpired() { // TODO Auto-generated
 * method stub return true; }
 * 
 * @Override public boolean isAccountNonLocked() { // TODO Auto-generated method
 * stub return true; }
 * 
 * @Override public boolean isCredentialsNonExpired() { // TODO Auto-generated
 * method stub return true; }
 * 
 * @Override public boolean isEnabled() { // TODO Auto-generated method stub
 * return true; }
 * 
 * }
 */