/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wa.backend.service.impl;

import com.wa.backend.Util.AmazonSES;
import com.wa.backend.Util.Utils;
import com.wa.backend.entity.UserEntity;
import com.wa.backend.entity.enums.RoleEnums;
import com.wa.backend.entity.enums.StatusEnums;
import com.wa.backend.repository.UserRepository;
import com.wa.backend.service.UserService;
import com.wa.backend.vo.ErrorMessages;
import com.wa.backend.vo.UserDto;
import com.wa.backend.vo.UserServiceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Benjamin.Abegunde
 */
@Service
public class UserServiceImpl implements UserService{
    
  
	@Autowired
	private UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	@Autowired
        AmazonSES amazonSES;
 
	@Override
	public UserDto createUser(UserDto user) {
                

                if (user.getEmail().isBlank())
                    throw new UserServiceException("Kindly provide a user email");
                if (user.getFirstName().isBlank())
                    throw new UserServiceException("Kindly input firstname");
                if (user.getLastName().isBlank())
                    throw new UserServiceException("Kindly input lastname");
                if (user.getTheRole().isBlank())
                    throw new UserServiceException("the role field cannot be empty");
                if (user.getMobilePhone().isBlank())
                    throw new UserServiceException("kindly enter your phone number");
                if (user.getMobilePhone().length() != 11)
                    throw new UserServiceException("Kindly enter an eleven digit phone number");
		if (!Utils.isRoleValid(user.getTheRole()))
                    throw new UserServiceException("role can only be user or admin");
                if (!Utils.isEmailValid(user.getEmail()))
                    throw new UserServiceException("email is not valid");
                if (userRepository.findByEmail(user.getEmail()) != null)
			throw new UserServiceException("Record already exists");
                
                
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
                Date today = new Date();
		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
                userEntity.setDateRegistered(today);
                userEntity.setStatus(StatusEnums.REGISTERED.name());
                userEntity.setTheRole(user.getTheRole().equalsIgnoreCase("User") ? RoleEnums.USER.name() : RoleEnums.ADMIN.name());
                userEntity.setVerified(Boolean.FALSE);

		UserEntity storedUserDetails = userRepository.save(userEntity);
 
		//BeanUtils.copyProperties(storedUserDetails, returnValue);
		UserDto returnValue  = modelMapper.map(storedUserDetails, UserDto.class);
                try {
                    amazonSES.verifyEmail(returnValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new UserServiceException("Record saved but email service failed!");
                    //return returnValue;
            }
               

		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
 
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getPassword(), 
				userEntity.getVerified(),
				true, true,
				true, new ArrayList<>());

		//return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException("User with ID: " + userId + " not found");
                if (userEntity.getStatus().equalsIgnoreCase(StatusEnums.DEACTIVATED.name()))
                    throw new UsernameNotFoundException("User with ID: " + userId + " not found");

		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserDto returnValue = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());               
               
                Date dateUpdated= new Date();
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
                userEntity.setDateUpdated(dateUpdated);
                userEntity.setMobilePhone(user.getMobilePhone());
                userEntity.setEmail(user.getEmail());
                userEntity.setTitle(user.getTitle());
                

		UserEntity updatedUserDetails = userRepository.save(userEntity);
		returnValue = new ModelMapper().map(updatedUserDetails, UserDto.class);

		return returnValue;
	}

	@Transactional
	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
                String email = userEntity.getEmail();
                Date today = new Date();
                userEntity.setStatus(StatusEnums.DEACTIVATED.name());
                userEntity.setDateDeactivated(today);
                userRepository.save(userEntity);
               try {
                amazonSES.offBoard(email);
            } catch (Exception e) {
                return;
            }
                
                
		//userRepository.delete(userEntity);

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
                int newPage = page;
		List<UserDto> returnValue = new ArrayList<>();
		
		if(page>0) newPage = page-1;
		
		Pageable pageableRequest = PageRequest.of(newPage, limit);
		
		Page<UserEntity> usersPage = userRepository.findAllUsersNotDeactivated(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		
        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }
		
		return returnValue;
	}

	@Override
	public boolean verifyEmailToken(String token) {
	    boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = Utils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setVerified(Boolean.TRUE);
                userEntity.setStatus(StatusEnums.VERIFIED.name());
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
	}

	
	
}
