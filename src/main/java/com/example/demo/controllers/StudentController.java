package com.example.demo.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.SuccessDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.service.StudentService;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.ResponseDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
import com.example.demo.service.dto.StudentRegistrationDto;
import com.example.demo.service.dto.StudentRegistrationResponseDto;
import com.example.demo.utils.ResponseUtil;

@RestController
@CrossOrigin(origins="*")
public class StudentController {

	private static final Logger log = LoggerFactory.getLogger(StudentController.class);
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private ResponseUtil<Object> responseUtil;
	
	@PostMapping(path = "students")
	public ResponseEntity<ResponseDto> registerNewStudent(@Valid @RequestBody StudentRegistrationDto studentRegistrationDto) throws StudentException {
		log.info("New student registration for gen reg no: {}, name:{}", studentRegistrationDto.getGenRegNo(), studentRegistrationDto.getFirstName());
		StudentRegistrationResponseDto studentRegistrationResponseDto = studentService.registerNewStudent(studentRegistrationDto);
		return responseUtil.populateSuccessResponseWithMessage(studentRegistrationResponseDto, SuccessDetails.STUDENT_REGISTRATION_SUCCESSFUL);
	}
	
	
	@GetMapping(path="/students")
	public ResponseEntity<ResponseDto> getStudentList(@RequestHeader String academicYear, @RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "10") int size) throws StudentException{
		log.info("Fetching student list for academic year {}, page {} and size {}", academicYear, page, size);
		FetchStudentsResponseDto fetchStudentsResponseDto = studentService.getStudentList(academicYear, page, size);
		return responseUtil.populateSuccessResponseWithMessageAndPagination(fetchStudentsResponseDto.getStudentDetailsList(), fetchStudentsResponseDto.getTotalItems(), fetchStudentsResponseDto.getTotalPages(), fetchStudentsResponseDto.getCurrentPage(), SuccessDetails.STUDENT_DETAILS_FETCHED_SUCCESSFULLY);
	}
	
	@GetMapping(path = "/students/general-register/{regNo}")
	public ResponseEntity<ResponseDto> getStudentDetailsForRegNo(@PathVariable(value = "regNo") int genRegNo) throws StudentException {
		log.info("Getting student details for gen reg no {}", genRegNo);
		StudentDetailsForRegNoResponseDto studentDetails = studentService.getStudentDetailsForRegNo(genRegNo);
		return responseUtil.populateSuccessResponseWithMessage(studentDetails,
				SuccessDetails.STUDENT_DETAILS_FETCHED_SUCCESSFULLY);
	}
	
//	
//	@PostMapping(path="/Students/update")
//	public StudentDetailsBO updateStudentDetails(@RequestBody StudentDetailsBO studentDetailsBO) {
//		return studentService.updateStudentDetails(studentDetailsBO);
//	}
//	
//	@PostMapping(path="/Students/delete")
//	public boolean deleteStudentDetails(@RequestBody StudentDetailsBO studentDetailsBO) {
//		return studentService.deleteStudentCompleteDetails(studentDetailsBO.getStudentId());
//	}
//	
//	@GetMapping(path = "/Students/Receivables")
//	public List<StudentDetailsBO> getStudentsReceivables(){
//		return studentService.getStudentsReceivables();
//	}
//	
//	@GetMapping(path = "/Students/{studentId}/Fees/Assigned")
//	public Map<String, List<FeesDetailsBO>> getStudentsFeesAssinedDetails(@PathVariable String studentId){
//		return studentService.getStudentFeesAssignedDetails(studentId);
//	}
//	
//	@GetMapping(path = "/Students/{studentId}/Fees/Collections")
//	public List<StudentsFeesTransactionDetailsBO> getStudentFeesCollectionsTransactions(@PathVariable String studentId){
//		return studentService.getStudentsFeesCollectionsTransactions(studentId);
//	}
//	
//	@GetMapping(path = "/Students/{studentId}/Fees/Dues")
//	public Map<String, List<FeesDetailsBO>> getStudentFeesDueDetails(@PathVariable String studentId){
//		return studentService.getStudentFeesDueDetails(studentId);
//	}
//	
//	@PostMapping(path = "/Students/{studentId}/Fees/Collections")
//	public StudentsFeesTransactionDetailsBO addStudentFeesCollectionsTransactions(@PathVariable String studentId, @RequestBody StudentsFeesTransactionDetailsBO studentsFeesTransactionDetailsBO){
//		return studentService.addNewStudentFeeCollectionDetails(studentId, studentsFeesTransactionDetailsBO);
//	}
	
//	@GetMapping(path = "/test-api")
//	public ResponseEntity<ResponseDto> testApi(){
//		return responseUtil.populateSuccessResponseWithMessage("Dummy Data", "hello");
//	}
}
