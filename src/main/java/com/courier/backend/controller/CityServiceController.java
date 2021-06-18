package com.courier.backend.controller;

import com.courier.backend.beans.*;
import com.courier.backend.dto.RateCalculator;
import com.courier.backend.repo.*;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city-service")
public class CityServiceController {

    @Autowired
    private CityRepo cityRepo;
    @Autowired
    private ServiceRepo serviceRepo;
    @Autowired
    private WeightRepo weightRepo;

    @Autowired
    private ZoneRepo zoneRepo;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private CityServiceRepo cityServiceRepo;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(cityServiceRepo.findByOrderByIdAsc());
    }


    @PostMapping("/calculate")
    public ResponseEntity calculatePrice(@RequestBody RateCalculator city){
        if(city.getCityId() == null){
            return globalService.getErrorResponse("Select City!");
        }
        if(city.getServiceId() == null){
            return globalService.getErrorResponse("Select Service!");
        }
        if(city.getWeight() < 0){
            return globalService.getErrorResponse("Invalid Weight!");
        }

        double weight = 0.0;
        if(city.getVolume() != null &&  city.getVolume() == true){
            if(city.getHeight() > 0 && city.getWidth() > 0 && city.getLength() > 0 ){
                weight = (city.getHeight() * city.getWidth() * city.getLength()) / 5000;
            }
        }else{
            weight = city.getWeight();
        }

        Cities cities = cityRepo.getOne(city.getCityId());
        Service service = serviceRepo.getOne(city.getServiceId());

        CityServices price = new CityServices();

        List<CityServices> list = cityServiceRepo.findByServiceAndZoneId(service.getId() , cities.getZoneId());
        for (CityServices i: list) {
            if(i.getStartWeight() != null && i.getEndWeight() != null ){
                if(Double.compare(i.getStartWeight() , weight) <= 0 && Double.compare(i.getEndWeight() , weight) >= 0) {
                    price = i;
                    break;
                }
            }
             if(i.getDefaultValue() != null && i.getDefaultValue() == true ){
               price = i;
           }
        }

            return globalService.getSuccessResponse(price);
    }



    @PostMapping
    public ResponseEntity add(@RequestBody CityServices city){
        if(city.getZoneId() == null){
            return globalService.getErrorResponse("Select zone!");
        }
        if(city.getServiceId() == null){
            return globalService.getErrorResponse("Select Service!");
        }
        if(city.getWeightId() == null){
            return globalService.getErrorResponse("Select Weight!");
        }

        Zone zone = zoneRepo.getOne(city.getZoneId());
        Service service = serviceRepo.getOne(city.getServiceId());
        Weight weight = weightRepo.getOne(city.getWeightId());

        city.setZone(zone.getZone());
        city.setService(service.getService());

        if(weight.getDefaultCheck() != null && weight.getDefaultCheck() == true){
            city.setDefaultValue(true);
            city.setWeight(weight.getDefaultWeight() + " (Default)");
        }else{
            city.setDefaultValue(false);
            city.setWeight(weight.getStartWeight() + " - " +  weight.getEndWeight());
            city.setStartWeight(weight.getStartWeight());
            city.setEndWeight(weight.getEndWeight());
        }




        return globalService.getSuccessResponse(cityServiceRepo.save(city));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        cityServiceRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody CityServices city){
        if(city.getId() == null){
            return globalService.getErrorResponse("Invalid request!");
        }
        CityServices db = cityServiceRepo.getOne(city.getId());

        if(city.getZone() != null){
            Zone zone = zoneRepo.getOne(city.getZoneId());
            db.setZoneId(zone.getId());
            db.setZone(zone.getZone());
        }
        if(city.getServiceId() == null){
            Service service = serviceRepo.getOne(city.getServiceId());
            db.setServiceId(service.getId());
            db.setService(service.getService());
        }
        if(city.getWeightId() == null){
            Weight weight = weightRepo.getOne(city.getWeightId());
            db.setWeightId(weight.getId());
            city.setWeight(weight.getStartWeight() + " - " +  weight.getEndWeight());
        }
        if(city.getRate() > 0){

            db.setRate(city.getRate());
        }

        return globalService.getSuccessResponse(cityServiceRepo.save(db));
    }
}
