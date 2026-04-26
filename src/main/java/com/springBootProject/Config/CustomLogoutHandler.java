package com.springBootProject.Config;

import java.time.LocalDateTime;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.springBootProject.Entity.LoginHistory;
import com.springBootProject.Repository.LoginHistoryRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler{
	
	private final LoginHistoryRepository loginHistoryRepository;
	
	//-------- Runs this automatically(when the user clicks logout button)--------------------//
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
			@Nullable Authentication authentication) {
		
		//-----It gets the session--------------------------------------------//
		HttpSession session = request.getSession(false);
		if(session != null) {
			
			//-------It fetches the historyId(created during the login)-------------//
			Long historyId = (Long) session.getAttribute("historyId");
			
			if(historyId != null) {
				
				//------- It fetches the DB record----------------------------------//
				LoginHistory history = loginHistoryRepository.findById(historyId).orElse(null);
				
				if(history != null) {
					
					//------- It updates the logout time----------------------------//
					history.setLogoutTime(LocalDateTime.now());
					loginHistoryRepository.save(history);
				}
			}
		}
		
	}

}
