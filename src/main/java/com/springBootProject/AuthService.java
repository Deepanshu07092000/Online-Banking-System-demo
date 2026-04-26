package com.springBootProject;

import com.springBootProject.Dto.SignupRequestDTO;
import com.springBootProject.Entity.User;


/*
 * This is service interface which define what needs to be done 
 * It contains only the method signature not the Logic
 */
public interface AuthService {
	void registerUser(SignupRequestDTO signupRequestDTO);
	
	//------------ Generate the Random(4 digit) form number-------------------//
	String generateFormNumber();
	
	//------------ Generate the Random(12 digit) account number---------------//
	String generateAccountNo();
	
	//------------ User login method --------------------------------//
	User login(String email,String password);
	
	//-------------- Change password method----------------------------//
	void changePassword(User user, String currentPassword, String newPassword, String confirmPassword);
}
