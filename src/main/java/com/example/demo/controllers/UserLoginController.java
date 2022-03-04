package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.LoginRequest;
import com.example.demo.bo.LoginResponse;
import com.example.demo.bo.QuickUserRegistrationRequest;
import com.example.demo.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class UserLoginController {

	@Autowired
	AuthenticationService authenticationService;

	@PostMapping
	public LoginResponse loginWithPassword(@RequestBody LoginRequest loginDetailsBO) {
		return authenticationService.loginWithPassword(loginDetailsBO);
	}
	
	@PostMapping(path = "/default-user-registration") // One time method call for admin setup
	public String addDefaultUserRegistration(@RequestBody QuickUserRegistrationRequest quickUserRegistrationRequest) {
		return authenticationService.addDefaultUserRegistration(quickUserRegistrationRequest);
	}
}
