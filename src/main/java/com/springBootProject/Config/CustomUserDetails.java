package com.springBootProject.Config;

import java.util.Collection;
import java.util.Collections;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springBootProject.Entity.User;

/*
 * Spring Security,authentication ---> standard interface, Entity X
 * Convert User Entity -> Spring Security User
 */
public class CustomUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private final User user;
	
	 //-------- Constructor injection
    public CustomUserDetails(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return this.user;
    }
	
	//---------------- Convert role into Spring Security authority------------------------------//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
            new SimpleGrantedAuthority( user.getRole().startsWith("ROLE_") ? user.getRole() : "ROLE_" + user.getRole())
        );
    }
	
	//--------------- Return encoded password
	@Override
	public String getPassword() {
		 return user.getPassword();
	}
	
	//--------------- Use email as userName
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	//-------------- Account not expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    //--------------- Account not locked (we use status instead, Automatically blocks login if status = BLOCKED)
    @Override
    public boolean isAccountNonLocked() {
        return !"BLOCKED".equalsIgnoreCase(user.getStatus());
    }
    
    //--------------- Credentials not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    //------------- Account enabled(only ACTIVE users can login)
    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(user.getStatus());
    }


}
