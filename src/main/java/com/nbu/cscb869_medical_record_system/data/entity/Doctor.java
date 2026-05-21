package com.nbu.cscb869_medical_record_system.data.entity;

import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends Person{
    @ElementCollection
    @CollectionTable(name = "doctor_specialties", joinColumns = @JoinColumn(name = "doctor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "specialty", nullable = false)
    private List<MedicalSpecialty> specialties;

    @Column(name = "can_be_general_practitioner", nullable = false)
    private boolean canBeGeneralPractitioner;

    //Constructors, getters, setters handled by Lombok annotation

}
