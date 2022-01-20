package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.ClassDetaislBO;
import com.example.demo.services.ClassesService;

@RestController
@CrossOrigin(origins="*")
public class ClassesController {

	@Autowired
	private ClassesService classesService;
	
	@PostMapping(path="/Classes")
	public ClassDetaislBO addNewClass(@RequestBody ClassDetaislBO classDetaislBO) {
		return classesService.addNewClass(classDetaislBO);
	}
	
	@GetMapping(path="/Classes")
	public List<ClassDetaislBO> getClasses() {
		return classesService.getClassesDetails();
	}
	
	@PostMapping(path="/Classes/advance")
	public List<ClassDetaislBO> addNewClasses(@RequestBody List<ClassDetaislBO> classDetaislBOs){
		return classesService.addNewClasses(classDetaislBOs);
	}
	
	@PostMapping(path="/Classes/Subjects")
	public List<String> addClassSubjects(@RequestParam String classId, @RequestBody List<String> subjectIds) {
		return classesService.addClassSubject(classId, subjectIds);
	}
}
