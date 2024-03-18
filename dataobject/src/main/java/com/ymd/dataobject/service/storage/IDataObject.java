package com.ymd.dataobject.service.storage;

import com.ymd.dataobject.exception.ObjectNotFoundException;

import java.net.URI;
import java.net.URL;

public interface IDataObject {

    public boolean doesExist(URI remoteFullPath);

    public void upload(byte[] file, URI remoteFullPath) throws Exception;

    public void download(URI localFullPath, URI remoteFullPath) throws ObjectNotFoundException;

    public URL publish(URI remoteFullPath, int expirationTime) throws ObjectNotFoundException;

    public void remove(URI remoteFullPath, boolean isRecursive);
}