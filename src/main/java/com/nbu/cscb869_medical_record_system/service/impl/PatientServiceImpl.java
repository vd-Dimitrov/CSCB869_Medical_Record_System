package com.nbu.cscb869_medical_record_system.service.impl;

import com.nbu.cscb869_medical_record_system.data.dto.PatientDto;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.repository.PatientRepository;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.helpers.EntityMapper;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final EntityMapper entityMapper;
    private final PatientRepository patientRepository;

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Patient", id));
    }

    @Override
    public List<Patient> findByGeneralPractitioner(Long doctorId) {
        return patientRepository.findByGeneralPractitionerId(doctorId);
    }

    @Override
    public Patient save(PatientDto dto) {
        Patient patient = new Patient();
        entityMapper.map(dto, patient);
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Long id, PatientDto dto) {
        Patient patient = findById(id);
        entityMapper.map(dto, patient);
        return patientRepository.save(patient);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        patientRepository.deleteById(id);
    }

}
