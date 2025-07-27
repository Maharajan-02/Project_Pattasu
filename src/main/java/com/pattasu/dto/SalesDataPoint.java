package com.pattasu.dto;

public class SalesDataPoint {
    private String date;
    private double revenue;

    public SalesDataPoint(String date, double revenue) {
        this.date = date;
        this.revenue = revenue;
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

    
}
