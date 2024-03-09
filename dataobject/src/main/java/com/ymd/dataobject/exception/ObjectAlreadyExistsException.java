package com.ymd.dataobject.exception;

public class ObjectAlreadyExistsException extends DataObjectImplException {
    public ObjectAlreadyExistsException() {
        super("Object already exists.");
    }
}