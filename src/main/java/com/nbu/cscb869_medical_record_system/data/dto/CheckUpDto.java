package com.nbu.cscb869_medical_record_system.data.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CheckUpDto {

    private Long id;

    @NotNull(message = "Doctor is required")
    private Long doctorId;

    @NotNull(message = "Patient is required")
    private Long patientId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotEmpty(message = "At least one diagnosis is required")
    private List<Long> diagnosisIds;

    private String treatment;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price must be non-negative")
    private BigDecimal price;

    private boolean createSickLeave;

    @Min(value = 1, message = "Sick leave duration must be at least 1 day")
    private Integer sickLeaveDuration;
}
