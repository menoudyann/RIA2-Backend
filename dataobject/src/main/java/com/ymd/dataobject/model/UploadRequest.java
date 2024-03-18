package com.ymd.dataobject.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadRequest {

    private MultipartFile file;
    private String filename;
}
