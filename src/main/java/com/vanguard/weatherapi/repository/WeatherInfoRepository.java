package com.vanguard.weatherapi.repository;

import com.vanguard.weatherapi.entity.WeatherInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {

}
