package com.springBootProject.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions,Long> {
	
	//----- Fetch all transactions where account is sender or Receiver(show latest transactions first--------------//
	List<Transactions> findByFromAccountOrToAccountOrderByTransactionDateandTimeDesc(Account fromAccount,Account toAccount);
	
	/*
	 * Dynamic JPQL query with optional parameter --> means If parameter is present -> apply filter
	 						   								If parameter is null --> ignore filter
	 	Fetch all transaction records from DB
	 	filter ---> accountNo,StartDate,EndDate					   								
	 						   
	 */
	@Query("""
			SELECT t FROM Transactions t
			WHERE 
			(:accountNo IS NULL OR 
			 t.fromAccount.accountNumber = :accountNo OR 
			 t.toAccount.accountNumber = :accountNo)
			AND
			(:start IS NULL OR t.transactionDateandTime >= :start)
			AND
			(:end IS NULL OR t.transactionDateandTime <= :end)
			""")
			List<Transactions> filterTransactions(@Param("accountNo") String accountNo,@Param("start") LocalDateTime start,
			        							  @Param("end") LocalDateTime end);

}
