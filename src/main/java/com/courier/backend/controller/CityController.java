package com.courier.backend.controller;

import com.courier.backend.beans.Cities;
import com.courier.backend.beans.Zone;
import com.courier.backend.repo.CityRepo;
import com.courier.backend.repo.ZoneRepo;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private ZoneRepo zoneRepo;

    @Autowired
    private GlobalService globalService;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(cityRepo.findAll());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Cities cities){
        if(cities.getCity().isEmpty()){
            return globalService.getErrorResponse("Enter City!");
        }

        if(cities.getZoneId() == null){
            return globalService.getErrorResponse("Select Zone!");
        }
        Zone zone = zoneRepo.getOne(cities.getZoneId());
        if(zone == null){
            return globalService.getErrorResponse("Invalid zone!");

        }
        cities.setZone(zone.getZone());
        return globalService.getSuccessResponse(cityRepo.save(cities));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        cityRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Cities cities){
        Cities db = cityRepo.getOne(cities.getId());
        if(db == null){
            return globalService.getErrorResponse("Invalid request!");
        }
        if(!cities.getCity().isEmpty()){
            db.setCity(cities.getCity());
        }
        Zone zone = zoneRepo.getOne(cities.getZoneId());
        if(cities.getZoneId() != null){
            db.setZoneId(cities.getZoneId());
            db.setZone(zone.getZone());
        }
        return globalService.getSuccessResponse(cityRepo.save(db));
    }
}
