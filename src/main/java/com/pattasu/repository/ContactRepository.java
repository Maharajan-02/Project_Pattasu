package com.pattasu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pattasu.entity.ContactInfo;

@Repository
public interface ContactRepository extends JpaRepository<ContactInfo, String>{

}
