package com.nbu.cscb869_medical_record_system.data.repository;

import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckUpRepository extends JpaRepository<CheckUp, Long> {
    List<CheckUp> findByPatientId(Long patientId);
    List<CheckUp> findByDoctorId(Long doctorId);
    List<CheckUp> findByDiagnosisId(Long diagnosisId);

    List<CheckUp> findByDoctorAndDateBetween(Doctor doctor, LocalDate from, LocalDate to);
    List<CheckUp> findByDoctorIdAndDateBetween(Long doctorId, LocalDate from, LocalDate to);
}
