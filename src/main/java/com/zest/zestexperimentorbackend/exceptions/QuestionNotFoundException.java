package com.zest.zestexperimentorbackend.exceptions;


//TODO Make the exceptions in hierarchy
public class QuestionNotFoundException extends NotFoundExeption{
    public QuestionNotFoundException(String id) {
        super("Question not found " + id);
    }
}
