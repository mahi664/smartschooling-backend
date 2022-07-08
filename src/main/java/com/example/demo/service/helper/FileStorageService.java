package com.example.demo.service.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.exception.FileStorageException;

@Service
public class FileStorageService {

	private final Logger log = LoggerFactory.getLogger(FileStorageService.class);
	
	private final Path fileStorageLocation;
	
	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) throws FileStorageException {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception ex) {
			log.error("Error while creating directory for file upload", ex.getMessage());
			throw new FileStorageException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}
	
	public String storeFile(MultipartFile file) throws FileStorageException {
		log.info("Storing file {} to directory {}", file.getOriginalFilename(), fileStorageLocation);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception ex) {
			log.error("Error Occured while uploading file", ex.getMessage());
			throw new FileStorageException(ErrorDetails.FILE_UPLOAD_ERROR, ex);
		}
		return fileName;
	}
}
