package com.trackingVisitingApi.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.Page;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.service.v1.PatientService;
import com.trackingVisitingApi.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("unit")
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PatientService patientService;

    @Autowired
    ObjectMapper objectMapper;

    List<PatientDto> patientDtos;

    @BeforeEach
    void setUp() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");
        DoctorDto doctor = new DoctorDto(1L, "Dr. Adam", "Taylor", timeZone.getID(), 2);
        LocalDateTime startUTC = LocalDateTime.parse("2025-02-26T13:45");
        LocalDateTime endUTC = LocalDateTime.parse("2025-02-26T14:10");
        patientDtos = List.of(
                new PatientDto(1L, "John", "Doe", List.of()),
                new PatientDto(2L, "Jane", "Smith", List.of(
                        new VisitDto(2L, DateTimeUtil.convertWithAppendOffset(startUTC, timeZone), DateTimeUtil.convertWithAppendOffset(endUTC, timeZone), doctor)
                ))
        );
    }

    @Test
    void testCreatePatient() throws Exception {
        Patient patient = new Patient(1L, patientDtos.get(0).getFirstName(), patientDtos.get(0).getLastName());
        when(patientService.create(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDtos.get(0))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(objectMapper.writeValueAsString(patientDtos.get(0))));
    }

    @Test
    void testGetAllPatients() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);

        org.springframework.data.domain.Page<PatientDto> page = new PageImpl<>(patientDtos, pageable, 2);
        var expectedPage = new Page<>(page);
        when(patientService.getPatientsPage(any(Pageable.class), anyString(), any(), anyBoolean()))
                .thenReturn(page);

        mockMvc.perform(get("/v1/patients")
                        .param("page", "0")
                        .param("size", "20")
                        .param("search", "Doe")
                        .param("doctorIds", "1,2")
                        .param("useTimeZone", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    void testGetPatient() throws Exception {
        when(patientService.getDtoById(1L)).thenReturn(Optional.of(patientDtos.get(0)));

        mockMvc.perform(get("/v1/patients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patientDtos.get(0))));
    }

    @Test
    void testGetPatientNotFound() throws Exception {
        when(patientService.getDtoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/patients/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteById() throws Exception {
        Long id = 1L;
        doNothing().when(patientService).deleteById(id);
        mockMvc.perform(delete("/v1/patients/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByIdNotFound() throws Exception {
        Long id = 1L;
        doNothing().when(patientService).deleteById(id);
        mockMvc.perform(delete("/v1/patients/{id}", id))
                .andExpect(status().isOk());
    }

}