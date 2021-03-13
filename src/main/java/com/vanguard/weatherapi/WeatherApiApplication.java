package com.vanguard.weatherapi;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.vanguard.weatherapi.entity.WeatherInfo;
import com.vanguard.weatherapi.service.WeatherApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/weather")
@SpringBootApplication
public class WeatherApiApplication {

	@Autowired
	WeatherApiService service;

	@RequestMapping(value = "/current", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<MappingJacksonValue> getCurrentWeather(
			@RequestHeader(name = "X-TOKEN", required = true) String token, @RequestParam(value = "city") String city,
			@RequestParam(value = "country") String country) {

		WeatherInfo info = service.fetchCurrentWeather(city, country);

		MappingJacksonValue wrapper = new MappingJacksonValue(info);
		wrapper.setFilters(new SimpleFilterProvider().addFilter("userFilter",
				SimpleBeanPropertyFilter.filterOutAllExcept("description")));
		return ResponseEntity.ok(wrapper);
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiApplication.class, args);
	}

}
