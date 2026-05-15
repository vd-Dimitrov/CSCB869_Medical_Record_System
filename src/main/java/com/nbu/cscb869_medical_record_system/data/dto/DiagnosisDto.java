package com.nbu.cscb869_medical_record_system.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiagnosisDto {
    private Long id;

    @NotBlank(message = "name cannot be blank")
    private String name;

    private String description;
}
