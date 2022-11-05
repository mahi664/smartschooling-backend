package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.FeeReceivablesResponseDto;
import com.example.demo.service.dto.FeeReceivablesStatsDto;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
import com.example.demo.service.dto.StudentFeesAssignedDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnRequestDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnResponseDto;
import com.example.demo.service.dto.StudentFeesReceivableDetailsDto;
import com.example.demo.service.dto.StudentImportResponseDto;
import com.example.demo.service.dto.FilterListRequestDto;
import com.example.demo.service.dto.StudentRegistrationDto;
import com.example.demo.service.dto.StudentRegistrationResponseDto;

public interface StudentService {

	/**
	 * 
	 * @param studentRegistrationDto
	 * @return
	 * @throws StudentException
	 */
	public StudentRegistrationResponseDto registerNewStudent(StudentRegistrationDto studentRegistrationDto) throws StudentException;

	/**
	 * 
	 * @param academicYear
	 * @param page
	 * @param size
	 * @param studentListRequestDto 
	 * @return
	 * @throws StudentException
	 */
	public FetchStudentsResponseDto getStudentList(String academicYear, int page, int size, FilterListRequestDto studentListRequestDto) throws StudentException;

	/**
	 * 
	 * @param genRegNo
	 * @return
	 * @throws StudentException
	 */
	public StudentDetailsForRegNoResponseDto getStudentDetailsForRegNo(int genRegNo) throws StudentException;

	/**
	 * 
	 * @param file
	 * @return 
	 * @throws StudentException 
	 */
	public List<StudentImportResponseDto> importStudentDetailsFromFile(MultipartFile file) throws StudentException;

	/**
	 * Get Fee Receivables
	 * @param quickSearchText 
	 * @param size 
	 * @param page 
	 * 
	 * @return
	 * @throws StudentException 
	 */
	public FeeReceivablesResponseDto getFeeReceivables(int page, int size, String quickSearchText) throws StudentException;

	/**
	 * Get Fee Receivables Statistics
	 * 
	 * @return
	 * @throws StudentException 
	 */
	public FeeReceivablesStatsDto getFeeReceivablesStatistics() throws StudentException;

	/**
	 * @param studentId
	 * @return
	 * @throws StudentException 
	 */
	public List<StudentFeesAssignedDetailsDto> getStudentsFeesAssignedDetails(String studentId) throws StudentException;

	/**
	 * @param studentId
	 * @return
	 * @throws StudentException 
	 */
	public List<StudentFeesPaidDetailsDto> getStudentsFeesPaidDetails(String studentId) throws StudentException;

	/**
	 * @param studentId
	 * @return
	 * @throws StudentException 
	 */
	public StudentFeesReceivableDetailsDto getStudentsFeesReceivableDetails(String studentId) throws StudentException;

	/**
	 * @param studentId
	 * @param feesPaidTrxnRequest
	 * @return
	 * @throws StudentException 
	 */
	public StudentFeesPaidTrxnResponseDto addStudentFeesPaidDetails(String studentId,
			StudentFeesPaidTrxnRequestDto feesPaidTrxnRequest) throws StudentException;
}
