package com.example.demo.service.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.constant.FeeTypes;
import com.example.demo.data.entity.AcademicDetails;
import com.example.demo.data.entity.ClassDetails;
import com.example.demo.data.entity.FeeReceivableDetails;
import com.example.demo.data.entity.FeesPaidAmnt;
import com.example.demo.data.entity.FeesTotalReceivableAmnt;
import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.data.entity.StudentBasicDetails;
import com.example.demo.data.entity.StudentClassDetails;
import com.example.demo.data.entity.StudentFeePaidDetails;
import com.example.demo.data.entity.StudentFeesDetails;
import com.example.demo.data.entity.StudentTransportDetails;
import com.example.demo.service.dto.ClassDto;
import com.example.demo.service.dto.FeeReceivableDetailsDto;
import com.example.demo.service.dto.FeeReceivablesStatsDto;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.StudentDetailsDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
import com.example.demo.service.dto.StudentImportData;
import com.example.demo.service.dto.StudentImportResponseDto;
import com.example.demo.service.dto.StudentRegistrationDto;
import com.example.demo.service.dto.StudentRegistrationResponseDto;
import com.example.demo.utils.Constants;

@Service
public class StudentServiceAdapter {

	private static final Logger log = LoggerFactory.getLogger(StudentServiceAdapter.class);
	
	/**
	 * 
	 * @param nextStudentId 
	 * @param studentRegistrationDto
	 * @return
	 */
	public StudentBasicDetails getStudentBasicDetails(String studentId, StudentRegistrationDto studentRegistrationDto) {
		log.info("Setting student basic details entity for student name {} and id {}",
				studentRegistrationDto.getFirstName(), studentId);
		return StudentBasicDetails.builder().studentId(studentId)
				.address(studentRegistrationDto.getAddress()).adharNumber(studentRegistrationDto.getAdharNumber())
				.alternateMobile(studentRegistrationDto.getAlternateMobileNumber())
				.birthDate(studentRegistrationDto.getBirthDate()).caste(studentRegistrationDto.getCaste())
				.email(studentRegistrationDto.getEmail()).firstName(studentRegistrationDto.getFirstName())
				.gender(studentRegistrationDto.getGender()).lastName(studentRegistrationDto.getLastName())
				.middleName(studentRegistrationDto.getMiddleName())
				.mobileNumber(studentRegistrationDto.getMobileNumber())
				.nationality(studentRegistrationDto.getNationality()).religion(studentRegistrationDto.getReligion())
				.transportOpted(studentRegistrationDto.isTransportOpted()).build();
	}

	/**
	 * 
	 * @param studentId 
	 * @param studentRegistrationDto
	 * @return
	 */
	public GeneralRegister getGeneralRegister(String studentId, StudentRegistrationDto studentRegistrationDto) {
		log.info("Setting general register entity for student name {} and id {}", studentRegistrationDto.getFirstName(),
				studentId);
		return GeneralRegister.builder().admissionDate(studentRegistrationDto.getAdmissionDate())
				.admissionStd(studentRegistrationDto.getAdmissionStd()).bookNo(studentRegistrationDto.getBookNo())
				.regNo(studentRegistrationDto.getGenRegNo()).studentId(studentId)
				.previousSchool(studentRegistrationDto.getPrevSchool())
				.academicYear(studentRegistrationDto.getAcademicYear()).build();
	}

	/**
	 * 
	 * @param nextStudentId
	 * @param studentRegistrationDto
	 * @return
	 */
	public StudentRegistrationResponseDto getStudentRegistrationResponse(String studentId,
			StudentRegistrationDto studentRegistrationDto) {
		log.info("populating student registration resposne for student name {} and id {}",
				studentRegistrationDto.getFirstName(), studentId);
		return StudentRegistrationResponseDto.builder().genRegNo(studentRegistrationDto.getGenRegNo())
				.studentId(studentId).studentName(studentRegistrationDto.getFirstName()).build();
	}

