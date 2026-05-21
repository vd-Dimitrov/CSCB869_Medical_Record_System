package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.DoctorDto;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.enums.MedicalSpecialty;
import com.nbu.cscb869_medical_record_system.data.repository.DoctorRepository;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    DoctorRepository doctorRepository;
    @InjectMocks
    DoctorServiceImpl service;

    Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        ReflectionTestUtils.setField(doctor, "id", 1L);
        doctor.setName("Dr. Petrov");
        doctor.setSpecialties(List.of(MedicalSpecialty.FAMILY_MEDICINE));
        doctor.setCanBeGeneralPractitioner(true);
    }

    @Test
    void findAll_returnsAllFromRepository() {
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        assertThat(service.findAll()).containsExactly(doctor);
        verify(doctorRepository).findAll();
    }

    @Test
    void findById_existingId_returnsDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        assertThat(service.findById(1L)).isSameAs(doctor);
    }

    @Test
    void findById_missingId_throwsResourceNotFoundException() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findGeneralPractitioners_delegatesToRepository() {
        when(doctorRepository.findByCanBeGeneralPractitionerTrue()).thenReturn(List.of(doctor));

        assertThat(service.findGeneralPractitioners()).containsExactly(doctor);
        verify(doctorRepository).findByCanBeGeneralPractitionerTrue();
    }

    @Test
    void save_returnsNullWithoutPersisting() {
        DoctorDto dto = new DoctorDto();
        dto.setName("Dr. Smith");
        dto.setEgn("1234567890");
        dto.setSpecialties(List.of(MedicalSpecialty.CARDIOLOGY));

        Doctor result = service.save(dto);

        assertThat(result).isNull();
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void update_mapsOntoExistingDoctorAndPersists() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        DoctorDto dto = new DoctorDto();
        dto.setName("Dr. Updated");
        dto.setEgn("9876543210");
        dto.setSpecialties(List.of(MedicalSpecialty.CARDIOLOGY));
        dto.setCanBeGeneralPractitioner(false);

        Doctor result = service.update(1L, dto);

        assertThat(result).isSameAs(doctor);
        assertThat(doctor.getName()).isEqualTo("Dr. Updated");
        assertThat(doctor.getSpecialties()).containsExactly(MedicalSpecialty.CARDIOLOGY);
        verify(doctorRepository).save(doctor);
    }

    @Test
    void update_missingId_throwsResourceNotFoundException() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());
        DoctorDto dto = new DoctorDto();
        dto.setSpecialties(List.of(MedicalSpecialty.NEUROLOGY));

        assertThatThrownBy(() -> service.update(99L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesFromRepository() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        service.delete(1L);

        verify(doctorRepository).deleteById(1L);
    }

    @Test
    void delete_missingId_throwsWithoutCallingDelete() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(doctorRepository, never()).deleteById(any());
    }
}
