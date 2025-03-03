package com.trackingVisitingApi.repository.v1.crud;

import com.trackingVisitingApi.entity.v1.Patient;
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
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Tag("integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientCrudRepositoryIntegrationTest {

    @Autowired
    PatientCrudRepository patientCrudRepository;

    @BeforeEach
    void setUp() {
        patientCrudRepository.deleteAll();
    }

    @Test
    void testSaveAndFindPatient() {
        Patient patient = new Patient(null, "John", "Doe");
        Patient savedPatient = patientCrudRepository.save(patient);
        
        Optional<Patient> foundPatient = patientCrudRepository.findById(savedPatient.getId());
        
        assertThat(foundPatient)
                .isPresent()
                .contains(savedPatient);
    }

    @Test
    void testDeletePatient() {
        Patient patient = new Patient(null, "Alice", "Smith");
        Patient savedPatient = patientCrudRepository.save(patient);

        patientCrudRepository.deleteById(savedPatient.getId());
        
        Optional<Patient> deletedPatient = patientCrudRepository.findById(savedPatient.getId());
        assertThat(deletedPatient).isEmpty();
    }

    @Test
    void testPagingAndSorting() {
        List<Patient> patients = List.of(
                new Patient(null, "John", "Doe"),
                new Patient(null, "Alice", "Smith"),
                new Patient(null, "Bob", "Brown")
        );
        patientCrudRepository.saveAll(patients);

        Pageable pageable = PageRequest.of(0, 2, Sort.by("lastName").ascending());
        Page<Patient> page = patientCrudRepository.findAll(pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0)).isEqualTo(patients.get(2));
        assertThat(page.getContent().get(1)).isEqualTo(patients.get(0));
    }

    @Test
    void testSaveAllPatients() {
        List<Patient> patients = List.of(
                new Patient(null, "John", "Doe"),
                new Patient(null, "Alice", "Smith")
        );
        Iterable<Patient> savedPatients = patientCrudRepository.saveAll(patients);

        assertThat(savedPatients).hasSize(2);
    }

    @Test
    void testExistsById() {
        Patient patient = new Patient(null, "Charlie", "Brown");
        Patient savedPatient = patientCrudRepository.save(patient);

        boolean exists = patientCrudRepository.existsById(savedPatient.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void testCountPatients() {
        List<Patient> patients = List.of(
                new Patient(null, "John", "Doe"),
                new Patient(null, "Alice", "Smith")
        );
        patientCrudRepository.saveAll(patients);

        long count = patientCrudRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void testFindAllPatients() {
        List<Patient> patients = List.of(
                new Patient(null, "John", "Doe"),
                new Patient(null, "Alice", "Smith")
        );
        patientCrudRepository.saveAll(patients);

        Iterable<Patient> allPatients = patientCrudRepository.findAll();

        assertThat(allPatients).hasSize(2);
    }

    @Test
    void testDeleteAllPatients() {
        List<Patient> patients = List.of(
                new Patient(null, "John", "Doe"),
                new Patient(null, "Alice", "Smith")
        );
        patientCrudRepository.saveAll(patients);

        patientCrudRepository.deleteAll();

        long count = patientCrudRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testDeleteAllById() {
        Patient patient1 = new Patient(null, "John", "Doe");
        Patient patient2 = new Patient(null, "Alice", "Smith");
        patientCrudRepository.saveAll(List.of(patient1, patient2));

        patientCrudRepository.deleteAllById(List.of(patient1.getId()));

        long count = patientCrudRepository.count();
        assertThat(count).isEqualTo(1);
    }
}