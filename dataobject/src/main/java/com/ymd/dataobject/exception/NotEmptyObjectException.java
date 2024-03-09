package com.ymd.dataobject.exception;

public class NotEmptyObjectException extends DataObjectImplException {

    public NotEmptyObjectException() {
        super("Oject is not empty.");
    }
}