package com.trackingVisitingApi.service.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.repository.v1.DoctorRepository;
import com.trackingVisitingApi.repository.v1.crud.DoctorCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorCrudRepository doctorCrudRepository;
    private final DoctorRepository doctorRepository;
    private final CacheManager cacheManager;

    @Transactional
    public Doctor create(Doctor doctor) {
        Objects.requireNonNull(doctor);
        Doctor saved = doctorCrudRepository.save(doctor);
        cacheManager.getCache("doctors").clear();
        cacheManager.getCache("doctorIds").put(saved.getId(), Optional.of(saved));
        return saved;
    }

    @Cacheable(value = "doctorIds", key = "#id")
    public Optional<Doctor> getById(Long id) {
        Objects.requireNonNull(id);
        return doctorCrudRepository.findById(id);
    }

    @Cacheable(value = "doctors", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #search")
    public Page<Doctor> getAll(Pageable pageable, String search) {
        Objects.requireNonNull(pageable, search);
        return doctorRepository.getPageAndSearch(pageable, search);
    }

    @Transactional
    public void deleteById(Long id) {
        Objects.requireNonNull(id);
        doctorCrudRepository.deleteById(id);
        cacheManager.getCache("doctors").clear();
        cacheManager.getCache("doctorIds").evict(id);
    }

}