package com.nbu.cscb869_medical_record_system.service.impl;

import com.nbu.cscb869_medical_record_system.data.dto.PatientDto;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.repository.DoctorRepository;
import com.nbu.cscb869_medical_record_system.data.repository.PatientRepository;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Patient is not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findByGeneralPractitioner(Long doctorId) {
        return patientRepository.findByGeneralPractitionerId(doctorId);
    }

    @Override
    public Patient save(PatientDto dto) {
        Patient patient = new Patient();
        mapDtoToEntity(dto, patient);
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Long id, PatientDto dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
    private void mapDtoToEntity(PatientDto dto, Patient patient){
        patient.setName(dto.getName());
        patient.setEgn(dto.getEgn());
        patient.setHasInsurance(dto.isHasHealthInsurance());
        if (dto.getGeneralPractitionerId() != null){
            Doctor gp = doctorRepository.findById(dto.getGeneralPractitionerId())
                    .orElseThrow( () -> new ResourceNotFoundException("Doctor not found with id: " + dto.getGeneralPractitionerId()));
            patient.setGeneralPractitioner(gp);
        }
    }

}
