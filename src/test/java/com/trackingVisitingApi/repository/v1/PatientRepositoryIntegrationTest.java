package com.trackingVisitingApi.repository.v1;

import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Tag("integration")
@Import(PatientRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientRepositoryIntegrationTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    static final LocalDateTime startUTC = LocalDateTime.parse("2025-01-10T10:00");
    static final LocalDateTime endUTC = LocalDateTime.parse("2025-01-10T10:30");
    static final DoctorDto doctorDto;
    static final VisitDto visitDto;
    static final PatientDto patientDto;

    static {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");
        OffsetDateTime start = DateTimeUtil.convertWithAppendOffset(startUTC, timeZone);
        OffsetDateTime end = DateTimeUtil.convertWithAppendOffset(endUTC, timeZone);
        doctorDto = new DoctorDto(1L, "Alice", "Smith", timeZone.getID(), 1);
        visitDto = new VisitDto(1L, start, end, doctorDto);
        patientDto = new PatientDto(1L, "John", "Doe", List.of(visitDto));
    }

    @BeforeEach
    void setupDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "patients", "doctors", "visits");

        jdbcTemplate.update("INSERT INTO patients (id, first_name, last_name) VALUES (?, ?, ?)",
                patientDto.getId(), patientDto.getFirstName(), patientDto.getLastName());

        jdbcTemplate.update("INSERT INTO doctors (id, first_name, last_name, timezone) VALUES (?, ?, ?, ?)",
                doctorDto.getId(), doctorDto.getFirstName(), doctorDto.getLastName(), doctorDto.getTimezone());

        jdbcTemplate.update("INSERT INTO visits (id, patient_id, doctor_id, start_date_time, end_date_time) VALUES (?, ?, ?, ?, ?)",
                visitDto.getId(), patientDto.getId(), doctorDto.getId(), startUTC, endUTC);
    }

    @Test
    void testGetPatientDtoById() {
        Optional<PatientDto> result = patientRepository.getPatientDtoById(1L, false);
        assertThat(result).isPresent()
                .contains(patientDto);
    }

    @Test
    void testGetPatientDtoById_returnsEmptyOptional() {
        Optional<PatientDto> result = patientRepository.getPatientDtoById(2L, false);
        assertThat(result).isEmpty();
    }

}