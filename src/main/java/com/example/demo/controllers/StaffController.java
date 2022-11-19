package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.SortFields;
import com.example.demo.constant.SuccessDetails;
import com.example.demo.exception.StaffException;
import com.example.demo.exception.StudentException;
import com.example.demo.service.StaffService;
import com.example.demo.service.dto.FilterListRequestDto;
import com.example.demo.service.dto.ResponseDto;
import com.example.demo.service.dto.StaffBasicDetailsDto;
import com.example.demo.service.dto.StaffDetailsListResponseDto;
import com.example.demo.service.dto.StffBasicDetailsResponseDto;
import com.example.demo.service.dto.StudentListFilterDto;
import com.example.demo.utils.ResponseUtil;

@RestController
@RequestMapping(value = "/staff")
@CrossOrigin(origins = "*")
public class StaffController {

	private final Logger log = LoggerFactory.getLogger(StaffController.class);
	
	@Autowired
	ResponseUtil<Object> responseUtil;
	
	@Autowired
	StaffService staffService;
	
	/**
	 * @param staffBasicDetailsDto
	 * @return
	 * @throws StudentException 
	 * @throws StaffException 
	 */
	@PostMapping
	public ResponseEntity<ResponseDto> addStaffBasicDetails(
			@Valid @RequestBody StaffBasicDetailsDto staffBasicDetailsDto) throws StaffException, StudentException {
		log.info("Adding basic details for staff name {}", staffBasicDetailsDto.getFirstName());
		StffBasicDetailsResponseDto userBasicDetailsResponseDto = staffService
				.addStaffBasicDetails(staffBasicDetailsDto);
		return responseUtil.populateSuccessResponseWithMessage(userBasicDetailsResponseDto,
				SuccessDetails.STAFF_BASIC_DETAILS_ADDED_SUCCESSFULLY);
	}
	
	@GetMapping
	public ResponseEntity<ResponseDto> getStaffDetailsList(@RequestHeader String academicYear,
			@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(value = "caste", required = false) List<String> castes,
			@RequestParam(value = "religion", required = false) List<String> religions,
			@RequestParam(required = false) String gender, @RequestParam(required = false) String maritalStatus,
			@RequestParam(value = "sortBy", required = false) List<SortFields> sortOrder,
			@RequestParam(value = "quickSearch", required = false) String quickSearchText)
			throws StaffException {
		log.info("Fetching staff list for academic year {}, page {} and size {}", academicYear, page, size);
		StaffDetailsListResponseDto staffDetailsListResponseDto = staffService.getStaffDetailsList(academicYear, page,
				size,
				FilterListRequestDto.builder()
						.filterDto(StudentListFilterDto.builder().castes(castes).gender(gender).religions(religions)
								.maritalStatus(maritalStatus).quickSearchText(quickSearchText).build())
						.sortOrders(sortOrder).build());
		return responseUtil.populateSuccessResponseWithMessageAndPagination(
				staffDetailsListResponseDto.getStaffDetailsDtos(), staffDetailsListResponseDto.getTotalItems(),
				staffDetailsListResponseDto.getTotalPages(), staffDetailsListResponseDto.getCurrentPage(),
				SuccessDetails.STUDENT_DETAILS_FETCHED_SUCCESSFULLY);
	}
	
//	@GetMapping
//	public List<UserBasicDetailsBO> getUsers(){
//		return usersService.getUsers();
//	}
//	
//	@GetMapping(path = "/{userId}")
//	public UserAdvanceDetailsBO getUsersAdvanceDetails(@PathVariable String userId) {
//		return usersService.getUsersAdvanceDetails(userId);
//	}
//	
//	@PostMapping(path = "/{userId}/academic-details")
//	public Map<String, UserAcademicDetailsBO> addUserAcademicDetails(@PathVariable String userId, @RequestBody Map<String, UserAcademicDetailsBO> usreAcademicDetails){
//		return usersService.addUserAcademicDetails(userId, usreAcademicDetails);
//	}
//	
//	@PostMapping(path = "/{userId}/role-details")
//	public List<RoleDetailsBO> addUserRoleDetails(@PathVariable String userId, @RequestBody List<RoleDetailsBO> userRoles){
//		return usersService.addUserRoleDetails(userId, userRoles);
//	}
}
