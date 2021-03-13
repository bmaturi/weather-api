package com.vanguard.weatherapi.service;

import com.vanguard.weatherapi.entity.WeatherInfo;

public interface WeatherApiService {

    public WeatherInfo fetchCurrentWeather(String city, String country);

}
