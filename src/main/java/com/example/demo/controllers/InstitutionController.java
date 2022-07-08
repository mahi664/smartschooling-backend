package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.InstituteDetailsBO;
import com.example.demo.service.InstituteService;

@RestController
public class InstitutionController {
	
	@Autowired
	private InstituteService instituteService;
	
	@GetMapping(path="/Institute")
	public InstituteDetailsBO getInstitute() {
		return instituteService.getInstituteDetails();
	}
	
	@PostMapping(path="/Institute")
	public InstituteDetailsBO addInstitute(@RequestBody InstituteDetailsBO instituteDetailsBO) {
		return instituteService.addNewInstitue(instituteDetailsBO);
	}
}
