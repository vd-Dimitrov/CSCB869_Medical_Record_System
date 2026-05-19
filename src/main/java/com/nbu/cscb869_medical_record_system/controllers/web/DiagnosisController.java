package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.data.dto.DiagnosisDto;
import com.nbu.cscb869_medical_record_system.data.entity.Diagnosis;
import com.nbu.cscb869_medical_record_system.service.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("diagnoses", diagnosisService.findAll());
        return "diagnoses/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("diagnosisDto", new DiagnosisDto());
        return "diagnoses/form";
    }


    @PostMapping("/new")
    public String create(@Valid @ModelAttribute DiagnosisDto diagnosisDto, BindingResult result) {
        if (result.hasErrors()) return "diagnoses/form";
        diagnosisService.save(diagnosisDto);
        return "redirect:/diagnoses";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Diagnosis diagnosis = diagnosisService.findById(id);
        DiagnosisDto dto = new DiagnosisDto();
        dto.setId(diagnosis.getId());
        dto.setName(diagnosis.getName());
        dto.setDescription(diagnosis.getDescription());
        model.addAttribute("diagnosisDto", dto);
        return "diagnoses/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute DiagnosisDto diagnosisDto,
                         BindingResult result) {
        if (result.hasErrors()) return "diagnoses/form";
        diagnosisService.update(id, diagnosisDto);
        return "redirect:/diagnoses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        diagnosisService.delete(id);
        return "redirect:/diagnoses";
    }
}
