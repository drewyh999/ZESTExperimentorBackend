package com.zest.zestexperimentorbackend.Controllers;


import com.zest.zestexperimentorbackend.Entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.Repositories.QuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
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
    void addQuestion(@RequestBody List<BaseQuestion> questionList){
        questionList.forEach(q -> log.info(q.getQuestionMedia().toString()));
        questionRepository.saveAll(questionList);
    }

    @GetMapping("/questions/{id}")
    BaseQuestion experiment(@PathVariable String id){
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

    @DeleteMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    void deleteQuestion(@RequestBody List<String> questionIdList){
        questionIdList.forEach(questionRepository::deleteById);
    }
}
