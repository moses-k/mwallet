package com.mwallet.tps.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


public class WithdrawFundRequest {
	
	@Positive
	@NotNull
	private BigDecimal amount;
	@NotBlank
	@NotNull
	private String walletId;
	@NotBlank
	@NotNull
	private String atmCode;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public String getWalletId() {
		return walletId;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	public String getAtmCode() {
		return atmCode;
	}
	public void setAtmCode(String atmCode) {
		this.atmCode = atmCode;
	}
	
	

	
	

}
