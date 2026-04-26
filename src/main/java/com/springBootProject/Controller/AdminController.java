package com.springBootProject.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.LoginHistory;
import com.springBootProject.Entity.Transactions;
import com.springBootProject.Entity.User;

import com.springBootProject.Repository.AccountRepository;
import com.springBootProject.Repository.LoginHistoryRepository;
import com.springBootProject.Repository.TransactionRepository;
import com.springBootProject.Repository.UserRepository;


import lombok.RequiredArgsConstructor;


/*
 * @RestController = Automatically converts every return value to JSON.
   @Controller = Allows you to return HTML templates.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
	
	
	private final UserRepository userRepository;
	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	private final LoginHistoryRepository loginHistoryRepository;
	
	// ==========================================
    //   JSON APIs (Used by Postman)
    // ==========================================
	
	//--------------- API to fetch all the users from the database---------------------------------//
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(){
		
		//--------Fetch all users from the database-----------------------------//
		List<User> users = userRepository.findAll();
		
		//-------Check if the list is empty-------------------------------------//
		if(users.isEmpty()) {
			
			//--Return HTTP 204(no content) when no users found-----//
			return ResponseEntity.noContent().build();
		}
		
		//--- Return HTTP 200(ok) with the list of users in response body------//
		return ResponseEntity.ok(users);
	}
	
	//--------------- API to update the user status(ACTIVE or BLOCKED)-------------------------------//
	/*
	 * API(blocked)---> localhost:8080/api/admin/user/1/status?status=BLOCKED
	 * API(active)----> localhost:8080/api/admin/user/1/status?status=ACTIVE
	 */
	@PutMapping("/user/{id}/status")
	public ResponseEntity<String> updateUserStatus(@PathVariable Long id,@RequestParam String status){
		
		//------------ Find user by UserId from database---------------------------------//
		User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
		
		//------------- Validate status input(only allow ACTIVE or BLOCKED)--------------//
		if(!status.equalsIgnoreCase("ACTIVE") && !status.equalsIgnoreCase("BLOCKED")) {
				return ResponseEntity.badRequest().body("Invalid status code");
			}
		
		//-------------- Update status (convert to uppercase for consistency)
	    user.setStatus(status.toUpperCase());
		
	    //--------------- Save updated user back to database
	    userRepository.save(user);
				
	    //--------------- Return success response
	    return ResponseEntity.ok("User status updated to " + status.toUpperCase());
	}
	
	//----------------- API to fetch all transaction details----------------------------------------//
	@GetMapping("/transactions")
	public ResponseEntity<List<Transactions>> getAllTransactions(){
		
		//----------------  Fetch all transactions from DB
	    List<Transactions> transactions = transactionRepository.findAll();

	    //----------------- If no transactions found
	    if (transactions.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    //----------------- Return all transactions
	    return ResponseEntity.ok(transactions);
	}
	
	//----------------- API to fetch all accounts details-------------------------------------------//
	@GetMapping("/accounts")
	public ResponseEntity<List<Account>> getAllAccounts(){
		
		//---------------  Fetch all accounts from DB
	    List<Account> accounts = accountRepository.findAll();

	    //---------------- Handle empty case
	    if (accounts.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    //---------------- Return all accounts
	    return ResponseEntity.ok(accounts);
	}
	
	//-----------------API to fetch all loginHistory details----------------------------------------//
	@GetMapping("/login-history")
	public ResponseEntity<List<LoginHistory>> getLoginHistory() {

	    //--------------- Fetch login history from DB
	    List<LoginHistory> history = loginHistoryRepository.findAll();

	    //-------------- If empty
	    if (history.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    //--------------  Return all login history
	    return ResponseEntity.ok(history);
	}
}
