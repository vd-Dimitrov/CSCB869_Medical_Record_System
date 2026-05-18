package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminController {
    private final UserService userService;

    @GetMapping
    public String list(Model model){
        model.addAttribute("users",  userService.findAll());
        return "admin/list";
    }
}
