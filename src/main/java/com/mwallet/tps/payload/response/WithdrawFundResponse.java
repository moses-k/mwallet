package com.mwallet.tps.payload.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WithdrawFundResponse extends MessageResponse{
	
	private String transactionCode;
	@JsonIgnore
	private String txnmode;
	private BigDecimal amount;
	private String atmcode;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getTxnmode() {
		return txnmode;
	}
	public void setTxnmode(String txnmode) {
		this.txnmode = txnmode;
	}
	public String getAtmcode() {
		return atmcode;
	}
	public void setAtmcode(String atmcode) {
		this.atmcode = atmcode;
	}
	
}
