package com.nbu.cscb869_medical_record_system.controllers.rest;

import com.nbu.cscb869_medical_record_system.data.dto.CheckUpDto;
import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.service.CheckUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkups")
@RequiredArgsConstructor
public class CheckUpRestController {

    private final CheckUpService checkUpService;

    @GetMapping
    public List<CheckUp> getAll(@AuthenticationPrincipal SecurityUser securityUser){
        if(securityUser.getRole() == UserRole.PATIENT) {
            return checkUpService.findByPatient(securityUser.getPatientId());
        }
        return checkUpService.findAll();
    }

    @GetMapping("/{id}")
    public CheckUp getById(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser){
        CheckUp checkUp = checkUpService.findById(id);
        if (securityUser.getRole() == UserRole.PATIENT && !checkUp.getPatient().getId().equals(securityUser.getPatientId()))
        {
            throw new AccessDeniedException("You can only view your own check-ups");
        }
        return checkUp;
    }

    @PostMapping
    public ResponseEntity<CheckUp> create(@Valid @RequestBody CheckUpDto dto,
                                          @AuthenticationPrincipal SecurityUser securityUser){
        if (securityUser.getRole() == UserRole.DOCTOR){
            dto.setDoctorId(securityUser.getDoctorId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(checkUpService.save(dto));
    }

    @PutMapping("/{id}")
    public CheckUp update(@PathVariable Long id,
                          @Valid @RequestBody CheckUpDto dto,
                          @AuthenticationPrincipal SecurityUser securityUser){
        Long doctorId = securityUser.getRole() == UserRole.DOCTOR ? securityUser.getDoctorId() : null;
        return checkUpService.update(id, dto, doctorId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        checkUpService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
