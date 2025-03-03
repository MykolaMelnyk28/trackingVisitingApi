package com.trackingVisitingApi.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    @Id
    private Long id;

    @Column("patient_id")
    private AggregateReference<Patient, Long> patient;

    @Column("doctor_id")
    private AggregateReference<Doctor, Long> doctor;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

}