	/**
	 * 
	 * @param studentId
	 * @param studentRegistrationDto
	 * @return
	 */
	public StudentClassDetails getStudentClassDetails(String studentId,
			StudentRegistrationDto studentRegistrationDto) {
		log.info("populating student registration resposne for student name {} and id {}",
				studentRegistrationDto.getFirstName(), studentId);
		AcademicDetails academicDetails = AcademicDetails.builder().academicId(studentRegistrationDto.getAcademicYear())
				.build();
		ClassDetails classDetails = ClassDetails.builder().classId(studentRegistrationDto.getAdmissionStd()).build();
		return StudentClassDetails.builder().academicDetails(academicDetails).classDetails(classDetails)
				.studentId(studentId).build();
	}

	/**
	 * 
	 * @param nextStudentId
	 * @param studentRegistrationDto
	 * @return
	 */
	public StudentTransportDetails getStudentTransportDetails(String studentId,
			StudentRegistrationDto studentRegistrationDto) {
		log.info("populating student registration resposne for student name {} and id {}",
				studentRegistrationDto.getFirstName(), studentId);
		return StudentTransportDetails.builder().routeId(studentRegistrationDto.getRoute()).studentId(studentId).build();
	}

	/**
	 * 
	 * @param pagedStudentData
	 * @return
	 */
	public FetchStudentsResponseDto getFetchStudentResponseDto(Page<StudentBasicDetails> pagedStudentData) {
		log.info("populating fetch student resposne dto");
		List<StudentDetailsDto> studentDetailsDtos = pagedStudentData.getContent().stream()
				.map(mapper -> getStudentDetailsDto(mapper)).collect(Collectors.toList());
		return FetchStudentsResponseDto.builder().currentPage(pagedStudentData.getNumber())
				.totalItems(pagedStudentData.getTotalElements()).totalPages(pagedStudentData.getTotalPages())
				.studentDetailsList(studentDetailsDtos).build();
	}

	/**
	 * 
	 * @param studentBasicDetails
	 * @return
	 */
	public StudentDetailsDto getStudentDetailsDto(StudentBasicDetails studentBasicDetails) {
		log.info("Populating studet details dto for {}", studentBasicDetails.toString());
		return StudentDetailsDto.builder().address(studentBasicDetails.getAddress())
				.adharNumber(studentBasicDetails.getAdharNumber())
				.alternateMobile(studentBasicDetails.getAlternateMobile()).birthDate(studentBasicDetails.getBirthDate())
				.caste(studentBasicDetails.getCaste())
				.classDet(ClassDto.builder()
						.id(studentBasicDetails.getStudentClassDetails().stream().findFirst().get().getClassDetails()
								.getClassId())
						.name(studentBasicDetails.getStudentClassDetails().stream().findFirst().get().getClassDetails()
								.getClassName())
						.build())
				.email(studentBasicDetails.getEmail()).firstName(studentBasicDetails.getFirstName())
				.gender(studentBasicDetails.getGender())
				.genRegNo(studentBasicDetails.getGeneralRegisterDetails().stream().findFirst().get().getRegNo())
				.lastName(studentBasicDetails.getLastName()).middleName(studentBasicDetails.getMiddleName())
				.mobileNumber(studentBasicDetails.getMobileNumber()).nationality(studentBasicDetails.getNationality())
				.religion(studentBasicDetails.getReligion()).transportOpted(studentBasicDetails.isTransportOpted())
				.studentId(studentBasicDetails.getStudentId())
				.build();
	}

	/**
	 * 
	 * @param studentId 
	 * @param studentRegistrationDto
	 * @param feeName2IdMap
	 * @return
	 */
	public List<StudentFeesDetails> getStudentFeesDetails(String studentId, StudentRegistrationDto studentRegistrationDto,
			Map<String, String> feeName2IdMap) {
		log.info("getting student fees details {}", studentId);
		List<StudentFeesDetails> studentFeesDetails = new ArrayList<>();
		studentFeesDetails.add(StudentFeesDetails.builder().academicId(studentRegistrationDto.getAcademicYear())
				.feeId(feeName2IdMap.get(FeeTypes.TUTION_FEES.getFeeType())).studentId(studentId).build());
		if (studentRegistrationDto.isTransportOpted()) {
			studentFeesDetails.add(StudentFeesDetails.builder().academicId(studentRegistrationDto.getAcademicYear())
					.feeId(feeName2IdMap.get(FeeTypes.TRANSPORT_FEES.getFeeType())).studentId(studentId).build());
		}
		return studentFeesDetails;
	}

