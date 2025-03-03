package com.trackingVisitingApi.service.v1;

import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.repository.v1.PatientRepository;
import com.trackingVisitingApi.repository.v1.VisitRepository;
import com.trackingVisitingApi.repository.v1.crud.PatientCrudRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    PatientCrudRepository patientCrudRepository;

    @Mock
    VisitRepository visitRepository;

    @Mock
    PatientRepository patientRepository;

    @Mock
    CacheManager cacheManager;

    @InjectMocks
    PatientService patientService;

    @Test
    void shouldCreatePatientSuccessfully() {
        Patient request = new Patient(null, "John", "Doe");
        Patient expected = new Patient(1L, "John", "Doe");
        when(patientCrudRepository.save(eq(request))).thenReturn(expected);
        when(cacheManager.getCache(anyString())).thenReturn(mock(Cache.class));

        Patient actual = patientService.create(request);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
        verify(patientCrudRepository, times(1)).save(request);
    }

    @Test
    void shouldReturnPatientById() {
        Long patientId = 1L;
        Patient patient = new Patient(patientId, "John", "Doe");

        when(patientCrudRepository.findById(patientId)).thenReturn(Optional.of(patient));

        Optional<Patient> foundPatient = patientService.getById(patientId);

        assertThat(foundPatient).isPresent();
        assertThat(foundPatient.get().getId()).isEqualTo(patientId);
        verify(patientCrudRepository, times(1)).findById(patientId);
    }

    @Test
    void shouldReturnEmptyOptionalWhenPatientNotFound() {
        Long patientId = 1L;
        when(patientCrudRepository.findById(patientId)).thenReturn(Optional.empty());

        Optional<Patient> foundPatient = patientService.getById(patientId);

        assertThat(foundPatient).isEmpty();
        verify(patientCrudRepository, times(1)).findById(patientId);
    }

    @Test
    void shouldReturnPatientDtoById() {
        Long patientId = 1L;
        DoctorDto doctorDto = new DoctorDto(1L, "Dr. Adam", "Taylor", "Europe/Kiev", 3);
        PatientDto patientDto = new PatientDto(patientId, "John", "Doe", List.of(
                new VisitDto(1L, OffsetDateTime.parse("2025-02-25T15:00:00+02:00"), OffsetDateTime.parse("2025-02-25T15:30:00+02:00"), doctorDto)
        ));

        when(patientRepository.getPatientDtoById(patientId, true)).thenReturn(Optional.of(patientDto));

        Optional<PatientDto> result = patientService.getDtoById(patientId);

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
        verify(patientRepository, times(1)).getPatientDtoById(patientId, true);
    }

    @Test
    void shouldReturnEmptyOptionalWhenPatientDtoNotFound() {
        Long patientId = 1L;
        when(patientRepository.getPatientDtoById(patientId, true)).thenReturn(Optional.empty());

        Optional<PatientDto> result = patientService.getDtoById(patientId);

        assertThat(result).isEmpty();
        verify(patientRepository, times(1)).getPatientDtoById(patientId, true);
    }

    @Test
    void shouldReturnPatientsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> doctorIds = List.of(1L, 2L);
        String search = "john";
        boolean useTimeZone = false;

        DoctorDto doctorDto = new DoctorDto(1L, "Dr. Adam", "Taylor", "Europe/Kiev", 3);
        List<PatientDto> patientList = List.of(
                new PatientDto(1L, "John", "Doe", List.of(
                        new VisitDto(1L, OffsetDateTime.parse("2025-02-25T15:00:00+02:00"), OffsetDateTime.parse("2025-02-25T15:30:00+02:00"), doctorDto)
                ))
        );
        Page<PatientDto> mockPage = new PageImpl<>(patientList);

        when(visitRepository.getVisitsGroupedByPatient(eq(pageable), anyList(), anyList(), anyBoolean())).thenReturn(mockPage);

        Page<PatientDto> result = patientService.getPatientsPage(pageable, search, doctorIds, useTimeZone);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
        verify(visitRepository, times(1)).getVisitsGroupedByPatient(eq(pageable), anyList(), anyList(), anyBoolean());
    }
}