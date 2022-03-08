package com.zest.zestexperimentorbackend.Controllers;

import com.zest.zestexperimentorbackend.Entities.Questions.BaseQuestion;
import com.zest.zestexperimentorbackend.Repositories.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zest.zestexperimentorbackend.Exceptions.QuestionNotFoundException;

import java.util.List;

@RestController
public class QuestionController {
    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/questions")
    List<BaseQuestion> allQuestions(){
        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    ResponseEntity addQuestion(@RequestBody List<BaseQuestion> questionList){
        questionRepository.saveAll(questionList);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping("/questions/{id}")
    BaseQuestion experiment(@PathVariable String id){
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

}
