package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.CheckUpDto;
import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;

import java.time.LocalDate;
import java.util.List;

public interface CheckUpService {
    List<CheckUp> findAll();

    CheckUp findById(Long id);

    List<CheckUp> findByPatient(Long patientId);
    List<CheckUp> findByDoctor(Long doctorId);
    List<CheckUp> findByDiagnosis(Long diagnosisId);
    List<CheckUp> findByDoctorAndPeriod(Long doctorId, LocalDate from, LocalDate to);

    CheckUp save(CheckUpDto dto);
    CheckUp  update(Long id, CheckUpDto dto, Long currentDoctorId);

    void delete(Long id);
}
