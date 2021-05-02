package com.courier.backend.controller;

import com.courier.backend.beans.Service;
import com.courier.backend.beans.Zone;
import com.courier.backend.repo.ServiceRepo;
import com.courier.backend.repo.ZoneRepo;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private GlobalService globalService;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(serviceRepo.findByOrderByIdAsc());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Service service){
        if(service.getService().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }
        return globalService.getSuccessResponse(serviceRepo.save(service));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        serviceRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Service service){
        Service db = serviceRepo.getOne(service.getId());
        if(db == null){
            return globalService.getErrorResponse("Invalid request!");
        }
        if(!service.getService().isEmpty()){
            db.setService(service.getService());
        }

        return globalService.getSuccessResponse(serviceRepo.save(db));
    }
}
