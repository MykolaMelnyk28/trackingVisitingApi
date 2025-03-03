package com.trackingVisitingApi.controller.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.Page;
import com.trackingVisitingApi.service.v1.DoctorService;
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
import java.util.TimeZone;

@Tag(name = "Doctor")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(
            summary = "Create a new doctor",
            description = "Creates a new doctor with the provided details and returns the created doctor information.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Doctor successfully created"
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
    public ResponseEntity<DoctorDto> createDoctor(
            @RequestBody @Valid DoctorDto doctorDto,
            TimeZone timeZone,
            UriComponentsBuilder uriBuilder
    ) {
        Doctor doctor = new Doctor(null, doctorDto.getFirstName(), doctorDto.getLastName(), timeZone.getID(), 0);
        doctor = doctorService.create(doctor);
        URI uri = uriBuilder.path("/{id}").build(doctor.getId());
        return ResponseEntity.created(uri).body(DoctorDto.of(doctor));
    }

    @GetMapping
    @Operation(
            summary = "Get all doctors",
            description = "Retrieves a paginated list of doctors, optionally filtered by a search term.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of doctors successfully retrieved"
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
    public ResponseEntity<Page<DoctorDto>> getAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size,
            @RequestParam(defaultValue = "", required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new Page<>(
                doctorService.getAll(pageable, search).map(DoctorDto::of)
        ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get doctor by ID",
            description = "Retrieves details of a specific doctor by their unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Doctor details successfully retrieved"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid doctor ID"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Doctor not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<DoctorDto> getById(
            @PathVariable
            @Positive(message = "id must be positive")
            Long id
    ) {
        return ResponseEntity.of(doctorService.getById(id).map(DoctorDto::of));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable
            @Positive(message = "id must be positive")
            Long id
    ) {
        doctorService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}