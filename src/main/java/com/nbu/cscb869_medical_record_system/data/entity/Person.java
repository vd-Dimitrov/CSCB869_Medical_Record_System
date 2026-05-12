package com.nbu.cscb869_medical_record_system.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, length = 10)
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "EGN should be exactly 10 digits")
    private String  egn;

    @Column(nullable = false)
    @NotBlank
    private String name;

}
