package com.zest.zestexperimentorbackend.controllers;


import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.QuestionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO Abstract the service layer
@RestController
public class QuestionController {

    private static final Log log = LogFactory.getLog(QuestionController.class);

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    List<BaseQuestion> allQuestions(@RequestParam(value="alias", defaultValue = "") String alias){
        return questionService.getByAlias(alias);
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    void addQuestion(@RequestBody List<BaseQuestion> questionList){
        questionService.save(questionList);
    }

    @GetMapping("/questions/{id}")
    BaseQuestion getQuestion(@PathVariable String id){
        return questionService.findById(id);
    }

    @DeleteMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteQuestion(@PathVariable String id){
        questionService.deleteById(id);
    }
}
