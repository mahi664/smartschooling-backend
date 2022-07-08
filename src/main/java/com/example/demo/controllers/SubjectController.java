package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.SubjectDetailsBO;
import com.example.demo.service.SubjectService;

@RestController
@CrossOrigin(origins = "*")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;
	
	@PostMapping(path="/Subjects")
	public List<SubjectDetailsBO> addNewSubjects(@RequestBody List<SubjectDetailsBO> subjectDetailsBOs){
		return subjectService.addNewSubjects(subjectDetailsBOs);
	}
	
	@GetMapping(path="/Subjects")
	public List<SubjectDetailsBO> getSubjects(){
		return subjectService.getSubjects();
	}
}
