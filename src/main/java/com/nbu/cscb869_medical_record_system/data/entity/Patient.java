package com.nbu.cscb869_medical_record_system.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.rmi.server.UID;

@Entity
public class Patient extends Person {
    @ManyToOne
    @JoinColumn(name = "general_practitioner_id")
    private Doctor generalPractitioner;

    public Doctor getGeneralPractitioner() {
        return generalPractitioner;
    }

    public void setGeneralPractitioner(Doctor generalPractitioner) {
        this.generalPractitioner = generalPractitioner;
    }
}
