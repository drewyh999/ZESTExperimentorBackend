package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.PilotService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PilotController {
    private final PilotService pilotService;

    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
    @PostMapping("/pilot")
    public List<BaseQuestion> runPilot(HttpSession session, @RequestBody ArrayList<Answer> answerList){
        return pilotService.runPilot(session, answerList);
    }
}
