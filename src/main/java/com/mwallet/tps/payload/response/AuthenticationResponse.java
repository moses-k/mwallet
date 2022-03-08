package com.mwallet.tps.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
	

	public AuthenticationResponse(String authgenticationToken, String username) {
		super();
		this.authenticationToken = authgenticationToken;
		this.username = username;
	}
	private String authenticationToken;
	private String username;
	public String getAuthgenticationToken() {
		return authenticationToken;
	}
	public void setAuthgenticationToken(String authgenticationToken) {
		this.authenticationToken = authgenticationToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	

}
