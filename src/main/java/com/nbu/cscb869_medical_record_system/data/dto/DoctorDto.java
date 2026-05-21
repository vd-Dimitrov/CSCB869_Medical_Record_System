package com.nbu.cscb869_medical_record_system.data.dto;

import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "egn is required")
    @Pattern(regexp = "\\d{10}", message = "egn must be 10 digits")
    private String egn;

    @NotEmpty(message = "At least one specialty is required")
    private List<MedicalSpecialty> specialties;

    private boolean canBeGeneralPractitioner;
}
