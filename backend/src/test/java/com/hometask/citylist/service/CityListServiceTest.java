package com.hometask.citylist.service;

import com.hometask.citylist.model.City;
import com.hometask.citylist.repository.CityRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Elnur Mammadov
 */

@ExtendWith(MockitoExtension.class)
class CityListServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private CityListService cityListService;

    @Test
    void testGetAllCities() {
        // Arrange
        List<City> cities = Arrays.asList(
            City.builder()
                .id(1L)
                .name("Baku")
                .photo("https://example.com/baku")
                .build(),
            City.builder()
                .id(1L)
                .name("London")
                .photo("https://example.com/london")
                .build());
        Page<City> page = new PageImpl<>(cities);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        when(cityRepository.findAll(pageable)).thenReturn(page);

        // Act
        List<City> result = cityListService.getAllCities(0, 10);

        // Assert
        assertEquals(cities, result);
    }

    @Test
    void testGetCityByName() {
        // Arrange
        String name = "Baku";
        List<City> cities = Collections.singletonList(City.builder()
            .id(1L)
            .name("Baku")
            .photo("https://example.com/baku")
            .build());
        Page<City> page = new PageImpl<>(cities);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        when(cityRepository.getCitiesByName(pageable, name)).thenReturn(page);

        // Act
        List<City> result = cityListService.getCityByName(0, 10, name);

        // Assert
        assertEquals(cities, result);
    }

    @Test
    void testUpdateCity() {
        // Arrange
        City city = City.builder()
            .id(1L)
            .name("Baku")
            .photo("https://example.com/baku")
            .build();
        String name = "Updated Name";
        String photo = "https://example.com/updated-photo.jpg";

        when(validationService.validateAndSave(cityRepository, city)).thenReturn(city);

        // Act
        City result = cityListService.updateCity(city, name, photo);

        // Assert
        assertEquals(name, result.getName());
        assertEquals(photo, result.getPhoto());
        verify(validationService).validateAndSave(cityRepository, city);
    }

    @Test
    void testGetCityById() throws NotFoundException {
        // Arrange
        City city = City.builder()
            .id(1L)
            .name("Baku")
            .photo("https://example.com/baku")
            .build();
        Long id = 1L;

        when(cityRepository.findById(id)).thenReturn(Optional.of(city));

        // Act
        City result = cityListService.getCityById(id);

        // Assert
        assertEquals(city, result);
    }

    @Test
    void testGetCityByIdNotFound() {
        // Arrange
        Long id = 1L;

        when(cityRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NotFoundException.class, () -> {
            // Act
            cityListService.getCityById(id);
        });
    }
}
