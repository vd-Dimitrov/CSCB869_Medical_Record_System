package com.nbu.cscb869_medical_record_system.controllers.rest;

import com.nbu.cscb869_medical_record_system.data.dto.CheckUpDto;
import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
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
        return checkUpService.findAll();
    }

    @GetMapping("/{id}")
    public CheckUp getById(@PathVariable Long id){
        return checkUpService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CheckUp> create(@Valid @RequestBody CheckUpDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(checkUpService.save(dto));
    }

    //ToDo create user verification
//    @PutMapping("/{id}")
//    public CheckUp update(@PathVariable Long id,
//                          @Valid @RequestBody CheckUpDto dto){
//        return checkUpService.update(id, dto);
//    }
}
