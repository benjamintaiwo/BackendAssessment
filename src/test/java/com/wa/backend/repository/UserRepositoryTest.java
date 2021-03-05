/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wa.backend.repository;

import com.wa.backend.entity.UserEntity;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 *
 * @author Benjamin.Abegunde
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	static boolean recordsCreated = false;
	

	@BeforeEach
	void setUp() throws Exception {
		
		if(!recordsCreated) createRecords();
	}

	@Test
	final void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(1, 1);
		Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(page);
		
        List<UserEntity> userEntities = page.getContent();
        assertNotNull(userEntities);
        assertFalse(userEntities.size() == 1);
	}
	
	
	

	
	
 
	@Test 
	final void testUpdateUserEmailVerificationStatus()
	{
		boolean newEmailVerificationStatus = true;
		userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
		
		UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");
		
		boolean storedEmailVerificationStatus = storedUserDetails.getVerified();
		
		assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);

	}
	
	
	@Test 
	final void testFindUserEntityByUserId()
	{
		String userId = "1a2b3c";
		UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
		
		assertNotNull(userEntity);
		assertTrue(userEntity.getUserId().equals(userId));
	}
	

	
	@Test 
	final void testUpdateUserEntityEmailVerificationStatus()
	{
		boolean newEmailVerificationStatus = true;
		userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
		
		UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");
		
		boolean storedEmailVerificationStatus = storedUserDetails.getVerified();
		
		assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);

	}
	
	private void createRecords()
	{
		// Prepare User Entity
             Date today = new Date();
	     UserEntity userEntity = new UserEntity();
             userEntity.setTitle("Mr");
	     userEntity.setFirstName("Ben");
	     userEntity.setLastName("Taiwo");
	     userEntity.setPassword("xxx");
	     userEntity.setEmail("test@test.com");
	     userEntity.setVerified(true);
             userEntity.setMobilePhone("08067846033");
             userEntity.setDateRegistered(today);
             userEntity.setDateVerified(today);
             userEntity.setUserId("1a2b3c");;
             
	
	     userRepository.save(userEntity);
	     
	     
			// Prepare User Entity
	     UserEntity userEntity2 = new UserEntity();
             userEntity.setTitle("Mr");
	     userEntity2.setFirstName("Ben");
	     userEntity2.setLastName("Taiwo");
	     userEntity2.setPassword("xxx");
	     userEntity2.setEmail("test@test.com");
             userEntity2.setVerified(true);
             userEntity2.setMobilePhone("08067846033");
             userEntity2.setDateRegistered(today);
             userEntity2.setDateVerified(today);
             userEntity2.setUserId("1a2b3cdfgrowh");
	    
	     userRepository.save(userEntity2);
	     
	     recordsCreated = true;
    
	}
}
