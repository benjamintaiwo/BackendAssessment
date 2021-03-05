package com.wa.backend.service.impl;

import com.wa.backend.Util.AmazonSES;
import com.wa.backend.Util.Utils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.wa.backend.entity.UserEntity;
import com.wa.backend.entity.enums.RoleEnums;
import com.wa.backend.repository.UserRepository;
import com.wa.backend.vo.UserDto;
import com.wa.backend.vo.UserServiceException;
import java.util.Date;
import org.mockito.Mockito;
/**
 *
 * @author Benjamin.Abegunde
 */
class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;
 
	@Mock
	Utils utils;
	
	@Mock
	AmazonSES amazonSES;
        
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
 
	String userId = "hhty57ehfy";
	String encryptedPassword = "74hghd8474jf";
	
	UserEntity userEntity;
 
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Date today = new Date();
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Ben");
		userEntity.setLastName("Taiwo");
		userEntity.setUserId(userId);
		userEntity.setPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("7htnfhr758");
                userEntity.setMobilePhone("08067848034");
                userEntity.setDateRegistered(today);
                userEntity.setTheRole(RoleEnums.USER.name());
	
	}

	@Test
	final void testGetUser() {
 
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser("test@test.com");

		assertNotNull(userDto);
		assertEquals("Ben", userDto.getFirstName());

	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class,

				() -> {
					userService.getUser("test@test.com");
				}

		);
	}
	
	@Test
	final void testCreateUser_CreateUserServiceException()
	{
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDto userDto = new UserDto();
		userDto.setFirstName("Ben");
		userDto.setLastName("Taiwo");
		userDto.setPassword("12345678");
		userDto.setEmail("test@test.com");
 	
		assertThrows(UserServiceException.class,

				() -> {
					userService.createUser(userDto);
				}

		);
	}
	
	@Test
	final void testCreateUser()
	{
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
                Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));
		
 		
		UserDto userDto = new UserDto();
		
		userDto.setFirstName("Ben");
		userDto.setLastName("Taiwo");
		userDto.setPassword("12345678");
		userDto.setEmail("test@test.com");
                userDto.setMobilePhone("08067848034");
                

		UserDto storedUserDetails = userService.createUser(userDto);
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());

		verify(bCryptPasswordEncoder, times(1)).encode("12345678");
		verify(userRepository,times(1)).save(any(UserEntity.class));
	}
	
	
	
	


}
