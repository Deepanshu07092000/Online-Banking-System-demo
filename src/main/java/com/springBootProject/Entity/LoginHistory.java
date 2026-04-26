package com.springBootProject.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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
@Table(name = "login_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long sessionId;
	
	//--------- Link with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
	private LocalDateTime loginTime;
	
	private LocalDateTime logoutTime;
	
}
