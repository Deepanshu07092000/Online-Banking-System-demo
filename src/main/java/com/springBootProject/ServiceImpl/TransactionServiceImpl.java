package com.springBootProject.ServiceImpl;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springBootProject.TransactionService;
import com.springBootProject.Entity.Account;
import com.springBootProject.Entity.Transactions;
import com.springBootProject.Entity.Type.TransactionStatus;
import com.springBootProject.Entity.Type.TransactionType;
import com.springBootProject.Repository.AccountRepository;
import com.springBootProject.Repository.TransactionRepository;


import lombok.RequiredArgsConstructor;

@Service											//Marks this class as Spring Bean //
@RequiredArgsConstructor							// Automatically creates constructor for final fields //
@Transactional														/*
																	this is used at the service layer to ensure the Atomicity
																	if any step transaction fails -> rollBack automatically
																	prevents partial updates & maintain data consistency
																	*/
public class TransactionServiceImpl implements TransactionService{
	
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	
	//-----------Deposit method(performs the deposit transaction)-----------------------------//
	@Override
	public void deposit(String accountNumber, Double amount) {
		
		//------------------ Check null or invalid amount
	    if (amount == null || amount <= 0) {
	        throw new RuntimeException("Invalid deposit amount..");
	    }
		
		//-------- Fetch the accountNumber from the DB-------------------------------------//
		Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found......"));
		
		//---------- Update balance----------------------------------------------------//
		account.setAvailableBalance(account.getAvailableBalance() + amount);
		
		//----------- Save the balance to the DB---------------------------------------//
		accountRepository.save(account);
		
		//------------- Create Transaction---------------------------------------------//
		Transactions transactions = Transactions.builder()
									.amount(amount)
									.type(TransactionType.DEPOSIT)
									.status(TransactionStatus.SUCCESS)
									.transactionDateandTime(LocalDateTime.now())
									.toAccount(account)
									.build();
		
		//-------------- Save the transaction to the DB---------------------------------//
		transactionRepository.save(transactions);
	}
	
	//------------Withdraw method(performs the withdraw transaction)--------------------------//
	@Override
	public void withdraw(String accountNumber, Double amount) {
		
		//-------------------- Check invalid amount
	    if (amount == null || amount <= 0) {
	        throw new RuntimeException("Invalid withdraw amount");
	    }
	    
	    //--------------------- Minimum withdraw condition
	    if (amount < 500) {
	        throw new RuntimeException("Minimum withdraw amount is 500");
	    }
		
		//-------- Fetch the accountNumber from the DB-------------------------------------//
		Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found......"));
		
		//------------------- Check if balance is zero
	    if (account.getAvailableBalance() <= 0) {
	        throw new RuntimeException("No balance. Please deposit first.");
	    }
		
		//-------- withdraw amount condition check-------------------------------------//
		if (account.getAvailableBalance() < amount) {
		    throw new RuntimeException("Insufficient Balance");
		   }
		
		//---------- Update balance----------------------------------------------------//
		account.setAvailableBalance(account.getAvailableBalance() - amount);
				
		//----------- Save the balance to the DB---------------------------------------//
		accountRepository.save(account);
		
		//------------- Create Transaction---------------------------------------------//
		Transactions transactions = Transactions.builder()
									.amount(amount)
									.type(TransactionType.WITHDRAW)
									.status(TransactionStatus.SUCCESS)
									.transactionDateandTime(LocalDateTime.now())
									.toAccount(account)
									.build();
				
		//-------------- Save the transaction to the DB---------------------------------//
		transactionRepository.save(transactions);
	}
	
	//------------Transfer method(performs the transfer transaction)--------------------------//
	@Override
	public void transfer(String fromAccountNumber, String toAccountNumber, Double amount) {
		
		//---------------- Check Invalid amount
	    if (amount == null || amount <= 0) {
	        throw new RuntimeException("Invalid transfer amount");
	    }
	    
	    //------------------ Prevent self-transfer
	    if (fromAccountNumber.equals(toAccountNumber)) {
	        throw new RuntimeException("Cannot transfer to the same account");
	    }
	
	//-------- Fetch the accountNumber(Sender) from the DB-------------------------------------//
		Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber).orElseThrow(()-> new RuntimeException("Sender Account not found......"));
		
	//-------- Fetch the accountNumber(Receiver) from the DB-------------------------------------//
		Account toAccount = accountRepository.findByAccountNumber(toAccountNumber).orElseThrow(()-> new RuntimeException("Receiver Account not found......"));
		
		//------------------ Check if sender balance is zero
	    if (fromAccount.getAvailableBalance() <= 0) {
	        throw new RuntimeException("No balance. Please deposit first.");
	    }
		
	    //-------------------- Insufficient balance
		 if (fromAccount.getAvailableBalance() < amount) {
	            throw new RuntimeException("Insufficient Balance");
	        }
	// ---------- Deduct from sender
	        fromAccount.setAvailableBalance(fromAccount.getAvailableBalance() - amount);
	        
	//------------- Add to the Receiver 
	        toAccount.setAvailableBalance(toAccount.getAvailableBalance() + amount);
	        
	//----------- Save the balance to the DB---------------------------------------//
	        accountRepository.save(fromAccount);
	        accountRepository.save(toAccount); 
	        
	//------------- Create Transaction---------------------------------------------//
		Transactions transactions = Transactions.builder()
										.amount(amount)
										.type(TransactionType.TRANSFER)
										.status(TransactionStatus.SUCCESS)
										.transactionDateandTime(LocalDateTime.now())
										.fromAccount(fromAccount)
										.toAccount(toAccount)
										.build();
					
	//-------------- Save the transaction to the DB---------------------------------//
		transactionRepository.save(transactions);        
		
	}

	//----------------- Transaction History method--------------------------------------------------//
	@Override
	public List<Transactions> getTransactionHistory(String accountNumber) {
		
		//-------- Fetch the accountId from the DB-------------------------------------//
				Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found......"));
		
				return transactionRepository.findByFromAccountOrToAccountOrderByTransactionDateandTimeDesc(account, account);
	}
	
	//------- Implementation to Fetch Account Holder Name ------------------------------------------//
	@Override
	public String getAccountHolderName(String accountNumber) {
		
	    //---------------- Fetch the account from DB
	    Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found...")); 

	    //--------------- Return the name if account exists
	    if (account != null && account.getUser() != null) {
	        return account.getUser().getName(); 
	    }
	    
	    return "Not Found"; 
	}
	
}
