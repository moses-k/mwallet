package com.mwallet.tps.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mwallet.tps.payload.request.CustomerLoginRequest;
import com.mwallet.tps.payload.request.CustomerRegistrationRequest;
import com.mwallet.tps.payload.response.AuthenticationResponse;
import com.mwallet.tps.payload.response.CustomerRegResponse;
import com.mwallet.tps.service.AuthService;

@RestController
@RequestMapping(path = "/mwallet/auth")
public class AuthController {
	@Autowired
	AuthService authService;

	/*Customer registration*/
	@PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CustomerRegResponse> registerCustomer(
			@Valid @NotNull @RequestBody CustomerRegistrationRequest custRegRequest) {
		CustomerRegResponse regResponse = authService.customerRegistration(custRegRequest);
	return new ResponseEntity<CustomerRegResponse>(regResponse, HttpStatus.CREATED);
	}
	
	/*Customer login*/
	@PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<AuthenticationResponse> customerLogin(
			@Valid @NotNull @RequestBody CustomerLoginRequest custLoginRequest) {
		AuthenticationResponse loginResponse = authService.customerLogin(custLoginRequest);
	return new ResponseEntity<AuthenticationResponse>(loginResponse, HttpStatus.OK);
	}
	

}
