package com.springBootProject.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.LoginHistory;
import com.springBootProject.Entity.Transactions;
import com.springBootProject.Entity.User;
import com.springBootProject.Repository.AccountRepository;
import com.springBootProject.Repository.LoginHistoryRepository;
import com.springBootProject.Repository.TransactionRepository;
import com.springBootProject.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController2 {
	
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final LoginHistoryRepository loginHistoryRepository;
	private final TransactionRepository transactionRepository;
	
	// ==========================================
    //  NEW: HTML View for Admin Login Portal
    // ==========================================
	
	//----------------- Opens the Admin_login.html page-------------------------------------------------//
    @GetMapping("/admin-login")
    public String showAdminLoginPage() {
        return "admin_login"; 
    }
    
    
    @PostMapping("/admin-login")
    public String handleAdminLogin(@RequestParam String username, @RequestParam String email, @RequestParam String password,
            						HttpSession session,Model model) {

        //--------- TEMPORARY HARDCODE (for now)
        if (username.equals("Admin User") && email.equals("admin1995@gmail.com") && password.equals("admin@123")) {
        	
        	Map<String, String> adminData = new HashMap<>();
        	adminData.put("username", username);
        	adminData.put("email", email);
        	adminData.put("password", password);
        	
            session.setAttribute("admin", adminData);

            return "redirect:/api/admin/admin-dashboard";
            
        } else {
            model.addAttribute("error", "Invalid Admin Credentials");
            
            return "admin_login";
        }
    }
    
    //-------------------- Opens the Admin_dashboard.html(page)--------------------------------------------------//
    @GetMapping("/admin-dashboard")
    public String openAdminDashboard(HttpSession session, Model model) {

        Object adminObj = session.getAttribute("admin");

        if (adminObj == null) {
            return "redirect:/api/admin/admin-login";
        }

        model.addAttribute("admin", adminObj);

        return "admin_dashboard";
    }
    
    //------------------- opens the ViewAllUsers.html(page)-------------------------------------------------------//
    @GetMapping("/admin-users")
    public String viewAllUsers(HttpSession session, Model model) {

        //-------------------Session check
        if (session.getAttribute("admin") == null) {
            return "redirect:/api/admin/admin-login";
        }
        
        // ---------------- FETCH DATA FROM DB
        List<User> users = userRepository.findByRole("ROLE_USER");
        
        //------------------ SEND TO UI
        model.addAttribute("users", users);

        return "viewAllUsers";
    }
    
    //------------------- opens the ViewAllAccounts.html(page)-----------------------------------------------------//
    @GetMapping("/admin-accounts")
    public String viewAllAccounts(HttpSession session, Model model) {

        //------------- Session check
        if (session.getAttribute("admin") == null) {
            return "redirect:/api/admin/admin-login";
        }

        //--------------- Fetch data from DB
        List<Account> accounts = accountRepository.findAll();

        //--------------- Send to UI
        model.addAttribute("accounts", accounts);

        return "viewAllAccounts";
    }
    
    //---------------------- opens the ViewAllLoginHistory.html(page)-----------------------------------------------//
    @GetMapping("/admin-login-history")
    public String viewLoginHistory(@RequestParam(required = false) String fromDate,@RequestParam(required = false) String toDate,
            						HttpSession session,Model model) {

        //--------- Session check
        if (session.getAttribute("admin") == null) {
            return "redirect:/api/admin/admin-login";
        }

        List<LoginHistory> histories;

        //------------- FILTER LOGIC
        if (fromDate != null && toDate != null && !fromDate.isEmpty() && !toDate.isEmpty()) {

            LocalDate start = LocalDate.parse(fromDate);
            LocalDate end = LocalDate.parse(toDate);

            histories = loginHistoryRepository.findByLoginTimeBetween(start.atStartOfDay(), end.atTime(23, 59, 59));
        }
        else {
            //---------- NO FILTER → fetch all
            histories = loginHistoryRepository.findAll();
        }

        //------ SEND DATA TO UI
        model.addAttribute("loginHistories", histories);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "ViewAllLoginHistory";
    }
    
    //---------------------- opens the ViewAllTransactions.html(page)----------------------------------------------------//
    @GetMapping("/admin-viewAllTransactions")
    public String viewAllTransactions(@RequestParam(required = false) String accountNo,@RequestParam(required = false) String fromDate,
            						  @RequestParam(required = false) String toDate,HttpSession session,Model model) {
    	
    	//----------------------- Session check
        if (session.getAttribute("admin") == null) {
            return "redirect:/api/admin/admin-login";
        }

        List<Transactions> transactions;

        //---------------------- FILTER LOGIC
        if ((accountNo != null && !accountNo.isEmpty()) ||
            (fromDate != null && toDate != null && !fromDate.isEmpty() && !toDate.isEmpty())) {

            LocalDateTime start = null;
            LocalDateTime end = null;

            if (fromDate != null && toDate != null && !fromDate.isEmpty() && !toDate.isEmpty()) {
                start = LocalDate.parse(fromDate).atStartOfDay();
                end = LocalDate.parse(toDate).atTime(23, 59, 59);
            }

            transactions = transactionRepository.filterTransactions(accountNo, start, end);

        	} 
        	else {
        	//---------------------- NO FILTER → fetch all
            transactions = transactionRepository.findAll();
        	}
        
        //----------------------- SEND DATA TO UI
        model.addAttribute("transactions", transactions);

        return "ViewAllTransactions";
    }
    


}
