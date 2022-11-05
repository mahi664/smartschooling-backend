package com.example.demo.service.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.Gender;
import com.example.demo.constant.ServiceConstants;
import com.example.demo.data.entity.UserBasicDetails;
import com.example.demo.data.repository.CasteRepository;
import com.example.demo.data.repository.ReligionRepository;
import com.example.demo.data.repository.UserBasicDetailsRepository;
import com.example.demo.exception.StaffException;
import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.StaffBasicDetailsDto;

@Service
public class StaffValidator {

	private final Logger log = LoggerFactory.getLogger(StaffValidator.class);

	@Autowired
	UserBasicDetailsRepository userBasicDetailsRepository;

	@Autowired
	ReligionRepository religionRepository;

	@Autowired
	CasteRepository casteRepository;

	/**
	 * @param staffBasicDetailsDto
	 * @throws StaffException
	 * @throws StudentException
	 */
	public void validateStaffBasicDetailsRequest(StaffBasicDetailsDto staffBasicDetailsDto)
			throws StaffException, StudentException {
		log.info("Validating staff basic details for staff name {}", staffBasicDetailsDto.getFirstName());
		log.info("Validating is user is already registered");
		List<UserBasicDetails> userBasicDetails = userBasicDetailsRepository
				.getUserBasicDetailsByMobileNumber(staffBasicDetailsDto.getMobileNumber());
		if (!CollectionUtils.isEmpty(userBasicDetails)) {
			log.error("User record already registered for mobile number {}", staffBasicDetailsDto.getMobileNumber());
			throw new StaffException(ErrorDetails.DUPLICATE_STAFF_DETAILS);
		}

		log.info("Validating religion {}", staffBasicDetailsDto.getReligion());
		Optional.ofNullable(religionRepository.getReligionByCode(staffBasicDetailsDto.getReligion()))
				.orElseThrow(() -> new StudentException(ErrorDetails.RELIGION_NOT_FOUND));

		log.info("Validating caste {}", staffBasicDetailsDto.getCaste());
		Optional.ofNullable(casteRepository.getCasteDetailsByCaste(staffBasicDetailsDto.getCaste()))
				.orElseThrow(() -> new StudentException(ErrorDetails.CASTE_NOT_FOUND));

		log.info("Validating gender {}", staffBasicDetailsDto.getGender());
		if (!Arrays.asList(Gender.values()).stream().map(Gender::getGender).collect(Collectors.toList())
				.contains(staffBasicDetailsDto.getGender())) {
			log.info("Gender not found");
			throw new StudentException(ErrorDetails.GENDER_NOT_FOUND);
		}

		log.info("Validating Nationality {}", staffBasicDetailsDto.getNationality());
		if (!ServiceConstants.NATIONALITY_INDIAN.equals(staffBasicDetailsDto.getNationality())) {
			log.info("Invalid Nationality details");
			throw new StudentException(ErrorDetails.INVALID_NATIONALITY);
		}

		log.info("Validating Mobile Number {}", staffBasicDetailsDto.getMobileNumber());
		if (!staffBasicDetailsDto.getMobileNumber().matches(ServiceConstants.MOBILE_NUMBER_REGEX)) {
			log.info("Invalid Mobile Number");
			throw new StudentException(ErrorDetails.INVALID_MOBILE_NUMBER);
		}

		log.info("Validating alternate mobile number {}", staffBasicDetailsDto.getAlternateMobileNumber());
		if (!StringUtils.isEmpty(staffBasicDetailsDto.getAlternateMobileNumber())
				&& !staffBasicDetailsDto.getAlternateMobileNumber().matches(ServiceConstants.MOBILE_NUMBER_REGEX)) {
			log.info("Invalid Alternate Mobile Number");
			throw new StudentException(ErrorDetails.INVALID_ALTERNATE_MOBILE_NUMBER);
		}

		log.info("Validating Adhar Number {}", staffBasicDetailsDto.getAdharNumber());
		if (!StringUtils.isEmpty(staffBasicDetailsDto.getAdharNumber())
				&& !staffBasicDetailsDto.getAdharNumber().matches(ServiceConstants.ADHAR_NUMBER_REGEX)) {
			log.info("Invalid Adhar number");
			throw new StudentException(ErrorDetails.INVALID_ADHAR_NUMBER);
		}
	}

}
