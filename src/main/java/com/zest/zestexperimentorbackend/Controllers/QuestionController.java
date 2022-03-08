package com.zest.zestexperimentorbackend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.zestexperimentorbackend.Entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.Entities.Questions.DemographicQuestion;
import com.zest.zestexperimentorbackend.Repositories.QuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zest.zestexperimentorbackend.Exceptions.QuestionNotFoundException;

import java.util.List;

@RestController
public class QuestionController {
    private final QuestionRepository questionRepository;

    private static final Log log = LogFactory.getLog(QuestionController.class);

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/questions")
    List<BaseQuestion> allQuestions(){
        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    String addQuestion(@RequestBody List<BaseQuestion> questionList){
        questionList.forEach(q -> log.info(q.toString()));
        return "OK";
    }
    @GetMapping("/questions/{id}")
    BaseQuestion experiment(@PathVariable String id){
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

}
