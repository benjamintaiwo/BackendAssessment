/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wa.backend.repository;

import com.wa.backend.entity.UserEntity;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Benjamin.Abegunde
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findUserByEmailVerificationToken(String token); 
        
	@Query(value="select * from Users u where u.verified = 'true'", 
			countQuery="select count(*) from Users u where u.verified = 'true'", 
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress( Pageable pageableRequest );
	

        
    @Modifying
    @Transactional 
    @Query("UPDATE UserEntity u set u.verified =:verified where u.userId = :userId")
    void updateUserEntityEmailVerificationStatus(
    		@Param("verified") boolean verified,
            @Param("userId") String userId);
	

	
	
	
	@Transactional
	@Modifying
	@Query(value="update users u set u.verified=:verified where u.user_id=:userId", nativeQuery=true)
	void updateUserEmailVerificationStatus(@Param("verified") boolean verified, 
			@Param("userId") String userId);
	
	@Query("select user from UserEntity user where user.userId =:userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);



	
   

}
