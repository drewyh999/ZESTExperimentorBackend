package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExperimentController {

    private QuestionService questionService;

}
