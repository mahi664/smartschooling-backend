package com.example.demo.service.dto;

import java.util.List;

import com.example.demo.constant.SortFields;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentListRequestDto {

	private StudentListFilterDto filterDto;
	private List<SortFields> sortOrders;
}
