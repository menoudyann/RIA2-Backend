package com.ymd.labeldetector.service.vision;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public interface ILabelDetector {

    public String analyze(URL remoteFullPath) throws IOException, URISyntaxException;

    public String analyze(URL remoteFullPath, int maxLabels) throws IOException, URISyntaxException;

    public String analyze(URL remoteFullPath, float minConfidenceLevel) throws IOException, URISyntaxException;

    public String analyze(URL remoteFullPath, int maxLabels, float minConfidenceLevel) throws IOException, URISyntaxException;
}

