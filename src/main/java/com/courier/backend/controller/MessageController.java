package com.courier.backend.controller;

import com.courier.backend.beans.CustomerMessage;
import com.courier.backend.beans.Headlines;
import com.courier.backend.beans.MessageStatus;
import com.courier.backend.beans.News;
import com.courier.backend.repo.MessageRepo;
import com.courier.backend.repo.NewsRepo;
import com.courier.backend.utils.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private GlobalService globalService;
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping
    public ResponseEntity getAll(){
        return globalService.getSuccessResponse(messageRepo.findAll());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody CustomerMessage message){
        if(message.getEmail().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }
        if(message.getName().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }
        if(message.getMessage().isEmpty()){
            return globalService.getErrorResponse("Enter all fields");
        }
        message.setStatus(MessageStatus.Pending);
        return globalService.getSuccessResponse(messageRepo.save(message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        if(id == null){
            return globalService.getErrorResponse("Enter all fields");
        }
        messageRepo.deleteById(id);
        return globalService.getSuccessResponse("Delete Successfully!");
    }

    @PutMapping
    public ResponseEntity update(@RequestBody CustomerMessage message){
        CustomerMessage db = messageRepo.getOne(message.getId());
        if(db.getStatus().equals(MessageStatus.Pending)){
            db.setStatus(MessageStatus.Done);
        }

        return globalService.getSuccessResponse(messageRepo.save(db));
    }
}
