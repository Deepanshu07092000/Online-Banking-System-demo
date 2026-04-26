package com.springBootProject.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBootProject.Entity.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory,Long> {
	
	//-------- Return the list of loginHistory(based on the userId)---------------------------------//
	List<LoginHistory> findByUserId(Long userId); 
	
	//-------- Get the latest login record (based on the userId)-----------------------------------//
	Optional<LoginHistory> findTopByUserIdAndLogoutTimeIsNotNullOrderByLoginTimeDesc(Long userId);
	
	//---------- Return the list of loginHistory(fromDate and ToDate)-------------------------------//
	List<LoginHistory> findByLoginTimeBetween(LocalDateTime start, LocalDateTime end);

}
