package com.maybank.spring_boot_app.service;

import com.maybank.spring_boot_app.entity.MaybankStaff;
import com.maybank.spring_boot_app.entity.WeatherResponse;
import com.maybank.spring_boot_app.repository.MBStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MBStaffService {

    @Autowired
    private MBStaffRepository mbStaffRepository;
    @Autowired
    private ExternalWeatherService externalWeatherService;


    @Transactional
    public MaybankStaff createEntity(MaybankStaff entity) {
        return mbStaffRepository.save(entity);
    }

    @Transactional
    public MaybankStaff updateEntity(MaybankStaff entity) {
        return mbStaffRepository.save(entity);
    }

    public Optional<MaybankStaff> getMaybankStaffById(Long id) {
        return mbStaffRepository.findById(id);
    }

    public Page<MaybankStaff> getEntities(Pageable pageable) {
        return mbStaffRepository.findAll(pageable);
    }

    public WeatherResponse getWeatherInfo(Long id, String zip, String countryCode) {
        MaybankStaff maybankStaff = mbStaffRepository.findById(id).orElseThrow();
        String currentWeatherByLocation = externalWeatherService.getCurrentWeatherByLocation(maybankStaff.getLocation());
        String currentWeatherByZipCountryCode = externalWeatherService.getCurrentWeatherByZipCountryCode(zip, countryCode);
        return new WeatherResponse(maybankStaff.getLocation(), currentWeatherByLocation, currentWeatherByZipCountryCode);
    }
}

