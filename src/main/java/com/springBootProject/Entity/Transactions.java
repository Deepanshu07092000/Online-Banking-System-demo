package com.springBootProject.Entity;

import java.time.LocalDateTime;

import com.springBootProject.Entity.Type.TransactionStatus;
import com.springBootProject.Entity.Type.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions_details")

/*
 * --------How transaction works--------------------
 * Deposit --> fromAccount(null)    toAccount(User)
 * Withdraw --> fromAccount(User)   toAccount(null)
 * Transfer --> fromAccount(sender) toAccount(Receiver)
 */
public class Transactions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double amount;
	
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
	//------SENDER ACCOUNT-------------------//
	@ManyToOne
	@JoinColumn(name = "from_account_id")
	private Account fromAccount;
	
	//--------RECIEVER ACCOUNT-----------------//
	@ManyToOne
	@JoinColumn(name = "to_account_id")
	private Account toAccount;
	
	private LocalDateTime transactionDateandTime;

}
