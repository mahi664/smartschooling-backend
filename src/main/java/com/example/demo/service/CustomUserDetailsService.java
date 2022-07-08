package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.bo.LoginRequest;
import com.example.demo.bo.UserLoinCredsAndRoles;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	AuthenticationService authenticationService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserLoinCredsAndRoles userLoinCredsAndRoles = authenticationService.getUserDetailsByUserName(username);
		
		if(userLoinCredsAndRoles!=null) {
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userLoinCredsAndRoles.getRole().getRoleName());
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(simpleGrantedAuthority);
			return new User(userLoinCredsAndRoles.getUsername(),userLoinCredsAndRoles.getPassword(), authorities);
		}
		return null;
	}

}
