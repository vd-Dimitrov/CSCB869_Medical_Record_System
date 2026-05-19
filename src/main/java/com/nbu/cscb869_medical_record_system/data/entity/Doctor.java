package com.nbu.cscb869_medical_record_system.data.entity;

import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends Person{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicalSpecialty specialty;

    @Column(name = "can_be_general_practitioner", nullable = false)
    private boolean canBeGeneralPractitioner;

    //Constructors, getters, setters handled by Lombok annotation

}
