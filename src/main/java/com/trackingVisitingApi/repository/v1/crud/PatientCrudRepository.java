package com.trackingVisitingApi.repository.v1.crud;

import com.trackingVisitingApi.entity.v1.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientCrudRepository extends CrudRepository<Patient, Long>, PagingAndSortingRepository<Patient, Long> {


}
