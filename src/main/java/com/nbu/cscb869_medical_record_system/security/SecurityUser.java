package com.nbu.cscb869_medical_record_system.security;

import com.nbu.cscb869_medical_record_system.data.entity.AppUser;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final AppUser user;

    public SecurityUser(AppUser user){
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public UserRole getRole(){
        return user.getRole();
    }

    public Long getDoctorId(){
        return user.getDoctor() != null ? user.getDoctor().getId() : null;
    }

    public Long getPatientId(){
        return user.getRole() != null ? user.getPatient().getId() : null;
    }

    public AppUser getAppUser(){
        return user;
    }
}
