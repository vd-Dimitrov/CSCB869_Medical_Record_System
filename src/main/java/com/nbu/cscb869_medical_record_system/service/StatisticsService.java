package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    List<Patient> getPatientsByDiagnosis(Long diagnosisId);

    String getMostCommonDiagnosis();

    List<Patient> getPatientsByGP(Long doctorId);

    BigDecimal getTotalPatientPaidAmount();

    Map<Doctor, BigDecimal> getPatientPaidAmountByDoctor();

    Map<Doctor, Long> getPatientCountByGP();

    Map<Doctor, Long> getVisitCountByDoctor();

    List<CheckUp> getPatientVisitHistory(Long patientId);

    List<CheckUp> getCheckUpsByDoctorAndPeriod(Long doctorId, LocalDate from, LocalDate to);

    String getMonthWithMostSickLeaves();

    List<Doctor> getDoctorsWithMostSickLeaves();
}
