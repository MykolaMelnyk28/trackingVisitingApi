package com.trackingVisitingApi.repository.v1;

public interface Queries {

    String INSERT_DOCTOR = "INSERT INTO doctors (first_name, last_name, timezone) VALUES (?, ?, ?)";
    String INSERT_PATIENT = "INSERT INTO patients (first_name, last_name) VALUES (?, ?)";
    String INSERT_VISIT = "INSERT INTO visits (patient_id, doctor_id, start_date_time, end_date_time) VALUES (?, ?, ?, ?)";

    String PATIENTS_WITH_VISITS = """
            SELECT
            p.id AS patient_id,
            p.first_name AS patient_first_name,
            p.last_name AS patient_last_name,
            v.id AS visit_id,
            v.start_date_time AS visit_start_date_time,
            v.end_date_time AS visit_end_date_time,
            d.id AS doctor_id,
            d.first_name AS doctor_first_name,
            d.last_name AS doctor_last_name,
            d.timezone AS doctor_timezone,
            d.total_patients AS doctor_total_patients
            FROM patients p
            LEFT JOIN visits v ON p.id = v.patient_id
            LEFT JOIN doctors d ON d.id = v.doctor_id
            """;

    String VISITS_GROUPED = """
            SELECT
            v.id AS visit_id,
            v.patient_id AS visit_patient_id,
            v.start_date_time AS visit_start_date_time,
            v.end_date_time AS visit_end_date_time,
            d.id AS doctor_id,
            d.first_name AS doctor_first_name,
            d.last_name AS doctor_last_name,
            d.timezone AS doctor_timezone,
            d.total_patients AS doctor_total_patients
            FROM visits v
            LEFT JOIN doctors d ON d.id = v.doctor_id
            """;

    String COUNT_VISITS_BETWEEN_START_AND_END = "SELECT COUNT(*) FROM visits v WHERE v.end_date_time > ? AND v.start_date_time < ?";

}
