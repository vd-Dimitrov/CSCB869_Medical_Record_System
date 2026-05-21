package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.PatientDto;
import com.nbu.cscb869_medical_record_system.data.entity.Patient;
import com.nbu.cscb869_medical_record_system.data.repository.PatientRepository;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.helpers.EntityMapper;
import com.nbu.cscb869_medical_record_system.service.impl.PatientServiceImpl;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    PatientRepository patientRepository;
    @Mock
    EntityMapper entityMapper;
    @InjectMocks
    PatientServiceImpl service;

    Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        ReflectionTestUtils.setField(patient, "id", 1L);
        patient.setName("Stefan Angelov");
    }

    @Test
    void findAll_returnsAllFromRepository() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        assertThat(service.findAll()).containsExactly(patient);
        verify(patientRepository).findAll();
    }

    @Test
    void findById_existingId_returnsPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        assertThat(service.findById(1L)).isSameAs(patient);
    }

    @Test
    void findById_missingId_throwsResourceNotFoundException() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findByGeneralPractitioner_delegatesToRepository() {
        when(patientRepository.findByGeneralPractitionerId(2L)).thenReturn(List.of(patient));

        assertThat(service.findByGeneralPractitioner(2L)).containsExactly(patient);
        verify(patientRepository).findByGeneralPractitionerId(2L);
    }

    @Test
    void save_mapsNewEntityAndPersists() {
        PatientDto dto = new PatientDto();
        when(patientRepository.save(any(Patient.class))).thenAnswer(inv -> inv.getArgument(0));

        service.save(dto);

        verify(entityMapper).map(eq(dto), any(Patient.class));
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void update_mapsOntoExistingPatientAndPersists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        PatientDto dto = new PatientDto();

        Patient result = service.update(1L, dto);

        assertThat(result).isSameAs(patient);
        verify(entityMapper).map(dto, patient);
        verify(patientRepository).save(patient);
    }

    @Test
    void update_missingId_throwsResourceNotFoundException() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new PatientDto()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesFromRepository() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        service.delete(1L);

        verify(patientRepository).deleteById(1L);
    }

    @Test
    void delete_missingId_throwsWithoutCallingDelete() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(patientRepository, never()).deleteById(any());
    }
}
