package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bo.RouteDetailsBO;
import com.example.demo.services.RoutesService;

@RestController
@CrossOrigin(origins="*")
public class RoutesController {
	
	@Autowired
	private RoutesService routesService;
	
	@PostMapping(path="/routes")
	public List<RouteDetailsBO> addNewRoutes(@RequestBody List<RouteDetailsBO> routeDetailsBOs){
		return routesService.addNewRoutes(routeDetailsBOs);
	}
	
	@GetMapping(path="/routes")
	public List<RouteDetailsBO> getRoutes(){
		return routesService.getRoutes();
	}
}
