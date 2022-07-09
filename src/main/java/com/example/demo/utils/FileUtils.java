package com.example.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.StudentImportFields;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.StudentRegistrationDto;

@Service
public class FileUtils {

	private final Logger log = LoggerFactory.getLogger(FileUtils.class);
	
	private List<String> errorMessages;

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws FileStorageException
	 * @throws StudentException 
	 */
	public List<StudentRegistrationDto> readStudentImportFile(InputStream inputStream)
			throws FileStorageException, StudentException {
		log.info("Reading content from the excel file");
		Workbook workbook;
		List<StudentRegistrationDto> studentRegistrationDtos = null;
		errorMessages = new ArrayList<>();
		try {
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				if (studentRegistrationDtos == null) {
					studentRegistrationDtos = new ArrayList<>();
				}
				StudentRegistrationDto studentRegistrationDto = new StudentRegistrationDto();
				if (row.getCell(0) == null) {
					errorMessages.add(StudentImportFields.GEN_REG_NO.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setGenRegNo((int) ((double) readValueFromCell(row.getCell(0),
							StudentImportFields.GEN_REG_NO, row.getRowNum() + 1)));
				}
				if (row.getCell(1) == null) {
					errorMessages.add(StudentImportFields.BOOK_NO.getField() + " In row " + (row.getRowNum() + 1) + " "
							+ ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setBookNo((int) ((double) readValueFromCell(row.getCell(1),
							StudentImportFields.BOOK_NO, row.getRowNum() + 1)));
				}
				if (row.getCell(2) == null) {
					errorMessages.add(StudentImportFields.FIRST_NAME.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setFirstName((String) (readValueFromCell(row.getCell(2),
							StudentImportFields.FIRST_NAME, row.getRowNum() + 1)));
				}
				studentRegistrationDto.setMiddleName(row.getCell(3) == null ? Constants.BLANK_STRING
						: ((String) (readValueFromCell(row.getCell(3), StudentImportFields.MIDDLE_NAME,
								row.getRowNum() + 1))));
				if (row.getCell(4) == null) {
					errorMessages.add(StudentImportFields.LAST_NAME.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setLastName((String) (readValueFromCell(row.getCell(4),
							StudentImportFields.LAST_NAME, row.getRowNum() + 1)));
				}
				studentRegistrationDto.setAdharNumber(row.getCell(5) == null ? Constants.BLANK_STRING
						: ((String) (readValueFromCell(row.getCell(5), StudentImportFields.ADHAR_NO,
								row.getRowNum() + 1))));
				if (row.getCell(6) == null) {
					errorMessages.add(StudentImportFields.BIRTH_DATE.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setBirthDate((Date) (readValueFromCell(row.getCell(6),
							StudentImportFields.BIRTH_DATE, row.getRowNum() + 1)));
				}
				if (row.getCell(7) == null) {
					errorMessages.add(StudentImportFields.GENDER.getField() + " In row " + (row.getRowNum() + 1) + " "
							+ ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setGender((String) (readValueFromCell(row.getCell(7),
							StudentImportFields.GENDER, row.getRowNum() + 1)));
				}
				if (row.getCell(8) == null) {
					errorMessages.add(StudentImportFields.RELIGION.getField() + " In row " + (row.getRowNum() + 1) + " "
							+ ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setReligion((String) (readValueFromCell(row.getCell(8),
							StudentImportFields.RELIGION, row.getRowNum() + 1)));
				}
				if (row.getCell(9) == null) {
					errorMessages.add(StudentImportFields.CASTE.getField() + " In row " + (row.getRowNum() + 1) + " "
							+ ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setCaste((String) (readValueFromCell(row.getCell(9),
							StudentImportFields.CASTE, row.getRowNum() + 1)));
				}
				if (row.getCell(10) == null) {
					errorMessages.add(StudentImportFields.NATIONALITY.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setNationality((String) (readValueFromCell(row.getCell(10),
							StudentImportFields.NATIONALITY, row.getRowNum() + 1)));
				}
				if (row.getCell(11) == null) {
					errorMessages.add(StudentImportFields.MOBILE_NO.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setMobileNumber(((String) (readValueFromCell(row.getCell(11),
							StudentImportFields.MOBILE_NO, row.getRowNum() + 1))));
				}
				studentRegistrationDto.setAlternateMobileNumber(row.getCell(12) == null ? Constants.BLANK_STRING
						: ((String) (readValueFromCell(row.getCell(12), StudentImportFields.ALT_MOB_NO,
								row.getRowNum() + 1))));
				studentRegistrationDto.setEmail(row.getCell(13) == null ? Constants.BLANK_STRING
						: ((String) (readValueFromCell(row.getCell(13), StudentImportFields.EMAIL,
								row.getRowNum() + 1))));
				if (row.getCell(14) == null) {
					errorMessages.add(StudentImportFields.ADDRESS.getField() + " In row " + (row.getRowNum() + 1) + " "
							+ ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setAddress((String) (readValueFromCell(row.getCell(14),
							StudentImportFields.ADDRESS, row.getRowNum() + 1)));
				}
				if (row.getCell(15) == null) {
					errorMessages.add(StudentImportFields.ADMISSION_STD.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setAdmissionStd((String) (readValueFromCell(row.getCell(15),
							StudentImportFields.ADMISSION_STD, row.getRowNum() + 1)));
				}
				if (row.getCell(16) == null) {
					errorMessages.add(StudentImportFields.ADMISSION_DATE.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setAdmissionDate((Date) (readValueFromCell(row.getCell(16),
							StudentImportFields.ADMISSION_DATE, row.getRowNum() + 1)));
				}
				if (row.getCell(17) == null) {
					errorMessages.add(StudentImportFields.ACADEMIC_YEAR.getField() + " In row " + (row.getRowNum() + 1)
							+ " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setAcademicYear((String) (readValueFromCell(row.getCell(17),
							StudentImportFields.ACADEMIC_YEAR, row.getRowNum() + 1)));
				}
				studentRegistrationDto.setPrevSchool(row.getCell(18) == null ? Constants.BLANK_STRING
						: ((String) (readValueFromCell(row.getCell(18), StudentImportFields.PREV_SCHOOL,
								row.getRowNum() + 1))));
				if (row.getCell(19) == null) {
					errorMessages.add(StudentImportFields.TRANSPORT_OPTED.getField() + " In row "
							+ (row.getRowNum() + 1) + " " + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
				} else {
					studentRegistrationDto.setTransportOpted((boolean) (readValueFromCell(row.getCell(19),
							StudentImportFields.TRANSPORT_OPTED, row.getRowNum() + 1)));
				}
				studentRegistrationDto.setRoute(row.getCell(20) == null ? Constants.BLANK_STRING
						: ((String) (readValueFromCell(row.getCell(20), StudentImportFields.ROUTE,
								row.getRowNum() + 1))));
				log.info("Data read: {}", studentRegistrationDto.toString());
				studentRegistrationDtos.add(studentRegistrationDto);
			}
			workbook.close();
		} catch (IOException ex) {
			log.error("Error in reading file content", ex.getMessage());
			throw new FileStorageException(ErrorDetails.FILE_READ_ERROR, ex);
		} catch (Exception ex) {
			log.error("Error in reading file content", ex.getMessage());
			throw new FileStorageException(ErrorDetails.FILE_READ_ERROR, ex);
		}
		if (!errorMessages.isEmpty()) {
			log.info("Invalid Fields in excel");
			throw new StudentException(ErrorDetails.BAD_REQUEST, errorMessages);
		}
		return studentRegistrationDtos;
	}

	private Object readValueFromCell(Cell cell, StudentImportFields field, int rowNum) {
		Object object = "";
		if (cell == null) {
			errorMessages.add(field + "In row " + rowNum + ErrorDetails.FIELD_MUST_NOT_BLANK.getErrorDescription());
		} else if (StudentImportFields.BIRTH_DATE.equals(field) || StudentImportFields.ADMISSION_DATE.equals(field)) {
			try {
				object = cell.getDateCellValue();
			} catch (Exception ex) {
				log.error("Can not read value from excel for {} in row {}", field, rowNum, ex.getMessage());
				errorMessages
						.add(ErrorDetails.INVALID_VALUE.getErrorDescription() + " for " + field + " in row " + rowNum);
			}
		} else if(StudentImportFields.MOBILE_NO.equals(field) || StudentImportFields.ALT_MOB_NO.equals(field) || StudentImportFields.ADHAR_NO.equals(field)){
			try {
				cell.setCellType(CellType.STRING);
				object = cell.getStringCellValue();
			} catch (Exception ex) {
				log.error("Can not read value from excel for {} in row {}", field, rowNum, ex.getMessage());
				errorMessages
						.add(ErrorDetails.INVALID_VALUE.getErrorDescription() + " for " + field + " in row " + rowNum);
			}
		}else {
			try {
				switch (cell.getCellType()) {
				case NUMERIC:
					object = cell.getNumericCellValue();
					break;
				case STRING:
					object = cell.getStringCellValue();
					break;
				case BOOLEAN:
					object = cell.getBooleanCellValue();
					break;
				default:
					object = 0;
					break;
				}
			} catch (Exception ex) {
				log.error("Can not read value from excel for {} in row {}", field, rowNum, ex.getMessage());
				errorMessages
						.add(ErrorDetails.INVALID_VALUE.getErrorDescription() + " for " + field + " in row " + rowNum);
			}
		}
		return object;
	}
}
