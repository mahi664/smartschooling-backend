package com.example.demo.service.dto;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentFeesPaidTrxnRequestDto {

	@NotNull(message = "Transaction Date can not be empty or null")
	private Date trxnDate;
	@NotBlank(message = "Account id can not be empty")
	private String accountId;
	@Valid
	private List<StudentFeesPaidTrxnDetailsDto> feesPaidTrxnDetailsDtos;
}
