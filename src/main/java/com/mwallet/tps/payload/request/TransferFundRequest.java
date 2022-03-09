package com.mwallet.tps.payload.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


public class TransferFundRequest {
	
	@NotBlank
	@NotNull
	private String senderWallet;
	
	@NotBlank
	@NotNull
	private String receiverPhoneNumber;
	
	@Positive
	@NotNull
	private BigDecimal sendAmount;

	public String getSenderWallet() {
		return senderWallet;
	}

	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber;
	}

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSenderWallet(String senderWallet) {
		this.senderWallet = senderWallet;
	}

	public void setReceiverPhoneNumber(String receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	
	

}
