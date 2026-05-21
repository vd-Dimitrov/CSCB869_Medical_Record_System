package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.data.entity.SickLeave;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.service.SickLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sickleaves")
public class SickLeaveController {

    private final SickLeaveService sickLeaveService;

    @GetMapping
    public String list(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        List<SickLeave> sickLeaves;
        if (securityUser.getRole() == UserRole.PATIENT) {
            sickLeaves = sickLeaveService.findByPatient(securityUser.getPatientId());
        } else {
            sickLeaves = sickLeaveService.findAll();
        }
        model.addAttribute("sickLeaves", sickLeaves);
        return "sickleaves/list";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        sickLeaveService.delete(id);
        return "redirect:/sickleaves";
    }
}
