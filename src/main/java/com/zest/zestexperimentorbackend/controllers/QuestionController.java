package com.zest.zestexperimentorbackend.controllers;


import com.zest.zestexperimentorbackend.persists.entities.questions.BaseQuestion;
import com.zest.zestexperimentorbackend.services.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping(value = "/questions",params = "alias")
    List<BaseQuestion> getQuestionByAlias(String alias){
        return questionService.getByAlias(alias);
    }

    @GetMapping(value = "/questions",params = "questiontype")
    List<BaseQuestion> getQuestionByQuestionType(BaseQuestion.QuestionType type){
        return questionService.getQuestionByType(type);
    }

    @GetMapping("/questions")
    List<BaseQuestion> allQuestions(){
        return questionService.getAll();
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    void addQuestion(@RequestBody List<BaseQuestion> questionList){
        questionService.save(questionList);
    }

    @GetMapping("/questions/{id}")
    BaseQuestion getQuestion(@PathVariable String id){
        return questionService.getById(id);
    }

    @DeleteMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteQuestion(@PathVariable String id){
        questionService.deleteById(id);
    }
}
