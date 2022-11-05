package com.example.demo.service;

import com.example.demo.exception.StaffException;
import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.FilterListRequestDto;
import com.example.demo.service.dto.StaffBasicDetailsDto;
import com.example.demo.service.dto.StaffDetailsListResponseDto;
import com.example.demo.service.dto.StffBasicDetailsResponseDto;

public interface StaffService {

	/**
	 * @param userBasicDetailsDto
	 * @return
	 * @throws StaffException
	 * @throws StudentException
	 */
	StffBasicDetailsResponseDto addStaffBasicDetails(StaffBasicDetailsDto userBasicDetailsDto)
			throws StaffException, StudentException;

	/**
	 * @param academicYear
	 * @param page
	 * @param size
	 * @param filterRequest
	 * @return
	 * @throws StaffException 
	 */
	StaffDetailsListResponseDto getStaffDetailsList(String academicYear, int page, int size,
			FilterListRequestDto filterRequest) throws StaffException;

}
