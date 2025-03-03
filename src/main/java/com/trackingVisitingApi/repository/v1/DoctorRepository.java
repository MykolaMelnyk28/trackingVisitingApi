package com.trackingVisitingApi.repository.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DoctorRepository {

    private final JdbcTemplate jdbcTemplate;

    public Page<Doctor> getPageAndSearch(Pageable pageable, String search) {
        String sql = "SELECT * FROM doctors d WHERE d.first_name LIKE ? OR d.last_name LIKE ? LIMIT ? OFFSET ?";

        List<Doctor> doctors = jdbcTemplate.query(sql, new Object[]{
                "%" + search + "%",
                "%" + search + "%",
                pageable.getPageSize(),
                pageable.getOffset()
        }, new BeanPropertyRowMapper<>(Doctor.class));

        String countSql = "SELECT COUNT(*) FROM doctors d WHERE d.first_name LIKE ? OR d.last_name LIKE ?";
        int totalRecords = jdbcTemplate.queryForObject(countSql, Integer.class, "%" + search + "%", "%" + search + "%");

        return new PageImpl<>(doctors, pageable, totalRecords);
    }

}
