/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wa.backend.entity;

import com.wa.backend.entity.enums.RoleEnums;
import com.wa.backend.entity.enums.StatusEnums;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Benjamin.Abegunde
 */
@Entity
@Table(name="users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false, length = 50)
    private String title;

    @Column(nullable=false, length=50)
    private String firstName;

    @Column(nullable=false, length=50)
    private String lastName;

    @Column(nullable=false, length=120)
    private String email;
    
    @Column(nullable=false, length=120)
    private String mobilePhone;
    
    @Column(nullable=false)
    private String password;

    private String emailVerificationToken;

    @Column(nullable=false)
    private Boolean verified = false;
   
    @Enumerated(EnumType.STRING)
    private RoleEnums theRole;
    
    @Enumerated(EnumType.STRING)
    private StatusEnums status;
    
    private LocalDate dateVerified, dateDeactivated;
    
    private LocalDate dateRegistered, dateUpdated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public RoleEnums getTheRole() {
        return theRole;
    }

    public void setTheRole(RoleEnums theRole) {
        this.theRole = theRole;
    }

    public StatusEnums getStatus() {
        return status;
    }

    public void setStatus(StatusEnums status) {
        this.status = status;
    }

    public LocalDate getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(LocalDate dateVerified) {
        this.dateVerified = dateVerified;
    }

    public LocalDate getDateDeactivated() {
        return dateDeactivated;
    }

    public void setDateDeactivated(LocalDate dateDeactivated) {
        this.dateDeactivated = dateDeactivated;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDate dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    
    
    
    
    
    
}
