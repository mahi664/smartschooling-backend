package com.example.demo.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDetailsListResponseDto {

	private List<StaffBasicDetailsDto> staffDetailsDtos;
	private long totalItems;
	private int totalPages;
	private int currentPage;
}
