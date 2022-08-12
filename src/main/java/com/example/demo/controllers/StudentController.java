package com.example.demo.controllers;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.SortFields;
import com.example.demo.constant.SuccessDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.service.StudentService;
import com.example.demo.service.dto.FeeReceivablesResponseDto;
import com.example.demo.service.dto.FeeReceivablesStatsDto;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.ResponseDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
import com.example.demo.service.dto.StudentFeesAssignedDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnRequestDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnResponseDto;
import com.example.demo.service.dto.StudentFeesReceivableDetailsDto;
import com.example.demo.service.dto.StudentImportResponseDto;
import com.example.demo.service.dto.StudentListFilterDto;
import com.example.demo.service.dto.StudentListRequestDto;
import com.example.demo.service.dto.StudentRegistrationDto;
import com.example.demo.service.dto.StudentRegistrationResponseDto;
import com.example.demo.utils.ResponseUtil;

@RestController
@CrossOrigin(origins = "*")
public class StudentController {

	private static final Logger log = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@Autowired
	private ResponseUtil<Object> responseUtil;

	/**
	 * Register new student
	 * 
	 * @param studentRegistrationDto
	 * @return
	 * @throws StudentException
	 */
	@PostMapping(path = "students")
	public ResponseEntity<ResponseDto> registerNewStudent(
			@Valid @RequestBody StudentRegistrationDto studentRegistrationDto) throws StudentException {
		log.info("New student registration for gen reg no: {}, name:{}", studentRegistrationDto.getGenRegNo(),
				studentRegistrationDto.getFirstName());
		StudentRegistrationResponseDto studentRegistrationResponseDto = studentService
				.registerNewStudent(studentRegistrationDto);
		return responseUtil.populateSuccessResponseWithMessage(studentRegistrationResponseDto,
				SuccessDetails.STUDENT_REGISTRATION_SUCCESSFUL);
	}

	/**
	 * Get student list
	 * 
	 * @param academicYear
	 * @param page
	 * @param size
	 * @param classIds
	 * @param castes
	 * @param religions
	 * @param gender
	 * @param transportOpted
	 * @param routes
	 * @param sortOrder
	 * @param quickSearchText
	 * @return
	 * @throws StudentException
	 */
	@GetMapping(path = "/students")
	public ResponseEntity<ResponseDto> getStudentList(@RequestHeader String academicYear,
			@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(value = "classId", required = false) List<String> classIds,
			@RequestParam(value = "caste", required = false) List<String> castes,
			@RequestParam(value = "religion", required = false) List<String> religions,
			@RequestParam(required = false) String gender, @RequestParam(required = false) Boolean transportOpted,
			@RequestParam(value = "route", required = false) List<String> routes,
			@RequestParam(value = "sortBy", required = false) List<SortFields> sortOrder,
			@RequestParam(value = "quickSearch", required = false) String quickSearchText) throws StudentException {
		log.info("Fetching student list for academic year {}, page {} and size {}", academicYear, page, size);
		FetchStudentsResponseDto fetchStudentsResponseDto = studentService.getStudentList(academicYear, page, size,
				StudentListRequestDto.builder()
						.filterDto(StudentListFilterDto.builder().classIds(classIds).castes(castes).gender(gender)
								.religions(religions).transportOpted(transportOpted).routeIds(routes)
								.quickSearchText(quickSearchText).build())
						.sortOrders(sortOrder).build());
		return responseUtil.populateSuccessResponseWithMessageAndPagination(
				fetchStudentsResponseDto.getStudentDetailsList(), fetchStudentsResponseDto.getTotalItems(),
				fetchStudentsResponseDto.getTotalPages(), fetchStudentsResponseDto.getCurrentPage(),
				SuccessDetails.STUDENT_DETAILS_FETCHED_SUCCESSFULLY);
	}
	
	/**
	 * Import students from file upload
	 * 
	 * @param file
	 * @return
	 * @throws StudentException
	 */
	@PostMapping(path = "/students/import")
	public ResponseEntity<ResponseDto> importStudentsFromFileUpload(@RequestParam MultipartFile file)
			throws StudentException {
		log.info("importing students from {}", file.getOriginalFilename());
		List<StudentImportResponseDto> studentImportResponseDtoList = studentService.importStudentDetailsFromFile(file);
		return responseUtil.populateSuccessResponseWithMessage(studentImportResponseDtoList,
				SuccessDetails.STUDENT_REGISTRATION_SUCCESSFUL);
	}

	/**
	 * Get Student Details For Reg No
	 * 
	 * @param genRegNo
	 * @return
	 * @throws StudentException
	 */
	@GetMapping(path = "/students/general-register/{regNo}")
	public ResponseEntity<ResponseDto> getStudentDetailsForRegNo(@PathVariable(value = "regNo") int genRegNo)
			throws StudentException {
		log.info("Getting student details for gen reg no {}", genRegNo);
		StudentDetailsForRegNoResponseDto studentDetails = studentService.getStudentDetailsForRegNo(genRegNo);
		return responseUtil.populateSuccessResponseWithMessage(studentDetails,
				SuccessDetails.STUDENT_DETAILS_FETCHED_SUCCESSFULLY);
	}
	
