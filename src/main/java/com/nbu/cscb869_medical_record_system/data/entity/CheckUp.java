package com.nbu.cscb869_medical_record_system.data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CheckUp {
    private long checkUpId;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private String diagnose;
    private String treatment;
    private BigDecimal checkUpPrice;

    public long getCheckUpId() {
        return checkUpId;
    }

    public void setCheckUpId(long checkUpId) {
        this.checkUpId = checkUpId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public BigDecimal getCheckUpPrice() {
        return checkUpPrice;
    }

    public void setCheckUpPrice(BigDecimal checkUpPrice) {
        this.checkUpPrice = checkUpPrice;
    }
}
