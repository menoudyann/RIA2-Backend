package com.ymd.dataobject.model;

import lombok.Data;

@Data
public class PublishRequest {

    private String remoteFullPath;
    private int expirationTime = 90;
}
