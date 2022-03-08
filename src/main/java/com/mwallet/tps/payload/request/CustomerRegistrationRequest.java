package com.mwallet.tps.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CustomerRegistrationRequest {
	
	@NotBlank
	@NotNull
	private String email;
	@NotBlank
	@NotNull
	private String password;
	@NotBlank
	@NotNull
	private String phoneNumber;
	@NotBlank
	@NotNull
	private String name;
	@NotBlank
	@NotNull
	private String txnPin;
	
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTxnPin() {
		return txnPin;
	}
	public void setTxnPin(String txnPin) {
		this.txnPin = txnPin;
	}
}
