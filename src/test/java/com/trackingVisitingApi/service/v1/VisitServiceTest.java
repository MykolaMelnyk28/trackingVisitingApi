package com.trackingVisitingApi.service.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.entity.v1.Visit;
import com.trackingVisitingApi.exception.ResourceConflictException;
import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.repository.v1.VisitRepository;
import com.trackingVisitingApi.repository.v1.crud.VisitCrudRepository;
import com.trackingVisitingApi.util.DateTimeUtil;
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
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    VisitCrudRepository visitCrudRepository;

    @Mock
    VisitRepository visitRepository;

    @Mock
    DoctorService doctorService;

    @Mock
    CacheManager cacheManager;

    @InjectMocks
    VisitService visitService;

    @Test
    void shouldCreateVisitSuccessfully() {
        var patientReference = AggregateReference.<Patient, Long>to(1L);
        var doctorReference = AggregateReference.<Doctor, Long>to(2L);

        LocalDateTime startUTC = LocalDateTime.parse("2025-03-05T11:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-03-05T11:30");

        Visit visit = new Visit(1L, patientReference, doctorReference, startUTC, endUTC);

        when(visitRepository.countVisitsBetweenStartAndEnd(eq(startUTC), eq(endUTC))).thenReturn(0);
        when(visitCrudRepository.save(any(Visit.class))).thenReturn(visit);
        when(cacheManager.getCache(anyString())).thenReturn(mock(Cache.class));

        Visit actual = visitService.create(visit);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getPatient()).isEqualTo(patientReference);
        assertThat(actual.getDoctor()).isEqualTo(doctorReference);

        assertThat(actual.getStartDateTime()).isEqualTo(startUTC);
        assertThat(actual.getEndDateTime()).isEqualTo(endUTC);

        verify(visitRepository, times(1)).countVisitsBetweenStartAndEnd(eq(startUTC), eq(endUTC));
        verify(visitCrudRepository, times(1)).save(any(Visit.class));
    }

    @Test
    void shouldThrowExceptionWhenVisitOverlaps() {
        LocalDateTime startUTC = LocalDateTime.parse("2025-03-05T13:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-03-05T13:30");

        Visit visit = new Visit(null, AggregateReference.to(1L), AggregateReference.to(2L), startUTC, endUTC);

        when(visitRepository.countVisitsBetweenStartAndEnd(any(), any())).thenReturn(1);

        assertThrows(ResourceConflictException.class, () -> visitService.create(visit));

        verify(visitRepository, times(1)).countVisitsBetweenStartAndEnd(any(), any());
        verifyNoInteractions(visitCrudRepository);
    }

    @Test
    void shouldReturnVisitById() {
        Long visitId = 1L;

        var patientReference = AggregateReference.<Patient, Long>to(1L);
        var doctorReference = AggregateReference.<Doctor, Long>to(2L);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Visit visit = new Visit(visitId, patientReference, doctorReference, start, end);

        when(visitCrudRepository.findById(visitId)).thenReturn(Optional.of(visit));

        Optional<Visit> foundVisit = visitService.getById(visitId);

        assertThat(foundVisit)
                .isNotNull()
                .isPresent()
                .contains(visit);
        verify(visitCrudRepository, times(1)).findById(visitId);
    }

    @Test
    void shouldReturnEmptyOptionalWhenVisitNotFound() {
        Long visitId = 1L;
        when(visitCrudRepository.findById(visitId)).thenReturn(Optional.empty());

        Optional<Visit> foundVisit = visitService.getById(visitId);

        assertThat(foundVisit)
                .isNotNull()
                .isEmpty();
        verify(visitCrudRepository, times(1)).findById(visitId);
    }

    @Test
    void shouldReturnVisitsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> patientIds = List.of(1L, 2L);
        List<Long> doctorIds = List.of(3L, 4L);
        boolean useTimeZone = false;

        TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");
        DoctorDto doctorDto = new DoctorDto(1L, "Dr. Adam", "Taylor", timeZone.getID(), 2);

        LocalDateTime startUTC = LocalDateTime.parse("2025-03-05T11:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-03-05T11:30");

        OffsetDateTime start = DateTimeUtil.convertWithAppendOffset(startUTC, timeZone);
        OffsetDateTime end = DateTimeUtil.convertWithAppendOffset(endUTC, timeZone);

        Page<VisitDto> mockPage = new PageImpl<>(List.of(new VisitDto(1L, start, end, doctorDto)));
        when(visitRepository.getVisits(pageable, patientIds, doctorIds, useTimeZone)).thenReturn(mockPage);

        Page<VisitDto> result = visitService.getVisitsPage(pageable, patientIds, doctorIds, useTimeZone);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(visitRepository, times(1)).getVisits(pageable, patientIds, doctorIds, useTimeZone);
    }

    @Test
    void testToDto() {
        var patientReference = AggregateReference.<Patient, Long>to(1L);
        var doctorReference = AggregateReference.<Doctor, Long>to(2L);

        LocalDateTime startUTC = LocalDateTime.parse("2025-03-05T11:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-03-05T11:30");

        TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");
        Doctor doctor = new Doctor(1L, "Adam", "Jonson", timeZone.getID(), 1);
        Visit visit = new Visit(1L, patientReference, doctorReference, startUTC, endUTC);

        DoctorDto doctorDto = new DoctorDto(1L, doctor.getFirstName(), doctor.getLastName(), doctor.getTimezone(), doctor.getTotalPatients());
        VisitDto expected = new VisitDto(
                1L,
                DateTimeUtil.convertWithAppendOffset(startUTC, timeZone),
                DateTimeUtil.convertWithAppendOffset(endUTC, timeZone),
                doctorDto
        );

        when(doctorService.getById(anyLong())).thenReturn(Optional.of(doctor));

        VisitDto actual = visitService.toDto(visit);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

}
