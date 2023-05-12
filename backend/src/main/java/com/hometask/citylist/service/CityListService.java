package com.hometask.citylist.service;

import com.hometask.citylist.model.City;
import com.hometask.citylist.repository.CityRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elnur Mammadov
 */

@Service
@RequiredArgsConstructor
public class CityListService {
    private final CityRepository cityRepository;
    private final ValidationService validationService;

    @Transactional(readOnly = true)
    public List<City> getAllCities(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id"));

        Page<City> pagedResult = cityRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<City> getCityByName(Integer pageNo, Integer pageSize, String name) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id"));

        Page<City> pagedResult = cityRepository.getCitiesByName(paging, name);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public City updateCity(City city,
                           String name,
                           String photo
    ) {
        city.validateUrl(photo);
        city.setName(name);
        city.setPhoto(photo);
        city = validationService.validateAndSave(cityRepository, city);
        return city;
    }

    @Transactional(readOnly = true)
    public City getCityById(Long id) throws NotFoundException {
        return cityRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("unable to find city by id (" + id + ")"));
    }
}
