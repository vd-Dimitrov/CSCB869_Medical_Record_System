package com.nbu.cscb869_medical_record_system.service.impl;

import com.nbu.cscb869_medical_record_system.data.dto.CheckUpDto;
import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.entity.SickLeave;
import com.nbu.cscb869_medical_record_system.data.repository.CheckUpRepository;
import com.nbu.cscb869_medical_record_system.data.repository.SickLeaveRepository;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.helpers.EntityMapper;
import com.nbu.cscb869_medical_record_system.service.CheckUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CheckUpServiceImpl implements CheckUpService {

    private final CheckUpRepository checkUpRepository;
    private final SickLeaveRepository sickLeaveRepository;
    private final EntityMapper entityMapper;

    @Override
    public List<CheckUp> findAll() {
        return checkUpRepository.findAll();
    }

    @Override
    public CheckUp findById(Long id) {
        return checkUpRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Check up", id));
    }

    @Override
    public List<CheckUp> findByPatient(Long patientId) {
        return checkUpRepository.findByPatientId(patientId);
    }

    @Override
    public List<CheckUp> findByDoctor(Long doctorId) {
        return checkUpRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<CheckUp> findByDiagnosis(Long diagnosisId) {
        return checkUpRepository.findByDiagnosesId(diagnosisId);
    }

    @Override
    public List<CheckUp> findByDoctorAndPeriod(Long doctorId, LocalDate from, LocalDate to) {
        return checkUpRepository.findByDoctorIdAndDateBetween(doctorId, from, to);
    }

    @Override
    @Transactional
    public CheckUp save(CheckUpDto dto) {
        CheckUp checkUp = new CheckUp();
        entityMapper.map(dto, checkUp);
        CheckUp saved = checkUpRepository.save(checkUp);
        if (dto.isCreateSickLeave() && dto.getSickLeaveDuration() != null) {
            createSickLeave(saved, dto.getSickLeaveDuration());
        }
        return saved;
    }

    @Override
    @Transactional
    public CheckUp update(Long id, CheckUpDto dto, Long currentDoctorId) {
        CheckUp checkUp = findById(id);
        if (currentDoctorId != null && !checkUp.getDoctor().getId().equals(currentDoctorId)){
            throw new AccessDeniedException("You can only edit your own check-ups");
        }
        entityMapper.map(dto, checkUp);
        CheckUp saved = checkUpRepository.save(checkUp);
        if (dto.isCreateSickLeave() && dto.getSickLeaveDuration() != null) {
            createSickLeave(saved, dto.getSickLeaveDuration());
        }
        return saved;
    }

    @Override
    public void delete(Long id) {
        findById(id);
        checkUpRepository.deleteById(id);
    }

    private void createSickLeave(CheckUp checkUp, int durationDays) {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setPatient(checkUp.getPatient());
        sickLeave.setDoctor(checkUp.getDoctor());
        sickLeave.setStartDate(checkUp.getDate());
        sickLeave.setDurationDays(durationDays);
        sickLeaveRepository.save(sickLeave);
    }
}
