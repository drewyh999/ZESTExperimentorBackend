package com.zest.zestexperimentorbackend.Exceptions;


//TODO Make the exceptions in hierarchy
public class QuestionNotFoundException extends NotFoundExeption{
    public QuestionNotFoundException(String id) {
        super("Question not found " + id);
    }
}
