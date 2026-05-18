package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.data.dto.UserDto;
import com.nbu.cscb869_medical_record_system.data.entity.AppUser;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import com.nbu.cscb869_medical_record_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @GetMapping
    public String list(Model model){
        model.addAttribute("users",  userService.findAll());
        return "admin/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        populateFormModel(model);
        return "admin/form";
    }


    @PostMapping("/new")
    public String create(@Valid @ModelAttribute UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            populateFormModel(model);
            return "admin/form";
        }
        userService.save(userDto);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        AppUser user = userService.findById(id);
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        if (user.getDoctor() != null) dto.setDoctorId(user.getDoctor().getId());
        if (user.getPatient() != null) dto.setPatientId(user.getPatient().getId());
        model.addAttribute("userDto", dto);
        populateFormModel(model);
        return "admin/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute UserDto userDto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            populateFormModel(model);
            return "admin/form";
        }
        userService.update(id, userDto);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("patients", patientService.findAll());
    }
}
