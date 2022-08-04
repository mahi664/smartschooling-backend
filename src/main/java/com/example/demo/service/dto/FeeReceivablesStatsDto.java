package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeReceivablesStatsDto {

	private double totalAmnt;
	private double paidAmnt;
	private double dueAmnt;
}
