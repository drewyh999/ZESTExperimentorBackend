package com.zest.zestexperimentorbackend.persists.entities;

import com.zest.zestexperimentorbackend.persists.entities.questionchoices.QuestionChoice;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "Testees")
@Data
public class Testee {
    @Id
    private String id;

    //the map of QuestionID to the answer text
    private Map<String, QuestionChoice> answerMap;

    private String testGroup;

    public Testee(Map<String, QuestionChoice> answerMap) {
        this.answerMap = answerMap;
    }
}

