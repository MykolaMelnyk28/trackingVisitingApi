package com.trackingVisitingApi.payload.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "can not be empty")
    @NotEmpty(message = "can not be empty")
    private String firstName;

    @NotNull(message = "can not be empty")
    @NotEmpty(message = "can not be empty")
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<VisitDto> lastVisits = new ArrayList<>();

}
