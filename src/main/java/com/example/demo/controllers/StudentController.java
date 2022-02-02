package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.FeesDetailsBO;
import com.example.demo.bo.StudentDetailsBO;
import com.example.demo.bo.StudentsFeesTransactionDetailsBO;
import com.example.demo.services.StudentService;

@RestController
@CrossOrigin(origins="*")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	
	@PostMapping(path="/Students")
	public StudentDetailsBO addNewStudent(@RequestBody StudentDetailsBO studentDetailsBO) {
		return studentService.addNewStudent(studentDetailsBO);
	}
	
	
	@GetMapping(path="/Students")
	public List<StudentDetailsBO> getStudentDetails(){
		return studentService.getStudentDetails();
	}
	
	@PostMapping(path="/Students/update")
	public StudentDetailsBO updateStudentDetails(@RequestBody StudentDetailsBO studentDetailsBO) {
		return studentService.updateStudentDetails(studentDetailsBO);
	}
	
	@PostMapping(path="/Students/delete")
	public boolean deleteStudentDetails(@RequestBody StudentDetailsBO studentDetailsBO) {
		return studentService.deleteStudentCompleteDetails(studentDetailsBO.getStudentId());
	}
	
	@GetMapping(path = "/Students/Receivables")
	public List<StudentDetailsBO> getStudentsReceivables(){
		return studentService.getStudentsReceivables();
	}
	
	@GetMapping(path = "/Students/{studentId}/Fees/Assigned")
	public Map<String, List<FeesDetailsBO>> getStudentsFeesAssinedDetails(@PathVariable String studentId){
		return studentService.getStudentFeesAssignedDetails(studentId);
	}
	
	@GetMapping(path = "/Students/{studentId}/Fees/Collections")
	public List<StudentsFeesTransactionDetailsBO> getStudentFeesCollectionsTransactions(@PathVariable String studentId){
		return studentService.getStudentsFeesCollectionsTransactions(studentId);
	}
}
