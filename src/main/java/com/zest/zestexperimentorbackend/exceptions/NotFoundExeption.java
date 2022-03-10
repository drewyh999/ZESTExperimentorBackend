package com.zest.zestexperimentorbackend.exceptions;

public class NotFoundExeption extends RuntimeException {
    public NotFoundExeption(String id){
        super(id);
    }
}
