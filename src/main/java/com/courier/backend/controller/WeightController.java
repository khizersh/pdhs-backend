package com.courier.backend.controller;

import com.courier.backend.beans.Service;
import com.courier.backend.beans.Weight;
import com.courier.backend.repo.ServiceRepo;
import com.courier.backend.repo.WeightRepo;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weight")
public class WeightController {
    @Autowired
    private WeightRepo weightRepo;

    @Autowired
    private GlobalService globalService;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(weightRepo.findByOrderByIdAsc());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Weight weight){
        if(weight.getStartWeight() < 0){
            return globalService.getErrorResponse("Invalid weight!");
        }
        if(weight.getEndWeight() < 0){
            return globalService.getErrorResponse("Invalid weight!");
        }
        if(weight.getDefaultCheck()){
            if(weight.getDefaultWeight() < 0){
                return globalService.getErrorResponse("Invalid weight!");
            }
            weight.setStartWeight(null);
            weight.setEndWeight(null);
        }
        return globalService.getSuccessResponse(weightRepo.save(weight));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        weightRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Weight weight){
        Weight db = weightRepo.getOne(weight.getId());
        if(db == null){
            return globalService.getErrorResponse("Invalid request!");
        }
        if(weight.getStartWeight() != null){
            db.setStartWeight(weight.getStartWeight());
        }
        if(weight.getEndWeight() != null){
            db.setEndWeight(weight.getEndWeight());
        }

        return globalService.getSuccessResponse(weightRepo.save(db));
    }
}
