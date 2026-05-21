package com.nbu.cscb869_medical_record_system.service;

import com.nbu.cscb869_medical_record_system.data.dto.CheckUpDto;
import com.nbu.cscb869_medical_record_system.data.entity.CheckUp;
import com.nbu.cscb869_medical_record_system.data.entity.Doctor;
import com.nbu.cscb869_medical_record_system.data.repository.CheckUpRepository;
import com.nbu.cscb869_medical_record_system.exceptions.AccessDeniedException;
import com.nbu.cscb869_medical_record_system.exceptions.ResourceNotFoundException;
import com.nbu.cscb869_medical_record_system.helpers.EntityMapper;
import com.nbu.cscb869_medical_record_system.service.impl.CheckUpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckUpServiceImplTest {

    @Mock
    CheckUpRepository checkUpRepository;
    @Mock
    EntityMapper entityMapper;
    @InjectMocks
    CheckUpServiceImpl service;

    CheckUp checkUp;
    Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        ReflectionTestUtils.setField(doctor, "id", 1L);

        checkUp = new CheckUp();
        ReflectionTestUtils.setField(checkUp, "id", 1L);
        checkUp.setDoctor(doctor);
        checkUp.setDate(LocalDate.now());
    }

    @Test
    void findAll_returnsAllFromRepository() {
        when(checkUpRepository.findAll()).thenReturn(List.of(checkUp));

        assertThat(service.findAll()).containsExactly(checkUp);
        verify(checkUpRepository).findAll();
    }

    @Test
    void findById_existingId_returnsCheckUp() {
        when(checkUpRepository.findById(1L)).thenReturn(Optional.of(checkUp));

        assertThat(service.findById(1L)).isSameAs(checkUp);
    }

    @Test
    void findById_missingId_throwsResourceNotFoundException() {
        when(checkUpRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findByPatient_delegatesToRepository() {
        when(checkUpRepository.findByPatientId(5L)).thenReturn(List.of(checkUp));

        assertThat(service.findByPatient(5L)).containsExactly(checkUp);
        verify(checkUpRepository).findByPatientId(5L);
    }

    @Test
    void findByDoctor_delegatesToRepository() {
        when(checkUpRepository.findByDoctorId(1L)).thenReturn(List.of(checkUp));

        assertThat(service.findByDoctor(1L)).containsExactly(checkUp);
        verify(checkUpRepository).findByDoctorId(1L);
    }

    @Test
    void save_mapsNewEntityAndPersists() {
        CheckUpDto dto = new CheckUpDto();
        when(checkUpRepository.save(any(CheckUp.class))).thenAnswer(inv -> inv.getArgument(0));

        service.save(dto);

        verify(entityMapper).map(eq(dto), any(CheckUp.class));
        verify(checkUpRepository).save(any(CheckUp.class));
    }

    @Test
    void update_withNullDoctorId_adminBypass_succeeds() {
        when(checkUpRepository.findById(1L)).thenReturn(Optional.of(checkUp));
        when(checkUpRepository.save(checkUp)).thenReturn(checkUp);
        CheckUpDto dto = new CheckUpDto();

        CheckUp result = service.update(1L, dto, null);

        assertThat(result).isSameAs(checkUp);
        verify(entityMapper).map(dto, checkUp);
        verify(checkUpRepository).save(checkUp);
    }

    @Test
    void update_withMatchingDoctorId_ownerSucceeds() {
        when(checkUpRepository.findById(1L)).thenReturn(Optional.of(checkUp));
        when(checkUpRepository.save(checkUp)).thenReturn(checkUp);

        service.update(1L, new CheckUpDto(), 1L);

        verify(checkUpRepository).save(checkUp);
    }

    @Test
    void update_withDifferentDoctorId_throwsAccessDenied() {
        when(checkUpRepository.findById(1L)).thenReturn(Optional.of(checkUp));

        assertThatThrownBy(() -> service.update(1L, new CheckUpDto(), 99L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("own check-ups");
    }

    @Test
    void update_missingCheckUp_throwsResourceNotFoundException() {
        when(checkUpRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new CheckUpDto(), null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesFromRepository() {
        when(checkUpRepository.findById(1L)).thenReturn(Optional.of(checkUp));

        service.delete(1L);

        verify(checkUpRepository).deleteById(1L);
    }

    @Test
    void delete_missingId_throwsWithoutCallingDelete() {
        when(checkUpRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(checkUpRepository, never()).deleteById(any());
    }
}