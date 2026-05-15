package com.nbu.cscb869_medical_record_system.service.impl;

import com.nbu.cscb869_medical_record_system.data.dto.DiagnosisDto;
import com.nbu.cscb869_medical_record_system.data.entity.Diagnosis;
import com.nbu.cscb869_medical_record_system.data.repository.DiagnosisRepository;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.helpers.EntityMapper;
import com.nbu.cscb869_medical_record_system.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosisServicesImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Diagnosis> findAll() {
        return diagnosisRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Diagnosis findById(Long id) {
        return diagnosisRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Diagnosis", id));
    }

    @Override
    public Diagnosis save(DiagnosisDto dto) {
        Diagnosis diagnosis = new Diagnosis();
        entityMapper.map(dto, diagnosis);
        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public Diagnosis update(Long id, DiagnosisDto dto) {
        Diagnosis diagnosis = findById(id);
        entityMapper.map(dto, diagnosis);
        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        diagnosisRepository.deleteById(id);
    }
}
