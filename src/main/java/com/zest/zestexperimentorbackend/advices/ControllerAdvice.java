package com.zest.zestexperimentorbackend.advices;

import com.zest.zestexperimentorbackend.exceptions.ExportException;
import com.zest.zestexperimentorbackend.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ResponseBody
    @ExceptionHandler(ExportException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    String ExportExceptionHandler(ExportException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    String ServiceExceptionHandler(ServiceException ex){ return ex.getMessage(); }
}
