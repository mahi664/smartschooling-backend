package com.example.demo.service.dto;

import java.util.List;

import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.data.entity.StudentBasicDetails;
import com.example.demo.data.entity.StudentClassDetails;
import com.example.demo.data.entity.StudentFeesDetails;
import com.example.demo.data.entity.StudentTransportDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentImportData {

	private List<StudentBasicDetails> studentBasicDetailsList;
	private List<StudentTransportDetails> studentTransportDetailsList;
	private List<StudentClassDetails> studentClassDetailsList;
	private List<StudentFeesDetails> studentFeesDetailsList;
	private List<GeneralRegister> generalRegisterList;
}
