package com.ymd.dataobject.controller;

import com.ymd.dataobject.exception.ObjectNotFoundException;
import com.ymd.dataobject.model.PublishRequest;
import com.ymd.dataobject.model.UploadRequest;
import com.ymd.dataobject.service.storage.GoogleDataObjectImpl;
import com.ymd.dataobject.service.storage.IDataObject;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class DataObjectController {

    private IDataObject dataObject;
    private Dotenv dotenv;

    public DataObjectController() {
        this.dataObject = new GoogleDataObjectImpl();
        dotenv = Dotenv.load();
    }

    @GetMapping("/api/v1/dataobject/publish")
    public URL publish(@ModelAttribute PublishRequest request) throws URISyntaxException, ObjectNotFoundException {
        return dataObject.publish(new URI(request.getRemoteFullPath()), request.getExpirationTime());
    }

    @PostMapping( "/api/v1/dataobject/upload")
    public ResponseEntity<?> upload(@ModelAttribute UploadRequest uploadRequest) throws Exception {
        Map<String, Object> response = new HashMap<>();
        String filename = uploadRequest.getFilename();

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
        String formattedDateTime = currentDateTime.format(formatter);
        String remoteFullPathString = dotenv.get("GOOGLE_BUCKET_URI") + formattedDateTime + uploadRequest.getFilename().substring(filename.lastIndexOf("."));
        URI remoteFullPath = new URI(remoteFullPathString);
        MultipartFile file = uploadRequest.getFile();
        byte[] bytes = file.getBytes();
        if (dataObject.doesExist(remoteFullPath)) {
            response.put("message", "File already exists");
            return ResponseEntity.unprocessableEntity().body(response);
        }
        dataObject.upload(bytes, remoteFullPath);
        response.put("message", "File uploaded successfully");
        response.put("remoteFullPath", remoteFullPath);
        return ResponseEntity.ok().body(response);
    }
}

