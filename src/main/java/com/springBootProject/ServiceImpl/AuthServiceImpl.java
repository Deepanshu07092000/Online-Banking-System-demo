package com.springBootProject.ServiceImpl;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springBootProject.AuthService;
import com.springBootProject.Dto.SignupRequestDTO;
import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.User;
import com.springBootProject.Repository.AccountRepository;
import com.springBootProject.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

/*
 * Implementing service interface with serviceImpl (Loose Coupling)
 * Controller depend on interface not on the specified class
 * Multiple implementation --> (AuthServiceImpl --> normal users)
 * Contains business logic     (AdminAuthServiceImpl --> ADMIN logic)
 * Uses Repository			   (OAuthServiceImpl --> Google login)
 */

@Service								// Marks this class as Spring Bean //
@RequiredArgsConstructor				// Automatically creates constructor for final fields //
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;  // @Autowired -> field injection not for Constructor injection //
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;

	@Override
	public void registerUser(SignupRequestDTO signupRequestDTO) {
		
		//---- Check if email already exists-------------------------------// 
		if(userRepository.existsByEmail(signupRequestDTO.getEmail())) {
			throw new RuntimeException("Email Already registered.......");
		}
		
		//-------- Check if Aadhar Number already exists---------------------------//
	    if(userRepository.existsByAadharNumber(signupRequestDTO.getAadharNumber())) {
	        throw new RuntimeException("AadharNo already exist please provide another AadharNo");
	    }
	    
	    //--------- Check if PAN Number already exists
	    if(userRepository.existsByPanNumber(signupRequestDTO.getPanNumber())) {
	        throw new RuntimeException("PanNumber already exists !provide another panNumber");
	    }
		
	    //---------- Terms and Conditions check
		if(!signupRequestDTO.isTermsAccepted()) {
		    throw new RuntimeException("Please accept terms and conditions");
		}
		
		//------- Map DTO <--> Entity----------------------------------------//
		User user = User.builder()
				.formNo(signupRequestDTO.getFormNo())
				
				//----------- Page 1 details-----------------------------//
				.name(signupRequestDTO.getName())
				.fatherName(signupRequestDTO.getFatherName())
				.dateOfBirth(signupRequestDTO.getDateOfBirth())
				.gender(signupRequestDTO.getGender())
				.email(signupRequestDTO.getEmail())
				.password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .maritalStatus(signupRequestDTO.getMaritalStatus())
                .address(signupRequestDTO.getAddress())
                .city(signupRequestDTO.getCity())
                .pinCode(signupRequestDTO.getPinCode())
                .state(signupRequestDTO.getState())
                
             // -------- Page 2 details ---------------------------------------//
                .category(signupRequestDTO.getCategory())
                .income(signupRequestDTO.getIncome())
                .education(signupRequestDTO.getEducation())
                .occupation(signupRequestDTO.getOccupation())
                .panNumber(signupRequestDTO.getPanNumber())
                .aadharNumber(signupRequestDTO.getAadharNumber())
                .seniorCitizen(signupRequestDTO.getSeniorCitizen())
                .accountType(signupRequestDTO.getAccountType())
                .existingAccount(signupRequestDTO.getExistingAccount())
                .role("ROLE_USER")
                .status("ACTIVE")
                .build();
		
		//-------------- SAVE USER---------------------------------------------//
	    User savedUser = userRepository.save(user);
	    
	    //-------------- CREATE ACCOUNT
	    Account account = Account.builder()
	            .user(savedUser)
	            .accountType(savedUser.getAccountType())
	            .accountNumber(generateAccountNo())
	            .availableBalance(0.0)
	            .createdAt(java.time.LocalDateTime.now())
	            .build();

	    accountRepository.save(account);
		
	}
	
	//----------- Function to generate the random(4 digit) formNo----------------------//
	public String generateFormNumber() {
	    Random random = new Random();
	    int num = 1000 + random.nextInt(9000); // 4-digit
	    return String.valueOf(num);
	}
	
	//----------- Function to generate the random(12 digit) AccountNo--------------------//
	@Override
	public String generateAccountNo() {
		 String accountNumber;
		    do {
		        long number = (long)(Math.random() * 1_000_000_000000L); // 12 digit
		        accountNumber = String.valueOf(number);
		    } while (accountRepository.existsByAccountNumber(accountNumber));

		    return accountNumber;
	}
	
	//------------- method to check the login credentials(Email, Password)----------------//
	@Override
	public User login(String email, String password) {
		
		//-------------------- Fetch user from database using email
		 User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		 
		//------------------ Check if user is BLOCKED
		    if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
		        throw new RuntimeException("Your account is blocked. Please contact admin.");
		    }
	   //------------------- Validate password    
		    if (!passwordEncoder.matches(password, user.getPassword())) {
		        throw new RuntimeException("Invalid password");
		    }
	   //-------------- Everything is fine return user
		    return user;
	}
	
	//---------------- method to change the password(from the Profile page)----------------------//
	@Override
	public void changePassword(User user, String currentPassword, String newPassword, String confirmPassword) {
		
		//-------------- Check current password
	    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
	        throw new RuntimeException("Current password does not match............");
	    }
	    
	    //--------------- Prevent same password reuse
	    if (passwordEncoder.matches(newPassword, user.getPassword())) {
	        throw new RuntimeException("same password can be taken.........");
	    }
	    
	    //--------------- Check confirm password
	    if (!newPassword.equals(confirmPassword)) {
	        throw new RuntimeException("password not match.........");
	    }
	    
	    //---------------- Encode and update
	    user.setPassword(passwordEncoder.encode(newPassword));

	    //----------------- Save to DB
	    userRepository.save(user);
		
	}
}
