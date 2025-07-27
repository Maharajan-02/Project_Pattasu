package com.pattasu.controller;

import com.pattasu.dto.AnalyticsSummaryResponse;
import com.pattasu.dto.SalesDataPoint;
import com.pattasu.service.AdminAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/analytics")
public class AdminAnalyticsController {

    @Autowired
    private AdminAnalyticsService analyticsService;

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummaryResponse> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/sales")
    public ResponseEntity<List<SalesDataPoint>> getSalesData() {
        return ResponseEntity.ok(analyticsService.getSalesData());
    }
}
