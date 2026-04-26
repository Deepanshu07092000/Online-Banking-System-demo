package com.springBootProject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration										// tells Spring this class contain security setup
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomLogoutHandler customLogoutHandler;
	private final CustomUserDetailsService customUserDetailsService;
	
	//--------- PasswordEncoder Bean----------------------------//
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//-------------- UserDetailsService Bean-------------------------//
	@Bean
	public UserDetailsService userDetailsService() {
	    return customUserDetailsService;
	}
	
	
	//---------- Security Configuration---------------------------//
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

	    httpSecurity
	        .csrf(csrf -> csrf.disable())				//Disables CSRF protection we are not using Spring default form we are using HTML custom forms //
	        .userDetailsService(customUserDetailsService)
	        .authorizeHttpRequests(auth -> auth         // Defines which URLs are public and which need login //
	            .requestMatchers(       
	            			"/api/auth/**",
	            			"/api/transaction/**",
	            			"/api/admin/admin-login",
	            			"/api/admin/admin-dashboard",
	            			"/api/admin/admin-users"
	            ).permitAll()
	            
	            //------------- Admin only
	            .requestMatchers("/api/admin/**").hasRole("ADMIN")
//	            
//	            // -------------- User only
//	            .requestMatchers("/api/user/**").hasRole("USER")
	            
	            //---------------- Everything else requires authentication
	            .anyRequest().authenticated()
	        )
	        
	        .httpBasic(org.springframework.security.config.Customizer.withDefaults())

	        //------ KEEP FORM LOGIN DISABLED (you are using custom login)
	        .formLogin(form -> form.disable())		// Disables Spring default login system because we have created our own login controller
	    
	    //----------- ADD LOGOUT CONFIG
        .logout(logout -> logout
            .logoutUrl("/logout")                         // matches your form action(URL triggered when user clicks logout)
            .addLogoutHandler(customLogoutHandler)
            .logoutSuccessUrl("/api/auth/do-login")   	 // where to go after logout
            .invalidateHttpSession(true)                 // clear session
            .deleteCookies("JSESSIONID")                 // remove session cookie
            .permitAll()
        );

	    return httpSecurity.build();
	}
}
