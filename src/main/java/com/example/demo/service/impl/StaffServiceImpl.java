package com.example.demo.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.UserBasicDetails;
import com.example.demo.data.entity.UserLoginDetails;
import com.example.demo.data.repository.UserBasicDetailsRepository;
import com.example.demo.data.repository.UserLoginDetailsRepository;
import com.example.demo.exception.StaffException;
import com.example.demo.exception.StudentException;
import com.example.demo.service.StaffService;
import com.example.demo.service.adapter.StaffServiceAdapter;
import com.example.demo.service.dto.FilterListRequestDto;
import com.example.demo.service.dto.StaffBasicDetailsDto;
import com.example.demo.service.dto.StaffDetailsListResponseDto;
import com.example.demo.service.dto.StffBasicDetailsResponseDto;
import com.example.demo.service.validator.StaffValidator;

@Service
public class StaffServiceImpl implements StaffService{

	private final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);
	
	@Autowired
	StaffValidator staffValidator;
	
	@Autowired
	StaffServiceAdapter staffServiceAdapter;
	
	@Autowired
	UserBasicDetailsRepository userBasicDetailsRepository;
	
	@Autowired
	UserLoginDetailsRepository userLoginDetailsRepository;
	
	@Override
	public StffBasicDetailsResponseDto addStaffBasicDetails(StaffBasicDetailsDto staffBasicDetailsDto)
			throws StaffException, StudentException {
		log.info("Adding basic details for staff name {}", staffBasicDetailsDto.getFirstName());
		staffValidator.validateStaffBasicDetailsRequest(staffBasicDetailsDto);
		String nextUserId = Integer.toString(userBasicDetailsRepository.getMaxUserId() + 1);
		UserBasicDetails userBasicDetails = staffServiceAdapter.getUserDetailsEntity(nextUserId, staffBasicDetailsDto);
		userBasicDetailsRepository.addUserBasicDetails(userBasicDetails);
		UserLoginDetails userLoginDetails = staffServiceAdapter.getUserLoginDetailEntity(nextUserId,
				staffBasicDetailsDto);
		userLoginDetailsRepository.addUserLoginDetails(userLoginDetails);
		return staffServiceAdapter.populateStaffBasicDetailsResponseDto(nextUserId, staffBasicDetailsDto);
	}

	@Override
	public StaffDetailsListResponseDto getStaffDetailsList(String academicYear, int page, int size,
			FilterListRequestDto filterRequestDto) throws StaffException {
		log.info("Fetching student list for academic year {}, page {} and size {}", academicYear, page, size);
		PageRequest pageable = PageRequest.of(page, size);
		Page<UserBasicDetails> pagedStudentData = Optional
				.ofNullable(userBasicDetailsRepository.getStaffDetails(academicYear, pageable, filterRequestDto))
				.orElseThrow(() -> new StaffException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND));
		return staffServiceAdapter.getStaffDetailsResponseDto(pagedStudentData);
	}
	
}
