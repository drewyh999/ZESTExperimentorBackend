package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.exceptions.ServiceException;
import com.zest.zestexperimentorbackend.persists.entities.answers.Answer;
import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.PilotService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@RestController
public class PilotController {
    private final PilotService pilotService;

    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @CrossOrigin(origins = "${server.allowedorigin}", allowedHeaders = "*", allowCredentials = "true")
    @PostMapping("/pilot/{invitation_id}")
    public List<BaseQuestion> runPilot(HttpSession session,
                                       @RequestBody ArrayList<Answer> answerList,
                                       @PathVariable String invitation_id
    ) throws ServiceException {
        return pilotService.runPilot(session, answerList, invitation_id);
    }
}
