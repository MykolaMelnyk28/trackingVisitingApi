package com.trackingVisitingApi.repository.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.entity.v1.Visit;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Tag("integration")
@Import(VisitRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VisitRepositoryIntegrationTest {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "visits", "doctors", "patients");

        TimeZone europeKiev = TimeZone.getTimeZone("Europe/Kiev");
        TimeZone asiaSingapore = TimeZone.getTimeZone("Asia/Singapore");

        List<Doctor> doctors = List.of(
                new Doctor(1L, "John", "Doe", europeKiev.getID(), 100),
                new Doctor(2L, "Jane", "Roe", asiaSingapore.getID(), 200)
        );
        List<Patient> patients = List.of(
                new Patient(1L, "Alice", "Smith"),
                new Patient(2L, "Bob", "Johnson")
        );
        List<Visit> visits = List.of(
                new Visit(
                        1L,
                        AggregateReference.to(1L),
                        AggregateReference.to(1L),
                        LocalDateTime.parse("2025-03-01T10:00:00"),
                        LocalDateTime.parse("2025-03-01T11:00:00")
                ),
                new Visit(
                        2L,
                        AggregateReference.to(2L),
                        AggregateReference.to(2L),
                        LocalDateTime.parse("2025-03-02T12:00:00"),
                        LocalDateTime.parse("2025-03-02T13:00:00")
                )
        );

        String insertDoctor = "INSERT INTO doctors (id, first_name, last_name, timezone, total_patients) VALUES (?, ?, ?, ?, ?)";
        String insertPatient = "INSERT INTO patients (id, first_name, last_name) VALUES (?, ?, ?)";
        String insertVisit = "INSERT INTO visits (id, start_date_time, end_date_time, patient_id, doctor_id) VALUES (?, ?, ?, ?, ?)";

        doctors.forEach(x ->
                jdbcTemplate.update(insertDoctor, x.getId(), x.getFirstName(), x.getLastName(), x.getTimezone() ,x.getTotalPatients())
        );
        patients.forEach(x ->
                jdbcTemplate.update(insertPatient, x.getId(), x.getFirstName(), x.getLastName())
        );

        visits.forEach(x ->
                jdbcTemplate.update(insertVisit, x.getId(), x.getStartDateTime(), x.getEndDateTime(), x.getPatient().getId(), x.getDoctor().getId())
        );
    }

    @Test
    public void testGetVisitsWithoutFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitDto> result = visitRepository.getVisits(pageable, Collections.emptyList(), Collections.emptyList(), false);

        assertThat(result).isNotNull();
        assertEquals(2, result.getTotalElements());

        VisitDto visit = result.getContent().get(0);
        assertNotNull(visit.getDoctor());
        assertNotNull(visit.getStart());
        assertNotNull(visit.getEnd());
    }

    @Test
    public void testGetVisitsWithFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitDto> result = visitRepository.getVisits(pageable, List.of(1L), List.of(), false);
        assertThat(result).isNotNull();
        assertEquals(1, result.getTotalElements());
        VisitDto visit = result.getContent().get(0);
        assertEquals(1L, visit.getId());
    }

    @Test
    public void testGetVisitsGroupedByPatient() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Predicate<PatientDto>> filters = Arrays.asList(patient -> true);
        Page<PatientDto> result = visitRepository.getVisitsGroupedByPatient(pageable, filters, Collections.emptyList(), false);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        Optional<PatientDto> patientOpt = result.getContent().stream().filter(p -> p.getId().equals(1L)).findFirst();
        assertTrue(patientOpt.isPresent());
        PatientDto patient = patientOpt.get();
        assertNotNull(patient.getLastVisits());
        assertEquals(1, patient.getLastVisits().size());
        assertEquals(1L, patient.getLastVisits().get(0).getId());
    }

    @Test
    public void testCountVisitsBetweenStartAndEnd_returnsOne() {
        LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 3, 1, 12, 0);
        int count = visitRepository.countVisitsBetweenStartAndEnd(start, end);
        assertEquals(1, count);
    }

    @Test
    public void testCountVisitsBetweenStartAndEnd_returnsZero() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);
        int count = visitRepository.countVisitsBetweenStartAndEnd(start, end);
        assertEquals(0, count);
    }
}