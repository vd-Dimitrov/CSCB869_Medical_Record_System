package com.nbu.cscb869_medical_record_system.controllers.rest;

import com.nbu.cscb869_medical_record_system.data.dto.DiagnosisDto;
import com.nbu.cscb869_medical_record_system.data.entity.Diagnosis;
import com.nbu.cscb869_medical_record_system.service.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
public class DiagnosisRestController {

    private final DiagnosisService diagnosisService;

    @GetMapping
    public List<Diagnosis> getAll(){
        return diagnosisService.findAll();
    }

    @GetMapping("/{id}")
    public Diagnosis getById(@PathVariable Long id){
        return diagnosisService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Diagnosis> create(@Valid @RequestBody DiagnosisDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(diagnosisService.save(dto));
    }

    @PutMapping("/{id}")
    public Diagnosis update(@PathVariable Long id, @Valid @RequestBody DiagnosisDto dto){
        return diagnosisService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        diagnosisService.delete(id);
        return ResponseEntity.noContent().build();
    }
 }
