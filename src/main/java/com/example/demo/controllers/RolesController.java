package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.LeaveDetailsBO;
import com.example.demo.bo.RoleDetailsBO;
import com.example.demo.bo.SubjectDetailsBO;
import com.example.demo.services.RoleDetailsService;

@RestController
@RequestMapping(value = "/roles")
@CrossOrigin(origins = "*")
public class RolesController {

	@Autowired
	private RoleDetailsService roleDetailsService;
	
	@PostMapping
	public RoleDetailsBO addNewRoleDetails(@RequestBody RoleDetailsBO roleDetailsBO) {
		return roleDetailsService.addNewRoleDetails(roleDetailsBO);
	}
	
	@GetMapping
	public List<RoleDetailsBO> getRoleDetails(){
		return roleDetailsService.getRolesDetails();
	}
	
	@PostMapping(path="/{roleId}/leave-types")
	public List<LeaveDetailsBO> addRoleApplicableLeaveConfig(@PathVariable String roleId, @RequestBody List<LeaveDetailsBO> leaveTypes) {
		return roleDetailsService.addRoleApplicableLeaveConfig(roleId, leaveTypes, true);
	}
	
	@GetMapping(path="/{roleId}/leave-types")
	public List<LeaveDetailsBO> getRoleApplicableLeaveConfig(@PathVariable String roleId) {
		return roleDetailsService.getRoleApplicableLeaveConfig(roleId);
	}
}
