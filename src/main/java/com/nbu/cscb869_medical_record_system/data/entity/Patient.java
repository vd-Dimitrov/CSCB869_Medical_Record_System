package com.nbu.cscb869_medical_record_system.data.entity;

public class Patient extends Person{
    Doctor generalPractitioner;

    public Doctor getGeneralPractitioner() {
        return generalPractitioner;
    }

    public void setGeneralPractitioner(Doctor generalPractitioner) {
        this.generalPractitioner = generalPractitioner;
    }
}
