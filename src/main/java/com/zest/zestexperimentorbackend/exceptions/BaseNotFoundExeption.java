package com.zest.zestexperimentorbackend.exceptions;

public class BaseNotFoundExeption extends RuntimeException{
    public BaseNotFoundExeption(String id,String classtring){
        super("Resource not found with id" + id + " with class of " + classtring);
    }
}
