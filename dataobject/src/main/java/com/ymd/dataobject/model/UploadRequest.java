package com.ymd.dataobject.model;

import lombok.Data;

@Data
public class UploadRequest {

    private String localFullPath;
    private String remoteFullPath;
}
