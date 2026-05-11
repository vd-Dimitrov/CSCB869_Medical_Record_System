package com.nbu.cscb869_medical_record_system.data.repository;

import com.nbu.cscb869_medical_record_system.data.entity.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {
    List<SickLeave> findByPatientId(Long patientId);
    List<SickLeave> findByDoctorId(Long doctorId);

    @Query(value = "SELECT MONTH(start_date) as m, YEAR(start_date) as y, COUNT (*) as cnt "+
    "FROM sick_leaves GROUP BY y, m, ORDER BY  cnt DESC LIMIT 1", nativeQuery = true)
    List<Object[]> findMonthWithMostSickLeaves();

    @Query(value = "SELECT s.doctor, COUNT(S) as cnt FROM SickLeave s GROUP BY s.doctor ORDER BY cnt DESC")
    List<Object[]> findDoctorSickLeaveCounts();
}
