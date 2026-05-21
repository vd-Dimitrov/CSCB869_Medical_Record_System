package com.nbu.cscb869_medical_record_system.helpers;

import com.nbu.cscb869_medical_record_system.data.dto.*;
import com.nbu.cscb869_medical_record_system.data.entity.*;
import com.nbu.cscb869_medical_record_system.data.repository.DiagnosisRepository;
import com.nbu.cscb869_medical_record_system.data.repository.DoctorRepository;
import com.nbu.cscb869_medical_record_system.data.repository.PatientRepository;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityMapper {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DiagnosisRepository diagnosisRepository;


    public static void map(DoctorDto dto, Doctor entity){
        entity.setName(dto.getName());
        entity.setSpecialties(dto.getSpecialties());
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

    public void map(DiagnosisDto dto, Diagnosis entity){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }

    public void map(CheckUpDto dto, CheckUp checkUp){
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow( () -> new ResourceNotFoundException("Patient", dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow( () -> new ResourceNotFoundException("Doctor", dto.getDoctorId()));
        List<Diagnosis> diagnoses = dto.getDiagnosisIds().stream()
                .map(id -> diagnosisRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Diagnosis", id)))
                .collect(java.util.stream.Collectors.toList());

        checkUp.setPatient(patient);
        checkUp.setDoctor(doctor);
        checkUp.setDate(dto.getDate());
        checkUp.setDiagnoses(diagnoses);
        checkUp.setTreatment(dto.getTreatment());
        checkUp.setPrice(dto.getPrice());
        checkUp.setPaidByPatient(!patient.isHasInsurance());
    }

    public void map(SickLeaveDto dto, SickLeave sickLeave){
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow( () -> new ResourceNotFoundException("Patient", dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow( () -> new ResourceNotFoundException("Doctor", dto.getDoctorId()));
        sickLeave.setPatient(patient);
        sickLeave.setDoctor(doctor);
        sickLeave.setStartDate(dto.getStartDate());
        sickLeave.setDurationDays(dto.getDuration());
    }

}
