package com.nbu.cscb869_medical_record_system.helpers;

import com.nbu.cscb869_medical_record_system.data.dto.DoctorDto;
import com.nbu.cscb869_medical_record_system.data.dto.PatientDto;
import com.nbu.cscb869_medical_record_system.data.dto.UserDto;
import com.nbu.cscb869_medical_record_system.data.entity.AppUser;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.repository.DoctorRepository;
import com.nbu.cscb869_medical_record_system.data.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityMapper {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public EntityMapper(DoctorRepository doctorRepository, PatientRepository patientRepository){
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public static void map(DoctorDto dto, Doctor entity){
        entity.setName(dto.getName());
        entity.setSpecialty(dto.getSpecialty());
        entity.setEgn(dto.getEgn());
        entity.setCanBeGeneralPractitioner(dto.isCanBeGeneralPractitioner());
    }

    public void map(PatientDto dto, Patient entity){
        entity.setName(dto.getName());
        entity.setEgn(dto.getEgn());
        entity.setHasInsurance(dto.isHasHealthInsurance());
        entity.setGeneralPractitioner(doctorRepository.getReferenceById(dto.getGeneralPractitionerId()));
    }

    public void map(UserDto dto, AppUser entity){
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setDoctor(doctorRepository.findById(dto.getDoctorId()).orElseThrow());
        entity.setPatient(patientRepository.findById(dto.getPatientId()).orElseThrow());
    }

}
