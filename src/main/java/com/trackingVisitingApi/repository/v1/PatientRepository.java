package com.trackingVisitingApi.repository.v1;

import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PatientRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<PatientDto> getPatientDtoById(Long id, boolean useTimeZone) {
        String sql = Queries.PATIENTS_WITH_VISITS + " WHERE p.id = ? LIMIT 1";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> row = rows.get(0);
        Long patientId = (Long) row.get("patient_id");
        if (patientId == null) {
            return Optional.empty();
        }

        PatientDto patient = new PatientDto();
        if (patient.getLastVisits() == null) {
            patient.setLastVisits(new ArrayList<>());
        }

        patient.setId(patientId);
        patient.setFirstName((String)row.get("patient_first_name"));
        patient.setLastName((String)row.get("patient_last_name"));

        Long doctorId = (Long)row.get("doctor_id");
        if (row.containsKey("visit_id") && row.get("visit_id") != null) {
            VisitDto visit = new VisitDto();
            visit.setId((Long)row.get("visit_id"));

            TimeZone doctorTimeZone = TimeZone.getTimeZone((String)row.get("doctor_timezone"));

            LocalDateTime startUTC = (LocalDateTime) row.get("visit_start_date_time");
            if (startUTC != null) {
                visit.setStart(DateTimeUtil.convertWithAppendOffset(startUTC, doctorTimeZone));
            }

            LocalDateTime endUTC = (LocalDateTime) row.get("visit_end_date_time");
            if (endUTC != null) {
                visit.setEnd(DateTimeUtil.convertWithAppendOffset(endUTC, doctorTimeZone));
            }

            DoctorDto doctor = new DoctorDto();
            doctor.setId(doctorId);
            doctor.setFirstName((String)row.get("doctor_first_name"));
            doctor.setLastName((String)row.get("doctor_last_name"));
            doctor.setTimezone(doctorTimeZone.getID());
            doctor.setTotalPatients((Integer)row.get("doctor_total_patients"));

            visit.setDoctor(doctor);
            if (!patient.getLastVisits().contains(visit)) {
                patient.getLastVisits().add(visit);
            }
        }

        return Optional.of(patient);
    }

}
