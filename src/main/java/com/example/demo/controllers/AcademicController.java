package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.AcademicDetailsBO;
import com.example.demo.services.AcademicService;

@RestController
@CrossOrigin(origins="*")
public class AcademicController {

	@Autowired
	private AcademicService academicService;
	
	@GetMapping(path="/academic/details")
	public List<AcademicDetailsBO> getAcademicDetails(){
		return academicService.getAcademicDetails();
	}
	
	@PostMapping(path = "/academic/details")
	public List<AcademicDetailsBO> addNewAcademicDetails(@RequestBody List<AcademicDetailsBO> academicDetailsBOs){
		return academicService.addNewAcademicDetails(academicDetailsBOs);
	}
}
