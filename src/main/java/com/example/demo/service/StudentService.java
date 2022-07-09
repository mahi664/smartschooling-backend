package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
import com.example.demo.service.dto.StudentImportResponseDto;
import com.example.demo.service.dto.StudentListRequestDto;
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
	public FetchStudentsResponseDto getStudentList(String academicYear, int page, int size, StudentListRequestDto studentListRequestDto) throws StudentException;

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
}
