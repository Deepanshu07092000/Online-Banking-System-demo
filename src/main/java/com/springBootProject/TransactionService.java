package com.springBootProject;

import java.util.List;

import com.springBootProject.Entity.Transactions;

/*
 * This is service interface which define what needs to be done 
 * It contains only the method signature not the Logic
 */
public interface TransactionService {
	
	//------Deposit money into account----------------//
	void deposit(String accountNumber,Double amount);
	
	//-----withdraw money from account-----------------//
	void withdraw(String accountNumber,Double amount);
	
	//-------Transfer money between accounts------------//
	void transfer(String fromAccountNumber,String toAccountNumber,Double amount);
	
	//-------- Get all Transactions of a User(Account)--------------//
	List<Transactions> getTransactionHistory(String accountNumber);
	
	//------- Fetch Account Holder Name by Account Number ------------//
	String getAccountHolderName(String accountNumber);
}
