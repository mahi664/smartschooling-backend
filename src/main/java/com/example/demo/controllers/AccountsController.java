package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.AccountsDetailsBO;
import com.example.demo.services.AccountsService;

@RestController
public class AccountsController {
	
	@Autowired
	private AccountsService accountsService;
	
	@GetMapping(path = "/Accounts/Details")
	public List<AccountsDetailsBO> getAccountDetails(){
		return accountsService.getAccountDetails();
	}
	
	@PostMapping(path = "/Accounts/Details")
	public AccountsDetailsBO addNewAccountDetails(@RequestBody AccountsDetailsBO accountsDetailsBO) {
		return accountsService.addNewAccountDetails(accountsDetailsBO);
	}
}
