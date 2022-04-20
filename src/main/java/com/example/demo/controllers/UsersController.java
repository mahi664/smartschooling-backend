package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.RoleDetailsBO;
import com.example.demo.bo.UserAcademicDetailsBO;
import com.example.demo.bo.UserAdvanceDetailsBO;
import com.example.demo.bo.UserBasicDetailsBO;
import com.example.demo.services.UsersService;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "*")
public class UsersController {

	@Autowired
	UsersService usersService;
	
	@PostMapping
	public UserBasicDetailsBO addNewUser(@RequestBody UserBasicDetailsBO userBasicDetailsBO) {
		return usersService.addNewUser(userBasicDetailsBO);
	}
	
	@GetMapping
	public List<UserBasicDetailsBO> getUsers(){
		return usersService.getUsers();
	}
	
	@GetMapping(path = "/{userId}")
	public UserAdvanceDetailsBO getUsersAdvanceDetails(@PathVariable String userId) {
		return usersService.getUsersAdvanceDetails(userId);
	}
	
	@PostMapping(path = "/{userId}/academic-details")
	public Map<String, UserAcademicDetailsBO> addUserAcademicDetails(@PathVariable String userId, @RequestBody Map<String, UserAcademicDetailsBO> usreAcademicDetails){
		return usersService.addUserAcademicDetails(userId, usreAcademicDetails);
	}
	
	@PostMapping(path = "/{userId}/role-details")
	public List<RoleDetailsBO> addUserRoleDetails(@PathVariable String userId, @RequestBody List<RoleDetailsBO> userRoles){
		return usersService.addUserRoleDetails(userId, userRoles);
	}
}
