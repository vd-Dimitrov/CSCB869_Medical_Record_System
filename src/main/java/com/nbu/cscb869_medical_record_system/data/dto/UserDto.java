package com.nbu.cscb869_medical_record_system.data.dto;

import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Username should not be blank")
    private String username;

    @NotBlank(message = "Password should not be blank")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;

    private Long doctorId;

    private Long patientId;
}

