package com.pattasu.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.pattasu.dto.StatsDTO;

@Component
public interface StatsService {
	
	ResponseEntity<StatsDTO> getQuickStats();

}
