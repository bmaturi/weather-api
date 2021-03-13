package com.vanguard.weatherapi.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    private List<Weather> weather;

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Response [weather=" + weather + "]";
    }

}
