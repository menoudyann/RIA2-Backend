package com.ymd.dataobject.controller;

import com.ymd.dataobject.exception.ObjectNotFoundException;
import com.ymd.dataobject.model.PublishRequest;
import com.ymd.dataobject.model.UploadRequest;
import com.ymd.dataobject.service.storage.GoogleDataObjectImpl;
import com.ymd.dataobject.service.storage.IDataObject;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@RestController
public class DataObjectController {

    private IDataObject dataObject;

    public DataObjectController() {
        this.dataObject = new GoogleDataObjectImpl();
    }

    @GetMapping("/api/v1/dataobject/publish")
    public URL publish(@ModelAttribute PublishRequest request) throws URISyntaxException, ObjectNotFoundException {
        return dataObject.publish(new URI(request.getRemoteFullPath()), request.getExpirationTime());
    }

    @PostMapping("/api/v1/dataobject/upload")
    public void upload(@RequestBody UploadRequest request) throws Exception {
        dataObject.upload(new URI(request.getLocalFullPath()), new URI(request.getRemoteFullPath()));
    }
}
