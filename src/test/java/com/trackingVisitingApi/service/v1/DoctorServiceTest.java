package com.trackingVisitingApi.service.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.repository.v1.DoctorRepository;
import com.trackingVisitingApi.repository.v1.crud.DoctorCrudRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    DoctorCrudRepository doctorCrudRepository;
    
    @Mock
    DoctorRepository doctorRepository;

    @Mock
    CacheManager cacheManager;
    
    @InjectMocks
    DoctorService doctorService;

    @Test
    void shouldCreateDoctorSuccessfully() {
        TimeZone timeZone = TimeZone.getDefault();
        Doctor request = new Doctor(null, "Dr. John", "Doe", timeZone.getID(), 0);
        Doctor expected = new Doctor(1L, "Dr. John", "Doe", timeZone.getID(), 0);
        
        when(doctorCrudRepository.save(eq(request))).thenReturn(expected);
        when(cacheManager.getCache(anyString())).thenReturn(mock(Cache.class));

        Doctor actual = doctorService.create(request);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
        verify(doctorCrudRepository, times(1)).save(request);
    }

    @Test
    void shouldGetDoctorByIdSuccessfully() {
        TimeZone timeZone = TimeZone.getDefault();
        Long doctorId = 1L;
        Doctor expected = new Doctor(doctorId, "Dr. John", "Doe", timeZone.getID(), 0);
        
        when(doctorCrudRepository.findById(eq(doctorId))).thenReturn(Optional.of(expected));

        Optional<Doctor> actual = doctorService.getById(doctorId);

        assertThat(actual).isPresent().contains(expected);
        verify(doctorCrudRepository, times(1)).findById(doctorId);
    }

    @Test
    void shouldGetDoctorByIdReturnsEmptyOptional() {
        Long doctorId = 2L;

        when(doctorCrudRepository.findById(eq(doctorId))).thenReturn(Optional.empty());

        Optional<Doctor> actual = doctorService.getById(doctorId);

        assertThat(actual).isEmpty();
        verify(doctorCrudRepository, times(1)).findById(doctorId);
    }

    @Test
    void shouldGetAllDoctorsWithSearch() {
        String search = "John";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> expectedPage = new PageImpl<>(List.of(
                new Doctor(1L, "Dr. John", "Doe", "Europe/Belgrade", 0),
                new Doctor(2L, "Dr. Jane", "Doe", "Europe/Kiev", 0)
        ));

        when(doctorRepository.getPageAndSearch(eq(pageable), eq(search))).thenReturn(expectedPage);

        Page<Doctor> actual = doctorService.getAll(pageable, search);

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(2);
        assertThat(actual.getContent()).extracting(Doctor::getFirstName).containsExactly("Dr. John", "Dr. Jane");
        
        verify(doctorRepository, times(1)).getPageAndSearch(pageable, search);
    }

}
