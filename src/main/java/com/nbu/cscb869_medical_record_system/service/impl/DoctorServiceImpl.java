package com.nbu.cscb869_medical_record_system.service.impl;

import com.nbu.cscb869_medical_record_system.data.dto.DoctorDto;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.repository.DoctorRepository;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.helpers.EntityMapper;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Doctor", id));
    }

    @Override
    public List<Doctor> findGeneralPractitioners() {
        return doctorRepository.findByCanBeGeneralPractitionerTrue();
    }

    @Override
    public Doctor save(DoctorDto dto) {
        Doctor doctor = new Doctor();
        EntityMapper.map(dto, doctor);
        return null;
    }

    @Override
    public Doctor update(Long id, DoctorDto dto) {
        Doctor doctor = findById(id);
        EntityMapper.map(dto, doctor);
        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        doctorRepository.deleteById(id);
    }
}
