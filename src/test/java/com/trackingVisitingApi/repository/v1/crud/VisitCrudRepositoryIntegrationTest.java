package com.trackingVisitingApi.repository.v1.crud;

import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.entity.v1.Visit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Tag("integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VisitCrudRepositoryIntegrationTest {

    @Autowired
    VisitCrudRepository visitCrudRepository;

    @Autowired
    PatientCrudRepository patientCrudRepository;

    @Autowired
    DoctorCrudRepository doctorCrudRepository;

    @BeforeEach
    void setUp() {
        visitCrudRepository.deleteAll();
        patientCrudRepository.deleteAll();
        doctorCrudRepository.deleteAll();
    }

    @Test
    void testSaveAndFindVisit() {
        Patient patient = new Patient(null, "John", "Doe");
        Doctor doctor = new Doctor(null, "Dr. Smith", "Jones", "UTC", 0);
        patientCrudRepository.save(patient);
        doctorCrudRepository.save(doctor);

        LocalDateTime startUTC = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-03-02T10:00");

        Visit visit = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC, endUTC);

        Visit savedVisit = visitCrudRepository.save(visit);
        
        Optional<Visit> foundVisit = visitCrudRepository.findById(savedVisit.getId());

        assertThat(foundVisit)
                .isPresent()
                .contains(savedVisit);
    }

    @Test
    void testDeleteVisit() {
        Patient patient = new Patient(null, "Alice", "Smith");
        Doctor doctor = new Doctor(null, "Dr. Brown", "White", "UTC", 0);
        patient = patientCrudRepository.save(patient);
        doctor = doctorCrudRepository.save(doctor);

        LocalDateTime startUTC = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-03-02T10:00");

        Visit visit = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC, endUTC);
        Visit savedVisit = visitCrudRepository.save(visit);

        visitCrudRepository.deleteById(savedVisit.getId());

        Optional<Visit> deletedVisit = visitCrudRepository.findById(savedVisit.getId());
        assertThat(deletedVisit).isEmpty();
    }

    @Test
    void testPagingAndSorting() {
        Patient patient1 = new Patient(null, "John", "Doe");
        Patient patient2 = new Patient(null, "Alice", "Smith");
        Patient patient3 = new Patient(null, "Bob", "Brown");
        patientCrudRepository.saveAll(List.of(patient1, patient2, patient3));

        Doctor doctor = new Doctor(null, "Dr. White", "Johnson", "UTC", 0);
        doctorCrudRepository.save(doctor);

        LocalDateTime startUTC1 = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC1 = LocalDateTime.parse("2025-03-02T10:00");

        LocalDateTime startUTC2 = LocalDateTime.parse("2025-03-02T10:00");
        LocalDateTime endUTC2 = LocalDateTime.parse("2025-03-02T11:00");

        LocalDateTime startUTC3 = LocalDateTime.parse("2025-03-02T11:00");
        LocalDateTime endUTC3 = LocalDateTime.parse("2025-03-02T12:00");

        Visit visit1 = new Visit(null, AggregateReference.to(patient1.getId()), AggregateReference.to(doctor.getId()),
                startUTC1, endUTC1);
        Visit visit2 = new Visit(null, AggregateReference.to(patient2.getId()), AggregateReference.to(doctor.getId()),
                startUTC2, endUTC2);
        Visit visit3 = new Visit(null, AggregateReference.to(patient3.getId()), AggregateReference.to(doctor.getId()),
                startUTC3, endUTC3);

        visitCrudRepository.saveAll(List.of(visit1, visit2, visit3));

        Pageable pageable = PageRequest.of(0, 2, Sort.by("startDateTime").ascending());
        Page<Visit> page = visitCrudRepository.findAll(pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0)).isEqualTo(visit1);
        assertThat(page.getContent().get(1)).isEqualTo(visit2);
    }

    @Test
    void testSaveAllVisits() {
        Patient patient = new Patient(null, "John", "Doe");
        Doctor doctor = new Doctor(null, "Dr. White", "Johnson", "UTC", 0);
        patientCrudRepository.save(patient);
        doctorCrudRepository.save(doctor);

        LocalDateTime startUTC1 = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC1 = LocalDateTime.parse("2025-03-02T10:00");

        LocalDateTime startUTC2 = LocalDateTime.parse("2025-03-02T10:00");
        LocalDateTime endUTC2 = LocalDateTime.parse("2025-03-02T11:00");

        Visit visit1 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC1, endUTC1);
        Visit visit2 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC2, endUTC2);

        Iterable<Visit> savedVisits = visitCrudRepository.saveAll(List.of(visit1, visit2));

        assertThat(savedVisits).hasSize(2);
    }

    @Test
    void testExistsById() {
        Patient patient = new Patient(null, "Charlie", "Brown");
        Doctor doctor = new Doctor(null, "Dr. Green", "Lee", "UTC", 0);
        patientCrudRepository.save(patient);
        doctorCrudRepository.save(doctor);

        LocalDateTime startUTC = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-03-02T10:00");

        Visit visit = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC, startUTC);
        Visit savedVisit = visitCrudRepository.save(visit);

        boolean exists = visitCrudRepository.existsById(savedVisit.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void testCountVisits() {
        Patient patient = new Patient(null, "David", "Harris");
        Doctor doctor = new Doctor(null, "Dr. Blue", "White", "UTC", 0);
        patientCrudRepository.save(patient);
        doctorCrudRepository.save(doctor);

        LocalDateTime startUTC1 = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC1 = LocalDateTime.parse("2025-03-02T10:00");

        LocalDateTime startUTC2 = LocalDateTime.parse("2025-03-02T10:00");
        LocalDateTime endUTC2 = LocalDateTime.parse("2025-03-02T11:00");

        Visit visit1 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC1, endUTC1);
        Visit visit2 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC2, endUTC2);

        visitCrudRepository.saveAll(List.of(visit1, visit2));

        long count = visitCrudRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void testDeleteAllVisits() {
        Patient patient = new Patient(null, "Emily", "White");
        Doctor doctor = new Doctor(null, "Dr. Black", "Green", "UTC", 0);
        patientCrudRepository.save(patient);
        doctorCrudRepository.save(doctor);

        LocalDateTime startUTC1 = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC1 = LocalDateTime.parse("2025-03-02T10:00");

        LocalDateTime startUTC2 = LocalDateTime.parse("2025-03-02T10:00");
        LocalDateTime endUTC2 = LocalDateTime.parse("2025-03-02T11:00");

        Visit visit1 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC1, endUTC1);
        Visit visit2 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC2, endUTC2);

        visitCrudRepository.saveAll(List.of(visit1, visit2));

        visitCrudRepository.deleteAll();

        long count = visitCrudRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testDeleteAllById() {
        Patient patient = new Patient(null, "Zoe", "Adams");
        Doctor doctor = new Doctor(null, "Dr. Red", "Brown", "UTC", 0);
        patientCrudRepository.save(patient);
        doctorCrudRepository.save(doctor);

        LocalDateTime startUTC1 = LocalDateTime.parse("2025-03-02T09:00");
        LocalDateTime endUTC1 = LocalDateTime.parse("2025-03-02T10:00");

        LocalDateTime startUTC2 = LocalDateTime.parse("2025-03-02T10:00");
        LocalDateTime endUTC2 = LocalDateTime.parse("2025-03-02T11:00");

        Visit visit1 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC1, endUTC1);
        Visit visit2 = new Visit(null, AggregateReference.to(patient.getId()), AggregateReference.to(doctor.getId()),
                startUTC2, endUTC2);
        visitCrudRepository.saveAll(List.of(visit1, visit2));

        visitCrudRepository.deleteAllById(List.of(visit1.getId()));

        long count = visitCrudRepository.count();
        assertThat(count).isEqualTo(1);
    }
}
