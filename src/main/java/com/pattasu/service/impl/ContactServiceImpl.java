package com.pattasu.service.impl;

import org.springframework.stereotype.Service;

import com.pattasu.entity.ContactInfo;
import com.pattasu.repository.ContactRepository;
import com.pattasu.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

	private final ContactRepository contactRepository;
	
	public ContactServiceImpl(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}
	
	@Override
	public void addContact(ContactInfo contactInfoDto) {

		ContactInfo contactInfo = new ContactInfo(contactInfoDto);
		contactRepository.save(contactInfo);
		
	}

	@Override
	public ContactInfo getContactInfo() {
		return contactRepository.findAll().get(0);
	}

}
