package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.PatientDto;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> findAll();

    Patient findById(Long id);

    List<Patient> findByGeneralPractitioner(Long doctorId);

    Patient save(PatientDto dto);
    Patient update(Long id, PatientDto dto);

    void delete(Long id);
}
