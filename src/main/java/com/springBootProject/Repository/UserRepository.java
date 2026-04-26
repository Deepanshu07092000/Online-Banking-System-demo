package com.springBootProject.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBootProject.Entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	
	//----- Custom methods(return user present or not)-----------------//
	Optional<User> findByEmail(String email);
	
	//------ Check email already exits or not (return true or false)------------//
	boolean existsByEmail(String email);
	
	//-------- Check if PanNumber already exists or not(return true or false)----------------//
	boolean existsByPanNumber(String panNumber);
	
	//-------- Check if AadharNumber already exists or not(return true or false)----------------//
	boolean existsByAadharNumber(String aadharNUmber);
	
	//--------- Find users only not admin from DB------------------------------------------------//
	List<User> findByRole(String role);
}
