package com.nbu.cscb869_medical_record_system.service.impl;

import com.nbu.cscb869_medical_record_system.data.dto.SickLeaveDto;
import com.nbu.cscb869_medical_record_system.data.entity.SickLeave;
import com.nbu.cscb869_medical_record_system.data.repository.SickLeaveRepository;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.helpers.EntityMapper;
import com.nbu.cscb869_medical_record_system.service.SickLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SickLeaveServiceImpl implements SickLeaveService {
    private final SickLeaveRepository sickLeaveRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SickLeave> findAll() {
        return sickLeaveRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SickLeave findById(Long id) {
        return sickLeaveRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Sick leave", id));
    }

    @Override
    public List<SickLeave> findByPatient(Long patientId) {
        return sickLeaveRepository.findByPatientId(patientId);
    }

    @Override
    public List<SickLeave> findByDoctor(Long doctorId) {
        return sickLeaveRepository.findByDoctorId(doctorId);
    }

    @Override
    public SickLeave save(SickLeaveDto dto) {
        SickLeave sickLeave = new SickLeave();
        entityMapper.map(dto, sickLeave);
        return sickLeaveRepository.save(sickLeave);
    }

    @Override
    public SickLeave update(Long id, SickLeaveDto dto, Long currentDoctorId) {
        SickLeave sickLeave = sickLeaveRepository.getReferenceById(id);
        if (currentDoctorId != null && !sickLeave.getDoctor().getId().equals(currentDoctorId)){
            throw new AccessDeniedException("You can only edit your own sick leaves");
        }
        entityMapper.map(dto, sickLeave);
        return sickLeaveRepository.save(sickLeave);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        sickLeaveRepository.deleteById(id);
    }
}
