package com.trackingVisitingApi.service.v1;

import com.trackingVisitingApi.entity.v1.Patient;
import com.trackingVisitingApi.payload.v1.PatientDto;
import com.trackingVisitingApi.repository.v1.PatientRepository;
import com.trackingVisitingApi.repository.v1.VisitRepository;
import com.trackingVisitingApi.repository.v1.crud.PatientCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientCrudRepository patientCrudRepository;
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final CacheManager cacheManager;

    @Transactional
    public Patient create(Patient patient) {
        Objects.requireNonNull(patient);
        Patient saved = patientCrudRepository.save(patient);
        cacheManager.getCache("patients").clear();
        cacheManager.getCache("patientIds").put(saved.getId(), Optional.of(saved));
        cacheManager.getCache("patientDtoIds").evict(saved.getId());
        return saved;
    }

    @Cacheable(value = "patients", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #search + '-' + #doctorIds + '-' + #useTimeZone")
    public Page<PatientDto> getPatientsPage(Pageable pageable, String search, List<Long> doctorIds, boolean useTimeZone) {
        Objects.requireNonNull(pageable);
        Objects.requireNonNull(search);
        Objects.requireNonNull(doctorIds);
        String lowerCaseSearch = search.toLowerCase();
        return visitRepository.getVisitsGroupedByPatient(pageable, List.of(
                x -> lowerCaseSearch.isEmpty() ||
                     x.getFirstName().toLowerCase().contains(lowerCaseSearch) ||
                     x.getLastName().toLowerCase().contains(lowerCaseSearch)
        ), doctorIds, useTimeZone);
    }

    @Cacheable(value = "patientIds", key = "#id")
    public Optional<Patient> getById(Long id) {
        Objects.requireNonNull(id);
        return patientCrudRepository.findById(id);
    }

    @Cacheable(value = "patientDtoIds", key = "#id")
    public Optional<PatientDto> getDtoById(Long id) {
        Objects.requireNonNull(id);
        return patientRepository.getPatientDtoById(id, true);
    }

    @Transactional
    public void deleteById(Long id) {
        Objects.requireNonNull(id);
        patientCrudRepository.deleteById(id);
        cacheManager.getCache("patients").clear();
        cacheManager.getCache("patientIds").evict(id);
        cacheManager.getCache("patientDtoIds").evict(id);
    }

}
