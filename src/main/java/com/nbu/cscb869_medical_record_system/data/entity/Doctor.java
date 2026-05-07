package com.nbu.cscb869_medical_record_system.data.entity;

import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;

public class Doctor extends Person{
    MedicalSpecialty medicalSpecialty;
    boolean canBeGeneralPractitioner;

    public MedicalSpecialty getMedicalSpecialty() {
        return medicalSpecialty;
    }

    public void setMedicalSpecialty(MedicalSpecialty medicalSpecialty) {
        this.medicalSpecialty = medicalSpecialty;
    }

    public boolean isCanBeGeneralPractitioner() {
        return canBeGeneralPractitioner;
    }

    public void setCanBeGeneralPractitioner(boolean canBeGeneralPractitioner) {
        this.canBeGeneralPractitioner = canBeGeneralPractitioner;
    }
}
