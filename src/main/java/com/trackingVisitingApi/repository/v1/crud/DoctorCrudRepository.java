package com.trackingVisitingApi.repository.v1.crud;

import com.trackingVisitingApi.entity.v1.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorCrudRepository extends CrudRepository<Doctor, Long>, PagingAndSortingRepository<Doctor, Long> {


}
