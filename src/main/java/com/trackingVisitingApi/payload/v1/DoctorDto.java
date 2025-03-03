package com.trackingVisitingApi.payload.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trackingVisitingApi.entity.v1.Doctor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {

    public static DoctorDto of(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return new DoctorDto(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getTimezone(),
                doctor.getTotalPatients()
        );
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "can not be empty")
    @NotEmpty(message = "can not be empty")
    private String firstName;

    @NotNull(message = "can not be empty")
    @NotEmpty(message = "can not be empty")
    private String lastName;

    @NotNull(message = "can not be empty")
    @NotEmpty(message = "can not be empty")
    private String timezone;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int totalPatients;

}
