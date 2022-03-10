package com.mwallet.tps.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwallet.tps.payload.request.CustomerProfileRequest;
import com.mwallet.tps.payload.response.CustomerDetailsResponse;
import com.mwallet.tps.service.UserDetailsServiceImpl;

@RestController
@RequestMapping(path = "/mwallet/customer")
public class MwalletCustomerController {
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	/* Customer profile */
	@GetMapping(path = "/profile", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CustomerDetailsResponse> registerCustomer(
			@Valid @NotNull @RequestBody CustomerProfileRequest custProfileRequest) {
		CustomerDetailsResponse customerDetails = userDetailsServiceImpl.getCustomerDetails(
				custProfileRequest.getEmail());
	return new ResponseEntity<CustomerDetailsResponse>(customerDetails, HttpStatus.OK);
	}

		
		
}
