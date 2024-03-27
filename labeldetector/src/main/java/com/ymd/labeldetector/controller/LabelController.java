package com.ymd.labeldetector.controller;

import com.ymd.labeldetector.model.AnalyzeRequest;
import com.ymd.labeldetector.service.vision.GoogleLabelDetectorImpl;
import com.ymd.labeldetector.service.vision.ILabelDetector;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


@RestController
@CrossOrigin
public class LabelController {

    private ILabelDetector labelDetector;

    public LabelController() {
        this.labelDetector = new GoogleLabelDetectorImpl();
    }

    @PostMapping("/api/v1/labeldetector/analyze")
    public String analyze(@RequestBody AnalyzeRequest request) throws IOException, URISyntaxException {
        int maxLabels = request.getMaxLabels();
        float minConfidenceLevel = request.getMinConfidence();
        return labelDetector.analyze(new URL(request.getRemoteFullPath()), maxLabels, minConfidenceLevel);
    }
}
