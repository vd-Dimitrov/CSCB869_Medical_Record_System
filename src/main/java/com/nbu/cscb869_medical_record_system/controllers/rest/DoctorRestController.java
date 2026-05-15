package com.nbu.cscb869_medical_record_system.controllers.rest;

import com.nbu.cscb869_medical_record_system.data.dto.DoctorDto;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    @GetMapping
    public List<Doctor> getAll(){
        return doctorService.findAll();
    }

    @GetMapping("/{id}")
    public Doctor getById(@PathVariable Long id){
        return doctorService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Doctor> create (@Valid @RequestBody DoctorDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.save(dto));
    }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @Valid @RequestBody DoctorDto dto){
        return doctorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
