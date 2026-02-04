package com.pattasu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ContactInfo {

	@Id
	private String shopName;
	
	private String address;
	
	private String phoneNumber;
	
	private String mailId;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopeName) {
		this.shopName = shopeName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	
	public ContactInfo() {}
	
	public ContactInfo (ContactInfo contactDto){
		this.shopName = contactDto.getShopName();
		this.address = contactDto.getAddress();
		this.mailId = contactDto.getMailId();
		this.phoneNumber = contactDto.getPhoneNumber();
	}
	
	
}
