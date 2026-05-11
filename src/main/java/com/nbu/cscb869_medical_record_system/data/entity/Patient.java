package com.nbu.cscb869_medical_record_system.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "patients")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Patient extends Person {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_practitioner_id")
    private Doctor generalPractitioner;

    @Column(nullable = false)
    private boolean hasInsurance;

    //Constructors, getters, setters handled by Lombok annotation

}
