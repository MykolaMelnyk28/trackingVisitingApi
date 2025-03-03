package com.trackingVisitingApi.payload.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime start;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime end;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private DoctorDto doctor;

}
