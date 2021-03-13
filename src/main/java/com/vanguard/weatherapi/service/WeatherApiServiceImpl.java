package com.vanguard.weatherapi.service;

import java.time.Duration;

import com.vanguard.weatherapi.entity.WeatherInfo;
import com.vanguard.weatherapi.json.Response;
import com.vanguard.weatherapi.repository.WeatherInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherApiServiceImpl implements WeatherApiService {

    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=QUERY&appid=5d9dbc993b839cedd5bfa6dd09aa63cd";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WeatherInfoRepository weatherRepo;

    @Override
    public WeatherInfo fetchCurrentWeather(String city, String country) {
        Response response = restTemplate.getForObject(API_URL.replace("QUERY", city + "," + country), Response.class);
        return weatherRepo.save(new WeatherInfo(city, country, response.getWeather().get(0).getDescription()));
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(3000)).setReadTimeout(Duration.ofMillis(3000)).build();
    }

}
