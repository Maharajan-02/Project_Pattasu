package com.pattasu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pattasu.dto.StatsDTO;
import com.pattasu.service.StatsService;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

	private final StatsService statsService;
	
	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}
	
 	@PreAuthorize("hasAuthority('admin')")
    @GetMapping()
    public ResponseEntity<StatsDTO> quickStats(){
    	
    	return statsService.getQuickStats();
    }
	 
}
