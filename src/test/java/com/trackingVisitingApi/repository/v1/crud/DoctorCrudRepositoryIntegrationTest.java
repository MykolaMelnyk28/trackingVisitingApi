package com.trackingVisitingApi.repository.v1.crud;

import com.trackingVisitingApi.entity.v1.Doctor;
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
class DoctorCrudRepositoryIntegrationTest {

    @Autowired
    DoctorCrudRepository doctorCrudRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        doctorCrudRepository.deleteAll();
    }

    @Test
    void testSaveAndFindDoctor() {
        Doctor doctor = new Doctor(null, "John", "Doe", "UTC", 0);
        Doctor savedDoctor = doctorCrudRepository.save(doctor);

        Optional<Doctor> foundDoctor = doctorCrudRepository.findById(savedDoctor.getId());

        assertThat(foundDoctor)
                .isPresent()
                .contains(savedDoctor);
    }

    @Test
    void testDeleteDoctor() {
        Doctor doctor = new Doctor(null, "Alice", "Smith", "UTC+2", 0);
        Doctor savedDoctor = doctorCrudRepository.save(doctor);

        doctorCrudRepository.deleteById(savedDoctor.getId());

        Optional<Doctor> deletedDoctor = doctorCrudRepository.findById(savedDoctor.getId());
        assertThat(deletedDoctor).isEmpty();
    }

    @Test
    void testPagingAndSorting() {
        List<Doctor> doctors = List.of(
                new Doctor(null, "John", "Doe", "UTC", 0),
                new Doctor(null, "Alice", "Smith", "UTC+2", 0),
                new Doctor(null, "Bob", "Brown", "UTC-5", 0)
        );
        doctorCrudRepository.saveAll(doctors);

        Pageable pageable = PageRequest.of(0, 2, Sort.by("lastName").ascending());
        Page<Doctor> page = doctorCrudRepository.findAll(pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0)).isEqualTo(doctors.get(2));
        assertThat(page.getContent().get(1)).isEqualTo(doctors.get(0));
    }

    @Test
    void testSaveAllDoctors() {
        List<Doctor> doctors = List.of(
                new Doctor(null, "John", "Doe", "UTC", 10),
                new Doctor(null, "Alice", "Smith", "UTC+2", 15)
        );
        Iterable<Doctor> savedDoctors = doctorCrudRepository.saveAll(doctors);

        assertThat(savedDoctors).hasSize(2);
    }

    @Test
    void testExistsById() {
        Doctor doctor = new Doctor(null, "Charlie", "Brown", "UTC-3", 0);
        Doctor savedDoctor = doctorCrudRepository.save(doctor);

        boolean exists = doctorCrudRepository.existsById(savedDoctor.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void testCountDoctors() {
        List<Doctor> doctors = List.of(
                new Doctor(null, "John", "Doe", "UTC", 5),
                new Doctor(null, "Alice", "Smith", "UTC+2", 10)
        );
        doctorCrudRepository.saveAll(doctors);

        long count = doctorCrudRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void testFindAllDoctors() {
        List<Doctor> doctors = List.of(
                new Doctor(null, "John", "Doe", "UTC", 20),
                new Doctor(null, "Alice", "Smith", "UTC+1", 30)
        );
        doctorCrudRepository.saveAll(doctors);

        Iterable<Doctor> allDoctors = doctorCrudRepository.findAll();

        assertThat(allDoctors).hasSize(2);
    }

    @Test
    void testDeleteAllDoctors() {
        List<Doctor> doctors = List.of(
                new Doctor(null, "John", "Doe", "UTC", 20),
                new Doctor(null, "Alice", "Smith", "UTC+1", 30)
        );
        doctorCrudRepository.saveAll(doctors);

        doctorCrudRepository.deleteAll();

        long count = doctorCrudRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testDeleteAllById() {
        Doctor doctor1 = new Doctor(null, "John", "Doe", "UTC", 20);
        Doctor doctor2 = new Doctor(null, "Alice", "Smith", "UTC+1", 30);
        doctorCrudRepository.saveAll(List.of(doctor1, doctor2));

        doctorCrudRepository.deleteAllById(List.of(doctor1.getId()));

        long count = doctorCrudRepository.count();
        assertThat(count).isEqualTo(1);
    }
}