package com.example.demo.service.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.Gender;
import com.example.demo.constant.ServiceConstants;
import com.example.demo.data.entity.AcademicDetails;
import com.example.demo.data.entity.Caste;
import com.example.demo.data.entity.ClassDetails;
import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.data.entity.Religion;
import com.example.demo.data.entity.Routes;
import com.example.demo.data.repository.AcademicDetailsRepository;
import com.example.demo.data.repository.AccountsRepository;
import com.example.demo.data.repository.CasteRepository;
import com.example.demo.data.repository.ClassDetailsRespository;
import com.example.demo.data.repository.GeneralRegisterRepository;
import com.example.demo.data.repository.ReligionRepository;
import com.example.demo.data.repository.RoutesRepository;
import com.example.demo.data.repository.StudentDetailsRepository;
import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.StudentFeesDueDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnRequestDto;
import com.example.demo.service.dto.StudentRegistrationDto;
import com.example.demo.utils.Constants;

@Service
public class StudentValidator {

	private final Logger log = LoggerFactory.getLogger(StudentValidator.class);

	@Autowired
	private GeneralRegisterRepository generalRegisterRepository;

	@Autowired
	private RoutesRepository routesRepository;

	@Autowired
	private ClassDetailsRespository classDetailsRespository;

	@Autowired
	private ReligionRepository religionRepository;

	@Autowired
	private CasteRepository casteRepository;

	@Autowired
	private AcademicDetailsRepository academicDetailsRepository;

