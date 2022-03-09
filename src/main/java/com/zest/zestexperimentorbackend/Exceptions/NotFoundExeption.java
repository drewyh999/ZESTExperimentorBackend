package com.zest.zestexperimentorbackend.Exceptions;

public class NotFoundExeption extends RuntimeException {
    public NotFoundExeption(String id){
        super(id);
    }
}
