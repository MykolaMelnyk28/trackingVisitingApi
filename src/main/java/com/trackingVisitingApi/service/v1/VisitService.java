package com.trackingVisitingApi.service.v1;

import com.trackingVisitingApi.entity.v1.Doctor;
import com.trackingVisitingApi.entity.v1.Visit;
import com.trackingVisitingApi.exception.ResourceConflictException;
import com.trackingVisitingApi.exception.ResourceNotFoundException;
import com.trackingVisitingApi.payload.v1.DoctorDto;
import com.trackingVisitingApi.payload.v1.VisitDto;
import com.trackingVisitingApi.repository.v1.VisitRepository;
import com.trackingVisitingApi.repository.v1.crud.VisitCrudRepository;
import com.trackingVisitingApi.util.DateTimeUtil;
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
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitCrudRepository visitCrudRepository;
    private final VisitRepository visitRepository;
    private final DoctorService doctorService;
    private final CacheManager cacheManager;

    @Transactional
    public Visit create(Visit visit) {
        Objects.requireNonNull(visit);
        int countOverlaps = visitRepository.countVisitsBetweenStartAndEnd(visit.getStartDateTime(), visit.getEndDateTime());
        if (countOverlaps > 0) {
            throw new ResourceConflictException("Visit time overlaps with an existing visit");
        }

        Visit saved = visitCrudRepository.save(visit);

        cacheManager.getCache("visits").clear();
        cacheManager.getCache("visitIds").put(saved.getId(), Optional.of(saved));

        return saved;
    }

    @Cacheable(value = "visitIds", key = "#id")
    public Optional<Visit> getById(Long id) {
        Objects.requireNonNull(id);
        return visitCrudRepository.findById(id);
    }

    @Cacheable(value = "visits",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #patientsIdsList + '-' + #doctorIdsList + '-' + #useTimeZone")
    public Page<VisitDto> getVisitsPage(Pageable pageable, List<Long> patientsIdsList, List<Long> doctorIdsList, boolean useTimeZone) {
        return visitRepository.getVisits(pageable, patientsIdsList, doctorIdsList, useTimeZone);
    }

    @Transactional
    public void deleteById(Long id) {
        Objects.requireNonNull(id);
        visitCrudRepository.deleteById(id);
        cacheManager.getCache("visits").clear();
        cacheManager.getCache("visitIds").evict(id);
    }

    public VisitDto toDto(Visit visit) {
        if (visit == null) {
            return null;
        } else if (visit.getDoctor() == null) {
            throw new IllegalStateException();
        }
        Doctor doctor = doctorService.getById(visit.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        TimeZone timeZone = TimeZone.getTimeZone(doctor.getTimezone());
        return new VisitDto(
                visit.getId(),
                DateTimeUtil.convertWithAppendOffset(visit.getStartDateTime(), timeZone),
                DateTimeUtil.convertWithAppendOffset(visit.getEndDateTime(), timeZone),
                DoctorDto.of(doctor)
        );
    }

}
