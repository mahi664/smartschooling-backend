package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.BranchDetailsBO;
import com.example.demo.service.BranchService;

@RestController
public class BranchController {

	@Autowired
	private BranchService branchService;
	
	@PostMapping(path="/Branches")
	public BranchDetailsBO addBranch(String instituteId, @RequestBody BranchDetailsBO branchDetailsBO) {
		return branchService.addNewBranch(instituteId,branchDetailsBO);
	}
	
	@GetMapping(path="/Branches")
	public List<BranchDetailsBO> getBranches(){
		return branchService.getAllBranches();
	}
}
