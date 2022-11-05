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
public class StudentListFilterDto {

	private List<String> classIds;
	private Boolean transportOpted;
	private String gender;
	private List<String> castes;
	private List<String> religions;
	private List<String> routeIds;
	private String quickSearchText;
	private String maritalStatus;
}
