package com.nbu.cscb869_medical_record_system.controller;

import com.nbu.cscb869_medical_record_system.config.SecurityConfig;
import com.nbu.cscb869_medical_record_system.controllers.web.CheckUpController;
import com.nbu.cscb869_medical_record_system.data.entity.*;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.security.UserDetailsServiceImpl;
import com.nbu.cscb869_medical_record_system.service.CheckUpService;
import com.nbu.cscb869_medical_record_system.service.DiagnosisService;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import com.nbu.cscb869_medical_record_system.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CheckUpController.class)
@Import(SecurityConfig.class)
class CheckUpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CheckUpService checkUpService;

    @MockitoBean
    DoctorService doctorService;

    @MockitoBean
    PatientService patientService;

    @MockitoBean
    DiagnosisService diagnosisService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    CheckUp checkUp;
    Doctor doctor;
    Patient patient;
    Diagnosis diagnosis;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        ReflectionTestUtils.setField(doctor, "id", 1L);
        doctor.setName("Dr. Petrov");

        patient = new Patient();
        ReflectionTestUtils.setField(patient, "id", 1L);

        diagnosis = new Diagnosis();
        ReflectionTestUtils.setField(diagnosis, "id", 1L);

        checkUp = new CheckUp();
        ReflectionTestUtils.setField(checkUp, "id", 1L);
        checkUp.setDoctor(doctor);
        checkUp.setPatient(patient);
        checkUp.setDiagnoses(List.of(diagnosis));
        checkUp.setDate(LocalDate.now());
        checkUp.setPrice(BigDecimal.valueOf(25));
    }

    private SecurityUser adminUser() {
        AppUser appUser = new AppUser();
        appUser.setRole(UserRole.ADMIN);
        return new SecurityUser(appUser);
    }

    private SecurityUser doctorUser(Long doctorId) {
        Doctor d = new Doctor();
        ReflectionTestUtils.setField(d, "id", doctorId);
        AppUser appUser = new AppUser();
        appUser.setRole(UserRole.DOCTOR);
        appUser.setDoctor(d);
        return new SecurityUser(appUser);
    }

    private SecurityUser patientUser() {
        Patient p = new Patient();
        ReflectionTestUtils.setField(p, "id", 1L);
        AppUser appUser = new AppUser();
        appUser.setRole(UserRole.PATIENT);
        appUser.setPatient(p);
        return new SecurityUser(appUser);
    }

    @Test
    void list_asAdmin_seesAllCheckUps() throws Exception {
        when(checkUpService.findAll()).thenReturn(List.of(checkUp));

        mockMvc.perform(get("/checkups").with(user(adminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("checkups/list"))
                .andExpect(model().attributeExists("checkUps"));
    }

    @Test
    void list_asDoctor_seesAllCheckUps() throws Exception {
        when(checkUpService.findAll()).thenReturn(List.of(checkUp));

        mockMvc.perform(get("/checkups").with(user(doctorUser(1L))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("checkUps"));
    }

    @Test
    void list_asPatient_seesOnlyOwnCheckUps() throws Exception {
        when(checkUpService.findByPatient(1L)).thenReturn(List.of(checkUp));

        mockMvc.perform(get("/checkups").with(user(patientUser())))
                .andExpect(status().isOk())
                .andExpect(model().attribute("checkUps", List.of(checkUp)));
    }

    @Test
    void detail_returnsDetailView() throws Exception {
        when(checkUpService.findById(1L)).thenReturn(checkUp);

        mockMvc.perform(get("/checkups/1").with(user(adminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("checkups/detail"))
                .andExpect(model().attribute("checkUp", checkUp));
    }

    @Test
    void newForm_asDoctor_prePopulatesDoctorId() throws Exception {
        when(doctorService.findById(1L)).thenReturn(doctor);
        when(patientService.findAll()).thenReturn(List.of());
        when(diagnosisService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/checkups/new").with(user(doctorUser(1L))))
                .andExpect(status().isOk())
                .andExpect(view().name("checkups/form"))
                .andExpect(model().attributeExists("checkUpDto", "doctors", "patients", "diagnoses"));
    }

    @Test
    void newForm_asAdmin_showsAllDoctors() throws Exception {
        when(doctorService.findAll()).thenReturn(List.of(doctor));
        when(patientService.findAll()).thenReturn(List.of());
        when(diagnosisService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/checkups/new").with(user(adminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("checkups/form"));
    }

    @Test
    void editForm_asAdmin_returnsForm() throws Exception {
        when(checkUpService.findById(1L)).thenReturn(checkUp);
        when(doctorService.findAll()).thenReturn(List.of(doctor));
        when(patientService.findAll()).thenReturn(List.of());
        when(diagnosisService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/checkups/1/edit").with(user(adminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("checkups/form"));
    }

    @Test
    void editForm_asOwnerDoctor_returnsForm() throws Exception {
        when(checkUpService.findById(1L)).thenReturn(checkUp);
        when(doctorService.findById(1L)).thenReturn(doctor);
        when(patientService.findAll()).thenReturn(List.of());
        when(diagnosisService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/checkups/1/edit").with(user(doctorUser(1L))))
                .andExpect(status().isOk())
                .andExpect(view().name("checkups/form"));
    }

    @Test
    void editForm_asWrongDoctor_isForbidden() throws Exception {
        when(checkUpService.findById(1L)).thenReturn(checkUp);

        mockMvc.perform(get("/checkups/1/edit").with(user(doctorUser(99L))))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/checkups/1/delete").with(csrf()).with(user(adminUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/checkups"));

        verify(checkUpService).delete(1L);
    }
}
