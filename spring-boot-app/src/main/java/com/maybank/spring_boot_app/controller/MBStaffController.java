package com.maybank.spring_boot_app.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maybank.spring_boot_app.entity.MaybankStaff;
import com.maybank.spring_boot_app.entity.WeatherResponse;
import com.maybank.spring_boot_app.service.MBStaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
    @RequestMapping("/api/mb")
public class MBStaffController {

    private static final Logger log = LoggerFactory.getLogger(MBStaffController.class);
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @Autowired
    private MBStaffService mbStaffService;
    @Autowired
    private PagedResourcesAssembler<MaybankStaff> pagedResourcesAssembler;

    @PostMapping("/create")
    public ResponseEntity<?> createMBStaff(@RequestBody MaybankStaff entity) {
        try {
            if (entity != null && entity.getName() != null && !entity.getName().isEmpty()) {
                if (entity.getId() != null) {
                    Optional<MaybankStaff> maybankStaffOpt = mbStaffService.getMaybankStaffById(entity.getId());
                    if (maybankStaffOpt.isPresent()) {
                        log.info("Duplicated key! Entity with ID {} already exists.", entity.getId());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicated key! Entity with the provided ID already exists.");
                    }
                }
                log.info("createEntity MB Request - {}" , gson.toJson(entity));
                MaybankStaff maybankStaff = mbStaffService.createEntity(entity);
                log.info("createEntity MB Response - {}" , gson.toJson(maybankStaff));
                return ResponseEntity.status(HttpStatus.CREATED).body(maybankStaff);
            } else {
                log.info("Invalid input: required fields are missing or empty.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: required fields are missing or empty.");
            }
        } catch (Exception e) {
            System.err.println("Error creating entity: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating entity: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateMBStaff(@PathVariable Long id, @RequestBody MaybankStaff entity) {
        try {
            Optional<MaybankStaff> existingStaffOpt = mbStaffService.getMaybankStaffById(id);
            if (existingStaffOpt.isPresent()) {
                MaybankStaff existingStaff = existingStaffOpt.get();
                if (entity.getName() != null && !entity.getName().isEmpty()) {
                    existingStaff.setName(entity.getName());
                }
                if (entity.getLocation() != null && !entity.getLocation().isEmpty()) {
                    existingStaff.setLocation(entity.getLocation());
                }
                log.info("updateEntity MB Request- ID={}, {} ", id, gson.toJson(entity));
                MaybankStaff updatedStaff = mbStaffService.updateEntity(existingStaff);
                log.info("updateEntity MB Response- {} ", gson.toJson(updatedStaff));
                return ResponseEntity.ok(updatedStaff);
            } else {
                log.info("Record not found for ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for ID: " + id);
            }
        } catch (Exception e) {
            log.error("Error updating entity: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating entity: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public PagedModel<EntityModel<MaybankStaff>> getMBStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<MaybankStaff> pagedStaffModel;
        try {
            pagedStaffModel = mbStaffService.getEntities(pageable);
        } catch (Exception e) {
            log.error("Error retrieving MB Staff", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving MB Staff", e);
        }

        PagedModel<EntityModel<MaybankStaff>> pagedModel = pagedResourcesAssembler.toModel(pagedStaffModel);
        log.info("getMBStaff MB Request - Page {}, Size {} ", page, size);
        log.info("Staff members retrieved: {}", pagedModel.getContent().size());
        pagedModel.getContent().forEach(entityModel ->
                log.info("Staff member: {}", gson.toJson(entityModel.getContent()))
        );
        PagedModel.PageMetadata metadata = pagedModel.getMetadata();
        log.info("Page number: {}", metadata.getNumber());
        log.info("Total pages: {}", metadata.getTotalPages());
        log.info("Total elements: {}", metadata.getTotalElements());
        return pagedModel;
    }

    @GetMapping("/{id}/weather")
    public ResponseEntity<WeatherResponse> getWeather(
            @PathVariable Long id,
            @RequestParam(defaultValue = "47500") String zip,
            @RequestParam(defaultValue = "MY") String countryCode) {
        log.info("Weather data request - ID: {}, Zip: {}, countryCode: {} ", id, zip, countryCode);
        WeatherResponse response;
        try {
            response = mbStaffService.getWeatherInfo(id, zip, countryCode);
        } catch (Exception e) {
            log.error("Error retrieving weather data for ID: {}, Zip: {}, countryCode: {}", id, zip, countryCode, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving weather data", e);
        }
        log.info("Weather data response: {} ", gson.toJson(response));
        return ResponseEntity.ok(response);
    }

}

