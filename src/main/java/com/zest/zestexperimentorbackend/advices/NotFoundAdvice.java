package com.zest.zestexperimentorbackend.advices;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(BaseNotFoundExeption.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String NotFoundHandler(BaseNotFoundExeption ex) {
        return ex.getMessage();
    }
}
