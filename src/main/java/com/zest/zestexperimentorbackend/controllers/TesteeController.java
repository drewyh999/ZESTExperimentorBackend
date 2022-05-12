package com.zest.zestexperimentorbackend.controllers;

import com.zest.zestexperimentorbackend.services.TesteeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteeController {
    private final TesteeService testeeService;

    public TesteeController(TesteeService testeeService) {
        this.testeeService = testeeService;
    }

    @GetMapping(value = "/testees/amount",params={"finished", "mode"})
    long getTesteeAmount(Boolean finished, String mode){
        return testeeService.getAmountByIsFinishingAndMode(finished,mode);
    }
}
