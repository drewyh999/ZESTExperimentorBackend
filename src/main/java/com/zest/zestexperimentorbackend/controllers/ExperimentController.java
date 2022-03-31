package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.ExperimentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ExperimentController {

    private final ExperimentService experimentService;

    public ExperimentController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @GetMapping("/experiment")
    List<BaseQuestion> runExperiment(HttpSession session, @RequestBody List<Answer> answerList){
        return experimentService.runExperiment(session, answerList);
    }
}
