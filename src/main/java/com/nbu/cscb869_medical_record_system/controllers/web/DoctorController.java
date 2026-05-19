package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.data.dto.DoctorDto;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public String list(Model model){
        List<Doctor> doctors = doctorService.findAll();
        model.addAttribute("doctors", doctors);
        return "doctors/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", doctorService.findById(id));
        return "doctors/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("doctorDto", new DoctorDto());
        model.addAttribute("specialties", MedicalSpecialty.values());
        return "doctors/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute DoctorDto doctorDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("specialties", MedicalSpecialty.values());
            return "doctors/form";
        }
        doctorService.save(doctorDto);
        return "redirect:/doctors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.findById(id);
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setEgn(doctor.getEgn());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setCanBeGeneralPractitioner(doctor.isCanBeGeneralPractitioner());
        model.addAttribute("doctorDto", dto);
        model.addAttribute("specialties", MedicalSpecialty.values());
        return "doctors/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute DoctorDto doctorDto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("specialties", MedicalSpecialty.values());
            return "doctors/form";
        }
        doctorService.update(id, doctorDto);
        return "redirect:/doctors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        doctorService.delete(id);
        return "redirect:/doctors";
    }
}
