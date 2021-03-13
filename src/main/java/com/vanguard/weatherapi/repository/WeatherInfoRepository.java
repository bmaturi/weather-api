package com.vanguard.weatherapi.repository;

import com.vanguard.weatherapi.entity.WeatherInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {

}
