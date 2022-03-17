package com.zest.zestexperimentorbackend.advices;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.exceptions.ExportException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExportAdvice {
    @ResponseBody
    @ExceptionHandler(ExportException.class)
    @ResponseStatus(HttpStatus.GONE)
    String NotFoundHandler(ExportException ex) {
        return ex.getMessage();
    }
}
