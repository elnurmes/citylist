package com.hometask.citylist.controller;

import com.hometask.citylist.model.City;
import com.hometask.citylist.service.CityListService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Elnur Mammadov
 */
@RestController
@RequestMapping(path = "/v1/city", produces = "application/json")
@RequiredArgsConstructor
public class CityListController {

    private final CityListService service;

    @GetMapping("/all")
    public ResponseEntity<List<City>> getAllCities(@RequestParam(defaultValue = "0") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        List<City> list = service.getAllCities(pageNo, pageSize);

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<List<City>> getCityByName(@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @PathVariable String name) {
        return new ResponseEntity<>(service.getCityByName(pageNo, pageSize, name), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<City> getCityById(@PathVariable String id) throws NotFoundException {
        return new ResponseEntity<>(service.getCityById(Long.parseLong(id)), new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('CITY_EDIT')")
    public ResponseEntity<City> updateCity(@Valid @RequestBody City request) throws NotFoundException {
        var city = service.getCityById(request.getId());
        return new ResponseEntity<>(service.updateCity(city, request.getName(), request.getPhoto()), new HttpHeaders(), HttpStatus.OK);
    }
}
