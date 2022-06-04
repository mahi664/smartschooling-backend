package com.example.demo.service;

import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
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
	 * @return
	 * @throws StudentException
	 */
	public FetchStudentsResponseDto getStudentList(String academicYear, int page, int size) throws StudentException;

	/**
	 * 
	 * @param genRegNo
	 * @return
	 * @throws StudentException
	 */
	public StudentDetailsForRegNoResponseDto getStudentDetailsForRegNo(int genRegNo) throws StudentException;
}
