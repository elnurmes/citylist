package com.hometask.citylist.repository;

import com.hometask.citylist.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Elnur Mammadov
 */

@Repository
public interface CityRepository extends PagingAndSortingRepository<City, Long> {

    Page<City> findAllByName(Pageable pageable, String name);
    Page<City> getCitiesByName(Pageable pageable, String name);
}
