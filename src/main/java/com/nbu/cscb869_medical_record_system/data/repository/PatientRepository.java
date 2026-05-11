package com.nbu.cscb869_medical_record_system.data.repository;

import com.nbu.cscb869_medical_record_system.data.entity.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
