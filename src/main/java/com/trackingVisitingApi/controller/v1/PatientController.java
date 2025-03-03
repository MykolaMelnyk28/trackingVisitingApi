package com.trackingVisitingApi.controller.v1;

import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.payload.v1.Page;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.service.v1.PatientService;
import com.trackingVisitingApi.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Patient")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/patients")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(
            summary = "Create a new patient",
            description = "Creates a new patient with the provided details and returns the created patient information.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Patient successfully created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<PatientDto> createPatient(
            @RequestBody @Valid PatientDto patientDto,
            UriComponentsBuilder uriBuilder
    ) {
        Patient patient = patientService.create(new Patient(null, patientDto.getFirstName(), patientDto.getLastName()));
        patientDto.setId(patient.getId());
        URI uri = uriBuilder.path("/{id}").build(patient.getId());
        return ResponseEntity.created(uri).body(patientDto);
    }

    @GetMapping
    @Operation(
            summary = "Get all patients",
            description = "Retrieves a paginated list of patients, optionally filtered by search query and doctor IDs.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of patients successfully retrieved"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<Page<PatientDto>> getAllPatients(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size,
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "", required = false) String doctorIds,
            @RequestParam(defaultValue = "true", required = false) boolean useTimeZone
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<Long> doctorIdsList = StringUtil.splitIds(doctorIds);
        return ResponseEntity.ok(new Page<>(
                patientService.getPatientsPage(pageable, search, doctorIdsList, useTimeZone)
        ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get patient by ID",
            description = "Retrieves details of a specific patient by their unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patient details successfully retrieved"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid patient ID"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patient not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<PatientDto> getPatient(
            @PathVariable
            @Positive(message = "id must be positive")
            Long id
    ) {
        return ResponseEntity.of(patientService.getDtoById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable
            @Positive(message = "id must be positive")
            Long id
    ) {
        patientService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
