package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.FeesDetailsBO;
import com.example.demo.services.FeesService;

@RestController
@CrossOrigin(origins="*")
public class FeesController {
	
	@Autowired
	private FeesService feesService;
	
	@PostMapping(path="/Fees/Types")
	public List<FeesDetailsBO> addFeeTypes(@RequestBody List<FeesDetailsBO> feesDetailsBOs){
		return feesService.addFeeTypes(feesDetailsBOs);
	}
	
	@GetMapping(path="/Fees/Types")
	public List<FeesDetailsBO> getFeeTypes(){
		return feesService.getFeeTypes();
	}
	
	@PostMapping(path="/Fees/Details")
	public List<FeesDetailsBO> addFeeDetails(@RequestBody List<FeesDetailsBO> feesDetailsBOs){
		return feesService.addFeeDetails(feesDetailsBOs);
	}
	
	@GetMapping(path="/Fees/Details/{feeId}")
	public List<FeesDetailsBO> getFeeDetails(@PathVariable String feeId){
		return feesService.getFeeDetails(feeId);
	}
}
