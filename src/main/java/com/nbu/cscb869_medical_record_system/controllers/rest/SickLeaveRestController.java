package com.nbu.cscb869_medical_record_system.controllers.rest;

import com.nbu.cscb869_medical_record_system.data.dto.SickLeaveDto;
import com.nbu.cscb869_medical_record_system.data.entity.SickLeave;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.service.SickLeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sickleaves")
@RequiredArgsConstructor
public class SickLeaveRestController {

    private final SickLeaveService sickLeaveService;

    @GetMapping
    public List<SickLeave> getAll(@AuthenticationPrincipal SecurityUser securityUser){
        if (securityUser.getRole()== UserRole.PATIENT){
            return sickLeaveService.findByPatient(securityUser.getPatientId());
        }
        return sickLeaveService.findAll();
    }

    @GetMapping("/{id}")
    public SickLeave getById(@AuthenticationPrincipal SecurityUser securityUser,
                             @PathVariable Long id){
        SickLeave sickLeave = sickLeaveService.findById(id);
        if (securityUser.getRole() == UserRole.PATIENT &&
                !sickLeave.getPatient().getId().equals(securityUser.getPatientId())){
            throw new AccessDeniedException("You can only view your own sick leaves");
        }
        return sickLeave;
    }

    @PostMapping
    public ResponseEntity<SickLeave> create(@Valid @RequestBody SickLeaveDto dto,
                                            @AuthenticationPrincipal SecurityUser securityUser){
        if (securityUser.getRole() == UserRole.DOCTOR){
            dto.setId(securityUser.getDoctorId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(sickLeaveService.save(dto));
    }

    @PutMapping("/{id}")
    public SickLeave update(@Valid @RequestBody SickLeaveDto dto,
                            @PathVariable Long id,
                            @AuthenticationPrincipal SecurityUser securityUser){
        Long doctorId = securityUser.getRole() == UserRole.DOCTOR ? securityUser.getDoctorId() : null;
        return sickLeaveService.update(id, dto, doctorId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        sickLeaveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
