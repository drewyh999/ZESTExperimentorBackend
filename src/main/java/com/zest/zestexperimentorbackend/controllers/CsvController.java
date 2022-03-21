package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.services.CSVService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CsvController {
    private final CSVService csvService;

    public CsvController(CSVService csvService) {
        this.csvService = csvService;
    }

    @GetMapping(value = "/csv",params = "mode")
    void pilotCSV(HttpServletResponse servletResponse,String mode) throws IOException, BaseNotFoundExeption {
        csvService.exportCSV(servletResponse,mode);
    }
}
