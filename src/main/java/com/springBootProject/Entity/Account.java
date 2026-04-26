package com.springBootProject.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Account_details")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;
	
	//-------------Relation with User(One user -> one account)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	private String accountType;
	
	@Column(unique = true)
	private String accountNumber;
	
	@Column(name = "available_Balance")
	private Double availableBalance;
	
	private LocalDateTime createdAt;
	
	@PrePersist								// It is a JPA lifecycle method that runs automatically	BEFORE the object is saved into the database.
	public void setDefaultValues() {

	    //-------- Set default balance(if the new user register availableBalance = 0.0)-----//
	    if (this.availableBalance == null) {
	        this.availableBalance = 0.00;
	    }
	}

}
