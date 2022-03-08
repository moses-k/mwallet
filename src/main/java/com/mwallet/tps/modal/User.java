package com.mwallet.tps.modal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_details")
public class User {
	
	public User() {
		
	}
	
	@Id
	@Column(name = "relationshipno", columnDefinition = "VARCHAR(16) NOT NULL")
	private String relationshipNo;
	
	@Column(name = "email", columnDefinition="VARCHAR(100) NOT NULL")
	private String email;
	
	@Column(name = "password", columnDefinition="VARCHAR(100) NOT NULL")
	private String password;
	
	@Column(name = "txnPin", columnDefinition="VARCHAR(4)")
	private String txnPin;
	
	@Column(name = "name", columnDefinition="VARCHAR(100) NOT NULL")
	private String name;
	
	@Column(name = "loginAttempts")
	private String loginAttempts;
	
	@Column(name = "txnTries")
	private String txnTries;
	
	@Column(name = "createdOn")
	private String createdOn;
	
	@Column(name = "expiry")
	private String expiry;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "role_code", referencedColumnName = "role_code", unique = true)
	private UserRole role;
	
	//@OneToOne(mappedBy = "relationshipno")
   // private Wallet wallet;
	
	public String getRelationshipNo() {
		return relationshipNo;
	}

	public void setRelationshipNo(String relationshipNo) {
		this.relationshipNo = relationshipNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTxnPin() {
		return txnPin;
	}

	public void setTxnPin(String txnPin) {
		this.txnPin = txnPin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(String loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public String getTxnTries() {
		return txnTries;
	}

	public void setTxnTries(String txnTries) {
		this.txnTries = txnTries;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	
}
