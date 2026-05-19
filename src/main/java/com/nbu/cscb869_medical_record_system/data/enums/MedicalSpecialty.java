package com.nbu.cscb869_medical_record_system.data.enums;

public enum MedicalSpecialty {
    FAMILY_MEDICINE("Family medicine"),
    INTERNAL_MEDICINE("Internal medicine"),
    PEDIATRICS("Pediatrics"),
    CARDIOLOGY("Cardiology"),
    NEUROLOGY("Neurology"),
    DERMATOLOGY("Dermatology"),
    PSYCHIATRY("Psychiatry"),
    RADIOLOGY("Radiology"),
    SURGERY("Surgery"),
    EMERGENCY_MEDICINE("Emergency medicine"),;

    private final String displayName;

    MedicalSpecialty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}