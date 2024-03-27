package com.ymd.dataobject.service.storage;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.ymd.dataobject.exception.ObjectNotFoundException;
import io.github.cdimascio.dotenv.Dotenv;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleDataObjectImpl implements IDataObject {

    private Storage storage;
    private Dotenv dotenv;

    public GoogleDataObjectImpl() {
        dotenv = Dotenv.load();
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(dotenv.get("GOOGLE_APPLICATION_CREDENTIALS"))).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean doesExist(URI remoteFullPath) {
        boolean result = false;

        if (isBucket(remoteFullPath)) {
            Bucket bucket = storage.get(remoteFullPath.getHost());
            if (bucket != null) {
                result = bucket.exists();
            }
        } else {
            Page<Blob> blobs =
                    storage.list(
                            remoteFullPath.getHost(),
                            Storage.BlobListOption.prefix(remoteFullPath.getPath().substring(1)));
            int size = 0;
            for (Blob blob : blobs.iterateAll()) {
                size++;
            }
            return size > 0;
        }
        return result;
    }

    @Override
    public void upload(byte[] file, URI remoteFullPath) throws Exception {
        String bucketName = remoteFullPath.getHost();
        String objectName = remoteFullPath.getPath().substring(1);

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, new ByteArrayInputStream(file), Storage.BlobWriteOption.crc32cMatch());
    }

    @Override
    public void download(URI localFullPath, URI remoteFullPath) throws ObjectNotFoundException {
        String bucketName = remoteFullPath.getHost();
        String objectName = remoteFullPath.getPath().substring(1);

        try {
            Blob blob = storage.get(BlobId.of(bucketName, objectName));
            blob.downloadTo(Paths.get(localFullPath));
        } catch (Exception e) {
            throw new ObjectNotFoundException();
        }
    }

    @Override
    public URL publish(URI remoteFullPath, int expirationTime) throws ObjectNotFoundException {
        String bucketName = remoteFullPath.getHost();
        String objectName = remoteFullPath.getPath().substring(1);

        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob == null) {
            throw new ObjectNotFoundException();
        }

        return blob.signUrl(expirationTime, TimeUnit.SECONDS);
    }

    @Override
    public void remove(URI remoteFullPath, boolean isRecursive) {
        String bucketName = remoteFullPath.getHost();
        String objectName = remoteFullPath.getPath().substring(1);

        if (isRecursive) {
            Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(objectName));
            for (Blob blob : blobs.iterateAll()) {
                storage.delete(bucketName, blob.getName());
            }
        } else {
            Blob blob = storage.get(bucketName, objectName);
            if (blob == null) {
                return;
            }
            storage.delete(bucketName, objectName);
        }
    }

    private boolean isBucket(URI remoteFullPath) {
        return remoteFullPath.getPath().equals("/");
    }

}