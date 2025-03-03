package com.trackingVisitingApi.repository.v1.crud;

import com.trackingVisitingApi.entity.v1.Visit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitCrudRepository extends CrudRepository<Visit, Long>, PagingAndSortingRepository<Visit, Long> {


}
