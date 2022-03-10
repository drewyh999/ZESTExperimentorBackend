package com.zest.zestexperimentorbackend.exceptions;

public class ScheduleNotFoundException extends NotFoundExeption{
    public ScheduleNotFoundException(String id){
        super(("Schedule not found " + id));
    }
}
