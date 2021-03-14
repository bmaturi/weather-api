package com.vanguard.weatherapi;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.vanguard.weatherapi.entity.WeatherInfo;
import com.vanguard.weatherapi.exception.ErrorResponse;
import com.vanguard.weatherapi.request.WeatherRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping(value = "/current", method = RequestMethod.GET, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<MappingJacksonValue> getCurrentWeather(
			@RequestHeader(name = "X-TOKEN", required = true) String token, @RequestBody WeatherRequest req) {

		if (!StringUtils.hasLength(req.getCity()) || !StringUtils.hasLength(req.getCountry())
				|| !StringUtils.hasLength(token)) {
			return handleAppErrors(HttpStatus.BAD_REQUEST, "Invalid Request. City/Country/Token is mandatory.");
		}

		Bucket bucket = bucketService.resolveBucket(token);
		if (bucket == null) {
			return handleAppErrors(HttpStatus.BAD_REQUEST, "Invalid Request");
		}

		ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
		if (probe.isConsumed()) {

			WeatherInfo info = service.fetchCurrentWeather(req.getCity(), req.getCountry());
			if (info == null) {
				return handleAppErrors(HttpStatus.NOT_FOUND, "Weather info for city/country not found.");
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

	// Handle app error response
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final ResponseEntity<MappingJacksonValue> handleAppErrors(HttpStatus status, String message) {
		List<String> details = new ArrayList<>();
		details.add(message);
		ErrorResponse error = new ErrorResponse("Encountered error.", details);
		return new ResponseEntity(error, status);
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiApplication.class, args);
	}

}
