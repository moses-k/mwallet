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
@Table(name = "txn_details")
public class Transactions {

	@Id
	@Column(name = "txn_code", columnDefinition="VARCHAR(12) NOT NULL")
	private String txnCode;

	/*@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "walletid", referencedColumnName = "walletid", unique = true)
	@JsonIgnore
	private Wallet wallet;*/
	
	@Column(name = "walletid", columnDefinition = "VARCHAR(16) NOT NULL")
	private String walletId;

	@Column(name = "txnamount", precision = 10, scale = 2)
	private BigDecimal txnAmount;

	@Column(name = "txntype", columnDefinition="VARCHAR(10) NOT NULL") // eg WTB, WPP etc
	private String txnType;
	
	@Column(name = "paymode", columnDefinition="CHAR(3) NOT NULL") //C- Credit, D - Debit
	private String payMode;

	@Column(name = "txndatetime")
	private String dateTime;

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}


	public BigDecimal getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	
	
	

}
