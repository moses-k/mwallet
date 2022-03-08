package com.mwallet.tps.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mwallet.tps.modal.User;
import com.mwallet.tps.modal.Wallet;
import com.mwallet.tps.payload.response.CustomerDetailsResponse;
import com.mwallet.tps.repository.CustomerRepository;
import com.mwallet.tps.repository.WalletRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	WalletRepository walletRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = customerRepository.findByEmail(username);
		User user = userOptional
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email : " + username));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), 
				user.getPassword(), true, true, true, true, getAuthorities("USER"));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Collections.singletonList(new SimpleGrantedAuthority(role));

	}

	@Transactional
	public CustomerDetailsResponse getCustomerDetails(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = customerRepository.findByEmail(username);
		User user = userOptional
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email : " + username));
		CustomerDetailsResponse customerDetailsResponse = new CustomerDetailsResponse();

		Optional<Wallet> walletOptional = walletRepository.findByRelationshipNo(user.getRelationshipNo());
		Wallet walletDetails = walletOptional
				.orElseThrow(() -> new UsernameNotFoundException("No Wallet found for user : " + username));

		customerDetailsResponse.setRelationshipNo(user.getRelationshipNo());
		customerDetailsResponse.setName(user.getName());
		customerDetailsResponse.setEmail(user.getEmail());
		customerDetailsResponse.setPhoneNumber(user.getPhoneNumber());
		customerDetailsResponse.setEmail(user.getEmail());
		customerDetailsResponse.setStatusCode("200");
		customerDetailsResponse.setTimestamp(Instant.now().toString());
		customerDetailsResponse.setMessage("User details retrieved");
		customerDetailsResponse.setAccount(walletDetails);

		return customerDetailsResponse;

	}

}
