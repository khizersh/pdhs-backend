package com.courier.backend.controller;

import com.courier.backend.beans.*;
import com.courier.backend.dto.RateCalculator;
import com.courier.backend.repo.*;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        Cities cities = cityRepo.getOne(city.getCityId());
        Service service = serviceRepo.getOne(city.getServiceId());


        double weight = 0.0;
        if(city.getVolume()){
            if(city.getHeight() > 0 && city.getWidth() > 0 && city.getLength() > 0 ){
                weight = (city.getHeight() * city.getWidth() * city.getLength()) / 5000;
            }
        }else{
            weight = city.getWeight();
        }

        CityServices price = new CityServices();
        CityServices onePrice = new CityServices();
        for (CityServices i:cityServiceRepo.findByServiceIdAndCityId(service.getId() , cities.getId())) {
            if( i.getWeight().equals(weight)){
                price = i;

            }
        }

        System.out.println("price: "+ price );

        if(price.getId()  == null){
            for (CityServices i:cityServiceRepo.findByServiceIdAndCityId(service.getId() , cities.getId())) {
                if( i.getWeight().equals(1.0)){
                    onePrice = i;

                }
            }
            if(onePrice.getId() != null){

            price.setRate(onePrice.getRate() * weight);
            }
        }
        System.out.println("onePrice: "+ onePrice );

            return globalService.getSuccessResponse(price);
    }



    @PostMapping
    public ResponseEntity add(@RequestBody CityServices city){
        if(city.getCityId() == null){
            return globalService.getErrorResponse("Select City!");
        }
        if(city.getServiceId() == null){
            return globalService.getErrorResponse("Select Service!");
        }
        if(city.getWeightId() == null){
            return globalService.getErrorResponse("Select Weight!");
        }

        Cities cities = cityRepo.getOne(city.getCityId());
        Service service = serviceRepo.getOne(city.getServiceId());
        Weight weight = weightRepo.getOne(city.getWeightId());

        city.setCity(cities.getCity());
        city.setService(service.getService());
        city.setWeight(weight.getWeight());

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

        if(city.getCityId() != null){
            Cities cities = cityRepo.getOne(city.getCityId());
            db.setCityId(cities.getId());
            db.setCity(cities.getCity());
        }
        if(city.getServiceId() == null){
            Service service = serviceRepo.getOne(city.getServiceId());
            db.setServiceId(service.getId());
            db.setService(service.getService());
        }
        if(city.getWeightId() == null){
            Weight weight = weightRepo.getOne(city.getWeightId());
            db.setWeightId(weight.getId());
            db.setWeight(weight.getWeight());
        }
        if(city.getRate() > 0){

            db.setRate(city.getRate());
        }



        return globalService.getSuccessResponse(cityServiceRepo.save(db));
    }
}
