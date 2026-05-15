package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.DiagnosisDto;
import com.nbu.cscb869_medical_record_system.data.entity.Diagnosis;

import java.util.List;

public interface DiagnosisService {
    List<Diagnosis> findAll();

    Diagnosis findById(Long id);

    Diagnosis save(DiagnosisDto dto);

    Diagnosis update(Long id, DiagnosisDto dto);

    void delete(Long id);

}
