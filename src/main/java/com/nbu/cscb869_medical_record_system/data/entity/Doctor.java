package com.nbu.cscb869_medical_record_system.data.entity;

import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;
import jakarta.persistence.Entity;

@Entity
public class Doctor extends Person{
    private MedicalSpecialty medicalSpecialty;
    private boolean canBeGeneralPractitioner;

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
