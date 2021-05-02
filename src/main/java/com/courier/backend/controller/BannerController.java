package com.courier.backend.controller;

import com.courier.backend.beans.Banner;
import com.courier.backend.repo.BannerRepo;
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
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    private BannerRepo bannerRepo;
    @Autowired
    private GlobalService service;
    @Autowired
    private FileStorageService fileService;

    @GetMapping
    public ResponseEntity getAll(){
        return service.getSuccessResponse(bannerRepo.findAll());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping
    public ResponseEntity add(@RequestParam String banner, @RequestParam MultipartFile file   ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Banner ban = mapper.readValue(banner, Banner.class);
        if(ban.getTitle().isEmpty()){
            return service.getErrorResponse("Enter title!");
        }
        if(ban.getDescription().isEmpty()){
            return service.getErrorResponse("Enter description!");
        }
        if(file == null){
            return service.getErrorResponse("Select image!");
        }
        String img = fileService.storeAndReturnFile(file);
        ban.setImage(img);
        return service.getSuccessResponse(bannerRepo.save(ban));
    }

    @PutMapping()
    public ResponseEntity edit(@RequestParam String banner, @RequestParam(value = "file" , required = false) MultipartFile file   ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Banner ban = mapper.readValue(banner, Banner.class);
        if(ban.getId() == null){
            return service.getErrorResponse("Invalid request!");
        }
        Banner db = bannerRepo.getOne(ban.getId());
        if(!ban.getTitle().isEmpty()){
            db.setTitle(ban.getTitle());
        }
        if(!ban.getDescription().isEmpty()){
            db.setDescription(ban.getDescription());
        }
        if(!ban.getUrl().isEmpty()){
            db.setUrl(ban.getUrl());
        }
        if(file != null){
            String img = fileService.storeAndReturnFile(file);
            ban.setImage(img);
        }

        return service.getSuccessResponse(bannerRepo.save(ban));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteBanner(@PathVariable Integer id  ) throws IOException {

        if(id == null){
            return service.getErrorResponse("Invalid request!");
        }
        if(!bannerRepo.existsById(id)){
            return service.getErrorResponse("Invalid request!");
        }

        bannerRepo.deleteById(id);

        return service.getSuccessResponse("Delete successfully!");
    }

}
