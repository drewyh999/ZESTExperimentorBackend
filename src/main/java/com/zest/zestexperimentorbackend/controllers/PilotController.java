package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.PilotService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PilotController {
    private final PilotService pilotService;

    private static final Log log = LogFactory.getLog(PilotController.class);

    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping("/pilot")
    public List<BaseQuestion> runPilot(HttpSession session, @RequestBody ArrayList<Answer> answerList){
        answerList.forEach(e -> log.info(e.toString()));
        return pilotService.runPilot(session, answerList);
    }
}
