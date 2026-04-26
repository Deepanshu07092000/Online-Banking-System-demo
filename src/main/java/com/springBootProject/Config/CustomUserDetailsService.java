package com.springBootProject.Config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springBootProject.Entity.User;
import com.springBootProject.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	//----------------- Inject UserRepository to fetch user from DB
    private final UserRepository userRepository;
    
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		 System.out.println("🔥 Loading user: " + email);  // 👈 ADD THIS LINE
		
		 //----------------- Fetch user from database using email
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        
        // Convert User → CustomUserDetails (Spring Security format)
        return new CustomUserDetails(user);
	}

}
