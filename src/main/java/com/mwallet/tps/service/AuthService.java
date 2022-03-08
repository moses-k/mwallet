package com.mwallet.tps.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mwallet.tps.modal.User;
import com.mwallet.tps.modal.UserRole;
import com.mwallet.tps.modal.Wallet;
import com.mwallet.tps.payload.request.CustomerLoginRequest;
import com.mwallet.tps.payload.request.CustomerRegistrationRequest;
import com.mwallet.tps.payload.response.AuthenticationResponse;
import com.mwallet.tps.payload.response.CustomerRegResponse;
import com.mwallet.tps.repository.CustomerRepository;
import com.mwallet.tps.repository.WalletRepository;
import com.mwallet.tps.security.JwtProvider;
import com.mwallet.tps.utility.Utilities;

@Service
@Transactional
public class AuthService {
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	WalletRepository walletRepository;

	@Transactional
	public CustomerRegResponse customerRegistration(CustomerRegistrationRequest regRequest) {
		CustomerRegResponse customerRegResponse = new CustomerRegResponse();
		try {

			SimpleDateFormat formatter1 = new SimpleDateFormat("yyMMddHHmm");

			User newUser = new User();
			UserRole role = new UserRole();
			Wallet wallet = new Wallet();
			newUser.setRelationshipNo((formatter1.format(new java.util.Date())).toString());
			newUser.setName(regRequest.getName());
			newUser.setPassword(passwordEncoder.encode(regRequest.getPassword()));
			// newUser.setTxnPin(regRequest.getTxnPin());
			newUser.setEmail(regRequest.getEmail());
			newUser.setPhoneNumber(regRequest.getPhoneNumber());
			newUser.setCreatedOn(Utilities.getCurrentTimeStamp());
			newUser.setExpiry("9999-12-31 00:00:00");
			role.setCode(002);
			role.setDescription("Customer");
			newUser.setRole(role);
			newUser.setLoginAttempts("0");
			newUser.setTxnTries("0");

			List<User> users = customerRepository.findAll();
			for (User other : users) {
				if (other.getEmail().equals(newUser.getEmail())) {
					customerRegResponse.setStatusCode("201");
					customerRegResponse.setMessage("Customer already exist");
					customerRegResponse.setRelationshipNo(newUser.getRelationshipNo());
					customerRegResponse.setName(newUser.getName());
					customerRegResponse.setTimestamp(Utilities.getCurrentTimeStamp());

					return customerRegResponse;
				}
			}
			
			//Create a wallet for the customer
			wallet.setWalletId((formatter1.format(new java.util.Date())).toString());
			wallet.setDescription("Fiat Wallet");
			wallet.setCreatedon(Utilities.getCurrentTimeStamp());
			wallet.setCurrency("KES");
			wallet.setCurrentBalance(new BigDecimal("0"));
			wallet.setStatus("A"); //active
			wallet.setLastModified(Utilities.getCurrentTimeStamp());
			wallet.setUser(newUser);
			
			
			//save to the database
			walletRepository.save(wallet);
			
			//customerRegResponse..save(newUser);
			customerRegResponse.setStatusCode("201");
			customerRegResponse.setMessage("Registration successful");
			customerRegResponse.setRelationshipNo(newUser.getRelationshipNo());
			customerRegResponse.setName(newUser.getName());
			customerRegResponse.setTimestamp(Utilities.getCurrentTimeStamp());

		} catch (Exception e) {
			System.out.println("Exception in customerRegistration " + e.getMessage());
		} finally {

		}

		return customerRegResponse;
	}

	public AuthenticationResponse customerLogin(CustomerLoginRequest loginRequest) {

		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(token, loginRequest.getEmail());
		 /*return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
	                .username(loginRequest.getUsername())
	                .build();*/
		
	}
	/* public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
	        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
	        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
	        return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenRequest.getRefreshToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
	                .username(refreshTokenRequest.getUsername())
	                .build();
	    }

	    public boolean isLoggedIn() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	    }*/

}
