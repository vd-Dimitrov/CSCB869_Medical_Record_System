package com.nbu.cscb869_medical_record_system.controllers.rest;

import com.nbu.cscb869_medical_record_system.data.dto.PatientDto;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patients")
public class PatientRestController {

    private final PatientService patientService;

    @GetMapping
    public List<Patient> getAll(@AuthenticationPrincipal SecurityUser securityUser){
        if (securityUser.getRole()== UserRole.PATIENT){
            return List.of(patientService.findById(securityUser.getPatientId()));
        }
        return patientService.findAll();
    }

    @GetMapping("/{id}")
    public Patient getById(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser){
        if (securityUser.getRole() == UserRole.PATIENT && !id.equals(securityUser.getPatientId())){
            throw new AccessDeniedException("You can only view your own record");
        }
        return patientService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Patient> create(@Valid @RequestBody PatientDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.save(dto));
    }
    @PutMapping("/{id}")
    public Patient update(@Valid @RequestBody PatientDto dto, @PathVariable Long id){
        return patientService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Patient> delete(@PathVariable Long id){
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
