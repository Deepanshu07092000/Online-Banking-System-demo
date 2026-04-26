package com.springBootProject.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	
	//----------------- Page 1 Fields--------------------------------------------//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String formNo;
	
	private String name;
	private String fatherName;
	
	private LocalDate dateOfBirth;
	
	private String gender;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	@Column(nullable = false)
	private String role;
	
	@Column(nullable = false)
	private String status;
	
	private String maritalStatus;
	private String address;
	private String city;
	private String pinCode;
	private String state;
	
	//----------------- Page 2 Fields ----------------------------------------------//

	private String category;
	private String income;
	private String education;
	private String occupation;
	
	@Column(unique = true,nullable = false)
	private String panNumber;
	
	@Column(unique = true,nullable = false)
	private String aadharNumber;
	
	private String seniorCitizen;     // Yes / No
	private String accountType;       // Savings / Current
	private String existingAccount;   // Yes / No

}
