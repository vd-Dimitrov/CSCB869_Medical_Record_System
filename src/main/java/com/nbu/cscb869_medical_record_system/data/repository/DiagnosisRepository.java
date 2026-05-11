package com.nbu.cscb869_medical_record_system.data.repository;

import com.nbu.cscb869_medical_record_system.data.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Diagnosis> findByNameIgnoreCase(String name);
}
