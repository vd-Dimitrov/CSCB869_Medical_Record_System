package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.data.dto.PatientDto;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping
    public String list(@AuthenticationPrincipal SecurityUser user, Model model) {
        List<Patient> patients;
        if (Objects.requireNonNull(user.getRole()) == UserRole.PATIENT) {
            patients = List.of(patientService.findById(user.getPatientId()));
        } else {
            patients = patientService.findAll();
        }
        model.addAttribute("patients", patients);
        return "patients/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @AuthenticationPrincipal SecurityUser user, Model model) {
        if (user.getRole().name().equals("PATIENT") && !id.equals(user.getPatientId())) {
            return "redirect:/patients/" + user.getPatientId();
        }
        model.addAttribute("patient", patientService.findById(id));
        return "patients/details";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("patientDto", new PatientDto());
        model.addAttribute("generalPractitioners", doctorService.findGeneralPractitioners());
        return "patients/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute PatientDto patientDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("generalPractitioners", doctorService.findGeneralPractitioners());
            return "patients/form";
        }
        patientService.save(patientDto);
        return "redirect:/patients";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.findById(id);
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setEgn(patient.getEgn());
        dto.setHasHealthInsurance(patient.isHasInsurance());
        if (patient.getGeneralPractitioner() != null) {
            dto.setGeneralPractitionerId(patient.getGeneralPractitioner().getId());
        }
        model.addAttribute("patientDto", dto);
        model.addAttribute("generalPractitioners", doctorService.findGeneralPractitioners());
        return "patients/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute PatientDto patientDto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("generalPractitioners", doctorService.findGeneralPractitioners());
            return "patients/form";
        }
        patientService.update(id, patientDto);
        return "redirect:/patients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        patientService.delete(id);
        return "redirect:/patients";
    }

}
