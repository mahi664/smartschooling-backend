package com.example.demo.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchStudentsResponseDto {

	private List<StudentDetailsDto> studentDetailsList;
	private long totalItems;
	private int totalPages;
	private int currentPage;
}
