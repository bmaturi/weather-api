package com.vanguard.weatherapi;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.vanguard.weatherapi.entity.WeatherInfo;
import com.vanguard.weatherapi.service.BucketService;
import com.vanguard.weatherapi.service.WeatherApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@RestController
@RequestMapping(value = "/weather")
@SpringBootApplication
public class WeatherApiApplication {

	@Autowired
	WeatherApiService service;

	@Autowired
	BucketService bucketService;

	@RequestMapping(value = "/current", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<MappingJacksonValue> getCurrentWeather(
			@RequestHeader(name = "X-TOKEN", required = true) String token, @RequestParam(value = "city") String city,
			@RequestParam(value = "country") String country) {

		if (!StringUtils.hasLength(city) || !StringUtils.hasLength(country) || !StringUtils.hasLength(token)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		Bucket bucket = bucketService.resolveBucket(token);
		if (bucket == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
		if (probe.isConsumed()) {

			WeatherInfo info = service.fetchCurrentWeather(city, country);
			if (info == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);

			}

			MappingJacksonValue wrapper = new MappingJacksonValue(info);
			wrapper.setFilters(new SimpleFilterProvider().addFilter("userFilter",
					SimpleBeanPropertyFilter.filterOutAllExcept("description")));
			return ResponseEntity.ok(wrapper);
		}

		long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
				.header("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill)).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiApplication.class, args);
	}

}
