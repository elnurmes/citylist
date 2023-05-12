package com.hometask.citylist.controller;

import com.hometask.citylist.model.City;
import com.hometask.citylist.service.CityListService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Elnur Mammadov
 */
@ExtendWith(MockitoExtension.class)
class CitylistControllerTest {

    @Mock
    private CityListService service;

    @InjectMocks
    private CityListController controller;

    @Test
    void getAllCities_ReturnsListOfCities() {
        // Arrange
        List<City> cities = new ArrayList<>();
        cities.add(City.builder()
                .id(1L)
                .name("Baku")
                .photo("http:/google.com/123")
            .build());
        cities.add(City.builder()
            .id(2L)
            .name("London")
            .photo("http:/google.com/456")
            .build());
        when(service.getAllCities(0, 10)).thenReturn(cities);

        // Act
        ResponseEntity<List<City>> response = controller.getAllCities(0, 10);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Baku");
        assertThat(response.getBody().get(1).getName()).isEqualTo("London");
    }

    @Test
    void getCityByName_ReturnsListOfCities() {
        // Arrange
        List<City> cities = new ArrayList<>();
        cities.add(City.builder()
            .id(1L)
            .name("Baku")
            .photo("http:/google.com/123")
            .build());
        when(service.getCityByName(0, 10, "London")).thenReturn(cities);

        // Act
        ResponseEntity<List<City>> response = controller.getCityByName(0, 10, "London");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Baku");
    }

    @Test
    void updateCity_ReturnsUpdatedCity() throws NotFoundException {
        // Arrange
        City city = City.builder()
                .id(1L)
                .name("Baku")
                .photo("http:/google.com/123")
            .build();
        city.setId(1L);
        City updatedCity = City.builder()
            .name("Bakou")
            .build();
        updatedCity.setId(1L);
        when(service.getCityById(1L)).thenReturn(city);
        when(service.updateCity(city, "Bakou", null)).thenReturn(updatedCity);
        City request = City.builder()
            .name("Bakou")
            .build();
        request.setId(1L);

        // Act
        ResponseEntity<City> response = controller.updateCity(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Bakou");
    }
}
