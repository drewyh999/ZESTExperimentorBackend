package com.zest.zestexperimentorbackend.Exceptions;


//TODO Make the exceptions in hierarchy
public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException(String id) {
        super("Question not found " + id);
    }
}
