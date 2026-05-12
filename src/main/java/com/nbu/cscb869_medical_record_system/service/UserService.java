package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.UserDto;
import com.nbu.cscb869_medical_record_system.data.entity.AppUser;

import java.util.List;

public interface UserService {
    List<AppUser> findAll();

    AppUser findById(Long id);

    AppUser save(UserDto dto);

    AppUser update(Long id, UserDto dto);

    void delete(Long id);
}
