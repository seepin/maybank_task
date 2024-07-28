package com.maybank.spring_boot_app.entity;

public class WeatherResponse {
    private String location;
    private String currentWeatherByLocation;
    private String currentWeatherByZipCountryCode;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrentWeatherByLocation() {
        return currentWeatherByLocation;
    }

    public void setCurrentWeatherByLocation(String currentWeatherByLocation) {
        this.currentWeatherByLocation = currentWeatherByLocation;
    }

    public String getCurrentWeatherByZipCountryCode() {
        return currentWeatherByZipCountryCode;
    }

    public void setCurrentWeatherByZipCountryCode(String currentWeatherByZipCountryCode) {
        this.currentWeatherByZipCountryCode = currentWeatherByZipCountryCode;
    }

    public WeatherResponse() {}

    public WeatherResponse(String location, String currentWeatherByLocation, String currentWeatherByZipCountryCode) {
        this.location = location;
        this.currentWeatherByLocation = currentWeatherByLocation;
        this.currentWeatherByZipCountryCode = currentWeatherByZipCountryCode;
    }


}