package com.trackingVisitingApi.controller.v1;

import com.trackingVisitingApi.entity.v1.Visit;
import com.trackingVisitingApi.payload.v1.Page;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.payload.v1.VisitRequest;
import com.trackingVisitingApi.service.v1.VisitService;
import com.trackingVisitingApi.util.DateTimeUtil;
import com.trackingVisitingApi.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Visit")
@RestController
@AllArgsConstructor
@RequestMapping("/v1/visits")
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    @Operation(
            summary = "Create a new visit",
            description = "Creates a new visit with the provided details and returns the created visit information.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Visit successfully created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict between internal data and specify Visit details"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<VisitDto> createVisit(
            @RequestBody @Valid VisitRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        LocalDateTime startUTC = DateTimeUtil.convertWithRemoveOffset(request.getStart());
        LocalDateTime endUTC = DateTimeUtil.convertWithRemoveOffset(request.getEnd());

        Visit visit = new Visit(
                null,
                AggregateReference.to(request.getPatientId()),
                AggregateReference.to(request.getDoctorId()),
                startUTC,
                endUTC
        );

        visit = visitService.create(visit);

        URI uri = uriBuilder.path("/{id}").build(visit.getId());
        return ResponseEntity.created(uri).body(visitService.toDto(visit));
    }

    @GetMapping
    @Operation(
            summary = "Get all visits",
            description = "Retrieves a paginated list of visits, optionally filtered by patient id and by doctor id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of visits successfully retrieved"
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
    public ResponseEntity<Page<VisitDto>> getAllVisits(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size,
            @RequestParam(defaultValue = "", required = false) String patientIds,
            @RequestParam(defaultValue = "", required = false) String doctorIds,
            @RequestParam(defaultValue = "true", required = false) boolean useTimeZone
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<Long> patientsIdsList = StringUtil.splitIds(patientIds);
        List<Long> doctorIdsList = StringUtil.splitIds(doctorIds);
        return ResponseEntity.ok(new Page<>(
                visitService.getVisitsPage(pageable, patientsIdsList,doctorIdsList, useTimeZone)
        ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get visit by ID",
            description = "Retrieves details of a specific visit by their unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Visit details successfully retrieved"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid visit ID"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Visit not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<VisitDto> getVisitById(
            @PathVariable
            @Positive(message = "id must be positive")
            Long id
    ) {
        return ResponseEntity.of(visitService.getById(id).map(visitService::toDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable
            @Positive(message = "id must be positive")
            Long id
    ) {
        visitService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