	/**
	 * 
	 * @param generalRegisterDetails
	 * @param studentBasicDetails
	 * @param studentTransportDetails
	 * @return
	 */
	public StudentDetailsForRegNoResponseDto getStudentDetailsForRegNoResponseDto(
			GeneralRegister generalRegisterDetails, StudentBasicDetails studentBasicDetails,
			StudentTransportDetails studentTransportDetails) {
		log.info("Populating student details for reg no response dto for {}", generalRegisterDetails.toString());
		return StudentDetailsForRegNoResponseDto.builder().address(studentBasicDetails.getAddress())
				.adharNumber(studentBasicDetails.getAdharNumber())
				.admissionDate(generalRegisterDetails.getAdmissionDate())
				.admissionStd(generalRegisterDetails.getAdmissionStd())
				.alternateMobile(studentBasicDetails.getAlternateMobile()).birthDate(studentBasicDetails.getBirthDate())
				.bookNo(generalRegisterDetails.getBookNo()).caste(studentBasicDetails.getCaste())
				.email(studentBasicDetails.getEmail()).firstName(studentBasicDetails.getFirstName())
				.gender(studentBasicDetails.getGender()).genRegNo(generalRegisterDetails.getRegNo())
				.lastName(studentBasicDetails.getLastName()).middleName(studentBasicDetails.getMiddleName())
				.mobileNumber(studentBasicDetails.getMobileNumber()).nationality(studentBasicDetails.getNationality())
				.prevSchool(generalRegisterDetails.getPreviousSchool()).religion(studentBasicDetails.getReligion())
				.studentId(studentBasicDetails.getStudentId()).transportOpted(studentBasicDetails.isTransportOpted())
				.route(studentTransportDetails != null ? studentTransportDetails.getRouteId() : null)
				.academicYear(generalRegisterDetails.getAcademicYear()).build();
	}

	/**
	 * 
	 * @param nextStudentId
	 * @param studentRegistrationDtoList
	 * @param feeName2IdMap 
	 * @return
	 */
	public StudentImportData getStudentRegistrationDetails(String studentId,
			@Valid List<StudentRegistrationDto> studentRegistrationDtoList, Map<String, String> feeName2IdMap) {
		log.info("setting student registration entities for {} students", studentRegistrationDtoList.size());
		int studId = Integer.parseInt(studentId);
		List<StudentBasicDetails> studentBasicDetailsList = new ArrayList<>();
		List<StudentClassDetails> studentClassDetailsList = new ArrayList<>();
		List<StudentTransportDetails> studentTransportDetailsList = new ArrayList<>();
		List<StudentFeesDetails> studentFeesDetailsList = new ArrayList<>();
		List<GeneralRegister> generalRegisterList = new ArrayList<>();
		for (StudentRegistrationDto studentRegistrationDto : studentRegistrationDtoList) {
			studentBasicDetailsList.add(getStudentBasicDetails(Integer.toString(studId), studentRegistrationDto));
			studentClassDetailsList.add(getStudentClassDetails(Integer.toString(studId), studentRegistrationDto));
			if (studentRegistrationDto.isTransportOpted()) {
				studentTransportDetailsList
						.add(getStudentTransportDetails(Integer.toString(studId), studentRegistrationDto));
			}
			studentFeesDetailsList
					.addAll(getStudentFeesDetails(Integer.toString(studId), studentRegistrationDto, feeName2IdMap));
			generalRegisterList.add(getGeneralRegister(Integer.toString(studId), studentRegistrationDto));
			studId++;
		}
		return StudentImportData.builder().studentBasicDetailsList(studentBasicDetailsList)
				.studentClassDetailsList(studentClassDetailsList).studentFeesDetailsList(studentFeesDetailsList)
				.studentTransportDetailsList(studentTransportDetailsList).generalRegisterList(generalRegisterList)
				.build();
	}

