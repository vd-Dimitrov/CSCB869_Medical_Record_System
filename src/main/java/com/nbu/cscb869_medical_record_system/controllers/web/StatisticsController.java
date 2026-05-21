package com.nbu.cscb869_medical_record_system.controllers.web;

import com.nbu.cscb869_medical_record_system.service.DoctorService;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import com.nbu.cscb869_medical_record_system.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {


    private final StatisticsService statisticsService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping
    public String index(Model model) {
        populateIndex(model);
        return "statistics/index";
    }

    @GetMapping("/by-gp")
    public String byGP(@RequestParam Long doctorId, Model model) {
        model.addAttribute("gpPatients", statisticsService.getPatientsByGP(doctorId));
        model.addAttribute("gpDoctor", doctorService.findById(doctorId));
        populateIndex(model);
        return "statistics/index";
    }

    @GetMapping("/by-doctor-period")
    public String byDoctorAndPeriod(@RequestParam Long doctorId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                    Model model) {
        model.addAttribute("periodCheckUps", statisticsService.getCheckUpsByDoctorAndPeriod(doctorId, from, to));
        model.addAttribute("periodDoctor", doctorService.findById(doctorId));
        model.addAttribute("periodFrom", from);
        model.addAttribute("periodTo", to);
        populateIndex(model);
        return "statistics/index";
    }

    @GetMapping("/patient-history")
    public String patientHistory(@RequestParam Long patientId, Model model) {
        model.addAttribute("visitHistory", statisticsService.getPatientVisitHistory(patientId));
        model.addAttribute("historyPatient", patientService.findById(patientId));
        populateIndex(model);
        return "statistics/index";
    }

    private void populateIndex(Model model) {
        model.addAttribute("mostCommonDiagnosis", statisticsService.getMostCommonDiagnosis());
        model.addAttribute("totalPatientPaid", statisticsService.getTotalPatientPaidAmount());
        model.addAttribute("patientPaidByDoctor", statisticsService.getPatientPaidAmountByDoctor());
        model.addAttribute("patientCountByGP", statisticsService.getPatientCountByGP());
        model.addAttribute("visitCountByDoctor", statisticsService.getVisitCountByDoctor());
        model.addAttribute("monthWithMostSickLeaves", statisticsService.getMonthWithMostSickLeaves());
        model.addAttribute("doctorsWithMostSickLeaves", statisticsService.getDoctorsWithMostSickLeaves());
        model.addAttribute("patientsByDiagnosis", statisticsService.getPatientsByAllDiagnoses());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("patients", patientService.findAll());
    }
}

