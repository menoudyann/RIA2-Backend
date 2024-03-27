package com.ymd.labeldetector.service.vision;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.ymd.labeldetector.model.Label;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GoogleLabelDetectorImpl implements ILabelDetector {

    protected ImageAnnotatorClient client;
    protected int defaultMaxLabels;
    protected float defaultMinConfidenceLevel;

    public GoogleLabelDetectorImpl() {
        Dotenv dotenv = Dotenv.load();
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(dotenv.get("GOOGLE_APPLICATION_CREDENTIALS"))).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            client = ImageAnnotatorClient.create(settings);
            defaultMaxLabels = Integer.parseInt(dotenv.get("DEFAULT_MAX_LABELS"));
            defaultMinConfidenceLevel = Float.parseFloat(dotenv.get("DEFAULT_MAX_LABELS"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String analyze(URL remoteFullPath) throws IOException, URISyntaxException {
        return analyze(remoteFullPath, defaultMaxLabels, defaultMinConfidenceLevel);
    }

    @Override
    public String analyze(URL remoteFullPath, int maxLabels) throws IOException, URISyntaxException {
        return analyze(remoteFullPath, maxLabels, defaultMinConfidenceLevel);
    }

    @Override
    public String analyze(URL remoteFullPath, float minConfidenceLevel) throws IOException, URISyntaxException {
        return analyze(remoteFullPath, defaultMaxLabels, minConfidenceLevel);
    }

    @Override
    public String analyze(URL remoteFullPath, int maxLabels, float minConfidenceLevel) throws IOException, URISyntaxException {
        List<Label> labels = new ArrayList<>();
        Gson gson = new Gson();

        List<AnnotateImageRequest> requests = new ArrayList<>();

        Image img;
        if ("file".equals(remoteFullPath.getProtocol())) {
            byte[] data = Files.readAllBytes(Paths.get(remoteFullPath.toURI()));
            ByteString imgBytes = ByteString.copyFrom(data);
            img = Image.newBuilder().setContent(imgBytes).build();
        } else {
            ImageSource imgSource = ImageSource.newBuilder().setImageUri(remoteFullPath.toString()).build();
            img = Image.newBuilder().setSource(imgSource).build();
        }

        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).setMaxResults(maxLabels).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return null;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    if (annotation.getScore() >= minConfidenceLevel / 100) {
                        Label label = new Label(annotation.getDescription(), annotation.getScore());
                        labels.add(label);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gson.toJson(labels);
    }
}
