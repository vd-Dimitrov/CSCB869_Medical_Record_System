package com.nbu.cscb869_medical_record_system.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDto {
    private Long id;

    @NotBlank(message = "name can't be blank")
    private String name;

    @NotBlank(message = "egn is required")
    @Pattern(regexp = "\\d{10}", message = "EGN must be exactly 10 digis")
    private String egn;

    @NotNull(message = "General Practitioner is required")
    private Long generalPractitionerId;

    private boolean hasHealthInsurance;
}
