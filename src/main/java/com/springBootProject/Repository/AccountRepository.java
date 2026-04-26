package com.springBootProject.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBootProject.Entity.Account;

public interface AccountRepository extends JpaRepository<Account,Long> {
	
	//------------ Find account using userId
    Optional<Account> findByUserId(Long userId);
    
    //------------ Find account using accountNo
    Optional<Account> findByAccountNumber(String accountNumber);
    
    //------------Check if account number already exists (for uniqueness)
    boolean existsByAccountNumber(String accountNumber);


}
