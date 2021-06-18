package com.courier.backend.controller;

import com.courier.backend.beans.AmazonClient;
import com.courier.backend.beans.Banner;
import com.courier.backend.beans.Offers;
import com.courier.backend.repo.OfferRepo;
import com.courier.backend.utils.FileStorageService;
import com.courier.backend.utils.GlobalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/offer")
public class OfferController {


    @Autowired
    private OfferRepo offerRepo;
    @Autowired
    private GlobalService service;
    @Autowired
    private FileStorageService fileService;

    @Autowired
    private AmazonClient amazonClient;

    @GetMapping
    public ResponseEntity getAll(){
        return service.getSuccessResponse(offerRepo.findAll());
    }


    @PostMapping
    public ResponseEntity add(@RequestParam String offer, @RequestParam MultipartFile file   ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Offers offers = mapper.readValue(offer, Offers.class);
        if(offers.getTitle().isEmpty()){
            return service.getErrorResponse("Enter title!");
        }
        if(offers.getDescription().isEmpty()){
            return service.getErrorResponse("Enter description!");
        }
        if(file == null){
            return service.getErrorResponse("Select image!");
        }
        String img = amazonClient.uploadFile(file);
        offers.setImage(img);
        return service.getSuccessResponse(offerRepo.save(offers));
    }

    @PutMapping()
    public ResponseEntity edit(@RequestParam String offer, @RequestParam(value = "file" , required = false) MultipartFile file   ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Offers offers = mapper.readValue(offer, Offers.class);
        if(offers.getId() == null){
            return service.getErrorResponse("Invalid request!");
        }
        Offers db = offerRepo.getOne(offers.getId());
        if(!offers.getTitle().isEmpty()){
            db.setTitle(offers.getTitle());
        }
        if(!offers.getDescription().isEmpty()){
            db.setDescription(offers.getDescription());
        }

        if(file != null){
            String img = amazonClient.uploadFile(file);
            offers.setImage(img);
        }

        return service.getSuccessResponse(offerRepo.save(offers));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteBanner(@PathVariable Integer id  ) throws IOException {

        if(id == null){
            return service.getErrorResponse("Invalid request!");
        }
        if(!offerRepo.existsById(id)){
            return service.getErrorResponse("Invalid request!");
        }
        Offers off = offerRepo.getOne(id);
        try{
        amazonClient.deleteFileFromS3Bucket(off.getImage());

        }catch (Exception e){

        }

        offerRepo.deleteById(id);

        return service.getSuccessResponse("Delete successfully!");
    }
}
