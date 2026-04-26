package com.springBootProject.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springBootProject.TransactionService;
import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.Transactions;
import com.springBootProject.Entity.User;
import com.springBootProject.Repository.AccountRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountRepository accountRepository;
	

	
	//------------DEPOSIT(page will open)------------------------------------//
	@GetMapping("/deposit")
	public String showDepositPage(HttpSession session,Model model) {
		
		//-------- Get logged-in user from session
	    User user = (User) session.getAttribute("loggedInUser");

	    if (user == null) {
	        return "redirect:/api/auth/do-login";
	    }
	    
	    //--------- Fetch account using userId
	    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));
	    
	    //---------- Send account to HTML
	    model.addAttribute("account", account);
	    
		return "deposit";
	}
	
	//----------- Handle Deposit feature---------------------------------------//
	@PostMapping("/deposit")
	public String deposit(@RequestParam("accountNumber")  String accountNumber,     	//@RequestParam(Frontend --> Backend) --> Takes the value from HTML form and stores it into the variable(data is coming from form fields)
						  @RequestParam("amount") Double amount,		   				// Model(Backend--> Frontend) --> Sends the data from controller to HTML page
						  HttpSession session,
						  Model model) {
		
		//--- Call service layer(deposit method--> updates the balance , save the transaction)----------------//
		try {
		transactionService.deposit(accountNumber, amount);
		
		model.addAttribute("success","Amount deposited successfully..");
		} catch(Exception e) {
			model.addAttribute("error",e.getMessage());
		}
		
		//---- IMPORTANT: Send account again
	    User user = (User) session.getAttribute("loggedInUser");

	    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));

	    model.addAttribute("account", account);
	    
		return "deposit";
	}
	
	//----------- WITHDRAW(page will open)----------------------------------------//
	@GetMapping("/withdraw")
	public String showWithdrawPage(HttpSession session,Model model) {
		
		//-------- Get logged-in user from session
	    User user = (User) session.getAttribute("loggedInUser");

	    if (user == null) {
	        return "redirect:/api/auth/do-login";
	    }
	    
	    //--------- Fetch account using userId
	    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));
	    
	    //---------- Send account to HTML
	    model.addAttribute("account", account);
	    
		return "withdraw";
	}
	
	//------------- Handle Withdraw feature----------------------------------------//
	@PostMapping("/withdraw")
	public String withdraw(@RequestParam("accountNumber") String accountNumber,
							@RequestParam("amount") Double amount,
							HttpSession session,
							Model model) {
		
		try {
		transactionService.withdraw(accountNumber, amount);
		
		model.addAttribute("success","Amount withdrawn successfully...");
		} catch(Exception e) {
			model.addAttribute("error",e.getMessage());
		}
		
		//---------------Send account again
	    User user = (User) session.getAttribute("loggedInUser");

	    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));

	    model.addAttribute("account", account);
	    
		return "withdraw";
	}
	
	//------------TRANSFER(page will open)------------------------------------//
		@GetMapping("/transfer")
		public String showTransferPage(HttpSession session,Model model) {
			
			//-------- Get logged-in user from session
		    User user = (User) session.getAttribute("loggedInUser");

		    if (user == null) {
		        return "redirect:/api/auth/do-login";
		    }
		    
		    //--------- Fetch account using userId
		    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));
		    
		    //---------- Send account to HTML
		    model.addAttribute("account", account);
		    
			return "transfer";
		}
		
		
	//------------- Handle transfer feature----------------------------------------//
		@PostMapping("/transfer")
		public String transfer(@RequestParam("fromAccountNumber") String fromAccountNumber,
							   @RequestParam("toAccountNumber")  String toAccountNumber,
							   @RequestParam("amount") Double amount,
							   HttpSession session,
								Model model) {
			
			try {
			transactionService.transfer(fromAccountNumber,toAccountNumber,amount);
			
			model.addAttribute("success","Amount transferred successfully...");
			} catch(Exception e) {
				
				model.addAttribute("error",e.getMessage());
			}
			
			//---------------Send account again
		    User user = (User) session.getAttribute("loggedInUser");

		    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));

		    model.addAttribute("account", account);
		    
			return "transfer";
		}
		
	
	//-------------- TRANSACTION HISTORY(page will open)-----------------------------//
		@GetMapping("/history")
		public String getTransactionHistory(HttpSession session,
											Model model) {
			
			//------------------- Get logged-in user from session
		    User user = (User) session.getAttribute("loggedInUser");

		    if (user == null) {
		        return "redirect:/api/auth/do-login";
		    }
		    
		    //------------------- Fetch account using userId
		    Account account = accountRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Account not found"));
		    String accountNumber = account.getAccountNumber();
		    
		    //-------------------- Get transactions
		    List<Transactions> transactions = transactionService.getTransactionHistory(accountNumber);
		    
		    //---------------------- Mask account number (VERY IMPORTANT UI FEATURE)
		    String maskedAccountNo = "XXXXXX" + account.getAccountNumber().substring(account.getAccountNumber().length() - 4);
		    
		    //----------------------- Send data to UI
		    model.addAttribute("transactions", transactions);
		    model.addAttribute("userName", user.getName());
		    model.addAttribute("maskedAccountNo", maskedAccountNo);
			
			return "transaction_History";
		}
		
	//----------- AJAX Endpoint to fetch account holder name -----------------------------------//
		@GetMapping("/getName/{accountNumber}")
		@ResponseBody
		public String getAccountName(@PathVariable String accountNumber) {
			
		    // ------Call the service method--------------------------------//
		    String name = transactionService.getAccountHolderName(accountNumber); 
		    
		    return name; 
		}

}
