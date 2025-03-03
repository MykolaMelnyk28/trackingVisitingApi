package com.trackingVisitingApi.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.entity.v1.Visit;
import com.trackingVisitingApi.exception.ResourceConflictException;
import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.payload.v1.VisitRequest;
import com.trackingVisitingApi.service.v1.VisitService;
import com.trackingVisitingApi.util.DateTimeUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("unit")
@WebMvcTest(VisitController.class)
public class VisitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    VisitService visitService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreateSingleVisit() throws Exception {
        TimeZone timeZone = TimeZone.getDefault();
        LocalDateTime startUTC = LocalDateTime.parse("2025-02-27T11:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-02-27T11:30");
        OffsetDateTime startDateTime = DateTimeUtil.convertWithAppendOffset(startUTC, timeZone);
        OffsetDateTime endDateTime = DateTimeUtil.convertWithAppendOffset(endUTC, timeZone);

        Patient patient = new Patient(1L, "John", "Smith");
        Doctor doctor = new Doctor(1L, "Tom", "Ren", timeZone.getID(), 1);
        DoctorDto doctorDto = DoctorDto.of(doctor);

        VisitRequest request = VisitRequest.builder()
                .start(startDateTime)
                .end(endDateTime)
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .build();


        Visit visit = new Visit(
                null,
                AggregateReference.to(request.getPatientId()),
                AggregateReference.to(request.getDoctorId()),
                startUTC,
                endUTC
        );
        Visit saved = new Visit(
                1L,
                visit.getPatient(),
                visit.getDoctor(),
                startUTC,
                endUTC
        );
        VisitDto expected = new VisitDto(1L, request.getStart(), request.getEnd(), doctorDto);

        when( visitService.create(any(Visit.class)) ).thenReturn(saved);
        when( visitService.toDto(any(Visit.class)) ).thenReturn(expected);

        mockMvc.perform(post("/v1/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void testCreatingVisitWithOverlappingTimeRange() throws Exception {
        TimeZone timeZone = TimeZone.getDefault();
        LocalDateTime startUTC = LocalDateTime.parse("2025-02-27T11:00");
        LocalDateTime endUTC = LocalDateTime.parse("2025-02-27T11:30");
        OffsetDateTime startDateTime = DateTimeUtil.convertWithAppendOffset(startUTC, timeZone);
        OffsetDateTime endDateTime = DateTimeUtil.convertWithAppendOffset(endUTC, timeZone);

        VisitRequest request = VisitRequest.builder()
                .start(startDateTime)
                .end(endDateTime)
                .patientId(1L)
                .doctorId(1L)
                .build();

        when( visitService.create(any(Visit.class)) )
                .thenThrow(ResourceConflictException.class);

        mockMvc.perform(post("/v1/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void testDeleteById() throws Exception {
        Long id = 1L;
        doNothing().when(visitService).deleteById(id);
        mockMvc.perform(delete("/v1/visits/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByIdNotFound() throws Exception {
        Long id = 1L;
        doNothing().when(visitService).deleteById(id);
        mockMvc.perform(delete("/v1/visits/{id}", id))
                .andExpect(status().isOk());
    }

}