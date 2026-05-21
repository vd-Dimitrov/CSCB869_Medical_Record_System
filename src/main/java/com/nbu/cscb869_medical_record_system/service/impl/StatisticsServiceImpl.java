package com.nbu.cscb869_medical_record_system.service.impl;

import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.repository.CheckUpRepository;
import com.nbu.cscb869_medical_record_system.data.repository.DoctorRepository;
import com.nbu.cscb869_medical_record_system.data.repository.PatientRepository;
import com.nbu.cscb869_medical_record_system.data.repository.SickLeaveRepository;
import com.nbu.cscb869_medical_record_system.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final CheckUpRepository checkUpRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SickLeaveRepository sickLeaveRepository;

    @Override
    public List<Patient> getPatientsByDiagnosis(Long diagnosisId) {
        return checkUpRepository.findByDiagnosisId(diagnosisId).stream()
                .map(CheckUp::getPatient)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public String getMostCommonDiagnosis() {
        List<Object[]> results = checkUpRepository.findDiagnosisCounts();
        if (results.isEmpty()) return "N/A";
        return String.valueOf(results.get(0)[0]);
    }

    @Override
    public List<Patient> getPatientsByGP(Long doctorId) {
        return patientRepository.findByGeneralPractitionerId(doctorId);
    }

    @Override
    public BigDecimal getTotalPatientPaidAmount() {
        BigDecimal result = checkUpRepository.findTotalPatientPaidAmount();
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public Map<Doctor, BigDecimal> getPatientPaidAmountByDoctor() {
        List<Object[]> results = checkUpRepository.findPatientPaidAmountByDoctor();
        Map<Doctor, BigDecimal> map = new HashMap<>();
        for (Object[] obj : results) {
            map.put((Doctor) obj[0], (BigDecimal) obj[1]);
        }
        return map;
    }

    @Override
    public Map<Doctor, Long> getPatientCountByGP() {
        List<Doctor> gps = doctorRepository.findByCanBeGeneralPractitionerTrue();
        Map<Doctor, Long> map = new HashMap<>();
        for (Doctor gp : gps) {
            long count = patientRepository.findByGeneralPractitionerId(gp.getId()).size();
            map.put(gp, count);
        }
        return map;
    }

    @Override
    public Map<Doctor, Long> getVisitCountByDoctor() {
        List<Object[]> results = checkUpRepository.findVisitCountByDoctor();
        Map<Doctor, Long> map = new HashMap<>();
        for (Object[] row : results) {
            map.put((Doctor) row[0], (Long) row[1]);
        }
        return map;
    }

    @Override
    public List<CheckUp> getPatientVisitHistory(Long patientId) {
        return checkUpRepository.findByPatientId(patientId);
    }

    @Override
    public List<CheckUp> getCheckUpsByDoctorAndPeriod(Long doctorId, LocalDate from, LocalDate to) {
        return checkUpRepository.findByDoctorIdAndDateBetween(doctorId, from, to);
    }

    @Override
    public String getMonthWithMostSickLeaves() {
        List<Object[]> results = sickLeaveRepository.findMonthWithMostSickLeaves();
        if (results.isEmpty()) return "N/A";
        Object[] row = results.get(0);
        int month = ((Number) row[0]).intValue();
        int year = ((Number) row[1]).intValue();
        return Month.of(month).name() + " " + year;
    }

    @Override
    public List<Doctor> getDoctorsWithMostSickLeaves() {
        List<Object[]> results = sickLeaveRepository.findDoctorSickLeaveCounts();
        if (results.isEmpty()) return List.of();
        long maxCount = ((Long) results.get(0)[1]);
        return results.stream()
                .filter(row -> ((Long) row[1]) == maxCount)
                .map(row -> (Doctor) row[0])
                .collect(Collectors.toList());
    }
}
