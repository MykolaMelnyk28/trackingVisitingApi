package com.trackingVisitingApi.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.service.v1.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("unit")
@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    DoctorService doctorService;

    @Autowired
    ObjectMapper objectMapper;

    DoctorDto doctorDto;

    @BeforeEach
    void setUp() {
        doctorDto = new DoctorDto(1L, "John", "Doe", "Australia/Sydney", 0);
    }

    @Test
    void testCreateDoctor() throws Exception {
        Doctor doctor = new Doctor(1L, "John", "Doe", "Australia/Sydney", 0);
        when(doctorService.create(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(objectMapper.writeValueAsString(doctorDto)));
    }

    @Test
    void testGetAllDoctors() throws Exception {
        Page<Doctor> doctorsPage = mock(Page.class);
        when(doctorService.getAll(any(Pageable.class), anyString())).thenReturn(doctorsPage);
        when(doctorsPage.map(any())).thenReturn(mock(Page.class));
        var expectedPage = new com.trackingVisitingApi.payload.v1.Page<>(doctorsPage.map(DoctorDto::of));

        mockMvc.perform(get("/v1/doctors")
                        .param("page", "0")
                        .param("size", "20")
                        .param("search", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    void testGetDoctorById() throws Exception {
        Doctor doctor = new Doctor(1L, "John", "Doe", "UTC", 0);
        when(doctorService.getById(1L)).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/v1/doctors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetDoctorByIdNotFound() throws Exception {
        when(doctorService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/doctors/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteById() throws Exception {
        Long id = 1L;
        doNothing().when(doctorService).deleteById(id);
        mockMvc.perform(delete("/v1/doctors/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByIdNotFound() throws Exception {
        Long id = 1L;
        doNothing().when(doctorService).deleteById(id);
        mockMvc.perform(delete("/v1/doctors/{id}", id))
                .andExpect(status().isOk());
    }

}