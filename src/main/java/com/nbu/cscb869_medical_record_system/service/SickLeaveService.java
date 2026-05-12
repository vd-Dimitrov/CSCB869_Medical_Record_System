package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.SickLeaveDto;
import com.nbu.cscb869_medical_record_system.data.entity.SickLeave;

import java.util.List;

public interface SickLeaveService {
    List<SickLeave> findAll();
    SickLeave findById(Long id);
    List<SickLeave> findByPatient(Long patientId);
    List<SickLeave> findByDoctor(Long doctorId);
    SickLeave save(SickLeaveDto dto);
    SickLeave update(Long  id, SickLeaveDto dto, Long currentDoctorId);
    void delete(Long id);
}