	/**
	 * @param page
	 * @param size
	 * @param quickSearchText
	 * @return
	 * @throws StudentException
	 */
	@GetMapping(path = "/students/receivables")
	public ResponseEntity<ResponseDto> getFeeReceivables(@RequestParam(defaultValue = "0", required = false) int page, 
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(value = "quickSearch", required = false) String quickSearchText) throws StudentException {
		log.info("Getting Student's Fee Receivables");
		FeeReceivablesResponseDto feeReceivablesResponseDto = studentService.getFeeReceivables(page, size,
				quickSearchText);
		return responseUtil.populateSuccessResponseWithMessageAndPagination(
				feeReceivablesResponseDto.getFeeReceivableDetails(), feeReceivablesResponseDto.getTotalItems(),
				feeReceivablesResponseDto.getTotalPages(), feeReceivablesResponseDto.getCurrentPage(),
				SuccessDetails.RECEIVABLES_FETCHED_SUCCESSFULLY);
	}
	
	/**
	 * @return
	 * @throws StudentException
	 */
	@GetMapping(path = "/students/receivables/statistics")
	public ResponseEntity<ResponseDto> getFeeReceivableStats() throws StudentException {
		log.info("Getting Fee Receivables Statistics");
		FeeReceivablesStatsDto feeReceivablesStatsDto = studentService.getFeeReceivablesStatistics();
		return responseUtil.populateSuccessResponseWithMessage(feeReceivablesStatsDto,
				SuccessDetails.RECEIVABLES_STATS_FETCHED_SUCCESSFULLY);
	}
	
	/**
	 * @param studentId
	 * @return
	 * @throws StudentException 
	 */
	@GetMapping(path = "/students/{studentId}/fees-assigned")
	public ResponseEntity<ResponseDto> getStudentsFeesAssignedDetails(@PathVariable String studentId)
			throws StudentException {
		log.info("Getting fees assigned for the student id {}", studentId);
		List<StudentFeesAssignedDetailsDto> studentsFeesAssignedDetailsList = studentService
				.getStudentsFeesAssignedDetails(studentId);
		return responseUtil.populateSuccessResponseWithMessage(studentsFeesAssignedDetailsList,
				SuccessDetails.FEES_ASSIGNED_DETAILS_FETCHED_SUCCESSFULLY);
	}
	
	/**
	 * @param studentId
	 * @return
	 * @throws StudentException
	 */
	@GetMapping(path = "/students/{studentId}/fees-paid")
	public ResponseEntity<ResponseDto> getStudentsFeesPaidDetails(@PathVariable String studentId)
			throws StudentException {
		log.info("Getting fees paid details for the student id {}", studentId);
		List<StudentFeesPaidDetailsDto> studentsFeesAssignedDetailsList = studentService
				.getStudentsFeesPaidDetails(studentId);
		return responseUtil.populateSuccessResponseWithMessage(studentsFeesAssignedDetailsList,
				SuccessDetails.FEES_PAID_DETAILS_FETCHED_SUCCESSFULLY);
	}
	
	/**
	 * @param studentId
	 * @return
	 * @throws StudentException
	 */
	@GetMapping(path = "/students/{studentId}/receivables")
	public ResponseEntity<ResponseDto> getStudentsFeesReceivables(@PathVariable String studentId)
			throws StudentException {
		log.info("Getting Studnet fees receivable details for the student id {}", studentId);
		StudentFeesReceivableDetailsDto studentsFeesAssignedDetailsList = studentService
				.getStudentsFeesReceivableDetails(studentId);
		return responseUtil.populateSuccessResponseWithMessage(studentsFeesAssignedDetailsList,
				SuccessDetails.STUDENT_FEES_RECEIVABLES_FETCHED_SUCCESSFULLY);
	}
	
	/**
	 * @param studentId
	 * @param feesPaidTrxnRequest
	 * @return
	 * @throws StudentException
	 */
	@PostMapping(path = "/students/{studentId}/fees-paid")
	public ResponseEntity<ResponseDto> addStudentsFeesPaidDetails(@PathVariable String studentId,
			@Valid @RequestBody StudentFeesPaidTrxnRequestDto feesPaidTrxnRequest) throws StudentException {
		log.info("Saving student fees paid details for the student id {}", studentId);
		StudentFeesPaidTrxnResponseDto feesPaidTrxnResponseDto = studentService.addStudentFeesPaidDetails(studentId,
				feesPaidTrxnRequest);
		return responseUtil.populateSuccessResponseWithMessage(feesPaidTrxnResponseDto,
				SuccessDetails.TRXN_SAVED_SUCCESSFULLY);
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

}
