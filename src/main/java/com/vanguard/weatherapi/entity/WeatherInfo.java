package com.vanguard.weatherapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity
@JsonFilter("userFilter")
public class WeatherInfo {

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;

    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "DESCRIPTION")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WeatherInfo(String cityName, String country, String description) {
        this.cityName = cityName;
        this.country = country;
        this.description = description;
    }

}
