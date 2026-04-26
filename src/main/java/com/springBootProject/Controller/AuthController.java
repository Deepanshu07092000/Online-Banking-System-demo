package com.springBootProject.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springBootProject.AuthService;
import com.springBootProject.Dto.SignupRequestDTO;
import com.springBootProject.Entity.LoginHistory;
import com.springBootProject.Entity.User;
import com.springBootProject.Repository.LoginHistoryRepository;
import com.springBootProject.Util.validation.Step1Validation;
import com.springBootProject.Util.validation.Step2Validation;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

//@RestController
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
	
	private final AuthService authService;
	private final LoginHistoryRepository loginHistoryRepository;
	
	/*
	 * @RequiredArgsConstructor --> Constructor Injection
	 * @RequestBody --> JSON convert java object 
	 * @Valid --> Triggers DTO validation (PAN format , AADHAR format, NotBlank Checks)
	 */
	
	//-- API uses when sending the data in JSON format-------//
//	@PostMapping("/UserRegisteration")
//	public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
//		authService.registerUser(signupRequestDTO);
//		return ResponseEntity.ok("User Registered Successfully.....");
//		
//	}
	
	//------------------ Login Begins(login page will open)-----------------------------------//
	@GetMapping("/do-login")
	public String openLoginPage() {
	    return "login"; 
	}
	
	//----------- Matches the login credentials---------------------------------------------//
	@PostMapping("/do-login")
	public String loginUser(@RequestParam("username")String username,@RequestParam("email")String email,@RequestParam("password")String password,
							HttpSession session,Model model) {
		try {
	        //--------- Validate using (email + password)
	        User user = authService.login(email, password);

	        //---------- Store user in session
	        session.setAttribute("loggedInUser", user);
	        
	        //----------- SAVE LOGIN HISTORY (ADD THIS)
	        LoginHistory history = LoginHistory.builder()
	                .user(user)
	                .loginTime(java.time.LocalDateTime.now())
	                .build();
	        loginHistoryRepository.save(history);
	        
	        //------------ Store historyId in session
	        session.setAttribute("historyId", history.getSessionId());

	        //----------- (Optional) store userName if you want
	        session.setAttribute("username", username);
	        
	        //----------- Redirect to Dashboard

	        return "redirect:/api/auth/dashboard";
	        
	    } catch (Exception e) {
	    	 model.addAttribute("error", e.getMessage());
			return "login";
	    }
	}
	
	//-------------- Logout action performed(Redirects to the login page)------------------------//
	@PostMapping("/logout")
	public String logout(HttpSession session) {

	    //---------- Clear session
	    session.invalidate();

	    //---------- Redirect to login page
	    return "redirect:/api/auth/do-login";
	}
	
	
	//---------- this will open signup.html
	@GetMapping("/signup")
	public String openSignupPage(Model model) {
		String formNo = authService.generateFormNumber();
		model.addAttribute("formNo",formNo);
	    return "signup"; 
	}
	
	//-----API uses the HTML form data(first SignUp page will open)------------------------//
	@PostMapping("/signup2")
	public String handlePage1(@Validated(Step1Validation.class) @ModelAttribute SignupRequestDTO signupRequestDTO,    	 //  triggers validation
								BindingResult result,																	//  holds validation errors
								HttpSession session, Model model) {
		
	     //----------------- If validation fails (e.g., age < 18)
	     
	    if (result.hasErrors()) {

	     //----------------- Keep formNo (otherwise it will disappear)
	        model.addAttribute("formNo", signupRequestDTO.getFormNo());
	        model.addAttribute("signupRequestDTO", signupRequestDTO);

	        return "signup"; //-------- return same page
	    }

	    // -------- store page 1 data in session
	    session.setAttribute("signupData", signupRequestDTO);

	    // -------- pass formNo to next page
	    model.addAttribute("formNo", signupRequestDTO.getFormNo());
	    
	    return "signup2";
	}
	
	//----Uses the HTML form data(then SignUp2 page will open)-----------------------------//
	@PostMapping("/register")
	public String registerUser(@Validated(Step2Validation.class) @ModelAttribute SignupRequestDTO dto, BindingResult result ,
								HttpSession session, Model model) {
		
		if (result.hasErrors()) {
	        return "signup2";
	    }

	    //---------- get page1 data from session
	    SignupRequestDTO existingData = (SignupRequestDTO) session.getAttribute("signupData");

	    //---------- merge page2 data into page1 data
	    existingData.setCategory(dto.getCategory());
	    existingData.setIncome(dto.getIncome());
	    existingData.setEducation(dto.getEducation());
	    existingData.setOccupation(dto.getOccupation());
	    existingData.setPanNumber(dto.getPanNumber());
	    existingData.setAadharNumber(dto.getAadharNumber());
	    existingData.setSeniorCitizen(dto.getSeniorCitizen());
	    existingData.setAccountType(dto.getAccountType());
	    existingData.setExistingAccount(dto.getExistingAccount());
	    existingData.setTermsAccepted(dto.isTermsAccepted());
	    

	    //----------- save to DB
	    try {
	    authService.registerUser(existingData);
	    
	    //---------- clear session only if the registration succeeds
	    session.invalidate();
	    
	    //-----------send message to signup2 page
	    model.addAttribute("success", "Registration Successful! Please login to continue.");
	    } catch(Exception e) {
	    
	    //---------- catches the "AadharNumber" or "PanNumber" already exist 
	    model.addAttribute("error",e.getMessage());	
	    
	    //---------- Return to signup2 so that the user can see the error and can change it
	    return "signup2";
	    }
	    
	    //---------- Stay on the same page
	    return "signup2";
	}
	
	//---------------- Open the Change_Password page----------------------------------------------//
	@GetMapping("/change-password")
	public String showChangePasswordPage() {
		return "Change_Password";
	}
	
	//----------------- Handle Change_Password form(logic)-----------------------------------------//
	@PostMapping("/update-password")
	public String updatePassword(@RequestParam("currentPassword") String currentPassword,
							     @RequestParam("newPassword") String newPassword,
							     @RequestParam("confirmPassword") String confirmPassword,
							     HttpSession session,
							     Model model) {
		
		//------------- Get logged-in user from session
	    User user = (User) session.getAttribute("loggedInUser");

	    if (user == null) {
	        return "redirect:/api/auth/do-login";
	    }
		
	    try {
	    	authService.changePassword(user,currentPassword,newPassword,confirmPassword);
	    	model.addAttribute("success","Password updated successfully!");
	    	return "Change_Password";
	    } catch (RuntimeException e) {

	        //--------- Custom error handling for your UI
	        if (e.getMessage().contains("same")) {
	            model.addAttribute("errorSame", true);
	        } else if (e.getMessage().contains("match")) {
	            model.addAttribute("errorMismatch", true);
	        } else {
	            model.addAttribute("errorMismatch", true);
	        }
	        return "Change_Password";
	    }
		
	}

}
