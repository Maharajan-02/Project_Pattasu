package com.pattasu.service;

import org.springframework.stereotype.Service;

import com.pattasu.entity.ContactInfo;

@Service
public interface ContactService {
	
	void addContact(ContactInfo contactInfo);
	
	ContactInfo getContactInfo ();

}
