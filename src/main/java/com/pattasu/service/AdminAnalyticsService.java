package com.pattasu.service;

import com.pattasu.dto.AnalyticsSummaryResponse;
import com.pattasu.dto.SalesDataPoint;

import java.util.List;

public interface AdminAnalyticsService {
    AnalyticsSummaryResponse getSummary();
    List<SalesDataPoint> getSalesData();
}
