package com.zest.zestexperimentorbackend.persists.entities;

import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "Testees")
@Data
public class Testee {
    @Id
    private String id;

    private Map<String, String> answerMap;

    private String testGroup;

    public Testee(String testGroup) {
        this.testGroup = testGroup;
        this.answerMap = new HashMap<>();
    }
}

