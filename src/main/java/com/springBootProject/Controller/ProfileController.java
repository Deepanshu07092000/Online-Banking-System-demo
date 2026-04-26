package com.springBootProject.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.LoginHistory;
import com.springBootProject.Entity.User;
import com.springBootProject.Repository.AccountRepository;
import com.springBootProject.Repository.LoginHistoryRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ProfileController {
	
	 private final AccountRepository accountRepository;
	 private final LoginHistoryRepository loginHistoryRepository;
	
	
	//---------- Open the Profile Info page(User login into it)--------------------------------//
		@GetMapping("/profile")
		public String showProfile(HttpSession session, Model model) {

		    //------------- Get logged-in user from session
		    User user = (User) session.getAttribute("loggedInUser");

		    if (user == null) {
		        return "redirect:/api/auth/do-login";
		    }

		    //------------- Fetch account from DB
		    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));
		    
		    //------------- Fetch LAST LOGIN
		    LoginHistory latestLogin = loginHistoryRepository.findTopByUserIdAndLogoutTimeIsNotNullOrderByLoginTimeDesc(user.getId()).orElse(null);

		    //-------------- Send objects to UI
		    model.addAttribute("user", user);
		    model.addAttribute("account", account);
		    
		    //------------- Send last login
		    if (latestLogin != null) {
		        model.addAttribute("lastLogin", latestLogin.getLoginTime());
		    }

		    return "ProfileInfo";
		}

}
