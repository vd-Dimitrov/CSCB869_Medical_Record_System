package com.nbu.cscb869_medical_record_system.data.repository;

import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckUpRepository extends JpaRepository<CheckUp, Long> {
    List<CheckUp> findByPatientId(Long patientId);
    List<CheckUp> findByDoctorId(Long doctorId);
    List<CheckUp> findByDiagnosesId(Long diagnosisId);

    List<CheckUp> findByDoctorAndDateBetween(Doctor doctor, LocalDate from, LocalDate to);
    List<CheckUp> findByDoctorIdAndDateBetween(Long doctorId, LocalDate from, LocalDate to);

    @Query("SELECT d.name, COUNT(c) FROM CheckUp c JOIN c.diagnoses d GROUP BY d ORDER BY COUNT(c) DESC")
    List<Object[]> findDiagnosisCounts();

    @Query("SELECT COALESCE(SUM(c.price), 0) FROM CheckUp c WHERE c.paidByPatient = true")
    BigDecimal findTotalPatientPaidAmount();

    @Query("SELECT c.doctor, COALESCE(SUM(c.price), 0) as total FROM CheckUp c WHERE c.paidByPatient = true GROUP BY c.doctor ORDER BY total DESC")
    List<Object[]> findPatientPaidAmountByDoctor();

    @Query("SELECT c.doctor, COUNT(c) as cnt FROM CheckUp c GROUP BY c.doctor ORDER BY cnt DESC")
    List<Object[]> findVisitCountByDoctor();


}
