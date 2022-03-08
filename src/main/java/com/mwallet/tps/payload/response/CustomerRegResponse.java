package com.mwallet.tps.payload.response;

public class CustomerRegResponse extends MessageResponse{
	
	private String relationshipNo;
	private String name;
	
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
	
} 
