package com.example.demo.utils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.SuccessDetails;
import com.example.demo.service.dto.ErrorResponseDto;
import com.example.demo.service.dto.ResponseDto;
import com.example.demo.service.dto.SuccessResponseListDto;
import com.example.demo.service.dto.SuccessResponseObjectDto;

@Component
public class ResponseUtil<I> {

	/**
	 * 
	 * @param data
	 * @param success
	 * @return
	 */
	public ResponseEntity<ResponseDto> populateSuccessResponseWithMessage(I data, SuccessDetails success) {
		ResponseDto responseDto = new ResponseDto();
		SuccessResponseObjectDto<I> successResponseObjectDto = new SuccessResponseObjectDto<>();
		successResponseObjectDto.setSuccessCode(getCustomSuccessCode(success.getSuccessCode()));
		successResponseObjectDto.setSuccessMessage(success.getSucessMessage());
		successResponseObjectDto.setData(data);
		responseDto.setSuccess(successResponseObjectDto);
		return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param studentDetailsList
	 * @param totalItems
	 * @param totalPages
	 * @param currentPage
	 * @param studentDetailsFetchedSuccessfully
	 * @return
	 */
	public ResponseEntity<ResponseDto> populateSuccessResponseWithMessageAndPagination(I studentDetailsList,
			long totalItems, int totalPages, int currentPage, SuccessDetails success) {
		ResponseDto responseDto = new ResponseDto();
		SuccessResponseListDto<I> successResponseListDto = new SuccessResponseListDto<>();
		successResponseListDto.setSuccessCode(getCustomSuccessCode(success.getSuccessCode()));
		successResponseListDto.setSuccessMessage(success.getSucessMessage());
		successResponseListDto.setTotalItems(totalItems);
		successResponseListDto.setTotalPages(totalPages);
		successResponseListDto.setCurrentPage(currentPage);
		successResponseListDto.setData(studentDetailsList);
		responseDto.setSuccess(successResponseListDto);
		return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param successCode
	 * @return
	 */
	private String getCustomSuccessCode(int successCode) {
		return "SCC-"+ String.format("%04d", successCode);
	}

	/**
	 * 
	 * @param error
	 * @param errorMessages
	 * @return
	 */
	public static ResponseEntity<ResponseDto> populateErrorResponse(ErrorDetails error, List<String> errorMessages) {
		ResponseDto responseDto = new ResponseDto();
		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setErrorCode(getCustomErrorCode(error.getErrorCode()));
		errorResponseDto.setErrorMessage(error.getErrorDescription());
		errorResponseDto.setErrorMessages(errorMessages);
		responseDto.setError(errorResponseDto);
		return new ResponseEntity<ResponseDto>(responseDto, error.getHttpStatus());
	}
	
	/**
	 * 
	 * @param error
	 * @param errorMessages
	 * @return
	 */
	public static ResponseEntity<Object> populateErrorResponseAsObject(ErrorDetails error,
			List<String> errorMessages) {
		ResponseDto responseDto = new ResponseDto();
		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setErrorCode(getCustomErrorCode(error.getErrorCode()));
		errorResponseDto.setErrorMessage(error.getErrorDescription());
		errorResponseDto.setErrorMessages(errorMessages);
		responseDto.setError(errorResponseDto);
		return new ResponseEntity<Object>(responseDto, error.getHttpStatus());
	}
	
	public static ResponseEntity<Object> populateErrorResponseAsObject(ErrorDetails error) {
		ResponseDto responseDto = new ResponseDto();
		ErrorResponseDto errorResponseDto = new ErrorResponseDto();
		errorResponseDto.setErrorCode(getCustomErrorCode(error.getErrorCode()));
		errorResponseDto.setErrorMessage(error.getErrorDescription());
		responseDto.setError(errorResponseDto);
		return new ResponseEntity<Object>(responseDto, error.getHttpStatus());
	}

	/**
	 * 
	 * @param errorCode
	 * @return
	 */
	private static String getCustomErrorCode(int errorCode) {
		return "ERR-"+ String.format("%04d", errorCode);
	}
}
