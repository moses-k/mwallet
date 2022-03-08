package com.mwallet.tps.service;

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
import com.mwallet.tps.repository.CustomerRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	CustomerRepository  customerRepository;
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = customerRepository.findByEmail(username);
		User user = userOptional
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email : " + username));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),true,true,true,true,
				getAuthorities("USER"));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Collections.singletonList(new SimpleGrantedAuthority(role));

	}
}
