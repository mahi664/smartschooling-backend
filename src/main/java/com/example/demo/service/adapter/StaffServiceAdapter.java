package com.example.demo.service.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.data.entity.UserBasicDetails;
import com.example.demo.data.entity.UserLoginDetails;
import com.example.demo.service.dto.StaffBasicDetailsDto;
import com.example.demo.service.dto.StaffDetailsListResponseDto;
import com.example.demo.service.dto.StffBasicDetailsResponseDto;
import com.example.demo.utils.Constants;

@Service
public class StaffServiceAdapter {

	private final Logger log = LoggerFactory.getLogger(StaffServiceAdapter.class);
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * @param userId
	 * @param staffBasicDetailsDto
	 * @return
	 */
	public StffBasicDetailsResponseDto populateStaffBasicDetailsResponseDto(String userId,
			StaffBasicDetailsDto staffBasicDetailsDto) {
		log.info("Populating staff basic details response");
		return StffBasicDetailsResponseDto.builder().firstName(staffBasicDetailsDto.getFirstName())
				.lastName(staffBasicDetailsDto.getLastName()).userId(userId).build();
	}

	/**
	 * @param userId
	 * @param staffBasicDetailsDto
	 * @return
	 */
	public UserBasicDetails getUserDetailsEntity(String userId, StaffBasicDetailsDto staffBasicDetailsDto) {
		log.info("Preparing user basic details entity for user id {}", userId);
		return UserBasicDetails.builder().address(staffBasicDetailsDto.getAddress())
				.adhar(staffBasicDetailsDto.getAdharNumber())
				.alternateMobile(staffBasicDetailsDto.getAlternateMobileNumber())
				.birthDate(staffBasicDetailsDto.getBirthDate()).caste(staffBasicDetailsDto.getCaste())
				.email(staffBasicDetailsDto.getEmailId()).firstName(staffBasicDetailsDto.getFirstName())
				.gender(staffBasicDetailsDto.getGender()).lastName(staffBasicDetailsDto.getLastName())
				.maritalStatus(staffBasicDetailsDto.getMaritalStatus()).middleName(staffBasicDetailsDto.getMiddleName())
				.mobile(staffBasicDetailsDto.getMobileNumber()).nationality(staffBasicDetailsDto.getNationality())
				.religion(staffBasicDetailsDto.getReligion()).userId(userId).build();
	}

	/**
	 * @param userId
	 * @param staffBasicDetailsDto
	 * @return
	 */
	public UserLoginDetails getUserLoginDetailEntity(String userId, StaffBasicDetailsDto staffBasicDetailsDto) {
		String userName = staffBasicDetailsDto.getLastName().charAt(0) + "" + staffBasicDetailsDto.getFirstName() + ""
				+ userId;
		String password = bCryptPasswordEncoder.encode(Constants.DEFAULT_PASSWORD);
		return UserLoginDetails.builder().password(password).userId(userId).userName(userName).build();
	}

	/**
	 * @param pagedStaffData
	 * @return
	 */
	public StaffDetailsListResponseDto getStaffDetailsResponseDto(Page<UserBasicDetails> pagedStaffData) {
		log.info("populating staff details list resposne dto");
		List<StaffBasicDetailsDto> staffBasicDetailsDtos = pagedStaffData.getContent().stream()
				.map(mapper -> getStaffDetailsDto(mapper)).collect(Collectors.toList());
		return StaffDetailsListResponseDto.builder().currentPage(pagedStaffData.getNumber())
				.totalItems(pagedStaffData.getTotalElements()).totalPages(pagedStaffData.getTotalPages())
				.staffDetailsDtos(staffBasicDetailsDtos).build();
	}

	/**
	 * @param userBasicDetails
	 * @return
	 */
	private StaffBasicDetailsDto getStaffDetailsDto(UserBasicDetails userBasicDetails) {
		log.info("Populating staff basic details dto");
		return StaffBasicDetailsDto.builder().address(userBasicDetails.getAddress())
				.adharNumber(userBasicDetails.getAdhar()).alternateMobileNumber(userBasicDetails.getAlternateMobile())
				.birthDate(userBasicDetails.getBirthDate()).caste(userBasicDetails.getCaste())
				.emailId(userBasicDetails.getEmail()).firstName(userBasicDetails.getFirstName())
				.gender(userBasicDetails.getGender()).lastName(userBasicDetails.getLastName())
				.maritalStatus(userBasicDetails.getMaritalStatus()).middleName(userBasicDetails.getMiddleName())
				.mobileNumber(userBasicDetails.getMobile()).nationality(userBasicDetails.getNationality())
				.religion(userBasicDetails.getReligion()).userId(userBasicDetails.getUserId()).build();
	}

}
