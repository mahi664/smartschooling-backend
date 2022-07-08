package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.example.demo.service.helper.FileStorageProperties;

//import com.example.demo.storage.StorageProperties;
//import com.example.demo.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class DemoApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return(application.sources(DemoApplication.class));
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
