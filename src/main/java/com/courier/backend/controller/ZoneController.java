package com.courier.backend.controller;

import com.courier.backend.beans.Cities;
import com.courier.backend.beans.Zone;
import com.courier.backend.dto.StationDto;
import com.courier.backend.repo.CityRepo;
import com.courier.backend.repo.ZoneRepo;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/zone")
public class ZoneController {

    @Autowired
    private ZoneRepo zoneRepo;
    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private GlobalService globalService;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(zoneRepo.findByOrderByIdAsc());
    }

    @GetMapping("/station")
    public ResponseEntity getStations(){
        List<StationDto> list = new ArrayList<>();

        for (Zone i:zoneRepo.findByOrderByIdAsc()) {
            StationDto dto = new StationDto();
            dto.setId(i.getId());
            dto.setZone(i.getZone());
            dto.setSubtitle(i.getSubtitle());
            dto.setCityList(cityRepo.findByZoneId(i.getId()));
            list.add(dto);
        }
        List<Integer> size = new ArrayList<>();
        list.forEach(i -> {
            size.add(i.getCityList().size());
        });

        Collections.sort(size);
        int s = size.get(size.size()-1);

        for (StationDto i:list) {

            if(i.getCityList().size() < s){
                int temp = s - i.getCityList().size();

                for (int j=0; j < temp; j++){
                    Cities city = new Cities();
                    i.getCityList().add(city);
                }
            }
        }

        return globalService.getSuccessResponse(list);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Zone zone){
        if(zone.getZone().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }
        return globalService.getSuccessResponse(zoneRepo.save(zone));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        zoneRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Zone zone){
        Zone db = zoneRepo.getOne(zone.getId());
        if(db == null){
            return globalService.getErrorResponse("Invalid request!");
        }
        if(!zone.getZone().isEmpty()){
            db.setZone(zone.getZone());
        }
        if(!zone.getSubtitle().isEmpty()){
            db.setSubtitle(zone.getSubtitle());
        }
        return globalService.getSuccessResponse(zoneRepo.save(db));
    }

}
