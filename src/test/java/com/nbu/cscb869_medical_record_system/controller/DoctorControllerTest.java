package com.nbu.cscb869_medical_record_system.controller;

import com.nbu.cscb869_medical_record_system.config.SecurityConfig;
import com.nbu.cscb869_medical_record_system.controllers.web.DoctorController;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;
import com.nbu.cscb869_medical_record_system.security.UserDetailsServiceImpl;
import com.nbu.cscb869_medical_record_system.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@Import(SecurityConfig.class)
class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    DoctorService doctorService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        ReflectionTestUtils.setField(doctor, "id", 1L);
        doctor.setName("Dr. Petrov");
        doctor.setEgn("7001015678");
        doctor.setSpecialty(MedicalSpecialty.FAMILY_MEDICINE);
        doctor.setCanBeGeneralPractitioner(true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void list_asAdmin_returnsListViewWithDoctors() throws Exception {
        when(doctorService.findAll()).thenReturn(List.of(doctor));

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/list"))
                .andExpect(model().attributeExists("doctors"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void detail_returnsDetailViewWithDoctor() throws Exception {
        when(doctorService.findById(1L)).thenReturn(doctor);

        mockMvc.perform(get("/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/detail"))
                .andExpect(model().attribute("doctor", doctor));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void newForm_asAdmin_returnsFormWithSpecialties() throws Exception {
        mockMvc.perform(get("/doctors/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/form"))
                .andExpect(model().attributeExists("doctorDto", "specialties"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void newForm_asDoctor_isForbidden() throws Exception {
        mockMvc.perform(get("/doctors/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_withValidData_redirectsToDoctorsList() throws Exception {
        mockMvc.perform(post("/doctors/new").with(csrf())
                        .param("name", "Dr. Smith")
                        .param("egn", "1234567890")
                        .param("specialty", "CARDIOLOGY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));

        verify(doctorService).save(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_withInvalidData_returnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/doctors/new").with(csrf())
                        .param("name", "")
                        .param("egn", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/form"))
                .andExpect(model().attributeExists("specialties"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editForm_asAdmin_returnsFormPopulatedWithExistingData() throws Exception {
        when(doctorService.findById(1L)).thenReturn(doctor);

        mockMvc.perform(get("/doctors/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/form"))
                .andExpect(model().attributeExists("doctorDto", "specialties"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_withValidData_redirectsToDoctorsList() throws Exception {
        when(doctorService.update(eq(1L), any())).thenReturn(doctor);

        mockMvc.perform(post("/doctors/1/edit").with(csrf())
                        .param("name", "Dr. Smith")
                        .param("egn", "1234567890")
                        .param("specialty", "CARDIOLOGY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_withInvalidData_returnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/doctors/1/edit").with(csrf())
                        .param("name", "")
                        .param("egn", "bad"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_redirectsToDoctorsList() throws Exception {
        mockMvc.perform(post("/doctors/1/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));

        verify(doctorService).delete(1L);
    }
}
