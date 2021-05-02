package com.courier.backend.controller;

import com.courier.backend.beans.Headlines;
import com.courier.backend.beans.News;
import com.courier.backend.repo.HeadlineRepo;
import com.courier.backend.repo.NewsRepo;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/headline")
public class HeadlineController {

    @Autowired
    private GlobalService globalService;
    @Autowired
    private HeadlineRepo headlineRepo;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(headlineRepo.findAll());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Headlines head){
        if(head.getHeadline().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }

        return globalService.getSuccessResponse(headlineRepo.save(head));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        headlineRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Headlines head){
        Headlines db = headlineRepo.getOne(head.getId());
        if(db == null){
            return globalService.getErrorResponse("Invalid request!");
        }
        if(!head.getHeadline().isEmpty()){
            db.setHeadline(head.getHeadline());
        }

        return globalService.getSuccessResponse(headlineRepo.save(db));
    }
}
