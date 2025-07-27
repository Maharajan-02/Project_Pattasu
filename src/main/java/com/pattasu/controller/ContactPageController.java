package com.pattasu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pattasu.entity.ContactInfo;
import com.pattasu.service.ContactService;

@RestController
@RequestMapping("/api/contact")
public class ContactPageController {

	private final ContactService contactService;
	
	public ContactPageController(ContactService contactService) {
        this.contactService = contactService;
    }
	
	@PostMapping("/update")
	public ResponseEntity<String> addOrUpdateContact(@RequestBody ContactInfo contactInfo) {
		try {
			contactService.addContact(contactInfo);
			return ResponseEntity.ok("Contact Updated Successfully");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Contact update failed due to "+ e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<ContactInfo> getContactInfo(){
		return ResponseEntity.ok(contactService.getContactInfo());
	}
}
