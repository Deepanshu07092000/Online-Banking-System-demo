package com.springBootProject.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.User;
import com.springBootProject.Repository.AccountRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class DashboardController {
	
	private final AccountRepository accountRepository;
	
	
	//--------- Temporary API to open the DashBoard page---------------//
		@GetMapping("/dashboard")
		public String showDashBoard(HttpSession session,Model model) {
			
			//--------- Get logged-in user from session
		    User user = (User) session.getAttribute("loggedInUser");
		    
		    if (user == null) {
		        return "redirect:/api/auth/do-login"; // not logged in
		    }
		    
		    //---------- Fetch account using userId
		    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found...."));
		    
		    //----------- Send data to UI
		    model.addAttribute("user", user);
		    model.addAttribute("account", account);
		    
			return "dashboard";
		}

}
