package com.zest.zestexperimentorbackend.controllers;


import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.ExperimentService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ExperimentController {

    private final ExperimentService experimentService;

    public ExperimentController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @CrossOrigin(origins = "${server.allowedorigin}", allowedHeaders = "*", allowCredentials = "true")
    @GetMapping("/experiment/{invitation_id}")
    List<BaseQuestion> runExperiment(HttpSession session,
                                     @RequestBody List<Answer> answerList,
                                     @PathVariable String invitationId) {
        return experimentService.runExperiment(session, answerList, invitationId);
    }
}
