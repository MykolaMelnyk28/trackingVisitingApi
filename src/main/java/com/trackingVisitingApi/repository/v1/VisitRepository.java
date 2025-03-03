package com.trackingVisitingApi.repository.v1;

import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

@Repository
@RequiredArgsConstructor
public class VisitRepository {

    private final JdbcTemplate jdbcTemplate;

    public Page<VisitDto> getVisits(Pageable pageable, List<Long> patientIds, List<Long> doctorIds, boolean useTimeZone) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Queries.VISITS_GROUPED);

        List<VisitDto> visits = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Long doctorId = (Long) row.get("doctor_id");
            Long patientId = (Long) row.get("visit_patient_id");
            if (row.containsKey("visit_id") && row.get("visit_id") != null &&
                (doctorIds.isEmpty() || doctorIds.contains(doctorId)) &&
                (patientIds.isEmpty() || patientIds.contains(patientId))
            ) {
                VisitDto visit = new VisitDto();
                visit.setId((Long) row.get("visit_id"));

                TimeZone doctorTimeZone = TimeZone.getTimeZone((String) row.get("doctor_timezone"));

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
                doctor.setFirstName((String) row.get("doctor_first_name"));
                doctor.setLastName((String) row.get("doctor_last_name"));
                doctor.setTimezone(doctorTimeZone.getID());
                doctor.setTotalPatients((Integer)row.get("doctor_total_patients"));

                visit.setDoctor(doctor);

                visits.add(visit);
            }
        }

        List<VisitDto> content = visits.stream().skip(pageable.getOffset()).limit(pageable.getPageSize()).toList();
        return new PageImpl<>(content, pageable, visits.size());
    }

    public Page<PatientDto> getVisitsGroupedByPatient(Pageable pageable, List<Predicate<PatientDto>> filters, List<Long> doctorIds, boolean useTimeZone) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(Queries.PATIENTS_WITH_VISITS);

        Map<Long, PatientDto> patientMap = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Long patientId = (Long) row.get("patient_id");

            PatientDto patient = patientMap.getOrDefault(patientId, new PatientDto());
            if (patient.getLastVisits() == null) {
                patient.setLastVisits(new ArrayList<>());
            }

            patient.setId(patientId);
            patient.setFirstName((String) row.get("patient_first_name"));
            patient.setLastName((String) row.get("patient_last_name"));

            if (!filters.stream().allMatch(x -> x.test(patient))) {
                continue;
            }

            Long doctorId = (Long) row.get("doctor_id");
            if (row.containsKey("visit_id") && row.get("visit_id") != null && (doctorIds.isEmpty() || doctorIds.contains(doctorId))) {
                VisitDto visit = new VisitDto();
                visit.setId((Long) row.get("visit_id"));

                TimeZone doctorTimeZone = TimeZone.getTimeZone((String) row.get("doctor_timezone"));

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
                doctor.setFirstName((String) row.get("doctor_first_name"));
                doctor.setLastName((String) row.get("doctor_last_name"));
                doctor.setTimezone(doctorTimeZone.getID());
                doctor.setTotalPatients((Integer)row.get("doctor_total_patients"));

                visit.setDoctor(doctor);
                if (!patient.getLastVisits().contains(visit)) {
                    patient.getLastVisits().add(visit);
                }
            }

            patientMap.put(patientId, patient);
        }

        List<PatientDto> content = patientMap.values().stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
        return new PageImpl<>(content, pageable, patientMap.values().size());
    }

    public int countVisitsBetweenStartAndEnd(LocalDateTime start, LocalDateTime end) {
        return jdbcTemplate.queryForObject(Queries.COUNT_VISITS_BETWEEN_START_AND_END, Integer.class, start, end);
    }

}
