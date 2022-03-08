package com.mwallet.tps.payload.response;

import com.mwallet.tps.modal.Wallet;

public class CustomerDetailsResponse extends MessageResponse{
	private String relationshipNo;
	private String name;
	private String email;
	private String phoneNumber;
	private Wallet account;
	
	public String getRelationshipNo() {
		return relationshipNo;
	}
	public void setRelationshipNo(String relationshipNo) {
		this.relationshipNo = relationshipNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Wallet getAccount() {
		return account;
	}
	public void setAccount(Wallet account) {
		this.account = account;
	}
	
	
}
