package com.zest.zestexperimentorbackend.Exceptions;

public class ScheduleNotFoundException extends NotFoundExeption{
    public ScheduleNotFoundException(String id){
        super(("Schedule not found " + id));
    }
}
