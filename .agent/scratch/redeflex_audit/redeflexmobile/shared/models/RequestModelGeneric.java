package com.axys.redeflexmobile.shared.models;

import java.util.List;

public class RequestModelGeneric<T> {

    public List<T> models;
    public T model;
    public Exception exception;
}
