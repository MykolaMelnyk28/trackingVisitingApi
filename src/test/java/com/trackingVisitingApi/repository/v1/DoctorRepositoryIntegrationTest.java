package com.trackingVisitingApi.repository.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Tag("integration")
@Import(DoctorRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DoctorRepositoryIntegrationTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "doctors", "visits", "patients");

        jdbcTemplate.update("INSERT INTO doctors (first_name, last_name, timezone) VALUES (?, ?, ?)", "John", "Smith", "Europe/Belgrade");
        jdbcTemplate.update("INSERT INTO doctors (first_name, last_name, timezone) VALUES (?, ?, ?)", "Jane", "Doe", "Europe/Kiev");
        jdbcTemplate.update("INSERT INTO doctors (first_name, last_name, timezone) VALUES (?, ?, ?)", "Jim", "Smith", "Asia/Dubai");
        jdbcTemplate.update("INSERT INTO doctors (first_name, last_name, timezone) VALUES (?, ?, ?)", "Jack", "Jackson", "Asia/Tokyo");
    }

    @Test
    void testGetPageAndSearch() {
        PageRequest pageable = PageRequest.of(0, 2);

        String search = "J";
        String lowerSearch = search.toLowerCase();
        Page<Doctor> resultPage = doctorRepository.getPageAndSearch(pageable, search);

        assertThat(resultPage)
                .isNotNull()
                .isNotEmpty();
        assertThat(resultPage.getContent().size())
                .isEqualTo(2);

        assertThat(resultPage.getContent()).allMatch(d ->
                // contains ignore case
                d.getFirstName().toLowerCase().contains(lowerSearch) ||
                d.getLastName().toLowerCase().contains(lowerSearch)
        );

        assertThat(resultPage.getTotalElements())
                .isEqualTo(4);
    }
}