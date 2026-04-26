package com.springBootProject.Dto;

import java.time.LocalDate;

import com.springBootProject.Util.validation.AgeAbove18;
import com.springBootProject.Util.validation.Step1Validation;
import com.springBootProject.Util.validation.Step2Validation;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
	
	//----------------- Page 1 details----------------------------------------------//
	
	@NotBlank(groups = Step1Validation.class)
	private String name;
	
	@NotBlank(groups = Step1Validation.class)
	private String fatherName;
	
	@NotNull(message = "Date of Birth is required", groups = Step1Validation.class)
	@AgeAbove18                                         //Custom Validation -->(our Logic)//
	private LocalDate dateOfBirth;
	
	@NotBlank(groups = Step1Validation.class)
	private String gender;
	
	@Email
	@NotBlank(groups = Step1Validation.class)
	private String email;
	
	@NotBlank(groups = Step1Validation.class)
	@Pattern(
	    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
	    message = "Password must be 8+ chars with uppercase, lowercase, number & special character",
	    groups = Step1Validation.class
	)
	private String password;
	
	@NotBlank(groups = Step1Validation.class)
	private String maritalStatus;
	
	@NotBlank(groups = Step1Validation.class)
	private String address;
	
	@NotBlank(groups = Step1Validation.class) 
	private String city;
	
	@Pattern(regexp = "^[0-9]{6}$", message = "Pin code must be 6 digits",groups = Step1Validation.class)
	private String pinCode;
	
	@NotBlank(groups = Step1Validation.class)
	private String state;
	
	//------------------------- Page 2 details------------------------------------------------//
	
	 	@NotBlank(groups = Step2Validation.class)
	    private String category;

	    @NotBlank(groups = Step2Validation.class)
	    private String income;

	    @NotBlank(groups = Step2Validation.class)
	    private String education;

	    @NotBlank(groups = Step2Validation.class)
	    private String occupation;

	    @NotBlank(groups = Step2Validation.class)
	    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN format")
	    private String panNumber;

	    @NotBlank(groups = Step2Validation.class)
	    @Pattern(regexp = "\\d{12}", message = "Aadhar must be 12 digits")
	    private String aadharNumber;

	    @NotBlank(groups = Step2Validation.class)
	    private String seniorCitizen;   				// Yes / No

	    @NotBlank(groups = Step2Validation.class)
	    private String accountType;     				// Savings / Current

	    @NotBlank(groups = Step2Validation.class)
	    private String existingAccount; 				// Yes / No
	    
	 // ---------------- Final ---------------- //

	    @AssertTrue(message = "You must accept terms and conditions",groups = Step2Validation.class)
	    private boolean termsAccepted;
	    
	    @NotBlank
	    private String formNo;

}
