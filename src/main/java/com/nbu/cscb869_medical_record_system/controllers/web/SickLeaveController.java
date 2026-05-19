package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.data.dto.SickLeaveDto;
import com.nbu.cscb869_medical_record_system.data.entity.SickLeave;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import com.nbu.cscb869_medical_record_system.service.SickLeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sickleaves")
public class SickLeaveController {

    private final SickLeaveService sickLeaveService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping
    public String list(@AuthenticationPrincipal SecurityUser securityUser, Model model){
        List<SickLeave> sickLeaves;
        if (securityUser.getRole() == UserRole.PATIENT){
            sickLeaves = sickLeaveService.findByPatient(securityUser.getPatientId());
        }else {
            sickLeaves = sickLeaveService.findAll();
        }
        model.addAttribute("sickLeaves", sickLeaves);
        model.addAttribute("currentDoctorId", securityUser.getDoctorId());
        return "sickleaves/list";
    }

    @GetMapping("/new")
    public String newForm(@AuthenticationPrincipal SecurityUser user, Model model) {
        SickLeaveDto dto = new SickLeaveDto();
        if (user.getRole() == UserRole.DOCTOR) {
            dto.setDoctorId(user.getDoctorId());
        }
        model.addAttribute("sickLeaveDto", dto);
        populateFormModel(model, user);
        return "sickleaves/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute SickLeaveDto sickLeaveDto,
                         BindingResult result,
                         @AuthenticationPrincipal SecurityUser user,
                         Model model) {
        if (result.hasErrors()) {
            populateFormModel(model, user);
            return "sickleaves/form";
        }
        sickLeaveService.save(sickLeaveDto);
        return "redirect:/sickleaves";
    }


    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, @AuthenticationPrincipal SecurityUser user, Model model) {
        SickLeave sickLeave = sickLeaveService.findById(id);
        if (user.getRole() == UserRole.DOCTOR && !sickLeave.getDoctor().getId().equals(user.getDoctorId())) {
            throw new AccessDeniedException("You can only edit your own sick leaves");
        }
        SickLeaveDto dto = new SickLeaveDto();
        dto.setId(sickLeave.getId());
        dto.setPatientId(sickLeave.getPatient().getId());
        dto.setDoctorId(sickLeave.getDoctor().getId());
        dto.setStartDate(sickLeave.getStartDate());
        dto.setDuration(sickLeave.getDurationDays());
        model.addAttribute("sickLeaveDto", dto);
        populateFormModel(model, user);
        return "sickleaves/form";
    }


    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute SickLeaveDto sickLeaveDto,
                         BindingResult result,
                         @AuthenticationPrincipal SecurityUser user,
                         Model model) {
        if (result.hasErrors()) {
            populateFormModel(model, user);
            return "sickleaves/form";
        }
        Long doctorId = user.getRole() == UserRole.DOCTOR ? user.getDoctorId() : null;
        sickLeaveService.update(id, sickLeaveDto, doctorId);
        return "redirect:/sickleaves";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        sickLeaveService.delete(id);
        return "redirect:/sickleaves";
    }
    private void populateFormModel(Model model, SecurityUser user) {
        model.addAttribute("patients", patientService.findAll());
        if (user.getRole() == UserRole.DOCTOR) {
            model.addAttribute("doctors", List.of(doctorService.findById(user.getDoctorId())));
        } else {
            model.addAttribute("doctors", doctorService.findAll());
        }
    }
}
