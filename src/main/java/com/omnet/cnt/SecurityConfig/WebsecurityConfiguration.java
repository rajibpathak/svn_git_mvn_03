/*
Document   : Spring Security Configuration
Author     : Jamal Abraar
last update: 03/05/2024
*/
package com.omnet.cnt.SecurityConfig;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebsecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .authorizeRequests()
	         .antMatchers("/css/**", "/img/**", "/js/**","/img/backgrounds/**","/**/*.json","/resources/**","/media/**").permitAll()
	         .antMatchers("/clientname","/clientlogo","/OmnetLogo").permitAll()//Client Api calls
	         .antMatchers("/LoginApi/**").permitAll()//Login Api/s
	         .antMatchers("/LoginValidation-**","/ForgotPassword","/PasswordChange-**").permitAll()
	         .anyRequest().authenticated()
	        .and()
	        .formLogin()
	            .loginPage("/") 
	            .permitAll()
	            .and()
	         .logout()
	            .logoutUrl("/logout") 
	            .logoutSuccessUrl("/")
	            .invalidateHttpSession(true) 
	            .deleteCookies("JSESSIONID") 
	            .permitAll() 
	            .and()
	        .sessionManagement()
	            .sessionAuthenticationErrorUrl("/") 
	            .sessionFixation().migrateSession()
	            .invalidSessionUrl("/") 
	            .maximumSessions(1) 
	            .expiredUrl("/") 
	            .and()
	        .and()
	        .csrf().disable(); 
	}
}
