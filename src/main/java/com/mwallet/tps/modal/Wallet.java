package com.mwallet.tps.modal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wallet_details")
public class Wallet {
	@Id
	@Column(name = "walletid", columnDefinition = "VARCHAR(16) NOT NULL")
	private String walletId;
	
	@Column(name = "description", columnDefinition="VARCHAR(100) NOT NULL")
	private String description;

	@Column(name = "current_bal", columnDefinition="VARCHAR(100) NOT NULL")
	private String currentBalance;
	
	@Column(name = "currency", columnDefinition="CHAR(4) NOT NULL")
	private String currency;
	
	@Column(name = "status", columnDefinition="VARCHAR(10) NOT NULL")
	private String status;
	
	@Column(name = "createdon")
	private String createdon;
	
	@Column(name = "lastmodified")
	private String lastModified;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "relationshipno", referencedColumnName = "relationshipno", unique = true)
	private User user;
	
	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedon() {
		return createdon;
	}

	public void setCreatedon(String createdon) {
		this.createdon = createdon;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
