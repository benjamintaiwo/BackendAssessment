package com.wa.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wa.backend.service.impl.UserServiceImpl;
import com.wa.backend.vo.UserDto;
import com.wa.backend.vo.UserRest;

/**
 *
 * @author Benjamin.Abegunde
 */
class UserControllerTest {

	@InjectMocks
	UserController userController;
	
	@Mock
	UserServiceImpl userService;
	
	UserDto userDto;
	
	final String USER_ID = "bfhry47fhdjd7463gdh";
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDto = new UserDto();
        userDto.setTitle(USER_ID);
        userDto.setFirstName("Ben");
        userDto.setLastName("Taiwo");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setEncryptedPassword("xcf58tugh47");
        userDto.setMobilePhone("08067845033");
		
	}

	@Test
	final void testGetUser() {
	    when(userService.getUserByUserId(anyString())).thenReturn(userDto);	
	    
	    UserRest userRest = userController.getUser(USER_ID);
	    
	    assertNotNull(userRest);
	    assertEquals(USER_ID, userRest.getUserId());
	    assertEquals(userDto.getFirstName(), userRest.getFirstName());
	    assertEquals(userDto.getLastName(), userRest.getLastName());
            assertEquals(userDto.getTitle(), userRest.getTitle());
            assertEquals(userDto.getMobilePhone(), userRest.getMobilePhone());
	}
	
	
	

}
