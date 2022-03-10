package com.zest.zestexperimentorbackend.controllers;


import com.zest.zestexperimentorbackend.persists.entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.persists.repositories.QuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.zest.zestexperimentorbackend.exceptions.QuestionNotFoundException;

import java.util.List;
//TODO Abstract the service layer
@RestController
public class QuestionController {
    private final QuestionRepository questionRepository;

    private static final Log log = LogFactory.getLog(QuestionController.class);

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/questions")
    List<BaseQuestion> allQuestions(@RequestParam(value="alias", defaultValue = "") String alias){
        if(alias.equals("")){
            return questionRepository.findAll();
        }
        else{
            return questionRepository.findAllByAliasContains(alias);
        }
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    void addQuestion(@RequestBody List<BaseQuestion> questionList){
        questionList.forEach(q -> log.info(q.getQuestionMedia().toString()));
        questionRepository.saveAll(questionList);
    }

    @GetMapping("/questions/{id}")
    BaseQuestion getQuestion(@PathVariable String id){
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

    @DeleteMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    void deleteQuestion(@RequestBody List<String> questionIdList){
        questionIdList.forEach(questionRepository::deleteById);
    }
}
