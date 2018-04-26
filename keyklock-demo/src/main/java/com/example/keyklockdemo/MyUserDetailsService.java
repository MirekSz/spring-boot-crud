
package com.example.keyklockdemo;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	public MyUserDetailsService() {
		super();
	}

	// API

	@Override
	public UserDetails loadUserByUsername(final String username) {
		return new org.springframework.security.core.userdetails.User(username, "mirek", Arrays.asList(new SimpleGrantedAuthority("USER")));
	}
}
