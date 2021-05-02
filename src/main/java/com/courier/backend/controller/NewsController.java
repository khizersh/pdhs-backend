package com.courier.backend.controller;

import com.courier.backend.beans.News;
import com.courier.backend.beans.Zone;
import com.courier.backend.repo.NewsRepo;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {


    @Autowired
    private GlobalService globalService;
    @Autowired
    private NewsRepo newsRepo;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(newsRepo.findAll());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody News news){
        if(news.getTitle().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }
        if(news.getDescription().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }
        return globalService.getSuccessResponse(newsRepo.save(news));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        newsRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody News news){
        News db = newsRepo.getOne(news.getId());
        if(db == null){
            return globalService.getErrorResponse("Invalid request!");
        }
        if(!news.getTitle().isEmpty()){
            db.setTitle(news.getTitle());
        }
        if(!news.getDescription().isEmpty()){
            db.setDescription(news.getDescription());
        }
        return globalService.getSuccessResponse(newsRepo.save(db));
    }
}
