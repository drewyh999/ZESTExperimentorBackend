package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.services.CSVService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CsvController {
    private final CSVService csvService;

    private static final Log log = LogFactory.getLog(CsvController.class);

    public CsvController(CSVService csvService) {
        this.csvService = csvService;
    }

    @CrossOrigin(origins = "${server.allowedorigin}", allowedHeaders = "*", allowCredentials = "true")
    @GetMapping(value = "/csv", params = "mode")
    void pilotCSV(HttpServletResponse servletResponse, String mode) throws IOException, BaseNotFoundExeption {
        csvService.exportCSV(servletResponse, mode);
        log.info("Exported csv of " + mode);
    }
}
