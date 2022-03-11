package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.ExperimentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class ExperimentController {

    private final ExperimentService experimentService;

    public ExperimentController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @GetMapping("/experiment")
    BaseQuestion runExperiment(HttpSession session, String ans){
        return experimentService.runExperiment(session,ans);
    }
}
