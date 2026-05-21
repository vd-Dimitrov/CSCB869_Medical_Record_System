package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.data.dto.CheckUpDto;
import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.service.CheckUpService;
import com.nbu.cscb869_medical_record_system.service.DiagnosisService;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/checkups")
@RequiredArgsConstructor
public class CheckUpController {

    private final CheckUpService checkUpService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnosisService diagnosisService;

    @GetMapping
    public String list(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        List<CheckUp> checkUps;
        if (securityUser.getRole() == UserRole.PATIENT) {
            checkUps = checkUpService.findByPatient(securityUser.getPatientId());
        } else {
            checkUps = checkUpService.findAll();
        }

        model.addAttribute("checkUps", checkUps);
        model.addAttribute("currentDoctorId", securityUser.getDoctorId());
        return "checkups/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("checkUp", checkUpService.findById(id));
        return "checkups/detail";
    }

    @GetMapping("/new")
    public String newForm(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        CheckUpDto checkUpDto = new CheckUpDto();
        if (securityUser.getRole() == UserRole.DOCTOR) {
            checkUpDto.setDoctorId(securityUser.getDoctorId());
        }
        model.addAttribute("checkUpDto", checkUpDto);
        populateFormModel(model, securityUser);
        return "checkups/form";
    }

    @PostMapping("/new")
    public String create(@AuthenticationPrincipal SecurityUser securityUser,
                         @Valid @ModelAttribute CheckUpDto checkUpDto,
                         BindingResult bindingResult,
                         Model model){
        if (bindingResult.hasErrors()) {
            populateFormModel(model, securityUser);
            return "checkups/form";
        }
        checkUpService.save(checkUpDto);
        return "redirect:/checkups";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, @AuthenticationPrincipal SecurityUser user, Model model) {
        CheckUp checkUp = checkUpService.findById(id);
        if (user.getRole() == UserRole.DOCTOR && !checkUp.getDoctor().getId().equals(user.getDoctorId())) {
            throw new AccessDeniedException("You can only edit your own check-ups");
        }
        CheckUpDto dto = new CheckUpDto();
        dto.setId(checkUp.getId());
        dto.setPatientId(checkUp.getPatient().getId());
        dto.setDoctorId(checkUp.getDoctor().getId());
        dto.setDate(checkUp.getDate());
        dto.setDiagnosisIds(checkUp.getDiagnoses().stream()
                .map(d -> d.getId())
                .collect(Collectors.toList()));
        dto.setTreatment(checkUp.getTreatment());
        dto.setPrice(checkUp.getPrice());
        model.addAttribute("checkUpDto", dto);
        populateFormModel(model, user);
        return "checkups/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute CheckUpDto checkUpDto,
                         BindingResult result,
                         @AuthenticationPrincipal SecurityUser user,
                         Model model) {
        if (result.hasErrors()) {
            populateFormModel(model, user);
            return "checkups/form";
        }
        Long doctorId = user.getRole() == UserRole.DOCTOR ? user.getDoctorId() : null;
        checkUpService.update(id, checkUpDto, doctorId);
        return "redirect:/checkups";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        checkUpService.delete(id);
        return "redirect:/checkups";
    }

    private void populateFormModel(Model model, SecurityUser user) {
        if (user.getRole() == UserRole.DOCTOR) {
            model.addAttribute("doctors", List.of(doctorService.findById(user.getDoctorId())));
        } else {
            model.addAttribute("doctors", doctorService.findAll());
        }
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("diagnoses", diagnosisService.findAll());
    }
}
