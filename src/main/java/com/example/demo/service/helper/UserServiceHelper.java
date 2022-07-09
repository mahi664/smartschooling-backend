package com.example.demo.service.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.bo.ClassDetaislBO;
import com.example.demo.bo.SubjectDetailsBO;
import com.example.demo.bo.UserAcademicDetailsBO;
import com.example.demo.bo.UserAcademicDetailsDto;

@Service
public class UserServiceHelper {

	public List<UserAcademicDetailsDto> populateUserAcademicDetailsDto(Map<String, UserAcademicDetailsBO> usreAcademicDetails) {
		List<UserAcademicDetailsDto> userAcademicDetList = new ArrayList<>();
		for(String key : usreAcademicDetails.keySet()) {
			UserAcademicDetailsBO userAcademicDetailsBO = usreAcademicDetails.get(key);
			for(ClassDetaislBO classDetBo : userAcademicDetailsBO.getUserClassSubjectsL()) {
				for(SubjectDetailsBO subjectDetBO : classDetBo.getSubjects()) {
					UserAcademicDetailsDto userAcademicDetailsDto = new UserAcademicDetailsDto();
					userAcademicDetailsDto.setAcademicId(key);
					userAcademicDetailsDto.setClassId(classDetBo.getClassId());
					userAcademicDetailsDto.setSubjectId(subjectDetBO.getSubjectId());
					userAcademicDetList.add(userAcademicDetailsDto);
				}
			}
		}
		return userAcademicDetList;
	}
	
}
