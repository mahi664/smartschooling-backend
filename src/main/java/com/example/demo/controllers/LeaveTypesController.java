package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.LeaveDetailsBO;
import com.example.demo.service.LeaveTypeService;

@RestController
@RequestMapping(value = "/Leave-Types")
@CrossOrigin(origins="*")
public class LeaveTypesController {

	@Autowired
	LeaveTypeService leaveTypeService;
	
	@PostMapping
	public LeaveDetailsBO addNewLeaveType(@RequestBody LeaveDetailsBO leaveDetailsBO) {
		return leaveTypeService.addNewLeaveType(leaveDetailsBO);
	}
	
	@GetMapping
	public List<LeaveDetailsBO> getLeaveTypes(){
		return leaveTypeService.getLeaveTypes();
	}
	
	@GetMapping(value = "/accrual-frequency")
	public List<String> getAccrualFrequencies(){
		return leaveTypeService.getAccrualFrequencies();
	}
}