	List<String> errorMessages = null;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private AccountsRepository accountsRepository;

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
		log.info("Validating religion {}", studentRegistrationDto.getReligion());
		Optional.ofNullable(religionRepository.getReligionByCode(studentRegistrationDto.getReligion()))
				.orElseThrow(() -> new StudentException(ErrorDetails.RELIGION_NOT_FOUND));
		log.info("Validating caste {}", studentRegistrationDto.getCaste());
		Optional.ofNullable(casteRepository.getCasteDetailsByCaste(studentRegistrationDto.getCaste()))
				.orElseThrow(() -> new StudentException(ErrorDetails.CASTE_NOT_FOUND));
		log.info("Validating gender {}", studentRegistrationDto.getGender());
		if (!Arrays.asList(Gender.values()).stream().map(Gender::getGender).collect(Collectors.toList())
				.contains(studentRegistrationDto.getGender())) {
			log.info("Gender not found");
			throw new StudentException(ErrorDetails.GENDER_NOT_FOUND);
		}
		log.info("Validating Nationality {}", studentRegistrationDto.getNationality());
		if (!ServiceConstants.NATIONALITY_INDIAN.equals(studentRegistrationDto.getNationality())) {
			log.info("Invalid Nationality details");
			throw new StudentException(ErrorDetails.INVALID_NATIONALITY);
		}
		log.info("Validating Academic Year {}", studentRegistrationDto.getAcademicYear());
		AcademicDetails academicDetails = Optional
				.ofNullable(academicDetailsRepository.getAcademicDetailById(studentRegistrationDto.getAcademicYear()))
				.orElseThrow(() -> new StudentException(ErrorDetails.ACADEMIC_DETAILS_NOT_FOUND));
		log.info("Validating Admission date {}", studentRegistrationDto.getAdmissionDate());
		if (!(studentRegistrationDto.getAdmissionDate().after(academicDetails.getAcademicStartDate())
				&& studentRegistrationDto.getAdmissionDate().before(academicDetails.getAcademicEndDate()))) {
			log.info("Invalid Admission Date {}", studentRegistrationDto.getAdmissionDate());
			throw new StudentException(ErrorDetails.INVALID_ADMISSION_DATE);
		}
		log.info("Validating Mobile Number {}", studentRegistrationDto.getMobileNumber());
		if (!studentRegistrationDto.getMobileNumber().matches(ServiceConstants.MOBILE_NUMBER_REGEX)) {
			log.info("Invalid Mobile Number");
			throw new StudentException(ErrorDetails.INVALID_MOBILE_NUMBER);
		}
		log.info("Validating alternate mobile number {}", studentRegistrationDto.getAlternateMobileNumber());
		if (!StringUtils.isEmpty(studentRegistrationDto.getAlternateMobileNumber())
				&& !studentRegistrationDto.getAlternateMobileNumber().matches(ServiceConstants.MOBILE_NUMBER_REGEX)) {
			log.info("Invalid Alternate Mobile Number");
			throw new StudentException(ErrorDetails.INVALID_ALTERNATE_MOBILE_NUMBER);
		}
		log.info("Validating Adhar Number {}", studentRegistrationDto.getAdharNumber());
		if (!StringUtils.isEmpty(studentRegistrationDto.getAdharNumber())
				&& !studentRegistrationDto.getAdharNumber().matches(ServiceConstants.ADHAR_NUMBER_REGEX)) {
			log.info("Invalid Adhar number");
			throw new StudentException(ErrorDetails.INVALID_ADHAR_NUMBER);
		}
	}

	/**
	 * Validate student import data
	 * 
	 * @param studentRegistrationDtoList
	 * @return
	 * @throws StudentException
	 */
	public List<String> validateStudentImportData(List<StudentRegistrationDto> studentRegistrationDtoList)
			throws StudentException {
		errorMessages = new ArrayList<>();
		validateGenRegNos(studentRegistrationDtoList);
		validateTransportDetails(studentRegistrationDtoList);
		validateAdmissionStd(studentRegistrationDtoList);
		validateReligion(studentRegistrationDtoList);
		validateCaste(studentRegistrationDtoList);
		validateGender(studentRegistrationDtoList);
		validateNationality(studentRegistrationDtoList);
		validateAcademicYear(studentRegistrationDtoList);
		validateOtherDetails(studentRegistrationDtoList);
		return errorMessages;
	}

	/**
	 * Validate Mobile Number, Alternate Mobile Number and Adhar number
	 * @param studentRegistrationDtoList
	 */
	private void validateOtherDetails(List<StudentRegistrationDto> studentRegistrationDtoList) {
		log.info("Validating mobile number for {} students", studentRegistrationDtoList.size());
		List<String> filteredStudentNames = studentRegistrationDtoList.stream()
				.filter(dto -> !dto.getMobileNumber().matches(ServiceConstants.MOBILE_NUMBER_REGEX))
				.map(StudentRegistrationDto::getFirstName).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(filteredStudentNames)) {
			log.info("Invalid Mobile number for some of the students");
			errorMessages.add("Invalid Mobile number for " + filteredStudentNames.toString());
		}

		filteredStudentNames = studentRegistrationDtoList.stream()
				.filter(dto -> (!StringUtils.isEmpty(dto.getAlternateMobileNumber())
						&& !dto.getAlternateMobileNumber().matches(ServiceConstants.MOBILE_NUMBER_REGEX)))
				.map(StudentRegistrationDto::getFirstName).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(filteredStudentNames)) {
			log.info("Alternate mobile number is not valid for some of the students");
			errorMessages.add("Invalid Alternate Mobile Number for " + filteredStudentNames.toString());
		}

		filteredStudentNames = studentRegistrationDtoList.stream()
				.filter(dto -> (!StringUtils.isEmpty(dto.getAdharNumber())
						&& !dto.getAdharNumber().matches(ServiceConstants.ADHAR_NUMBER_REGEX)))
				.map(StudentRegistrationDto::getFirstName).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(filteredStudentNames)) {
			log.info("Adhar number is not valid for some of the students");
			errorMessages.add("Invalid Adhar Number for " + filteredStudentNames.toString());
		}
	}

	/**
	 * Validate academic year and admission date
	 * 
	 * @param studentRegistrationDtoList
	 * @throws StudentException
	 */
	private void validateAcademicYear(List<StudentRegistrationDto> studentRegistrationDtoList) throws StudentException {
		log.info("Validating academic year for {} students", studentRegistrationDtoList.size());
		List<AcademicDetails> academicDetails;
		try {
			academicDetails = academicDetailsRepository.getAcademicDetails();
		} catch (StudentException ex) {
			log.error("Error while fetching academic details");
			throw ex;
		}
		List<String> academicIDs = academicDetails.stream().map(AcademicDetails::getAcademicId)
				.collect(Collectors.toList());
		List<String> filteredStduentDetails = studentRegistrationDtoList.stream()
				.filter(dto -> !academicIDs.contains(dto.getAcademicYear())).map(StudentRegistrationDto::getFirstName)
				.collect(Collectors.toList());
		if (filteredStduentDetails != null && !filteredStduentDetails.isEmpty()) {
			log.info("Invalid Academic details for some of the students");
			errorMessages.add("Invalid academic year details for " + filteredStduentDetails.toString());
		} else {
			validateAdmissionDate(studentRegistrationDtoList, academicDetails);
		}
	}

	/**
	 * Validate Admission date according to academic year
	 * 
	 * @param studentRegistrationDtoList
	 * @param academicDetails
	 */
	private void validateAdmissionDate(List<StudentRegistrationDto> studentRegistrationDtoList,
			List<AcademicDetails> academicDetails) {
		log.info("Validating admission date for {} students");
		Map<String, AcademicDetails> academicId2DetailsMap = academicDetails.stream()
				.collect(Collectors.toMap(AcademicDetails::getAcademicYear, Function.identity()));
		List<String> filteredStduentDetails = studentRegistrationDtoList.stream().filter(dto -> {
			AcademicDetails academicDetailObj = academicId2DetailsMap.get(dto.getAcademicYear());
			return !(dto.getAdmissionDate().after(academicDetailObj.getAcademicStartDate())
					&& dto.getAdmissionDate().before(academicDetailObj.getAcademicEndDate()));
		}).map(StudentRegistrationDto::getFirstName).collect(Collectors.toList());
		if (filteredStduentDetails != null && !filteredStduentDetails.isEmpty()) {
			log.info("Invalid admission date for some of the students");
			errorMessages.add("Invalid Admission Date for " + filteredStduentDetails.toString());
		}
	}

	/**
	 * Validate Nationality
	 * 
	 * @param studentRegistrationDtoList
	 */
	private void validateNationality(List<StudentRegistrationDto> studentRegistrationDtoList) {
		log.info("Validating nationality for {} students", studentRegistrationDtoList.size());
		List<String> filteredStudentList = studentRegistrationDtoList.stream()
				.filter(dto -> !ServiceConstants.NATIONALITY_INDIAN.equals(dto.getNationality()))
				.map(StudentRegistrationDto::getFirstName).collect(Collectors.toList());
		if (filteredStudentList != null && !filteredStudentList.isEmpty()) {
			log.info("Invalid Nationality details for some of the students");
			errorMessages.add("Invalid Nationality Details for " + filteredStudentList.toString());
		}
	}

	/**
	 * Validate Gender
	 * 
	 * @param studentRegistrationDtoList
	 */
	private void validateGender(List<StudentRegistrationDto> studentRegistrationDtoList) {
		log.info("Validating gender details for {} students", studentRegistrationDtoList.size());
		List<String> genders = Arrays.asList(Gender.values()).stream().map(Gender::getGender)
				.collect(Collectors.toList());
		List<String> filteredStudentDetails = studentRegistrationDtoList.stream()
				.filter(dto -> !genders.contains(dto.getGender())).map(StudentRegistrationDto::getFirstName)
				.collect(Collectors.toList());
		if (filteredStudentDetails != null && !filteredStudentDetails.isEmpty()) {
			log.info("Invalid gender details for some of the students");
			errorMessages.add("Invalid Gender Details for " + filteredStudentDetails.toString());
		}
	}

	/**
	 * Validate caste details
	 * 
	 * @param studentRegistrationDtoList
	 * @throws StudentException
	 */
	private void validateCaste(List<StudentRegistrationDto> studentRegistrationDtoList) throws StudentException {
		log.info("Validating caste details for {} students", studentRegistrationDtoList.size());
		List<Caste> casteList = null;
		try {
			casteList = casteRepository.getCasteDetails();
		} catch (StudentException ex) {
			log.error("Error while fetching caste details");
			throw ex;
		}
		if (casteList == null || casteList.isEmpty()) {
			log.info("caste details not found");
			throw new StudentException(ErrorDetails.CASTE_NOT_FOUND);
		}
		List<String> castes = casteList.stream().map(Caste::getCaste).collect(Collectors.toList());
		List<String> filteredStudentDetails = studentRegistrationDtoList.stream()
				.filter(dto -> !castes.contains(dto.getCaste())).map(StudentRegistrationDto::getFirstName)
				.collect(Collectors.toList());
		if (filteredStudentDetails != null && !filteredStudentDetails.isEmpty()) {
			log.info("invalid caste details for some students");
			errorMessages.add("Invalid caste details for " + filteredStudentDetails.toString());
		}
	}

	/**
	 * validate religion details for student list
	 * 
	 * @param studentRegistrationDtoList
	 * @throws StudentException
	 */
	private void validateReligion(List<StudentRegistrationDto> studentRegistrationDtoList) throws StudentException {
		log.info("Validating religion details for {} students", studentRegistrationDtoList.size());
		List<Religion> religions;
		try {
			religions = religionRepository.getReligions();
		} catch (StudentException ex) {
			log.error("Error while getting religion details");
			throw ex;
		}

		if (religions == null || religions.isEmpty()) {
			log.info("religion details not found");
			throw new StudentException(ErrorDetails.RELIGION_NOT_FOUND);
		}

		List<String> religionCode = religions.stream().map(Religion::getCode).collect(Collectors.toList());
		List<String> filteredStudentList = studentRegistrationDtoList.stream()
				.filter(dto -> !religionCode.contains(dto.getReligion())).map(StudentRegistrationDto::getFirstName)
				.collect(Collectors.toList());
		if (filteredStudentList != null && !filteredStudentList.isEmpty()) {
			log.info("Invalid Religion for some students");
			errorMessages.add("Invalid Religion for " + filteredStudentList.toString());
		}
	}

	/**
	 * validate admission std for students list
	 * 
	 * @param studentRegistrationDtoList
	 * @throws StudentException
	 */
	private void validateAdmissionStd(List<StudentRegistrationDto> studentRegistrationDtoList) throws StudentException {
		log.info("Validating class details for {} students", studentRegistrationDtoList.size());
		List<ClassDetails> classDetailsList;
		try {
			classDetailsList = classDetailsRespository.getClassDetails();
		} catch (StudentException ex) {
			log.error("Error while getting classes details");
			throw ex;
		}
		if (classDetailsList == null || classDetailsList.isEmpty()) {
			log.info("Classes details not present in the system");
			throw new StudentException(ErrorDetails.CLASS_DETAILS_NOT_FOUND);
		}
		List<String> classIDs = classDetailsList.stream().map(ClassDetails::getClassId).collect(Collectors.toList());
		List<String> filteredStudentDtos = studentRegistrationDtoList.stream()
				.filter(dto -> !classIDs.contains(dto.getAdmissionStd())).map(StudentRegistrationDto::getFirstName)
				.collect(Collectors.toList());
		if (filteredStudentDtos != null && !filteredStudentDtos.isEmpty()) {
			log.info("Invalid admission std for some students");
			errorMessages.add("Invalid Admission std for " + filteredStudentDtos.toString());
		}
	}

	/**
	 * validate transport details for student list
	 * 
	 * @param studentRegistrationDtoList
	 * @throws StudentException
	 */
	private void validateTransportDetails(List<StudentRegistrationDto> studentRegistrationDtoList)
			throws StudentException {
		log.info("validating transaport details for {} students", studentRegistrationDtoList.size());
		List<StudentRegistrationDto> filteredStudentDtos = studentRegistrationDtoList.stream().filter(
				dto -> !dto.isTransportOpted() && (dto.getRoute() != null && dto.getRoute() != Constants.BLANK_STRING))
				.collect(Collectors.toList());
		if (filteredStudentDtos != null && !filteredStudentDtos.isEmpty()) {
			log.info("transport opted should be true for if route is selected");
			errorMessages
					.add("Either transport opted should be true or route should be blank for " + filteredStudentDtos
							.stream().map(StudentRegistrationDto::getFirstName).collect(Collectors.toList()));
		}
		filteredStudentDtos = studentRegistrationDtoList.stream().filter(dto -> dto.isTransportOpted())
				.collect(Collectors.toList());
		if (filteredStudentDtos != null && !filteredStudentDtos.isEmpty()) {
			List<Routes> routes;
			try {
				routes = routesRepository.getRoutes(filteredStudentDtos.stream().map(StudentRegistrationDto::getRoute)
						.collect(Collectors.toList()));
			} catch (StudentException ex) {
				log.error("Error occured while transport details validation");
				throw ex;
			}
			if (routes != null && !routes.isEmpty()) {
				List<String> routeIDs = routes.stream().map(Routes::getRouteId).collect(Collectors.toList());
				filteredStudentDtos = filteredStudentDtos.stream().filter(dto -> !routeIDs.contains(dto.getRoute()))
						.collect(Collectors.toList());
			}
			if (filteredStudentDtos.size() > 0) {
				log.info("Invalid route id ");
				errorMessages.add("Invalid routes details for " + filteredStudentDtos.stream()
						.map(StudentRegistrationDto::getFirstName).collect(Collectors.toList()).toString());
			}
		}
	}

	/**
	 * validate gen reg no for list of students
	 * 
	 * @param studentRegistrationDtoList
	 * @throws StudentException
	 */
	private void validateGenRegNos(List<StudentRegistrationDto> studentRegistrationDtoList) throws StudentException {
		log.info("Validating gen reg no for {} students", studentRegistrationDtoList.size());
		List<Integer> genRegNos = studentRegistrationDtoList.stream().map(StudentRegistrationDto::getGenRegNo)
				.collect(Collectors.toList());
		if (genRegNos.size() == studentRegistrationDtoList.size()) {
			try {
				List<GeneralRegister> generalRegisterDetails = generalRegisterRepository
						.getGeneralRegisterDetails(genRegNos);
				if (generalRegisterDetails != null && !generalRegisterDetails.isEmpty()) {
					errorMessages.add(
							generalRegisterDetails.stream().map(GeneralRegister::getRegNo).collect(Collectors.toList())
									.toString() + " " + ErrorDetails.DUPLICATE_GEN_REG_NO.getErrorDescription());
				}
			} catch (StudentException ex) {
				log.error("Error occured while gen reg validation");
				throw ex;
			}
		} else {
			log.info("Gen reg no can not be blank");
			errorMessages.add("Gen Reg No " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
		}
	}

	/**
	 * @param studentId
	 * @param feesPaidTrxnRequest
	 * @param studentFeesDueDetails 
	 * @throws StudentException 
	 */
	public void validateStudentFeesPaidDetailsRequest(String studentId,
			StudentFeesPaidTrxnRequestDto feesPaidTrxnRequest, List<StudentFeesDueDetailsDto> studentFeesDueDetails)
			throws StudentException {
		log.info("validating student fees paid request for student id {}", studentId);
		Optional.ofNullable(studentDetailsRepository.getStudentDetails(studentId))
				.orElseThrow(() -> new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND));

		log.info("Validating account details for account id {}", feesPaidTrxnRequest.getAccountId());
		accountsRepository.getAccounts(feesPaidTrxnRequest.getAccountId());

		log.info("Validating academic years for fees paid trxn request");
		List<String> academicYears = feesPaidTrxnRequest.getFeesPaidTrxnDetailsDtos().stream()
				.map(StudentFeesPaidTrxnDetailsDto::getAcademicId).collect(Collectors.toList());
		List<AcademicDetails> academicDetails = academicDetailsRepository.getAcademicDetails();
		academicYears
				.removeAll(academicDetails.stream().map(AcademicDetails::getAcademicId).collect(Collectors.toList()));
		if (!CollectionUtils.isEmpty(academicYears)) {
			log.error("Invalid academic years {}", academicYears);
			throw new StudentException(ErrorDetails.ACADEMIC_DETAILS_NOT_FOUND,
					Stream.of("Invalid Academic years " + academicYears.toString()).collect(Collectors.toList()));
		}

		if (CollectionUtils.isEmpty(studentFeesDueDetails)) {
			log.error("No Dues found for student id {}", studentId);
			throw new StudentException(ErrorDetails.NO_FEE_DUES_AVAILABLE);
		}

		Map<String, Map<String, List<StudentFeesDueDetailsDto>>> academicId2FeeId2DetailsMap = studentFeesDueDetails
				.stream().collect(Collectors.groupingBy(StudentFeesDueDetailsDto::getAcademicYear,
						Collectors.groupingBy(StudentFeesDueDetailsDto::getFeeId)));
		errorMessages = new ArrayList<>();
		for (StudentFeesPaidTrxnDetailsDto feesPaidTrxnDetailsDto : feesPaidTrxnRequest.getFeesPaidTrxnDetailsDtos()) {
			if (!academicId2FeeId2DetailsMap.containsKey(feesPaidTrxnDetailsDto.getAcademicId())) {
				errorMessages.add("No Fee Dues for academic year " + feesPaidTrxnDetailsDto.getAcademicId());
			} else if (!academicId2FeeId2DetailsMap.get(feesPaidTrxnDetailsDto.getAcademicId())
					.containsKey(feesPaidTrxnDetailsDto.getFeeId())) {
				errorMessages.add("No Dues for Fee Id " + feesPaidTrxnDetailsDto.getFeeId() + " in academic year "
						+ feesPaidTrxnDetailsDto.getAcademicId());
			} else {
				Optional<StudentFeesDueDetailsDto> feeDueDetailsOptional = academicId2FeeId2DetailsMap
						.get(feesPaidTrxnDetailsDto.getAcademicId()).get(feesPaidTrxnDetailsDto.getFeeId()).stream()
						.findFirst();
				if (!feeDueDetailsOptional.isPresent()) {
					errorMessages.add("No Dues for Fee Id " + feesPaidTrxnDetailsDto.getFeeId() + " in academic year "
							+ feesPaidTrxnDetailsDto.getAcademicId());
				} else if (feeDueDetailsOptional.get().getAmount() < feesPaidTrxnDetailsDto.getAmount()) {
					errorMessages.add("Invalid Due amount for fee id " + feesPaidTrxnDetailsDto.getFeeId()
							+ " in academic year " + feesPaidTrxnDetailsDto.getAcademicId());
				}
			}
		}
		if (!CollectionUtils.isEmpty(errorMessages)) {
			throw new StudentException(ErrorDetails.FAILED_TO_VALIDATE_REQUEST_DATA, errorMessages);
		}
	}

}
