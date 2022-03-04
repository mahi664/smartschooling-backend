package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.AcademicDetailsBO;
import com.example.demo.services.AcademicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins="*")
@Tag(name = "Academic Details APIs", description = "REST end points for the Academic Details")
public class AcademicController {

	@Autowired
	private AcademicService academicService;
	
	@GetMapping(path="/academic/details")
	@Operation(summary = "Return a List of Academic Details", 
				description = "Api To Get List of Academic Details")
	public List<AcademicDetailsBO> getAcademicDetails(){
		return academicService.getAcademicDetails();
	}
	
	@PostMapping(path = "/academic/details")
	@Operation(summary = "Add New Academic Details",
				description = "An API to add new academic deails")
	public List<AcademicDetailsBO> addNewAcademicDetails(
			@Parameter(description = "List of Academic Details",
						schema = @Schema(implementation = AcademicDetailsBO.class))
			@RequestBody List<AcademicDetailsBO> academicDetailsBOs){
		return academicService.addNewAcademicDetails(academicDetailsBOs);
	}
}
