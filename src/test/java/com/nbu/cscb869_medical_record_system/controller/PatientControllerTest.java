package com.nbu.cscb869_medical_record_system.controller;

import com.nbu.cscb869_medical_record_system.config.SecurityConfig;
import com.nbu.cscb869_medical_record_system.controllers.web.PatientController;
import com.nbu.cscb869_medical_record_system.data.entity.AppUser;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.enums.UserRole;
import com.nbu.cscb869_medical_record_system.security.SecurityUser;
import com.nbu.cscb869_medical_record_system.security.UserDetailsServiceImpl;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import(SecurityConfig.class)
class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PatientService patientService;

    @MockitoBean
    DoctorService doctorService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        ReflectionTestUtils.setField(patient, "id", 1L);
        patient.setName("Stefan Angelov");
        patient.setEgn("9001011234");
    }

    private SecurityUser adminUser() {
        AppUser appUser = new AppUser();
        appUser.setRole(UserRole.ADMIN);
        return new SecurityUser(appUser);
    }

    private SecurityUser patientUser(Long patientId) {
        Patient p = new Patient();
        ReflectionTestUtils.setField(p, "id", patientId);
        AppUser appUser = new AppUser();
        appUser.setRole(UserRole.PATIENT);
        appUser.setPatient(p);
        return new SecurityUser(appUser);
    }

    private SecurityUser doctorUser() {
        Doctor d = new Doctor();
        ReflectionTestUtils.setField(d, "id", 1L);
        AppUser appUser = new AppUser();
        appUser.setRole(UserRole.DOCTOR);
        appUser.setDoctor(d);
        return new SecurityUser(appUser);
    }

    @Test
    void list_asAdmin_seesAllPatients() throws Exception {
        when(patientService.findAll()).thenReturn(List.of(patient));

        mockMvc.perform(get("/patients").with(user(adminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/list"))
                .andExpect(model().attributeExists("patients"));
    }

    @Test
    void list_asPatient_seesOnlySelf() throws Exception {
        when(patientService.findById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients").with(user(patientUser(1L))))
                .andExpect(status().isOk())
                .andExpect(model().attribute("patients", List.of(patient)));
    }

    @Test
    void detail_asPatient_viewingOwnProfile_returnsDetailView() throws Exception {
        when(patientService.findById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients/1").with(user(patientUser(1L))))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/details"))
                .andExpect(model().attribute("patient", patient));
    }

    @Test
    void detail_asPatient_viewingOtherProfile_redirectsToOwn() throws Exception {
        mockMvc.perform(get("/patients/99").with(user(patientUser(1L))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/1"));
    }

    @Test
    void detail_asDoctor_viewingAnyProfile_returnsDetailView() throws Exception {
        when(patientService.findById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients/1").with(user(doctorUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/details"));
    }

    @Test
    void newForm_asAdmin_returnsFormWithGPs() throws Exception {
        when(doctorService.findGeneralPractitioners()).thenReturn(List.of());

        mockMvc.perform(get("/patients/new").with(user(adminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/form"))
                .andExpect(model().attributeExists("patientDto", "generalPractitioners"));
    }

    @Test
    void create_withValidData_redirectsToPatientsList() throws Exception {
        when(patientService.save(any())).thenReturn(patient);

        mockMvc.perform(post("/patients/new").with(csrf()).with(user(adminUser()))
                        .param("name", "Stefan Angelov")
                        .param("egn", "9001011234")
                        .param("generalPractitionerId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));
    }

    @Test
    void create_withInvalidData_returnsFormWithErrors() throws Exception {
        when(doctorService.findGeneralPractitioners()).thenReturn(List.of());

        mockMvc.perform(post("/patients/new").with(csrf()).with(user(adminUser()))
                        .param("name", "")
                        .param("egn", "short"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/form"));
    }

    @Test
    void update_withValidData_redirectsToPatientsList() throws Exception {
        when(patientService.update(any(), any())).thenReturn(patient);

        mockMvc.perform(post("/patients/1/edit").with(csrf()).with(user(adminUser()))
                        .param("name", "Stefan Angelov")
                        .param("egn", "9001011234")
                        .param("generalPractitionerId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));
    }

    @Test
    void delete_redirectsToPatientsList() throws Exception {
        mockMvc.perform(post("/patients/1/delete").with(csrf()).with(user(adminUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        verify(patientService).delete(1L);
    }
}
