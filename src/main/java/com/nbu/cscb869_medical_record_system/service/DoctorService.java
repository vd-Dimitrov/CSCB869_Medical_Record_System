package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.DoctorDto;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> findAll();

    Doctor findById(Long id);

    List<Doctor> findGeneralPractitioners();

    Doctor save (DoctorDto dto);
    Doctor update(Long id, DoctorDto dto);
    void delete(Long id);
}