	public List<StudentImportResponseDto> getStudentRegistrationResponse(StudentImportData studentImportData) {
		log.info("populating student import response");
		List<StudentImportResponseDto> studentDetailsDtoList = new ArrayList<>();
		Map<String, GeneralRegister> studId2GenRegDetailsMap = studentImportData.getGeneralRegisterList()
				.stream().collect(Collectors.toMap(GeneralRegister::getStudentId, Function.identity()));
		Map<String, StudentTransportDetails> studId2TransportDetailsMap = studentImportData.getStudentTransportDetailsList().stream().collect(Collectors.toMap(StudentTransportDetails::getStudentId, Function.identity()));
		for (StudentBasicDetails studentBasicDetails : studentImportData.getStudentBasicDetailsList()) {
			GeneralRegister generalRegister = studId2GenRegDetailsMap.get(studentBasicDetails.getStudentId());
			StudentTransportDetails studentTransportDetails = studId2TransportDetailsMap
					.get(studentBasicDetails.getStudentId());
			studentDetailsDtoList.add(StudentImportResponseDto.builder().address(studentBasicDetails.getAddress())
					.adharNumber(studentBasicDetails.getAdharNumber())
					.alternateMobile(studentBasicDetails.getAlternateMobile())
					.birthDate(studentBasicDetails.getBirthDate()).caste(studentBasicDetails.getCaste())
					.academicYear(generalRegister.getAcademicYear()).admissionDate(generalRegister.getAdmissionDate())
					.admissionStd(generalRegister.getAdmissionStd()).bookNo(generalRegister.getBookNo())
					.email(studentBasicDetails.getEmail()).firstName(studentBasicDetails.getFirstName())
					.gender(studentBasicDetails.getGender()).genRegNo(generalRegister.getRegNo())
					.lastName(studentBasicDetails.getLastName()).middleName(studentBasicDetails.getMiddleName())
					.mobileNumber(studentBasicDetails.getMobileNumber())
					.nationality(studentBasicDetails.getNationality()).prevSchool(generalRegister.getPreviousSchool())
					.religion(studentBasicDetails.getReligion()).route(studentBasicDetails.isTransportOpted() ? studentTransportDetails.getRouteId() : Constants.BLANK_STRING)
					.studentId(studentBasicDetails.getStudentId())
					.transportOpted(studentBasicDetails.isTransportOpted()).build());

		}
		return studentDetailsDtoList;
	}

	
	/**
	 * Get Receivable receivables stats dto
	 * 
	 * @param totalReceivableAmnt
	 * @param totalPaidAmnt
	 * @return
	 */
	public FeeReceivablesStatsDto getFeeReceivableStatsDto(FeesTotalReceivableAmnt totalReceivableAmnt,
			FeesPaidAmnt totalPaidAmnt) {
		log.info("Populating Fee receivable stats dto for total amnt {} and paid amount {}",
				totalReceivableAmnt.getTotalAmnt(), totalPaidAmnt.getTotalAmnt());
		return FeeReceivablesStatsDto.builder().totalAmnt(totalReceivableAmnt.getTotalAmnt())
				.paidAmnt(totalPaidAmnt.getTotalAmnt())
				.dueAmnt(totalReceivableAmnt.getTotalAmnt() - totalPaidAmnt.getTotalAmnt()).build();
	}

	/**
	 * @param feeReceivableDetailsPagedData
	 * @param studentId2FeePaidMap 
	 * @return
	 */
	public List<FeeReceivableDetailsDto> getFeeReceivableDetailsDto(
			Page<FeeReceivableDetails> feeReceivableDetailsPagedData,
			Map<String, StudentFeePaidDetails> studentId2FeePaidMap) {
		return feeReceivableDetailsPagedData.getContent().stream().map(feeReceivableDetail -> {
			double feesPaid = studentId2FeePaidMap != null
					&& studentId2FeePaidMap.get(feeReceivableDetail.getStudentId()) != null
							? studentId2FeePaidMap.get(feeReceivableDetail.getStudentId()).getFeesPaid()
							: 0;
			return FeeReceivableDetailsDto.builder().firstName(feeReceivableDetail.getFirstName())
					.genRegNo(feeReceivableDetail.getGenRegNo()).lastName(feeReceivableDetail.getLastName())
					.middleName(feeReceivableDetail.getMiddleName()).mobileNumber(feeReceivableDetail.getMobileNumber())
					.studentId(feeReceivableDetail.getStudentId()).totalAmnt(feeReceivableDetail.getTotalFee())
					.paidAmnt(feesPaid).dueAmnt(feeReceivableDetail.getTotalFee() - feesPaid)
					.address(feeReceivableDetail.getAddress())
					.build();
		}).collect(Collectors.toList());
	}
}
