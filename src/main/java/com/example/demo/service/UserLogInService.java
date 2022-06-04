package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class UserLogInService {

	public String validateUserLogin(String username, String password) {
		String message = "";
		if(username.equals("admin") && password.equals("admin"))
			message = "Log in Successful";
		else
			message="Log in failed";
		return message;
	}

}
