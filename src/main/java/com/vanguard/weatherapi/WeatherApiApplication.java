package com.vanguard.weatherapi;

import com.vanguard.weatherapi.entity.WeatherInfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/weather")
@SpringBootApplication
public class WeatherApiApplication {

	@RequestMapping(value = "/current/{city}/{country}", method = RequestMethod.GET)
	public ResponseEntity<WeatherInfo> getCurrentWeather(@PathVariable(value = "city") String city,
			@PathVariable(value = "country") String country) {
		return new ResponseEntity<WeatherInfo>(HttpStatus.OK);
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiApplication.class, args);
	}

}
