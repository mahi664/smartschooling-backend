package com.example.demo.service.validator;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.repository.ClassDetailsRespository;
import com.example.demo.data.repository.GeneralRegisterRepository;
import com.example.demo.data.repository.RoutesRepository;
import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.StudentRegistrationDto;

@Service
public class StudentValidator {

	private final Logger log = LoggerFactory.getLogger(StudentValidator.class);

	@Autowired
	private GeneralRegisterRepository generalRegisterRepository;

	@Autowired
	private RoutesRepository routesRepository;
	
	@Autowired
	private ClassDetailsRespository classDetailsRespository;

	/**
	 * 
	 * @param studentRegistrationDto
	 * @throws StudentException
	 */
	public void validateStudentRegistrationRequest(StudentRegistrationDto studentRegistrationDto)
			throws StudentException {
		log.info("Validating gen reg no {} if it is alredy present", studentRegistrationDto.getGenRegNo());
		if (Optional
				.ofNullable(generalRegisterRepository.getGeneraRegisterDetails(studentRegistrationDto.getGenRegNo()))
				.isPresent()) {
			log.info("General register no {} is already present", studentRegistrationDto.getGenRegNo());
			throw new StudentException(ErrorDetails.DUPLICATE_GEN_REG_NO);
		}
		if (studentRegistrationDto.isTransportOpted()) {
			log.info("Validating route {}", studentRegistrationDto.getRoute());
			Optional.ofNullable(routesRepository.getRoute(studentRegistrationDto.getRoute()))
					.orElseThrow(() -> new StudentException(ErrorDetails.ROUTE_NOT_FOUND));
		}
		log.info("Validating admission std {}", studentRegistrationDto.getAdmissionStd());
		Optional.ofNullable(classDetailsRespository.getClassDetails(studentRegistrationDto.getAdmissionStd()))
				.orElseThrow(() -> new StudentException(ErrorDetails.ADMISSION_STD_NOT_FOUND));
	}

}
