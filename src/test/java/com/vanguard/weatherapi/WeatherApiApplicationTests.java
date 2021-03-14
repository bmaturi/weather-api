package com.vanguard.weatherapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class WeatherApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getWeatherInfo_WithAllParamsAndCorrectToken() throws Exception {
		String weatherReq = "{\"city\": \"sydney\", \"country\" : \"AUS\"}";
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token-1")
				.content(weatherReq)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void getWeatherInfo_WithAllParamsAndIncorrectToken() throws Exception {
		String weatherReq = "{\"city\": \"sydney\", \"country\" : \"AUS\"}";
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void getWeatherInfo_WithMissingCity() throws Exception {
		String weatherReq = "{\"city\": \"\", \"country\" : \"AUS\"}";
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void getWeatherInfo_WithJunkCityCountry() throws Exception {
		String weatherReq = "{\"city\": \"xyz\", \"country\" : \"ABC\"}";
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void getWeatherInfo_WithRequestExceedingThrottle() throws Exception {
		String weatherReq = "{\"city\": \"\", \"country\" : \"ABC\"}";
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token-2")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token-2")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token-2")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token-2")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token-2")
				.content(weatherReq)).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		// Bucket will be full from the previous 5 requests so should throw a 429
		mockMvc.perform(get("/weather/current").contentType(MediaType.APPLICATION_JSON).header("X-TOKEN", "token-2")
				.content(weatherReq)).andExpect(status().isTooManyRequests())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

}
