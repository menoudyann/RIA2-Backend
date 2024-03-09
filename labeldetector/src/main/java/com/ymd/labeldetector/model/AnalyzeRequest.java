package com.ymd.labeldetector.model;

import lombok.*;

@Data
public class AnalyzeRequest {

    private String remoteFullPath;
    private int maxLabels = 10;
    private float minConfidenceLevel = 90;
}
