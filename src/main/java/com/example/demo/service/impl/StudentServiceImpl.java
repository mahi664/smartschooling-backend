package com.example.demo.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.FeeReceivableDetails;
import com.example.demo.data.entity.FeeTypes;
import com.example.demo.data.entity.FeesPaidAmnt;
import com.example.demo.data.entity.FeesTotalReceivableAmnt;
import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.data.entity.StudentBasicDetails;
import com.example.demo.data.entity.StudentClassDetails;
import com.example.demo.data.entity.StudentFeeCollectionTransaction;
import com.example.demo.data.entity.StudentFeeCollectionTransactionDetails;
import com.example.demo.data.entity.StudentFeePaidDetails;
import com.example.demo.data.entity.StudentFeesAssignedDetails;
import com.example.demo.data.entity.StudentFeesDetails;
import com.example.demo.data.entity.StudentFeesPaidDetails;
import com.example.demo.data.entity.StudentTransportDetails;
import com.example.demo.data.repository.FeeTypesRepository;
import com.example.demo.data.repository.GeneralRegisterRepository;
import com.example.demo.data.repository.StudentClassDetailsRepository;
import com.example.demo.data.repository.StudentDetailsRepository;
import com.example.demo.data.repository.StudentFeeCollectionRepository;
import com.example.demo.data.repository.StudentFeesDetailsRepository;
import com.example.demo.data.repository.StudentTransportDetailsRepository;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.StudentException;
import com.example.demo.service.StudentService;
import com.example.demo.service.adapter.StudentServiceAdapter;
import com.example.demo.service.dto.FeeReceivableDetailsDto;
import com.example.demo.service.dto.FeeReceivablesResponseDto;
import com.example.demo.service.dto.FeeReceivablesStatsDto;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
import com.example.demo.service.dto.StudentFeesAssignedDetailsDto;
import com.example.demo.service.dto.StudentFeesDueDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidDetailsDto;
import com.example.demo.service.dto.StudentFeesPaidDetailsWrapperDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnRequestDto;
import com.example.demo.service.dto.StudentFeesPaidTrxnResponseDto;
import com.example.demo.service.dto.StudentFeesReceivableDetailsDto;
import com.example.demo.service.dto.StudentImportData;
import com.example.demo.service.dto.StudentImportResponseDto;
import com.example.demo.service.dto.FilterListRequestDto;
import com.example.demo.service.dto.StudentRegistrationDto;
import com.example.demo.service.dto.StudentRegistrationResponseDto;
import com.example.demo.service.validator.StudentValidator;
import com.example.demo.utils.FileUtils;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	@Autowired
	private StudentServiceAdapter studentServiceAdapter;
	
	@Autowired
	private StudentValidator studentValidator;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private GeneralRegisterRepository generalRegisterRepository;
	
	@Autowired
	private StudentClassDetailsRepository studentClassDetailsRepository;
	
	@Autowired
	private StudentTransportDetailsRepository studentTransportDetailsRepository;
	
	@Autowired
	private FeeTypesRepository feeTypesRepository;
	
	@Autowired
	private StudentFeesDetailsRepository studentFeesDetailsRepository;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Autowired
	private StudentFeeCollectionRepository studentFeeCollectionRepository;

	@Override
	@Transactional(rollbackFor = {StudentException.class, Exception.class})
	public StudentRegistrationResponseDto registerNewStudent(StudentRegistrationDto studentRegistrationDto)
			throws StudentException {
		log.info("new student registration for student name: {}", studentRegistrationDto.getFirstName());
		studentValidator.validateStudentRegistrationRequest(studentRegistrationDto);
		String nextStudentId = Integer.toString(studentDetailsRepository.getMaxStudentId() + 1);
		StudentBasicDetails studentBasicDetails = studentServiceAdapter.getStudentBasicDetails(nextStudentId,
				studentRegistrationDto);
		studentDetailsRepository.addStudentBasicDeails(studentBasicDetails);
		GeneralRegister generalRegister = studentServiceAdapter.getGeneralRegister(nextStudentId,
				studentRegistrationDto);
		generalRegisterRepository.addNewStudentInGeneralRegister(generalRegister);
		StudentClassDetails studentClassDetails = studentServiceAdapter.getStudentClassDetails(nextStudentId,
				studentRegistrationDto);
		studentClassDetailsRepository.addStudentClassDetails(studentClassDetails);
		if (studentRegistrationDto.isTransportOpted()) {
			StudentTransportDetails studentTransportDetails = studentServiceAdapter
					.getStudentTransportDetails(nextStudentId, studentRegistrationDto);
			studentTransportDetailsRepository.addSudentTransportDetails(studentTransportDetails);
		}
		Map<String, String> feeName2IdMap = feeTypesRepository.getFeeTypes().stream()
				.collect(Collectors.toMap(FeeTypes::getFeeName, FeeTypes::getFeeId));
		List<StudentFeesDetails> studentFeesDetails = studentServiceAdapter.getStudentFeesDetails(nextStudentId,
				studentRegistrationDto, feeName2IdMap);
		studentFeesDetailsRepository.addNewStudentFeesDetails(studentFeesDetails);
		return studentServiceAdapter.getStudentRegistrationResponse(nextStudentId, studentRegistrationDto);
	}

	@Override
	public FetchStudentsResponseDto getStudentList(String academicYear, int page, int size, FilterListRequestDto studentListRequestDto)
			throws StudentException {
		log.info("Fetching student list for academic year {}, page {} and size {}", academicYear, page, size);
		PageRequest pageable = PageRequest.of(page, size);
		Page<StudentBasicDetails> pagedStudentData = Optional
				.ofNullable(studentDetailsRepository.getStudentDetails(academicYear, pageable, studentListRequestDto))
				.orElseThrow(() -> new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND));
		return studentServiceAdapter.getFetchStudentResponseDto(pagedStudentData);
	}

	@Override
	public StudentDetailsForRegNoResponseDto getStudentDetailsForRegNo(int genRegNo) throws StudentException {
		log.info("Getting student details for gen reg no {}", genRegNo);
		GeneralRegister generalRegisterDetails = null;
		try {
			generalRegisterDetails = generalRegisterRepository.getGeneraRegisterDetails(genRegNo);
		} catch (StudentException ex) {
			log.error("Error occured while fetching general register details for {}", genRegNo);
			throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND, ex);
		}
		if (generalRegisterDetails == null) {
			log.info("No record found in general register details for {}", genRegNo);
			throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND);
		}
		StudentBasicDetails studentBasicDetails;
		try {
			studentBasicDetails = studentDetailsRepository.getStudentDetails(generalRegisterDetails.getStudentId());
		} catch (StudentException ex) {
			log.error("Error occured while fetching student basic details for {}", genRegNo);
			throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND, ex);
		}
		StudentTransportDetails studentTransportDetails = null;
		if(studentBasicDetails.isTransportOpted()) {
			try {
				studentTransportDetails = studentTransportDetailsRepository.getStudentTransportDetails(studentBasicDetails.getStudentId());
			} catch (Exception ex) {
				log.error("Error occured while fetching student transport details for {}", genRegNo);
				throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND, ex);
			}
		}
		return studentServiceAdapter.getStudentDetailsForRegNoResponseDto(generalRegisterDetails, studentBasicDetails, studentTransportDetails);
	}

	@Override
	@Transactional(rollbackFor = {StudentException.class, Exception.class})
	public List<StudentImportResponseDto> importStudentDetailsFromFile(MultipartFile file) throws StudentException {
		@Valid List<StudentRegistrationDto> studentRegistrationDtoList = null;
		try {
			log.info("Reading content of file {}", file.getOriginalFilename());
			studentRegistrationDtoList = fileUtils.readStudentImportFile(file.getInputStream());
		} catch (FileStorageException ex) {
			log.error("Error while reading content of uploaded file", ex.getMessage());
			throw new StudentException(ErrorDetails.FILE_UPLOAD_ERROR, ex);
		} catch (IOException ex) {
			log.error("Error while geting input stream of uploaded file", ex.getMessage());
			throw new StudentException(ErrorDetails.FILE_READ_ERROR, ex);
		}
		if (studentRegistrationDtoList == null || studentRegistrationDtoList.isEmpty()) {
			log.info("No data present in excel");
			throw new StudentException(ErrorDetails.BLANK_FILE_ERROR);
		}
		List<String> errorMessages = studentValidator.validateStudentImportData(studentRegistrationDtoList);
		if(errorMessages!=null && !errorMessages.isEmpty()) {
			log.info("Student import validation failed {}", errorMessages.toString());
			throw new StudentException(ErrorDetails.BAD_REQUEST, errorMessages);
		}
		return registerStudentDetails(studentRegistrationDtoList);
	}

	/**
	 * 
	 * @param studentRegistrationDtoList
	 * @return
	 * @throws StudentException
	 */
	public List<StudentImportResponseDto> registerStudentDetails(@Valid List<StudentRegistrationDto> studentRegistrationDtoList) throws StudentException {
		log.info("new student registration for {} students", studentRegistrationDtoList.size());
		String nextStudentId = Integer.toString(studentDetailsRepository.getMaxStudentId() + 1);
		Map<String, String> feeName2IdMap = feeTypesRepository.getFeeTypes().stream()
				.collect(Collectors.toMap(FeeTypes::getFeeName, FeeTypes::getFeeId));
		StudentImportData studentImportData = studentServiceAdapter.getStudentRegistrationDetails(nextStudentId, studentRegistrationDtoList, feeName2IdMap);
		studentDetailsRepository.addStudentBasicDeails(studentImportData.getStudentBasicDetailsList());
		generalRegisterRepository.addNewStudentInGeneralRegister(studentImportData.getGeneralRegisterList());
		studentClassDetailsRepository.addStudentClassDetails(studentImportData.getStudentClassDetailsList());
		if(!studentImportData.getStudentTransportDetailsList().isEmpty()) {
			studentTransportDetailsRepository.addSudentTransportDetails(studentImportData.getStudentTransportDetailsList());
		}
		studentFeesDetailsRepository.addNewStudentFeesDetails(studentImportData.getStudentFeesDetailsList());
		return studentServiceAdapter.getStudentRegistrationResponse(studentImportData);
	}

	@Override
	public FeeReceivablesResponseDto getFeeReceivables(int page, int size, String quickSearch) throws StudentException {
		log.info("Getting fee receivables");
		Pageable pageRequest = PageRequest.of(page, size);
		Page<FeeReceivableDetails> feeReceivableDetailsPagedData = studentDetailsRepository
				.getFeeReceivables(quickSearch, pageRequest);
		Map<String, StudentFeePaidDetails> studentId2FeePaidMap = studentDetailsRepository.getStudentsFeesPaidAmount()
				.stream().collect(Collectors.toMap(StudentFeePaidDetails::getStudentId, Function.identity()));
		List<FeeReceivableDetailsDto> feeReceivableDetailsDtoList = studentServiceAdapter
				.getFeeReceivableDetailsDto(feeReceivableDetailsPagedData, studentId2FeePaidMap);
		return FeeReceivablesResponseDto.builder().currentPage(feeReceivableDetailsPagedData.getNumber())
				.feeReceivableDetails(feeReceivableDetailsDtoList)
				.totalItems(feeReceivableDetailsPagedData.getTotalElements())
				.totalPages(feeReceivableDetailsPagedData.getTotalPages()).build();
	}

	@Override
	public FeeReceivablesStatsDto getFeeReceivablesStatistics() throws StudentException {
		log.info("Getting fee receivables statistics");
		FeesTotalReceivableAmnt totalReceivableAmnt = studentDetailsRepository.getTotalReceivableAmount();
		FeesPaidAmnt totalPaidAmnt = studentDetailsRepository.getTotalPaidAmount();
		return studentServiceAdapter.getFeeReceivableStatsDto(totalReceivableAmnt, totalPaidAmnt);
	}

	@Override
	public List<StudentFeesAssignedDetailsDto> getStudentsFeesAssignedDetails(String studentId)
			throws StudentException {
		log.info("Getting student fees assigned details for student id {}", studentId);
		log.info("validating student id {}", studentId);
		Optional.ofNullable(studentDetailsRepository.getStudentDetails(studentId))
				.orElseThrow(() -> new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND));
		List<StudentFeesAssignedDetails> studentFeeAssignedDetailsList = studentDetailsRepository
				.getStudentFeesAssignedDetails(studentId);
		return studentServiceAdapter.getStudentFeesAssignedDetailsDtoList(studentFeeAssignedDetailsList);
	}

	@Override
	public List<StudentFeesPaidDetailsDto> getStudentsFeesPaidDetails(String studentId) throws StudentException {
		log.info("Getting Fees paid details for student id {}", studentId);
		log.info("validating student id {}", studentId);
		Optional.ofNullable(studentDetailsRepository.getStudentDetails(studentId))
				.orElseThrow(() -> new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND));
		List<StudentFeesPaidDetails> studentFeesPaidDetailsList = studentDetailsRepository
				.getStudentFeesPaidDetails(studentId);
		return studentServiceAdapter.getStudentFeesPaidDetailsDtoList(studentFeesPaidDetailsList);
	}

	@Override
	public StudentFeesReceivableDetailsDto getStudentsFeesReceivableDetails(String studentId)
			throws StudentException {
		log.info("Getting studnets fees receivable details for student id {}", studentId);
		log.info("validating student id {}", studentId);
		Optional.ofNullable(studentDetailsRepository.getStudentDetails(studentId))
				.orElseThrow(() -> new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND));
		List<StudentFeesAssignedDetailsDto> studentFeesAssignedDetailsDtos = studentServiceAdapter
				.getStudentFeesAssignedDetailsDtoList(
						studentDetailsRepository.getStudentFeesAssignedDetails(studentId));
		List<StudentFeesPaidDetailsDto> studentFeesPaidDetailsDtos = studentServiceAdapter
				.getStudentFeesPaidDetailsDtoList(studentDetailsRepository.getStudentFeesPaidDetails(studentId));
		List<StudentFeesPaidDetailsWrapperDto> feesPaidWrapperDto = studentServiceAdapter.getStudentFeesPaidDetailsWrapperDto(studentFeesPaidDetailsDtos);
		List<StudentFeesDueDetailsDto> studentFeesDueDetailsDtos = studentServiceAdapter
				.getStudentFeesDueDetailsDtoList(studentFeesAssignedDetailsDtos, studentFeesPaidDetailsDtos);
		return studentServiceAdapter.getStudentFeesReceivableDetailsDto(studentFeesAssignedDetailsDtos,
				studentFeesPaidDetailsDtos, studentFeesDueDetailsDtos, feesPaidWrapperDto);
	}

	@Override
	@Transactional(rollbackFor = {StudentException.class, Exception.class})
	public StudentFeesPaidTrxnResponseDto addStudentFeesPaidDetails(String studentId,
			StudentFeesPaidTrxnRequestDto feesPaidTrxnRequest) throws StudentException {
		log.info("Saving student fees paid trxn details for student id {}", studentId);

		StudentFeesReceivableDetailsDto studentFeesReceivableDetailsDto = getStudentsFeesReceivableDetails(studentId);
		log.info("validating fees paid trxn request");
		studentValidator.validateStudentFeesPaidDetailsRequest(studentId, feesPaidTrxnRequest,
				studentFeesReceivableDetailsDto.getFeesDueDetails());

		log.info("adding student fees collection transaction");
		StudentFeeCollectionTransaction studentFeeCollectionTransaction = studentServiceAdapter
				.getStudentFeeCollectionTransaction(studentId, feesPaidTrxnRequest);
		studentFeeCollectionRepository.addStudentFeeCollection(studentFeeCollectionTransaction);

		log.info("getting max trxn det id");
		int nextTrxnDetId = studentFeeCollectionRepository.getMaxTrxnDetId() + 1;
		List<StudentFeeCollectionTransactionDetails> studentFeeCollectionTransactionDetails = studentServiceAdapter
				.getStudentFeeCollectionTransactionDetailsList(studentFeeCollectionTransaction.getCollectionId(),
						feesPaidTrxnRequest.getFeesPaidTrxnDetailsDtos(), nextTrxnDetId);
		studentFeeCollectionRepository.addStudentFeeCollectionDetails(studentFeeCollectionTransactionDetails);

		return studentServiceAdapter.getStudentFeesPaidTrxnResponseDto(studentFeeCollectionTransaction,
				studentFeeCollectionTransactionDetails);
	}
}
