package com.ymd.dataobject.exception;

public class ObjectNotFoundException extends DataObjectImplException {
    public ObjectNotFoundException() {
        super("Object not found.");
    }
}