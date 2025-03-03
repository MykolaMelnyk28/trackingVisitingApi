package com.trackingVisitingApi.payload.v1;

import com.trackingVisitingApi.validate.ValidDateRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidDateRange(start = "start", end = "end")
public class VisitRequest {

    @NotNull(message = "Start date cannot be null")
    private OffsetDateTime start;

    @NotNull(message = "End date cannot be null")
    private OffsetDateTime end;

    @NotNull(message = "Patient ID cannot be null")
    @Positive(message = "Patient ID must be a positive number")
    private Long patientId;

    @NotNull(message = "Doctor ID cannot be null")
    @Positive(message = "Doctor ID must be a positive number")
    private Long doctorId;
}