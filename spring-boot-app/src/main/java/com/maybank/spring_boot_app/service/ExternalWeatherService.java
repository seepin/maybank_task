package com.maybank.spring_boot_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalWeatherService {

    @Value("${weather.api.key}")
    private String apiKey;
    @Value("${weather.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;
    public ExternalWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCurrentWeatherByLocation(String location) {
        String url = apiUrl + "?q=" + location + "&appid=" + apiKey;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "Error fetching current weather data";
        }
    }

    public String getCurrentWeatherByZipCountryCode(String zip, String countryCode) {
        String url = apiUrl + "?zip=" + zip + "," + countryCode + "&appid=" + apiKey;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "Error fetching historical weather data";
        }
    }
}
