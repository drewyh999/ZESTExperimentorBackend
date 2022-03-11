package com.zest.zestexperimentorbackend.exceptions;

public class BaseNotFoundExeption extends RuntimeException{
    public BaseNotFoundExeption(String id,String classstring){
        super("Resource not found with id" + id);
    }
}
