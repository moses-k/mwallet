package com.mwallet.tps.modal;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "atm_wallet_details")
public class ATMWallet {
	@Id
	@Column(name = "walletid", columnDefinition = "VARCHAR(16) NOT NULL")
	private String walletId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "atmcode",  referencedColumnName = "atmcode", unique = true, columnDefinition="VARCHAR(10) NOT NULL")
	@JsonIgnore
	private ATM atm;
	
	@Column(name = "description", columnDefinition="VARCHAR(100) NOT NULL")
	private String description;

	@Column(name = "float_balance", precision = 10, scale = 2)
	private BigDecimal floatBalance;
	
	@Column(name = "cash_balance", precision = 10, scale = 2)
	private BigDecimal cashBalance;
	
	@Column(name = "currency", columnDefinition="CHAR(4) NOT NULL")
	private String currency;
	
	@Column(name = "status", columnDefinition="VARCHAR(10) NOT NULL")
	private String status;
	
	@Column(name = "createdon")
	private String createdon;
	
	@Column(name = "lastmodified")
	private String lastModified;
	
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

	public ATM getAtm() {
		return atm;
	}

	public BigDecimal getFloatBalance() {
		return floatBalance;
	}

	public BigDecimal getCashBalance() {
		return cashBalance;
	}

	public void setAtm(ATM atm) {
		this.atm = atm;
	}

	public void setFloatBalance(BigDecimal floatBalance) {
		this.floatBalance = floatBalance;
	}

	public void setCashBalance(BigDecimal cashBalance) {
		this.cashBalance = cashBalance;
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


}
