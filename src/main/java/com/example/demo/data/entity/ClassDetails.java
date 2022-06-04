package com.example.demo.data.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDetails {

	private String classId;
	private String className;
	private Date lastUpdateTime;
	private String lastUser;
}
